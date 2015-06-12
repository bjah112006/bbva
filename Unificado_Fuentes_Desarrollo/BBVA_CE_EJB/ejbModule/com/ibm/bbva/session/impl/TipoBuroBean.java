package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TipoBuro;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoBuroBeanLocal;

/**
 * Session Bean implementation class TipoBuroBean
 */
@Stateless
@Local(TipoBuroBeanLocal.class)
public class TipoBuroBean extends AbstractFacade<TipoBuro> implements TipoBuroBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public TipoBuroBean() {
    	super(TipoBuro.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<TipoBuro> buscarTodos() {
		List<TipoBuro> resultList = em.createNamedQuery("TipoBuro.findAll").getResultList();
		return resultList;
	}
}
