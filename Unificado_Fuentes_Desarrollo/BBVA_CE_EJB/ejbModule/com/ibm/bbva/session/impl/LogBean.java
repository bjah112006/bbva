package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Log;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.LogBeanLocal;

/**
 * Session Bean implementation class LogBean
 */
@Stateless
@Local(LogBeanLocal.class)
public class LogBean extends AbstractFacade<Log> implements LogBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public LogBean() {
        super(Log.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Log> buscarPorIdExpediente(long idExpediente) {
		List<Log> resultList = em.createNamedQuery("Log.findByIdExpediente").setParameter("idExpediente", idExpediente).getResultList();
		return resultList;
	}
    
}
