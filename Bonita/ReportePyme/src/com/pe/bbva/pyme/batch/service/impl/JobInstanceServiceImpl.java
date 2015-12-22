package com.pe.bbva.pyme.batch.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import com.pe.bbva.pyme.batch.service.JobInstanceService;
import com.pe.bbva.pyme.listener.WFFastPymeServletContextListener;
import com.pe.bbva.pyme.quartz.dao.TriggerDAO;
import com.pe.bbva.pyme.quartz.domain.Trigger;

/*
 * (non-Javadoc)
 * 
 * @see
 * com.bbva.batch.service.JobInstanceService
 */
@Service("jobInstanceService")
public class JobInstanceServiceImpl implements Serializable, JobInstanceService {

    private static final Logger LOG = Logger.getLogger(JobInstanceServiceImpl.class);
    private static final long serialVersionUID = 1L;

    @Resource(name = "jobLauncher")
    private JobLauncher jobLauncher;
    
    @Resource(name = "triggerDAO")
    private TriggerDAO triggerDAO;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.batch.service.JobInstanceService#execute(String)
     */
    @Override
    public void execute(String jobName) {
        execute(jobName, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.batch.service.JobInstanceService#execute(String, Date)
     */
    @Override
    public void execute(String jobName, Date date) {
        Job job = WFFastPymeServletContextListener.getBean(jobName);
        
        try {
            JobParameters param = new JobParametersBuilder()
                .addDate("fechaEjecucion", new Date())
                .addDate("fechaProceso", date == null ? new Date() : date)
                .toJobParameters();
            JobExecution execution;
            execution = jobLauncher.run(job, param);
            Long outId = execution.getId();
            LOG.error("Id Proceso: [" + outId + "]");
            LOG.error("Estado del Proceso: " + execution.getStatus());
            if (!execution.getAllFailureExceptions().isEmpty()) {
                LOG.error("Estado del Proceso: " + execution.getAllFailureExceptions());
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    @Override
    public List<Trigger> listTrigger() {
        return triggerDAO.listar();
    }

    @Override
    public long obtainLastExecution(String jobName) {
        return triggerDAO.obtainLastExecution(jobName);
    }
    
    
}
