package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.PersonaBeanLocal;

/**
 * Session Bean implementation class PersonaBean
 */
@Stateless
public class PersonaBean  extends AbstractFacade<Persona> implements PersonaBeanLocal{
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public PersonaBean() {
    	super(Persona.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	
	@Override
	public List<Persona> buscarTodos() {
		
		try{
			return (List<Persona>) em.createNamedQuery("Persona.findAll").getResultList();	
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public Persona buscarPorId(long id) {
		return (Persona) em.createNamedQuery("Persona.findById").setParameter("id", id).getSingleResult();
	}

}
