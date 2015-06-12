package com.ibm.bbva.session.impl;


import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TareaPerfilBeanLocal;



/**
 * Session Bean implementation class TareaPerfilBean
 */
@Stateless
@Local(TareaPerfilBeanLocal.class)
public class TareaPerfilBean extends AbstractFacade<TareaPerfil> implements TareaPerfilBeanLocal {


	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TareaPerfilBean() {
        super(TareaPerfil.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
	@Override
	public List<TareaPerfil> buscarPorIdTarea(long idTarea) {
		try{
			List<TareaPerfil> resultList = em.createNamedQuery("TareaPerfil.findByIdTarea")
					.setParameter("idTarea", idTarea)
					.getResultList();
			return resultList;			
		}catch(NoResultException e){
			return null;
		}

	}    
	
	@Override
	public List<TareaPerfil> buscarPorIdPerfil(long idPerfil) {
		try{
			List<TareaPerfil> resultList = em.createNamedQuery("TareaPerfil.findByIdPerfil")
					.setParameter("idPerfil", idPerfil)
					.getResultList();
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}    
	
}
