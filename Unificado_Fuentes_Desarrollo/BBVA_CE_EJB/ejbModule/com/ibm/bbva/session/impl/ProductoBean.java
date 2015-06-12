package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ProductoBeanLocal;

/**
 * Session Bean implementation class ProductoBean
 */
@Stateless
@Local(ProductoBeanLocal.class)
public class ProductoBean extends AbstractFacade<Producto> implements ProductoBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ProductoBean() {
        super(Producto.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Producto> buscarTodos() {
		try{
			List<Producto> resultList = em.createNamedQuery("Producto.findAll").getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}

	@Override
	public Producto buscarPorId(long id) {
		try{
			return (Producto) em.createNamedQuery("Producto.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

}
