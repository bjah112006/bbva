package com.ibm.bbva.ctacte.controller.comun;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieVerificarResultado;
import com.ibm.bbva.ctacte.controller.form.VerificarResultadoTramiteMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="pieVerificarResultado")
@ViewScoped
public class PieVerificarResultadoMB extends AbstractMBean{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieVerificarResultadoMB.class);
	@ManagedProperty(value="#{verificarResultadoTramite}")
	private VerificarResultadoTramiteMB verificarResultadoTramite;
	private IPieVerificarResultado iPieVerificarResultado;
	private Expediente expediente;
	private Empleado empleado;
	private int idTarea;
	private String opcionResultado;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private ExpedienteTareaProcesoDAO expedienteTareaProcesoDAO;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("PaginaActual {}",pagina);
		if ("formVerificarResultadoTramite".equals(pagina)){
			iPieVerificarResultado=verificarResultadoTramite;			
		}
		empleado=(Empleado)Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		expediente=(Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		//expediente.setEmpleado(empleado);
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_VERIFICAR_RESULTADO_TRAMITE.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		if (expediente.getResultado() != null) {
			opcionResultado=expediente.getResultado();
		}
	}	
	
	public void actionAceptar(ActionEvent ae){
		LOG.info("actionAceptar(ActionEvent ae)");
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR;
		LOG.info("codigo operacion"+expediente.getOperacion().getCodigoOperacion());
		if (expediente.getOperacion().getCodigoOperacion().equalsIgnoreCase(ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA)){
			LOG.info("Es revocatoria");
			ejecutarPreRegistroModificatorias(accion);
		}else{
			LOG.info("No es revocatoria");
			expedienteTerminadoFinalizado(accion);
		}
		subirArchivos(true, true);
		redirectAction("../bandeja/bandeja");
	}
	
	private void ejecutarPreRegistroModificatorias(String accion){
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR)){
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO);
			tarea.setId(ConstantesBusiness.ID_TAREA_PRE_REGISTRO);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO);
			ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
			expedienteTarea.setEstadoTarea(estadoTarea);
			LOG.info("estado tarea" + estadoTarea.getId());
		}
		Util.generarRegistroHistExp(expediente);
		// inicio seteo la fecha fin del expediente
		Date fechaFin = null;
		DateTime fechaHoy = null;
		fechaHoy = new DateTime (new Date());
		fechaFin=fechaHoy.toDate();
		expediente.setFechaFin(fechaFin);
		expediente.setAccion(accion);
		LOG.info("fecha fin" + expediente.getFechaFin());
		// fin del seteo de la fecha fin del expediente
		expediente = expedienteDAO.update(expediente);
		expedienteTareaProcesoDAO.eliminarAnterioresByIdExp(expediente.getId());
		
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
	}
	
	private void expedienteTerminadoFinalizado(String accion){
		LOG.info("expedienteTerminadoFinalizado(String accion)");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expedienteTerminadoFinalizado(String accion)");
		//ExpedienteTarea expedienteTarea=new ExpedienteTarea();
		LOG.info("Tarea tarea=new Tarea();");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Tarea tarea=new Tarea();");
		EstadoTarea estadoTarea=new EstadoTarea();
		LOG.info("EstadoTarea estadoTarea=new EstadoTarea();");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("EstadoTarea estadoTarea=new EstadoTarea();");
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR)){			
			expediente.setEstado(new EstadoExpediente());						
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO);
			LOG.info("estado expediente" + expediente.getEstado().getId());
			//tarea.setId(ConstantesBusiness.ID_TAREA_PRE_REGISTRO);			
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO);
			ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
			expedienteTarea.setEstadoTarea(estadoTarea);
			LOG.info("estado tarea" + estadoTarea.getId());
		}

		LOG.info("Historial expediente");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Historial expediente");
		Util.generarRegistroHistExp(expediente);
		// inicio seteo la fecha fin del expediente		
		expediente.setFechaFin(new Date());
		expediente.setAccion(accion);
		LOG.info("fecha fin" + expediente.getFechaFin());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("fecha fin" + expediente.getFechaFin());
		// fin del seteo de la fecha fin del expediente
		expediente = expedienteDAO.update(expediente);
		LOG.info("Actualizo expediente");
		expedienteTareaProcesoDAO.eliminarAnterioresByIdExp(expediente.getId());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Actualizo expediente");
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		LOG.info("opcionResultadoBastanteo: " + opcionResultado);
		expedienteCC.getDatosFlujoCtaCte().setResultadoBastanteo(opcionResultado);
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
	}
	
	public void setIPieVerificarResultado(IPieVerificarResultado iPieVerificarResultado) {
		this.iPieVerificarResultado = iPieVerificarResultado;
	}
	public IPieVerificarResultado getIPieVerificarResultado() {
		return iPieVerificarResultado;
	}
	public void setVerificarResultadoTramite(VerificarResultadoTramiteMB verificarResultadoTramite) {
		this.verificarResultadoTramite = verificarResultadoTramite;
	}
	public VerificarResultadoTramiteMB getVerificarResultadoTramite() {
		return verificarResultadoTramite;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}
	
	public String getOpcionResultado() {
		return opcionResultado;
	}

	public void setOpcionResultado(String opcionResultado) {
		this.opcionResultado = opcionResultado;
	}
	
}
