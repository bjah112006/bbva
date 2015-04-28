package com.ibm.bbva.ctacte.controller.form;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.UrlContentManagerMB;
import com.ibm.bbva.ctacte.controller.comun.VerificaDocumentacionDigitalizadaMB;
import com.ibm.bbva.ctacte.controller.comun.VerificaFirmaObservacionesMB;
import com.ibm.bbva.ctacte.controller.comun.interf.IUrlContentManager;
import com.ibm.bbva.ctacte.controller.comun.interf.IVerificaDocumentacionDigitalizada;
import com.ibm.bbva.ctacte.controller.comun.interf.IVerificaFirmaObservaciones;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;


@ManagedBean (name="verificarCalidadFirma")
@ViewScoped
public class VerificarCalidadFirmasMB extends AbstractMBean
		implements IUrlContentManager,IVerificaFirmaObservaciones,IVerificaDocumentacionDigitalizada{

	
	private VerificaDocumentacionDigitalizadaMB verificaDocumentacionDigitalizada;
	private VerificaFirmaObservacionesMB verificaFirmaObservaciones;
	private UrlContentManagerMB urlContentManager;
	
	private boolean errorSeleccionarMotivoRechazo;
	private boolean existeRechazoSeleccionado;
	
	private boolean archivoRechazado;
	private String existeFirmaAsociada;
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(VerificarCalidadFirmasMB.class);
	
	@EJB
	private DocumentoExpDAO docExpedienteDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	
	public void setVerificaFirmaObservaciones(
			VerificaFirmaObservacionesMB verificaFirmaObservaciones) {
		this.verificaFirmaObservaciones=verificaFirmaObservaciones;
		
	}
	public void setVerificaDocumentacionDigitalizada(
			VerificaDocumentacionDigitalizadaMB verificaDocumentacionDigitalizada) {
		this.verificaDocumentacionDigitalizada=verificaDocumentacionDigitalizada;
		
	}
	
	public void aplicarCambioTipoDocumento(List<DocumentoExpWrapper> listaDocumentoExpW){
		LOG.info("aplicarCambioTipoDocumento(List<DocumentoExp> listaDocumentoExp)");
		List<DocumentoExp> listaDocumentoExp =  new ArrayList();
		for (int i = 0; i < listaDocumentoExpW.size(); i++) {
			DocumentoExp documentoExp = listaDocumentoExpW.get(i).getDocExpediente();
			String idMotivo = listaDocumentoExpW.get(i).getIdMotivo();
			if (StringUtils.isNotEmpty(idMotivo)){
				Motivo motivo = new Motivo();
				motivo.setId(Integer.parseInt(idMotivo));
				documentoExp.setMotivo(motivo);
			}
			
			listaDocumentoExp.add(documentoExp);
			
		}

		int cont = 0;
		for (int i = 0; i < listaDocumentoExp.size(); i++) {
			cont = 0;
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("ID iiii-->"+listaDocumentoExp.get(i).getDocumento().getId());
			for (int j = 0; j < listaDocumentoExp.size(); j++) {
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("id-->"+listaDocumentoExp.get(j).getDocumento().getId());
				if (listaDocumentoExp.get(i).getDocumento().getId().intValue() == listaDocumentoExp.get(j).getDocumento().getId().intValue())
					cont = cont + 1;
			}
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("contador** -->"+cont);
			if (cont > 1){
				mensajeErrorPrincipal("tablaDocumentos", "Existen documentos duplicados");
				//Habilitando/Deshabilitando botones Aprobar/Rechazar				
				break;
			}
		}
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("contador -->"+cont);
		if (cont == 1){ // Si no existen documento duplicados 
			DocumentoExp docExp=null;
//			Motivo motivo=new Motivo();
			for (int i = 0; i < listaDocumentoExp.size(); i++) {
				docExp = (DocumentoExp)listaDocumentoExp.get(i);
//				motivo=listaDocumentoExp.get(i).getMotivo();
				//Seteamos el Motivo de Rechazo y Fecha Vigencia en objetos Vacios, 
				//para que no guarde motivo de Rechazo ni Fecha de Vigencia
				//docExp.setFechaVigencia(null); // se graba la fecha propuesta
//				docExp.setMotivo(motivo);
//				docExp.setFlagRechazado(ConstantesAdmin.FLAG_FALSO);
				//fin seteo
//				docExpedienteDAO.update(docExp);
				
				//inicio mod man1
				if(docExp.getNombreArchivo() != null){
					DocumentoExp docExpBD = docExpedienteDAO.load(listaDocumentoExp.get(i).getId());
					Documento doc = new Documento();
					doc.setId(docExp.getDocumento().getId());				
					Expediente exp = new Expediente();
					exp.setId(docExp.getExpediente().getId());
					Motivo mot = new Motivo();
					mot.setId(docExp.getMotivo().getId());
					docExpBD.setDocumento(doc);
					docExpBD.setExpediente(exp);
					docExpBD.setMotivo(mot);
					docExpBD.setFechaVigencia(null);
					docExpBD.setMotivo(null);
					docExpBD.setFlagRechazado(ConstantesAdmin.FLAG_FALSO);
					docExp = docExpedienteDAO.update(docExpBD);
				}
				//fin mod man1
			}
		}
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Actualización satisfactoria", "Cambios aplicados!"));
		
	}
	
	public void ejecutarRechazarDocumentacion(String accion){
		LOG.info("rechazarDocumentacion()");
		rechazarDocumentacion(accion);
	}
	
	
	
	public void rechazarDocumentacion(String accion){		
		LOG.info("grabarInformacion(String accion)");
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("accion-->"+accion);
		
		//Actualizando Documento Expediente
		List<DocumentoExpWrapper> listaDocumentoExpW = verificaDocumentacionDigitalizada.getListaDocExpedienteW();
		List<DocumentoExp> listaDocExpediente = new ArrayList<DocumentoExp>();
		LOG.info("Tamaño lista" + listaDocumentoExpW.size() );
		for (int i = 0; i < listaDocumentoExpW.size(); i++) {
			DocumentoExp documentoExp = listaDocumentoExpW.get(i).getDocExpediente();	
			//existeRechazoSeleccionado = listaDocumentoExpW.get(i).isFlagComboRechazadoFirmas();			
			LOG.info("existeRechazoSeleccionado" + existeRechazoSeleccionado);
			if (listaDocumentoExpW.get(i).isFlagComboRechazadoFirmas()) {
				LOG.info("existeRechazoSeleccionado" + existeRechazoSeleccionado);
				String idMotivo = listaDocumentoExpW.get(i).getIdMotivo();
				LOG.info("idMotivo" + idMotivo);
				if (StringUtils.isNotEmpty(idMotivo)
						&& !ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(idMotivo)
						&& !ConstantesAdmin.CODIGO_NO_SELECCIONADO
								.equals(idMotivo)) {
					Motivo motivo = new Motivo();
					motivo.setId(Integer.parseInt(idMotivo));
					documentoExp.setMotivo(motivo);
					documentoExp.setFlagRechazado(ConstantesAdmin.FLAG_VERDADERO);
					listaDocExpediente.add(documentoExp);
					//break;
				}else {
					LOG.info("Mensaje de error 1, no se selecciono");
					mensajeErrorPrincipal("tablaDocumentos:idMotivo","Seleccione el motivo de rechazo ");
					return;
				}
				//listaDocExpediente.add(documentoExp);
			}
		} 
		LOG.info("tamaño listaDocExpediente" + listaDocExpediente.size());
		if (listaDocExpediente.isEmpty()) {
			LOG.info("existeRechazoSeleccionado" + existeRechazoSeleccionado);
			//documentoExp.setMotivo(null);
			//documentoExp.setFlagRechazado(null);
			LOG.info("Mensaje de error 2, no se selecciono");
			mensajeErrorPrincipal("tablaDocumentos:idFlagRechazar","Seleccionar los documentos a rechazar");
			return;
		}
		LOG.info("Salio del for");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		List<DocumentoExp> docsExp = docExpedienteDAO.getListaDocumentos(expediente.getId());
		for (DocumentoExp de : docsExp) {
			if (!listaDocExpediente.contains(de)) {
				de.setFlagRechazado("0");
				de.setMotivo(null);
				//documentoExpDAO.update(de);
			}
		}
		DocumentoExp docExp = null;
		for (int i = 0; i < listaDocExpediente.size(); i++) {
			docExp = listaDocExpediente.get(i);
			docExpedienteDAO.update(docExp);
		}
		
		expediente.setObservaciones(verificaFirmaObservaciones.getStrObservaciones());
		LOG.info("observaciones" + expediente.getObservaciones());
		expediente.setAccion(accion);	
		
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_RECHAZAR)){
			LOG.info("entro al if de accion");
			EstadoExpediente estExpTemp = estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO); 
			expediente.setEstado(estExpTemp);
			tarea.setId(ConstantesBusiness.ID_TAREA_SUBSANAR_FIRMA);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}
		
		
		Util.generarRegistroHistExp(expediente);
		expediente = expedienteDAO.update(expediente);
		LOG.info("actualizo el expediente");
		
		// para enviar el objeto al process
		LOG.info("para enviar el objeto al process");
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		
		//ParticipeDAO participeDAO = DAOFactory.getInstance().getParticipeDAO();
		/*List<Participe> lista = participeDAO.findByExpedienteParticipesUnicos(expediente.getId());
		String existeFirmaAsociada = ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA;
		String existeFirmaNoAsociada = ConstantesBusiness.NO_EXISTE_FIRMA_NO_ASOCIADA;
		for (Participe p : lista) {
			if (ConstantesBusiness.FLAG_FIRMA_ASOCIADA.equals(p.getFlagFirmaAsociada())) {
				existeFirmaAsociada = ConstantesBusiness.EXISTE_FIRMA_ASOCIADA;
			}
			if (!ConstantesBusiness.FLAG_FIRMA_ASOCIADA.equals(p.getFlagFirmaAsociada())) {
				existeFirmaNoAsociada = ConstantesBusiness.EXISTE_FIRMA_NO_ASOCIADA;
			}
		}
		LOG.info("expedienteCC");
		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaAsociada(existeFirmaAsociada);
		LOG.info("expedienteCC 1");
		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaNoAsociada(existeFirmaNoAsociada);
		LOG.info("expedienteCC 2");*/
		
		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaAsociada(ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA);
		expedienteCC.getDatosFlujoCtaCte().setFlagExisteFirmaNoAsociada(ConstantesBusiness.EXISTE_FIRMA_NO_ASOCIADA);
		
		String tkiid = expedienteCC.getTaskID();
		LOG.info("tkkid" + tkiid.toString());
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
		
		redirectAction("../bandeja/bandeja");
	
	}
	
	public VerificaFirmaObservacionesMB getVerificaFirmaObservaciones() {
		return verificaFirmaObservaciones;
	}
	
	public VerificaDocumentacionDigitalizadaMB getVerificaDocumentacionDigitalizada() {
		return verificaDocumentacionDigitalizada;
	}
	
	public void activarSegunArchivoRechazado (boolean archivoRechazado) {
		LOG.debug("activarSegunArchivoRechazado ({})", archivoRechazado);
		LOG.debug("existeFirmaAsociada: {}", existeFirmaAsociada);
		this.archivoRechazado = archivoRechazado;
		if (archivoRechazado) {
			deshabilitarBoton (false, true);
		} else if (existeFirmaAsociada == null){
			deshabilitarBoton (true, true);
		} else {
			activarSegunFirmaAsociada (existeFirmaAsociada);
		}
	}
	
	public void activarSegunFirmaAsociada (String existeFirmaAsociada) {
		LOG.debug("activarSegunFirmaAsociada ({})", existeFirmaAsociada);
		LOG.debug("archivoRechazado: {}", archivoRechazado);
		this.existeFirmaAsociada = existeFirmaAsociada;
		if (existeFirmaAsociada == null) {
			deshabilitarBoton (true, true);
		} else if (existeFirmaAsociada.equalsIgnoreCase(ConstantesBusiness.EXISTE_FIRMA_ASOCIADA)){
			deshabilitarBoton (true, false);
			archivoRechazado = false;
		} else {
			deshabilitarBoton (false, true);
		}
	}
	
	public void deshabilitarBoton(boolean deshabBtnRechazar,boolean deshabBtnTerminar) {
		LOG.info("estado boton rechazar3 {}",deshabBtnRechazar);
		verificaFirmaObservaciones.setBlnRechazar(deshabBtnRechazar);
		verificaFirmaObservaciones.setBlnTerminar(deshabBtnTerminar);
		
	}
	public void habilitarBotonRechazar(boolean habBtnRechazar) {
		LOG.info("estado boton rechazar3-1 {}",habBtnRechazar);
		verificaFirmaObservaciones.setBlnRechazar(habBtnRechazar);
	}
	public UrlContentManagerMB getUrlContentManager() {
		return urlContentManager;
	}
	public void setUrlContentManager(UrlContentManagerMB urlContentManager) {
		this.urlContentManager = urlContentManager;
	}
	public boolean isErrorSeleccionarMotivoRechazo() {
		return errorSeleccionarMotivoRechazo;
	}
	public void setErrorSeleccionarMotivoRechazo(
			boolean errorSeleccionarMotivoRechazo) {
		this.errorSeleccionarMotivoRechazo = errorSeleccionarMotivoRechazo;
	}
	public void setExisteRechazoSeleccionado(boolean existeRechazoSeleccionado) {
		this.existeRechazoSeleccionado=existeRechazoSeleccionado;
		
	}
	public boolean isExisteRechazoSeleccionado() {
		return existeRechazoSeleccionado;
	}
	
}
