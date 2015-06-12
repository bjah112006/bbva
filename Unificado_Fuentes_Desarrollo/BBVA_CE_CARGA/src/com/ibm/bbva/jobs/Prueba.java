package com.ibm.bbva.jobs;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class Prueba implements Job {

	//private static final Logger LOG = LoggerFactory.getLogger("Prueba");
	
	/*
	public static void main(String[] args) {
		// TODO Apéndice de método generado automáticamente

		System.out.println("**********PRUEBA DE JOB**************");
		
		Date fecha = new Date();		
		System.out.println("fecha:"+fecha.getDate()+ " hora:"+fecha.getHours()+" : "+fecha.getMinutes()+" : "+fecha.getSeconds());
		
		ActualizarFeriados actualizarFeriados = new ActualizarFeriados();		
		//actualizarFeriados.load();
		
		actualizarFeriados.cargarVariables();
	}
	*/

	public void load(){
		Integer frecuenciaDias = 1;
		
		try {
		//LOG.info(" Iniciando scheduler ...");
    	
	    SchedulerFactory sf = new StdSchedulerFactory();
	    Scheduler sched = sf.getScheduler();
	    
				
		JobDetail job = JobBuilder.newJob(ActualizarFeriados.class).withIdentity("JOB_PRUEBA", "GRUPO_PRUEBA").build();
		
		Date startDate = null;
		
	    //LOG.info(" La primera ejecución será el : " + startDate.toString());

	    SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder
	    		.newTrigger()
	    		.withIdentity("TRIGGER_JOB_PRUEBA", "GRUPO_JOBS_PRUEBA")
	    		.forJob(job)
	    		.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24 * frecuenciaDias).repeatForever())
	    		.startAt(startDate)
	    		.build();
	    
	    sched.scheduleJob(job, trigger);
	    sched.start();
	    
	    //LOG.info("Job agendado satisfactoriamente ...");
	    
		}catch(Exception e){
			//LOG.error("Error cuando se agendaba la ejecución del job : ", e);
		}
	}
	
	public void logic(){
		
		//LOG.info("Job LOGICA");
		System.out.println("Job LOGICA");
		System.out.println("Llego a ejecutar");
	}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logic();
			System.out.println(" execute LOGICA");
		}catch(Exception e){
			//LOG.error("Error en el job de PRUEBA : ", e);
			System.out.println(" error del LOGICA");
		}
	}

}
