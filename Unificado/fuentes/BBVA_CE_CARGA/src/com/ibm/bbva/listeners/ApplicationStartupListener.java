package com.ibm.bbva.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.ibm.bbva.jobs.ActualizarFeriados;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.ibm.bbva.jobs.Prueba;

public class ApplicationStartupListener implements ServletContextListener{

	//private static final Logger LOG = LoggerFactory.getLogger("FeriadosCarga");

	public SchedulerFactory sf = new StdSchedulerFactory();
    public Scheduler sched = null; 	
    
    public ApplicationStartupListener(){    	
    	System.out.println("JOB CONSTRUIDO ...");
    }
    
    @Override
	public void contextDestroyed(ServletContextEvent sce) {
				
		//LOG.info("Cierre de aplicaci�n ...");
		//LOG.info("Cancelando agendaci�n de job ...");
		
		System.out.println("Cierre de aplicaci�n ...");
		System.out.println("Cancelando agendaci�n de job ...");
		
		if(this.sched != null){
			try {
				// Unschedule the job with the trigger
				this.sched.deleteJob(JobKey.jobKey("JOB_ACTUALIZAR_FERIADOS", "GRUPO_JOBS_CARGA"));
				//this.sched.deleteJob(JobKey.jobKey("JOB_PRUEBA", "GRUPO_PRUEBA"));
				this.sched.shutdown();
				//LOG.info("Agendaci�n de job cancelado satisfactoriamente ...");
			} catch (SchedulerException e) {
				//LOG.error("Ocurri� un error cuando se cancelaba la agendaci�n del job", e);
			}
		}
		
	}
    

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println("*************************JOB*****************************************");
		System.out.println("Inicio de aplicaci�n ...");
		System.out.println("Iniciando agendaci�n de job ...");
		
		/**
		 * Cargar JOBS,
		 * estos ser�n agendados para su ejecuci�n.
		 */
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			this.sched = sf.getScheduler();
			ActualizarFeriados actualizarFeriados = new ActualizarFeriados();		
			actualizarFeriados.load(sched);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}
    
    
    
}
