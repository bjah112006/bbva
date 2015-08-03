package com.ibm.bbva.session;

import javax.ejb.Local;

import com.ibm.bbva.entities.LogJob;

@Local
public interface LogJobBeanLocal 
{

	public void edit(LogJob logJob);
	
	public LogJob create(LogJob logJob);
	
}
