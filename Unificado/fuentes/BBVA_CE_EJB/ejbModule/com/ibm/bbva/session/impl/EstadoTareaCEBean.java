package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;

import com.ibm.bbva.entities.EstadoTareaCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(EstadoTareaCEBeanLocal.class)
public class EstadoTareaCEBean extends AbstractFacade<EstadoTareaCE> implements EstadoTareaCEBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public EstadoTareaCEBean() {
        super(EstadoTareaCE.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	
 
	@Override
	public List<EstadoTareaCE> buscarPorIdEstado(long idEstado) {
		List<EstadoTareaCE> resultList = em.createNamedQuery("EstadoTareaCE.findIdEstado")
				.setParameter("idEstado", idEstado)
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<EstadoTareaCE> buscarPorIdTarea(long idTarea) {
		List<EstadoTareaCE> resultList = em.createNamedQuery("EstadoTareaCE.findIdTarea")
				.setParameter("idTarea", idTarea)
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<EstadoTareaCE> buscarPorIdPerfil(long idPerfil) {		
		String query="select e from EstadoTareaCE e where e.tarea.id in (select p.tarea.id from TareaPerfil p where p.perfil.id=:idPerfil)";
		System.out.println("query : "+query);
		try{
			return em.createQuery(query)
				   .setParameter("idPerfil", idPerfil)
				   .getResultList();			
		}catch (NoResultException e) {
			return null;
		}				
	}
	@Override
	public List<EstadoTareaCE> buscar() {
		List<EstadoTareaCE> resultList = em.createNamedQuery("EstadoTareaCE.findAll")
				.getResultList();
		return resultList;
	} 	

	@Override
	public List<EstadoTareaCE> buscarPorIdPerfilMenosEnDesuso(long idPerfil) {
		String query="select e from EstadoTareaCE e where e.tarea.id in (select p.tarea.id from TareaPerfil p where p.perfil.id=:idPerfil) and e.estadoCE.id not in (16)";
		System.out.println("query : "+query);
		try{
			return em.createQuery(query)
				   .setParameter("idPerfil", idPerfil)
				   .getResultList();			
		}catch (NoResultException e) {
			return null;
		}
	} 	
	
}
