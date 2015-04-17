package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.ParametrosConf;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

/**
 * Session Bean implementation class ParametrosConfBean
 */
@Stateless
@Local(ParametrosConfBeanLocal.class)
@LocalBean
public class ParametrosConfBean extends AbstractFacade<ParametrosConf> implements ParametrosConfBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public ParametrosConfBean() {
    	super(ParametrosConf.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<ParametrosConf> buscarTodos() {
		List<ParametrosConf> resultList = em.createNamedQuery("ParametrosConf.findAll").getResultList();
		return resultList;
	}    
    
    @Override
	public List<ParametrosConf> buscarPorAplicativo(int codigoAplicativo) {
		List<ParametrosConf> resultList = em.createNamedQuery("ParametrosConf.findByAplicativo")
				.setParameter("codigoAplicativo", codigoAplicativo)
				.getResultList();
		return resultList;
	}
    
    @Override
	public ParametrosConf buscarPorVariable(int codigoAplicativo, String nombreVariable) {
    	try{
    		return (ParametrosConf) em.createNamedQuery("ParametrosConf.findByVariable")
    				.setParameter("codigoAplicativo", codigoAplicativo)
    				.setParameter("nombreVariable", nombreVariable)
    				.getSingleResult();
    	}catch(NoResultException e){
			return null;
		}
		
	}
}