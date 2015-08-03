package com.ibm.bbva.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.job.JobCargaLDAP;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

@WebListener
public class QuartzListener extends QuartzInitializerListener
{

	private static final Logger LOG = LoggerFactory.getLogger(QuartzListener.class);
	
	@EJB
	private ParametrosConfBeanLocal parametrosConfBeanLocal;
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
                                  
        try 
        {
        	
        	ServletContext ctx = sce.getServletContext();
            StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        	Scheduler scheduler = factory.getScheduler();
        	
        	String CRON_SCHEDULE = parametrosConfBeanLocal.buscarPorVariable(Constantes.CODIGO_APLICATIVO_PROCESO_LDAP, Constantes.CRON_SCHEDULE_PROCESO_CARGA_LDAP).getValorVariable();
        	
        	JobKey jobKey = new JobKey(Constantes.JOB_NAME, Constantes.GROUP_NAME);
        	JobDetail jobDetail = JobBuilder
                    .newJob(JobCargaLDAP.class)
                    .withIdentity(jobKey)
                    .build();
        	
        	TriggerKey triggerKey = TriggerKey.triggerKey(Constantes.TRIGGER_NAME, Constantes.GROUP_NAME);
            Trigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(CRON_SCHEDULE))
                    .build();
                                   
            if(scheduler.checkExists(jobKey))
            {
            	scheduler.deleteJob(jobKey);
            }            
            scheduler.scheduleJob(jobDetail, cronTrigger);
                                   
        } 
        catch (Exception e) 
        {
            LOG.error("Ocurri\u00f3 un error al calendarizar el trabajo", e);
        }
        
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        super.contextDestroyed(sce);
    }
    
}
