package com.ibm.bbva.session.impl;

import com.ibm.bbva.entities.DelegacionRiesgoCondicion;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DelegacionRiesgoCondicionBeanLocal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class DelegacionRiesgoBean
 */
@Stateless
@Local(DelegacionRiesgoCondicionBeanLocal.class)
public class DelegacionRiesgoCondicionBean extends AbstractFacade<DelegacionRiesgoCondicion> implements DelegacionRiesgoCondicionBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
   
	/**
     * Default constructor. 
     */
    public DelegacionRiesgoCondicionBean() {
    	super(DelegacionRiesgoCondicion.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DelegacionRiesgoCondicion buscarPorId(long idDelegRiesgCond) {
		try{
			return (DelegacionRiesgoCondicion) em.createNamedQuery("DelegacionRiesgoCondicion.findById")
					.setParameter("id", idDelegRiesgCond)
					.getSingleResult();			
		}catch(NoResultException e){
			return null;
		}

				
	}
}
