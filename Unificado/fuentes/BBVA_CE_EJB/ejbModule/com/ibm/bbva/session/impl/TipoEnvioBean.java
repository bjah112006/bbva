package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.TipoEnvio;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoEnvioBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TipoEnvioBean
 */
@Stateless
@Local(TipoEnvioBeanLocal.class)
public class TipoEnvioBean extends AbstractFacade<TipoEnvio> implements TipoEnvioBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoEnvioBean() {
        super(TipoEnvio.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoEnvio> buscarTodos() {
		List<TipoEnvio> resultList = em.createNamedQuery("TipoEnvio.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public TipoEnvio buscarPorId(long id) {
		return (TipoEnvio) em.createNamedQuery("TipoEnvio.findById").setParameter("id", id).getSingleResult();
	}

}
