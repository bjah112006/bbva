package com.ibm.bbva.ctacte.wrapper;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.DocumentacionDigitalizadaMB;
import com.ibm.bbva.ctacte.controller.comun.VerificaDocumentacionDigitalizadaMB;

public class DocumentoExpWrapper extends AbstractMBean  implements Serializable {
	
	private static final Logger LOG = LoggerFactory.getLogger(DocumentoExpWrapper.class);
	private DocumentacionDigitalizadaMB docDigitalizada;
	private VerificaDocumentacionDigitalizadaMB verificaDocDigitalizada;
	private DocumentoExp docExpediente;
//	private List<DocumentoHist> listaDocumentoHistorico;     comentado el 30/07
	private com.ibm.bbva.cm.service.Documento[] documentoHistCM;
//	private List<DocumentoHist> listaNewDocumentoHistorico; //agregados en la migracion masiva
	private boolean mostrarBotonMerge=false;
	private boolean desHabilitarBtnMerge;

	private boolean mostrarFechaVigencia=false;
	private boolean habilitarOpcionRechazo=true;
	private boolean habilitarOpRechazo=false;
	private boolean habilitarDocumento=false;
	private boolean newDocHistorial=false;
	private Date nomArchivoHistPropuesto=null;
	private Date maximoPermitido=null;
	private Date fechaVigenciaDocumento; // fecha que contiene la fecha de vigencia de BD
	private String mensajeErrorFechaVigencia;
	private String idComponente;
	
	//Inidicadores de error relacionado al objeto DocumentoExpWrapper
	private boolean errorFechaVigencia; //Indica si existe error en la fecha de vigencia del doc
	private boolean motivoRechazoSeleccionado; //true: Si esta marcado el check Rechazo y se tiene un motivo seleccionado
											   //false: Si esta marcado el check Rechazo y NO se tiene un motivo de rechazo seleccionado

	private boolean documentoCombinado; // true: Si el documento ha sido combinado
	 									// false: No ha sido Combinado
	
	private String idMotivo = ConstantesAdmin.CODIGO_NO_SELECCIONADO;
	private String escaneadoHistorico= ConstantesAdmin.SUJITO_HISTORIAL;;
	private String idCMHistoricoReciente;
	private String nombreFile; // nombre del archivo en la carpeta de windows   DOID0-HIST.pdf
	private String nombreFileFinal; // nombre del archivo en la carpeta de windows completo   DOID0-HIST-12982012123456.pdf

	private String rutaCM;
	private Integer nroPagina; // El número de página del último documento histórico
	
	public void obtenerDocumentoCM(String codigoDocumento, int idExpediente){
		LOG.info("public void obtenerDocumentoCM(String codigoDocumento, int idExpediente)");
		ConsultaContentManager consultaCM = new ConsultaContentManager ();
		LOG.info("codigoDocumento: " + codigoDocumento);
		LOG.info("idExpediente: " + idExpediente);
		com.ibm.bbva.cm.service.Documento documento= consultaCM.CM_ObtenerDocumentItemType(codigoDocumento,idExpediente);
		if (documento != null){
			rutaCM=documento.getUrlContent();
		}
		
		LOG.info("rutaCM:"+rutaCM);

		LOG.info("ruta CM: " + rutaCM);
	}

	public String getNombreFileFinal() {
		LOG.info("nombreFile: "+nombreFile);
		LOG.info("nomArchivoHistPropuesto: "+parseDateString(nomArchivoHistPropuesto));
		//return nombreFileFinal;
		return nombreFile.concat("-").concat(parseDateString(nomArchivoHistPropuesto));
	}

	public void setNombreFileFinal(String nombreFileFinal) {
		this.nombreFileFinal = nombreFileFinal;
	}

	public String getNombreFile() {
		return nombreFile;
	}

	public void setNombreFile(String nombreFile) {
		this.nombreFile = nombreFile;
		try{
			this.nombreFileFinal = nombreFile.concat("-").concat(docExpediente.getNombreArchivo());
		}catch(Exception e){e.getStackTrace();}
	}

	public void setIdCMHistoricoReciente(String idCMHistoricoReciente) {
		this.idCMHistoricoReciente = idCMHistoricoReciente;
	}


	public com.ibm.bbva.cm.service.Documento[] getDocumentoHistCM() {
		LOG.info("DocumentoExpWrapper [Codigo Documento="+this.getDocExpediente().getDocumento().getCodigoDocumento()+"] -> getDocumentoHistCM():"+((documentoHistCM == null)?"NULL":documentoHistCM.length));
		return documentoHistCM;
	}


	public void setDocumentoHistCM(com.ibm.bbva.cm.service.Documento[] documentoHistCM) {
		this.documentoHistCM = documentoHistCM;
	}


	public String getEscaneadoHistorico() {
		return escaneadoHistorico;
	}

	public void setEscaneadoHistorico(String escaneadoHistorico) {
		this.escaneadoHistorico = escaneadoHistorico;
	}

	public boolean isDesHabilitarBtnMerge() {
		//if(listaDocumentoHistorico.size() == 0 )
		LOG.info("Inicio isDesHabilitarBtnMerge()");
		LOG.info("Esta escaneado..."+docExpediente.getFlagEscaneado());
		
		if(docExpediente.getFlagEscaneado() == null || ConstantesAdmin.FLAG_FALSO.equals(docExpediente.getFlagEscaneado()) || documentoHistCM == null || documentoHistCM.length == 0){
			desHabilitarBtnMerge = true;
//			if ((docExpediente.getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD)
//					|| docExpediente.getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS))
//					&& documentoHistCM != null && documentoHistCM.length > 0 ) {
//				desHabilitarBtnMerge = false;
//			}
		}else{
			desHabilitarBtnMerge = false;
		}
		
		LOG.info("desHabilitarBtnMerge.."+desHabilitarBtnMerge);
		LOG.info("Fin isDesHabilitarBtnMerge()");
		return desHabilitarBtnMerge;
	}

	public void setDesHabilitarBtnMerge(boolean desHabilitarBtnMerge) {
		this.desHabilitarBtnMerge = desHabilitarBtnMerge;
	}
	
	public String getIdMotivo() {
		if (isDocumentoLegal() && 
			ConstantesAdmin.FLAG_VERDADERO.equals(getDocExpediente().getFlagRechazado()) && 
			getDocExpediente().getMotivo() != null )
			return getDocExpediente().getMotivo().getId()+"";
		return idMotivo;
	}

	public void setIdMotivo(String idMotivo) {
		this.idMotivo = idMotivo;
	}

	public DocumentoExpWrapper(DocumentacionDigitalizadaMB doc) {
		this.docDigitalizada = doc;
	}
	
	public DocumentoExpWrapper(VerificaDocumentacionDigitalizadaMB doc){
		this.verificaDocDigitalizada=doc;
	}
	
	public boolean isMotivoRechazoSeleccionado() {
		return motivoRechazoSeleccionado;
	}

	public void setMotivoRechazoSeleccionado(boolean motivoRechazoSeleccionado) {
		this.motivoRechazoSeleccionado = motivoRechazoSeleccionado;
	}

	public String getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(String idComponente) {
		this.idComponente = idComponente;
	}
	
	public String getMensajeErrorFechaVigencia() {
		return mensajeErrorFechaVigencia;
	}

	public void setMensajeErrorFechaVigencia(String mensajeErrorFechaVigencia) {
		this.mensajeErrorFechaVigencia = mensajeErrorFechaVigencia;
	}
	
	public boolean isErrorFechaVigencia() {
		return errorFechaVigencia;
	}

	public void setErrorFechaVigencia(boolean errorFechaVigencia) {
		this.errorFechaVigencia = errorFechaVigencia;
	}

	public Date getMaximoPermitido() {
		DateTime fecEscanDocHistIni = new DateTime (getDocExpediente().getFechaEscaneo());
		maximoPermitido = fecEscanDocHistIni.minusDays(1).toDate();
		return maximoPermitido;
	}

	public void setMaximoPermitido(Date maximoPermitido) {
			
		this.maximoPermitido = maximoPermitido;
	}

	public Date getNomArchivoHistPropuesto() {
		return nomArchivoHistPropuesto;
	}

		
	public boolean isDocumentoCombinado() {
		return documentoCombinado;
	}

	public void setDocumentoCombinado(boolean documentoCombinado) {
		this.documentoCombinado = documentoCombinado;
	}

	public void setNomArchivoHistPropuesto(Date nomArchivoHistPropuesto) {
		LOG.info("setNomArchivoHistPropuesto(Date nomArchivoHistPropuesto)");
		LOG.info("nomArchivoHistPropuesto-->"+nomArchivoHistPropuesto);
		//Actualizando el nombre del archivo del documento historico
		com.ibm.bbva.cm.service.Documento docHist = null;
		if(getDocumentoHistCM() !=null && getDocumentoHistCM().length > 0 ){
			LOG.info("actualizando el nombre del DOc Historico");
			docHist = getDocumentoHistCM()[0];
			docHist.setNombreArchivo(parseDateString(nomArchivoHistPropuesto));
			getDocumentoHistCM()[0] = docHist;
		}
		this.nomArchivoHistPropuesto = nomArchivoHistPropuesto;
		
		
	}

	public boolean isNewDocHistorial() {
		return newDocHistorial;
	}

	public void setNewDocHistorial(boolean newDocHistorial) {
		if (newDocHistorial)
			escaneadoHistorico = ConstantesAdmin.SUJITO_HISTORIAL;
		this.newDocHistorial = newDocHistorial;
	}

	public void setHabilitarOpcionRechazo(boolean habilitarOpcionRechazo) {
		this.habilitarOpcionRechazo = habilitarOpcionRechazo;
	}
	
	public boolean isHabilitarOpcionRechazo() {
		//if (isDocumentoLegalEscaneado())
		if (isDocumentoLegal())
			setHabilitarOpcionRechazo(false); // debe permitir rechazar documentos que no han sido adjuntados
		return habilitarOpcionRechazo;
	}
	

	public boolean isHabilitarOpRechazo() {
		return habilitarOpRechazo;
	}

	public void setHabilitarOpRechazo(boolean habilitarOpRechazo) {
		this.habilitarOpRechazo = habilitarOpRechazo;
	}

	public boolean isFlagObligatorio() {
		return ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagObligatorio()) ? 
				 true : false;
	}

	public void setFlagObligatorio(boolean flagObligatorio) {
		docExpediente.setFlagObligatorio(flagObligatorio ? ConstantesAdmin.FLAG_VERDADERO : ConstantesAdmin.FLAG_FALSO);
	}
	
	public boolean isFlagReqEscaneo() {
		return ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagReqEscaneo()) ? 
				 true : false;
	}

	public void setFlagReqEscaneo(boolean flagReqEscaneo) {
		docExpediente.setFlagReqEscaneo(flagReqEscaneo ? ConstantesAdmin.FLAG_VERDADERO : ConstantesAdmin.FLAG_FALSO);
	}
	
	public boolean isDocumentoLegal(){
		LOG.info("isDocumentoLegal()");
		if (docExpediente.getDocumento().getTipoDocumento() != null && 
			docExpediente.getDocumento().getTipoDocumento().equalsIgnoreCase(ConstantesBusiness.TIPO_DOCUMENTO_LEGAL)) {
		    return true;
		} else {
			return false;
		}
	}
	
	public boolean isDocumentoLegalEscaneado(){
		LOG.info("isDocumentoLegalEscaneado()");
		if (docExpediente.getDocumento().getTipoDocumento() != null && 
			docExpediente.getDocumento().getTipoDocumento().equalsIgnoreCase(ConstantesBusiness.TIPO_DOCUMENTO_LEGAL) &&
		    ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagEscaneado())) {
			LOG.info("true");
		    return true;
		} else {
			LOG.info("false");
			return false;
		}
		
	}
	
	public boolean isDocumentoLegalEscaneadoFirmas(){
		LOG.info("isDocumentoLegalEscaneadoFirmas()");
		if (docExpediente.getDocumento().getTipoDocumento() == null &&			
		    ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagEscaneado())) {
			LOG.info("true");
		    return true;
		} else {
			LOG.info("false");
			return false;
		}
		
	}

	public boolean isFlagComboRechazado() {
		LOG.info("isFlagComboRechazado() ");
		//if(isDocumentoLegalEscaneado() && ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagRechazado()) ){
		if(isDocumentoLegal() && ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagRechazado()) ){
			LOG.info("true");
			return true;
		}
		LOG.info("false");
		return false;
	}
/*  INICIO AGREGADO PARA MESA DE FIRMAS */	
	public boolean isFlagComboRechazadoFirmas(){
		LOG.info("docExpediente.getFlagRechazado() " + docExpediente.getFlagRechazado());
		if(ConstantesAdmin.FLAG_VERDADERO.equals(docExpediente.getFlagRechazado()) ){
//			docExpediente.setFlagRechazado(null);
			return true;
		}
		return false;
	}
	
	public void setFlagComboRechazadoFirmas(boolean flagRechazadoFirmas) {
		LOG.info("flagRechazadoFirmas" + flagRechazadoFirmas);
		if (flagRechazadoFirmas)
			docExpediente.setFlagRechazado("1");
		else
			docExpediente.setFlagRechazado(null);
	}	
	
/*FIN AGREGADO PARA MESA DE FIRMAS*/

	public void setFlagComboRechazado(boolean flagRechazado) {
		if (flagRechazado)
			docExpediente.setFlagRechazado("1");
		else
			docExpediente.setFlagRechazado(null);
	}	

	public boolean isMostrarFechaVigencia() {
		//Para ver si muestro la caja de fecha de vigencia de los documentos actuales escaneados
		if (docExpediente.getDocumento().getFlagVigencia() !=null &&
				docExpediente.getDocumento().getFlagVigencia().equals(ConstantesAdmin.FLAG_VERDADERO)){
			setMostrarFechaVigencia(true);
		}
		return mostrarFechaVigencia;
	}
	
	public void setMostrarFechaVigencia(boolean mostrarFechaVigencia) {
		this.mostrarFechaVigencia = mostrarFechaVigencia;
	}
	
	public void setHabilitarDocumento(boolean habilitarDocumento) {
		this.habilitarDocumento = habilitarDocumento;
	}
	public boolean isHabilitarDocumento() {
		return habilitarDocumento;
	}
	public DocumentoExp getDocExpediente() {
		return docExpediente;
	}
	public boolean isMostrarBotonMerge() {
		if(docExpediente.getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_COPIA_LITERAL))
//		if(docExpediente.getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_COPIA_LITERAL)
//			|| docExpediente.getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD)
//			|| docExpediente.getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS))
			mostrarBotonMerge = true;
		return mostrarBotonMerge;
	}
	
	public void setDocExpediente(DocumentoExp docExpediente) {
		this.docExpediente = docExpediente;
		setMotivoRechazoSeleccionado(docExpediente.getMotivo()!=null);
	}

	public Date getFechaVigenciaDocumento() {
		return fechaVigenciaDocumento;
	}

	public void setFechaVigenciaDocumento(Date fechaVigenciaDocumento) {
		this.fechaVigenciaDocumento = fechaVigenciaDocumento;
	}
	
	public void validarFechaVigencia(AjaxBehaviorEvent value) {
		//Date strNewValue = value.getDate();
		Date strNewValue = getDocExpediente().getFechaVigencia();
		String strTipoDoc = (docExpediente.getDocumento().getTipoDocumento()==null? "":docExpediente.getDocumento().getTipoDocumento());
		LOG.info("strNewValue-->"+strNewValue);
		LOG.info("fechaVigenciaDocumento-->"+getFechaVigenciaDocumento());
		LOG.info("-->"+value.getComponent().getId());
		String strIdComponente = value.getComponent().getParent().getClientId();
		strIdComponente = strIdComponente.substring(5);
		
		setIdComponente(strIdComponente);
		
		if (strNewValue.after(getFechaVigenciaDocumento())){
			setMensajeErrorFechaVigencia( "La fecha del documento " +
					strTipoDoc + " es mayor a la vigencia máxima de " + docExpediente.getDocumento().getVigenciaDias() + " días");
			mensajeErrorPrincipal(getIdComponente(),  mensajeErrorFechaVigencia);
			setErrorFechaVigencia(true);
		}else if (strNewValue.before(new Date())){
			setMensajeErrorFechaVigencia( "La fecha de vigencia del documento " +
					strTipoDoc + " no puede ser menor o igual a hoy ");
			mensajeErrorPrincipal(getIdComponente(),  mensajeErrorFechaVigencia);
			setErrorFechaVigencia(true);
			LOG.info("MUESTRA ENSAJE-->"+getIdComponente());
		}else{
			setMensajeErrorFechaVigencia("");
			mensajeErrorPrincipal(getIdComponente(), mensajeErrorFechaVigencia);
			setErrorFechaVigencia(false);
		}
		docDigitalizada.buscarErrorFechaVigencia();
			
	}
	
	public void verificaSeleccionMotivoR(AjaxBehaviorEvent event){
		LOG.info("verificaSeleccionMotivoR()");
		setMotivoRechazoSeleccionado(false);
		if ( this.isFlagComboRechazado() == true ){ // si esta checado
			LOG.info("Esta checado");
		}else{
			setIdMotivo(ConstantesAdmin.CODIGO_NO_SELECCIONADO);
			getDocExpediente().setMotivo(null);
			LOG.info("NOOO Esta checado");
		}
		//Recorre todos los checks buscando si existe uno marcado en donde NO tiene motivo de Rechazo seleccionado
		docDigitalizada.verificarSeleccionMotivoR();
	}
	
	// PARA VERIFICAR CALIDAD DE FIRMAS 	
	public void verificaSeleccionMotivoRechazo(AjaxBehaviorEvent event){
		LOG.info("verificaSeleccionMotivoRechazo()");
		setMotivoRechazoSeleccionado(false);
		if ( this.isFlagComboRechazadoFirmas() == true ){ // si esta checado
			LOG.info("Esta checado");
		}else{
			setIdMotivo(ConstantesAdmin.CODIGO_CAMPO_VACIO);
			getDocExpediente().setMotivo(null);
			LOG.info("NOOO Esta checado");
		}
		//Recorre todos los checks buscando si existe uno marcado en donde NO tiene motivo de Rechazo seleccionado
		verificaDocDigitalizada.verificarSeleccionMotivoRechazo();
	}
	
	
	public void seleccionarMotivo (ValueChangeEvent ae) {
		LOG.info("seleccionarMotivo (ValueChangeEvent ae)");
		String strValue = (String)ae.getNewValue();
		LOG.info("strValue-->"+strValue);
		strValue = (strValue==null?"0":strValue);
		
		
		//Si se seleccionó cualquier valor que no sea campo vacío
		if (!strValue.equalsIgnoreCase(ConstantesAdmin.CODIGO_NO_SELECCIONADO)){
			//Actualizando el valor idMotivo en DocExpediente
			setIdMotivo(strValue);		
			Motivo motivo = new Motivo();
			motivo.setId(Integer.valueOf(strValue));
			getDocExpediente().setMotivo(motivo);
			setMotivoRechazoSeleccionado(true);
		}else{
			getDocExpediente().setMotivo(null);
			setMotivoRechazoSeleccionado(false);
		}
		//Recorre todos los checks buscando si existe uno marcado en donde NO tiene motivo de Rechazo seleccionado
		docDigitalizada.verificarSeleccionMotivoR();

	}
	
	public String parseDateString(Date fecha){
		LOG.info("parseDateString(Date fecha)");
		String fechaTexto=null;
		
		if (fecha !=null){
			Format formatter;
			formatter = new SimpleDateFormat("yyyyMMddHHmmss");  
			fechaTexto = formatter.format(fecha);
		}
		LOG.info("fechaTexto-->"+fechaTexto);
		return fechaTexto;
	}

	public void actualizarMensajeError(AjaxBehaviorEvent event){	
		LOG.info("actualizarMensajeError()");
		mensajeErrorPrincipal("idDocDigitalizada:tablaDocumentos", "");
	}

	public String getRutaCM() {
		return rutaCM;
	}

	public void setRutaCM(String rutaCM) {
		this.rutaCM = rutaCM;
	}

	public void setVerificaDocDigitalizada(VerificaDocumentacionDigitalizadaMB verificaDocDigitalizada) {
		this.verificaDocDigitalizada = verificaDocDigitalizada;
	}

	public VerificaDocumentacionDigitalizadaMB getVerificaDocDigitalizada() {
		return verificaDocDigitalizada;
	}

	public Integer getNroPagina() {
		return nroPagina;
	}

	public void setNroPagina(Integer nroPagina) {
		this.nroPagina = nroPagina;
	}
	
}
