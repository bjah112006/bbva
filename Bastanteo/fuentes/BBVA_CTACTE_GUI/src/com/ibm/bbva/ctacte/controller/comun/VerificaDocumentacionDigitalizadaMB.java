package com.ibm.bbva.ctacte.controller.comun;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IVerificaDocumentacionDigitalizada;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadFirmasMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;


@ManagedBean(name="verificaDocumentacionDigitalizada")
@ViewScoped
public class VerificaDocumentacionDigitalizadaMB extends AbstractMBean{
	
	private static final long serialVersionUID = 8512389245523752041L;
	private static final Logger LOG = LoggerFactory.getLogger(VerificaDocumentacionDigitalizadaMB.class);
	
	@ManagedProperty(value="#{verificarCalidadFirma}")
	private VerificarCalidadFirmasMB verificarCalidadFirmas;
	private IVerificaDocumentacionDigitalizada iVerificaDocumentacionDigitalizada;
	private List<DocumentoExpWrapper> listaDocExpedienteW;
	private List<SelectItem> listaSelectMotivoR;
	private List<SelectItem> listaSelectDocumento;
//	private List<DocumentoHist> listaDocumentoHistorico;
//	private List<DocumentoHist> listaNewDocumentoHistorico;
	private com.ibm.bbva.cm.service.Documento[] documentoHistCM;
	private com.ibm.bbva.cm.service.Documento[] documentoNewHistCM;
	
	//private ;
	private DocumentoExp documentoExpediente;	
	private Expediente expediente;
	private Motivo motivo;
	private boolean existeRechazoSeleccionado;  //true si hay un check de rechazo 
	private boolean errorSeleccionarMotivoRechazo;  //true si NO seleccinó Motivo de rechazo
	public boolean habilitarBotonRechazar;
	public boolean deshabilitarBotonTerminar;
	public boolean flagComboRechazado;
	private int id; // codDocumento
	private int idTarea;
	
	private String abrirArchivo ;
	
	@EJB
	private DocumentoExpDAO docExpDAO;
	@EJB
	private MotivoDAO motivoDAO;
	
	@PostConstruct
	public void iniciar(){		
		LOG.info("iniciar ()");	
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}",pagina);
		if ("formVerificarCalidadFirma".equals(pagina)) {	
			LOG.info("Entro a la pagina{} ",pagina);
			iVerificaDocumentacionDigitalizada = verificarCalidadFirmas;
		}
		iVerificaDocumentacionDigitalizada.setVerificaDocumentacionDigitalizada(this);
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		//DocumentoDAO documentoDAO=DAOFactory.getInstance().getDocumentoDAO();
		LOG.info("expediente.getId()-->"+expediente.getId());
		//listaDocExpediente = docExpDAO.findByExpdiente(expediente.getId());
		List<DocumentoExp> listaDocExpediente = docExpDAO.findByExpdienteEscaneado(expediente.getId(),
				ConstantesBusiness.FLAG_ESCANEADO,ConstantesBusiness.CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD,
				ConstantesBusiness.CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS);
		//listaDocExpediente=docExpDAO.getListaDocumentosCU15(expediente.getId());
		LOG.info("lista tam-->"+listaDocExpediente.size());
		//Evaluando cuantos documentos estarian habilitados para la reTipificacion
		

		
		// Cargando la lista de Documentos
		List<SelectItem> listaSelectDoc = Util.crearItems(listaDocExpediente, false, "documento.id", "documento.descripcion");		
		listaSelectDocumento=listaSelectDoc;
		
		// cargando la lista de Motivos de Rechazo
		//List<Motivo> listaMotivos = motivoDAO.findByTarea(expediente.getTarea().getId(),ConstantesBusiness.TIPO_MOTIVO_RECHAZO_DOCUMENTO);
		List<Motivo> listaMotivos=motivoDAO.findByTarea(ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_FIRMAS, ConstantesBusiness.TIPO_MOTIVO_RECHAZO_DOCUMENTO);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("lista tam Motivos-->"+listaMotivos.size());
		List<SelectItem> listaSelectMov = Util.crearItems(listaMotivos, true, "id", "descripcion");
		//listaSelectMotivoR=Util.crearItems(listaSelectMov,true,"id","descripcion");
		
		ListSorter.ordenarMotivos(listaSelectMov);
		
		setListaSelectMotivoR(listaSelectMov);
		//listaSelectMotivoR=listaSelectMov;		
		//Obteniendo datos para la columna Lista de Archivos Historicos
		
//		DocumentoHistDAO docHistDAO = DAOFactory.getInstance().getDocumentoHistDAO();
//		List<DocumentoHist> listDocumentoHistExp = null;
		com.ibm.bbva.cm.service.Documento[] documentoCM = null;
		ConsultaContentManager consultaCM = new ConsultaContentManager (); 
		listaDocExpedienteW = new ArrayList();
		DocumentoExpWrapper docWrapper = null;
		String strTipoDoc=null;
		
		

		int contador =0;
		String flag;
		for (int i = 0; i < listaDocExpediente.size(); i++) {
//			listDocumentoHistExp = docHistDAO.getListDocHistoricobyClienteDocumento(codCliente,listaDocExpediente.get(i).getDocumento().getId());
			try {
				documentoCM = consultaCM.CM_historialDocumentoxCliente(listaDocExpediente.get(i).getDocumento().getCodigoDocumento(), expediente.getCliente().getCodigoCentral(), ConstantesBusiness.NUM_DOC_HISTORICOS_RECIBIDOS_CM, listaDocExpediente.get(i));
			} catch (Exception e) {
				
				e.printStackTrace();
				LOG.error("Consulta documentos CM :"+expediente.getId());
			}
			//llama a un nuevo constructor			
			docWrapper = new DocumentoExpWrapper(this);
			
			
			//Obtener url content por cada documento
			if (listaDocExpediente.get(i).getFlagEscaneado()!=null){
				if (listaDocExpediente.get(i).getFlagEscaneado().compareTo("1")==0){
					docWrapper.obtenerDocumentoCM(listaDocExpediente.get(i).getDocumento().getCodigoDocumento(),expediente.getId());
				}
			}
			
			//Verificando si el registro debe mostrarse habilitado o dehabilitado
			strTipoDoc = ((DocumentoExp)listaDocExpediente.get(i)).getDocumento().getTipoDocumento();
			flag = ((DocumentoExp)listaDocExpediente.get(i)).getFlagEscaneado(); 
			flag = (flag==null?" ":flag);
			
			if (contador > 1)
				if (strTipoDoc!=null && strTipoDoc.equalsIgnoreCase(ConstantesBusiness.TIPO_DOCUMENTO_LEGAL))
					if (flag.equals(ConstantesAdmin.FLAG_VERDADERO)){	
						    //valida si el documento fue escaneado para el Exp en curso
						if(expediente.getFechaRegistro().before(listaDocExpediente.get(i).getFechaEscaneo()) || expediente.getFechaRegistro().compareTo(listaDocExpediente.get(i).getFechaEscaneo()) == 0 ){
								docWrapper.setHabilitarDocumento(false);
							}
					}
			docWrapper.setFechaVigenciaDocumento(listaDocExpediente.get(i).getFechaVigencia());
			docWrapper.setDocExpediente(listaDocExpediente.get(i));
//			docWrapper.setListaDocumentoHistorico(listDocumentoHistExp);
//			documentoCM.setCodigoDocumento(docWrapper.getDocExpediente().getDocumento().getCodigoDocumento());
			docWrapper.setDocumentoHistCM(documentoCM);
			listaDocExpedienteW.add(docWrapper);
		}
		//Fin Obteniendo datos para la columna Lista de Archivos Historicos		
	}
	
	public void verificarSeleccionMotivoRechazo(){
		LOG.info("verificarSeleccionMotivoR()");
		iVerificaDocumentacionDigitalizada.setErrorSeleccionarMotivoRechazo(false);
		iVerificaDocumentacionDigitalizada.setExisteRechazoSeleccionado(false);
		boolean existeCheckSeleccionado = false;
		for (int i = 0; i < listaDocExpedienteW.size(); i++) {
			LOG.info("RECHAZO-->"+listaDocExpedienteW.get(i).isFlagComboRechazadoFirmas());
			LOG.info("MOTIVO-->"+listaDocExpedienteW.get(i).isMotivoRechazoSeleccionado());
			if(listaDocExpedienteW.get(i).isFlagComboRechazadoFirmas() == true ){
				iVerificaDocumentacionDigitalizada.setExisteRechazoSeleccionado(true);
				existeCheckSeleccionado = true;
				if (listaDocExpedienteW.get(i).isMotivoRechazoSeleccionado() == false){
					iVerificaDocumentacionDigitalizada.setErrorSeleccionarMotivoRechazo(true);
					break;
				}
			}
		}
		
		iVerificaDocumentacionDigitalizada.activarSegunArchivoRechazado(existeCheckSeleccionado);
		
////		actualizarBotonRechazar();
	}
	
	public void actualizarBotonRechazar(){
		LOG.info("actualizarBotonRechazar()");
		boolean existeRechazo=false;
		for (int i = 0; i < listaDocExpedienteW.size(); i++) {
			 if (listaDocExpedienteW.get(i).isFlagComboRechazado()){
				 existeRechazo=true;
				 break;
			 }
		}
		if(existeRechazo){ // existe al menos uno checado
	    	//boton rechazar habilitar
	    	//terminar vinculacion deshabilitar	    	
			actualizarBoton(false,true);
		}else
			actualizarBoton(true,false);
	}	
	
	public void actualizarBoton(boolean estadoBtnRechazar,boolean estadoBtnTerminar ){
		LOG.info("estado boton estadoBtnRechazar"+estadoBtnRechazar);
		LOG.info("estado boton estadoBtnTerminar"+estadoBtnTerminar);
		iVerificaDocumentacionDigitalizada.deshabilitarBoton(estadoBtnRechazar,estadoBtnTerminar);
		
	}
	
	public void abrirDocumento (ActionEvent ae) {
		LOG.info("abrirDocumento (ActionEvent ae)");
		String docum = getRequestParameter("documento");
		abrirArchivo = docum;
	}
	
	public DocumentoExp getDocumentoExpediente() {
		return documentoExpediente;
	}

	public void setDocumentoExpediente(DocumentoExp documentoExpediente) {
		this.documentoExpediente = documentoExpediente;
	}

	public boolean isExisteRechazoSeleccionado() {
		return existeRechazoSeleccionado;
	}

	public void setExisteRechazoSeleccionado(boolean existeRechazoSeleccionado) {
		this.existeRechazoSeleccionado = existeRechazoSeleccionado;
	}

	public boolean isErrorSeleccionarMotivoRechazo() {
		return errorSeleccionarMotivoRechazo;
	}

	public void setErrorSeleccionarMotivoRechazo(
			boolean errorSeleccionarMotivoRechazo) {
		this.errorSeleccionarMotivoRechazo = errorSeleccionarMotivoRechazo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void aplicarCambioTipoDocumento(ActionEvent ae){
		LOG.info("aplicarCambioTipoDocumento(ActionEvent ae)");
	/*//	Util.generarRegistroHistExp(expediente);	
		motivo.setId(idMotivo);
		LOG.info("id Motivo : "+motivo.getId());
		expediente.setMotivo(motivo);
		ExpedienteDAO expedienteDAO = DAOFactory.getInstance().getExpedienteDAO();
		expedienteDAO.update(expediente);*/
		
		LOG.info("aplicarCambioTipoDocumento(ActionEvent action)");
		iVerificaDocumentacionDigitalizada.aplicarCambioTipoDocumento(getListaDocExpedienteW());
	}

	public VerificarCalidadFirmasMB getVerificarCalidadFirmas() {
		return verificarCalidadFirmas;
	}

	public void setVerificarCalidadFirmas(
			VerificarCalidadFirmasMB verificarCalidadFirmas) {
		this.verificarCalidadFirmas = verificarCalidadFirmas;
	}

	public IVerificaDocumentacionDigitalizada getIVerificaDocumentacionDigitalizada() {
		return iVerificaDocumentacionDigitalizada;
	}

	public void setIVerificaDocumentacionDigitalizada(
			IVerificaDocumentacionDigitalizada verificaDocumentacionDigitalizada) {
		iVerificaDocumentacionDigitalizada = verificaDocumentacionDigitalizada;
	}

	public List<DocumentoExpWrapper> getListaDocExpedienteW() {
		return listaDocExpedienteW;
	}

	public void setListaDocExpedienteW(List<DocumentoExpWrapper> listaDocExpedienteW) {
		this.listaDocExpedienteW = listaDocExpedienteW;
	}

	public List<SelectItem> getListaSelectMotivoR() {
		return listaSelectMotivoR;
	}

	public void setListaSelectMotivoR(List<SelectItem> listaSelectMotivoR) {
		this.listaSelectMotivoR = listaSelectMotivoR;
	}

	public List<SelectItem> getListaSelectDocumento() {
		return listaSelectDocumento;
	}

	public void setListaSelectDocumento(List<SelectItem> listaSelectDocumento) {
		this.listaSelectDocumento = listaSelectDocumento;
	}

//	public List<DocumentoHist> getListaDocumentoHistorico() {
//		return listaDocumentoHistorico;
//	}
//
//	public void setListaDocumentoHistorico(
//			List<DocumentoHist> listaDocumentoHistorico) {
//		this.listaDocumentoHistorico = listaDocumentoHistorico;
//	}
//
//	public List<DocumentoHist> getListaNewDocumentoHistorico() {
//		return listaNewDocumentoHistorico;
//	}
//
//	public void setListaNewDocumentoHistorico(
//			List<DocumentoHist> listaNewDocumentoHistorico) {
//		this.listaNewDocumentoHistorico = listaNewDocumentoHistorico;
//	}
//
//	public List<DocumentoExp> getListaDocExpediente() {
//		return listaDocExpediente;
//	}
//
//	public void setListaDocExpediente(List<DocumentoExp> listaDocExpediente) {
//		this.listaDocExpediente = listaDocExpediente;
//	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public String getAbrirArchivo() {
		return abrirArchivo;
	}

	public void setAbrirArchivo(String abrirArchivo) {
		this.abrirArchivo = abrirArchivo;
	}

	
	

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}
	
	public boolean isHabilitarBotonRechazar() {
		return habilitarBotonRechazar;
	}

	public void setHabilitarBotonRechazar(boolean habilitarBotonRechazar) {
		this.habilitarBotonRechazar = habilitarBotonRechazar;
	}

	public boolean isDeshabilitarBotonTerminar() {
		return deshabilitarBotonTerminar;
	}

	public void setDeshabilitarBotonTerminar(boolean deshabilitarBotonTerminar) {
		this.deshabilitarBotonTerminar = deshabilitarBotonTerminar;
	}

	public com.ibm.bbva.cm.service.Documento[] getDocumentoHistCM() {
		return documentoHistCM;
	}

	public void setDocumentoHistCM(com.ibm.bbva.cm.service.Documento[] documentoHistCM) {
		this.documentoHistCM = documentoHistCM;
	}

	public com.ibm.bbva.cm.service.Documento[] getDocumentoNewHistCM() {
		return documentoNewHistCM;
	}

	public void setDocumentoNewHistCM(
			com.ibm.bbva.cm.service.Documento[] documentoNewHistCM) {
		this.documentoNewHistCM = documentoNewHistCM;
	}
}
