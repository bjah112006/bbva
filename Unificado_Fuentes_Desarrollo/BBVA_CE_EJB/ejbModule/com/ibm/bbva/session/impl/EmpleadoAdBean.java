package com.ibm.bbva.session.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.EmpleadoAd;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EmpleadoAdBeanLocal;

/**
 * Session Bean implementation class EmpleadoAdBean
 */
@Stateless
public class EmpleadoAdBean extends AbstractFacade<EmpleadoAd> implements EmpleadoAdBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public EmpleadoAdBean() {
    	super(EmpleadoAd.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public EmpleadoAd buscarPorCodigo(String codigo) {
		try{
			return (EmpleadoAd) em.createNamedQuery("EmpleadoAd.findByCodigo").setParameter("codigo", codigo).getSingleResult();		
		}catch(NoResultException e){
			return null;
		}
	}
	
}
