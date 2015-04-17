package com.ibm.bbva.session.impl;


import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TareaAccion;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TareaAccionBeanLocal;



/**
 * Session Bean implementation class TareaPerfilBean
 */
@Stateless
@Local(TareaAccionBeanLocal.class)
public class TareaAccionBean extends AbstractFacade<TareaAccion> implements TareaAccionBeanLocal {


	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TareaAccionBean() {
        super(TareaAccion.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
	@Override
	public List<TareaAccion> buscarPorIdTarea(long idTarea) {
		try{
			List<TareaAccion> resultList = em.createNamedQuery("TareaAccion.findByIdTarea")
					.setParameter("idTarea", idTarea)
					.getResultList();
			return resultList;			
		}catch(NoResultException e){
			return null;
		}

	}    
	
	@Override
	public List<TareaAccion> buscarPorIdAccion(long idAccion) {
		try{
			List<TareaAccion> resultList = em.createNamedQuery("TareaAccion.findByIdAccion")
					.setParameter("idAccion", idAccion)
					.getResultList();
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}    
	
}
