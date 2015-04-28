package com.ibm.bbva.ctacte.controller.form;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.FinaliceSolicitud1MB;
import com.ibm.bbva.ctacte.controller.comun.SubsanarIdentifiquePJOperacionMB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IFinaliceSolicitud1;
import com.ibm.bbva.ctacte.controller.comun.interf.ISubsanarIdentifiquePJOperacion;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.Util;


@ManagedBean(name="subsanarDocumento")
@ViewScoped
public class SubsanarDocumentoMB extends AbstractMBean 
		implements ISubsanarIdentifiquePJOperacion, IDigitaliceDocumentacion, 
			IFinaliceSolicitud1{
	
	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(SubsanarDocumentoMB.class);
	
	private SubsanarIdentifiquePJOperacionMB subsanarIdentifiquePJOperacion;
	private DigitaliceDocumentacionMB digitaliceDocumentacion;
	private FinaliceSolicitud1MB finaliceSolicitud1;

	@EJB
	private DocumentoExpDAO docdao;
	@EJB
	private ExpedienteDAO expedienteDAO;
	
	public void setSubsanarIdentifiquePJOperacion(
			SubsanarIdentifiquePJOperacionMB subsanarIdentifiquePJOperacion) {
		this.subsanarIdentifiquePJOperacion = subsanarIdentifiquePJOperacion;
		
	}

	public void actualizar() {
		LOG.info("actualizar()");
		
	}
	public void escanear() {
		LOG.info("escanear()");
		
	}

	public void setFinaliceSolicitud1(FinaliceSolicitud1MB finaliceSolicitud1MB) {
		this.finaliceSolicitud1 = finaliceSolicitud1MB;
	}

	public void setDigitaliceDocumentacion(
			DigitaliceDocumentacionMB digitaliceDocumentacion) {
		this.digitaliceDocumentacion = digitaliceDocumentacion;
	}

	public SubsanarIdentifiquePJOperacionMB getSubsanarIdentifiquePJOperacion() {
		return subsanarIdentifiquePJOperacion;
	}

	public DigitaliceDocumentacionMB getDigitaliceDocumentacion() {
		return digitaliceDocumentacion;
	}

	public FinaliceSolicitud1MB getFinaliceSolicitud1() {
		return finaliceSolicitud1;
	}
	
	public void actualizarSubidaDocumentos(Character flagDocumentos) {
		LOG.info("actualizarSubidaDocumentos({})", flagDocumentos);
		boolean faltaDocumentos = ConstantesBusiness.SIN_DOCUMENTOS.equals(flagDocumentos) ||
				ConstantesBusiness.FALTA_DOCUMENTOS.equals(flagDocumentos);
		if (finaliceSolicitud1!=null) {
			if (faltaDocumentos) {
				finaliceSolicitud1.setDisRegEnvExp(true);
			} else {
				finaliceSolicitud1.setDisRegEnvExp(false);
			}
		}		
	}

	public void copiarDatos() {
	}

	public boolean esValido() {
		return false;
	}

	public void guardarExpediente(int idEstadoExp, int idTarea, int idEstado,
			String accion) {
	}

	public boolean hayIndicaciones() {
		return finaliceSolicitud1.hayIndicaciones();
	}

	public void habilitarBoton(boolean habReasignar) {
	}
	
	@Override
	public void grabarReenviarExpediente(String accion) {
		LOG.info("grabarReenviarExpediente(String accion)");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		String pagina = getNombrePrincipal();
		
		EstadoTarea estadoTarea=new EstadoTarea();
		if (hayIndicaciones()) {
			LOG.info("Es valido");
			Tarea tarea=new Tarea();
			if ("formSubsanarDocumentos".equals(pagina)){
				if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR)){
					expediente.setEstado(new EstadoExpediente());
					expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
					tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO);
					estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
				}
			}else if ("formSubsanarFirmas".equals(pagina)){
				LOG.info("Esta en Subsanar Firmas");
				if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR)){
					LOG.info("accion" + accion);
					expediente.setEstado(new EstadoExpediente());
					LOG.info("estado expediente" + expediente.getEstado());
					expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
					tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_DOCUMENTOS);
					estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
				}
			}
		}
		// Le quito las observaciones y flag rechazado a los documentos antes de reenviar
		List<DocumentoExp> listaDocs = digitaliceDocumentacion.obtenerDocumentosExpMemoria();
		for (DocumentoExp de : listaDocs) {
			de.setMotivo(null);
			de.setFlagRechazado(null);
			docdao.update(de);
		}
		
		// falta grabar los documentos escaneados
		expediente.setObservaciones(finaliceSolicitud1.getIndicAdicionales());
		//expediente.setMotivo(null);
		Util.generarRegistroHistExp(expediente);
		expediente = expedienteDAO.update(expediente);
		LOG.info("actualizo el expediente");
		LOG.info("Ahora hara el envio del objeto al process");
		
		// FIX para que no se pierda la descripción del estado del expediente
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		
		// para enviar el objeto al process
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		// Flag Envio Content
		expedienteCC.getDatosFlujoCtaCte().setFlagEnvioContent(docdao.validarFlagEnvioContent(expediente.getId()));
		LOG.info("FlagEnvioContent: " + expedienteCC.getDatosFlujoCtaCte().getFlagEnvioContent());
		
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
		LOG.info("realizo el envio al process");
		// para subir archivos al CM 
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("setSubirArchivos");
		subirArchivos(true);
		LOG.info("redirecciona a la bandeja");
		redirectAction("../bandeja/bandeja");
		LOG.info("exito");
	}
	
}
