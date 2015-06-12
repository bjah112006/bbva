package com.ibm.bbva.session.impl;

import com.ibm.bbva.entities.DelegacionRiesgo;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DelegacionRiesgoBeanLocal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class DelegacionRiesgoBean
 */
@Stateless
@Local(DelegacionRiesgoBeanLocal.class)
public class DelegacionRiesgoBean extends AbstractFacade<DelegacionRiesgo> implements DelegacionRiesgoBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
   
	/**
     * Default constructor. 
     */
    public DelegacionRiesgoBean() {
    	super(DelegacionRiesgo.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DelegacionRiesgo buscarPorId(long idTipoCategoria, long idProducto, long idMoneda) {
		try{
			return (DelegacionRiesgo) em.createNamedQuery("DelegacionRiesgo.findById")
					.setParameter("idTipoCategoria", idTipoCategoria)
					.setParameter("idProducto", idProducto)
					.setParameter("idMoneda", idMoneda)
					.getSingleResult();			
		}catch(NoResultException e){
			return null;
		}

				
	}
}
