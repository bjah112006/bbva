package com.ibm.bbva.ctacte.controller.form;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.DatosClienteMB;
import com.ibm.bbva.ctacte.controller.comun.DatosGeneralesMB;
import com.ibm.bbva.ctacte.controller.comun.DocumentacionDigitalizadaMB;
import com.ibm.bbva.ctacte.controller.comun.ResultadoRevisionMB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosCliente;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosGenerales;
import com.ibm.bbva.ctacte.controller.comun.interf.IDocumentacionDigitalizada;
import com.ibm.bbva.ctacte.controller.comun.interf.IResultadoRevision;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;

@ManagedBean(name = "verificarCalidadDocumentos")
@ViewScoped
public class VerificarCalidadDocumentoMB extends AbstractMBean implements
		IDatosGenerales, IDatosCliente, IResultadoRevision,
		IDocumentacionDigitalizada {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(VerificarCalidadDocumentoMB.class);

	private DatosClienteMB datosCliente;
	private DatosGeneralesMB datosGenerales;
	private ResultadoRevisionMB resultadoRevision;
	private DocumentacionDigitalizadaMB docDigitalizada;

	// flag de errores a controlar para habilitar Botones Aprobar y Rechazar
	private boolean errorValidacionFechaVigencia;
	private boolean errorCambioTipoDocumento;
	private boolean existeRechazoSeleccionado; // true si hay un check de
												// rechazo
	private boolean errorSeleccionarMotivoRechazo; // true si NO seleccinó
													// Motivo de rechazo
	private boolean faltaHistorialRequeridos; // true si existe algun documento
												// requerido q No tenga
												// Historial
												// falso si todos los documentos
												// requeridos tienen Historial

	@EJB
	private DocumentoExpDAO docExpedienteDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;

	@PostConstruct
	public void iniciar() {
		iniciarEnvioFTP();
	}

	public boolean isFaltaHistorialRequeridos() {
		return faltaHistorialRequeridos;
	}

	public void setFaltaHistorialRequeridos(boolean faltaHistorialRequeridos) {
		this.faltaHistorialRequeridos = faltaHistorialRequeridos;
	}

	public boolean isExisteRechazoSeleccionado() {
		return existeRechazoSeleccionado;
	}

	public void setExisteRechazoSeleccionado(boolean existeRechazoSeleccionado) {
		this.existeRechazoSeleccionado = existeRechazoSeleccionado;
	}

	public boolean isErrorValidacionFechaVigencia() {
		return errorValidacionFechaVigencia;
	}

	public void setErrorValidacionFechaVigencia(
			boolean errorValidacionFechaVigencia) {
		this.errorValidacionFechaVigencia = errorValidacionFechaVigencia;
	}

	public boolean isErrorCambioTipoDocumento() {
		return errorCambioTipoDocumento;
	}

	public void setErrorCambioTipoDocumento(boolean errorCambioTipoDocumento) {
		this.errorCambioTipoDocumento = errorCambioTipoDocumento;
	}

	public boolean isErrorSeleccionarMotivoRechazo() {
		return errorSeleccionarMotivoRechazo;
	}

	public void setErrorSeleccionarMotivoRechazo(
			boolean errorSeleccionarMotivoRechazo) {
		this.errorSeleccionarMotivoRechazo = errorSeleccionarMotivoRechazo;
	}

	public void setDocumentacionDigitalizada(
			DocumentacionDigitalizadaMB docDigitalizada) {
		this.docDigitalizada = docDigitalizada;
		actualizarBotones();
	}

	public void setResultadoRevision(ResultadoRevisionMB resultadoRevision) {
		this.resultadoRevision = resultadoRevision;
		actualizarBotones();
	}

	public void setDatosCliente(DatosClienteMB datosCliente) {
		this.datosCliente = datosCliente;
	}

	public void setDatosGenerales(DatosGeneralesMB datosGenerales) {
		this.datosGenerales = datosGenerales;
	}

	private void actualizarBotones() {
		if (resultadoRevision != null && docDigitalizada != null) {
			docDigitalizada.verificarSeleccionMotivoR();
		}
	}

	public void aplicarCambioTipoDocumento(
			List<DocumentoExpWrapper> listaDocumentoExpW) {
		LOG.info("aplicarCambioTipoDocumento(List<DocumentoExp> listaDocumentoExp)");
		List<DocumentoExp> listaDocumentoExp = new ArrayList();

		LOG.info("listaDocumentoExpW.size() : {}", listaDocumentoExpW.size());
		for (int i = 0; i < listaDocumentoExpW.size(); i++) {
			listaDocumentoExp.add(((DocumentoExpWrapper) listaDocumentoExpW
					.get(i)).getDocExpediente());
		}

		int cont = 0;
		for (int i = 0; i < listaDocumentoExp.size(); i++) {
			cont = 0;
			// +POR SOLICITUD BBVA+//+POR SOLICITUD
			// BBVA+System.out..println("ID iiii-->"+listaDocumentoExp.get(i).getDocumento().getId());
			for (int j = 0; j < listaDocumentoExp.size(); j++) {
				// +POR SOLICITUD BBVA+//+POR SOLICITUD
				// BBVA+System.out..println("id-->"+listaDocumentoExp.get(j).getDocumento().getId());
				if (listaDocumentoExp.get(i).getDocumento().getId().intValue() == listaDocumentoExp
						.get(j).getDocumento().getId().intValue())
					cont = cont + 1;
			}
			LOG.info("contador : {}", cont);
			// +POR SOLICITUD BBVA+//+POR SOLICITUD
			// BBVA+System.out..println("contador** -->"+cont);
			if (cont > 1) {
				mensajeErrorPrincipal("idDocDigitalizada:tablaDocumentos",
						"Existen documentos duplicados");
				// Habilitando/Deshabilitando botones Aprobar/Rechazar
				setErrorCambioTipoDocumento(true);
				actualizarBtnAprobarRechazarbyErrores();

				// Botonería adicional de la pantalla
				docDigitalizada.setDesHabilitarBtnEliminar(true);
				docDigitalizada.setDesHabilitarBtnEscaneo(true);
				docDigitalizada.setDesHabilitarBtnMerge(true);
				break;
			}
		}
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("contador -->"+cont);
		if (cont == 1) { // Si no existen documento duplicados
			// DocumentoExp docExp=null;
			LOG.info("listaDocumentoExp.size() : {}", listaDocumentoExp.size());
			for (int i = 0; i < listaDocumentoExp.size(); i++) {
				DocumentoExp docExp = (DocumentoExp) listaDocumentoExp.get(i);
				// Seteamos el Motivo de Rechazo y Fecha Vigencia en objetos
				// Vacios,
				// para que no guarde motivo de Rechazo ni Fecha de Vigencia
				// docExp.setFechaVigencia(null); // se graba la fecha propuesta
				// docExp.setMotivo(null);
				// docExp.setFlagRechazado(ConstantesAdmin.FLAG_FALSO);
				// docExpedienteDAO.update(docExp);
				// inicio mod man1
				if (docExp.getNombreArchivo() != null) {
					DocumentoExp docExpBD = docExpedienteDAO
							.load(listaDocumentoExp.get(i).getId());
					Documento doc = new Documento();
					doc.setId(docExp.getDocumento().getId());
					Expediente exp = new Expediente();
					exp.setId(docExp.getExpediente().getId());
					docExpBD.setDocumento(doc);
					docExpBD.setExpediente(exp);
					docExpBD.setFechaVigencia(null);
					docExpBD.setMotivo(null);
					docExpBD.setFlagRechazado(ConstantesAdmin.FLAG_FALSO);
					docExp = docExpedienteDAO.update(docExpBD);
				}
				// fin mod man1
			}

			// Habilitando/Deshabilitando botones Aprobar/Rechazar
			setErrorCambioTipoDocumento(false);
			actualizarBtnAprobarRechazarbyErrores();

			// Botonería adicional de la pantalla
			docDigitalizada.setDesHabilitarBtnEliminar(false);
			docDigitalizada.setDesHabilitarBtnEscaneo(false);
			docDigitalizada.setDesHabilitarBtnMerge(false);

			docDigitalizada.iniciarSeccionDocumentacionDigitalizada();
			actualizarBtnAprobarRechazar(false, true);
			// docDigitalizada.actualizarEstadoBtnMerge();
		}
	}

	public void escanearHistorico() {
		LOG.info("escanearHistorico()");
	}

	public void aprobarDocumentacion(String accion) {
		LOG.info("aprobarDocumentacion()");
		// Realizo las validaciones en caso los flags de error estuvieran en
		// true

		// fin validaciones

		grabarInformacion(accion);
		// llamo al API
	}

	public void rechazarDocumentacion(String accion) {
		LOG.info("rechazarDocumentacion()");
		grabarInformacion(accion);
		// llamo al API
	}

	public void actualizarBtnAprobarRechazar(boolean deshBtnAprobar,
			boolean deshBtnRechazar) {
		LOG.info("actualizarBtnAprobarRechazar({}, {})", deshBtnAprobar,
				deshBtnRechazar);
		resultadoRevision.setDesHabilitarBotonAprobar(deshBtnAprobar);
		resultadoRevision.setDesHabilitarBotonRechazar(deshBtnRechazar);
	}

	public void grabarInformacion(String accion) {
		LOG.info("grabarInformacion(String accion)");
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("accion-->"+accion);
		// Actualizando Documento Expediente
		List<DocumentoExp> listaDocExpediente = docDigitalizada
				.getListaDocExpediente();
		DocumentoExp docExp = null;

		for (int i = 0; i < listaDocExpediente.size(); i++) {
			docExp = listaDocExpediente.get(i);
			docExp.setFlagAlterno(null); // este flag solo es temporal
			docExpedienteDAO.update(docExp);
		}

		// Actualizando el expediente
		Expediente expediente = (Expediente) Util
				.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		expediente.setObservaciones(resultadoRevision.getComentario());
		expediente.setObservacionesBastanteo(datosGenerales
				.getMensajePlazoSubsanacion());
		LOG.info("expediente.getObservacionesBastanteo(): "
				+ expediente.getObservacionesBastanteo());
		expediente.setAccion(accion);
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("estado Id "+expediente.getEstado().getId());
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("estado Id "+expediente.getEstado().getDescripcion());
		// En este obj expediente se actualiza: Doc Historico, Fecha Vigencia,
		// si aplica: Rechazo y Motivo, Observaciones

		Tarea tarea = new Tarea();
		EstadoTarea estadoTarea = new EstadoTarea();

		// Genero un registro en Historial con los datos nuevo
		if (accion
				.equalsIgnoreCase(ConstantesBusiness.ACCION_RECHAZAR_DOCUMENTACION)) {
			// expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_SUBSANAR_DOCUMENTO);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		} else if (accion
				.equalsIgnoreCase(ConstantesBusiness.ACCION_APROBAR_DOCUMENTACION)) {
			// expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}

		// Genero un expediente Historial con los datos nuevo
		Util.generarRegistroHistExp(expediente);

		// Actualizo el expediente
		expediente = expedienteDAO.update(expediente);

		// Renombro los nombre de los archivos de la carpeta TRANSFERENCIAS
		// renombrarArchivos();

		//
		// Util.removeObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		// Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION,expediente);

		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		// bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);

		subirArchivos(true);
		// redirectAction("../bandeja/bandeja");

	}

	public void renombrarArchivos(ActionEvent ae) {
		LOG.info("renombrarArchivos (ActionEvent ae)");

		StringBuilder archivos = new StringBuilder();
		for (DocumentoExpWrapper dew : docDigitalizada.getListaDocExpedienteW()) {
			// +POR SOLICITUD BBVA+//+POR SOLICITUD
			// BBVA+System.out..println("renombrarArchivos1 "+dew.isNewDocHistorial());
			if (dew.isNewDocHistorial()) { // si el registro tiene un nuevo doc
											// historial
				// +POR SOLICITUD BBVA+//+POR SOLICITUD
				// BBVA+System.out..println("renombrarArchivos2");
				String nombreActual = dew.getNombreFile().concat(
						ConstantesAdmin.EXTENSION_ARCHIVO_PDF);
				String nombreNuevo = dew.getNombreFileFinal().concat(
						ConstantesAdmin.EXTENSION_ARCHIVO_PDF);
				// archivos.append("{\"actual\":\"").append(nombreActual).append("\",\"nuevo\":\"").append(nombreNuevo).append("\"}");
				archivos.append(nombreActual).append(":").append(nombreNuevo)
						.append(";");
				// +POR SOLICITUD BBVA+//+POR SOLICITUD
				// BBVA+System.out..println("archivos "+archivos.toString());
			}
		}
		addCallbackParam("archivos", archivos.toString());
	}

	public void actualizarBtnAprobarRechazarbyErrores() {
		LOG.info("actualizarBtnAprobarRechazarbyErrores()");
		Expediente expediente = (Expediente) Util
				.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);

		LOG.info("errorValidacionFechaVigencia-->"
				+ errorValidacionFechaVigencia);
		LOG.info("errorCambioTipoDocumento-->" + errorCambioTipoDocumento);
		LOG.info("existeRechazoSeleccionado-->" + existeRechazoSeleccionado);
		LOG.info("errorSeleccionarMotivoRechazo-->"
				+ errorSeleccionarMotivoRechazo);
		LOG.info("faltaHistorialRequeridos-->" + faltaHistorialRequeridos);

		// Si es operacion Nuevo Bastanteo entonces anulación la validación
		// Faltan Historialles para los Obligatorios
		if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO
				.equalsIgnoreCase(expediente.getOperacion()
						.getCodigoOperacion()))
			faltaHistorialRequeridos = false;

		LOG.info("faltaHistorialRequeridos-->" + faltaHistorialRequeridos);

		if (isExisteRechazoSeleccionado()) { // si hay rechazo
			if (isErrorSeleccionarMotivoRechazo()) { // y no se seleccionó
														// motivo de rechazo
				actualizarBtnAprobarRechazar(true, true);
				LOG.info("CASO 1");
			} else if (!isErrorCambioTipoDocumento()
					&& !isErrorValidacionFechaVigencia()) {// //y no hay otros
															// errores
				actualizarBtnAprobarRechazar(true, false);
				LOG.info("CASO 2");
			} else { // y Hay errores
				actualizarBtnAprobarRechazar(true, true);
				LOG.info("CASO 3");
			}
		} else if (isErrorCambioTipoDocumento()
				|| isErrorValidacionFechaVigencia()
				|| isFaltaHistorialRequeridos()) {
			// si hay error en retipificacion o en la fecha de vigencia o falta
			// historico de archivos requeridos
			actualizarBtnAprobarRechazar(true, true);
			LOG.info("CASO 4");
		} else { // Si no hay error y No se seleccionó rechazo
			actualizarBtnAprobarRechazar(false, true);
			LOG.info("CASO 5");
		}
	}

}
