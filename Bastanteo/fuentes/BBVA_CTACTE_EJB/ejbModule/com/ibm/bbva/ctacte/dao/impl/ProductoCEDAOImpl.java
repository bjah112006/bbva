package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Producto;
import com.ibm.bbva.ctacte.bean.ProductoCE;
import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ProductoCEDAO;

/**
 * Session Bean implementation class EmpleadoBean
 */
@Stateless
@Local(ProductoCEDAO.class)
public class ProductoCEDAOImpl extends GenericDAO<ProductoCE, Integer> implements ProductoCEDAO{
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public ProductoCEDAOImpl() {
		super(ProductoCE.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

 	@Override
	public ProductoCE findByID(Long id) {
		String query="SELECT e FROM ProductoCE e WHERE e.id = :id";
		System.out.println("query = "+query);
		try{
			ProductoCE resultList = (ProductoCE) em.createQuery(query)
					.setParameter("id", id)
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public ProductoCE findByCodigo(String codigo) {
		try {
			Query query = em.createQuery(
	 			"select o from ProductoCE o where o.codigo=:codigo");
			query.setParameter("codProducto", codigo);
			ProductoCE productoCE = (ProductoCE) query.getSingleResult();
			return productoCE;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/*
	@Override
	public ProductoCE findProductoByCodigo(String codigo) {
		try {
			Query query = em.createQuery(
	 			"select o from ProductoCE o where o.codigo=:codigo");
			query.setParameter("codProducto", codigo);
			ProductoCE productoCE = (ProductoCE) query.getSingleResult();
			return productoCE;
		} catch (NoResultException e) {
			return null;
		}
	}
	*/
	
	
}
