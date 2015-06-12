package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TipoCategoria;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoCategoriaBeanLocal;

/**
 * Session Bean implementation class TipoCategoriaBean
 */
@Stateless
@Local(TipoCategoriaBeanLocal.class)
public class TipoCategoriaBean extends AbstractFacade<TipoCategoria> implements TipoCategoriaBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoCategoriaBean() {
        super(TipoCategoria.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoCategoria> buscarTodos() {
		List<TipoCategoria> resultList = em.createNamedQuery("TipoCategoria.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public TipoCategoria buscarPorId(long id){
		try{
			TipoCategoria result = (TipoCategoria) em.createNamedQuery("TipoCategoria.findById")
					.setParameter("id", id)
					.getSingleResult();
			return result;
		}catch(NoResultException e){
			return null;
		}
	}

}
