package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoDoiBeanLocal;

/**
 * Session Bean implementation class TipoDoiBean
 */
@Stateless
@Local(TipoDoiBeanLocal.class)
public class TipoDoiBean extends AbstractFacade<TipoDoi> implements TipoDoiBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoDoiBean() {
    	super(TipoDoi.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoDoi> buscarTodos() {
		List<TipoDoi> resultList = em.createNamedQuery("TipoDoi.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public TipoDoi buscarPorId(long id) {
		return (TipoDoi) em.createNamedQuery("TipoDoi.findById").setParameter("id", id).getSingleResult();
	}

}
