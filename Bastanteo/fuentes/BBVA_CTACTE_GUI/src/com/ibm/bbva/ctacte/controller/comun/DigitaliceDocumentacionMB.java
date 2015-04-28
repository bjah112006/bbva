package com.ibm.bbva.ctacte.controller.comun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.CasoNegocio;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EscaneoWeb;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.GuiaDocument;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.Producto;
import com.ibm.bbva.ctacte.bean.servicio.sfp.TipoPJSFP;
import com.ibm.bbva.ctacte.business.DocumentoExpBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.form.AprobarRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.form.PreRegistroMB;
import com.ibm.bbva.ctacte.controller.form.RegistrarNuevoBastanteoMB;
import com.ibm.bbva.ctacte.controller.form.RevisarAprobarBastanteoMB;
import com.ibm.bbva.ctacte.controller.form.RevisarEjecutarRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.form.SubsanarDocumentoMB;
import com.ibm.bbva.ctacte.controller.form.SubsanarFirmasMB;
import com.ibm.bbva.ctacte.controller.form.VerificarRealizarBastanteoMB;
import com.ibm.bbva.ctacte.dao.CasoNegocioDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.EscaneoWebDAO;
import com.ibm.bbva.ctacte.dao.GuiaDocumentDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.dao.servicio.SistemaFirmasPoderesDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper1;

@ManagedBean (name="digitaliceDocumentacion")
@ViewScoped
public class DigitaliceDocumentacionMB extends AbstractMBean {

	private static final long serialVersionUID = 8512389245523752041L;
	private static final Logger LOG = LoggerFactory.getLogger(DigitaliceDocumentacionMB.class);

	@ManagedProperty (value="#{registrarNuevoBastanteo}")
	private RegistrarNuevoBastanteoMB registrarNuevoBastanteo;
	@ManagedProperty (value="#{preRegistro}")
	private PreRegistroMB preRegistro;
	@ManagedProperty (value="#{subsanarDocumento}")
	private SubsanarDocumentoMB subsanarDocumento;
	@ManagedProperty(value="#{subsanarFirmas}")
	private SubsanarFirmasMB subsanarFirmas;
	@ManagedProperty(value="#{aprobarRevocatoria}")
	private AprobarRevocatoriaMB aprobarRevocatoria;
	@ManagedProperty(value="#{revisarEjecutarRevocatoria}")
	private RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria;
	@ManagedProperty(value="#{revisarAprobarBastanteo}")
	private RevisarAprobarBastanteoMB revisarAprobarBastanteo;
	@ManagedProperty(value="#{verificarRealizarBastanteo}")
	private VerificarRealizarBastanteoMB verificarRealizarBastanteo;
	private IDigitaliceDocumentacion managedBean;
	private Expediente expediente;
	private boolean mostrar;
	private boolean mostrarTab;
	private List<TipoPJSFP> listaTipoPJSFP;
	private List<SelectItem> listaTipoPJ;
	private List<CasoNegocio> listaCasoNegocioBD;
	private List<SelectItem> listaCasoNegocio;
	private List<Participe> listaFirmaObservada;
	private String selTipoPJ;
	private String selCasoNegocio;
	private List<DocumentoExpWrapper1> listaDocumentoExp;
	private List<DocumentoExp> listaDocExp;
	private Cuenta cuenta;
	private Operacion operacion;
	private String casoNegocioTextoAyuda;
	private boolean mostRegMod;
	private boolean mostCasoNegocio;
	private boolean mostEscaneado;
	private boolean mostObservaciones;
	private boolean mostObsGen;
	private boolean mostImagen;
	private boolean mostFirmas;
	private boolean mostApplet;	
	private boolean mostNroPagina;
	private boolean iniciarApplet;
	private boolean soloLectTipoPJ;
	private boolean soloLectCasoNegocio;
	private boolean hayEscaneo;
	private String ejecutoCobroComision;
	private String listaDocumentos;
	private String tramaDocumentos;
	private String tramaDescarga;
	private Integer idExpediente;
	private List<DocumentoExp> documentoExp;
	private String identificadorpID;
	private int idTarea;
	private String codigoCentral;	
	private String tramaParametrosApplet;
	private boolean disLinkVistaUnica;
	private EscaneoWeb escaneoWeb;
	private Empleado empleado;
	private String cleanTransferDir;
	private String strObservacionesHistorial;
	@EJB
	private SistemaFirmasPoderesDAO sfpDAO;
	@EJB
	private EscaneoWebDAO ewDAO;
	@EJB
	private DocumentoExpBusiness business;
	@EJB
	private DocumentoExpDAO documentoExpDAO;
	@EJB
	private ParticipeDAO participeDAO;
	@EJB
	private HistorialDAO historialDAO;
	@EJB
	private GuiaDocumentDAO guiaDocumentDAO;
	@EJB
	private CasoNegocioDAO casoNegocioDAO;
		
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		mostrar = true;
		iniciarApplet = true;
		disLinkVistaUnica = false;
		mostCasoNegocio = false;
		cleanTransferDir = "0";
		
		listaTipoPJSFP = sfpDAO.obtenerTiposPJ();
		listaTipoPJ = Util.crearItems(listaTipoPJSFP, true, "codigo", "descripcion");
		LOG.info("**********listaTipoPJ********"+listaTipoPJ.size());
		listaDocumentoExp = Collections.EMPTY_LIST;
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);	
		crearListaParticipes(expediente);
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		
		escaneoWeb = ewDAO.findById(new Integer(1));
		
		empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
			
		LOG.info("expediente {}", expediente);
		
		if (!(expediente==null || expediente.getCliente()==null)){
		    codigoCentral = expediente.getCliente().getCodigoCentral() == null ?"":expediente.getCliente().getCodigoCentral();		  
		}else{
			disLinkVistaUnica = false;
		}
		LOG.info("disLinkVistaUnica {}", disLinkVistaUnica);
		LOG.info("formRegistrarNuevoBastanteo.equals(pagina) {}", "formRegistrarNuevoBastanteo".equals(pagina));
		
		if ("formRegistrarNuevoBastanteo".equals(pagina)) {
			managedBean = registrarNuevoBastanteo;
			mostRegMod = true;
			mostObsGen = false;
			mostImagen=true;
			mostApplet=true;
			mostEscaneado=false;
			mostNroPagina=true;
			LOG.info("mostRegMod {}", mostRegMod);
			LOG.info("mostEscaneado {}", mostEscaneado);
			
			LOG.info("**********expediente.getCuentaCobroComision()**********", expediente.getCuentaCobroComision());
			LOG.info("**********expediente.getOperacion()**********", expediente.getOperacion());
			/*
			operacion = expediente.getOperacion();
			if (expediente.getCuentaCobroComision() != null) {
				LOG.info("**********call seleccionarTipoPJ**********");
				seleccionarTipoPJ (expediente.getCodTipoPj(), expediente.getProducto().getCodigo());
				mostrar = true;
			} else {
				mostrar = false;
			}
			*/
			
			LOG.info("codTipoPJ : {}",expediente.getCodTipoPj());
			selTipoPJ = expediente.getCodTipoPj();
			if (selTipoPJ!=null && !selTipoPJ.trim().equals("")) {
				soloLectTipoPJ = true;
			} else {
				soloLectTipoPJ = false;
			}
			
		} else if ("formSubsanarDocumentos".equals(pagina)) {
			managedBean = subsanarDocumento;
			operacion = expediente.getOperacion();
			Producto prod = expediente.getProducto();
			seleccionarTipoPJ (expediente.getCodTipoPj(), prod == null? null : prod.getCodigo());
			mostRegMod = false;
			mostEscaneado=false;
			mostObservaciones=true;
			mostObsGen = true;
			mostImagen=true;
			mostFirmas=false;
			mostApplet=true;
			mostNroPagina=true;
			
			// Fix incidencia observación en subsanar documentos
			Historial historial = historialDAO.findUltimaTarea(expediente.getId(), ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_DOCUMENTOS);
			if (historial != null) {
				strObservacionesHistorial = historial.getObservaciones();
			}
		} else if ("formSubsanarFirmas".equals(pagina)) {
			managedBean = subsanarFirmas;
			operacion = expediente.getOperacion();
			Producto prod = expediente.getProducto();
			seleccionarTipoPJ (expediente.getCodTipoPj(), prod == null? null : prod.getCodigo());
			mostRegMod = false;
			mostEscaneado=false;
			mostImagen=true;
			mostObservaciones=true;
			mostObsGen = true;
			mostFirmas=true;
			mostApplet=true;
			mostNroPagina=false;
			
			// Fix incidencia observación en subsanar documentos
			Historial historial = historialDAO.findUltimaTarea(expediente.getId(), ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_FIRMAS);
			if (historial != null) {
				strObservacionesHistorial = historial.getObservaciones();
			}
		} else if ("formPreRegistro".equals(pagina)) {
			LOG.info("formPreRegistro");
			managedBean = preRegistro;
			mostRegMod = true;
			mostImagen=true;
			mostObsGen = false;
			mostApplet=true;
			mostEscaneado=false;
			mostNroPagina=true;
			operacion = expediente.getOperacion();
			LOG.info("expediente.getCodTipoPj(): " + expediente.getCodTipoPj());
			
			String codigoProducto ="";
			if (expediente.getProducto()!=null && expediente.getProducto().getCodigo()!=null){
				codigoProducto = expediente.getProducto().getCodigo();
			}
			seleccionarTipoPJ (expediente.getCodTipoPj(), codigoProducto);
//			if (expediente.getCuentaCobroComision() != null) {
//				seleccionarTipoPJ (expediente.getCodTipoPj(), expediente.getProducto().getCodigo());
//				mostrar = true;
//			} else {
//				mostrar = false;
//			}
//			LOG.info("mostrar:" + mostrar);
		}else if ("formAprobarRevocatoria".equals(pagina)){
			managedBean=aprobarRevocatoria;
			operacion = expediente.getOperacion();
			Producto prod = expediente.getProducto();
			seleccionarTipoPJ (expediente.getCodTipoPj(), prod == null? null : prod.getCodigo());
			mostRegMod = false;
			mostObsGen = false;
			mostImagen=true;
			mostFirmas=false;
			mostApplet=false;
			mostEscaneado=false;
			mostNroPagina=false;
		}else if ("formRevisarEjecutarRevocatoria".equals(pagina)){
			managedBean=revisarEjecutarRevocatoria;
			operacion = expediente.getOperacion();
			Producto prod = expediente.getProducto();
			seleccionarTipoPJ (expediente.getCodTipoPj(), prod == null? null : prod.getCodigo());
			mostRegMod = false;
			mostObsGen = false;
			mostFirmas=false;
			mostImagen=true;
			mostApplet=false;
			mostEscaneado=false;
			mostNroPagina=false;
		}else if("formRevisarAprobarBastanteo".equals(pagina)){
			managedBean=revisarAprobarBastanteo;
			operacion = expediente.getOperacion();
			Producto prod = expediente.getProducto();
			seleccionarTipoPJ (expediente.getCodTipoPj(), prod == null? null : prod.getCodigo());
			mostRegMod = false;
			mostObsGen = false;
			mostFirmas=false;
			mostImagen=false;
			mostApplet=false;
			mostEscaneado=false;
			mostNroPagina=false;
			disLinkVistaUnica = true;
		}else if ("formVerificarRealizarBastanteo".equals(pagina)){
			managedBean=verificarRealizarBastanteo;
			operacion = expediente.getOperacion();
			Producto prod = expediente.getProducto();
			seleccionarTipoPJ (expediente.getCodTipoPj(), prod == null? null : prod.getCodigo());
			mostRegMod = false;
			mostEscaneado=false;			
			mostObservaciones=false;
			mostImagen=false;
			mostObsGen = false;
			mostFirmas=false;
			mostApplet=false;
			mostrarTab=false;
			mostNroPagina=false;
			disLinkVistaUnica = true;
		}else if ("formVerificarCalidadDocumentos".equals(pagina)){
			disLinkVistaUnica = true;
		}
		LOG.info("managedBean.setDigitaliceDocumentacion(this)");
		managedBean.setDigitaliceDocumentacion(this);
		
		setIdExpediente(expediente.getId());
		LOG.info("expediente init : {}",expediente);
	}
	
	public boolean isMostrarTab() {
		return mostrarTab;
	}

	public void setMostrarTab(boolean mostrarTab) {
		this.mostrarTab = mostrarTab;
	}

	public boolean isMostImagen() {
		return mostImagen;
	}

	public void setMostImagen(boolean mostImagen) {
		this.mostImagen = mostImagen;
	}

	public void seleccionarTipoPJ (ActionEvent ae) {
		LOG.info("seleccionarTipoPJ (ValueChangeEvent vce)");
		selTipoPJ = getRequestParameter("idTipoPJ");
		String codProd = cuenta==null ? null : cuenta.getProductoCod();
		LOG.info("tipo PJ seleccionado : {} - cod producto : {}", selTipoPJ, codProd);
		if (!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(selTipoPJ)) {
			seleccionarTipoPJ(selTipoPJ, codProd);
		} else {
			mostCasoNegocio = false;
			listaDocumentoExp = Collections.EMPTY_LIST;
			crearTrama();
		}
		LOG.info("elementos en la lista : {}", listaDocumentoExp.size());
	}
	
	public void hayEscaneoMetodo(AjaxBehaviorEvent event){		
		boolean hayEscaneo=false;
		for (int i=0; i<listaDocumentoExp.size();i++){
			String nombreArchivo = listaDocumentoExp.get(i).getDocumentoExp().getNombreArchivo();
			if (StringUtils.isEmpty(nombreArchivo)){
				hayEscaneo=true;
				 break;
			}
		}
		if (hayEscaneo){
			deshabilitarBotonReasignar(false);
		}
	}
	
	public void deshabilitarBotonReasignar(boolean habReasignar){
		managedBean.habilitarBoton(habReasignar);
	}
	
	public void seleccionarTipoPJ() {
		seleccionarTipoPJ (expediente.getCodTipoPj(), cuenta.getProductoCod());
	}

	public void seleccionarTipoPJ(String codTipoPJ, String codProd) {
		LOG.info("seleccionarTipoPJ(String codTipoPJ, String codProd)");
		selTipoPJ = codTipoPJ;
		LOG.info("codTipoPJ : {}",codTipoPJ);
		if (selTipoPJ!=null && !selTipoPJ.trim().equals("")) {
			soloLectTipoPJ = true;
		} else {
			soloLectTipoPJ = false;
		}
		if ((codTipoPJ == null) || (codProd == null)) {
			mostCasoNegocio = false;
			listaDocumentoExp = Collections.EMPTY_LIST;
			crearTrama();
			return;
		}
		
		// Solo los expedientes nuevos o en preRegistro sin documentos permiten modificar el caso de negocio
		if (expediente.getId() == null || esPreRegistroAutomaticoSinDocumentos()) {
			soloLectCasoNegocio = false;
			List<GuiaDocument> lstGDTmp = guiaDocumentDAO.findByTipoPJOperacionCasoNegocio(codTipoPJ, operacion.getId(), ConstantesBusiness.ID_CASO_NEGOCIO_NINGUNO);
			if (lstGDTmp.size() > 0) { // existe guía que no tiene definido caso de negocio
				LOG.info("existe guía que no tiene definido caso de negocio");
				mostCasoNegocio = false;
				seleccionarCasoNegocio(codTipoPJ, codProd, new Integer(ConstantesBusiness.ID_CASO_NEGOCIO_NINGUNO));
			} else {
				mostCasoNegocio = true;
				listaCasoNegocioBD = casoNegocioDAO.listaCasosNegocioGuia(codTipoPJ, operacion.getId());
				if (listaCasoNegocioBD.size() == 1) {
					LOG.info("existe guía solamente para un caso de negocio");
					listaCasoNegocio = Util.crearItems(listaCasoNegocioBD, false, "id", "descripcion", true);
					seleccionarCasoNegocio(codTipoPJ, codProd, listaCasoNegocioBD.get(0).getId());
				} else if (listaCasoNegocioBD.size() == 0) { // no hay ningún caso de negocio
					LOG.info("no existe guía para esos parámetros");
					mostCasoNegocio = false;
					listaCasoNegocio = Collections.EMPTY_LIST;
					seleccionarCasoNegocio(codTipoPJ, codProd, new Integer(ConstantesAdmin.CODIGO_CAMPO_VACIO));
				} else {
					LOG.info("existe guía para más de caso de negocio");
					listaCasoNegocio = Util.crearItems(listaCasoNegocioBD, true, "id", "descripcion", true);
					seleccionarCasoNegocio(codTipoPJ, codProd, new Integer(ConstantesAdmin.CODIGO_CAMPO_VACIO));
				}
			}
		} else {
			LOG.info("el expediente ya tiene guía cargada");
			soloLectCasoNegocio = true;
			if (expediente.getCasoNegocio() == null || expediente.getCasoNegocio().getId().equals(ConstantesBusiness.ID_CASO_NEGOCIO_NINGUNO)) {
				mostCasoNegocio = false;
				listaCasoNegocio = Collections.EMPTY_LIST;
				seleccionarCasoNegocio(codTipoPJ, codProd, new Integer(ConstantesBusiness.ID_CASO_NEGOCIO_NINGUNO));
			} else {
				mostCasoNegocio = true;
				listaCasoNegocioBD = new ArrayList<CasoNegocio>();
				listaCasoNegocioBD.add(expediente.getCasoNegocio());
				listaCasoNegocio = Util.crearItems(listaCasoNegocioBD, false, "id", "descripcion", true);
				seleccionarCasoNegocio(codTipoPJ, codProd, expediente.getCasoNegocio().getId());
			}
			seleccionarCasoNegocio(codTipoPJ, codProd, expediente.getCasoNegocio() != null ? expediente.getCasoNegocio().getId() : new Integer(ConstantesBusiness.ID_CASO_NEGOCIO_NINGUNO));
		}
//		crearListaDocumentoExp(codTipoPJ, codProd);
	}
	
	public void seleccionarCasoNegocio(String codTipoPJ, String codProd, Integer idCasoNegocio) {
		selCasoNegocio = idCasoNegocio.toString();
		if (!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(selCasoNegocio)) {
			CasoNegocio casoNegocioAyuda = casoNegocioDAO.load(idCasoNegocio);
			if (casoNegocioAyuda != null) {
				casoNegocioTextoAyuda = casoNegocioAyuda.getTextoAyudaStr();
			}
			crearListaDocumentoExp(codTipoPJ, codProd, idCasoNegocio);
		} else {
			listaDocumentoExp = Collections.EMPTY_LIST;
			casoNegocioTextoAyuda = null;
			crearTrama();
		}
	}
	
	public void seleccionarCasoNegocioListener(ValueChangeEvent ae) {
		selCasoNegocio = (String) ae.getNewValue();
		seleccionarCasoNegocio(selTipoPJ, cuenta.getProductoCod(), Integer.parseInt(selCasoNegocio));
	}
	
	private boolean esPreRegistroAutomaticoSinDocumentos() {
		boolean esPreRegistroAutomaticoSinDocumentos = false;
		if (expediente.getId() != null
				&& (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())
					|| ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion()))
				&& ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO == expediente.getEstado().getId().intValue()) {
			List<DocumentoExp> listaDocExp = documentoExpDAO.findByExpdiente(expediente.getId());
			if (listaDocExp == null || listaDocExp.size() == 0) {
				esPreRegistroAutomaticoSinDocumentos = true;
			} else {
				esPreRegistroAutomaticoSinDocumentos = false;
			}
		}
		return esPreRegistroAutomaticoSinDocumentos;
	}

	private void crearListaDocumentoExp(String codTipoPJ, String codProd, Integer idCasoNegocio) {
		LOG.info("crearListaDocumentoExp(String codTipoPJ, String codProd, idCasoNegocio)"+codTipoPJ+","+codProd+","+idCasoNegocio);
		LOG.info("codigoCentral : "+codigoCentral);   
		listaDocumentoExp = new ArrayList<DocumentoExpWrapper1> ();
		LOG.info("expediente.getId() : "+expediente.getId());
		cleanTransferDir = "1"; //para limpiar la carpeta Transferencias_CC
		
		if (expediente.getId() == null || esPreRegistroAutomaticoSinDocumentos()) {
			List<GuiaDocument> listaGuia = business.obtenerDocumentosExp(codProd, codTipoPJ, operacion, casoNegocioDAO.load(idCasoNegocio));
			for (GuiaDocument gd : listaGuia) {
				DocumentoExpWrapper1 dew1 = new DocumentoExpWrapper1 (gd, codigoCentral, operacion.getCodigoOperacion());
				//dew1.setRutaCM(ConstantesBusiness.RUTA_CM_VISOR);
				listaDocumentoExp.add(dew1);
			}
		} else {
			LOG.info("DocumentoExpDAO");
			List<DocumentoExp> listaDocExp=null;
			String pagina=getNombrePrincipal();
			LOG.info("**************pagina**********"+pagina);
			if (("formRegistrarNuevoBastanteo".equals(pagina))|| ("formPreRegistro".equals(pagina))){
				LOG.info("**************documentoExpDAO.findByExpdiente**********"+expediente.getId());
				listaDocExp = documentoExpDAO.findByExpdiente(expediente.getId());
			}else if (("formRevisarAprobarBastanteo".equals(pagina))||("formAprobarRevocatoria".equals(pagina))|| ("formVerificarRealizarBastanteo".equals(pagina))){
				String documentosLegales=ConstantesBusiness.TIPO_DOCUMENTO_LEGAL;
				listaDocExp=documentoExpDAO.findByDocumentosLegales(expediente.getId(),documentosLegales);
			}else if ("formSubsanarFirmas".equals(pagina)){
				//String documentosLegales=ConstantesBusiness.TIPO_DOCUMENTO_LEGAL;
				listaDocExp = documentoExpDAO.findByExpdienteRechazado(expediente.getId(),
						ConstantesBusiness.CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD,
						ConstantesBusiness.CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS);
				//listaDocExp=documentoExpDAO.findByDocumentosLegalesRechazados(expediente.getId(),documentosLegales);
			}else if ("formSubsanarDocumentos".equals(pagina)){
				//String documentosLegales=ConstantesBusiness.TIPO_DOCUMENTO_LEGAL;
				//listaDocExp=documentoExpDAO.findByDocumentosLegalesRechazados(expediente.getId(), documentosLegales);
				listaDocExp=documentoExpDAO.findByExpdiente(expediente.getId()); // se debe visualizar la guía completa
			}else if ("formRevisarEjecutarRevocatoria".equals(pagina)){
				listaDocExp=documentoExpDAO.findByExpdienteCartaRevocatoria(expediente.getId(),
						ConstantesBusiness.CODIGO_CARTA_REVOCATORIA);
			}
			int idTarea = expediente.getExpedienteTareas().iterator().next().getTarea().getId();
			String tramaPidCM = "";
			String tramaNombreArchivo = "";
			String tramaTipo = "";
			
		    int i = 0;
			for (DocumentoExp de : listaDocExp) {
				LOG.info("*************DocumentoExp de : listaDocExp**********"+de.getDocumento().getDescripcion());
				LOG.info("*************codigoCentral**********"+codigoCentral);
				LOG.info("*************operacion.getCodigoOperacion()**********"+operacion.getCodigoOperacion());
				LOG.info("*************idTarea**********"+idTarea);
				DocumentoExpWrapper1 dew1 = new DocumentoExpWrapper1 (de, codigoCentral, operacion.getCodigoOperacion(), idTarea);
				LOG.info("*************DocumentoExpWrapper1**********"+dew1);
				//dew1.setRutaCM(ConstantesBusiness.RUTA_CM_VISOR);
				listaDocumentoExp.add(dew1);
				
				if (listaDocumentoExp.get(i).getTipoVisor()==2) {
					if (i==0) {
						tramaPidCM = tramaPidCM + listaDocumentoExp.get(i).getDocumentoExp().getPidCM();
						tramaNombreArchivo = tramaNombreArchivo + listaDocumentoExp.get(i).getDocumentoExp().getNombreArchivo();
					}else{
						tramaPidCM = tramaPidCM + "|" + listaDocumentoExp.get(i).getDocumentoExp().getPidCM();
						tramaNombreArchivo = tramaNombreArchivo + "|" +  listaDocumentoExp.get(i).getDocumentoExp().getNombreArchivo();
					}
				}
				i++;				
			}
			
			LOG.info("tramaPidCM : "+tramaPidCM);
			LOG.info("tramaNombreArchivo : "+tramaNombreArchivo);
			
			tramaParametrosApplet = "&parnom="+tramaNombreArchivo+"&parpid="+tramaPidCM+"&type=";
		}
		LOG.info("operacion -> {}", operacion.getCodigoOperacion());
		if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion.getCodigoOperacion())) {
			mostEscaneado=true;
		}
		if ("formSubsanarDocumentos".equals(getNombrePrincipal())){
			crearTramaSubsanarDocumentos();
		} else {
			crearTrama();
		}
	}
	
	private void crearTrama () {
		LOG.info("crearTrama ()");
		StringBuilder builder = new StringBuilder ();
		StringBuilder descarga = new StringBuilder ();
		boolean esPrimero = true;
		boolean esPrimeroD = true;
		boolean faltaDocumentos;
		
		if (listaDocumentoExp.isEmpty()) {
			faltaDocumentos = true;
		} else {
			faltaDocumentos = false;
		}
		ejecutoCobroComision = ConstantesBusiness.FLAG_NO_EJECUTO_COBRO_COMISION;
		int escaneados = 0;
		for (DocumentoExpWrapper1 dew1 : listaDocumentoExp) {
			LOG.info("dew1.getDocumentoExp().getFlagEscaneado() : ", dew1.getDocumentoExp().getFlagEscaneado());
			LOG.info("dew1.getDocumentoExp().getDocumento().getDescripcion() : ", dew1.getDocumentoExp().getDocumento().getDescripcion());
			boolean estaEscaneado = ConstantesBusiness.FLAG_ESCANEADO.equals(dew1.getDocumentoExp().getFlagEscaneado());
			if (estaEscaneado) {
				if (!esPrimeroD){
					descarga.append(";");
				}
				String codDoc = dew1.getDocumentoExp().getDocumento().getCodigoDocumento();
				LOG.info("CodigoDocumento : ", codDoc);
				if (ConstantesBusiness.CODIGO_DOCUMENTO_VOUCHER_PAGO_COMISION_BASTANTEO.equals(codDoc)) {
					ejecutoCobroComision = ConstantesBusiness.FLAG_EJECUTO_COBRO_COMISION;
				}
				descarga.append(codDoc);
				esPrimeroD = false;
				escaneados++;
			} else{
				Documento doc = dew1.getDocumentoExp().getDocumento();
				if (!esPrimero){
					builder.append(";");
				}
				builder.append(doc.getCodigoDocumento()).append(";").append(doc.getDescripcion());
				esPrimero = false;
				//if (dew1.isReqEscaneo()) {
				if (dew1.isObligatorio()) {
					faltaDocumentos = true;
				}
			}
		}
		Character flagDocumentos;
		if (faltaDocumentos) {
			if (escaneados == 0) {
				flagDocumentos = ConstantesBusiness.SIN_DOCUMENTOS;
			} else {
				flagDocumentos = ConstantesBusiness.FALTA_DOCUMENTOS;
			}
		} else {
			flagDocumentos = ConstantesBusiness.TODO_DOCUMENTOS;
		}
		tramaDescarga = descarga.toString();
		tramaDocumentos = builder.toString();
		LOG.info("crearTrama - Trama : {}", tramaDocumentos);
		LOG.info("crearTrama - faltaDocumentos : {}", faltaDocumentos);
		if(isModoConsulta()){
			tramaDocumentos="";
			tramaDescarga="";
		}
		managedBean.actualizarSubidaDocumentos (flagDocumentos);
	}
	
	private void crearTramaSubsanarDocumentos() {
		LOG.info("crearTramaSubsanarDocumentos ()");
		StringBuilder builder = new StringBuilder ();
		StringBuilder descarga = new StringBuilder ();
		boolean esPrimero = true;
		boolean esPrimeroD = true;
		boolean faltaDocumentos;
		
		if (listaDocumentoExp.isEmpty()) {
			faltaDocumentos = true;
		} else {
			faltaDocumentos = false;
		}
		ejecutoCobroComision = ConstantesBusiness.FLAG_NO_EJECUTO_COBRO_COMISION;
		int escaneados = 0;
		for (DocumentoExpWrapper1 dew1 : listaDocumentoExp) {
			LOG.info("dew1.getDocumentoExp().getFlagEscaneado() : ", dew1.getDocumentoExp().getFlagEscaneado());
			LOG.info("dew1.getDocumentoExp().getDocumento().getDescripcion() : ", dew1.getDocumentoExp().getDocumento().getDescripcion());
			
			if (dew1.getDocumentoExp().getDocumento().getCodigoDocumento().equals(ConstantesBusiness.CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD)
					|| dew1.getDocumentoExp().getDocumento().getCodigoDocumento().equals(ConstantesBusiness.CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS)) {
				dew1.setDisEliminarDoc(true); // los documentos No Jurídicos no deben poder adjuntarse ni eliminarse en Subsanación de Documentos
				continue;
			}
			
			boolean estaEscaneado = ConstantesBusiness.FLAG_ESCANEADO.equals(dew1.getDocumentoExp().getFlagEscaneado());
			
			// Al subsanar documentos se pueden adjuntar documentos ya escaneados para que se haga merge en content
			Documento doc = dew1.getDocumentoExp().getDocumento();
			if (!esPrimero){
				builder.append(";");
			}
			builder.append(doc.getCodigoDocumento()).append(";").append(doc.getDescripcion());
			esPrimero = false;
			
			if (estaEscaneado) {
				if (!esPrimeroD){
					descarga.append(";");
				}
				String codDoc = dew1.getDocumentoExp().getDocumento().getCodigoDocumento();
				LOG.info("CodigoDocumento : ", codDoc);
				if (ConstantesBusiness.CODIGO_DOCUMENTO_VOUCHER_PAGO_COMISION_BASTANTEO.equals(codDoc)) {
					ejecutoCobroComision = ConstantesBusiness.FLAG_EJECUTO_COBRO_COMISION;
				}
				descarga.append(codDoc);
				esPrimeroD = false;
				escaneados++;
			} else{
				//if (dew1.isReqEscaneo()) {
				if (dew1.isObligatorio()) {
					faltaDocumentos = true;
				}
			}
		}
		Character flagDocumentos;
		if (faltaDocumentos) {
			if (escaneados == 0) {
				flagDocumentos = ConstantesBusiness.SIN_DOCUMENTOS;
			} else {
				flagDocumentos = ConstantesBusiness.FALTA_DOCUMENTOS;
			}
		} else {
			flagDocumentos = ConstantesBusiness.TODO_DOCUMENTOS;
		}
		tramaDescarga = descarga.toString();
		tramaDocumentos = builder.toString();
		LOG.info("crearTrama - Trama : {}", tramaDocumentos);
		LOG.info("crearTrama - faltaDocumentos : {}", faltaDocumentos);
		if(isModoConsulta()){
			tramaDocumentos="";
			tramaDescarga="";
		}
		managedBean.actualizarSubidaDocumentos (flagDocumentos);
	}
	
	private void crearListaParticipes(Expediente expediente){
		if (expediente!=null && expediente.getCliente()!=null) {	  
			listaFirmaObservada=participeDAO.findByFirmaObservadas(expediente);
		} else {
			listaFirmaObservada = Collections.EMPTY_LIST;
		}
	}
	
	public void eliminarDocumento (ActionEvent ae) {
		LOG.info("eliminarDocumento (ActionEvent ae)");
		String docum = getRequestParameter("codigoDocumento");
		DocumentoExpWrapper1 doc = buscarPorCodigoDocumento (docum);
		if (doc!=null) {
			DocumentoExp docExp = doc.getDocumentoExp();
			docExp.setFlagEscaneado(ConstantesBusiness.FLAG_NO_ESCANEADO);
			docExp.setNombreArchivo(null);
			docExp.setFechaEscaneo(null);
			docExp.setMotivo(null);
			docExp.setFlagCm(null);
			docExp.setPidCM(null);
			docExp.setIdCm(null);
			docExp.setIdCmCopia(null);
			docExp.setFlagRechazado(null);
			docExp.setFlagAlterno(null);
			
			doc.setDocumentoCM(null);
			doc.actualizarVisor();

			if ("formSubsanarDocumentos".equals(getNombrePrincipal())){
				crearTramaSubsanarDocumentos();
			} else {
				crearTrama();
			}
		}
	}
	
	public void actualizarDocumentos (ActionEvent ae) {
		LOG.info("actualizarDocumentos (ActionEvent ae)");
		LOG.info("listaDocumentos : {}", listaDocumentos);
		if (listaDocumentos!=null && !listaDocumentos.equals("")) {
			StringTokenizer st = new StringTokenizer (listaDocumentos, ",");
			while (st.hasMoreTokens()) {
				String tk = st.nextToken();
				LOG.info("documento : {}", tk);
				DocumentoExpWrapper1 doc = buscarPorCodigoDocumento (tk);
				LOG.info("DocumentoExpWrapper1 : {}", doc);
				if (doc!=null) {
					DocumentoExp docExp = doc.getDocumentoExp();
					
					boolean mergeDocumento = false;
					if (ConstantesBusiness.FLAG_ESCANEADO.equals(docExp.getFlagEscaneado()) && docExp.getIdCm() != null) {
						mergeDocumento = true;
					}
					if (!mergeDocumento) {
						docExp.setFlagEscaneado(ConstantesBusiness.FLAG_ESCANEADO);
						Date fechaEscaneo = new Date ();
						docExp.setNombreArchivo(ConstantesBusiness.FORMATO_FECHA.format(fechaEscaneo));
						docExp.setFechaEscaneo(fechaEscaneo);
						docExp.setFlagCm(null);
						docExp.setPidCM(null);
						docExp.setIdCm(null);
						docExp.setIdCmCopia(null);
						docExp.setFlagAlterno(null);
						doc.setDocumentoCM(null);
					} else {
						docExp.setMotivo(null);
						docExp.setFlagRechazado(null);
						docExp.setFlagAlterno("1"); // este campo no estaba siendo usado, ahora sirve para saber si el documento ha sido combinado
						doc.setDocumentoCM(null); // para que el visor y el botón eliminar usen el documento local 
					}
					
					doc.actualizarVisor();
				}
			}
			if ("formSubsanarDocumentos".equals(getNombrePrincipal())){
				crearTramaSubsanarDocumentos();
			} else {
				crearTrama();
			}
		}
	}
	
	private DocumentoExpWrapper1 buscarPorCodigoDocumento (String codigo) {
		LOG.info("buscarPorCodigoDocumento (String codigo)");
		for (DocumentoExpWrapper1 dew : listaDocumentoExp) {
			LOG.info("dew.getDocumentoExp().getDocumento().getCodigoDocumento()) : {}", dew.getDocumentoExp().getDocumento().getCodigoDocumento());
			if (codigo.equals(dew.getDocumentoExp().getDocumento().getCodigoDocumento())) {
				return dew;
			}
		}
		return null;
	}
	
	public boolean esValido () {
		LOG.info("esValido ()");
		LOG.info("selTipoPJ : {}", selTipoPJ);
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(selTipoPJ)) {
			mensajeErrorPrincipal("idDigDoc:idTipoPJ", "Seleccione un tipo de PJ");
			return false;
		}
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(selCasoNegocio)) {
			mensajeErrorPrincipal("idDigDoc:idCasoNegocio", "Seleccione un caso de negocio");
			return false;
		}
		return true;
	}

	public void copiarDatos() {
		LOG.info("copiarDatos()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("expediente fin "+expediente);
		TipoPJSFP tipoPJSFP = null;
		for (TipoPJSFP tpj : listaTipoPJSFP) {
			if (tpj.getCodigo().equals(selTipoPJ)) {
				tipoPJSFP = tpj;
				break;
			}
		}
		if (tipoPJSFP != null) {
			expediente.setCodTipoPj(tipoPJSFP.getCodigo());
			expediente.setDesTipoPj(tipoPJSFP.getDescripcion());
		}
		if (!ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(selCasoNegocio)) {
			LOG.info("selCasoNegocio: "+selCasoNegocio);
			expediente.setCasoNegocio(casoNegocioDAO.load(Integer.parseInt(selCasoNegocio)));
		}
		expediente.setFlagEjecutoCobroComision(ejecutoCobroComision);
		listaDocExp = new ArrayList<DocumentoExp> ();
		for (DocumentoExpWrapper1 docExp : listaDocumentoExp) {
			listaDocExp.add(docExp.getDocumentoExp());
		}
	}
	
	public List<DocumentoExp> obtenerDocumentosExpMemoria() {
		List<DocumentoExp> lista = new ArrayList<DocumentoExp> ();
		for (DocumentoExpWrapper1 docExp : listaDocumentoExp) {
			lista.add(docExp.getDocumentoExp());
		}
		return lista;
	}

	public List<SelectItem> getListaTipoPJ() {
		return listaTipoPJ;
	}

	public void setListaTipoPJ(List<SelectItem> listaTipoPJ) {
		this.listaTipoPJ = listaTipoPJ;
	}

	public List<SelectItem> getListaCasoNegocio() {
		return listaCasoNegocio;
	}

	public void setListaCasoNegocio(List<SelectItem> listaCasoNegocio) {
		this.listaCasoNegocio = listaCasoNegocio;
	}

	public String getSelTipoPJ() {
		return selTipoPJ;
	}

	public void setSelTipoPJ(String selTipoPJ) {
		this.selTipoPJ = selTipoPJ;
	}

	public String getSelCasoNegocio() {
		return selCasoNegocio;
	}

	public void setSelCasoNegocio(String selCasoNegocio) {
		this.selCasoNegocio = selCasoNegocio;
	}

	public boolean isMostrar() {
		return mostrar;
	}

	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}

	public List<DocumentoExpWrapper1> getListaDocumentoExp() {
		return listaDocumentoExp;
	}

	public void setListaDocumentoExp(List<DocumentoExpWrapper1> listaDocumentoExp) {
		this.listaDocumentoExp = listaDocumentoExp;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Operacion getOperacion() {
		return operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	public SubsanarDocumentoMB getSubsanarDocumento() {
		return subsanarDocumento;
	}

	public void setSubsanarDocumento(SubsanarDocumentoMB subsanarDocumento) {
		this.subsanarDocumento = subsanarDocumento;
	}

	public boolean isMostRegMod() {
		return mostRegMod;
	}

	public void setMostRegMod(boolean mostRegMod) {
		this.mostRegMod = mostRegMod;
	}

	public boolean isMostCasoNegocio() {
		return mostCasoNegocio;
	}

	public void setMostCasoNegocio(boolean mostCasoNegocio) {
		this.mostCasoNegocio = mostCasoNegocio;
	}

	public boolean isMostObsGen() {
		return mostObsGen;
	}

	public void setMostObsGen(boolean mostObsGen) {
		this.mostObsGen = mostObsGen;
	}

	public boolean isSoloLectTipoPJ() {
		return soloLectTipoPJ;
	}

	public void setSoloLectTipoPJ(boolean soloLectTipoPJ) {
		this.soloLectTipoPJ = soloLectTipoPJ;
	}

	public boolean isSoloLectCasoNegocio() {
		return soloLectCasoNegocio;
	}

	public void setSoloLectCasoNegocio(boolean soloLectCasoNegocio) {
		this.soloLectCasoNegocio = soloLectCasoNegocio;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public String getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(String listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public boolean isIniciarApplet() {
		return iniciarApplet;
	}

	public void setIniciarApplet(boolean iniciarApplet) {
		this.iniciarApplet = iniciarApplet;
	}

	public String getTramaDocumentos() {
		return tramaDocumentos;
	}

	public void setTramaDocumentos(String tramaDocumentos) {
		this.tramaDocumentos = tramaDocumentos;
	}

	public List<DocumentoExp> getListaDocExp() {
		return listaDocExp;
	}

	public void setListaDocExp(List<DocumentoExp> listaDocExp) {
		this.listaDocExp = listaDocExp;
	}

	public SubsanarFirmasMB getSubsanarFirmas() {
		return subsanarFirmas;
	}

	public void setSubsanarFirmas(SubsanarFirmasMB subsanarFirmas) {
		this.subsanarFirmas = subsanarFirmas;
	}

	public IDigitaliceDocumentacion getManagedBean() {
		return managedBean;
	}

	public void setManagedBean(IDigitaliceDocumentacion managedBean) {
		this.managedBean = managedBean;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public List<Participe> getListaFirmaObservada() {
		return listaFirmaObservada;
	}

	public void setListaFirmaObservada(List<Participe> listaFirmaObservada) {
		this.listaFirmaObservada = listaFirmaObservada;
	}

	public AprobarRevocatoriaMB getAprobarRevocatoria() {
		return aprobarRevocatoria;
	}

	public void setAprobarRevocatoria(AprobarRevocatoriaMB aprobarRevocatoria) {
		this.aprobarRevocatoria = aprobarRevocatoria;
	}

	public RevisarEjecutarRevocatoriaMB getRevisarEjecutarRevocatoria() {
		return revisarEjecutarRevocatoria;
	}

	public void setRevisarEjecutarRevocatoria(
			RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria) {
		this.revisarEjecutarRevocatoria = revisarEjecutarRevocatoria;
	}

	public RevisarAprobarBastanteoMB getRevisarAprobarBastanteo() {
		return revisarAprobarBastanteo;
	}

	public void setRevisarAprobarBastanteo(
			RevisarAprobarBastanteoMB revisarAprobarBastanteo) {
		this.revisarAprobarBastanteo = revisarAprobarBastanteo;
	}

	public void setRegistrarNuevoBastanteo(RegistrarNuevoBastanteoMB registrarNuevoBastanteo) {
		this.registrarNuevoBastanteo = registrarNuevoBastanteo;
	}

	public RegistrarNuevoBastanteoMB getRegistrarNuevoBastanteo() {
		return registrarNuevoBastanteo;
	}

	public void setPreRegistro(PreRegistroMB preRegistro) {
		this.preRegistro = preRegistro;
	}

	public PreRegistroMB getPreRegistro() {
		return preRegistro;
	}

	public List<DocumentoExp> getDocumentoExp() {
		return documentoExp;
	}

	public void setDocumentoExp(List<DocumentoExp> documentoExp) {
		this.documentoExp = documentoExp;
	}

	public String getIdentificadorpID() {
		return identificadorpID;
	}

	public void setIdentificadorpID(String identificadorpID) {
		this.identificadorpID = identificadorpID;
	}

	public boolean isMostApplet() {
		return mostApplet;
	}

	public void setMostApplet(boolean mostApplet) {
		this.mostApplet = mostApplet;
	}

	public boolean isMostNroPagina() {
		return mostNroPagina;
	}

	public void setMostNroPagina(boolean mostNroPagina) {
		this.mostNroPagina = mostNroPagina;
	}

	public void setMostFirmas(boolean mostFirmas) {
		this.mostFirmas = mostFirmas;
	}

	public boolean isMostFirmas() {
		return mostFirmas;
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

	public VerificarRealizarBastanteoMB getVerificarRealizarBastanteo() {
		return verificarRealizarBastanteo;
	}

	public void setVerificarRealizarBastanteo(
			VerificarRealizarBastanteoMB verificarRealizarBastanteo) {
		this.verificarRealizarBastanteo = verificarRealizarBastanteo;
	}

	public boolean isMostObservaciones() {
		return mostObservaciones;
	}

	public void setMostObservaciones(boolean mostObservaciones) {
		this.mostObservaciones = mostObservaciones;
	}
	
	public boolean isMostEscaneado() {
		return mostEscaneado;
	}

	public void setMostEscaneado(boolean mostEscaneado) {
		this.mostEscaneado = mostEscaneado;
	}

	public boolean isHayEscaneo() {
		return hayEscaneo;
	}

	public void setHayEscaneo(boolean hayEscaneo) {
		this.hayEscaneo = hayEscaneo;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public String getTramaParametrosApplet() {
		return tramaParametrosApplet;
	}

	public void setTramaParametrosApplet(String tramaParametrosApplet) {
		this.tramaParametrosApplet = tramaParametrosApplet;
	}

	public boolean isDisLinkVistaUnica() {
		return disLinkVistaUnica;
	}

	public void setDisLinkVistaUnica(boolean disLinkVistaUnica) {
		this.disLinkVistaUnica = disLinkVistaUnica;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public EscaneoWeb getEscaneoWeb() {
		return escaneoWeb;
	}

	public void setEscaneoWeb(EscaneoWeb escaneoWeb) {
		this.escaneoWeb = escaneoWeb;
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

	public String getStrObservacionesHistorial() {
		return strObservacionesHistorial;
	}

	public void setStrObservacionesHistorial(String strObservacionesHistorial) {
		this.strObservacionesHistorial = strObservacionesHistorial;
	}

	public String getCasoNegocioTextoAyuda() {
		return casoNegocioTextoAyuda;
	}

	public void setCasoNegocioTextoAyuda(String casoNegocioTextoAyuda) {
		this.casoNegocioTextoAyuda = casoNegocioTextoAyuda;
	}
}
