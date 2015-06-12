package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EstadoBeanLocal;

/**
 * Session Bean implementation class EstadoBean
 */
@Stateless
@Local(EstadoBeanLocal.class)
public class EstadoBean extends AbstractFacade<Estado> implements EstadoBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(EstadoBean.class);

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public EstadoBean() {
    	super(Estado.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<Estado> buscarTodos() {
		List<Estado> resultList = em.createNamedQuery("Estado.findAll").getResultList();
		return resultList;
	}    
    
    @Override
	public Estado buscarPorId(long id) {
		return (Estado) em.createNamedQuery("Estado.findById").setParameter("id", id).getSingleResult();
	}
    
	@Override
	public List<Estado> buscarPorIdPerfil(long idPerfil) {
		
		String query="select e from Estado e where e.tarea.id in (select p.tarea.id from TareaPerfil p where p.perfil.id=:idPerfil)";
		
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
	public List<Estado> buscarPorIdTarea(long idTarea) {
		
		String query="select e from Estado e where e.tarea.id=:idTarea";
		
		LOG.info("query : "+query);	
		
		try{
			return em.createQuery(query)
				   .setParameter("idTarea", idTarea)
				   .getResultList();			
		}catch (NoResultException e) {
			return null;
		}

				
	}
	
    
    
}
