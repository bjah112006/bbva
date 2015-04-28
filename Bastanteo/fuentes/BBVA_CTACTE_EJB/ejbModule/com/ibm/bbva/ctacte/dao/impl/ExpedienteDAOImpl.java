package com.ibm.bbva.ctacte.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class ExpedienteBean
 */
@Stateless
@Local(ExpedienteDAO.class)
public class ExpedienteDAOImpl extends GenericDAO<Expediente, Integer> implements ExpedienteDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteDAOImpl.class);
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ExpedienteDAOImpl() {
        super(Expediente.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Expediente> findByCodigoCentral(String codigoCentral) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent");
		query.setParameter("codCent", codigoCentral);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralSinEstado(String codigoCentral,
			List<Integer> estados) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.estado.id not in (:listEst)");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("listEst", estados);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralConEstado(String codigoCentral,
			Integer idEstado) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.estado.id = :idEst");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("idEst", idEstado);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralConCodOperacion(
			String codigoCentral, String codOperacion) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.operacion.codigoOperacion=:codOper");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("codOper", codOperacion);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralSinIdExpediente(
			String codigoCentral, Integer idExpediente) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.id <> :idExp");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("idExp", idExpediente);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public Expediente getExpediente(String codigoCentral, int idEstado,
			String codOperacion) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and " +
 			"o.estado.id=:idEst and o.operacion.codigoOperacion=:codOper");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("idEst", idEstado);
		query.setParameter("codOper", codOperacion);
		//Expediente expediente = (Expediente) query.uniqueResult();
		Expediente expediente = null;
		List<Expediente> expedientes = query.getResultList();
		if (!expedientes.isEmpty()) {
			expediente = (Expediente) expedientes.get(0);
		}
		return expediente;
	}

	@Override
	public List<Expediente> findByEstado(String codUsuario, int idestado) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.estado.id=:idestado and o.empleado.codigo=:codusuario");
		query.setParameter("idestado", idestado);
		query.setParameter("codusuario", codUsuario);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idestado-->"+idestado);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codUsuario-->"+codUsuario);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByFiltrofromExpediente(String nroExp,
			String estadoExp, String codCentral, String idOperacion,
			String nroDoi, String razonSocial, String codGestor,
			String idTerritorio, String idOficina, String codResponsable,
			Date fechaInicioLimInf, Date fechaInicioLimSup) {
		/* Consulta registros de la tabla Expediente cuyos estados sea igual a Preregistro ()estadoExp = 1 )
		 * 
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("select o from Expediente o ");
		sb.append("where o.estado.id = 1");
		if (!nroExp.equalsIgnoreCase("0"))
			sb.append(" and o.id =:nroExp");
		if (codCentral != null && !codCentral.trim().equals(""))
			sb.append(" and o.cliente.codigoCentral =:codCentral");
		if (!idOperacion.equalsIgnoreCase("0"))
			sb.append(" and o.operacion.id =:idOperacion");
		if (nroDoi != null && !nroDoi.trim().equals(""))
			sb.append(" and o.cliente.numeroDoi =:nroDoi");
		if (razonSocial != null && !razonSocial.trim().equals(""))
			sb.append(" and upper(o.cliente.razonSocial) like :razonSocial");
		if (codGestor != null && !codGestor.trim().equals(""))
			sb.append(" and o.empleado.codigo =:codGestor");
		if (!idTerritorio.equalsIgnoreCase("0"))
			sb.append(" and o.oficina.territorio.id =:idTerritorio");
		if (!idOficina.equalsIgnoreCase("0"))
			sb.append(" and o.oficina.id =:idOficina");
		if (codResponsable != null && !codResponsable.trim().equals(""))
			sb.append(" and o.empleado.codigo =:codResponsable");
		if (fechaInicioLimInf != null && fechaInicioLimSup != null)
			sb.append(" and o.fechaRegistro between :fechaInicioLimInf and :fechaInicioLimSup");
		
		LOG.info("strSQLHist-->"+sb.toString());
		Query query = em.createQuery(sb.toString());
		if (!nroExp.equalsIgnoreCase("0"))
			query.setParameter("nroExp", Integer.parseInt(nroExp));
		if (codCentral != null && !codCentral.trim().equals(""))
			query.setParameter("codCentral", codCentral);
		if (!idOperacion.equalsIgnoreCase("0"))
			query.setParameter("idOperacion", Integer.parseInt(idOperacion));
		if (nroDoi != null && !nroDoi.trim().equals(""))
			query.setParameter("nroDoi", nroDoi);
		if (razonSocial != null && !razonSocial.trim().equals(""))
			query.setParameter("razonSocial", "%"+razonSocial.toUpperCase()+"%");
		if (codGestor != null && !codGestor.trim().equals(""))
			query.setParameter("codGestor", codGestor);
		if (!idTerritorio.equalsIgnoreCase("0"))
			query.setParameter("idTerritorio", Integer.parseInt(idTerritorio));
		if (!idOficina.equalsIgnoreCase("0"))
			query.setParameter("idOficina", Integer.parseInt(idOficina));
		if (codResponsable != null && !codResponsable.trim().equals(""))
			query.setParameter("codResponsable", codResponsable);
		if (fechaInicioLimInf != null && fechaInicioLimSup != null) {
			query.setParameter("fechaInicioLimInf", fechaInicioLimInf);
			query.setParameter("fechaInicioLimSup", fechaInicioLimSup);
		}
		
		List<Expediente> expedientes = query.getResultList();
		
		return expedientes;
	}

}
