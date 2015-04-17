package com.ibm.bbva.session.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TblCeIbmDelegacionOficina;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TblCeIbmDelegacionOficinaLocal;

/**
 * Session Bean implementation class TblCeIbmDelegacionOficina
 */
@Stateless
@Local(TblCeIbmDelegacionOficinaLocal.class)
public class TblCeIbmDelegacionOficinaBean extends AbstractFacade<TblCeIbmDelegacionOficina> implements TblCeIbmDelegacionOficinaLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public TblCeIbmDelegacionOficinaBean() {
        super(TblCeIbmDelegacionOficina.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public TblCeIbmDelegacionOficina buscarPorIdNivelIdMonedaIdProducto(long idNivel, long idMoneda, long idProducto) {
		try {
			return (TblCeIbmDelegacionOficina) em.createNamedQuery("TblCeIbmDelegacionOficina.findById")
					.setParameter("idNivel", idNivel)
					.setParameter("idMoneda", idMoneda)
					.setParameter("idProducto", idProducto)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
				
	}
	
	@Override
	public TblCeIbmDelegacionOficina buscarPorIdNivelIdProducto(long idNivel, long idProducto) {
		try {
			return (TblCeIbmDelegacionOficina) em.createNamedQuery("TblCeIbmDelegacionOficina.findByNivelProducto")
					.setParameter("idNivel", idNivel)					
					.setParameter("idProducto", idProducto)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
				
	}
	
	@Override
	public TblCeIbmDelegacionOficina buscarPorId(long idNivel) {
		try {
			return (TblCeIbmDelegacionOficina) em.createNamedQuery("TblCeIbmDelegacionOficina.findById")
					.setParameter("idNivel", idNivel)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
				
	}

}
