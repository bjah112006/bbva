package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.RetraccionTarea;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.RetraccionTareaBeanLocal;

/**
 * Session Bean implementation class RetraccionTareaBean
 */
@Stateless
@Local(RetraccionTareaBeanLocal.class)
public class RetraccionTareaBean extends AbstractFacade<RetraccionTarea> implements RetraccionTareaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public RetraccionTareaBean() {
        super(RetraccionTarea.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public RetraccionTarea buscarPorParametros(long idAccion, Long salida, Long llegada) {
	    try{
	        Query q = em.createNamedQuery("RetraccionTarea.find");
	        
			q.setParameter("idAccion", idAccion);
			q.setParameter("salida", salida);
			q.setParameter("llegada", llegada);	        
	        return (RetraccionTarea) q.getSingleResult();
	        
	    } catch(NoResultException e) {
	        return null;
	    }
	}
	
	@Override
	public List<RetraccionTarea> buscarPorFlagRetraer(long idTareaActual, long idTareaAnterior){	
		try{
			List<RetraccionTarea> resultList = em.createNamedQuery("TareaPerfil.findByFlagRetraer")
					.setParameter("idTareaActual", idTareaActual)
					.setParameter("idTareaAnterior", idTareaAnterior)
					.getResultList();
			//Solo debería devolver una fila
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}	
	
	@Override
	public List<RetraccionTarea> buscarPorFlagRetraerTareaAnt(long idTareaAnterior){	
		try{
			List<RetraccionTarea> resultList = em.createNamedQuery("TareaPerfil.findByFlagRetraerAnt")
					.setParameter("idTareaAnterior", idTareaAnterior)
					.getResultList();
			//Solo debería devolver una fila
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}	
	
	
	@Override
	public List<RetraccionTarea> buscarTodos(){	
		try{
			List<RetraccionTarea> resultList = em.createNamedQuery("TareaPerfil.findAll")
					.getResultList();
			//Solo debería devolver una fila
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}
	
 

	

}
