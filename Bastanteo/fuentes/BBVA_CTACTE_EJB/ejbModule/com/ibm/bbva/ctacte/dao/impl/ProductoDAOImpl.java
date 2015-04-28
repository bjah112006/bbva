package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Producto;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ProductoDAO;

/**
 * Session Bean implementation class ProductoBean
 */
@Stateless
@Local(ProductoDAO.class)
public class ProductoDAOImpl extends GenericDAO<Producto, Integer> implements ProductoDAO{

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ProductoDAOImpl() {
        super(Producto.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

    @Override
	public List<Producto> findAll() {
		Query query = em.createQuery(
 			"select o from Producto o ");
		List<Producto> productos = query.getResultList();
		return productos;
	}
    
    
	@Override
	public Producto findByID(Long id) {
		try {
			Query query = em.createQuery(
	 			"select o from Producto o where o.id=:id");
			query.setParameter("id", id);
			Producto producto = (Producto) query.getSingleResult();
			return producto;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Producto findByCodigo(String codigo) {
		try {
			Query query = em.createQuery(
	 			"select o from Producto o where o.codigo=:codigo");
			query.setParameter("codigo", codigo);
			Producto producto = (Producto) query.getSingleResult();
			return producto;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Producto findProductoByCodigo(String codigo) {
		try {
			Query query = em.createQuery(
	 			"select o from Producto o where o.codigo=:codigo");
			query.setParameter("codigo", codigo);
			Producto producto = (Producto) query.getSingleResult();
			return producto;
		} catch (NoResultException e) {
			return null;
		}
	}

}
