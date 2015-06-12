package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.ProductoEtiqueta;
import com.ibm.bbva.session.ProductoEtiquetaBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.session.AbstractFacade;


/**
 * Session Bean implementation class ProductoEtiquetaBean
 */
@Stateless
@Local(ProductoEtiquetaBeanLocal.class)
public class ProductoEtiquetaBean extends AbstractFacade<ProductoEtiqueta> implements ProductoEtiquetaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ProductoEtiquetaBean() {
        super(ProductoEtiqueta.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<ProductoEtiqueta> buscarTodos() {
		List<ProductoEtiqueta> resultList = em.createNamedQuery("ProductoEtiqueta.findAll").getResultList();
		return resultList;
	}
	
    @Override
	public ProductoEtiqueta buscarPorId(long id) {
		return (ProductoEtiqueta) em.createNamedQuery("ProductoEtiqueta.findById").setParameter("id", id).getSingleResult();
	}
    
	@Override
	public List<ProductoEtiqueta> buscarPorIdProducto(long idProducto) {
		List<ProductoEtiqueta> resultList = em.createNamedQuery("ProductoEtiqueta.buscarPorIdProducto").setParameter("idProducto", idProducto).getResultList();
		return resultList;
	}	
}
