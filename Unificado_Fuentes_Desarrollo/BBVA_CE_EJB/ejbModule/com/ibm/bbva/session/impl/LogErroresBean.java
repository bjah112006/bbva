package com.ibm.bbva.session.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.LogErrores;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.LogErroresBeanLocal;

/**
 * Session Bean implementation class LogErroresBean
 */
@Stateless
public class LogErroresBean extends AbstractFacade<LogErrores> implements LogErroresBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(LogErroresBean.class);
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public LogErroresBean() {
        super(LogErrores.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<LogErrores> buscarLogErrores(Date fechaInicio, Date fechaFin,
			String idPerfil, String codigoEmpleado, String idTarea,
			String idEstado, String codigoExpediente) {
		
		String where = "";
		
		// fechaInicio
		if ( fechaInicio!=null ) {
			where += (where.length()==0? "" : " AND ")+" le.fechaIncidente >= :fechaInicio ";
		}
		
		// fechaFin
		if ( fechaFin!=null ) {			
			where += (where.length()==0? "" : " AND ")+" le.fechaIncidente <= :fechaFin ";
		}
		
		// idPerfil
		if ( idPerfil != null && !idPerfil.trim().equals("")) {
			where += (where.length()==0? "" : " AND ")+" le.perfil.id = :idPerfil ";
		}
		
		// codigoEmpleado
		if ( codigoEmpleado != null && !codigoEmpleado.trim().equals("")) {
			where += (where.length()==0? "" : " AND ")+" le.empleado.codigo = :codigoEmpleado ";
		}
		
		// idTarea 
		if ( idTarea != null && !idTarea.trim().equals("")) {
			where += (where.length()==0? "" : " AND ")+" le.tarea.id = :idTarea ";
		}
		
		// idEstado 
		if ( idEstado != null && !idEstado.trim().equals("")) {
			where += (where.length()==0? "" : " AND ")+" le.estadoCE.id = :idEstado ";
		}
		
		// codigoExpediente
		if ( codigoExpediente != null && !codigoExpediente.trim().equals("")) {
			where += (where.length()==0? "" : " AND ")+" le.expediente.id = :codigoExpediente ";
		}	
		
		String query = "SELECT le FROM LogErrores le "+(where.length()==0? "" : (" WHERE "+where)+ " ORDER BY le.fechaIncidente DESC");
		
		LOG.info("query : "+query);
		
		Query sql = (Query) em.createQuery(query);
		
		// fechaInicio
		if ( fechaInicio!=null ) {
			sql.setParameter("fechaInicio", new Timestamp(fechaInicio.getTime()));
			LOG.info("fechaInicio "+new Timestamp (fechaInicio.getTime()));
		}
		
		// fechaFin
		if ( fechaFin!=null ) {
			sql.setParameter("fechaFin", new Timestamp (fechaFin.getTime()));
			LOG.info("fechaFin "+new Timestamp (fechaFin.getTime()));
		}
		
		// idPerfil
		if ( idPerfil != null && !idPerfil.trim().equals("")) {
			sql.setParameter("idPerfil", Long.valueOf(idPerfil));
			LOG.info("idPerfil : " + idPerfil);
		}
		
		// codigoEmpleado
		if ( codigoEmpleado != null && !codigoEmpleado.trim().equals("")) {
			sql.setParameter("codigoEmpleado", codigoEmpleado);
			LOG.info("codigoEmpleado : " + codigoEmpleado);
		}
		
		// idTarea
		if ( idTarea != null && !idTarea.trim().equals("")) {
			sql.setParameter("idTarea", Long.valueOf(idTarea));
			LOG.info("idTarea : " + idTarea);
		}
		
		// idEstado
		if ( idEstado != null && !idEstado.trim().equals("")) {
			sql.setParameter("idEstado", Long.valueOf(idEstado));
			LOG.info("idEstado : " + idEstado);
		}
		
		// codigoExpediente
		if ( codigoExpediente != null && !codigoExpediente.trim().equals("")) {
			sql.setParameter("codigoExpediente", Long.valueOf(codigoExpediente));
			LOG.info("codigoExpediente : " + codigoExpediente);
		}
		
		LOG.info("sql : "+sql);
		
		List<LogErrores> resultList = sql.getResultList();
				
		return resultList;
	}

	@Override
	public LogErrores create(LogErrores entity) {
		try{
			LogErrores le = super.create(entity);
			return le;
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	@Override
	public void update(LogErrores entity){
		super.edit(entity);
	}

}
