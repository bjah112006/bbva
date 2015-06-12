package com.ibm.bbva.session.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ExpedienteBeanLocal;

/**
 * Session Bean implementation class ExpedienteBean
 */
@Stateless
@Local(ExpedienteBeanLocal.class)
public class ExpedienteBean extends AbstractFacade<Expediente> implements ExpedienteBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteBean.class);

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public ExpedienteBean() {
        super(Expediente.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public Expediente buscarPorId(long id) {
		try{
			return (Expediente) em.createNamedQuery("Expediente.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public List<Expediente> buscarPorIdCliente(long idCliente) {
		List<Expediente> resultList = em.createNamedQuery("Expediente.findByIdCliente").setParameter("idCliente", idCliente).getResultList();
		return resultList;
	}
/*
	@Override
	public List<Expediente> buscarPorIdEmpleado(long idUsuario) {
		try{
			List<Expediente> resultList = em.createNamedQuery("Expediente.findByIdUsuario")
					.setParameter("idUsuario", idUsuario)
					.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}		
	}*/
	
	@Override
	public List<Expediente> buscarPorIdEmpleado(long id) {
		String idActivo="1";
	//	String query="SELECT e FROM Empleado e WHERE e.id = :id and e.flagActivo like :idActivo";
		String query="SELECT e FROM Expediente e WHERE e.empleado.id = :id and e.empleado.flagActivo like :idActivo";
		LOG.info("query = "+query);
		try{
			List<Expediente> resultList = (List<Expediente>) em.createQuery(query)
					.setParameter("id", id)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public List<Expediente> reporteHistPlano (long idOficina, Date fechaInicio, Date fechaFin) {
		String where = "";
		
		if ( idOficina>0 ) {
			where += (where.length()==0? "" : " AND ")+"e.empleado.oficina.id= :idOficina";
		}
		
		if ( fechaInicio!=null ) {
			where += (where.length()==0? "" : " AND ")+"e.fecRegistro>= :fechaInicio";
		}
		if ( fechaFin!=null ) {
			where += (where.length()==0? "" : " AND ")+"e.fecRegistro<= :fechaFin";
		}
		
		String query = "SELECT e FROM Expediente e " +
	                   (where.length()==0? "" : (" WHERE "+where))+
	                   " ORDER BY e.id";
		
		Query q = em.createQuery(query);
		
		if ( idOficina>0 ) {
		   q.setParameter("idOficina", idOficina);	
	    }
        if ( fechaInicio!=null ) {
		   q.setParameter("fechaInicio", new Timestamp(fechaInicio.getTime()));

    	}
	    if ( fechaFin!=null ) {
		   q.setParameter("fechaFin", new Timestamp(fechaFin.getTime()));
	    }	    
		List<Expediente> resultList = q.getResultList();
		
	return resultList;
	}

	@Override
	public Expediente create(Expediente entity) {
		return super.create(entity);
	}

	@Override
	public void edit(Expediente entity) {
		super.edit(entity);
	}
	
	@Override
	public int actualizarExpediente(Long idEmpleado, Long idExpediente){
		
		String query="UPDATE Expediente d SET d.empleado.id=:idEmpleado where d.id=:idExpediente";

		try{
			int updatedEntities = em.createQuery( query )
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("idExpediente", idExpediente)
			        .executeUpdate();
					
			return updatedEntities;	
			
		}catch(NoResultException e){
			return 0;
		}

	}
	
	@Override
	public int actualizarEstadoExpediente(String estado, Long idExpediente){
		
		String query="UPDATE Expediente d SET d.flagActivo=:estado where d.id=:idExpediente";

		try{
			int updatedEntities = em.createQuery( query )
					.setParameter("estado", estado)
					.setParameter("idExpediente", idExpediente)
			        .executeUpdate();
					
			return updatedEntities;	
			
		}catch(NoResultException e){
			return 0;
		}

	}	
	
	@Override
	public Expediente buscarPorIdExpediente(long idExpediente) {
		try{
			Expediente objExp = (Expediente) em.createNamedQuery("Expediente.findByIdExpediente")
					.setParameter("idExpediente", idExpediente)
					.getSingleResult();
			return objExp;			
		}catch(NoResultException e){
			return null;
		}

	}	
}