package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.EstadoCivil;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EstadoCivilBeanLocal;

/**
 * Session Bean implementation class EstadoCivilBean
 */
@Stateless
@Local(EstadoCivilBeanLocal.class)
public class EstadoCivilBean extends AbstractFacade<EstadoCivil> implements EstadoCivilBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public EstadoCivilBean() {
    	super(EstadoCivil.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<EstadoCivil> buscarTodos() {
		List<EstadoCivil> resultList = em.createNamedQuery("EstadoCivil.findAll").getResultList();
		return resultList;
	}

	@Override
	public EstadoCivil buscarPorId(long id) {
		try {
			EstadoCivil estadoCivil = (EstadoCivil) em
					.createQuery("SELECT e FROM EstadoCivil e WHERE e.id = :id")
					.setParameter("id", id).getSingleResult();
			return estadoCivil;
		} catch (Exception e) {
			return null;
		}
	}

}
