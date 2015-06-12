package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.ParametrosProceso;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ParametrosProcesoBeanLocal;

/**
 * Session Bean implementation class ParametrosConfBean
 */
@Stateless
@Local(ParametrosProcesoBeanLocal.class)
@LocalBean
public class ParametrosProcesoBean extends AbstractFacade<ParametrosProceso> implements ParametrosProcesoBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public ParametrosProcesoBean() {
    	super(ParametrosProceso.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<ParametrosProceso> buscarTodos() {
		List<ParametrosProceso> resultList = em.createNamedQuery("ParametrosProceso.findAll").getResultList();
		return resultList;
	}    
    
    @Override
	public ParametrosProceso buscarPorVariable(String nombreVariable) {
		return (ParametrosProceso) em.createNamedQuery("ParametrosProceso.findByVariable").setParameter("nombreVariable", nombreVariable.trim()).getSingleResult();
	}
}