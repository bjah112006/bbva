package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.ClasifBanco;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ClasifBancoBeanLocal;

/**
 * Session Bean implementation class ClasifBancoBean
 */
@Stateless
@Local(ClasifBancoBeanLocal.class)
public class ClasifBancoBean extends AbstractFacade<ClasifBanco> implements ClasifBancoBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public ClasifBancoBean() {
    	super(ClasifBanco.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<ClasifBanco> buscarTodos() {
		List<ClasifBanco> resultList = em.createNamedQuery("ClasifBanco.findAll").getResultList();
		return resultList;
	}
    
    @Override
	public ClasifBanco buscarPorId(long id) {
		try{
			return (ClasifBanco) em.createNamedQuery("ClasifBanco.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
    
    @Override
	public List<ClasifBanco> buscarPorIdProducto(long idProducto) {
		try{
			List<ClasifBanco> resultList = em.createNamedQuery("ClasifBanco.findByProducto")
					.setParameter("idProducto", idProducto)
					.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
		
	}    
    

}
