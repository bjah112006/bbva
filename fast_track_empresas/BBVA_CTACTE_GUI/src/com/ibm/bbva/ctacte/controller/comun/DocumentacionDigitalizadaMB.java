package com.ibm.bbva.ctacte.controller.comun;

import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EscaneoWeb;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Mensaje;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDocumentacionDigitalizada;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadDocumentoMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.EscaneoWebDAO;
import com.ibm.bbva.ctacte.dao.MensajeDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;

@ManagedBean (name="documentaciondigitalizada")
@ViewScoped
public class DocumentacionDigitalizadaMB extends AbstractMBean {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(DocumentacionDigitalizadaMB.class);

	@ManagedProperty ("#{verificarCalidadDocumentos}")
	private VerificarCalidadDocumentoMB verificarCalidadDocumentos;
	private IDocumentacionDigitalizada managedBean;
	private List<DocumentoExpWrapper> listaDocExpedienteW;
	private List<SelectItem> listaSelectDocumento;
	private List<SelectItem> listaSelectDocumentoRetipif;
	private DocumentoExp[] documentoNewHistCM;
	private List<DocumentoExp> listaDocExpediente;
	private boolean mostrarBotonesEscaneo = false;
	private boolean mostrarBotonRetipificacion = false;
	private boolean desHabilitarBtnEscaneo;
	private boolean desHabilitarBtnEliminar;
	private boolean desHabilitarBtnRetipificar;
	private boolean desHabilitarBtnMerge;
	private String validacionColumnaFechaVigencia;
	private int idTareaActual;
	private int numeroTareasExpediente;
	private Expediente expediente;
	private List<Motivo> listaMotivos;
	private String codigoDocCombinado;
	private String textoAdvertenciaDocumentoCombinado;

	//Applet
	private String tramaDocumentos;
	private String tramaDescarga;
	private String tramaDescargaArcHist;
	private Integer idExpediente;
	private boolean iniciarApplet;
	private String eliminarArchivo;
	private String abrirArchivo;
	private boolean mostApplet = false;
	private String listaDocumentos;
	private String formOrigen;
	private boolean disLinkVistaUnica;
	private EscaneoWeb escaneoWeb;
	private Empleado empleado;
	private String cleanTransferDir;
	
	@EJB
	private EscaneoWebDAO ewDAO;
	@EJB
	private DocumentoExpDAO docExpDAO;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private MensajeDAO mensajeDAO;
	
	private boolean mostrarTablaDocDigitalizada = true;
	
	public void setMostrarTablaDocDigitalizada(boolean mostrarTablaDocDig) {
		this.mostrarTablaDocDigitalizada = mostrarTablaDocDig;
	}
	
	public boolean isMostrarTablaDocDigitalizada() {
		return mostrarTablaDocDigitalizada;
	}
	
	
	public List<Motivo> getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(List<Motivo> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public String getCodigoDocCombinado() {
		return codigoDocCombinado;
	}

	public void setCodigoDocCombinado(String codigoDocCombinado) {
		this.codigoDocCombinado = codigoDocCombinado;
	}

	public String getFormOrigen() {
		return formOrigen;
	}

	public void setFormOrigen(String formOrigen) {
		this.formOrigen = formOrigen;
	}

	public String getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(String listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public boolean isMostApplet() {
		return mostApplet;
	}

	public void setMostApplet(boolean mostApplet) {
		this.mostApplet = mostApplet;
	}

	public boolean isIniciarApplet() {
		return iniciarApplet;
	}

	public void setIniciarApplet(boolean iniciarApplet) {
		this.iniciarApplet = iniciarApplet;
	}

	public String getEliminarArchivo() {
		return eliminarArchivo;
	}

	public void setEliminarArchivo(String eliminarArchivo) {
		this.eliminarArchivo = eliminarArchivo;
	}

	public String getAbrirArchivo() {
		return abrirArchivo;
	}

	public void setAbrirArchivo(String abrirArchivo) {
		this.abrirArchivo = abrirArchivo;
	}

	public String getTramaDocumentos() {
		return tramaDocumentos;
	}

	public void setTramaDocumentos(String tramaDocumentos) {
		this.tramaDocumentos = tramaDocumentos;
	}

	public int getIdTareaActual() {
		return idTareaActual;
	}

	public void setIdTareaActual(int idTareaActual) {
		this.idTareaActual = idTareaActual;
	}

	public int getNumeroTareasExpediente() {
		return numeroTareasExpediente;
	}

	public void setNumeroTareasExpediente(int numeroTareasExpediente) {
		this.numeroTareasExpediente = numeroTareasExpediente;
	}

	public boolean isDesHabilitarBtnRetipificar() {
		return desHabilitarBtnRetipificar;
	}

	public void setDesHabilitarBtnRetipificar(boolean desHabilitarBtnRetipificar) {
		this.desHabilitarBtnRetipificar = desHabilitarBtnRetipificar;
	}

	public boolean isDesHabilitarBtnMerge() {
		return desHabilitarBtnMerge;
	}

	public void setDesHabilitarBtnMerge(boolean desHabilitarBtnMerge) {
		this.desHabilitarBtnMerge = desHabilitarBtnMerge;
	}

	public boolean isDesHabilitarBtnEliminar() {
		return desHabilitarBtnEliminar;
	}

	public void setDesHabilitarBtnEliminar(boolean desHabilitarBtnEliminar) {
		this.desHabilitarBtnEliminar = desHabilitarBtnEliminar;
	}

	public boolean isDesHabilitarBtnEscaneo() {
		return desHabilitarBtnEscaneo;
	}

	public void setDesHabilitarBtnEscaneo(boolean desHabilitarBtnEscaneo) {
		this.desHabilitarBtnEscaneo = desHabilitarBtnEscaneo;
	}

	public boolean isMostrarBotonRetipificacion() {
		return mostrarBotonRetipificacion;
	}

	public void setMostrarBotonRetipificacion(boolean mostrarBotonRetipificacion) {
		this.mostrarBotonRetipificacion = mostrarBotonRetipificacion;
	}

	public void setMostrarBotonesEscaneo(boolean mostrarBotonesEscaneo) {
		this.mostrarBotonesEscaneo = mostrarBotonesEscaneo;
	}

	public boolean isMostrarBotonesEscaneo() {
		return mostrarBotonesEscaneo;
	}	
	
	public List<DocumentoExpWrapper> getListaDocExpedienteW() {
		return listaDocExpedienteW;
	}

	public void setListaDocExpedienteW(List<DocumentoExpWrapper> listaDocExpedienteW) {
		this.listaDocExpedienteW = listaDocExpedienteW;
	}

	public List<SelectItem> getListaSelectDocumento() {
		return listaSelectDocumento;
	}

	public void setListaSelectDocumento(List<SelectItem> listaSelectDocumento) {
		this.listaSelectDocumento = listaSelectDocumento;
	}

	public List<DocumentoExp> getListaDocExpediente() {
		return listaDocExpediente;
	}

	public void setListaDocExpediente(List<DocumentoExp> listaDocExpediente) {
		this.listaDocExpediente = listaDocExpediente;
	}

	public VerificarCalidadDocumentoMB getVerificarCalidadDocumentos() {
		return verificarCalidadDocumentos;
	}

	public void setVerificarCalidadDocumentos(
			VerificarCalidadDocumentoMB verificarCalidadDocumentos) {
		this.verificarCalidadDocumentos = verificarCalidadDocumentos;
	}
	
	public void setTramaDescarga(String tramaDescarga) {
		this.tramaDescarga = tramaDescarga;
	}

	public String getTramaDescarga() {
		return tramaDescarga;
	}

	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}

	public Integer getIdExpediente() {
		return idExpediente;
	}
	
	public List<SelectItem> getListaSelectDocumentoRetipif() {
		return listaSelectDocumentoRetipif;
	}

	public void setListaSelectDocumentoRetipif(
			List<SelectItem> listaSelectDocumentoRetipif) {
		this.listaSelectDocumentoRetipif = listaSelectDocumentoRetipif;
	}

	public DocumentoExp[] getDocumentoNewHistCM() {
		return documentoNewHistCM;
	}

	public void setDocumentoNewHistCM(DocumentoExp[] documentoNewHistCM) {
		this.documentoNewHistCM = documentoNewHistCM;
	}

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		iniciarApplet = true;
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		idExpediente = expediente.getId();
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		disLinkVistaUnica=false;
		if (ConstantesAdmin.FORM_VERIFICAR_CALIDAD_DOCUMENTOS.endsWith(pagina)) {
			managedBean = verificarCalidadDocumentos;
			managedBean.setDocumentacionDigitalizada(this);		
			disLinkVistaUnica=true;
		}
		
		escaneoWeb = ewDAO.findById(new Integer(1));
		
		empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		
		cleanTransferDir = "1"; //para limpiar la carpeta Transferencias_CC
		
		Mensaje mensaje = mensajeDAO.load(ConstantesBusiness.ID_MENSAJE_ADVERTENCIA_DOCUMENTO_COMBINADO);
		if (mensaje != null) {
			textoAdvertenciaDocumentoCombinado = mensaje.getContenidoStr();
		} else {
			textoAdvertenciaDocumentoCombinado = "";
		}
		
		iniciarSeccionDocumentacionDigitalizada();
	}
	
	public List<SelectItem> getMotivoRechazoItems() {
		List<SelectItem> listaSelectMov = Util.crearItems(listaMotivos, "id", "descripcion");
		ListSorter.ordenarMotivos(listaSelectMov);
		return listaSelectMov;
	}
	
	public void iniciarSeccionDocumentacionDigitalizada(){
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		numeroTareasExpediente = expediente.getExpedienteTareas().size();
			
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			LOG.info("INGRESO AL FOR ***********+");
			LOG.info("expedienteTarea.getTarea().getNombrePagina()-->"+expedienteTarea.getTarea().getNombrePagina());
			if (ConstantesAdmin.FORM_VERIFICAR_CALIDAD_DOCUMENTOS.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTareaActual = expedienteTarea.getTarea().getId().intValue();
				LOG.info("INGRESO AL IF ***********+");
				break;
			}
		}
		
		LOG.info("expediente.getId()-->"+expediente.getId());
		LOG.info("idTarea-->"+idTareaActual);
		LOG.info("numeroTareasExpediente-->"+numeroTareasExpediente);
		
		//trayendo el listado de Documentos que le pertenecesn al Expediente
		listaDocExpediente = docExpDAO.findByExpdiente(expediente.getId());
		
		LOG.info("lista tam-->"+listaDocExpediente.size());
				
		//Evaluando cuantos documentos estarian habilitados para la reTipificacion
		String strTipoDoc=null;
		int contador =0;
		String flag;
		
		//Listado de documentos que se mostrarán en los combos para la retipificacion
		List<DocumentoExp> listaDocExpComboRetipif = new ArrayList();
		
		for (int i = 0; i < listaDocExpediente.size(); i++) {
			strTipoDoc = ((DocumentoExp)listaDocExpediente.get(i)).getDocumento().getTipoDocumento();
		
			if (strTipoDoc!=null && strTipoDoc.equalsIgnoreCase(ConstantesBusiness.TIPO_DOCUMENTO_LEGAL)){
				flag = ((DocumentoExp)listaDocExpediente.get(i)).getFlagEscaneado(); 
				flag = (flag==null?" ":flag);
				
				//valida si el documento fue escaneado para el Exp en curso (en teoria todo lo registrado en la tabla DOC_EXP con flag escaneado = 1 es porque se escaneo para el expediente en curso, ya q el doc esta asociado al exp) 
				if (flag.equals(ConstantesAdmin.FLAG_VERDADERO)){	
					if(escaneadoParaExpEnCurso(expediente.getFechaRegistro(),listaDocExpediente.get(i).getFechaEscaneo()) == true ){
						listaDocExpComboRetipif.add(listaDocExpediente.get(i));
						contador = contador +1;		
					}
				}
			}
		}

		// fin evaluacion
		LOG.info("contador-->"+contador);
		
		// Cargando la lista de Documentos
		List<SelectItem> listaSelectDoc = Util.crearItems(listaDocExpediente, false, "documento.id", "documento.descripcion");
		List<SelectItem> listaSelectDoc2 = Util.crearItems(listaDocExpComboRetipif, false, "documento.id", "documento.descripcion");
		setListaSelectDocumento(listaSelectDoc);
		setListaSelectDocumentoRetipif(listaSelectDoc2);
		
		// cargando la lista de Motivos de Rechazo
		listaMotivos = motivoDAO.findByTarea(idTareaActual,ConstantesBusiness.TIPO_MOTIVO_RECHAZO_DOCUMENTO);
		LOG.info("lista tam Motivos-->"+listaMotivos.size());
		//Seteando la fecha de vigencia para cada registro de Doc_Expediente
		Date fechaVigencia = null;
		DateTime fechaHoy = null;
		fechaHoy = new DateTime (new Date());

		Integer vigenciaDias=0;
		int diasVigencia=0;
		LOG.info("listaDocExpediente.size()-->"+listaDocExpediente.size());
		for (int j = 0; j < listaDocExpediente.size(); j++) {
			
			//Seteando la fecha de vigencia si y solo si no se tiene valor 
			if (listaDocExpediente.get(j).getFechaVigencia() == null){
				//Seteando la fecha de vigencia para cada registro de Doc_Expediente
				if (((DocumentoExp)listaDocExpediente.get(j)).getDocumento().getFlagVigencia() !=null &&
					((DocumentoExp)listaDocExpediente.get(j)).getDocumento().getFlagVigencia().equals(ConstantesAdmin.FLAG_VERDADERO)
					)
				{
					//fecha de vigencia contados a partir de hoy
					vigenciaDias = ((DocumentoExp)listaDocExpediente.get(j)).getDocumento().getVigenciaDias();
					diasVigencia = (vigenciaDias==null?0:vigenciaDias);
					LOG.info("vigenciaDias-->"+vigenciaDias);
					fechaVigencia = fechaHoy.plusDays(diasVigencia).toDate();
					
						listaDocExpediente.get(j).setFechaVigencia(fechaVigencia);
				}
			}
		}
		
		//Obteniendo datos para la columna Lista de Archivos Historicos
		int codCliente = expediente.getCliente().getId();
		DocumentoExp[] arrayDocumentoCM = null;
		listaDocExpedienteW = new ArrayList();
		DocumentoExpWrapper docWrapper = null;
		boolean existeArchivosHistoricos= false;
		strTipoDoc=null;
		
		
		for (int i = 0; i < listaDocExpediente.size(); i++) {
			existeArchivosHistoricos= false;
			try {
				LOG.info("*********TRY iniciarSeccionDocumentacionDigitalizada()********");
				LOG.info("listaDocExpediente.get(i).getDocumento().getCodigoDocumento() : "+listaDocExpediente.get(i).getDocumento().getCodigoDocumento());
				LOG.info("expediente.getCliente().getCodigoCentral() : "+expediente.getCliente().getCodigoCentral());
				arrayDocumentoCM = docExpDAO.findHistorialDocumentoxCliente(
						listaDocExpediente.get(i).getDocumento().getCodigoDocumento(),
						expediente.getCliente().getCodigoCentral(),
						ConstantesBusiness.NUM_DOC_HISTORICOS_RECIBIDOS_CM,
						listaDocExpediente.get(i)).toArray(new DocumentoExp[0]);
				
				/**/
				//LOG.info("*********arrayDocumentoCM********"+arrayDocumentoCM.length);
				if (arrayDocumentoCM !=null) {					
				   Arrays.sort(arrayDocumentoCM,new ArcHistCompare());
				}
				/**/
				
			} catch (Exception e) {
				LOG.info("*********CATCH iniciarSeccionDocumentacionDigitalizada()********");
				e.printStackTrace();
				//LOG.error("error: ",e);
				LOG.error("Consulta documentos CM :"+expediente.getId());
				setMostrarTablaDocDigitalizada(false);
				mensajeErrorPrincipal("tablaDocumentos", "Ocurrió un error al consultar al Content Manager...");
				return;
				//break;
			}
			
			docWrapper = new DocumentoExpWrapper(this);
			
			//Obtener url content por cada documento
			LOG.info("listaDocExpediente.get(i).getDocumento().getCodigoDocumento(): " + listaDocExpediente.get(i).getDocumento().getCodigoDocumento());
			LOG.info("expediente.getCliente().getCodigoCentral(): "+expediente.getCliente().getCodigoCentral());
			LOG.info("expediente.getId(): " + expediente.getId());			
//			if (listaDocExpediente.get(i).getFlagEscaneado()!=null){
//				if (listaDocExpediente.get(i).getFlagEscaneado().compareTo("1")==0){
//					docWrapper.obtenerDocumentoCM(listaDocExpediente.get(i).getDocumento().getCodigoDocumento(),expediente.getId());
//				}
//			}
			
			strTipoDoc = ((DocumentoExp)listaDocExpediente.get(i)).getDocumento().getTipoDocumento();
			flag = ((DocumentoExp)listaDocExpediente.get(i)).getFlagEscaneado(); 
			flag = (flag==null?" ":flag);
			
			if (contador > 1)
				if (strTipoDoc!=null && strTipoDoc.equalsIgnoreCase(ConstantesBusiness.TIPO_DOCUMENTO_LEGAL))
					if (flag.equals(ConstantesAdmin.FLAG_VERDADERO)){	
						if(escaneadoParaExpEnCurso(expediente.getFechaRegistro(),listaDocExpediente.get(i).getFechaEscaneo()) == true ){							
								LOG.info("expediente.getFechaRegistro()-->"+expediente.getFechaRegistro());
								LOG.info("fecha de escaneo del Documento-->"+listaDocExpediente.get(i).getFechaEscaneo());
								docWrapper.setHabilitarDocumento(true);
								setMostrarBotonRetipificacion(true);
							}
					}
			
			if (arrayDocumentoCM != null && arrayDocumentoCM.length > 0)
				existeArchivosHistoricos = true;
			
			LOG.info("listaDocExpediente.get(i).getFlagReqEscaneo()-->"+listaDocExpediente.get(i).getFlagReqEscaneo());
			if (ConstantesAdmin.FLAG_VERDADERO.equals(listaDocExpediente.get(i).getFlagReqEscaneo()) && !existeArchivosHistoricos )
				managedBean.setFaltaHistorialRequeridos(true);
			
			// Basta q algun documento no tenga Archivos históricos para mostrar el applet
			if (!existeArchivosHistoricos){
				setMostApplet(true);
			}
			//fin verificacion
			
			Integer nroPagina = null;
			if (listaDocExpediente.get(i).getDocumento().getFlagNroPagina().equals(Constantes.CHECK_SELECCIONADO) && arrayDocumentoCM != null && arrayDocumentoCM.length > 0) {
				DocumentoExp ultimoDocumentoHist = arrayDocumentoCM[0];
				if (ultimoDocumentoHist != null) {
					nroPagina = ultimoDocumentoHist.getNroPagina();
				} else {
					nroPagina = null;
				}
			}
			docWrapper.setNroPagina(nroPagina);
			

			// [Begin]-[15.07.23]-[Cambio valores de los documentos (casos de negocio)]
			Date fechaUltimoEscaneo = null;
			
			if (listaDocExpediente.get(i).getDocumento().getFlagUltimaFecha().equals(Constantes.CHECK_SELECCIONADO) && arrayDocumentoCM != null && arrayDocumentoCM.length > 0) {
				DocumentoExp ultimoDocumentoHist = docExpDAO.findByIdCm(arrayDocumentoCM[0].getId());
				if (ultimoDocumentoHist != null) {
					fechaUltimoEscaneo = ultimoDocumentoHist.getFechaRegistro();
				} else {
					fechaUltimoEscaneo = null;
				}
			}
			docWrapper.setUltimaFechaEscaneo(fechaUltimoEscaneo);
			// [End]-[15.07.23]
			
			docWrapper.setFechaVigenciaDocumento(listaDocExpediente.get(i).getFechaVigencia());
			docWrapper.setDocExpediente(listaDocExpediente.get(i));
			docWrapper.setDocumentoHistCM(arrayDocumentoCM);
			listaDocExpedienteW.add(docWrapper);			
		}
		//Fin Obteniendo datos para la columna Lista de Archivos Historicos
		
		DocumentoExp[] arrayDocHist = new DocumentoExp[0];
		LOG.info("LONGITUD DEL ARREGLO--> "+arrayDocHist.length);
		setDocumentoNewHistCM(arrayDocHist);
		
		crearTrama();
		setFormOrigen(ConstantesAdmin.SUJITO_HISTORIAL);
		if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equalsIgnoreCase(expediente.getOperacion().getCodigoOperacion()))
			mostApplet = false;
		
		LOG.info("mostApplet..."+mostApplet);
		LOG.info("FormOrigen-->"+formOrigen);

	}
	
	public boolean escaneadoParaExpEnCurso(Date fechaRegExp,Date fechaRegDoc)
	{
		
		 Calendar calExp=Calendar.getInstance();
		 Calendar calDoc=Calendar.getInstance();
		 calExp.setTime(fechaRegExp);
		 calDoc.setTime(fechaRegDoc);
		 
		 calExp.set(Calendar.MINUTE, 0);
		 calExp.set(Calendar.SECOND, 0);
		 calExp.set(Calendar.MILLISECOND, 0);
			
		 calDoc.set(Calendar.MINUTE, 0);
		 calDoc.set(Calendar.SECOND, 0);
		 calDoc.set(Calendar.MILLISECOND, 0);
		 
		 if(calExp.before(calDoc) || calExp.compareTo(calDoc) == 0 ){
			 return true;
		 }
		 return false;
	}

	public void  aplicarCambioTipoDocumento(ActionEvent action){
		LOG.info("aplicarCambioTipoDocumento(ActionEvent action)");
		managedBean.aplicarCambioTipoDocumento(getListaDocExpedienteW());
	}

	public void escanearHistorico(ActionEvent action){
		managedBean.escanearHistorico();
	}
	
	public void setValidacionColumnaFechaVigencia(
			String validacionColumnaFechaVigencia) {
		this.validacionColumnaFechaVigencia = validacionColumnaFechaVigencia;
	}
	
	public String getValidacionColumnaFechaVigencia(){
		LOG.info("validacionColumnaFechaVigencia()");
		for (int i = 0; i < listaDocExpedienteW.size(); i++) {
			LOG.info("fechaVigenciaDocumento-->"+listaDocExpedienteW.get(i).getFechaVigenciaDocumento());
			
			if (listaDocExpedienteW.get(i).getDocExpediente().getDocumento().getFlagVigencia() !=null &&
					listaDocExpedienteW.get(i).getDocExpediente().getDocumento().getFlagVigencia().equals(ConstantesAdmin.FLAG_VERDADERO))
				{ 
					if(listaDocExpedienteW.get(i).getDocExpediente().getFechaVigencia() != null && listaDocExpedienteW.get(i).getFechaVigenciaDocumento() != null){
						if (listaDocExpedienteW.get(i).getDocExpediente().getFechaVigencia().after(listaDocExpedienteW.get(i).getFechaVigenciaDocumento())){
							mensajeErrorPrincipal(listaDocExpedienteW.get(i).getIdComponente(),  listaDocExpedienteW.get(i).getMensajeErrorFechaVigencia());
						}else{
							mensajeErrorPrincipal(listaDocExpedienteW.get(i).getIdComponente(), "");
						}
					}
				}
		}
		return "";
	}
	
	//Este método recorre la lista de Documentos Wrapper en búsqueda del atributo errorFechaVigencia = true	
	public void buscarErrorFechaVigencia(){
		LOG.info("buscarErrorFechaVigencia()");
		managedBean.setErrorValidacionFechaVigencia(false);
		for (int i = 0; i < listaDocExpedienteW.size(); i++) {
			LOG.info("-->"+listaDocExpedienteW.get(i).isErrorFechaVigencia());
			if(listaDocExpedienteW.get(i).isErrorFechaVigencia() == true){
				managedBean.setErrorValidacionFechaVigencia(true);
				break;
			}
		}
		managedBean.actualizarBtnAprobarRechazarbyErrores();
		actualizarBtnRetipificacion();
	}
	
	public void verificarSeleccionMotivoR(){
		LOG.info("verificarSeleccionMotivoR()");
		managedBean.setErrorSeleccionarMotivoRechazo(false);
		managedBean.setExisteRechazoSeleccionado(false);
		for (int i = 0; i < listaDocExpedienteW.size(); i++) {
			LOG.info("RECHAZO-->"+listaDocExpedienteW.get(i).isFlagComboRechazado());
			LOG.info("MOTIVO-->"+listaDocExpedienteW.get(i).isMotivoRechazoSeleccionado());
			//solo evalúo los registros habilitados
			if(listaDocExpedienteW.get(i).isHabilitarOpcionRechazo() != true && listaDocExpedienteW.get(i).isFlagComboRechazado() == true ){
				managedBean.setExisteRechazoSeleccionado(true);
				if (listaDocExpedienteW.get(i).isMotivoRechazoSeleccionado() == false){
					managedBean.setErrorSeleccionarMotivoRechazo(true);
					break;
				}
			}
		}
		managedBean.actualizarBtnAprobarRechazarbyErrores();
	}
	
	public void verificarDocumentacionHistorica(){
		LOG.info("verificarDocumentacionHistorica()");
		
		managedBean.setFaltaHistorialRequeridos(false);
		List<DocumentoExpWrapper> listaDocExpedienteW = getListaDocExpedienteW();
		DocumentoExpWrapper docExpWrapper = null;
		for (int i = 0; i < listaDocExpedienteW.size(); i++) {
			docExpWrapper= listaDocExpedienteW.get(i);
			LOG.info("---");
			LOG.info("Codigo Documento - "+docExpWrapper.getDocExpediente().getDocumento().getCodigoDocumento());
			LOG.info("Flag Obligatorio - "+docExpWrapper.getDocExpediente().getFlagObligatorio());
			LOG.info("DocumentoHistCM is null - "+(docExpWrapper.getDocumentoHistCM()==null));
			if (docExpWrapper.getDocumentoHistCM()!=null)
				LOG.info("DocumentoHistCM length - "+docExpWrapper.getDocumentoHistCM().length);
			LOG.info("---");
			
			if (docExpWrapper.getDocExpediente().getFlagEscaneado().equals(ConstantesAdmin.FLAG_VERDADERO) && 
				((docExpWrapper.getDocumentoHistCM()!=null && docExpWrapper.getDocumentoHistCM().length == 0) || docExpWrapper.getDocumentoHistCM()==null)){
				managedBean.setFaltaHistorialRequeridos(true);
				break;
			}
		}
		
		managedBean.actualizarBtnAprobarRechazarbyErrores();
	}

	public void actualizarBtnRetipificacion(){
		LOG.info("actualizarBtnRetipificacion()");
		LOG.info("errorValidacionFechaVigencia-->"+managedBean.isErrorValidacionFechaVigencia());
		
		if (managedBean.isErrorValidacionFechaVigencia())
			actualizarBtnRetipificar(true);
		else
			actualizarBtnRetipificar(false);
	}
	
	public void actualizarBtnAprobarRechazar(boolean deshabilitarBtnAceptar, boolean deshabilitarBtnRechazar){
		managedBean.actualizarBtnAprobarRechazar(deshabilitarBtnAceptar,deshabilitarBtnRechazar);
	}
	
	public void actualizarBtnRetipificar(boolean deshabilitarBtnRetipificar){
		setDesHabilitarBtnRetipificar(deshabilitarBtnRetipificar);
	}
	
	public String parseDateString(Date fecha){
		 
		String fechaTexto=null;
		
		if (fecha !=null){
			Format formatter;
			formatter = ConstantesBusiness.FORMATO_FECHA;  // 2002
			fechaTexto = formatter.format(fecha);
		}
		return fechaTexto;
	}
		
// ********************************************Applet**************************************************

	public void crearTrama () {
		LOG.info("crearTrama ()");
		StringBuilder builder = new StringBuilder ();
		StringBuilder descarga = new StringBuilder ();
		StringBuilder descargaArcHist = new StringBuilder ();
		boolean esPrimero = true;
		boolean esPrimeroD = true;
//		boolean faltaDocumentos = false;
		for (DocumentoExpWrapper dew1 : listaDocExpedienteW) {
			DocumentoExp dexp = dew1.getDocExpediente();
			if (ConstantesBusiness.FLAG_ESCANEADO.equals(dexp.getFlagEscaneado())) {
				if (!esPrimeroD){
					descarga.append(";");
				}
				descarga.append(dew1.getDocExpediente().getDocumento().getCodigoDocumento());
				
				LOG.info("descarga "+dew1.getDocExpediente().getDocumento().getCodigoDocumento());
								
				esPrimeroD = false;
			}
			if (dew1.getDocumentoHistCM() == null || dew1.getDocumentoHistCM().length == 0) {
				Documento doc = dew1.getDocExpediente().getDocumento();
				
				//Documento doc =  dew1.getDocumentoHistCM()[0].getNombreArchivo();
				if (!esPrimero){
					builder.append(";");
				}
				builder.append(doc.getCodigoDocumento()).append(";").append(doc.getDescripcion());
				
				LOG.info("builder "+doc.getCodigoDocumento()+ ";"+doc.getDescripcion());
				
				esPrimero = false;
//				faltaDocumentos = true;
			}else{
				if (dew1.isNewDocHistorial() != true && (dew1.getDocumentoHistCM() != null && dew1.getDocumentoHistCM()[0].getDocumento().getCodigoDocumento().equalsIgnoreCase(ConstantesBusiness.CODIGO_DOCUMENTO_COPIA_LITERAL))) {
					/* Comentado porque causaba problemas si la copia literal no era el primer documento de la guía */
//					if (!esPrimero) {
//						descargaArcHist.append(";");
//					}
					descargaArcHist.append(dew1.getDocumentoHistCM()[0].getDocumento().getCodigoDocumento()).append(";")
							       .append(dew1.getDocumentoHistCM()[0].getNombreArchivo()).append(";")
							       .append(dew1.getDocumentoHistCM()[0].getIdCm());

					LOG.info("descargaArcHist " + dew1.getDocumentoHistCM()[0].getDocumento().getCodigoDocumento()
							+ ";" + dew1.getDocumentoHistCM()[0].getNombreArchivo() 
							+";" + dew1.getDocumentoHistCM()[0].getIdCm());

					esPrimero = false;
				}
			}
		}
		tramaDescarga = descarga.toString();
		tramaDocumentos = builder.toString();
		tramaDescargaArcHist = descargaArcHist.toString();
//		managedBean.actualizarSubidaDocumentos (faltaDocumentos);
	}
	
	
	public void actualizarDocumentos (ActionEvent ae) {
		LOG.info("actualizarDocumentos (ActionEvent ae)");
		LOG.info("listaDocumentos : {}", listaDocumentos);
		if (listaDocumentos!=null && !listaDocumentos.equals("")) {
			Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
			StringTokenizer st = new StringTokenizer (listaDocumentos, ",");
			DocumentoExp[] arrayDocumentoHistorico;
			DateTime fecEscanDocHistIni =null;
			Date fecEscanDocHistFin=null;
			String strNombreArchivo=null;
			
			while (st.hasMoreTokens()) {
				String tk = st.nextToken();
				LOG.info("documento : {}", tk);
				DocumentoExpWrapper doc = buscarPorCodigoDocumento (tk);
				if (doc!=null) {
					DocumentoExp docHistorico = new DocumentoExp();
					arrayDocumentoHistorico = new DocumentoExp[1];
	
					//Generando un nombre de archivo en base a la fecha de escaneo del doc actual
					fecEscanDocHistIni = new DateTime (doc.getDocExpediente().getFechaEscaneo());
					LOG.info("fecha de escaneo en TIMESTAMP-->"+fecEscanDocHistIni);
					fecEscanDocHistFin = fecEscanDocHistIni.minusDays(1).toDate();
					LOG.info("fecha de escaneo en TIMESTAMP menos 1 DIA-->"+fecEscanDocHistFin);
					
					
					strNombreArchivo = parseDateString(fecEscanDocHistFin);
					
					docHistorico.setNombreArchivo(strNombreArchivo);
					doc.setNewDocHistorial(true);
					doc.setNombreFile(doc.getDocExpediente().getDocumento().getCodigoDocumento().concat("-").concat(ConstantesAdmin.SUJITO_HISTORIAL));
					LOG.info("documento FILE NAME--------"+ doc.getNombreFile());
					//					listaDocH.add(docHistorial);				
					arrayDocumentoHistorico[0]= docHistorico;	
					
					doc.setDocumentoHistCM(arrayDocumentoHistorico);
					doc.setNomArchivoHistPropuesto(fecEscanDocHistFin);
					
					documentoNewHistCM = arrayDocumentoHistorico;
				}
			}
			
			LOG.info("************ DOCUMENTO ***************");
			for (int j = 0; j < getDocumentoNewHistCM().length; j++) { 
				LOG.info("id del historico ["+j+"]-->"+ getDocumentoNewHistCM()[j].getIdCm());
				LOG.info("nombre del historico ["+j+"]-->"+ getDocumentoNewHistCM()[j].getNombreArchivo());
			}
			LOG.info("************ FIN DOCUMENTO ***************");
			
			//mostrando la nueva lista de Documentos Historicos
			for (int i = 0; i < listaDocExpedienteW.size(); i++) {
				LOG.info("************ ID documento "+listaDocExpedienteW.get(i).getDocExpediente().getDocumento().getId()+ "***************");
				if (listaDocExpedienteW.get(i).getDocumentoHistCM() != null)
				for (int j = 0; j < listaDocExpedienteW.get(i).getDocumentoHistCM().length; j++) {
					LOG.info("historial del dcoumento ["+j+"]-->"+ listaDocExpedienteW.get(i).getDocumentoHistCM()[j].getNombreArchivo());
					LOG.info("id Documento["+j+"]-->"+ listaDocExpedienteW.get(i).getDocumentoHistCM()[j].getIdCm());
				}
			}
			//fin
			
			setListaDocExpedienteW(listaDocExpedienteW);
			setDocumentoNewHistCM(documentoNewHistCM);
			
			verificarDocumentacionHistorica();
			managedBean.actualizarBtnAprobarRechazarbyErrores();
			
			crearTrama ();
		}
	}
	
	
	public DocumentoExpWrapper buscarPorCodigoDocumento (String codigo) {
		LOG.info("buscarPorCodigoDocumento (String codigo)");
		for (DocumentoExpWrapper dew : listaDocExpedienteW) {
			if (codigo.equals(dew.getDocExpediente().getDocumento().getCodigoDocumento())) {
				return dew;
			}
		}
		return null;
	}
	
	public void actualizarDocumentoCombinado (ActionEvent ae) {
		LOG.info("actualizarDocumentoCombinado (ActionEvent ae)");
		
		Date fechaEscaneo = new Date ();
		desHabilitarBtnMerge = false;
		String strNombreArchivo = parseDateString(fechaEscaneo);
		LOG.info("codigoDocCombinado "+codigoDocCombinado);
		DocumentoExpWrapper doc = buscarPorCodigoDocumento (codigoDocCombinado); //D12
		if(doc!=null){
			doc.getDocExpediente().setNombreArchivo(strNombreArchivo);
			doc.setDocumentoCombinado(true);
			desHabilitarBtnMerge = true; // solo permitia combinar un documento
			LOG.info("doc HABILITADO MERGE -->"+doc.isDesHabilitarBtnMerge());
		}
		
		LOG.info("desHabilitarBtnMerge-->"+desHabilitarBtnMerge);
		LOG.info("nombreArchivoCombinado-->"+strNombreArchivo);
		LOG.info("codigoDocCombinado-->"+codigoDocCombinado);
		
	}
	
	public void eliminarDocumento (ActionEvent ae) {
		LOG.info("eliminarDocumento (ActionEvent ae)");
		String codigoDocHist = getRequestParameter("codigoDocumento");
		codigoDocHist = (codigoDocHist==null?"0":codigoDocHist);
		codigoDocHist = (codigoDocHist==""?"0":codigoDocHist);
		codigoDocHist = codigoDocHist.substring(0,codigoDocHist.indexOf("-"));
		LOG.info("codigoDocHist-->"+codigoDocHist);
		
		DocumentoExpWrapper doc = buscarPorCodigoDocumento (codigoDocHist);
		
		DocumentoExp[] docHistCM = new DocumentoExp[0]; 
		
		if (doc != null){
			LOG.info("eliminando el documento escaneado recientemente");
			doc.setDocumentoHistCM(docHistCM);
			doc.setNewDocHistorial(false);
			crearTrama();
		}else{
			LOG.info("doc es null");
		}
			
		verificarDocumentacionHistorica();
	}
	
	 private static class ArcHistCompare implements Comparator<DocumentoExp>
	 {
	   public int compare(DocumentoExp s1, DocumentoExp s2)
	   {
	      return (s2.getNombreArchivo().compareTo(s1.getNombreArchivo()));
	   }
	 }

	public String getTramaDescargaArcHist() {
		return tramaDescargaArcHist;
	}

	public void setTramaDescargaArcHist(String tramaDescargaArcHist) {
		this.tramaDescargaArcHist = tramaDescargaArcHist;
	}

	public boolean isDisLinkVistaUnica() {
		return disLinkVistaUnica;
	}
	
	public void setDisLinkVistaUnica(boolean disLinkVistaUnica) {
		this.disLinkVistaUnica = disLinkVistaUnica;
	}

	public EscaneoWeb getEscaneoWeb() {
		return escaneoWeb;
	}

	public void setEscaneoWeb(EscaneoWeb escaneoWeb) {
		this.escaneoWeb = escaneoWeb;
	}
	
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public String getCleanTransferDir() {
		if (cleanTransferDir != null && cleanTransferDir.equals("1")) {
			cleanTransferDir = "0";
			return "1";
		} else {
			return "0";
		}
	}

	public void setCleanTransferDir(String cleanTransferDir) {
		this.cleanTransferDir = cleanTransferDir;
	}

	public String getTextoAdvertenciaDocumentoCombinado() {
		return textoAdvertenciaDocumentoCombinado;
	}

	public void setTextoAdvertenciaDocumentoCombinado(
			String textoAdvertenciaDocumentoCombinado) {
		this.textoAdvertenciaDocumentoCombinado = textoAdvertenciaDocumentoCombinado;
	}
	
}
