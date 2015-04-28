package com.ibm.bbva.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.jobs.LDAPCarga;

public class ApplicationStartupListener implements ServletContextListener {
	
	private static final Logger LOG = LoggerFactory.getLogger("LDAPCarga");

	public SchedulerFactory sf = new StdSchedulerFactory();
    public Scheduler sched = null; 	
	 
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
				
		LOG.info("Cierre de aplicación ...");
		LOG.info("Cancelando agendación de job ...");
		
		if (this.sched != null) {
			try {
				// Unschedule the job with the trigger
				this.sched.deleteJob(JobKey.jobKey("JOB_CARGA_LDAP", "GRUPO_JOBS_LDAP"));
				this.sched.shutdown();
				LOG.info("Agendación de job cancelado satisfactoriamente ...");
			} catch (SchedulerException e) {
				LOG.error("Ocurrió un error cuando se cancelaba la agendación del job", e);
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		LOG.info("Inicio de aplicación ...");
		
		LOG.info("Iniciando agendación de job ...");
		
		/**
		 * Cargar JOBS,
		 * estos serán agendados para su ejecución.
		 */
	    try {
	    	LOG.info(" Iniciando scheduler ...");
	    	
	    	SchedulerFactory sf = new StdSchedulerFactory();
			this.sched = sf.getScheduler();
			
			LOG.info(" Scheduler obtenido ...");
			
			LDAPCarga ldapCarga = new LDAPCarga();
			ldapCarga.load(sched);
		} catch (SchedulerException e) {
			LOG.error(e.getMessage(), e);
		}
			
	}

}