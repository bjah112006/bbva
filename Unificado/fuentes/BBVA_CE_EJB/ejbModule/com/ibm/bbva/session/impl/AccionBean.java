package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Accion;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.AccionBeanLocal;

/**
 * Session Bean implementation class EmpleadoBean
 */
@Stateless
public class AccionBean  extends AbstractFacade<Accion> implements AccionBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public AccionBean() {
    	super(Accion.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Accion> buscarTodos() {
		List<Accion> resultList = em.createNamedQuery("Accion.findAll").getResultList();
		return resultList;
		
	}
	
	@Override
	public Accion buscarPorId(long id) {
		try{
			return (Accion) em.createNamedQuery("Accion.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public Accion buscarPorAccion(String strAccion){
		String query="SELECT a FROM Accion a "+
				 "WHERE a.descripcion= :strAccion ";
		System.out.println("query = "+query);
		try{
			Accion resultList = (Accion)em.createQuery(query)
					.setParameter("strAccion", strAccion)
					.getSingleResult();
			return resultList;
		}catch (NoResultException e) {
			return null;
		}
	}
	
}
