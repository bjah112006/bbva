package com.ibm.bbva.ctacte.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.ctacte.util.Util;

/**
 * Servlet implementation class CierreAutomaticoExpediente
 */
public class CierreAutomaticoExpediente extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CierreAutomaticoExpediente.class);
	
	@EJB
	private ParametrosConfDAO parametrosConfDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	@EJB
	private EstadoTareaDAO estadoTareaDAO;
	@EJB
	private ExpedienteTareaProcesoDAO expedienteTareaProcesoDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CierreAutomaticoExpediente() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cerrarExpedientes(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cerrarExpedientes(request, response);
	}
	
	private void cerrarExpedientes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String flagActivo = parametrosConfDAO.obtener(ParametrosSistema.CARGA, ConstantesParametros.CIERRE_AUTO_FLAG_ACTIVO).getValorVariable();
			
			if (flagActivo != null && flagActivo.equals("1")) {
				long diasAntiguedad = Long.valueOf(parametrosConfDAO.obtener(ParametrosSistema.CARGA, ConstantesParametros.CIERRE_AUTO_DIAS_ANTIGUEDAD).getValorVariable());
				
				RemoteUtils bandeja = new RemoteUtils();
				ConsultaCC consulta = new ConsultaCC();
				consulta.setNumeroTarea(Integer.toString(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE));
				consulta.setConsiderarUsuarios(false);
				
				List<ExpedienteCC> expedientesPorCerrar = bandeja.obtenerInstanciasTareasPorUsuarioCC(consulta);
				LOG.info("Se encontraron {} expedientes en la tarea {}.",
						expedientesPorCerrar.size(),
						ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE);
				
				Calendar fechaActual = Calendar.getInstance();
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				LOG.info("Fecha actual: {}", df.format(fechaActual.getTime()));
				
				for (ExpedienteCC expedienteCC : expedientesPorCerrar) {
					long difMilisegundos = fechaActual.getTimeInMillis() - expedienteCC.getActivado().getTimeInMillis();
					long difDias = TimeUnit.DAYS.convert(difMilisegundos, TimeUnit.MILLISECONDS);
					
					LOG.info("Fecha asignación expediente: {}", df.format(expedienteCC.getActivado().getTime()));
					LOG.info("El expediente {} tiene {} días de antiguedad.",
							expedienteCC.getCodigoExpediente(),
							difDias);
					
					if (difDias >= diasAntiguedad) { //Cerrar el expediente
						Expediente expediente = expedienteDAO.load(Integer.parseInt(expedienteCC.getCodigoExpediente()));
						expediente.setNumTerminal(request.getRemoteAddr());
						expediente.setFechaEnvio(expedienteCC.getActivado().getTime());
						Tarea tarea = tareaDAO.load(Integer.valueOf(expedienteCC.getDatosFlujoCtaCte().getIdTarea()));
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(expediente.getFechaEnvio());
						calendar.add(Calendar.MINUTE, tarea.getVerdeMinutos());
						expediente.setFechaProgramada(calendar.getTime());
						expediente.setEstado(estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO));
						
						ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
						expedienteTarea.setTarea(tarea);
						expedienteTarea.setEstadoTarea(estadoTareaDAO.load(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO));
						
						Empleado empleado = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
						Util.generarRegistroHistExp(expediente, empleado);
						
						Date fechaFin = null;
						DateTime fechaHoy = null;
						fechaHoy = new DateTime(new Date());
						fechaFin = fechaHoy.toDate();
						expediente.setFechaFin(fechaFin);
						expediente.setAccion(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR);
						expediente.setFlagCierreAutomatico(Constantes.CHECK_SELECCIONADO);
						expediente = expedienteDAO.update(expediente);
						expedienteTareaProcesoDAO.eliminarAnterioresByIdExp(expediente.getId());
						
						expedienteCC = RemoteUtils.copiarDatos(expediente, expedienteCC);
						expedienteCC.getDatosFlujoCtaCte().setAccion(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR);
						String tkiid = expedienteCC.getTaskID();
						bandeja.completarTarea(tkiid, expedienteCC);
						
						LOG.info("El expediente {} ha sido cerrado.", expedienteCC.getCodigoExpediente());
					} else {
						LOG.info("El expediente {} NO na sido cerrado.", expedienteCC.getCodigoExpediente());
					}
				}
			} else {
				LOG.warn("No se ejecutará el cierre automático porque el parámetro '{}' es igual a '{}'.",
						ConstantesParametros.CIERRE_AUTO_FLAG_ACTIVO,
						flagActivo);
			}
			
			PrintWriter pw = response.getWriter();
			pw.println("OK");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			PrintWriter pw = response.getWriter();
			pw.println("ERROR");
			pw.flush();
			pw.close();
		}
	}

}
