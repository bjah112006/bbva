package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.Garantia;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.GarantiaBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class GarantiaBean
 */
@Stateless
@Local(GarantiaBeanLocal.class)
public class GarantiaBean extends AbstractFacade<Garantia> implements GarantiaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public GarantiaBean() {
    	super(Garantia.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Garantia> buscarTodos() {
		List<Garantia> resultList = em.createNamedQuery("Garantia.findAll").getResultList();
		return resultList;
	}
	
    @Override
	public Garantia buscarPorId(long id) {
		return (Garantia) em.createNamedQuery("Garantia.findById").setParameter("id", id).getSingleResult();
	}
    
	@Override
	public List<Garantia> buscarPorIdProducto(long idProducto) {
		List<Garantia> resultList = em.createNamedQuery("Garantia.buscarPorIdProducto").setParameter("idProducto", idProducto).getResultList();
		return resultList;
	}
	
	@Override
	public List<Garantia> buscarPorFlagSinGarantia(long idProducto, String flagSinGarantia) {
		List<Garantia> resultList = em.createNamedQuery("Garantia.buscarSinGarantia")
				.setParameter("idProducto", idProducto)
				.setParameter("flagSinGarantia",flagSinGarantia)
				.getResultList();
		return resultList;
	}	
}
