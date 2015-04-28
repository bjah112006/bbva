package com.ibm.bbva.ctacte.business.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.PlazoSubsanacion;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.PlazoSubsanacionDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;

/**
 * Session Bean implementation class ExpedienteBusinessImpl
 */
@Stateless
@Local(ExpedienteBusiness.class)
public class ExpedienteBusinessImpl implements ExpedienteBusiness {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteBusinessImpl.class);
	
	@EJB
	private ClienteDAO clienteDAO;
	@EJB
	private EstadoTareaDAO estadoDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private PlazoSubsanacionDAO plazoSubsanacionDAO;

    /**
     * Default constructor. 
     */
    public ExpedienteBusinessImpl() {
    }

	@Override
	public void guardarExpediente(Expediente expediente, int idEstadoExp,
			int idTarea, int idEstadoTarea, String accion, Date fechaRegistro) {
		LOG.info("guardarExpediente (Expediente expediente, int idEstadoExp, int idTarea, int idEstadoTarea, String accion)");
		Cliente cliente = expediente.getCliente();
		if (cliente.getId()==null) {
			clienteDAO.save(cliente);
		} else {
			clienteDAO.update(cliente);
		}
		if (expediente.getId()==null) {
			expediente.setFechaRegistro(fechaRegistro);
		}
		
		expediente.setEstado(estadoExpedienteDAO.load(idEstadoExp));
		
		ExpedienteTarea tareaExpediente = new ExpedienteTarea(); 
		EstadoTarea estado = estadoDAO.load(idEstadoTarea);
		Tarea tarea = tareaDAO.load(idTarea);
		tareaExpediente.setExpediente(expediente);
		tareaExpediente.setEstadoTarea(estado);
		tareaExpediente.setTarea(tarea);
		
		HashSet<ExpedienteTarea> expTareaHst = new HashSet<ExpedienteTarea>();
		expTareaHst.add(tareaExpediente);
		
		expediente.setExpedienteTareas(expTareaHst);
		
		expediente.setAccion(accion);
		if (expediente.getId()==null) {
			expedienteDAO.save(expediente);
		} else {
			expedienteDAO.update(expediente);
		}
	}

	@Override
	public int dentroPlazoSubsanacion(Date dtFechaRegistroExpediente,
			Date dtFechaUltimoBastanteo) {
		int intDentroSubsanacion = ConstantesBusiness.FUERA_PLAZO_SUBSANACION;
		try {
			LOG.info("dentroPlazoSubsanacion - fechaRegistroExpediente: "+(dtFechaRegistroExpediente != null ? dtFechaRegistroExpediente : "null"));
			LOG.info("dentroPlazoSubsanacion - dtFechaUltimoBastanteo: "+(dtFechaUltimoBastanteo != null ? dtFechaUltimoBastanteo : "null"));
			long lngDias=0;
			long lngPlazoDias = 0;
			long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
			lngDias = (dtFechaRegistroExpediente.getTime() - dtFechaUltimoBastanteo.getTime()) / MILLSECS_PER_DAY;
			LOG.info("dentroPlazoSubsanacion - lngDias: "+lngDias);
			PlazoSubsanacion plazoSubsanacion = new PlazoSubsanacion();
			List<PlazoSubsanacion> listPlazoSubsanacion = plazoSubsanacionDAO.findAll();
			if (listPlazoSubsanacion.size()>0){
				plazoSubsanacion = listPlazoSubsanacion.get(0);
				lngPlazoDias = plazoSubsanacion.getPlazoDias();
				if (lngPlazoDias>=lngDias){
					intDentroSubsanacion = ConstantesBusiness.DENTRO_PLAZO_SUBSANACION;
				}
			}
			return intDentroSubsanacion;
		} catch (Exception ex) {
			LOG.error("", ex);
			return intDentroSubsanacion;
		}
	}

}
