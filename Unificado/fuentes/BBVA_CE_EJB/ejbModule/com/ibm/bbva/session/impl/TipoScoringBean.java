package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoScoringBeanLocal;

/**
 * Session Bean implementation class TipoScoringBean
 */
@Stateless
@Local(TipoScoringBeanLocal.class)
public class TipoScoringBean extends AbstractFacade<TipoScoring> implements TipoScoringBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public TipoScoringBean() {
    	super(TipoScoring.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<TipoScoring> buscarTodos() {
		List<TipoScoring> resultList = em.createNamedQuery("TipoScoring.findAll").getResultList();
		return resultList;
	}

	@Override
	public TipoScoring buscarPorId(long id) {
		return (TipoScoring) em.createNamedQuery("TipoScoring.findById").setParameter("id", id).getSingleResult();
	}

}
