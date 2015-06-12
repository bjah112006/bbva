package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.LineaMaxima;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.LineaMaximaBeanLocal;

/**
 * Session Bean implementation class LineaMaximaBean
 */
@Stateless
@Local(LineaMaximaBeanLocal.class)
public class LineaMaximaBean extends AbstractFacade<LineaMaxima> implements LineaMaximaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public LineaMaximaBean() {
        super(LineaMaxima.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<LineaMaxima> buscarTodos() {
		List<LineaMaxima> resultList;
		try{
			resultList = em.createNamedQuery("LineaMaxima.findAll").getResultList();
		}catch(NoResultException e){
			resultList=new ArrayList<LineaMaxima>();
		}
		
		return resultList;
	}	

}
