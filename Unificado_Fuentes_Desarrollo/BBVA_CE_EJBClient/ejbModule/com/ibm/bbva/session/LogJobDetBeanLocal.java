package com.ibm.bbva.session;

import javax.ejb.Local;

import com.ibm.bbva.entities.LogJobDet;

@Local
public interface LogJobDetBeanLocal 
{

	public void edit(LogJobDet logJobDet);
	
	public LogJobDet create(LogJobDet logJobDet);
	
}
