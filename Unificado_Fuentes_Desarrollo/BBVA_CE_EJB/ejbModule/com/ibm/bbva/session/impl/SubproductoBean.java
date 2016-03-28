package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.SubproductoBeanLocal;

/**
 * Session Bean implementation class SubproductoBean
 */
@Stateless
@Local(SubproductoBeanLocal.class)
public class SubproductoBean extends AbstractFacade<Subproducto> implements SubproductoBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public SubproductoBean() {
    	super(Subproducto.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Subproducto> buscarTodos() {
		List<Subproducto> resultList = em.createNamedQuery("Subproducto.findAll").getResultList();
		return resultList;
	}
	
    @Override
	public Subproducto buscarPorId(long id) {
    	try{
    		return (Subproducto) em.createNamedQuery("Subproducto.findById").setParameter("id", id).getSingleResult();
    	} catch(NoResultException e){
			return null;
		}
	}
    
       
	@Override
	public List<Subproducto> buscarPorIdProducto(long idProducto) {
		List<Subproducto> resultList = em.createNamedQuery("Subproducto.buscarPorIdProducto").setParameter("idProducto", idProducto).getResultList();
		return resultList;
	}
	
	@Override
	public List<Subproducto> buscarPorIdProd(long idProducto) {
		List<Subproducto> resultList = em.createNamedQuery("Subproducto.buscarPorIdProd").setParameter("idProducto", idProducto).setParameter("flag", "1") .getResultList();
		return resultList;
	}

}
