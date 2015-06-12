package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.HistorialLog;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.HistorialLogBeanLocal;

/**
 * Session Bean implementation class HistorialLogBean
 */
@Stateless
public class HistorialLogBean extends AbstractFacade<HistorialLog> implements HistorialLogBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public HistorialLogBean() {
        super(HistorialLog.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<HistorialLog> buscarPorIdExpediente(long idExpediente) {
		List<HistorialLog> resultList = em.createNamedQuery("HistorialLog.findbyIdExpediente").setParameter("idExpediente", idExpediente).getResultList();
		return resultList;
	}
	
	
	@Override
	public HistorialLog create(HistorialLog entity) {
		return super.create(entity);
	}
	
	
}