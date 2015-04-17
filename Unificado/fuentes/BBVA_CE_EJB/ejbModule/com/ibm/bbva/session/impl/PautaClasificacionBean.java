package com.ibm.bbva.session.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.PautaClasificacion;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.PautaClasificacionBeanLocal;

/**
 * Session Bean implementation class PautaClasificacionBean
 */
@Stateless
@Local(PautaClasificacionBeanLocal.class)
public class PautaClasificacionBean extends AbstractFacade<PautaClasificacion> implements PautaClasificacionBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public PautaClasificacionBean() {
        super(PautaClasificacion.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public PautaClasificacion buscarPorIdPersona(long idPersona) {
		try{
			return (PautaClasificacion) em.createNamedQuery("PautaClasificacion.findByTipoPersona")
					.setParameter("idPersona", idPersona)
					.getSingleResult();			
		}catch(NoResultException e){
			return null;
		}

				
	}	
	
}
