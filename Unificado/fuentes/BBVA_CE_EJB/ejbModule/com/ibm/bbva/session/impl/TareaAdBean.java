package com.ibm.bbva.session.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TareaAd;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TareaAdBeanLocal;

/**
 * Session Bean implementation class TareaAdBean
 */
@Stateless
public class TareaAdBean extends AbstractFacade<TareaAd> implements TareaAdBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TareaAdBean() {
        super(TareaAd.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public TareaAd buscarPorId(long id) {
		return (TareaAd) em.createNamedQuery("TareaAd.findById").setParameter("id", id).getSingleResult();
	}
    

}
