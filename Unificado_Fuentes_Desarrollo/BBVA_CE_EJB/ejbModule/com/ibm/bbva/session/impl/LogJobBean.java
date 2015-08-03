package com.ibm.bbva.session.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.LogJob;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.LogJobBeanLocal;

@Stateless
public class LogJobBean extends AbstractFacade<LogJob> implements LogJobBeanLocal
{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public LogJobBean() {
    	super(LogJob.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public LogJob create(LogJob entity){
		return super.create(entity);
	}

	@Override
	public void edit(LogJob entity){
		super.edit(entity);
	}
	
}
