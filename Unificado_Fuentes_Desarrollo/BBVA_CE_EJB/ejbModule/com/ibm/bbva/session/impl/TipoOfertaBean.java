package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoOfertaBeanLocal;

/**
 * Session Bean implementation class TipoOfertaBean
 */
@Stateless
@Local(TipoOfertaBeanLocal.class)
public class TipoOfertaBean extends AbstractFacade<TipoOferta> implements TipoOfertaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoOfertaBean() {
        super(TipoOferta.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoOferta> buscarTodos() {
		try{
			List<TipoOferta> resultList = em.createNamedQuery("TipoOferta.findAll").getResultList();
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public TipoOferta buscarPorId(long id) {
		try{
			return (TipoOferta) em.createNamedQuery("TipoOferta.findById").setParameter("id", id).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}	
}
