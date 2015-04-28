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

import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;

/**
 * Session Bean implementation class HistorialBean
 */
@Stateless
@Local(HistorialDAO.class)
public class HistorialDAOImpl extends GenericDAO<Historial, Integer> implements HistorialDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(HistorialDAOImpl.class);

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public HistorialDAOImpl() {
        super(Historial.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Historial> findByExpdiente(int idExpediente) {
		Query query = em.createQuery(
 			"select o from Historial o where o.expediente.id=:idExp order by o.fechaRegistro");
		query.setParameter("idExp", idExpediente);
		List<Historial> historial = query.getResultList();
		return historial;
	}

	@Override
	public List<Historial> findByFiltrofromHistorial(String codEstadoExp,
			String idTarea, String codUserResponsableTarea,
			String idOficinaTarea, String idTerritorioTarea, String nomTarea,
			String codCentral, String idOperacion, String nroDoi,
			String razonSocial, String codGestor, String nroExp,
			String idTerritorioExp, String idOficinaExp, String idEstudioExp,
			String idAbogadoExp, Date strFechaAtencionLimInf,
			Date strFechaAtencionLimSup, Date strFechaAsignacionLimInf,
			Date strFechaAsignacionLimSup, Date strFechaInicioLimInf,
			Date strFechaInicioLimSup, Date strFechaTerminoLimInf,
			Date strFechaTerminoLimSup) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println(" ****************************** findByFiltrofromHistorial *********************************");

		LOG.info(" codEstadoExp-->"+codEstadoExp);
		LOG.info(" idTarea-->"+idTarea);
		LOG.info(" codUserResponsableTarea-->"+codUserResponsableTarea);
		LOG.info(" idOficinaTarea-->"+idOficinaTarea);
		LOG.info(" idTerritorioTarea-->"+idTerritorioTarea);
		LOG.info(" nomTarea-->"+nomTarea);
		LOG.info(" codCentral-->"+codCentral);
		LOG.info(" idOperacion-->"+idOperacion);
		LOG.info(" nroDoi-->"+nroDoi);
		LOG.info(" razonSocial-->"+razonSocial);
		LOG.info(" codGestor-->"+codGestor);
		LOG.info(" nroExp-->"+nroExp);
		LOG.info(" idTerritorioExp-->"+idTerritorioExp);
		LOG.info(" idOficinaExp-->"+idOficinaExp);
		LOG.info(" idEstudioExp-->"+idEstudioExp);
		LOG.info(" idAbogadoExp-->"+idAbogadoExp);
		LOG.info(" strFechaAtencionLimInf-->"+strFechaAtencionLimInf);
		LOG.info(" strFechaAtencionLimSup-->"+strFechaAtencionLimSup);
		LOG.info(" strFechaAsignacionLimInf-->"+strFechaAsignacionLimInf);
		LOG.info(" strFechaAsignacionLimSup-->"+strFechaAsignacionLimSup);
		LOG.info(" strFechaInicioLimInf-->"+strFechaInicioLimInf);
		LOG.info(" strFechaInicioLimSup-->"+strFechaInicioLimSup);
		LOG.info(" strFechaTerminoLimInf-->"+strFechaTerminoLimInf);
		LOG.info(" strFechaTerminoLimSup-->"+strFechaTerminoLimSup);
		
		if (idEstudioExp == null){
			idEstudioExp = "0";
		}
		if (idAbogadoExp == null){
			idAbogadoExp = "0";
		}		

		StringBuilder sb = new StringBuilder();
		sb.append("select o from Historial o ");
		if (codEstadoExp.equalsIgnoreCase("0"))
			sb.append("where o.expediente.id in (select h.expediente.id from Historial h where h.estado.id in (2,3,4))");
		else if (codEstadoExp.equalsIgnoreCase("2"))
			sb.append("where o.expediente.id not in (select h.expediente.id from Historial h where h.estado.id in (3,4))");
		else
			sb.append("where o.expediente.id in (select h.expediente.id from Historial h where h.estado.id =:codEstadoExp)");
		if (!nroExp.equalsIgnoreCase("0"))
			sb.append(" and o.expediente.id =:nroExp");
		if (!idTarea.equalsIgnoreCase("0"))
			sb.append(" and o.tarea.id =:idTarea");
		if (!idOperacion.equalsIgnoreCase("0"))
			sb.append(" and o.operacion.id =:idOperacion");
		if (codUserResponsableTarea != null && !codUserResponsableTarea.trim().equals(""))
			sb.append(" and o.empleado.codigo =:codUserResponsableTarea");// responsable de la tarea
		if (!idEstudioExp.equalsIgnoreCase("0"))  
			sb.append(" and (select count(h) from Historial h where h.expediente.id = o.expediente.id and h.empleado.estudio.id =:idEstudioExp) > 0");//estudio
		if (!idTerritorioTarea.equalsIgnoreCase("0"))
			sb.append(" and o.oficina.territorio.id =:idTerritorioTarea");
		if (!idOficinaTarea.equalsIgnoreCase("0"))
			sb.append(" and o.oficina.id =:idOficinaTarea");
		if (!idAbogadoExp.equalsIgnoreCase("0"))
			sb.append(" and (select count(h) from Historial h where h.expediente.id = o.expediente.id and h.empleado.codigo =:idAbogadoExp) > 0");
		if (codGestor != null && !codGestor.trim().equals(""))
			sb.append(" and (select count(h) from Historial h where h.expediente.id = o.expediente.id and h.empleado.codigo =:codGestor and h.tarea.id = 1) > 0");//gestor del expediente (usuario de la tarea 1)
		if (!idTerritorioExp.equalsIgnoreCase("0"))
			sb.append(" and (select count(h) from Historial h where h.expediente.id = o.expediente.id and h.oficina.territorio.id =:idTerritorioExp and h.tarea.id = 1) > 0");//territorio donde fue creado el exp (tarea 1)
		if (!idOficinaExp.equalsIgnoreCase("0"))
			sb.append(" and (select count(h) from Historial h where h.expediente.id = o.expediente.id and h.oficina.id =:idOficinaExp and h.tarea.id = 1) > 0");//oficina donde fue creado el exp (tarea 1)
		if (codCentral != null && !codCentral.trim().equals(""))
			sb.append(" and o.cliente.codigoCentral =:codCentral");
		if (nroDoi != null && !nroDoi.trim().equals(""))
			sb.append(" and o.cliente.numeroDoi =:nroDoi");
		if (!nomTarea.equalsIgnoreCase("0"))
			sb.append(" and o.tarea.id =:nomTarea");
		if (strFechaAsignacionLimInf != null && strFechaAsignacionLimSup != null)
			sb.append(" and o.fechaRegistro between :strFechaAsignacionLimInf and :strFechaAsignacionLimSup");
		if (strFechaAtencionLimInf != null && strFechaAtencionLimSup != null)
			sb.append(" and o.fechaProgramada between :strFechaAtencionLimInf and :strFechaAtencionLimSup");
		if (strFechaInicioLimInf != null && strFechaInicioLimSup != null)
			sb.append(" and o.fechaEnvio between :strFechaInicioLimInf and :strFechaInicioLimSup");
		if  (strFechaTerminoLimInf != null && strFechaTerminoLimSup != null)
			sb.append(" and o.fechaFin between :strFechaTerminoLimInf and :strFechaTerminoLimSup");
		if (razonSocial != null && !razonSocial.trim().equals(""))
			sb.append(" and upper(o.cliente.razonSocial) like :razonSocial");
	
		LOG.info("strSQLHist-->"+sb.toString());
		Query query = em.createQuery(sb.toString());
		if (!codEstadoExp.equalsIgnoreCase("0") && !codEstadoExp.equalsIgnoreCase("2"))
			query.setParameter("codEstadoExp", Integer.parseInt(codEstadoExp));
		if (!nroExp.equalsIgnoreCase("0"))
			query.setParameter("nroExp", Integer.parseInt(nroExp));
		if (!idTarea.equalsIgnoreCase("0"))
			query.setParameter("idTarea", Integer.parseInt(idTarea));
		if (!idOperacion.equalsIgnoreCase("0"))
			query.setParameter("idOperacion", Integer.parseInt(idOperacion));
		if (codUserResponsableTarea != null && !codUserResponsableTarea.trim().equals(""))
			query.setParameter("codUserResponsableTarea", codUserResponsableTarea);
		if (!idEstudioExp.equalsIgnoreCase("0"))
			query.setParameter("idEstudioExp", Integer.parseInt(idEstudioExp));
		if (!idTerritorioTarea.equalsIgnoreCase("0"))
			query.setParameter("idTerritorioTarea", Integer.parseInt(idTerritorioTarea));
		if (!idOficinaTarea.equalsIgnoreCase("0"))
			query.setParameter("idOficinaTarea", Integer.parseInt(idOficinaTarea));
		if (!idAbogadoExp.equalsIgnoreCase("0"))
			query.setParameter("idAbogadoExp", idAbogadoExp);
		if (codGestor != null && !codGestor.trim().equals(""))
			query.setParameter("codGestor", codGestor);
		if (!idTerritorioExp.equalsIgnoreCase("0"))
			query.setParameter("idTerritorioExp", Integer.parseInt(idTerritorioExp));
		if (!idOficinaExp.equalsIgnoreCase("0"))
			query.setParameter("idOficinaExp", Integer.parseInt(idOficinaExp));
		if (codCentral != null && !codCentral.trim().equals(""))
			query.setParameter("codCentral", codCentral);
		if (nroDoi != null && !nroDoi.trim().equals(""))
			query.setParameter("nroDoi", nroDoi);
		if (!nomTarea.equalsIgnoreCase("0"))
			query.setParameter("nomTarea", Integer.parseInt(nomTarea));
		if (strFechaAsignacionLimInf != null && strFechaAsignacionLimSup != null) {
			query.setParameter("strFechaAsignacionLimInf", strFechaAsignacionLimInf);
			query.setParameter("strFechaAsignacionLimSup", strFechaAsignacionLimSup);
		}
		if (strFechaAtencionLimInf != null && strFechaAtencionLimSup != null) {
			query.setParameter("strFechaAtencionLimInf", strFechaAtencionLimInf);
			query.setParameter("strFechaAtencionLimSup", strFechaAtencionLimSup);
		}
		if (strFechaInicioLimInf != null && strFechaInicioLimSup != null) {
			query.setParameter("strFechaInicioLimInf", strFechaInicioLimInf);
			query.setParameter("strFechaInicioLimSup", strFechaInicioLimSup);
		}
		if  (strFechaTerminoLimInf != null && strFechaTerminoLimSup != null) {
			query.setParameter("strFechaTerminoLimInf", strFechaTerminoLimInf);
			query.setParameter("strFechaTerminoLimSup", strFechaTerminoLimSup);
		}
		if (razonSocial != null && !razonSocial.trim().equals(""))
			query.setParameter("razonSocial", "%"+razonSocial.toUpperCase()+"%");
		
		List<Historial> historiales = query.getResultList();
		
		return historiales;
	}

	@Override
	public Historial findUltimaTarea(int idExpediente, int idTarea) {
		Query query = em.createQuery(
 			"select o from Historial o where o.expediente.id=:idExp and o.tarea.id=:idTarea order by o.fechaRegistro DESC");
		query.setParameter("idExp", idExpediente);
		query.setParameter("idTarea", idTarea);
		List<Historial> historial = query.getResultList();
		if (historial.size() == 0) {
			return null;
		} else {
			return historial.get(0);
		}
	}

}
