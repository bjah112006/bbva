package com.ibm.bbva.thread;

import org.quartz.JobExecutionException;

import com.ibm.bbva.jobs.LDAPCarga;

public class ThreadCargaLdap extends Thread {

	
	public void run(){  
		LDAPCarga carga = new LDAPCarga();
		try {
			carga.execute();
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
}
