package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import javax.persistence.Query;

/**
 * Session Bean implementation class EstadoCEBean
 */
@Stateless
@Local(EstadoCEBeanLocal.class)
public class EstadoCEBean extends AbstractFacade<EstadoCE> implements EstadoCEBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public EstadoCEBean() {
    	super(EstadoCE.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public List<EstadoCE> buscarTodos() { 
		List<EstadoCE> resultList = em.createNamedQuery("EstadoCE.findAll").getResultList();
		return resultList;
	}    
    
    @Override
	public EstadoCE buscarPorId(long id) {
		return (EstadoCE) em.createNamedQuery("EstadoCE.findById").setParameter("id", id).getSingleResult();
	}
    
	@Override
	public List<EstadoCE> buscarPorIdPerfil(long idPerfil) {
		
		String query="select e from Estado e where e.tarea.id in (select p.tarea.id from TareaPerfil p where p.perfil.id=:idPerfil)";
		
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
	public List<EstadoCE> buscarPorIdEstado(long idEstado) {
		
		String query="select e from EstadoCE e where e.id=:idEstado";
		
		System.out.println("query : "+query);	
		
		try{
			return em.createQuery(query)
				   .setParameter("idEstado", idEstado)
				   .getResultList();			
		}catch (NoResultException e) {
			return null;
		}				
	}
	
	
	@Override
	public List<EstadoCE> buscarTodosMenosEnDesuso() {
		// Agregar al WHERE NOT IN todos los estados que ya no se usen
		Query query = em.createQuery("select o from EstadoCE o where o.id not in (16) order by o.descripcion");
		List<EstadoCE> list = query.getResultList();
		return list;
	}
    
}
