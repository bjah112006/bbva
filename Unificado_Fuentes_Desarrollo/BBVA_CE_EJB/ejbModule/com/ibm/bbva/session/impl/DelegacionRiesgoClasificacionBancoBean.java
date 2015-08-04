package com.ibm.bbva.session.impl;

import com.ibm.bbva.entities.DelegacionRiesgoClasificacionBanco;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DelegacionRiesgoClasificacionBancoBeanLocal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class DelegacionRiesgoBean
 */
@Stateless
@Local(DelegacionRiesgoClasificacionBancoBeanLocal.class)
public class DelegacionRiesgoClasificacionBancoBean extends AbstractFacade<DelegacionRiesgoClasificacionBanco> implements DelegacionRiesgoClasificacionBancoBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
   
	/**
     * Default constructor. 
     */
    public DelegacionRiesgoClasificacionBancoBean() {
    	super(DelegacionRiesgoClasificacionBanco.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DelegacionRiesgoClasificacionBanco buscarPorId(long idTipoCategoria, long idProducto) {
		try{
			return (DelegacionRiesgoClasificacionBanco) em.createNamedQuery("DelegacionRiesgoClasificacionBanco.findById")
					.setParameter("idTipoCategoria", idTipoCategoria)
					.setParameter("idProducto", idProducto)
					.getSingleResult();			
		}catch(NoResultException e){
			return null;
		}

				
	}
}
