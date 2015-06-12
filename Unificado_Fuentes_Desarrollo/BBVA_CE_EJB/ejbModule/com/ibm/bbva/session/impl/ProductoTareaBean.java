package com.ibm.bbva.session.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.ProductoTarea;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ProductoTareaBeanLocal;


/**
 * Session Bean implementation class ProductoEtiquetaBean
 */
@Stateless
@Local(ProductoTareaBeanLocal.class)
public class ProductoTareaBean extends AbstractFacade<ProductoTarea> implements ProductoTareaBeanLocal {
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ProductoTareaBean() {
    	super(ProductoTarea.class);
    }

	@Override                                             
	protected EntityManager getEntityManager() {
		return em;
	}
	@Override 
	public ProductoTarea buscarPorIdTareaIdProducto(long idProducto, long idTarea){
		String query="select ta from ProductoTarea ta "+
						"where ta.producto.id = :idProducto and ta.tarea.id= :idTarea and ta.flagValidacion like '1'";
		try{
			ProductoTarea productoTarea = (ProductoTarea)em.createQuery(query)
							.setParameter("idProducto", idProducto)
							.setParameter("idTarea", idTarea)
							.getSingleResult();
			return productoTarea;
		}catch(NoResultException e){
			return null;
		}
	}
}
