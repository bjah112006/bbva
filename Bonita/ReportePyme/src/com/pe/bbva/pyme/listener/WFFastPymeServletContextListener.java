package com.pe.bbva.pyme.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.pe.bbva.pyme.jobs.GenerarReporteJob;
import com.pe.bbva.pyme.utils.ConstantesEnum;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WFFastPymeServletContextListener extends QuartzInitializerListener {
	
	    private static final Logger LOG = LoggerFactory.getLogger(WFFastPymeServletContextListener.class);
	    
	    @Override
	    public void contextInitialized(ServletContextEvent sce) {
	        super.contextInitialized(sce);
	        ServletContext ctx = sce.getServletContext();
	        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
	        try {
	            Scheduler scheduler = factory.getScheduler();
	            JobDetail job = JobBuilder.newJob(GenerarReporteJob.class).build();
	            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(ConstantesEnum.TRIGGER_IDENTITY.getNombre()).withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).startNow().build();
	            scheduler.scheduleJob(job, trigger);
	            scheduler.start();
	        } catch (Exception e) {
	            LOG.error("Ocurri\u00f3 un error al calendarizar el trabajo", e);
	        }
	    }
	    @Override
	    public void contextDestroyed(ServletContextEvent sce) {
	        super.contextDestroyed(sce);
	    }
	}
