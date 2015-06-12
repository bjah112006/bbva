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

import com.ibm.bbva.jobs.ActualizarFeriados;

public class ApplicationStartupListener implements ServletContextListener{

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartupListener.class);

	public SchedulerFactory sf = new StdSchedulerFactory();
    public Scheduler sched = null; 	
    
    public ApplicationStartupListener(){    	
    	LOG.info("JOB CONSTRUIDO ...");
    }
    
    @Override
	public void contextDestroyed(ServletContextEvent sce) {
				
		LOG.info("Cierre de aplicaci�n ...");
		LOG.info("Cancelando agendaci�n de job ...");
		
		if(this.sched != null){
			try {
				// Unschedule the job with the trigger
				this.sched.deleteJob(JobKey.jobKey("JOB_ACTUALIZAR_FERIADOS", "GRUPO_JOBS_CARGA"));
				//this.sched.deleteJob(JobKey.jobKey("JOB_PRUEBA", "GRUPO_PRUEBA"));
				this.sched.shutdown();
				//LOG.info("Agendaci�n de job cancelado satisfactoriamente ...");
			} catch (SchedulerException e) {
				LOG.error("Ocurri� un error cuando se cancelaba la agendaci�n del job", e);
			}
		}
		
	}
    

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		LOG.info("*************************JOB*****************************************");
		LOG.info("Inicio de aplicaci�n ...");
		LOG.info("Iniciando agendaci�n de job ...");
		
		/**
		 * Cargar JOBS,
		 * estos ser�n agendados para su ejecuci�n.
		 */
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			this.sched = sf.getScheduler();
			
			// desprogramo el job por si se qued� de la vez anterior
			this.sched.deleteJob(JobKey.jobKey("JOB_ACTUALIZAR_FERIADOS", "GRUPO_JOBS_CARGA"));
			
			ActualizarFeriados actualizarFeriados = new ActualizarFeriados();		
			actualizarFeriados.load(sched);
		} catch (SchedulerException e) {
			LOG.error("Ocurri� un error cuando se iniciaba la agendaci�n del job", e);
		}

	}
    
    
    
}
