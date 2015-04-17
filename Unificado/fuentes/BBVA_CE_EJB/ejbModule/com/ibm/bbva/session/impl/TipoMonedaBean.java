package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoMonedaBeanLocal;

/**
 * Session Bean implementation class TipoMonedaBean
 */
@Stateless
@Local(TipoMonedaBeanLocal.class)
public class TipoMonedaBean extends AbstractFacade<TipoMoneda> implements TipoMonedaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoMonedaBean() {
        super(TipoMoneda.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoMoneda> buscarTodos() {
		List<TipoMoneda> resultList = em.createNamedQuery("TipoMoneda.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public TipoMoneda buscarPorId(long id) {
		return (TipoMoneda) em.createNamedQuery("TipoMoneda.findById").setParameter("id", id).getSingleResult();
	}

}
