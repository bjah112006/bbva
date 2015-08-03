package com.ibm.bbva.session.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.LogJobDet;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.LogJobDetBeanLocal;

@Stateless
public class LogJobDetBean extends AbstractFacade<LogJobDet> implements LogJobDetBeanLocal
{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public LogJobDetBean() {
    	super(LogJobDet.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public LogJobDet create(LogJobDet entity){
		return super.create(entity);
	}

	@Override
	public void edit(LogJobDet entity){
		super.edit(entity);
	}
	
}
