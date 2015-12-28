package com.pe.bbva.pyme.quartz.factory.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import com.pe.bbva.pyme.jobs.GenerarReporteJob;
import com.pe.bbva.pyme.quartz.ExecuteJob;
import com.pe.bbva.pyme.quartz.domain.JobConfig;
import com.pe.bbva.pyme.quartz.factory.QuartzFactory;
import com.pe.bbva.pyme.utils.DBUtil;

/**
 * F\u00E1brica de trabajos
 * 
 * @author jquedena
 *
 */
@Component("quartzFactory")
public class QuartzFactoryImpl implements QuartzFactory {

    private static final Logger LOG = Logger.getLogger(QuartzFactoryImpl.class);

    @Resource(name = "quartzScheduler")
    private Scheduler scheduler;

    @Resource(name = "dataSource")
    private DriverManagerDataSource dataSource;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.quartz.factory.QuartzFactory#createJob(com.bbva.batch.domain.JobConfig)
     */
    @Override
    public void createJob(JobConfig jobConfig, Class<? extends Job> clazz) throws Exception {
        // ss mm HH dd MM wd *
        String name = jobConfig.getJobName() + "Cron";
        LOG.error("cronSchedule: [" + jobConfig.getCronExpresion() + "]");

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", jobConfig.getJobName());
        jobDataMap.put("nombreAplicacion", jobConfig.getJobName());
        JobKey jobKey = new JobKey(jobConfig.getJobName(), jobConfig.getGroupName());
        JobDetail jobDetail = JobBuilder
            .newJob(clazz)
            .withIdentity(jobKey)
            .setJobData(jobDataMap)
            .build();

        TriggerKey triggerKey = TriggerKey.triggerKey(name, jobConfig.getGroupName());
        Trigger cronTrigger = TriggerBuilder
            .newTrigger()
            .withIdentity(triggerKey)
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule(jobConfig.getCronExpresion()))
            .build();

        scheduler.deleteJob(jobKey);
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    /**
     * Al crearse la instancia, se crear los trabajos de manera din\u00E1mica
     * @throws DataAccessException 
     */
    @PostConstruct
    public void init() {
        try {
            scheduler.clear();
            JobConfig jobConfig;
            String cron = "";
            try {
                cron = DBUtil.obtenerParametroDetalle(dataSource.getConnection(), "18", "002");
            } catch(Exception e) {
                cron = "0 0 23 1 * ? *";
                LOG.error("(1) Usando expresion por defecto.");
                LOG.debug("", e);
            }
            
            if(cron != null && cron.isEmpty()) {
                cron = "0 0 23 1 * ? *";
                LOG.error("(2) Usando expresion por defecto.");
            }
            
            jobConfig = new JobConfig();
            jobConfig.setJobName("migrarDocumentosJob");
            jobConfig.setGroupName("FastPyme");
            jobConfig.setCronExpresion(cron);
            createJob(jobConfig, ExecuteJob.class);
            
            jobConfig = new JobConfig();
            jobConfig.setJobName("generarReporteJob");
            jobConfig.setGroupName("FastPyme");
            jobConfig.setCronExpresion("0 0/1 * 1/1 * ? *");
            createJob(jobConfig, GenerarReporteJob.class);
        } catch(Exception e) {
            LOG.error("Error al crear los Jobs", e);
        }
    }

    @Override
    public void rescheduler(JobConfig jobConfig, Class<? extends Job> clazz) throws Exception {
        createJob(jobConfig, clazz);
    }
    
    @Override
    public void deleteJob(JobConfig jobConfig) throws Exception {
        JobKey jobKey = new JobKey(jobConfig.getJobName(), jobConfig.getGroupName());
        if(scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
    }
}
