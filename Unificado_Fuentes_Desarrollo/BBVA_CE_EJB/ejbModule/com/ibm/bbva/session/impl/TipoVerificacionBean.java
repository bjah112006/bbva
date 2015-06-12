package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.TipoVerificacion;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoVerificacionBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TipoVerificacionBean
 */
@Stateless
@Local(TipoVerificacionBeanLocal.class)
public class TipoVerificacionBean extends AbstractFacade<TipoVerificacion> implements TipoVerificacionBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoVerificacionBean() {
        super(TipoVerificacion.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoVerificacion> buscarTodos() {
		List<TipoVerificacion> resultList = em.createNamedQuery("TipoVerificacion.findAll").getResultList();
		return resultList;
	}
}
