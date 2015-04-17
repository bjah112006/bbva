package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.PerfilBeanLocal;

/**
 * Session Bean implementation class PerfilBean
 */
@Stateless
public class PerfilBean extends AbstractFacade<Perfil> implements PerfilBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public PerfilBean() {
    	super(Perfil.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/* (non-Javadoc)
	 * @see com.ibm.bbva.session.PerfilBeanLocal#obtenerExisteCarterizacion(long)
	 */
	/*
	@Override
	public List<Perfil> obtenerExisteCarterizacion(long idTerritorio) {
		try{
			List<Perfil> resultList = em.createNamedQuery("Perfil.obtenerExisteCarterizacion").setParameter("idTerritorio", idTerritorio).getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}		
	}*/

	/* (non-Javadoc)
	 * @see com.ibm.bbva.session.PerfilBeanLocal#buscarPorId(long)
	 */
	
	@Override
	public List<Perfil> buscarPorIdCaract(long idCaract) {
		List<Perfil> resultList = em.createNamedQuery("Perfil.findIdCaract")
				.setParameter("idCaract", idCaract)
				.setParameter("flagActivo", "1")
				.getResultList();
		return resultList;
	}
	
	
	@Override
	public Perfil buscarPorId(long id) {
		try{
			return (Perfil) em.createNamedQuery("Perfil.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}

	}
	
	@Override
	public List<Perfil> buscarTodos() {
		try{
			return em.createNamedQuery("Perfil.findAll")
					.getResultList();
		}catch(NoResultException e){
			return null;
		}

	}
	
	@Override
	public List<Perfil> buscarPorIdJefe(long idPerfilJefe) {
		try{
			List<Perfil> resultList = em.createNamedQuery("Perfil.findByIdJefe").setParameter("idPerfilJefe", idPerfilJefe).getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<Perfil> buscarPorProceso() {
		List<Perfil> resultList = em.createNamedQuery("Perfil.findByProcess")
				.setParameter("flagProceso", "1")
				.getResultList();
		return resultList;
	}
	
}
