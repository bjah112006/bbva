package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteTCWPSWeb;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.GuiaDocumentariaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;
import com.ibm.bbva.tabla.dto.DatosDocumentosExpIiceDTO;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;
import com.ibm.bbva.tabla.util.vo.DocumentoReutilizable;
import com.ibm.bbva.util.AyudaPanelDocumentos;
import com.ibm.bbva.util.PersonaPD;
import com.ibm.bbva.util.TipoDocumentoPD;
//import com.ibm.bbva.cm.iice.Documento;

@SuppressWarnings("serial")
@ManagedBean(name = "paneldocumentos")
@RequestScoped
public class PanelDocumentosMB extends AbstractMBean {

	@EJB
	private GuiaDocumentariaBeanLocal guiaDocumentariaBean;
	@EJB
	private DocumentoExpTcBeanLocal documentoExpTcBean;
	@EJB
	private TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	private FacadeLocal facade;
	@EJB
	private TareaPerfilBeanLocal tareaPerfilBean; 
	
	private Expediente expediente;
	private List<GuiaDocumentaria> listaGuiaDoc;
	private List<AyudaPanelDocumentos> listaAyudaPanelDocumentos;
	private List<PersonaPD> lstPersonaPDs; 
	private String opDocObligatorio;
	private String opDocNoObligatorio;
	private String descripDocObligatorio;
	private String descripDocNoObligatorio;
	private String colEliminarTipDoc;
	private String colModificarTipDoc;
	private String colObservarTipDoc;
	private GuiaDocumentaria objSelectGuiaDocumentaria;

	private boolean estCheck;
	private boolean opEliminar;
	private boolean opModificar;
	private boolean opObligatorio;
	private boolean opObservar;
	private boolean opValidar;
	private boolean contlista;
	private Documento objDoc;
	private DocumentoExpTc objDocumentoExpTc;
	private String strListaDocsTransferencias;
	private String strListaDocsObservados;
	private String strListaIdcmDocsObservados;
	private HtmlInputText htmlListaDocsTransferencias;
	private HtmlInputText htmlListaDocsObservados;
	private HtmlInputText htmlListaIdcmDocsObservados;
	private HtmlCommandButton btnActualizaPanelDocumentos;
	private HtmlCommandButton btnActualizaAppletYPanelDocumentos;
	private String strCodigoTipoDocEliminar;
	private HtmlInputText htmlCodigoTipoDocEliminar;
	private HtmlCommandButton btnEliminarDocumentoContent;
	private HtmlInputText htmlTramaDocumentoCambio;
	private String strTramaDocumentoCambio;
	private String strCodigoTipoDocsModificar;
	private HtmlInputText htmlCodigoTipoDocsModificar;
	private HtmlCommandButton btnModificarDocumentoContent;
	private HtmlInputText htmlIdCmObs;
	private HtmlInputText htmlValIdCmObs;
	private String strIdCmObs;
	private String strValIdCmObs;
	private boolean pantallaConsulta;
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	
	private boolean expedienteEnProceso;
	
	/**
	 * Boolean que determina si en el panel de documentos
	 * existen nuevos archivos a parte de los que estaban originalmente.
	 * 
	 * Este atributo se modificarÃ¡ cada vez que se actualiza la lista
	 * de archivos del panel de documentos, sea por la agregaciÃ³n de un documento
	 * o una eliminaciÃ³n.
	 * 
	 * Otros nombres : seHanSubidoArchivosNuevos
	 * 
	 */
	private boolean existenNuevosArchivos = false;
	
	private static final Logger LOG = LoggerFactory.getLogger(PanelDocumentosMB.class);
	
	public PanelDocumentosMB() {

	} 
	
	@PostConstruct
    public void init() {		
		LOG.info("init PanelDocumentosMB");
		
		opDocObligatorio=Constantes.OP_DOC_OBLIGATORIO;
		opDocNoObligatorio=Constantes.OP_DOC_NO_OBLIGATORIO;
		descripDocObligatorio=Constantes.DESCRIP_DOC_OBLIGATORIO;
		descripDocNoObligatorio=Constantes.DESCRIP_DOC_NO_OBLIGATORIO;		
		colEliminarTipDoc=Constantes.COL_ELIMINAR_TIP_DOC;
		colModificarTipDoc=Constantes.COL_MODIFICAR_TIP_DOC;
		colObservarTipDoc = Constantes.COL_OBSERVAR_TIP_DOC;
		contlista=false;
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		listaGuiaDoc = new ArrayList<GuiaDocumentaria>(); 
		listaAyudaPanelDocumentos=new ArrayList<AyudaPanelDocumentos>();
		objDocumentoExpTc=new DocumentoExpTc();
		existenNuevosArchivos = false;
		expedienteEnProceso = false;
		
		
		/**
		 * Chequear si el expediente está en proceso, en caso sea positivo
		 * el mensaje para todos los documentos del 'panel de documentos' será
		 * establecido como "En proceso"
		 * 
		 * El objeto ExpedienteTCWPS solo está disponible cuando se realiza la llamada
		 * desde TablaBandejaAsigMB o TablaBandejaPendMB
		 * 
		 */
		
		ExpedienteTCWPSWeb expedienteTCWPSWeb = (ExpedienteTCWPSWeb) getObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		
		if(expedienteTCWPSWeb != null){
			if(expedienteTCWPSWeb.getFlagEnProcesoTimer() != null){
				if(expedienteTCWPSWeb.getFlagEnProcesoTimer().equals("1")){
					expedienteEnProceso = true;
				}
			}
		}
		
		/*Mostrar o Ocultar Campos*/
		String nombJSP = getNombreJSPPrincipal();
		//30
		/*if (nombJSP.equals("formCambiarSituacionExp") || nombJSP.equals("formCambiarSituacionTramite") || 
			nombJSP.equals("formRealizarAltaTarjeta") || nombJSP.equals("formRegistrarAprobResolucion") || 
			nombJSP.equals("formArchivarExpediente") || nombJSP.equals("formVerificarExpDesestimados") ||
			nombJSP.equals("formVisualizarExpediente") || nombJSP.equals("formVerificarConformidadExpediente")){
				setEstCheck(true);
				setOpEliminar(false);
				setOpModificar(false);
		}else{//28
			if(nombJSP.equals("formAprobarExpediente") || nombJSP.equals("formRegistrarExpediente") || 
			   nombJSP.equals("formVerificarResultadoDomiciliaria") || nombJSP.equals("formCoordinarClienteSubsanar") || 
			   nombJSP.equals("formEjecutarEvalCrediticia") || nombJSP.equals("formEvaluarDevolucionRiesgos") || 
			   nombJSP.equals("formConsultarClienteModificaciones") || nombJSP.equals("formRevisarRegistrarDictamen") || 
			   nombJSP.equals("formRegularizarEscanearDocumentacion") || nombJSP.equals("formSolicitarVerificacionDomiciliaria")){
				setEstCheck(true);
				setOpEliminar(true);
				setOpModificar(false);
			}else{//29
				if(nombJSP.equals("formAnularModificarOpCu14") || nombJSP.equals("formAnularModificarOpCu18") ||
					nombJSP.equals("formRegistrarDatos")){
					setEstCheck(true);
					setOpEliminar(true);
					setOpModificar(true);					
				}
			}
			
		}*/

		//30
		pantallaConsulta = false;
		LOG.info("panel 0");
		if (nombJSP.equals("formVisualizarExpediente")){
			LOG.info("panel 1");
			setEstCheck(true);
			setOpEliminar(false);
			setOpModificar(false);
			setOpObservar(false);
			setOpObligatorio(false);
			pantallaConsulta = true;
		}else{//29
			if(nombJSP.equals("formVerificarConformidadExpediente")){
				LOG.info("panel 2");
				setEstCheck(true);
				setOpEliminar(true);
				setOpModificar(true);
				setOpObservar(true);
				setOpObligatorio(true);
			}else{//28			
				LOG.info("panel 3");
				setEstCheck(true);
				setOpEliminar(true);
				setOpModificar(false);
				setOpObservar(true);
				setOpObligatorio(true);
			}			
		}
		
		/*if(nombJSP.equals("formVerificarConformidadExpediente") || 
				nombJSP.equals("formEjecutarEvalCrediticia") ){
			setOpValidar(true);
		}else*/
			setOpValidar(false);
		
		LOG.info("INIT cargarDocumentosPanel");
		if (strCodigoTipoDocsModificar == null) {
			strListaDocsTransferencias = String.valueOf(getObjectSession("strCodigoTipoDocsModificar"));			
		}
		cargarDocumentosPanel(null);		
	}

	public void actualizaTipoOferta(ValueChangeEvent vce) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		ProductoNuevoMB tipoOferta = null;
		
		tipoOferta = (ProductoNuevoMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");		
		Object codigo = vce.getNewValue();
		tipoOferta.codTipoOferta= (String)codigo;
	}

	public void actualizaSubProducto(ValueChangeEvent vce) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		ProductoNuevoMB productoNuevo = null;
		
		productoNuevo = (ProductoNuevoMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");		
		Object codigo = vce.getNewValue();
		productoNuevo.codSubproducto= (String)codigo;
	}
	
		
	
	/**
	 * MÃ©todo que sucede luego de que el usuario
	 * presiona el botÃ³n de Aceptar, despuÃ©s de que se guarda el archivo a travÃ©s
	 * del Applet, el cual llama a un mÃ©todo JavaScript que actualiza la propiedad
	 * htmlListaDocsTransferencias y finalmente lo invoca.
	 * 
	 * @param event
	 */
	public void cargarDocumentosPanel(AjaxBehaviorEvent event){
		LOG.info("cargarDocumentosPanel : entro");
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		ProductoNuevoMB productoNuevo = null;
		
		if(htmlListaDocsTransferencias == null){
			Object objListaDocsTransferencias = getObjectSession("strListaDocsTransferencias");			
			if (objListaDocsTransferencias != null) {
				strListaDocsTransferencias = String.valueOf(objListaDocsTransferencias);
			}						
		}else{
			strListaDocsTransferencias = String.valueOf(htmlListaDocsTransferencias.getValue());
		}
				
		LOG.info("STR LISTA DOCS TRANSFERENCIAS : " + strListaDocsTransferencias);
		

		if(strListaDocsTransferencias != null && !strListaDocsTransferencias.equals("null")){
			LOG.info("STR LISTA DOCS TRANSFERENCIAS SPLIT : " );
			
			String[] listaDocs = strListaDocsTransferencias.trim().split(",");
			for(int i = 0; i< listaDocs.length; i++ ){
				LOG.info(" - DOC TRANSFERENCIA " + i + " : " + listaDocs[i]);
			}
			
			if(listaDocs.length == 1 && listaDocs[0].equals("")){
				this.setExistenNuevosArchivos(false);
			}else if(listaDocs.length > 0){
				this.setExistenNuevosArchivos(true);
			}else{
				this.setExistenNuevosArchivos(false);
			}
		}
		
		LOG.info("¿Existen nuevos archivos para el expediente en la tarea en curso? : " + this.isExistenNuevosArchivos());
		
		addObjectSession("strListaDocsTransferencias", strListaDocsTransferencias);		
		LOG.info("strListaDocsTransferencias:" + strListaDocsTransferencias);
					
		List<GuiaDocumentaria> listGuiaDocumentaria = (List<GuiaDocumentaria>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get(Constantes.LISTA_DOC_EXP_ADJ);
		List<AyudaPanelDocumentos> listAyuda = (List<AyudaPanelDocumentos>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		
		List<PersonaPD> listAyudaPD = (List<PersonaPD>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get(Constantes.LISTA_AYUDA_AGR_PANEL_DOCUMENTOS);
		
		if (getNombreJSPPrincipal().equals("formRegistrarExpediente")
				&& event == null && listGuiaDocumentaria != null
				&& listAyuda != null) {
			LOG.info("cargarDocumentosPanel 1");
			LOG.info("event : "+event);
			LOG.info("listGuiaDocumentaria : "+listGuiaDocumentaria);
			LOG.info("listAyuda : "+listAyuda);
			
			this.setListaGuiaDoc(listGuiaDocumentaria);
			this.setListaAyudaPanelDocumentos(listAyuda);
			this.setLstPersonaPDs(listAyudaPD);
			this.setContlista(true);
			
			/*Documentos Escaneados*/
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			DocumentosEscaneadosMB documentosEscaneadosMB = (DocumentosEscaneadosMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "documentosEscaneados");
			documentosEscaneadosMB.actualizarApplet(listaAyudaPanelDocumentos);
			
			documentosEscaneadosMB.documentosFaltantes(listaAyudaPanelDocumentos);
			
		/*} else if(productoNuevo.codSubproducto==null || productoNuevo.codSubproducto.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) 
				|| productoNuevo.codTipoOferta==null || productoNuevo.codTipoOferta.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) 
				|| productoNuevo.codSubproducto==null || productoNuevo.codSubproducto.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){*/
		} else{
			if(!getNombreJSPPrincipal().equals("formVisualizarExpediente") )
				productoNuevo = (ProductoNuevoMB)  
	 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
			
			 if(productoNuevo!=null && (productoNuevo.codProducto==null || productoNuevo.codProducto.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) 
						|| productoNuevo.codTipoOferta==null || productoNuevo.codTipoOferta.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))){			
					LOG.info("cargarDocumentosPanel 2");
					this.setListaAyudaPanelDocumentos(null);
					this.setContlista(false);
					removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
					removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
					removeObjectSession(Constantes.LISTA_AYUDA_AGR_PANEL_DOCUMENTOS);
				}else{
					LOG.info("cargarDocumentosPanel 3");
					this.setListaGuiaDoc(cargaGuiaDoc());
					this.obtenerListaPanelDocumento(listaGuiaDoc);
					this.setContlista(true);
					
					if(!this.isOpValidar()){
						/*Documentos Escaneados*/
						LOG.info("ValidaciÃ³n de Documentos que ya fueron cargados");
						ELContext elContext = FacesContext.getCurrentInstance().getELContext();
						DocumentosEscaneadosMB documentosEscaneadosMB = (DocumentosEscaneadosMB) FacesContext
								.getCurrentInstance().getApplication().getELResolver()
								.getValue(elContext, null, "documentosEscaneados");
						documentosEscaneadosMB.actualizarApplet(listaAyudaPanelDocumentos);
					}
				}			
			
		}
		
		/*evalua la grilla de reutilizable*/
		if(getNombreJSPPrincipal().equals("formRegistrarExpediente") ||
				getNombreJSPPrincipal().equals("formRegularizarEscanearDocumentacion") ||
				getNombreJSPPrincipal().equals("formEvaluarDevolucionRiesgos") ||
				getNombreJSPPrincipal().equals("formCoordinarClienteSubsanar") ||
				getNombreJSPPrincipal().equals("formConsultarClienteModificaciones")){
			DocumentoReutilizableMB documentoReutilizableMB = (DocumentoReutilizableMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentoReutilizable");
			List<DocumentoReutilizable> listaOriginalDocReu = documentoReutilizableMB.obtenerDatos();
			List<DocumentoReutilizable> listaNuevaDocReu = new ArrayList<DocumentoReutilizable>();
			
			if(this.listaAyudaPanelDocumentos != null && this.listaAyudaPanelDocumentos.size() > 0 && 
					listaOriginalDocReu!=null && listaOriginalDocReu.size()>0){
				for(DocumentoReutilizable documentoReutilizable : listaOriginalDocReu){
					boolean encontrado = false;
					boolean escaneado = false;
					for(AyudaPanelDocumentos objAux : this.listaAyudaPanelDocumentos){
						if (objAux.getObjGuiaDocumentaria() != null) {
							if(objAux.getObjGuiaDocumentaria().getTipoDocumento().getCodigo().equals(documentoReutilizable.getCodigoDocumento())){
								encontrado = true;
								if(objAux.isDocTransferencias()){
									escaneado = true;
								}
								break;
							}
						}
					}
					if(encontrado && !escaneado){
						listaNuevaDocReu.add(documentoReutilizable);
					}
				}
			}
			else{
				listaNuevaDocReu = new ArrayList<DocumentoReutilizable>();
			}
			
			String codigoTipoEliminarContent = getObjectSession("codigoTipoEliminarContent") == null ? null : (String)getObjectSession("codigoTipoEliminarContent");
			removeObjectSession("codigoTipoEliminarContent");
			LOG.info("codigoTipoEliminarContent:::::"+codigoTipoEliminarContent);
			
			String seleccionados = getObjectSession("reutilizablesSeleccionados") == null ? null : (String)getObjectSession("reutilizablesSeleccionados");
			LOG.info("seleccionados1:::::"+seleccionados);
			if(seleccionados != null){
				if(codigoTipoEliminarContent != null && !"".equals(codigoTipoEliminarContent)){
					seleccionados = seleccionados.replace(codigoTipoEliminarContent + ",", "");
				}
			}
			
			List<String> listaSeleccionados = new ArrayList<String>();
			if(seleccionados != null && !"".equals(seleccionados) && seleccionados.length() > 0){
				listaSeleccionados = Arrays.asList(seleccionados.split(","));
			}
			
			if(this.listaAyudaPanelDocumentos != null && this.listaAyudaPanelDocumentos.size() > 0){
				String seleccionadosTMP = "";
				for(AyudaPanelDocumentos objAux : this.listaAyudaPanelDocumentos){
					if (objAux.getObjGuiaDocumentaria() != null) {
						for(String codigoTipoDocumento : listaSeleccionados){
							if(objAux.getObjGuiaDocumentaria().getTipoDocumento().getCodigo().equals(codigoTipoDocumento)){
								seleccionadosTMP = seleccionadosTMP + codigoTipoDocumento + ",";
								break;
							}
						}
					}
				}
				LOG.info("seleccionadosTMP:::::"+seleccionadosTMP);
				seleccionados = seleccionadosTMP;
				listaSeleccionados = Arrays.asList(seleccionados.split(","));
				addObjectSession("reutilizablesSeleccionados", seleccionados);
			}
			
			for(DocumentoReutilizable documentoReutilizableNueva : listaNuevaDocReu){
				documentoReutilizableNueva.setSeleccion(false);
				for(String codigoTipoDocumento : listaSeleccionados){
					if(documentoReutilizableNueva.getCodigoDocumento().equals(codigoTipoDocumento)){
						documentoReutilizableNueva.setSeleccion(true);
					}
				}
			}
			
			documentoReutilizableMB.setListaDocumentoReutilizable(listaNuevaDocReu);
			
			addObjectSession("listaNuevaDocReu",listaNuevaDocReu);
		}
		
	}
	
	public void eliminarDocumentoContent(AjaxBehaviorEvent event){
		LOG.info("idCm="+strCodigoTipoDocEliminar);
		BigDecimal idCm = new BigDecimal(strCodigoTipoDocEliminar);//strCodigoTipoDocEliminar recibe ahora el idCM
		DocumentoExpTc docExpTC = documentoExpTcBean.consultarDocumentoExpediente(expediente.getId(), idCm);

		try {
			docExpTC.setIdCm(null);
			docExpTC.setPidCm(null);
			docExpTC.setFecReg(null);
			docExpTC.setFlagCm(null);
			docExpTC.setNombreArchivo(null);
			docExpTC.setFlagObs(null);
			docExpTC.setFlagEscaneado("0");
			docExpTC.setFlagDocReutilizable("0");
			
			documentoExpTcBean.edit(docExpTC);
		} catch(Exception e) {
			LOG.info("No se pudo eliminar el archivo en CM. Excepción.");
			LOG.error(e.getMessage(), e);
		}
		
		addObjectSession("codigoTipoEliminarContent",docExpTC.getTipoDocumento().getCodigo());
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		DocumentoReutilizableMB documentoReutilizableMB = (DocumentoReutilizableMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentoReutilizable");
		documentoReutilizableMB.eliminarGrillayCargarEnApplet(docExpTC.getTipoDocumento().getCodigo());
		
		cargarDocumentosPanel(event);
	}
	
	public void modificarDocumentoContent(AjaxBehaviorEvent event){
		String codigoTipoDocAntiguo = strCodigoTipoDocsModificar.substring(0, strCodigoTipoDocsModificar.indexOf(","));
		LOG.info("codigoTipoDocAntiguo="+codigoTipoDocAntiguo);
		String codigoTipoDocNuevo = strCodigoTipoDocsModificar.substring(strCodigoTipoDocsModificar.indexOf(",")+1);
		LOG.info("codigoTipoDocNuevo="+codigoTipoDocNuevo);
		DocumentoExpTc docExpTCMod = documentoExpTcBean.consultarUltimoDocumentoExpediente(expediente.getId(), codigoTipoDocAntiguo);
		DocumentoExpTc docExpTCOld = documentoExpTcBean.consultarDocumentoExpediente(expediente.getId(), codigoTipoDocNuevo);
		LOG.info("docExpTCMod.getId():" + docExpTCMod.getId());
		try {
			String resultado = facade.Content_actualizarTipoDoc((int) docExpTCMod.getId(), codigoTipoDocNuevo);
			if (!resultado.equals("KO")) { // ahora devuelve el PID
				TipoDocumento tipoDocumentoNuevo = tipoDocumentoBean.buscarPorCodigo(codigoTipoDocNuevo);
				docExpTCMod.setPidCm(resultado);
				docExpTCMod.setTipoDocumento(tipoDocumentoNuevo);
				docExpTCMod.setNombre(tipoDocumentoNuevo.getDescripcion());
				
				List<GuiaDocumentaria> listGuiaDocumentaria = (List<GuiaDocumentaria>) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSessionMap().get(Constantes.LISTA_DOC_EXP_ADJ);
				if (listGuiaDocumentaria != null && !listGuiaDocumentaria.isEmpty()) {
				    for (GuiaDocumentaria guia : listGuiaDocumentaria) {
				    	if (guia.getTipoDocumento().getId() == tipoDocumentoNuevo.getId()) {
					    	if (guia.getPersona() == null || guia.getPersona().getId() == 0) {
					    		docExpTCMod.setPersona(null); // vino como null en el query pero le setearon el titular
					    	} else {
					    		docExpTCMod.setPersona(guia.getPersona());
					    	}
					    	docExpTCMod.setObligatorio(guia.getObligatorio());
					    	break;
				    	}
				    }
				}
				
				documentoExpTcBean.edit(docExpTCMod);
				
				/* Si ya existe otro con el mismo tipo de documento debo eliminarlo.
				 * Al final en AyudaDocumento.java se crearÃ¡n los que hagan falta 
				 * de nuevo. */
				if (docExpTCOld != null) {
					//documentoExpTcBean.remove(docExpTCOld);
				}
			} else {
				LOG.info("No se pudo actualizar el archivo en CM. Resultado = "+resultado);
			}
		} catch(Exception e) {
			LOG.info("No se pudo actualizar el archivo en CM. ExcepciÃ³n.");
			LOG.error(e.getMessage(), e);
		}
		cargarDocumentosPanel(event);
	}
	
	public List<GuiaDocumentaria> cargaGuiaDoc() {		
		FacesContext ctx = FacesContext.getCurrentInstance();  
		GuiaDocumentariaMB guiaDocumentaria = null;
     	
		guiaDocumentaria = (GuiaDocumentariaMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "guiaDocumentaria");
				
		guiaDocumentaria.init();
		return guiaDocumentaria.cargaPanelDocumentos();
	}
	
	public String obtenerValorOblig(String valorOblig){		
		if (valorOblig.trim().equals(this.opDocObligatorio.trim()))
			return this.descripDocObligatorio;
		
		if (valorOblig.trim().equals(this.opDocNoObligatorio.trim()))
			return this.descripDocNoObligatorio;
		return null;
	}
	
	public DocumentoExpTc obtenerUrlEscaneado(GuiaDocumentaria objGD, DocumentoExpTc objDocumentoExpTc, AyudaPanelDocumentos objAyuda){		
		FacesContext ctx = FacesContext.getCurrentInstance();  
		GuiaDocumentariaMB guiaDocumentaria = null;		
		
		guiaDocumentaria = (GuiaDocumentariaMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "guiaDocumentaria");
		
		if(guiaDocumentaria.obtenerTipoDocCM(objGD).trim().equals("1")){
			objAyuda.setDocEscaneado(true);
			return objDocumentoExpTc=guiaDocumentaria.obtenerUrlTipoDocCM(objGD);
		}else{
			objAyuda.setDocEscaneado(false);
			return null;
		}
		
	}	
	
	public boolean validarDocumentos(){
		LOG.info("this.isOpValidar() -> "+this.isOpValidar());
		
		if(this.isOpValidar()){
			// esta validación ya nunca se hace, opValidar siempre es false
//			Map<String, Object> mapListDocumentosCM  = (Map<String, Object>) getObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM);
//			
//			if(mapListDocumentosCM!=null){				
//				for(GuiaDocumentaria ldg : listaGuiaDoc){
//					LOG.info("doc ES obligatorio???, doc = "+ldg.getId());
//					if(ldg.getObligatorio().trim().equals(Constantes.CODIGO_DOCUMENTO_OBLIGATORIO)){
//						Documento objDocumento= (Documento) mapListDocumentosCM.get(ldg.getTipoDocumento().getCodigo());
//						if(objDocumento!=null){
//							LOG.info("objDocumento obligatorio OK , doc = "+ldg.getId());
//						}else{
//							LOG.info("objDocumento obligatorio faltante, doc = "+ldg.getId());
//							return false;
//						}
//					}
//				}
//			}else{	
//				for(GuiaDocumentaria ldg : listaGuiaDoc){
//					if(ldg.getObligatorio().trim().equals(Constantes.CODIGO_DOCUMENTO_OBLIGATORIO)){
//						LOG.info("Guia documentaria tiene documentos obligatorios no cargados");
//						return false;
//					}	
//				}
//			}

		}
		LOG.info("Retorna TRUE validarDocumentos");
		
		/*
		List<PersonaPD> listaPersonaPD = (List<PersonaPD>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get(Constantes.LISTA_AYUDA_AGR_PANEL_DOCUMENTOS);
		
		for(PersonaPD objPersonaPD : listaPersonaPD)
		{
			List<TipoDocumentoPD> listaTipoDocumentoPD  = objPersonaPD.getLstTipoDocumentoPDs();
			for(TipoDocumentoPD objTipoDocumentoPD : listaTipoDocumentoPD)
			{
				List<AyudaPanelDocumentos> listaAyudaPanelDocumentos = objTipoDocumentoPD.getLstAyudaPanelDocumentos();
				for(AyudaPanelDocumentos objAyudaPanelDocumentos : listaAyudaPanelDocumentos)
				{
					if(objAyudaPanelDocumentos.getCodigoTipoDoc().equals("ODSR0"))
					{
						if(objAyudaPanelDocumentos.isDocGuia() && !objAyudaPanelDocumentos.isStd())
						{
							System.out.println(" XXX Escaneado : " + objAyudaPanelDocumentos.isDocEscaneado());
							return objAyudaPanelDocumentos.isDocEscaneado();
						}						
					}					
				}
			}
			
		}
		*/
		
		return true;

	}
	
	public void obtenerListaPanelDocumento(List<GuiaDocumentaria> listaGuiaDoc){
		String personaAnt="";
		String ObligatorioAnt="";
		boolean band=false;
		List<AyudaPanelDocumentos> listAyuda = new ArrayList<AyudaPanelDocumentos>();
		List<GuiaDocumentaria> lstGuiaDocMC = new ArrayList<GuiaDocumentaria>();
		AyudaPanelDocumentos objAyuda;		
		String strEliminar="";
		String strModificar="";
		String strObservar="";
		GuiaDocumentaria guiaDocumentaria = null;
		
		Empleado empleado = null;
		
		if(getObjectSession(Constantes.USUARIO_SESION) != null){
			empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		}else{
			empleado = (Empleado) getObjectSession(Constantes.USUARIO_AD_SESION);
		}
		
		if(empleado.getPerfil() != null)
			LOG.info("Perfil-empleado"+empleado.getPerfil().getId());		
		LOG.info("listaGuiaDoc.size: " + listaGuiaDoc.size());		
		for(GuiaDocumentaria l : listaGuiaDoc){								
					LOG.info("Codigo : "+l.getTipoDocumento().getCodigo());
					LOG.info("Descripcion : "+l.getTipoDocumento().getDescripcion());
					LOG.info("Obligatorio : "+l.getObligatorio());
					LOG.info("Persona : "+l.getPersona().getCodigo());
					LOG.info("Persona : "+l.getPersona().getDescripcion());
					LOG.info("Fecha Registro : "+l.getFeRegistro());
					LOG.info("Tarea-guia : "+l.getTarea().getId());
					LOG.info("IDCM : "+l.getIdCm());
					LOG.info("Perfil-guia : "+l.getTarea().getTareaPerfiles().get(0).getPerfil().getId());
					lstGuiaDocMC.add(l);
		 }
		if(getNombreJSPPrincipal().equals("formVisualizarExpediente")) {				
			LOG.info("MODO CONSULTA. SE LIMPIA LA GUIA");
			listaGuiaDoc.clear();			
		}
		
		
		/*FIX ERIKA ABREGU 27/06/2015 */
		List<DocumentoExpTc> docExpTcList = new ArrayList<DocumentoExpTc>();
		List<DatosDocumentosExpIiceDTO> listaDocExpDTO = new ArrayList<DatosDocumentosExpIiceDTO> ();
		if (expediente != null && expediente.getId() > 0) {
			/**FIX ERIKA ABREGU 05/07/2015
			 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
			if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
				LOG.info("Metodo obtenerDatos de PanelDocumentosMB Antiguo = "+expediente.getId());
				
				//preparando parametros
				ArrayList<Object> listaParametros = new ArrayList<Object>();
				listaParametros.add(new Long(expediente.getId()));
				
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
				
				listaDocExpDTO = tablaFacadeBean.getDocumentosPorExpIICE(listaParametros);
				if (listaDocExpDTO == null) {
					listaDocExpDTO = new ArrayList<DatosDocumentosExpIiceDTO> ();
				}
				docExpTcList =  ConvertHistorial.convertToDocumentoExp(listaDocExpDTO);
				
			}else{
				docExpTcList = documentoExpTcBean.buscarPorExpedienteFlagCM(expediente.getId(), Constantes.FLAG_CM);
			}
			/**FIX ERIKA ABREGU 05/07/2015
			 * FIN DE ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
		}
		
			
		//List<DocumentoExpTc> docExpTcList = documentoExpTcBean.buscarPorExpedienteFlagCM(expediente.getId(), Constantes.FLAG_CM);
		
		// verificamos si los documentos del content pertenecen a la lista de reutilizables y ya no estan en la guia para eliminarlos de la lista
		if(!getNombreJSPPrincipal().equals("formVisualizarExpediente")) {
			List<DocumentoExpTc> docExpTcListTMP = new ArrayList<DocumentoExpTc>();
			
			FacesContext ctx = FacesContext.getCurrentInstance();
			DocumentoReutilizableMB documentoReutilizableMB = (DocumentoReutilizableMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentoReutilizable");
			List<DocumentoReutilizable> listaOriginalDocReu = documentoReutilizableMB.obtenerDatos();
			
			List<DocumentoReutilizable> lista = getObjectSession("listaNuevaDocReu") == null ? null : (List<DocumentoReutilizable>)getObjectSession("listaNuevaDocReu");
			
			if(lista != null && lista.size() > 0){
				if(listaOriginalDocReu != null && listaOriginalDocReu.size() > 0){
					for(DocumentoExpTc documentoExpTc : docExpTcList){
						boolean existeReutilizable = false;
						for(DocumentoReutilizable documentoReutilizable : listaOriginalDocReu){
							if(documentoReutilizable.getCodigoDocumento().equals(documentoExpTc.getTipoDocumento().getCodigo())){
								existeReutilizable = true;
								break;
							}
						}
						if(!existeReutilizable){
							docExpTcListTMP.add(documentoExpTc);
						}
						else{
							boolean existeGuia = false;
							for(GuiaDocumentaria guiaDoc : listaGuiaDoc){	
								if(guiaDoc.getTipoDocumento().getCodigo().equals(documentoExpTc.getTipoDocumento().getCodigo())){
									existeGuia = true;
									break;
								}
							}
							if(existeGuia){
								docExpTcListTMP.add(documentoExpTc);
							}
							else{
								DocumentoExpTc docExpTCEliminar = documentoExpTcBean.findByTipoDocisNullCM(expediente.getId(), documentoExpTc.getTipoDocumento().getId());
								documentoExpTcBean.remove(docExpTCEliminar);

								try {
									documentoExpTc.setIdCm(null);
									documentoExpTc.setPidCm(null);
									documentoExpTc.setFecReg(null);
									documentoExpTc.setFlagCm(null);
									documentoExpTc.setNombreArchivo(null);
									documentoExpTc.setFlagObs(null);
									documentoExpTc.setFlagEscaneado("0");
									documentoExpTc.setFlagDocReutilizable("0");
										
									documentoExpTcBean.edit(documentoExpTc);
									
								} catch(Exception e) {
									LOG.info("No se pudo eliminar el archivo en CM. ExcepciÃ³n.");
									LOG.error(e.getMessage(), e);
								}
							}
						}
					}
					docExpTcList = docExpTcListTMP;
				}
			}
		}
		
		LOG.info("docExpTcList: " + docExpTcList.size());
		LOG.info("listaGuiaDoc: " + listaGuiaDoc.size());
		for(DocumentoExpTc d : docExpTcList){
			guiaDocumentaria = new GuiaDocumentaria();
			if (d.getObligatorio() != null){				
				guiaDocumentaria.setObligatorio(d.getObligatorio());
			}			
			if (d.getPersona() != null){				
				guiaDocumentaria.setPersona(d.getPersona());
			}
			if (d.getTipoDocumento() != null){				
				guiaDocumentaria.setTipoDocumento(d.getTipoDocumento());	
			}
			if (d.getFecReg() != null){				
				guiaDocumentaria.setFeRegistro(d.getFecReg());
			}
			if (d.getTarea() != null){				
				guiaDocumentaria.setTarea(d.getTarea());
			}
			if (d.getIdCm().longValue() != 0){				
				guiaDocumentaria.setIdCm(d.getIdCm().longValue());
			}
			if(d.getFlagEscaneado() != null){
				guiaDocumentaria.setFlagEscaneado(d.getFlagEscaneado());
			}
						
			String nombJSP = getNombreJSPPrincipal();
			LOG.info("nombJSP:" + nombJSP);
			LOG.info("expediente.getId():" + expediente.getId());		
			if (nombJSP.equals("formRegistrarExpediente") && expediente.getId() > 0) {
				int indexGuia = 0;
				for (int i = 0; i < listaGuiaDoc.size(); i++) {
					GuiaDocumentaria guiaTmp = listaGuiaDoc.get(i);										
					if (guiaTmp.getTipoDocumento().getCodigo().equals(d.getTipoDocumento().getCodigo())) {
						indexGuia = i;
						i = listaGuiaDoc.size();
					}
				}				
				listaGuiaDoc.remove(indexGuia);
				listaGuiaDoc.add(guiaDocumentaria);				
			} else {
				listaGuiaDoc.add(guiaDocumentaria);
			}			
			
			if (d.getTipoDocumento()!=null){
				LOG.info("Codigo d : "+d.getTipoDocumento().getCodigo());
				LOG.info("Descripcion d : "+d.getTipoDocumento().getDescripcion());
			}
			if (d.getObligatorio()!=null){
				LOG.info("Obligatorio d : "+d.getObligatorio());
			}
			if (d.getPersona()!=null){
				LOG.info("Persona d : "+d.getPersona().getCodigo());
				LOG.info("Persona d : "+d.getPersona().getDescripcion());
			}
			if (d.getFecReg()!=null){
				LOG.info("Fecha Registro : "+d.getFecReg());
			}
			if (d.getTarea()!=null){
				LOG.info("Tarea-doc : "+d.getTarea().getId());
				LOG.info("Perfil-doc : "+d.getTarea().getTareaPerfiles().get(0).getPerfil().getId());
			}
			if (d.getFlagObs()!=null){
				LOG.info("Observado d : "+d.getFlagObs());
			}
		}
		
		//List<GuiaDocumentaria> listaGuiaDocumentos = ordenarListaPorPersona(listaGuiaDoc);
		List<GuiaDocumentaria> listaGuiaDocumentos = ordenarListaPorFecha(listaGuiaDoc);
		
		/**FIX ERIKA ABREGU 05/07/2015
		 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
		 */
		if(!Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
			LOG.info("Metodo obtenerDatos de PanelDocumentosMB Antiguo = "+expediente.getId());
			listaGuiaDocumentos = ordenarListaPorObligatorio(listaGuiaDoc);
		}
		
		for(GuiaDocumentaria l : listaGuiaDoc){
			if (l.getTipoDocumento() != null){
				LOG.info("Order - Codigo : "+l.getTipoDocumento().getCodigo());
				LOG.info("Order - Descripcion : "+l.getTipoDocumento().getDescripcion());
			}
			if (l.getObligatorio() != null){
				LOG.info("Order - Obligatorio : "+l.getObligatorio());
			}
			if (l.getPersona() != null){
				LOG.info("Order - Persona : "+l.getPersona().getCodigo());
				LOG.info("Order - Persona : "+l.getPersona().getDescripcion());
			}
			if (l.getFeRegistro() != null){
				LOG.info("Order - Fecha Registro : "+l.getFeRegistro());
			}
			if (l.getTarea() != null){
				LOG.info("Tarea-guia2 : "+l.getTarea().getId());
				LOG.info("Perfil-guia2 : "+l.getTarea().getTareaPerfiles().get(0).getPerfil().getId());
			}
		}
				
		LOG.info("listaGuiaDocumentos.size:" + listaGuiaDocumentos.size());
		for(GuiaDocumentaria ldg : listaGuiaDocumentos){
			objAyuda = new AyudaPanelDocumentos();	
			
			/**FIX ERIKA ABREGU 05/07/2015
			 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
			if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
				LOG.info("Llenar objAyudacon datos de IICE en PanelDocumentosMB = "+expediente.getId());
				
				if(ldg!=null && ldg.getPersona()!=null && ldg.getPersona().getCodigo()!=null){
					if (personaAnt.equals("") || !ldg.getPersona().getCodigo().trim().equals(personaAnt.trim())){
						personaAnt=ldg.getPersona().getCodigo().trim();
						objAyuda.setStrPersona(ldg.getPersona().getDescripcion());
						
						if(strEliminar.trim().equals("") && !isOpEliminar()){						
							objAyuda.setColEliminar(this.colEliminarTipDoc);
							strEliminar=this.colEliminarTipDoc;
						}
						
						if(strModificar.trim().equals("") && !isOpModificar()){
							objAyuda.setColModificar(this.colModificarTipDoc);
							strModificar=this.colModificarTipDoc;
						}
						
						band=true;					
					}
					
					//if (!personaAnt.equals("") && (ObligatorioAnt.equals("") || !ldg.getObligatorio().trim().equals(ObligatorioAnt)) || band){
					//	ObligatorioAnt=ldg.getObligatorio().trim();
						
					//	objAyuda.setDescripcionTipoDoc(obtenerValorOblig(ObligatorioAnt));
					//	if (band==false)
					//		objAyuda.setStrPersona("");					
							
					//	objAyuda.setStd(true);
					//	listAyuda.add(objAyuda);
					//}
					
					//objAyuda = new AyudaPanelDocumentos();
					
					if (!personaAnt.equals("")){
						if(ldg.getPersona().getCodigo().trim().equals(personaAnt.trim())){
							objAyuda.setStrPersona("");
							//if(ldg.getObligatorio().trim().equals(ObligatorioAnt)){
								objAyuda.setDescripcionTipoDoc(ldg.getTipoDocumento().getDescripcion());
								objAyuda.setCodigoTipoDoc(ldg.getTipoDocumento().getCodigo());
								objAyuda.setStd(false);
								objAyuda.setObjGuiaDocumentaria(ldg);
								
								LOG.info("ldg.getIdCm(): " + ldg.getIdCm());
								
								if (ldg.getIdCm() > 0) {
									if (ldg.getFeRegistro()!=null) {
			         					SimpleDateFormat dateFormat = new SimpleDateFormat(Constantes.TEXTO_FORMATO_FECHA);
		         					
			         					try {
											objAyuda.setFeRegistro(dateFormat.parse(dateFormat.format(ldg.getFeRegistro())));
										} catch (ParseException e) {
											LOG.error(e.getMessage(), e);
										}
			         					objAyuda.setDocEscaneado(true);
		         					}
									
									
									
								} else {
									objAyuda.setDocEscaneado(false);
								}
									
								
								objAyuda.setDocTransferencias(true);
								objAyuda.setVisOpElimina(false);	
								objAyuda.setVisOpModificar(false);

								objAyuda.setVisOpElimina(false);
								objAyuda.setVisOpModificar(false);
								objAyuda.setVisOpObservar(false);
						
								boolean visOpEliminaTmp = false;
								boolean docGuiaTmp = false;
								for (GuiaDocumentaria objDocumentaria: lstGuiaDocMC) {
									LOG.info("objDocumentaria:" + objDocumentaria.getTipoDocumento().getCodigo() + 
											" - " + objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo());
									if (objDocumentaria.getTipoDocumento().getCodigo()
											.equals(objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo())) {
										visOpEliminaTmp = true;
										docGuiaTmp = true;
										break;
									}
								}
								
								objAyuda.setDocGuia(docGuiaTmp);
								objAyuda.setVisOpElimina(visOpEliminaTmp);								
								objAyuda.setIdCm(ldg.getIdCm());
								objAyuda.setColObservar(false);
								
								//Observacion de Guia
								//if (objAyuda.getIdCm() != 0) {
								//	for (DocumentoExpTc objDocExpTc : docExpTcList) {
								//		if (objDocExpTc.getIdCm() != null && objDocExpTc.getFlagObs() != null && objAyuda.getIdCm() == objDocExpTc.getIdCm().longValue()) {
								//			boolean colObservacion = objDocExpTc.getFlagObs().equals("1")? true: false;
								//			objAyuda.setColObservar(colObservacion);
								//			break;
								//		}
								//	}
								//} else {
								//	objAyuda.setColObservar(false);
								//}
															
								//boolean visOpObservarTmp = true;
								
								objAyuda.setVisOpObservar(false);
								objAyuda.setFlagEscaneado(ldg.getFlagEscaneado());
								
								LOG.info("Descripcion : "+ldg.getTipoDocumento().getDescripcion());
								LOG.info("VisOpElimina: "+objAyuda.isVisOpElimina());
								LOG.info("opEliminar : "+opEliminar);
								LOG.info("VisOpObservar:" + objAyuda.isVisOpObservar());
								LOG.info("opObservar : " + opObservar);
								LOG.info("DocEscaneado : "+objAyuda.isDocEscaneado());
								LOG.info("DocTransferencias : "+objAyuda.isDocTransferencias());
																
								listAyuda.add(objAyuda);
								band=false;	
							//}		
						}
					}
				
				}
				
				
				
				
			}else{
				if(ldg!=null && ldg.getPersona()!=null && ldg.getPersona().getCodigo()!=null){
					if (personaAnt.equals("") || !ldg.getPersona().getCodigo().trim().equals(personaAnt.trim())){
						personaAnt=ldg.getPersona().getCodigo().trim();
						objAyuda.setStrPersona(ldg.getPersona().getDescripcion());
						
						if(strEliminar.trim().equals("") && !isOpEliminar()){						
							objAyuda.setColEliminar(this.colEliminarTipDoc);
							strEliminar=this.colEliminarTipDoc;
						}
						
						if(strModificar.trim().equals("") && !isOpModificar()){
							objAyuda.setColModificar(this.colModificarTipDoc);
							strModificar=this.colModificarTipDoc;
						}
						
						band=true;					
					}
					
					if (!personaAnt.equals("") && (ObligatorioAnt.equals("") || !ldg.getObligatorio().trim().equals(ObligatorioAnt)) || band){
						ObligatorioAnt=ldg.getObligatorio().trim();
						
						objAyuda.setDescripcionTipoDoc(obtenerValorOblig(ObligatorioAnt));
						if (band==false)
							objAyuda.setStrPersona("");					
							
						objAyuda.setStd(true);
						listAyuda.add(objAyuda);
					}			
					objAyuda = new AyudaPanelDocumentos();
					
					if (!personaAnt.equals("") && !ObligatorioAnt.equals("")){
						if(ldg.getPersona().getCodigo().trim().equals(personaAnt.trim())){
							objAyuda.setStrPersona("");
							if(ldg.getObligatorio().trim().equals(ObligatorioAnt)){
								objAyuda.setDescripcionTipoDoc(ldg.getTipoDocumento().getDescripcion());
								objAyuda.setCodigoTipoDoc(ldg.getTipoDocumento().getCodigo());
								objAyuda.setStd(false);
								objAyuda.setObjGuiaDocumentaria(ldg);
								
								LOG.info("ldg.getIdCm(): " + ldg.getIdCm());
								
								if (ldg.getIdCm() > 0) {
									if (ldg.getFeRegistro()!=null) {
			         					SimpleDateFormat dateFormat = new SimpleDateFormat(Constantes.TEXTO_FORMATO_FECHA);
		         					
			         					try {
											objAyuda.setFeRegistro(dateFormat.parse(dateFormat.format(ldg.getFeRegistro())));
										} catch (ParseException e) {
											LOG.error(e.getMessage(), e);
										}
			         					objAyuda.setDocEscaneado(true);
		         					}
								} else {
									objAyuda.setDocEscaneado(false);
								}
									
								LOG.info("strListaDocsTransferencias:" + strListaDocsTransferencias);
								if (strListaDocsTransferencias != null && !strListaDocsTransferencias.trim().equals("")) {
									List<String> listaDocsTransferencias = Arrays.asList(strListaDocsTransferencias.split(","));
									for (String tipoDocTrans : listaDocsTransferencias) {
										LOG.info("tipoDocTrans: " + listaGuiaDoc + " - " + ldg.getTipoDocumento().getCodigo());
										if (tipoDocTrans.equals(ldg.getTipoDocumento().getCodigo())) {
											long idCmObservado = obtenerIdCmObservado(ldg, docExpTcList);
											if(idCmObservado != 0){
												// Se quita la observacion y se pone docTransferencias = true
												// Si es la guia observada
												if (idCmObservado == ldg.getIdCm()) {
													// Se quita la observacion
													objAyuda.setDocEscaneado(true);
													objAyuda.setDocTransferencias(false);
													DocumentoExpTc docExpTC;
													docExpTC = documentoExpTcBean.buscarPorId(idCmObservado);
													docExpTC.setFlagObs(Constantes.FLAG_OBS_DOCEXPTC_NOACTIVO);
													documentoExpTcBean.edit(docExpTC);
													docExpTcList = documentoExpTcBean.buscarPorExpedienteFlagCM(expediente.getId(), Constantes.FLAG_CM);
													LOG.info("Se quito la observacion para la guia con IDCM:" + idCmObservado);
												}
											}else{
												if (ldg.getFeRegistro()==null) {
													objAyuda.setDocTransferencias(true);
													break;
												}
											}
										}
									}
								}
								
								objAyuda.setVisOpElimina(false);	
								objAyuda.setVisOpModificar(false);
								objAyuda.setVisOpObservar(true);
								
								if(empleado.getPerfil() != null){
									if (empleado.getPerfil().getId()!=0 && ldg.getTarea().getTareaPerfiles().get(0).getPerfil().getId()!=0){
										if (empleado.getPerfil().getId()==ldg.getTarea().getTareaPerfiles().get(0).getPerfil().getId()){
											LOG.info("Entro peril");
											objAyuda.setVisOpElimina(true);
											objAyuda.setVisOpModificar(true);
											objAyuda.setVisOpObservar(false);
										}	
									}
								}

								boolean visOpEliminaTmp = false;
								boolean docGuiaTmp = false;
								for (GuiaDocumentaria objDocumentaria: lstGuiaDocMC) {
									LOG.info("objDocumentaria:" + objDocumentaria.getTipoDocumento().getCodigo() + 
											" - " + objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo());
									if (objDocumentaria.getTipoDocumento().getCodigo()
											.equals(objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo())) {
										visOpEliminaTmp = true;
										docGuiaTmp = true;
										break;
									}
								}
								
								objAyuda.setDocGuia(docGuiaTmp);
								objAyuda.setVisOpElimina(visOpEliminaTmp);								
								objAyuda.setIdCm(ldg.getIdCm());
								
								//Observacion de Guia
								if (objAyuda.getIdCm() != 0) {
									for (DocumentoExpTc objDocExpTc : docExpTcList) {
										if (objDocExpTc.getIdCm() != null && objDocExpTc.getFlagObs() != null && objAyuda.getIdCm() == objDocExpTc.getIdCm().longValue()) {
											boolean colObservacion = objDocExpTc.getFlagObs().equals("1")? true: false;
											objAyuda.setColObservar(colObservacion);
											break;
										}
									}
								} else {
									objAyuda.setColObservar(false);
								}
															
								boolean visOpObservarTmp = true;
								LOG.info("objAyuda.isDocEscaneado():" + objAyuda.isDocEscaneado());
								if (objAyuda.isDocEscaneado()) {
									visOpObservarTmp = esObservableGuiaDoc(objAyuda, docExpTcList, lstGuiaDocMC);
								}
								
								objAyuda.setVisOpObservar(visOpObservarTmp);
								objAyuda.setFlagEscaneado(ldg.getFlagEscaneado());
								
								LOG.info("Descripcion : "+ldg.getTipoDocumento().getDescripcion());
								LOG.info("VisOpElimina: "+objAyuda.isVisOpElimina());
								LOG.info("opEliminar : "+opEliminar);
								LOG.info("VisOpObservar:" + objAyuda.isVisOpObservar());
								LOG.info("opObservar : " + opObservar);
								LOG.info("DocEscaneado : "+objAyuda.isDocEscaneado());
								LOG.info("DocTransferencias : "+objAyuda.isDocTransferencias());
																
								listAyuda.add(objAyuda);
								band=false;	
							}		
						}
					}
				}
			}
		
			
		}	
		strListaDocsObservados = obtenerDocsObservados(listAyuda);
		strListaIdcmDocsObservados = obtenerIdcmDocsObservados(listAyuda);
		LOG.info("strListaDocsObervados:" + strListaDocsObservados);
		LOG.info("strListaIdcmDocsObservados:" + strListaIdcmDocsObservados);
		
		lstPersonaPDs = new ArrayList<PersonaPD>();
		
		//FIX2 ERIKA ABREGU
		if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
			LOG.info("Metodo obtenerListaPanelDocumentoAgrupada para IICE en PanelDocumentosMB = "+expediente.getId());
			PersonaPD objPersonaPD = new PersonaPD();
			List<TipoDocumentoPD> lstTipoDocumentoPDs = new ArrayList<TipoDocumentoPD>();
			
			Persona persona = new Persona();
			persona.setId(1);
			persona.setDescripcion("TITULAR");
			objPersonaPD.setPersona(persona);
			
			
			for(AyudaPanelDocumentos objAyudaPD :listAyuda){
				if(objAyudaPD!=null){
					List<AyudaPanelDocumentos> lstAyudaPanelDocumentos = new ArrayList<AyudaPanelDocumentos>();
					TipoDocumento tipoDoc = new TipoDocumento();
					TipoDocumentoPD tipoPersPD = new TipoDocumentoPD();
					tipoDoc.setCodigo(objAyudaPD.getCodigoTipoDoc()!=null?objAyudaPD.getCodigoTipoDoc():null);
					tipoDoc.setDescripcion(objAyudaPD.getDescripcionTipoDoc()!=null? objAyudaPD.getDescripcionTipoDoc():null);
					tipoPersPD.setTipoDocumento(tipoDoc);
					objAyudaPD.setDocTransferencias(false);
					lstAyudaPanelDocumentos.add(objAyudaPD);
					tipoPersPD.setLstAyudaPanelDocumentos(lstAyudaPanelDocumentos);
					lstTipoDocumentoPDs.add(tipoPersPD);
				}
			}
			
			objPersonaPD.setLstTipoDocumentoPDs(lstTipoDocumentoPDs);
			lstPersonaPDs.add(objPersonaPD);
			LOG.info("Guia Persona de IICE:" + persona.getCodigo() + " - " + persona.getDescripcion());
		}else{
			lstPersonaPDs = obtenerListaPanelDocumentoAgrupada(lstGuiaDocMC);
		}
		
		this.setListaAyudaPanelDocumentos(listAyuda);		
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		addObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS, listAyuda);
		removeObjectSession(Constantes.LISTA_AYUDA_AGR_PANEL_DOCUMENTOS);
		addObjectSession(Constantes.LISTA_AYUDA_AGR_PANEL_DOCUMENTOS, lstPersonaPDs);

	}
	
	

	public List<PersonaPD> obtenerListaPanelDocumentoAgrupada(List<GuiaDocumentaria> listaGuiaDocAgr){

		List<GuiaDocumentaria> lstGuiaDocMC = new ArrayList<GuiaDocumentaria>();
		List<PersonaPD> lstPersonaPDs = new ArrayList<PersonaPD>();
		
		AyudaPanelDocumentos objAyuda;
		PersonaPD objPersonaPD;	
		GuiaDocumentaria guiaDocumentaria = null;
		
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
		
		if(empleado.getPerfil() != null)
			LOG.info("Perfil-empleado"+empleado.getPerfil().getId());		
		LOG.info("listaGuiaDocAgr.size: " + listaGuiaDocAgr.size());		
		for(GuiaDocumentaria l : listaGuiaDocAgr){								
					LOG.info("Codigo : "+l.getTipoDocumento().getCodigo());
					LOG.info("Descripcion : "+l.getTipoDocumento().getDescripcion());
					LOG.info("Obligatorio : "+l.getObligatorio());
					LOG.info("Persona : "+l.getPersona().getCodigo());
					LOG.info("Persona : "+l.getPersona().getDescripcion());
					LOG.info("Fecha Registro : "+l.getFeRegistro());
					LOG.info("Tarea-guia : "+l.getTarea().getId());
					LOG.info("IDCM : "+l.getIdCm());
					LOG.info("Perfil-guia : "+l.getTarea().getTareaPerfiles().get(0).getPerfil().getId());
					lstGuiaDocMC.add(l);
		 }
		
		if(getNombreJSPPrincipal().equals("formVisualizarExpediente")) {				
			LOG.info("MODO CONSULTA. SE LIMPIA LA GUIA");
			listaGuiaDocAgr.clear();			
		}
		
		List<DocumentoExpTc> docExpTcList = documentoExpTcBean.buscarPorExpedienteFlagCM(expediente.getId(), Constantes.FLAG_CM);
		
		if(getNombreJSPPrincipal().equals("formVisualizarExpediente")) {
			docExpTcList = documentoExpTcBean.buscarPorExpedienteFlagEscaneado(expediente.getId(), Constantes.FLAG_ESCANEADO);
		}
		
		// verificamos si los documentos del content pertenecen a la lista de reutilizables y ya no estan en la guia para eliminarlos de la lista
		if(!getNombreJSPPrincipal().equals("formVisualizarExpediente")) {
			List<DocumentoExpTc> docExpTcListTMP = new ArrayList<DocumentoExpTc>();
			
			FacesContext ctx = FacesContext.getCurrentInstance();
			DocumentoReutilizableMB documentoReutilizableMB = (DocumentoReutilizableMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentoReutilizable");
			List<DocumentoReutilizable> listaOriginalDocReu = documentoReutilizableMB.obtenerDatos();
			
			List<DocumentoReutilizable> lista = getObjectSession("listaNuevaDocReu") == null ? null : (List<DocumentoReutilizable>)getObjectSession("listaNuevaDocReu");
			
			if(lista != null && lista.size() > 0){
				if(listaOriginalDocReu != null && listaOriginalDocReu.size() > 0){
					for(DocumentoExpTc documentoExpTc : docExpTcList){
						boolean existeReutilizable = false;
						for(DocumentoReutilizable documentoReutilizable : listaOriginalDocReu){
							if(documentoReutilizable.getCodigoDocumento().equals(documentoExpTc.getTipoDocumento().getCodigo())){
								existeReutilizable = true;
								break;
							}
						}
						if(!existeReutilizable){
							docExpTcListTMP.add(documentoExpTc);
						}
						else{
							boolean existeGuia = false;
							for(GuiaDocumentaria guiaDoc : listaGuiaDoc){
								if(guiaDoc.getTipoDocumento().getCodigo().equals(documentoExpTc.getTipoDocumento().getCodigo())){
									existeGuia = true;
									break;
		
								}
							}
							if(existeGuia){
								docExpTcListTMP.add(documentoExpTc);
							}
							else{
								DocumentoExpTc docExpTCEliminar = documentoExpTcBean.findByTipoDocisNullCM(expediente.getId(), documentoExpTc.getTipoDocumento().getId());
								documentoExpTcBean.remove(docExpTCEliminar);

								try {
									documentoExpTc.setIdCm(null);
									documentoExpTc.setPidCm(null);
									documentoExpTc.setFecReg(null);
									documentoExpTc.setFlagCm(null);
									documentoExpTc.setNombreArchivo(null);
									documentoExpTc.setFlagObs(null);
									documentoExpTc.setFlagEscaneado("0");
									documentoExpTc.setFlagDocReutilizable("0");
										
									documentoExpTcBean.edit(documentoExpTc);
								} catch(Exception e) {
									LOG.info("No se pudo eliminar el archivo en CM. ExcepciÃ³n.");
									LOG.error(e.getMessage(), e);
								}
							}
						}
					}
					docExpTcList = docExpTcListTMP;
				}
			}
		}
		
		LOG.info("docExpTcList: " + docExpTcList.size());
		LOG.info("listaGuiaDoc: " + listaGuiaDoc.size());
		for(DocumentoExpTc d : docExpTcList){
			guiaDocumentaria = new GuiaDocumentaria();
			if (d.getObligatorio() != null){				
				guiaDocumentaria.setObligatorio(d.getObligatorio());
			}			
			if (d.getPersona() != null){				
				guiaDocumentaria.setPersona(d.getPersona());
			}
			if (d.getTipoDocumento() != null){				
				guiaDocumentaria.setTipoDocumento(d.getTipoDocumento());	
			}
			if (d.getFecReg() != null){				
				guiaDocumentaria.setFeRegistro(d.getFecReg());
			}
			if (d.getTarea() != null){				
				guiaDocumentaria.setTarea(d.getTarea());
			}
			if(d.getIdCm() != null){
				if (d.getIdCm().longValue() != 0){				
					guiaDocumentaria.setIdCm(d.getIdCm().longValue());
				}
			}else{
				guiaDocumentaria.setIdCm(0);
			}
			if(d.getFlagEscaneado() != null){
				guiaDocumentaria.setFlagEscaneado(d.getFlagEscaneado());
			}
						
			String nombJSP = getNombreJSPPrincipal();
			LOG.info("nombJSP:" + nombJSP);
			LOG.info("expediente.getId():" + expediente.getId());		
			if (nombJSP.equals("formRegistrarExpediente") && expediente.getId() > 0) {
				int indexGuia = 0;
				for (int i = 0; i < listaGuiaDocAgr.size(); i++) {
					GuiaDocumentaria guiaTmp = listaGuiaDocAgr.get(i);										
					if (guiaTmp.getTipoDocumento().getCodigo().equals(d.getTipoDocumento().getCodigo())) {
						indexGuia = i;
						i = listaGuiaDocAgr.size();
					}
				}				
				listaGuiaDocAgr.remove(indexGuia);
				listaGuiaDocAgr.add(guiaDocumentaria);				
			} else {
				listaGuiaDocAgr.add(guiaDocumentaria);
			}			
			
			if (d.getTipoDocumento()!=null){
				LOG.info("Codigo d : "+d.getTipoDocumento().getCodigo());
				LOG.info("Descripcion d : "+d.getTipoDocumento().getDescripcion());
			}
			if (d.getObligatorio()!=null){
				LOG.info("Obligatorio d : "+d.getObligatorio());
			}
			if (d.getPersona()!=null){
				LOG.info("Persona d : "+d.getPersona().getCodigo());
				LOG.info("Persona d : "+d.getPersona().getDescripcion());
			}
			if (d.getFecReg()!=null){
				LOG.info("Fecha Registro : "+d.getFecReg());
			}
			if (d.getTarea()!=null){
				LOG.info("Tarea-doc : "+d.getTarea().getId());
				LOG.info("Perfil-doc : "+d.getTarea().getTareaPerfiles().get(0).getPerfil().getId());
			}
			if (d.getFlagObs()!=null){
				LOG.info("Observado d : "+d.getFlagObs());
			}
		}
		
		List<GuiaDocumentaria> listaGuiaDocumentosAgr = ordenarListaPorFecha(listaGuiaDocAgr);
		LOG.info("listaGuiaDocumentosAgr.size:" + listaGuiaDocumentosAgr.size());
		Set<String> lstCodigoPersona = new HashSet<String>();
		for(GuiaDocumentaria l : listaGuiaDocumentosAgr){
			if (l.getTipoDocumento()!=null){
				LOG.info("Order - Codigo : "+l.getTipoDocumento().getCodigo());
				LOG.info("Order - Descripcion : "+l.getTipoDocumento().getDescripcion());
			}
			if (l.getObligatorio()!=null){
				LOG.info("Order - Obligatorio : "+l.getObligatorio());
			}
			if (l.getPersona()!=null){
				LOG.info("Order - Persona : "+l.getPersona().getCodigo());
				LOG.info("Order - Persona : "+l.getPersona().getDescripcion());
				lstCodigoPersona.add(l.getPersona().getCodigo());
			}
			if (l.getFeRegistro()!=null){
				LOG.info("Order - Fecha Registro : "+l.getFeRegistro());
			}
			if (l.getTarea()!=null){
				LOG.info("Tarea-guia2 : "+l.getTarea().getId());
				LOG.info("Perfil-guia2 : "+l.getTarea().getTareaPerfiles().get(0).getPerfil().getId());
			}
			if (l.getTipoDocumento() !=null){
				LOG.info("Order - TipoDocumento : " + l.getTipoDocumento().getId());
				LOG.info("Order - TipoDocumento : " + l.getTipoDocumento().getDescripcion());
			}
		}
		if(lstCodigoPersona!=null)
			LOG.info("lstCodigoPersona tamaño::::" +lstCodigoPersona.size());
		//AgrupaciÃ³n por Persona.
		for (String codigoPersona : lstCodigoPersona) {
			objPersonaPD = new PersonaPD();
			Persona persona = guiaDocumentariaBean.obtenerPersonaPorCodigo(codigoPersona);
			objPersonaPD.setPersona(persona);
			lstPersonaPDs.add(objPersonaPD);
			LOG.info("Guia Persona:" + persona.getCodigo() + " - " + persona.getDescripcion());
		}
		
		List<TipoDocumento> listTipoDocTmp= tipoDocumentoBean.buscar();
		
		// Agrupación por tipo de Documento.
		for (PersonaPD objPd : lstPersonaPDs) {			
			Set<String> lstCodigoTipoDocumento = new HashSet<String>();
			for (GuiaDocumentaria objGd : listaGuiaDocumentosAgr) {
				if (objGd.getPersona().getCodigo().equals(objPd.getPersona().getCodigo())) {
					String strTipoDoc = objGd.getTipoDocumento().getCodigo();
					strTipoDoc = strTipoDoc.concat(",").concat(objGd.getObligatorio());
					lstCodigoTipoDocumento.add(strTipoDoc);
				}
			}
			
			for (String tipoDoc : lstCodigoTipoDocumento) {
				String[] arrTipoDoc = tipoDoc.split(",");
				TipoDocumentoPD tipoDocumentoPD = new TipoDocumentoPD();
				
				//TipoDocumento tipoDocumento = tipoDocumentoBean.buscarPorCodigo(arrTipoDoc[0]);
				TipoDocumento tipoDocumento =obtenerTipoDoc(arrTipoDoc[0], listTipoDocTmp);
				tipoDocumentoPD.setTipoDocumento(tipoDocumento);
				tipoDocumentoPD.setObligatorio(arrTipoDoc[1]);
				objPd.addTipoDocumentoPD(tipoDocumentoPD);
				if(tipoDocumento!=null)
					LOG.info("Guia Tipo Documento :" + tipoDocumento.getId() + " - " + tipoDocumento.getCodigo() + " - " + tipoDocumento.getDescripcion() + " - " + tipoDocumentoPD.getObligatorio());
				else
					LOG.info("tipoDocumento es nulo");
			}
			//listTipoDocTmp=null; // no entiendo por qué ponían esta variable en null pero se cae si el for es de más de 1 por obvias razones
			
			//Se ordena por tipo de documento (alfabeticamente) y por obligatoriedad
			objPd.ordernarTipoDocumentos();
			
			// Agregamos los documentos asociados al tipo de documento
			for (TipoDocumentoPD objTipoDocPD : objPd.getLstTipoDocumentoPDs()) {				
				
				for (GuiaDocumentaria ldg : listaGuiaDocumentosAgr) {
					if (objTipoDocPD.getTipoDocumento().getCodigo().equals(ldg.getTipoDocumento().getCodigo())) {
						
						objAyuda = new AyudaPanelDocumentos();		
				
						objAyuda.setDescripcionTipoDoc(ldg.getTipoDocumento().getDescripcion());
						objAyuda.setCodigoTipoDoc(ldg.getTipoDocumento().getCodigo());
						boolean isObligatorio = ldg.getObligatorio().equals(Constantes.OP_DOC_OBLIGATORIO)? false : true; 
						objAyuda.setStd(isObligatorio);
						objAyuda.setObjGuiaDocumentaria(ldg);
						
						LOG.info("ldg.getIdCm(): " + ldg.getIdCm());
						LOG.info("tipo doc = "+ldg.getTipoDocumento().getCodigo());								
						
						if (ldg.getIdCm() > 0) {
							if (ldg.getFeRegistro()!=null) {
	         					SimpleDateFormat dateFormat = new SimpleDateFormat(Constantes.TEXTO_FORMATO_FECHA);
         					
	         					try {
									objAyuda.setFeRegistro(dateFormat.parse(dateFormat.format(ldg.getFeRegistro())));
								} catch (ParseException e) {
									LOG.error(e.getMessage(), e);
								}
	         					objAyuda.setDocEscaneado(true);
         					}
						} else {
							objAyuda.setDocEscaneado(false);
						}
							
						LOG.info("strListaDocsTransferencias:" + strListaDocsTransferencias);
						if (strListaDocsTransferencias != null && !strListaDocsTransferencias.trim().equals("")) {
							List<String> listaDocsTransferencias = Arrays.asList(strListaDocsTransferencias.split(","));
							for (String tipoDocTrans : listaDocsTransferencias) {
								LOG.info("tipoDocTrans: " + listaGuiaDocAgr + " - " + ldg.getTipoDocumento().getCodigo());
								if (tipoDocTrans.equals(ldg.getTipoDocumento().getCodigo())) {
									long idCmObservado = obtenerIdCmObservado(ldg, docExpTcList);
									if(idCmObservado != 0){
										// Se quita la observacion y se pone docTransferencias = true
										// Si es la guia observada
										if (idCmObservado == ldg.getIdCm()) {
											// Se quita la observacion
											objAyuda.setDocEscaneado(true);
											objAyuda.setDocTransferencias(false);
											DocumentoExpTc docExpTC;
											docExpTC = documentoExpTcBean.buscarPorId(idCmObservado);
											docExpTC.setFlagObs(Constantes.FLAG_OBS_DOCEXPTC_NOACTIVO);
											documentoExpTcBean.edit(docExpTC);
											docExpTcList = documentoExpTcBean.buscarPorExpedienteFlagCM(expediente.getId(), Constantes.FLAG_CM);
											LOG.info("Se quito la observacion para la guia con IDCM:" + idCmObservado);
										}
									}else{
										if (ldg.getFeRegistro()==null) {
											objAyuda.setDocTransferencias(true);
											break;
										}
									}
								}
							}
						}
						
						objAyuda.setVisOpElimina(false);	
						objAyuda.setVisOpModificar(false);
						objAyuda.setVisOpObservar(true);
						
						if(empleado.getPerfil() != null){
							if (empleado.getPerfil().getId()!=0 && ldg.getTarea().getTareaPerfiles().get(0).getPerfil().getId()!=0){
								if (empleado.getPerfil().getId()==ldg.getTarea().getTareaPerfiles().get(0).getPerfil().getId()){
									LOG.info("Entro peril");
									objAyuda.setVisOpElimina(true);
									objAyuda.setVisOpModificar(true);
									objAyuda.setVisOpObservar(false);
								}	
							}
						}

						boolean visOpEliminaTmp = false;
						boolean docGuiaTmp = false;
						for (GuiaDocumentaria objDocumentaria: lstGuiaDocMC) {
							LOG.info("objDocumentaria:" + objDocumentaria.getTipoDocumento().getCodigo() + 
									" - " + objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo());
							if (objDocumentaria.getTipoDocumento().getCodigo()
									.equals(objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo())) {
								visOpEliminaTmp = true;
								docGuiaTmp = true;
								break;
							}
						}
						
						objAyuda.setDocGuia(docGuiaTmp);
						objAyuda.setVisOpElimina(visOpEliminaTmp);								
						objAyuda.setIdCm(ldg.getIdCm());
						//objAyuda.setIdDocExp(ldg.get)
						
						//Observacion de Guia
						if (objAyuda.getIdCm() != 0) {
							for (DocumentoExpTc objDocExpTc : docExpTcList) {
								if (objDocExpTc.getIdCm() != null && objDocExpTc.getFlagObs() != null && objAyuda.getIdCm() == objDocExpTc.getIdCm().longValue()) {
									boolean colObservacion = objDocExpTc.getFlagObs().equals("1")? true: false;
									objAyuda.setColObservar(colObservacion);
									break;
								}
							}
						} else {
							objAyuda.setColObservar(false);
						}
													
						
						boolean visOpObservarTmp = true;
						LOG.info("objAyuda.isDocEscaneado():" + objAyuda.isDocEscaneado());
						if (objAyuda.isDocEscaneado()) {
							visOpObservarTmp = esObservableGuiaDoc(objAyuda, docExpTcList, lstGuiaDocMC);
						}
						
						objAyuda.setVisOpObservar(visOpObservarTmp);
						
						objAyuda.setFlagEscaneado(ldg.getFlagEscaneado());
						
						LOG.info("Descripcion : "+ldg.getTipoDocumento().getDescripcion());
						LOG.info("VisOpElimina: "+objAyuda.isVisOpElimina());
						LOG.info("opEliminar : "+opEliminar);
						LOG.info("VisOpObservar:" + objAyuda.isVisOpObservar());
						LOG.info("opObservar : " + opObservar);
						LOG.info("DocEscaneado : "+objAyuda.isDocEscaneado());
						LOG.info("DocTransferencias : "+objAyuda.isDocTransferencias());
						LOG.info("Std : " + objAyuda.isStd());

						objTipoDocPD.addAyudaPanelDocumento(objAyuda);
					}
				}
			}
		}
		
		return lstPersonaPDs;

	}
	
	private TipoDocumento obtenerTipoDoc(String codigo, List<TipoDocumento> listTipoDocTmp){
		TipoDocumento objResult=null;
		
		for(TipoDocumento objTipoDoc: listTipoDocTmp){
			if(objTipoDoc!=null){
				if(objTipoDoc.getCodigo().equals(codigo)){
					objResult= objTipoDoc;
					break;
				}
			}
		}	
		if(objResult!=null)
			LOG.info("objTipoDoc.getCodigo():::"+objResult.getCodigo());
		else
			LOG.info("objResult es nulo");
		
		return objResult;
	}

	private long obtenerIdCmObservado(GuiaDocumentaria ldg, List<DocumentoExpTc> docExpTcList) {
		
		LOG.info("obtenerIdCmObservado::::docExpTcList: " + docExpTcList.size());
		long idCmObs = 0L;
		for (DocumentoExpTc documentoExpTc : docExpTcList) {
			String flagObs = documentoExpTc.getFlagObs();
			documentoExpTc.setFlagObs(flagObs == null? Constantes.FLAG_OBS_DOCEXPTC_NOACTIVO: flagObs);
			if (ldg.getTipoDocumento().getCodigo().equals(documentoExpTc.getTipoDocumento().getCodigo())
					&& documentoExpTc.getFlagObs().equals(Constantes.FLAG_OBS_DOCEXPTC_ACTIVO)) {
				idCmObs = documentoExpTc.getIdCm().longValue();
				break;
			}
		}
		return idCmObs;
	}

	private boolean esObservableGuiaDoc(AyudaPanelDocumentos objAyuda, List<DocumentoExpTc> docExpTcList, List<GuiaDocumentaria> lstGuiaDocuMC) {
		
		boolean observable = false;
		
		try{
			
			List<DocumentoExpTc> lstDocExpAdj = new ArrayList<DocumentoExpTc>();
			LOG.info("objAyuda.getCodigoTipoDoc(): " + objAyuda.getCodigoTipoDoc());
			LOG.info("docExpTcList.size(): " + docExpTcList.size());
			LOG.info("objAyuda.getIdCm():" + objAyuda.getIdCm());
			if (objAyuda.getIdCm() != 0) {
				for (DocumentoExpTc objDocExpTc : docExpTcList) {
					LOG.info("objDocExpTc.getTipoDocumento(): " + objDocExpTc.getTipoDocumento());
					LOG.info("objDocExpTc.getTipoDocumento().getCodigo():" + objDocExpTc.getTipoDocumento().getCodigo());
					if (objDocExpTc.getTipoDocumento().getCodigo().equals(objAyuda.getCodigoTipoDoc())) {
						lstDocExpAdj.add(objDocExpTc);
					}
				}
				
				LOG.info("lstDocExpAdj.size(): " + lstDocExpAdj.size());
				
				if (lstDocExpAdj.size() >= 1) {
					lstDocExpAdj = ordenarListaDocExpTCPorFecha(lstDocExpAdj);
					//Se obtiene la guÃ­a mas reciente.
					DocumentoExpTc objDocumentoExpTc = lstDocExpAdj.get(0);
					
					//Obtenemos la tarea 
					Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
					List<TareaPerfil> lstTareaPerfil = new ArrayList<TareaPerfil>();
					
					if(empleado.getPerfil() != null)
						lstTareaPerfil = tareaPerfilBean.buscarPorIdPerfil(empleado.getPerfil().getId());
						
					/**
					 * En ciertos casos se espera un NullPointerException que resulta
					 * cuando se hace clic en un expediente de la bandeja de históricos.
					 * 
					 * Se modificó el presente código para capturar esa excepción.
					 */
					
					if(objAyuda != null){
						if(objDocumentoExpTc != null && objDocumentoExpTc.getIdCm() != null) {
							if (objAyuda.getIdCm() == objDocumentoExpTc.getIdCm().longValue()) {
								// Se evalua si la guia pertenece a la tarea del perfil del empleado
								boolean perteneceTareaPerfil = false;
								for (TareaPerfil tareaPerfil : lstTareaPerfil) {
									if (tareaPerfil.getTarea().getId() == objDocumentoExpTc.getTarea().getId()) {
										perteneceTareaPerfil = true;
										break;
									}
								}
								//observable = true;
								observable = perteneceTareaPerfil? false: true;
							}
						}
					}
					
					
				}else{
					if (parteGuia(objAyuda, lstGuiaDocuMC)) {
						observable = true;
					}
					//observable = true;
				}
			}
		
		}catch(Exception e){
			LOG.error("Error en PanelDocumentosMB.esObservableGuiaDoc ", e);
			observable = false;
		}
		
		LOG.info("observable: " + observable);
		return observable;
	}

	private boolean parteGuia(AyudaPanelDocumentos objAyuda, List<GuiaDocumentaria> listaGuiaDocumMC) {
		
		boolean retorno = true;
		
		for (GuiaDocumentaria objDocumentaria: listaGuiaDocumMC) {
			LOG.info("objDocumentaria:" + objDocumentaria.getTipoDocumento().getCodigo() + 
				" - " + objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo());
			if (objDocumentaria.getTipoDocumento().getCodigo()
					.equals(objAyuda.getObjGuiaDocumentaria().getTipoDocumento().getCodigo())) {

				if (objAyuda.isDocEscaneado()) {
					retorno = false;
				}
				break;
			}
		}
		
		return retorno;
	}

	private List<DocumentoExpTc> ordenarListaDocExpTCPorFecha(List<DocumentoExpTc> lstDocExpTc) {

		Collections.sort(lstDocExpTc, new Comparator<DocumentoExpTc>() {
			public int compare(DocumentoExpTc de1, DocumentoExpTc de2) {
				Timestamp t1 = de1.getFecReg();
				Timestamp t2 = de2.getFecReg();
				Date date1 = t1 == null ? new Date() : new Date(t1.getTime());
				Date date2 = t2 == null ? new Date() : new Date(t2.getTime());
				return date2.compareTo(date1);
			}
		});
		return lstDocExpTc;
	}

	public List<GuiaDocumentaria> agregarListaNulos(
			List<GuiaDocumentaria> listaTemp, List<GuiaDocumentaria> listaResult) {
		List<GuiaDocumentaria> listaObli = new ArrayList<GuiaDocumentaria>();
		List<GuiaDocumentaria> listaNoObli = new ArrayList<GuiaDocumentaria>();
		List<GuiaDocumentaria> listaDoc = listaTemp;

		for (GuiaDocumentaria ldgTempAdd : listaDoc) {
			ldgTempAdd.setPersona(new Persona());
			ldgTempAdd.getPersona().setCodigo(
					Constantes.CODIGO_TIPO_PERSONA_TITULAR);
			listaResult.add(ldgTempAdd);
		}

		for (GuiaDocumentaria objListOrden : listaResult) {
			if (objListOrden.getObligatorio().equals(
					Constantes.CODIGO_DOCUMENTO_OBLIGATORIO))
				listaObli.add(objListOrden);
			else
				listaNoObli.add(objListOrden);
		}
		listaObli.addAll(listaNoObli);

		return listaObli;

	}

	public List<GuiaDocumentaria> ordenarListaPorPersona(
			List<GuiaDocumentaria> listaGuiaDoc) {
		
		Collections.sort(listaGuiaDoc, new Comparator<GuiaDocumentaria>(){
			public int compare(GuiaDocumentaria e1, GuiaDocumentaria e2) {
				int i = e1.getPersona().getCodigo().compareTo(e2.getPersona().getCodigo());
				int j = e1.getObligatorio().compareTo(e2.getObligatorio());
				int k = e1.getTipoDocumento().getDescripcion().compareTo(e2.getTipoDocumento().getDescripcion());
				int l = 0;
				int retStatus=0;
				
				if (e1.getFeRegistro()!=null && e2.getFeRegistro()!=null){
					if (e1.getFeRegistro().before(e2.getFeRegistro()))
						l=-1;
					else if(e1.getFeRegistro().after(e2.getFeRegistro()))
						l=1;
					else
						l=0;					
				}/*else if(e1.getFeRegistro()==null){
					l=1;
				}else if(e2.getFeRegistro()==null){
					l=-1;
				}*/
				
				
				if ( i < 0 ) {  
                    retStatus = -1;  
	            } else if ( i > 0 ) {  
	                    retStatus = 1;  
	            } else {  
	                    if ( j > 0 ) {  
	                            retStatus = -1;  
	                    } else if ( j < 0 ) {  
	                            retStatus = 1;  
	                    } else {  
	                            if ( k < 0 ) {  
	                                    retStatus = -1;  
	                            } else if( k > 0 ){  
	                                    retStatus = 1;  
	                            } else {  
		                            	if ( l < 0 ) {  
		                                    retStatus = -1;  
			                            } else if( l > 0 ){  
			                                    retStatus = 1;  
			                            } else {  
			                            	    retStatus = 0;  
			                            }    
	                            }  
	                    }  
	            }  
	            return retStatus;
			}
		});
	
	return listaGuiaDoc;

	}	
	
	public List<GuiaDocumentaria> ordenarListaPorFecha(
			List<GuiaDocumentaria> listaGuiaDoc) {

		Collections.sort(listaGuiaDoc, new Comparator<GuiaDocumentaria>() {
			public int compare(GuiaDocumentaria e1, GuiaDocumentaria e2) {
				Timestamp t1 = e1.getFeRegistro();
				Timestamp t2 = e2.getFeRegistro();
				Date date1 = t1 == null ? new Date() : new Date(t1.getTime());
				Date date2 = t2 == null ? new Date() : new Date(t2.getTime());
				return date2.compareTo(date1);
			}
		});
		return listaGuiaDoc;
	}
	
	public List<GuiaDocumentaria> ordenarListaPorObligatorio(
			List<GuiaDocumentaria> listaGuiaDoc) {

		Collections.sort(listaGuiaDoc, new Comparator<GuiaDocumentaria>() {
			public int compare(GuiaDocumentaria e1, GuiaDocumentaria e2) {
				return e2.getObligatorio().compareTo(e1.getObligatorio());
			}
		});
		return listaGuiaDoc;
	}
	
	public void observarDocAdjuntado(AjaxBehaviorEvent event) {
		LOG.info("observarDocAdjuntado(AjaxBehaviorEvent event)");
		if(htmlIdCmObs != null){
			strIdCmObs = String.valueOf(htmlIdCmObs.getValue());
		}
		if(htmlValIdCmObs != null){
			strValIdCmObs = String.valueOf(htmlValIdCmObs.getValue());
		}
		if (strIdCmObs != null) {
			LOG.info("strIdCmObs: " + strIdCmObs);
			LOG.info("strValIdCmObs: " + strValIdCmObs);
			LOG.info("listaAyudaPanelDocumentos.size:" + listaAyudaPanelDocumentos.size());
			expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
			LOG.info("Expediente:" + expediente.getId());
			DocumentoExpTc docExpTC;
			List<DocumentoExpTc> listaDocumentoExpTc = (List<DocumentoExpTc>) getObjectSession("listaDocumentoExpTc");
			if(listaDocumentoExpTc == null){
				listaDocumentoExpTc = new ArrayList<DocumentoExpTc>();
			}
			LOG.info("listaDocumentoExpTc inicio::::"+listaDocumentoExpTc.size());
			try {
				docExpTC = documentoExpTcBean.buscarPorId(Long.parseLong(strIdCmObs));
				
				boolean marcado = false;
				for (AyudaPanelDocumentos apd : listaAyudaPanelDocumentos) {
					if (apd.getIdCm() == Long.valueOf(strIdCmObs)) {
						//LOG.info(apd.getIdCm() + " - " + apd.isColObservar());
						//String flagObs = apd.isColObservar() ? "1" : "0";
						String flagObs = (strValIdCmObs == null || "".equals(strValIdCmObs)) ? "0" : ("true".equals(strValIdCmObs)) ? "1" : "0";
						apd.setColObservar("1".equals(flagObs) ? true : false);
						docExpTC.setFlagObs(flagObs);
						if("1".equals(flagObs)) marcado = true;
						break;
					}					
				}
				LOG.info("marcado::::"+marcado);
				if(!marcado){
					documentoExpTcBean.edit(docExpTC);
					int count = -1;
					for(DocumentoExpTc documentoExpTc : listaDocumentoExpTc){
						count++;
						if(documentoExpTc.getId() == docExpTC.getId()){
							break;
						}
					}
					if(count >= 0){
						listaDocumentoExpTc.remove(count);
					}
				}
				else{
					boolean existe = false;
					for(DocumentoExpTc documentoExpTc : listaDocumentoExpTc){
						if(documentoExpTc.getId() == docExpTC.getId()){
							documentoExpTc.setFlagObs(docExpTC.getFlagObs());
							existe = true;
							break;
						}
					}
					LOG.info("existe::::"+existe);
					if(!existe){
						listaDocumentoExpTc.add(docExpTC);
					}
					LOG.info("listaDocumentoExpTc fin::::"+listaDocumentoExpTc.size());
					addObjectSession("listaDocumentoExpTc", listaDocumentoExpTc);
				}
				
				LOG.info("docExpTC observado");
				strListaDocsObservados = obtenerDocsObservados(listaAyudaPanelDocumentos);
				strListaIdcmDocsObservados = obtenerIdcmDocsObservados(listaAyudaPanelDocumentos);
				LOG.info("strListaDocsObervados:" + strListaDocsObservados);
				LOG.info("strListaIdcmDocsObservados:" + strListaIdcmDocsObservados);
				
			} catch (Exception e) {
				LOG.info("El IDCM a Observar no es valido.");
			}			
		}
	}
	
		
	private String obtenerDocsObservados(
			List<AyudaPanelDocumentos> listaAyudaPD) {
		
		String observados = "";
		//List<String> lstObservados = new ArrayList<String>();
		for (AyudaPanelDocumentos apd : listaAyudaPD) {
			if (apd.getIdCm() != 0 && apd.isColObservar()) {
				LOG.info(apd.getIdCm() + " - " + apd.isColObservar());
				//lstObservados.add(apd.getCodigoTipoDoc()+ " - " + apd.getDescripcionTipoDoc());
				//observados = observados.concat(apd.getCodigoTipoDoc()+ " - " + apd.getDescripcionTipoDoc()).concat("\n");
				observados = observados + apd.getDescripcionTipoDoc() + ", ";
			}					
		}
		/*if(!"".equals(observados) && observados.length() > 2){
			observados = observados.substring(0, observados.length() - 2);
		}*/
		return observados;
	}
	
	private String obtenerIdcmDocsObservados(
			List<AyudaPanelDocumentos> listaAyudaPD) {
		
		String observados = "";
		//List<String> lstObservados = new ArrayList<String>();
		for (AyudaPanelDocumentos apd : listaAyudaPD) {
			if (apd.getIdCm() != 0 && apd.isColObservar()) {
				LOG.info(apd.getIdCm() + " - " + apd.isColObservar());
				//lstObservados.add(apd.getCodigoTipoDoc()+ " - " + apd.getDescripcionTipoDoc());
				//observados = observados.concat(apd.getCodigoTipoDoc()+ " - " + apd.getDescripcionTipoDoc()).concat("\n");
				observados = observados + apd.getIdCm() + ",";
			}					
		}
		/*if(!"".equals(observados) && observados.length() > 2){
			observados = observados.substring(0, observados.length() - 2);
		}*/
		return observados;
	}
	
	public void actualizarObservados(){
		LOG.info("en el metodo actualizarObservados");
		/*List<DocumentoExpTc> listaDocumentoExpTc = (List<DocumentoExpTc>) getObjectSession("listaDocumentoExpTc");
		if(listaDocumentoExpTc == null){
			listaDocumentoExpTc = new ArrayList<DocumentoExpTc>();
		}
		LOG.info("listaDocumentoExpTc:::::"+listaDocumentoExpTc.size());
		for(DocumentoExpTc documentoExpTc : listaDocumentoExpTc){
			LOG.info("documentoExpTc::::"+documentoExpTc.getFlagObs());
			documentoExpTcBean.edit(documentoExpTc);
		}
		removeObjectSession("listaDocumentoExpTc");
		removeObjectSession("actualizarObservados");*/
		
		LOG.info("strListaIdcmDocsObservados:::::"+strListaIdcmDocsObservados);
		LOG.info("listaAyudaPanelDocumentos::::"+listaAyudaPanelDocumentos.size());
		if(strListaIdcmDocsObservados != null && !"".equals(strListaIdcmDocsObservados)){
			String[] idcms = strListaIdcmDocsObservados.split(",");
			for (AyudaPanelDocumentos apd : listaAyudaPanelDocumentos) {
				LOG.info("apd.getIdCm():::::"+apd.getIdCm());
				for(String idcm : idcms){
					LOG.info("idcm:::::"+idcm);
					if(apd.getIdCm() == Long.parseLong(idcm)){
						LOG.info("a actualizar:::::"+apd.getIdCm());
						//apd.setColObservar(true);
						DocumentoExpTc docExpTC = documentoExpTcBean.buscarPorId(apd.getIdCm());
						LOG.info("antes de ser actualizado:::::"+docExpTC.getIdCm()+" - "+docExpTC.getFlagObs());
						docExpTC.setFlagObs("1");
						documentoExpTcBean.edit(docExpTC);
						break;
					}
				}					
			}
		}/*else if(listaAyudaPanelDocumentos != null){ 
			for (AyudaPanelDocumentos apd : listaAyudaPanelDocumentos) {
				LOG.info("apd.getIdCm():::::"+apd.getIdCm());
				LOG.info("apd.getIdDocExp():::::"+apd.getIdDocExp());
				
					if(apd.getIdCm() == 0){
						if(apd.getIdDocExp() > 0){
							LOG.info("a actualizar Flag escaneado para :::::"+apd.getIdCm());
							DocumentoExpTc docExpTC = documentoExpTcBean.buscarPorId(apd.getIdDocExp());
							LOG.info("antes de ser actualizado:::::"+docExpTC.getIdCm()+" - "+docExpTC.getFlagEscaneado());
							docExpTC.setFlagEscaneado("0");
							documentoExpTcBean.edit(docExpTC);
						}
						//break;
					}
								
			}
		}*/
		/*LOG.info("a actualizar Flag escaneado para :::::"+expediente.getId());
		List<DocumentoExpTc> listDocumentoExpTc = documentoExpTcBean.buscarPorExpediente(expediente.getId());
		if (!(listDocumentoExpTc == null || listDocumentoExpTc.isEmpty())){
	    	for (DocumentoExpTc docExpTc : listDocumentoExpTc) {
	    		if (docExpTc.getIdCm()==null) {
	    			LOG.info("antes de ser actualizado:::::"+docExpTc.getIdCm()+" - "+docExpTc.getFlagEscaneado());
	    			docExpTc.setFlagEscaneado("0");
					documentoExpTcBean.edit(docExpTc);
	    		}
	    		
	    	}
	    }*/
	}
	// Modificado EPY 31122015
	/*public void actualizarNoObservados(){
		LOG.info("en el metodo actualizarNoObservados");
		LOG.info("listaAyudaPanelDocumentos::::"+listaAyudaPanelDocumentos.size());
		if(listaAyudaPanelDocumentos!=null)
		for (AyudaPanelDocumentos apd : listaAyudaPanelDocumentos) {
			//apd.setColObservar(true);
			if(apd.getIdCm() > 0){
				DocumentoExpTc docExpTC = documentoExpTcBean.buscarPorId(apd.getIdCm());
				docExpTC.setFlagObs("0");
				documentoExpTcBean.edit(docExpTC);
			}
		}
	}*/
	
	public void actualizarNoObservados(){
		LOG.info("en el metodo actualizarNoObservados");
		LOG.info("listaAyudaPanelDocumentos::::"+listaAyudaPanelDocumentos.size());
		String iDsCM = "";
		if(listaAyudaPanelDocumentos!=null){
			for (AyudaPanelDocumentos apd : listaAyudaPanelDocumentos) {
				//apd.setColObservar(true);
				if(apd.getIdCm() > 0){
					if(iDsCM.length()==0){
								iDsCM = iDsCM+ apd.getIdCm();
					}else{
								iDsCM = iDsCM+","+apd.getIdCm();;
					}
				}/*else if(apd.getIdCm() == 0){
					LOG.info("a actualizar Flag escaneado para :::::"+apd.getIdCm());
					List<DocumentoExpTc> listDocumentoExpTc = documentoExpTcBean.buscarPorExpediente(expediente.getId());
					if (!(listDocumentoExpTc == null || listDocumentoExpTc.isEmpty())){
				    	for (DocumentoExpTc docExpTc : listDocumentoExpTc) {
				    		if (docExpTc.getIdCm()==null) {
				    			LOG.info("antes de ser actualizado:::::"+docExpTc.getIdCm()+" - "+docExpTc.getFlagEscaneado());
				    			docExpTc.setFlagEscaneado("0");
								documentoExpTcBean.edit(docExpTc);
				    		}
				    		
				    	}
				    }
					
				}*/
			}
			if(iDsCM.length()>0){
				documentoExpTcBean.actualizarDocumentosNoObservados(expediente.getId(), iDsCM);
			}
			
			/*LOG.info("a actualizar Flag escaneado para :::::"+expediente.getId());
			List<DocumentoExpTc> listDocumentoExpTc = documentoExpTcBean.buscarPorExpediente(expediente.getId());
			if (!(listDocumentoExpTc == null || listDocumentoExpTc.isEmpty())){
		    	for (DocumentoExpTc docExpTc : listDocumentoExpTc) {
		    		if (docExpTc.getIdCm()==null) {
		    			LOG.info("antes de ser actualizado:::::"+docExpTc.getIdCm()+" - "+docExpTc.getFlagEscaneado());
		    			docExpTc.setFlagEscaneado("0");
						documentoExpTcBean.edit(docExpTc);
		    		}
		    		
		    	}
		    }*/
		}
	}

	public String obtenerDocumentoUrl(DocumentoExpTc documentoExpTc){
		
		LOG.info("obtenerDocumentoUrl");
		return null;
	}

	public void setListaGuiaDoc(List<GuiaDocumentaria> listaGuiaDoc) {
		this.listaGuiaDoc = listaGuiaDoc;
	}

	public List<GuiaDocumentaria> getListaGuiaDoc() {
		return listaGuiaDoc;
	}

	public Expediente getExpedienteVO() {
		return expediente;
	}

	public void setExpedienteVO(Expediente expedienteVO) {
		this.expediente = expedienteVO;
	}

	public List<AyudaPanelDocumentos> getListaAyudaPanelDocumentos() {
		return listaAyudaPanelDocumentos;
	}

	public void setListaAyudaPanelDocumentos(
			List<AyudaPanelDocumentos> listaAyudaPanelDocumentos) {
		this.listaAyudaPanelDocumentos = listaAyudaPanelDocumentos;
	}

	public boolean isEstCheck() {
		return estCheck;
	}

	public void setEstCheck(boolean estCheck) {
		this.estCheck = estCheck;
	}

	public boolean isOpEliminar() {
		return opEliminar;
	}

	public void setOpEliminar(boolean opEliminar) {
		this.opEliminar = opEliminar;
	}

	public boolean isOpModificar() {
		return opModificar;
	}

	public void setOpModificar(boolean opModificar) {
		this.opModificar = opModificar;
	}

	public GuiaDocumentaria getObjSelectGuiaDocumentaria() {
		return objSelectGuiaDocumentaria;
	}

	public void setObjSelectGuiaDocumentaria(
			GuiaDocumentaria objSelectGuiaDocumentaria) {
		this.objSelectGuiaDocumentaria = objSelectGuiaDocumentaria;
	}

	public boolean isContlista() {
		return contlista;
	}

	public void setContlista(boolean contlista) {
		this.contlista = contlista;
	}

	public Documento getObjDoc() {
		return objDoc;
	}

	public void setObjDoc(Documento objDoc) {
		this.objDoc = objDoc;
	}

	public DocumentoExpTc getObjDocumentoExpTc() {
		return objDocumentoExpTc;
	}

	public void setObjDocumentoExpTc(DocumentoExpTc objDocumentoExpTc) {
		this.objDocumentoExpTc = objDocumentoExpTc;
	}

	public String getStrListaDocsTransferencias() {
		return strListaDocsTransferencias;
	}

	public void setStrListaDocsTransferencias(String strListaDocsTransferencias) {
		this.strListaDocsTransferencias = strListaDocsTransferencias;
	}

	public HtmlInputText getHtmlListaDocsTransferencias() {
		return htmlListaDocsTransferencias;
	}

	public void setHtmlListaDocsTransferencias(
			HtmlInputText htmlListaDocsTransferencias) {
		this.htmlListaDocsTransferencias = htmlListaDocsTransferencias;
	}

	public HtmlCommandButton getBtnActualizaPanelDocumentos() {
		return btnActualizaPanelDocumentos;
	}

	public void setBtnActualizaPanelDocumentos(
			HtmlCommandButton btnActualizaPanelDocumentos) {
		this.btnActualizaPanelDocumentos = btnActualizaPanelDocumentos;
	}

	public HtmlCommandButton getBtnActualizaAppletYPanelDocumentos() {
		return btnActualizaAppletYPanelDocumentos;
	}

	public void setBtnActualizaAppletYPanelDocumentos(
			HtmlCommandButton btnActualizaAppletYPanelDocumentos) {
		this.btnActualizaAppletYPanelDocumentos = btnActualizaAppletYPanelDocumentos;
	}

	public String getStrCodigoTipoDocEliminar() {
		return strCodigoTipoDocEliminar;
	}

	public void setStrCodigoTipoDocEliminar(String strCodigoTipoDocEliminar) {
		this.strCodigoTipoDocEliminar = strCodigoTipoDocEliminar;
	}

	public HtmlInputText getHtmlCodigoTipoDocEliminar() {
		return htmlCodigoTipoDocEliminar;
	}

	public void setHtmlCodigoTipoDocEliminar(HtmlInputText htmlCodigoTipoDocEliminar) {
		this.htmlCodigoTipoDocEliminar = htmlCodigoTipoDocEliminar;
	}

	public HtmlCommandButton getBtnEliminarDocumentoContent() {
		return btnEliminarDocumentoContent;
	}

	public void setBtnEliminarDocumentoContent(
			HtmlCommandButton btnEliminarDocumentoContent) {
		this.btnEliminarDocumentoContent = btnEliminarDocumentoContent;
	}

	public HtmlInputText getHtmlTramaDocumentoCambio() {
		return htmlTramaDocumentoCambio;
	}

	public void setHtmlTramaDocumentoCambio(HtmlInputText htmlTramaDocumentoCambio) {
		this.htmlTramaDocumentoCambio = htmlTramaDocumentoCambio;
	}

	public String getStrCodigoTipoDocsModificar() {
		return strCodigoTipoDocsModificar;
	}

	public void setStrCodigoTipoDocsModificar(String strCodigoTipoDocsModificar) {
		this.strCodigoTipoDocsModificar = strCodigoTipoDocsModificar;
	}

	public HtmlInputText getHtmlCodigoTipoDocsModificar() {
		return htmlCodigoTipoDocsModificar;
	}

	public void setHtmlCodigoTipoDocsModificar(
			HtmlInputText htmlCodigoTipoDocsModificar) {
		this.htmlCodigoTipoDocsModificar = htmlCodigoTipoDocsModificar;
	}

	public HtmlCommandButton getBtnModificarDocumentoContent() {
		return btnModificarDocumentoContent;
	}

	public void setBtnModificarDocumentoContent(
			HtmlCommandButton btnModificarDocumentoContent) {
		this.btnModificarDocumentoContent = btnModificarDocumentoContent;
	}

	public boolean isOpValidar() {
		return opValidar;
	}

	public void setOpValidar(boolean opValidar) {
		this.opValidar = opValidar;
	}

	public String getStrTramaDocumentoCambio() {
		return strTramaDocumentoCambio;
	}

	public void setStrTramaDocumentoCambio(String strTramaDocumentoCambio) {
		this.strTramaDocumentoCambio = strTramaDocumentoCambio;
	}

	public boolean isOpObservar() {
		return opObservar;
	}

	public void setOpObservar(boolean opObservar) {
		this.opObservar = opObservar;
	}
	
	public boolean isOpObligatorio() {
		return opObligatorio;
	}

	public void setOpObligatorio(boolean opObligatorio) {
		this.opObligatorio = opObligatorio;
	}

	public HtmlInputText getHtmlIdCmObs() {
		return htmlIdCmObs;
	}

	public void setHtmlIdCmObs(HtmlInputText htmlIdCmObs) {
		this.htmlIdCmObs = htmlIdCmObs;
	}
	
	public HtmlInputText getHtmlValIdCmObs() {
		return htmlValIdCmObs;
	}

	public void setHtmlValIdCmObs(HtmlInputText htmlValIdCmObs) {
		this.htmlValIdCmObs = htmlValIdCmObs;
	}

	public String getStrIdCmObs() {
		return strIdCmObs;
	}

	public void setStrIdCmObs(String strIdCmObs) {
		this.strIdCmObs = strIdCmObs;
	}
	
	public String getStrValIdCmObs() {
		return strValIdCmObs;
	}

	public void setStrValIdCmObs(String strValIdCmObs) {
		this.strValIdCmObs = strValIdCmObs;
	}

	public String getStrListaDocsObservados() {
		return strListaDocsObservados;
	}

	public void setStrListaDocsObservados(String strListaDocsObservados) {
		this.strListaDocsObservados = strListaDocsObservados;
	}
	
	public String getStrListaIdcmDocsObservados() {
		return strListaIdcmDocsObservados;
	}

	public void setStrListaIdcmDocsObservados(String strListaIdcmDocsObservados) {
		this.strListaIdcmDocsObservados = strListaIdcmDocsObservados;
	}

	public HtmlInputText getHtmlListaDocsObservados() {
		return htmlListaDocsObservados;
	}

	public void setHtmlListaDocsObservados(HtmlInputText htmlListaDocsObservados) {
		this.htmlListaDocsObservados = htmlListaDocsObservados;
	}
	
	public HtmlInputText getHtmlListaIdcmDocsObservados() {
		return htmlListaIdcmDocsObservados;
	}

	public void setHtmlListaIdcmDocsObservados(HtmlInputText htmlListaIdcmDocsObservados) {
		this.htmlListaIdcmDocsObservados = htmlListaIdcmDocsObservados;
	}

	public List<PersonaPD> getLstPersonaPDs() {
		return lstPersonaPDs;
	}

	public void setLstPersonaPDs(List<PersonaPD> lstPersonaPDs) {
		this.lstPersonaPDs = lstPersonaPDs;
	}

	public boolean isPantallaConsulta() {
		return pantallaConsulta;
	}

	public void setPantallaConsulta(boolean pantallaConsulta) {
		this.pantallaConsulta = pantallaConsulta;
	}

	public boolean isExistenNuevosArchivos() {
		return existenNuevosArchivos;
	}

	public void setExistenNuevosArchivos(boolean existenNuevosArchivos) {
		this.existenNuevosArchivos = existenNuevosArchivos;
	}

	public boolean isExpedienteEnProceso() {
		return expedienteEnProceso;
	}

	public void setExpedienteEnProceso(boolean expedienteEnProceso) {
		this.expedienteEnProceso = expedienteEnProceso;
	}

	
}