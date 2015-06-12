package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.EstadoTareaCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(EstadoTareaCEBeanLocal.class)
public class EstadoTareaCEBean extends AbstractFacade<EstadoTareaCE> implements EstadoTareaCEBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(EstadoTareaCEBean.class);
	
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
		LOG.info("query : "+query);
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
		LOG.info("query : "+query);
		try{
			return em.createQuery(query)
				   .setParameter("idPerfil", idPerfil)
				   .getResultList();			
		}catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public List<EstadoTareaCE> buscarPorTareaEstado(long idtarea, String estado) {
		String query="select e from EstadoTareaCE e inner join e.estado d where e.estado.id=d.id and e.tarea.id=: idtarea and d.descripcion like :idEstado";
		LOG.info("query : "+query);
		try{
			return em.createQuery(query)
				   .setParameter("idtarea", idtarea)
				   .setParameter("idEstado", estado)
				   .getResultList();			
		}catch (NoResultException e) {
			return null;
		}
	} 	
	
}
