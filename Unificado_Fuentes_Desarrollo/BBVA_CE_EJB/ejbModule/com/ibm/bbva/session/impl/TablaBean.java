package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Tabla;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TablaBeanLocal;

/**
 * Session Bean implementation class TablaBean
 */
@Stateless
@Local(TablaBeanLocal.class)
public class TablaBean extends AbstractFacade<Tabla> implements TablaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TablaBean() {
    	super(Tabla.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Tabla> buscarTodos() {
		List<Tabla> resultList = em.createNamedQuery("Tabla.findAll").getResultList();
		return resultList;
	}

    @Override
	public Tabla buscarPorId(long id) {
		return (Tabla) em.createNamedQuery("Tabla.findById").setParameter("id", id).getSingleResult();
	}
}
