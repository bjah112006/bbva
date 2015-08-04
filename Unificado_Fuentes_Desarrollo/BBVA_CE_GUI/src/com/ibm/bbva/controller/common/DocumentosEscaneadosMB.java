package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.iice.service.ContentServiceImplProxy;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;
import com.ibm.bbva.tabla.dto.DatosDocumentosExpIiceDTO;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;
import com.ibm.bbva.util.AyudaPanelDocumentos;

@SuppressWarnings("serial")
@ManagedBean(name = "documentosEscaneados")
@RequestScoped
public class DocumentosEscaneadosMB extends AbstractMBean {
	
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	@EJB
	private TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	DocumentoExpTcBeanLocal documentoExpTcBean;
	
	private String pathLog;
	private String pathEscaneados;
	private String pathTransferencias;
	private String pathDescargados;
	private String tipoVisualizacion;
	private String tramaDocumentos = "";
	private String tramaDocumentosReutilizables = "";
	private String tramaDocumentosContent = "";
	private String validaGuiaEscaneada;
	private String cleanTransferDir;
	
	private String paginaEscaneoWeb;
	private String idEmpresa;
	private String idSistema;
	private String tamanoMaxPDF;
	private Empleado empleado;
	
	private String documentosFaltantes = "";
	private String tramaTipoDocumentos = "";
	
	private String tramaDocumentosCargados = "";
	
	private boolean tieneGuiaDocumentaria = true;
	
	private static final Logger LOG = LoggerFactory.getLogger(DocumentosEscaneadosMB.class);
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	
	public DocumentosEscaneadosMB() {
		LOG.info("DocumentosEscaneadosMB");
		this.tipoVisualizacion = obtenerTipoVisualizacion();
		
		// valida si tiene guia documentaria
		tieneGuiaDocumentaria = true;
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		long idTarea = 0;
		if(expediente != null && expediente.getId() > 0){
			idTarea = expediente.getExpedienteTC().getTarea().getId();
		}
		else{
			idTarea = Constantes.ID_TAREA_1.longValue();
		}
		addObjectSession("tareaSession", String.valueOf(idTarea));
		Integer tamListGuiaDocumentaria = (Integer) getObjectSession("tamListaGuiaDoc");
		if((tamListGuiaDocumentaria == null || tamListGuiaDocumentaria.intValue() == 0) && idTarea != 1){
			tieneGuiaDocumentaria = false;
		}
				
		String limpiarArchivos = (String) getObjectSession(Constantes.CLEANTRANSFERDIR);
		if (limpiarArchivos == null) {
			this.cleanTransferDir = Constantes.CLEANTRANSFERDIR_UNO;
			addObjectSession(Constantes.CLEANTRANSFERDIR, Constantes.CLEANTRANSFERDIR_CERO);
		} else {
			this.cleanTransferDir = limpiarArchivos;
		}	
	}
	@PostConstruct
    public void init() {	
		
		String nombJSP = getNombreJSPPrincipal();

		if (!nombJSP.equals("formVisualizarExpediente")){
			LOG.info("BUSCANDO PATH");
			//	this.pathLog = Constantes.DIRECTORIO_LOG_DOC_ESCANEADOS;
			this.pathLog = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_LOG_DOC_ESCANEADOS").getValorVariable();
			LOG.info("pathLog = "+pathLog+" = "+Constantes.DIRECTORIO_LOG_DOC_ESCANEADOS);
			
		//	this.pathEscaneados = Constantes.PATH_ESCANEO;
			this.pathEscaneados = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_PATH_ESCANEO").getValorVariable();
			LOG.info("pathEscaneados = "+pathEscaneados+" = "+Constantes.PATH_ESCANEO);
			
		//	this.pathTransferencias = Constantes.DIRECTORIO_DOC_ESCANEADOS;
			this.pathTransferencias = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_DOC_ESCANEADOS").getValorVariable();
			LOG.info("pathTransferencias = "+pathTransferencias+" = "+Constantes.DIRECTORIO_DOC_ESCANEADOS);
			
		//	this.pathDescargados = Constantes.DIRECTORIO_DOC_ESCANEADOS_BAJADA;
			this.pathDescargados =parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "CONSTANTE_FTP_DIRECTORIO_DOC_ESCANEADOS_BAJADA").getValorVariable();
			LOG.info("pathDescargados = "+pathDescargados+" = "+Constantes.DIRECTORIO_DOC_ESCANEADOS_BAJADA);

			this.paginaEscaneoWeb =parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.PAGINA_ESCANEO_WEB).getValorVariable();
			LOG.info("paginaEscaneoWeb = "+paginaEscaneoWeb);
			
			this.idEmpresa =parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.ID_EMPRESA).getValorVariable();
			LOG.info("idEmpresa = "+idEmpresa);
			
			this.idSistema =parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.ID_SISTEMA).getValorVariable();
			LOG.info("idSistema = "+idSistema);
			
			this.tamanoMaxPDF = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.TAMANO_MAXIMO_ARCHIVO_MB).getValorVariable();
			LOG.info("tamanoMaxPDF = "+tamanoMaxPDF);			
		}else{
			LOG.info("No es necesario buscar rutas... PATH");
		}
		
		this.empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
	}
	
	public void limpiarCarpetaTransferencias() {
		this.cleanTransferDir = Constantes.CLEANTRANSFERDIR_UNO;
		addObjectSession(Constantes.CLEANTRANSFERDIR, Constantes.CLEANTRANSFERDIR_CERO);
	}
	
	public String obtenerTipoVisualizacion() {
		String nombJSP = getNombreJSPPrincipal();
/*		if (nombJSP.equals("formAprobarExpediente") ||
			nombJSP.equals("formConsultarClienteModificaciones") ||
			nombJSP.equals("formCoordinarClienteSubsanar") ||
			nombJSP.equals("formEjecutarEvalCrediticia") ||
			nombJSP.equals("formEvaluarDevolucionRiesgos") ||
//			nombJSP.equals("formEvaluarFactibilidadOp") ||
//			nombJSP.equals("formGestionarSubsanarOperacion") ||
			nombJSP.equals("formRegistrarExpediente") ||
//			nombJSP.equals("formRegistrarExpedienteCu23") ||
//			nombJSP.equals("formRegistrarExpedienteCu25") ||
			nombJSP.equals("formRegularizarEscanearDocumentacion") ||
			nombJSP.equals("formRevisarRegistrarDictamen") ||
			nombJSP.equals("formSolicitarVerificacionDomiciliaria") ||
			nombJSP.equals("formVerificarResultadoDomiciliaria")) {
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO1;
		}else if (nombJSP.equals("formAnularModificarOpCu14") ||
				  nombJSP.equals("formAnularModificarOpCu18") ||
				  nombJSP.equals("formRegistrarDatos") ||
//				  nombJSP.equals("formRegistrarEstado") ||
				  nombJSP.equals("formVerificarConformidadExpediente")){
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO2;
		}else if (nombJSP.equals("formArchivarExpediente") ||
				  nombJSP.equals("formCambiarSituacionExp") ||
				  nombJSP.equals("formCambiarSituacionTramite") ||
				  nombJSP.equals("formRealizarAltaTarjeta") ||
				  nombJSP.equals("formRegistrarAprobResolucion") ||
//				  nombJSP.equals("formVerificarConformidadCierreExp") ||
				  nombJSP.equals("formVerificarExpDesestimados") ||
				  nombJSP.equals("formVisualizarExpediente")){
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO3;
		}*/
		
		if (nombJSP.equals("formVisualizarExpediente")){ 
				return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO3;
		}else if (nombJSP.equals("formVerificarConformidadExpediente")){
				return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO2;
		}else{
				return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO1;
		}
	}
	
	public void actualizarApplet(List<AyudaPanelDocumentos> listaAyudaPanelDocumentos) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		
		for (AyudaPanelDocumentos obj : listaAyudaPanelDocumentos) {
			if (obj.getObjGuiaDocumentaria() != null) {
				TipoDocumento tipoDocumento = obj.getObjGuiaDocumentaria().getTipoDocumento();
				LOG.info("tipoDocumento.getCodigo(): " + tipoDocumento.getCodigo());
				if (sb.indexOf(tipoDocumento.getCodigo())<0) {
					sb.append(tipoDocumento.getCodigo());
					sb.append(",");
					sb.append(tipoDocumento.getDescripcion().replaceAll(",", " ").replaceAll("(\\r|\\n)", ""));
					sb.append(",");
					sb.append(obj.getObjGuiaDocumentaria().getObligatorio());
					sb.append(",");
					boolean flagExiste = false;
					if (obj.isDocEscaneado()) {
						for (AyudaPanelDocumentos objAux : listaAyudaPanelDocumentos) {
							if (objAux.getObjGuiaDocumentaria() != null) {
								TipoDocumento tipoDocumentoAux = objAux.getObjGuiaDocumentaria().getTipoDocumento();
								LOG.info("tipoDocumentoAux.getCodigo(): " + tipoDocumentoAux.getCodigo());
								LOG.info("objAux.isDocEscaneado(): " + objAux.isDocEscaneado());
								if(tipoDocumento.getCodigo().equals(tipoDocumentoAux.getCodigo()) && !objAux.isDocEscaneado()){
									flagExiste = true;
									break;
								}
							}
						}
						if(!flagExiste){
							sb2.append(tipoDocumento.getCodigo());
							sb2.append(",");
						}
					}					
				}else{
					sb.replace(sb.length()-2, sb.length()-1, "0");
				}
			}
		}
		
		// Modificacion Tipo de Documento
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		LOG.info("Empleado.getId():" + empleado.getId());
		
		if(empleado.getPerfil() != null){
			LOG.info("Empleado.getPerfil().getId():" + empleado.getPerfil().getId());
			List<TipoDocumento> lstTipoDocumentos = null;
			if (empleado.getPerfil().getId() == Constantes.ID_PERFIL_MESA_DE_CONTROL) {
				lstTipoDocumentos = tipoDocumentoBean.buscar();
			} else {
				lstTipoDocumentos = new ArrayList<TipoDocumento>();
				;
			}
		
			LOG.info("lstTipoDocumentos.size:" + lstTipoDocumentos.size());
			for (TipoDocumento tipoDocumento : lstTipoDocumentos) {
				LOG.info("tipoDocumento.getCodigo(): " + tipoDocumento.getCodigo());
				sb3.append(tipoDocumento.getCodigo());
				sb3.append(",");
				sb3.append(tipoDocumento.getDescripcion().replaceAll(",", " ").replaceAll("(\\r|\\n)", ""));
				sb3.append(",");
			}
		}

		if (sb.length() > 0) this.tramaDocumentos = sb.substring(0, sb.length() - 1).toString();
		if (sb2.length() > 0) this.tramaDocumentosContent = sb2.substring(0, sb2.length() - 1).toString();
		if (sb3.length() > 0) this.tramaTipoDocumentos = sb3.substring(0, sb3.length() - 1).toString();

		LOG.info("this.tramaDocumentos : " + this.tramaDocumentos);
		LOG.info("this.tramaDocumentosContent : " + this.tramaDocumentosContent);
		LOG.info("this.tramaTipoDocumentos: " + this.tramaTipoDocumentos);
	}
	
	public void documentosFaltantes(List<AyudaPanelDocumentos> listaAyudaPanelDocumentos) {
		StringBuffer sb = new StringBuffer();	
		for (AyudaPanelDocumentos obj : listaAyudaPanelDocumentos) {
			if (obj.getCodigoTipoDoc()==null && !obj.getDescripcionTipoDoc().equals(Constantes.DESCRIP_DOC_OBLIGATORIO)) {		
				break;
			}
			if (obj.getCodigoTipoDoc()!=null && obj.getIdCm() == 0 && !obj.isDocTransferencias()) {				
				//sb.append(obj.getCodigoTipoDoc());
				sb.append(obj.getDescripcionTipoDoc());
				//sb.append(",");
				sb.append(", ");
			}
		}
		//if (sb.length() > 0) this.documentosFaltantes = sb.substring(0, sb.length() - 1).toString();	
		if (sb.length() > 0) this.documentosFaltantes = sb.substring(0, sb.length() - 2).toString();	
		LOG.info("documentosFaltantes : " + this.documentosFaltantes);
	}
	
	public String getPathLog() {
		return pathLog;
	}

	public void setPathLog(String pathLog) {
		this.pathLog = pathLog;
	}

	public String getPathEscaneados() {
		return pathEscaneados;
	}

	public void setPathEscaneados(String pathEscaneados) {
		this.pathEscaneados = pathEscaneados;
	}

	public String getPathTransferencias() {
		return pathTransferencias;
	}

	public void setPathTransferencias(String pathTransferencias) {
		this.pathTransferencias = pathTransferencias;
	}

	public String getPathDescargados() {
		return pathDescargados;
	}

	public void setPathDescargados(String pathDescargados) {
		this.pathDescargados = pathDescargados;
	}

	public String getTipoVisualizacion() {
		return tipoVisualizacion;
	}

	public void setTipoVisualizacion(String tipoVisualizacion) {
		this.tipoVisualizacion = tipoVisualizacion;
	}

	public String getTramaDocumentos() {
		return tramaDocumentos;
	}

	public void setTramaDocumentos(String tramaDocumentos) {
		this.tramaDocumentos = tramaDocumentos;
	}

	public String getTramaDocumentosReutilizables() {
		return tramaDocumentosReutilizables;
	}

	public void setTramaDocumentosReutilizables(String tramaDocumentosReutilizables) {
		this.tramaDocumentosReutilizables = tramaDocumentosReutilizables;
	}

	public String getTramaDocumentosContent() {
		return tramaDocumentosContent;
	}

	public void setTramaDocumentosContent(String tramaDocumentosContent) {
		this.tramaDocumentosContent = tramaDocumentosContent;
	}

	public String getValidaGuiaEscaneada() {
		return validaGuiaEscaneada;
	}

	public void setValidaGuiaEscaneada(String validaGuiaEscaneada) {
		this.validaGuiaEscaneada = validaGuiaEscaneada;
	}

	public String getCleanTransferDir() {
		return cleanTransferDir;
	}

	public void setCleanTransferDir(String cleanTransferDir) {
		this.cleanTransferDir = cleanTransferDir;
	}
	
	public String getPaginaEscaneoWeb() {
		return paginaEscaneoWeb;
	}

	public void setPaginaEscaneoWeb(String paginaEscaneoWeb) {
		this.paginaEscaneoWeb = paginaEscaneoWeb;
	}
	public String getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getIdSistema() {
		return idSistema;
	}
	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}
	public String getTamanoMaxPDF() {
		return tamanoMaxPDF;
	}
	public void setTamanoMaxPDF(String tamanoMaxPDF) {
		this.tamanoMaxPDF = tamanoMaxPDF;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public String getDocumentosFaltantes() {
		return documentosFaltantes;
	}
	public void setDocumentosFaltantes(String documentosFaltantes) {
		this.documentosFaltantes = documentosFaltantes;
	}
	public String getTramaTipoDocumentos() {
		return tramaTipoDocumentos;
	}
	public void setTramaTipoDocumentos(String tramaTipoDocumentos) {
		this.tramaTipoDocumentos = tramaTipoDocumentos;
	}
	public boolean getTieneGuiaDocumentaria() {
		return tieneGuiaDocumentaria;
	}
	public void setTieneGuiaDocumentaria(boolean tieneGuiaDocumentaria) {
		this.tieneGuiaDocumentaria = tieneGuiaDocumentaria;
	}
	public String getTramaDocumentosCargados() {
		String trama = "";
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		List<DocumentoExpTc> listaDocExp = new ArrayList<DocumentoExpTc>();	
		List<DatosDocumentosExpIiceDTO> listaDocExpDTO = new ArrayList<DatosDocumentosExpIiceDTO> ();
		
		
		/**FIX ERIKA ABREGU 05/07/2015
		 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
		 */
		if(!Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
			LOG.info("Metodo getTramaDocumentosCargados de DocumentosEscaneados nuevo = "+expediente.getId());
			listaDocExp = documentoExpTcBean.buscarPorExpediente(expediente.getId());
			
			if(listaDocExp != null && listaDocExp.size() > 0 /*&& expediente.getAccion().equals(Constantes.ACCION_BOTON_DEVOLVER)*/){
				
				// sublista para determinar los documentos en content que no estan observados
				//List<DocumentoExpTc> listaDocExpTmp = new ArrayList<DocumentoExpTc>();
				Set<String> setDocExp = new HashSet<String>();
				for(DocumentoExpTc documentoExpTc : listaDocExp){
					if(documentoExpTc.getIdCm() != null){
						setDocExp.add(documentoExpTc.getTipoDocumento().getCodigo());
					}
				}
				
				for (String codigo : setDocExp) {
					/*boolean observado = false;
					for(DocumentoExpTc documentoExpTc : listaDocExp){
						if(codigo.equals(documentoExpTc.getTipoDocumento().getCodigo())){
							String flagObs = documentoExpTc.getFlagObs() == null ? "0" : documentoExpTc.getFlagObs();
							if("1".equals(flagObs)){
								observado = true;
								break;
							}
						}
					}
					if(!observado){
						trama += codigo + ",";
					}*/
					trama += codigo + ",";
				}
			}
			
		}else{
			//preparando parametros
			//ArrayList<Object> listaParametros = new ArrayList<Object>();
			//listaParametros.add(new Long(expediente.getId()));
			
			//if (this.tablaFacadeBean == null) {
			//	this.tablaFacadeBean = new TablaFacadeBean();
			//}
			
			//listaDocExpDTO = tablaFacadeBean.getDocumentosPorExpIICE(listaParametros);
			//if (listaDocExpDTO == null) {
			//	listaDocExpDTO = new ArrayList<DatosDocumentosExpIiceDTO> ();
			//}
			//listaDocExp =  ConvertHistorial.convertToDocumentoExp(listaDocExpDTO);
		}
		//FIN DE FIX2 ERIKA ABREGU
		
		
		
		
		if(!"".equals(trama) && trama.length() > 1){
			trama = trama.substring(0, trama.length() - 1);
		}
		tramaDocumentosCargados = trama;
		return tramaDocumentosCargados;
	}
	public void setTramaDocumentosCargados(String tramaDocumentosCargados) {
		this.tramaDocumentosCargados = tramaDocumentosCargados;
	}
	

	/*@EJB 
	TipoDoiBeanLocal tipoDoiBean;
	@EJB
	TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	GuiaDocumentariaBeanLocal guiaDocumentariaBean;
	@EJB
	DocumentoExpTcBeanLocal documentoExpTcBean;
	
	private List<CategoriaRenta> listaClienteCategoriaRenta = new ArrayList<CategoriaRenta>();
	private Expediente expediente;
	private List<SelectItem> tiposDOI;
	private String tipoDOISeleccionado;
	private String numeroDOI;
	private String comandoSesion;
	private String parametroSesion;
	private List<GuiaDocumentaria> listaGuiaDoc = new ArrayList<GuiaDocumentaria>();	
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	private List<SelectItem> listaTipDoc = new ArrayList<SelectItem>();
	private String idTipoDocumento;
	private String rutaParam;
	private String rutaParamBajada;
	private String docEscApplet;
	private List<String> listaArchivos;
	private String strTiposDocs;
	private String strGuiaDoc;
	private String strTipoVisualizacion;
	private String strIdExpediente;
	private String strHostWs;
	private String validaGuiaEscaneada;
	private String documentosFaltantes;
	private String documentosReutilizables;
	private String cleanTransferDir;
	private String pathLog;
	private String existenTransferencias;
	private String pipe;
	private String pathapiescaneo;
	private String pathmdb;
	private String pathescaneo;
	private GuiaDocumentariaMB guiaDoc;
	
	public DocumentosEscaneadosMB() {

	} 
	
	@PostConstruct
    public void init() {
		this.setRutaParam(Constantes.DIRECTORIO_DOC_ESCANEADOS);
		this.setRutaParamBajada(Constantes.DIRECTORIO_DOC_ESCANEADOS_BAJADA);

		tiposDOI = Util.crearItems(tipoDoiBean.buscarTodos(), true, "id",	"descripcion");
		comandoSesion = (String) getObjectSession(Constantes.COMANDO_SESION);
		parametroSesion = (String) getObjectSession(Constantes.PARAMETROS_SESION);			

		/* Obtiene Datos de Expediente *
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);		
		/*Parametros Applet*
		this.strGuiaDoc="";
		this.strTiposDocs="";	
		this.strIdExpediente = expediente.getId() == 0 ? "" : String.valueOf(expediente.getId());
		
		//this.strHostWs = ParametersDelegate.getInstance().getValue(Constantes.CONSTANTE_HOSTWS);		
		this.strHostWs = Constantes.CONSTANTE_HOSTWS; 
		
		cleanTransferDir = Constantes.CLEANTRANFERDIR;
		this.pathLog = Constantes.DIRECTORIO_LOG_DOC_ESCANEADOS;
		this.pipe = this.parametroSesion;
		this.pathapiescaneo = this.comandoSesion;	
		this.pathmdb =	Constantes.RUTA_MDB;
		this.pathescaneo = Constantes.PATH_ESCANEO;
		tipoViualizacion();
		actualizarLista();	
		
		if (expediente.getId()!=0) {
			String nombJSP = getNombreJSPPrincipal();
			
			if (!nombJSP.equals("formConsultarClienteModificaciones") &&
				!nombJSP.equals("formCoordinarClienteSubsanar") &&
				!nombJSP.equals("formEvaluarDevolucionRiesgos") &&
				!nombJSP.equals("formGestionarSubsanarOperacion") &&
				!nombJSP.equals("formRegistrarExpediente") &&
				!nombJSP.equals("formRegistrarExpedienteCu23") &&
				!nombJSP.equals("formRegistrarExpedienteCu25") &&
				!nombJSP.equals("formRegularizarEscanearDocumentacion")) {
				
				cleanTransferDir = Constantes.CLEANTRANFERDIR_CERO;
			    llenarGuiaDocApplet();		   
			}else{
				cargaGuiaDocApplet();
				documentosReutilizables = (String) getObjectSession(Constantes.DOCUMENTO_REUTILIZABLE_APPLET);
				this.setDocumentosReutilizables(documentosReutilizables);
			}
		}
	}

	public void actualizarLista() {
		listaTipoDocumento = obtenerDatos();
		listaTipDoc = Util.crearItems(listaTipoDocumento, true, "id", "descripcion");
	}

	public List<TipoDocumento> obtenerDatos() {
		List<TipoDocumento> listaTipDocumento = new ArrayList<TipoDocumento>();
		
		/* Obtiene Datos de Expediente *
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if(expediente.getId()!=0){
			
			listaClienteCategoriaRenta = expediente.getClienteNatural().getCategoriasRenta();
	
			/* filtro Guia Documentaria *
			GuiaDocumentaria guiaDocumentaria = new GuiaDocumentaria();
			guiaDocumentaria.setProducto(new Producto());
			guiaDocumentaria.setSubproducto(new Subproducto());
			guiaDocumentaria.setTipoOferta(new TipoOferta());
			
			guiaDocumentaria.setProducto(expediente.getProducto());
			guiaDocumentaria.setSubproducto(expediente.getExpedienteTC().getSubproducto());
			guiaDocumentaria.setTipoOferta(expediente.getExpedienteTC().getTipoOferta());
			guiaDocumentaria.setFlagCliente(expediente.getClienteNatural().getCodCliente() == Constantes.CODIGO_CLIENTE_NUEVO ? Constantes.FLAG_CLIENTE_NUEVO_0 : Constantes.FLAG_CLIENTE_NUEVO_1);
			guiaDocumentaria.setFlagPagoHab(expediente.getClienteNatural().getPagoHab());
			guiaDocumentaria.setFlagPep(expediente.getClienteNatural().getPerExpPub());
			guiaDocumentaria.setFlagTasaEsp(expediente.getExpedienteTC().getFlagSolTasaEsp());
			guiaDocumentaria.setFlagVerifDom(expediente.getExpedienteTC().getVerifDom());
			guiaDocumentaria.setFlagVerifLab(expediente.getExpedienteTC().getVerifLab());
	
			listaGuiaDoc = (List<GuiaDocumentaria>) guiaDocumentariaBean.buscarGuiaDoc(guiaDocumentaria);
		
			for (GuiaDocumentaria guiaDoc : listaGuiaDoc) {
				TipoDocumento tipoDoc = tipoDocumentoBean.buscarPorId(guiaDoc.getTipoDocumento().getId());
				listaTipDocumento.add(tipoDoc);
			}
		}
		return listaTipDocumento;
	}

	public String llenarGuiaDocApplet() {
		cargaGuiaDocApplet();
		
		String nombJSP = getNombreJSPPrincipal();
		if (nombJSP.equals("formConsultarClienteModificaciones") ||
			nombJSP.equals("formCoordinarClienteSubsanar") ||
			nombJSP.equals("formEvaluarDevolucionRiesgos") ||
			nombJSP.equals("formRegistrarExpediente") ||
			nombJSP.equals("formRegularizarEscanearDocumentacion")) {
		    cargaDocReutilizable();	
		}
		return null;
	}
	
	public void cargaGuiaDocApplet() {		
		FacesContext ctx = FacesContext.getCurrentInstance();  
		GuiaDocumentariaMB guiaDocumentaria = null;
     	
		guiaDocumentaria = (GuiaDocumentariaMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "guiaDocumentaria");
				
		guiaDocumentaria.init();
		guiaDocumentaria.cargaApplet();
		
		this.setStrGuiaDoc(guiaDocumentaria.getStrGuiaDoc());
		this.setStrTiposDocs(guiaDocumentaria.getStrTiposDocs());
		
		this.cleanTransferDir = Constantes.CLEANTRANFERDIR_CERO;
	}
	
	public void cargaDocReutilizable() {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		DocumentoReutilizableMB documentoReutilizable = null;
				
		documentoReutilizable = (DocumentoReutilizableMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "documentoReutilizable");
		
        if(documentoReutilizable!=null) {
			if (documentoReutilizable.getListaDocumentoReutilizable().size()>0) {
	           addObjectSession(Constantes.LISTA_DOC_REUTILIZABLES, documentoReutilizable.getListaDocumentoReutilizable());	
			}
			
			AyudaDocumentoReutilizable ayudaDocumentoReutilizable = new AyudaDocumentoReutilizable();		        
		    this.setDocumentosReutilizables(ayudaDocumentoReutilizable.cargaDocumentoReutilizable(guiaDoc, documentoReutilizable));			
        }
	}
	
	public void tipoViualizacion() {
		String nombJSP = getNombreJSPPrincipal();
		if (nombJSP.equals("formAprobarExpediente") ||			
			nombJSP.equals("formConsultarClienteModificaciones") ||
			nombJSP.equals("formCoordinarClienteSubsanar") ||
			nombJSP.equals("formEjecutarEvalCrediticia") ||
			nombJSP.equals("formEvaluarDevolucionRiesgos") ||
			nombJSP.equals("formEvaluarFactibilidadOp") ||
			nombJSP.equals("formGestionarSubsanarOperacion") ||					
			nombJSP.equals("formRegistrarExpediente") ||
			nombJSP.equals("formRegistrarExpedienteCu23") ||
			nombJSP.equals("formRegistrarExpedienteCu25") ||
			nombJSP.equals("formRegularizarEscanearDocumentacion") ||
			nombJSP.equals("formRevisarRegistrarDictamen") ||
			nombJSP.equals("formSolicitarVerificacionDomiciliaria") ||			
			nombJSP.equals("formVerificarResultadoDomiciliaria")) {			
			this.strTipoVisualizacion=Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO1;
		}else if (nombJSP.equals("formAnularModificarOpCu14") ||
				  nombJSP.equals("formAnularModificarOpCu18") ||
				  nombJSP.equals("formRegistrarDatos") ||
				  nombJSP.equals("formRegistrarEstado") ||
				  nombJSP.equals("formVerificarConformidadExpediente")){				
			this.strTipoVisualizacion=Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO2;
		}else if (nombJSP.equals("formArchivarExpediente") ||
				  nombJSP.equals("formCambiarSituacionExp") ||
				  nombJSP.equals("formCambiarSituacionTramite") ||
				  nombJSP.equals("formRealizarAltaTarjeta") ||
				  nombJSP.equals("formRegistrarAprobResolucion") ||	
				  nombJSP.equals("formVerificarConformidadCierreExp") ||				  
				  nombJSP.equals("formVerificarExpDesestimados") ||
				  nombJSP.equals("formVisualizarExpediente")){				
			this.strTipoVisualizacion=Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO3;
		}
	}
	
	public String getComandoSesion() {
		return comandoSesion;
	}

	public void setComandoSesion(String comandoSesion) {
		this.comandoSesion = comandoSesion;
	}

	public String getParametroSesion() {
		return parametroSesion;
	}

	public void setParametroSesion(String parametroSesion) {
		this.parametroSesion = parametroSesion;
	}

	public String getTipoDOISeleccionado() {
		return tipoDOISeleccionado;
	}

	public void setTipoDOISeleccionado(String tipoDOISeleccionado) {
		this.tipoDOISeleccionado = tipoDOISeleccionado;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public List<SelectItem> getTiposDOI() {
		return tiposDOI;
	}

	public void setTiposDOI(List<SelectItem> tiposDOI) {
		this.tiposDOI = tiposDOI;
	}

	public void setListaGuiaDoc(List<GuiaDocumentaria> listaGuiaDoc) {
		this.listaGuiaDoc = listaGuiaDoc;
	}

	public List<GuiaDocumentaria> getListaGuiaDoc() {
		return listaGuiaDoc;
	}

	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public String getRutaParam() {
		return rutaParam;
	}

	public void setRutaParam(String rutaParam) {
		this.rutaParam = rutaParam;
	}
	
	public String getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(String idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public List<CategoriaRenta> getListaClienteCategoriaRenta() {
		return listaClienteCategoriaRenta;
	}

	public void setListaClienteCategoriaRenta(
			List<CategoriaRenta> listaClienteCategoriaRenta) {
		this.listaClienteCategoriaRenta = listaClienteCategoriaRenta;
	}

	public Expediente getExpedienteVO() {
		return expediente;
	}

	public void setExpedienteVO(Expediente expedienteVO) {
		this.expediente = expedienteVO;
	}

	public List<SelectItem> getListaTipDoc() {
		return listaTipDoc;
	}

	public void setListaTipDoc(List<SelectItem> listaTipDoc) {
		this.listaTipDoc = listaTipDoc;
	}

	public String getDocEscApplet() {
		return docEscApplet;
	}

	public void setDocEscApplet(String docEscApplet) {
		this.docEscApplet = docEscApplet;
	}

	public List<String> getListaArchivos() {
		return listaArchivos;
	}

	public void setListaArchivos(List<String> listaArchivos) {
		this.listaArchivos = listaArchivos;
	}

	public String getStrTiposDocs() {
		return strTiposDocs;
	}

	public void setStrTiposDocs(String strTiposDocs) {
		this.strTiposDocs = strTiposDocs;
	}

	public String getStrGuiaDoc() {
		return strGuiaDoc;
	}

	public void setStrGuiaDoc(String strGuiaDoc) {
		this.strGuiaDoc = strGuiaDoc;
	}

	public String getStrTipoVisualizacion() {
		return strTipoVisualizacion;
	}

	public void setStrTipoVisualizacion(String strTipoVisualizacion) {
		this.strTipoVisualizacion = strTipoVisualizacion;
	}

	public String getStrIdExpediente() {
		return strIdExpediente;
	}

	public void setStrIdExpediente(String strIdExpediente) {
		this.strIdExpediente = strIdExpediente;
	}

	public String getStrHostWs() {
		return strHostWs;
	}

	public void setStrHostWs(String strHostWs) {
		this.strHostWs = strHostWs;
	}

	public String getRutaParamBajada() {
		return rutaParamBajada;
	}

	public void setRutaParamBajada(String rutaParamBajada) {
		this.rutaParamBajada = rutaParamBajada;
	}

	public String getValidaGuiaEscaneada() {
		return validaGuiaEscaneada;
	}

	public void setValidaGuiaEscaneada(String validaGuiaEscaneada) {
		this.validaGuiaEscaneada = validaGuiaEscaneada;
	}

	public String getDocumentosFaltantes() {
		return documentosFaltantes;
	}

	public void setDocumentosFaltantes(String documentosFaltantes) {
		this.documentosFaltantes = documentosFaltantes;
	}

	public String getCleanTransferDir() {
		return cleanTransferDir;
	}

	public void setCleanTransferDir(String cleanTransferDir) {
		this.cleanTransferDir = cleanTransferDir;
	}

	public String getPathLog() {
		return pathLog;
	}

	public void setPathLog(String pathLog) {
		this.pathLog = pathLog;
	}

	public String getExistenTransferencias() {
		return existenTransferencias;
	}

	public void setExistenTransferencias(String existenTransferencias) {		
		addObjectSession(Constantes.EXISTEN_TRANSFERENCIAS, existenTransferencias);
		this.existenTransferencias = existenTransferencias;
	}
	public String getPathescaneo() {
		return pathescaneo;
	}

	public void setPathescaneo(String pathescaneo) {
		this.pathescaneo = pathescaneo;
	}

	public String getPathmdb() {
		return pathmdb;
	}

	public void setPathmdb(String pathmdb) {
		this.pathmdb = pathmdb;
	}

	public String getPipe() {
		return pipe;
	}

	public void setPipe(String pipe) {
		this.pipe = pipe;
	}

	public String getPathapiescaneo() {
		return pathapiescaneo;
	}

	public void setPathapiescaneo(String pathapiescaneo) {
		this.pathapiescaneo = pathapiescaneo;
	}

	public String getDocumentosReutilizables() {
		return documentosReutilizables;
	}

	public void setDocumentosReutilizables(String documentosReutilizables) {
		this.documentosReutilizables = documentosReutilizables;
	}

	public GuiaDocumentariaMB getGuiaDoc() {
		return guiaDoc;
	}

	public void setGuiaDoc(GuiaDocumentariaMB guiaDoc) {
		this.guiaDoc = guiaDoc;
	}*/
}