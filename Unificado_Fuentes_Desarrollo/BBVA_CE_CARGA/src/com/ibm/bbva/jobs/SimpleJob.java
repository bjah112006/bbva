package com.ibm.bbva.jobs;

import java.util.Locale;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJob implements Job {

        public void execute(JobExecutionContext ctx) throws JobExecutionException {
                System.out.printf(new Locale("es", "PE"), "%tc Ejecutando tarea...%n", new java.util.Date());
        }
}

