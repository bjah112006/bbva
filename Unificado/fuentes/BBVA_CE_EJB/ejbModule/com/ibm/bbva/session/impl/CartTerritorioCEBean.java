package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CartTerritorioCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.CartTerritorioCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(CartTerritorioCEBeanLocal.class)
public class CartTerritorioCEBean extends AbstractFacade<CartTerritorioCE> implements CartTerritorioCEBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public CartTerritorioCEBean() {
        super(CartTerritorioCE.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	

	@Override
	public List<CartTerritorioCE> buscarPorIdTerr(long idTerritorio) {
		List<CartTerritorioCE> resultList = em.createNamedQuery("CartTerritorioCE.findIdTerr")
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("flagActivo", "1")				
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<CartTerritorioCE> buscarPorIdProdIdTerr(long idProducto, long idTerritorio) {
		List<CartTerritorioCE> resultList = em.createNamedQuery("CartTerritorioCE.findIdProdIdTerr")
				.setParameter("idProducto", idProducto)
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("flagActivo", "1")				
				.getResultList();
		return resultList;
	}
}
