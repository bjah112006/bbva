package com.ibm.bbva.session.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.ws.sdo.mediator.jdbc.queryengine.CTESelectStatementCreator;

/**
 * Session Bean implementation class TipoCambioCEBean
 */
@Stateless
@Local(TipoCambioCEBeanLocal.class)
public class TipoCambioCEBean extends AbstractFacade<TipoCambioCE> implements TipoCambioCEBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")	
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public TipoCambioCEBean() {
    	super(TipoCambioCE.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
        
    @Override
	public TipoCambioCE create(TipoCambioCE entity) {
		return super.create(entity);
	}
    
    @Override
	public List<TipoCambioCE> buscarTodos() {
		List<TipoCambioCE> resultList;
		try {			
			resultList = em.createNamedQuery("TipoCambioCE.findAll").getResultList();
			return resultList;
		} catch (Exception e) {			
			return null;
		}
	}    
    
    @Override
	public TipoCambioCE buscarPorFecha(Date fecha) {    
    	
		try {			
			Query query = em.createNamedQuery("TipoCambioCE.findByFecha",TipoCambioCE.class);
			query.setParameter("inFecha", fecha, TemporalType.DATE);
			return (TipoCambioCE) query.getSingleResult();
		} catch (Exception e) {			
			return null;
		}
		
	}

	@Override
	public TipoCambioCE buscarTop() {
		
		try {			
			Query query = em.createNamedQuery("TipoCambioCE.findAll",TipoCambioCE.class);
			query.setFirstResult(0);
			query.setMaxResults(1);
			return (TipoCambioCE) query.getSingleResult();
		} catch (Exception e) {	
			return null;
		}
		
	}
    
}
