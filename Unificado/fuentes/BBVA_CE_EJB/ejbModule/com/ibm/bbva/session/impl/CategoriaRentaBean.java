package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;

/**
 * Session Bean implementation class CategoriaRentaBean
 */
@Stateless
@Local(CategoriaRentaBeanLocal.class)
public class CategoriaRentaBean extends AbstractFacade<CategoriaRenta> implements CategoriaRentaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public CategoriaRentaBean() {
        super(CategoriaRenta.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<CategoriaRenta> buscarTodos() {
		List<CategoriaRenta> resultList = em.createNamedQuery("CategoriaRenta.findAll").getResultList();
		return resultList;
	}

	@Override
	public CategoriaRenta buscarPorId(long id) {
		return (CategoriaRenta) em.createNamedQuery("CategoriaRenta.findById").setParameter("id", id).getSingleResult();
	}

}
