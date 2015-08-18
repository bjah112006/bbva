package com.ibm.bbva.controller.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Cliente;
import pe.ibm.bean.ClienteWeb;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.Producto;
import pe.ibm.bean.ProductoWeb;
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.SegmentoBeanLocal;
import com.ibm.bbva.session.SubproductoBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarBandeja")
@RequestScoped
public class BuscarBandejaMB extends AbstractMBean {
	
	@EJB
	private TipoDoiBeanLocal tipoDoiBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private SubproductoBeanLocal subproductoBean;
	@EJB
	private SegmentoBeanLocal segmentoBean;
	@EJB
	private TipoOfertaBeanLocal tipoOfertaBean;
	@EJB
	private TerritorioBeanLocal territorioBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	@EJB
	private OficinaBeanLocal oficinaBean;
	@EJB
	private MensajesBeanLocal mensajeBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private FacadeLocal facade;	
	
	private List<SelectItem> tiposDOI;
	private List<SelectItem> productos;
	private List<SelectItem> subProductos;
	private List<SelectItem> segmentos;
	private List<SelectItem> tipoOfertas;	
	private List<SelectItem> territorios;
	private List<SelectItem> oficinas;
	private List<SelectItem> estados;	
	
	private List<Historial> lstHistorial;
	private List<HistorialDetalle> listaDet;
	private List<Documento> listaDocumentosCM;
	
	private String tipoDOISeleccionado;
	private String productoSeleccionado;
	private String subProductoSeleccionado;
	private String segmentoSeleccionado;
	private String tipoOfertaSeleccionado;	
	private String territorioSeleccionado;
	private String oficinaSeleccionado;
	private String estadoSeleccionado;	
	
	private String textoMensajeSinFiltro;
	private String textoMensajeSoloExpediente;
	private String textoMensajeOtrosCriterios;
	
	private DocumentoExpTc objDocumentoExpTc;	
	
	private String codigoExpediente;
	private String numeroDOI;
	private String apPaterno;
	private String apMaterno;
	private String nombres;
	private String codigoRVGL;
	private String codigoPreEvaluador;
	private String numContrato;
	private Date fechaInicio;
	private Date fechaFin;
	
	private boolean mostrarLinkExp=false;
	
	private static final int CARACTERES_DNI = 8;
	private static final int CARACTERES_RUC = 11;
	private static final int MAX_CARACTERES_OTROS = 10;
	private static final String ID_NO_EXISTE_TAREA = "-1";
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarBandejaMB.class);
	
	public BuscarBandejaMB() {
		
	}

	@PostConstruct
	public void init() {
		subProductos = Util.listaVacia();
		oficinas = Util.listaVacia();
		
		/*Carga Lista de Tipo Documento*/
		tiposDOI= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_TIPDOI);
		if(tiposDOI==null || tiposDOI.size()<=0){
			tiposDOI = Util.crearItems(tipoDoiBean.buscarTodos(), true, "id", "descripcion");
			addObjectSession(Constantes.BAND_LIST_TIPDOI, tiposDOI);
			LOG.info("Volvio a cargar tiposDOI");
		}
		if(tiposDOI!=null)
			LOG.info("tiposDOI tamaño:::"+tiposDOI.size());
		else
			LOG.info("tiposDOI es nulo o vacio");
		
		/*Carga Lista de Tipo Producto*/
		productos= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_PROD);
		if(productos==null || productos.size()<=0){
			productos = Util.crearItems(productoBean.buscarTodos(), true, "id", "descripcion");
			addObjectSession(Constantes.BAND_LIST_PROD, productos);
			LOG.info("Volvio a cargar productos");
		}
		/*Carga Lista de Tipo segmento*/
		segmentos= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_SEG);
		if(segmentos==null || segmentos.size()<=0){
			List<Segmento> ls = segmentoBean.buscarTodos();
			LOG.info("Volvio a cargar segmentoBean");
			List<Segmento> segmentoLista = new ArrayList<Segmento>();
			String des="", desAux="";
			for (Segmento s : ls) {	
				des=s.getDescripcion();
				if (!desAux.equals(des)){
					desAux=s.getDescripcion();
					segmentoLista.add(s);
				}				
			}
			
			
			segmentos = Util.crearItems(segmentoLista, true, "id", "descripcion");		
			addObjectSession(Constantes.BAND_LIST_SEG, segmentos);
		}

		
		/*Carga Lista de Tipo Oferta*/
		tipoOfertas= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_TIPOFERTA);
		if(tipoOfertas==null || tipoOfertas.size()<=0){
			tipoOfertas = Util.crearItems(tipoOfertaBean.buscarTodos(), true, "id", "descripcion");
			addObjectSession(Constantes.BAND_LIST_TIPOFERTA, tipoOfertas);
			LOG.info("Volvio a cargar tipoOfertas");
		}
		if(tipoOfertas!=null)
			LOG.info("tipoOfertas tamaño:::"+tipoOfertas.size());
		else
			LOG.info("tipoOfertas es nulo o vacio");
		
		/*Carga Lista de Territorio*/
		territorios= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_TERR);
		if(territorios==null || territorios.size()<=0){
			territorios = Util.crearItems(territorioBean.buscarTodos(), true, "id", "descripcion");
			
			LOG.info("Volvio a cargar territorios");
			Collections.sort(territorios, new Util.SecletItemComparator(false, true));
			addObjectSession(Constantes.BAND_LIST_TERR, territorios);
		}
		
		
		/*Carga Lista de Estado*/
		//estados = Util.crearItems(estadoCEBean.buscarTodosMenosEnDesuso(), true, "id", "descripcion");
		estados= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_EST);
		if(estados==null || estados.size()<=0){
			estados = Util.eliminarCamposRepetidos(Util.crearItems(estadoCEBean.buscarTodosMenosEnDesuso(), true, "id", "descripcion"));
			addObjectSession(Constantes.BAND_LIST_EST, estados);
			LOG.info("Volvio a eliminarCamposRepetidos estados");
		}
	}
	
	public void cambiarSubProductos(ValueChangeEvent vce) {
		Object codigo = vce.getNewValue();
		productoSeleccionado = (String)codigo;
		//LOG.info("productoSeleccionado   "+productoSeleccionado);
		if (productoSeleccionado==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(productoSeleccionado)) {
			subProductos = Util.listaVacia();
		} else {
			crearListaSubProductos (Long.parseLong(productoSeleccionado));
		}
	}

	private void crearListaSubProductos (long idProducto) {
		subProductos = Util.crearItems(subproductoBean.buscarPorIdProducto(idProducto), true,
				"id", "descripcion");
	}
	
	public void cambiarOficina(ValueChangeEvent vce) {
		Object codigo = vce.getNewValue();
		territorioSeleccionado = (String)codigo;
		if (territorioSeleccionado==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(territorioSeleccionado)) {
			oficinas = Util.listaVacia();
		} else {
			crearListaOficina (Long.parseLong(territorioSeleccionado));
		}
	}

	private void crearListaOficina (long idTerritorio) {
		//oficinas = Util.crearItems(oficinaBean.buscarPorIdTerritorio(idTerritorio), true, "id", "descripcion");
		oficinas = Util.crearItemsConcat(oficinaBean.buscarPorIdTerritorio(idTerritorio), true, "id", "codigo", "descripcion");
		Collections.sort(oficinas, new Util.SecletItemComparator(false, true));
	}
	
//	private List<Estado> listaEstadosUnicos () {
//		List<Estado> lista = estadoBean.buscarTodos();
//		Set<Estado> set = new TreeSet<Estado> (new Comparator<Estado>(){
//			public int compare(Estado o1, Estado o2) {
//				return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());
//			}
//		});
//		for (Estado estado : lista) {
//			set.add(estado);
//		}
//		return new ArrayList<Estado>(set);
//	}

	
	public void buscar () {
		Consulta consulta = new Consulta();		
		consulta.setCodigoExpediente(Util.validarCampo (codigoExpediente));
		consulta.setApPaterno(Util.validarCampo (apPaterno));
		consulta.setApMaterno(Util.validarCampo (apMaterno));
		consulta.setNombres(Util.validarCampo (nombres));
		consulta.setCodRVGL(Util.validarCampo (codigoRVGL));
		consulta.setNumeroDOI(Util.validarCampo (numeroDOI));
		consulta.setCodPreEvaluador(Util.validarCampo (codigoPreEvaluador));
		consulta.setNumeroContrato(Util.validarCampo (numContrato));
		//consulta.setTipoDOI(Util.obtenerDescripcion(tiposDOI, tipoDOISeleccionado));
		consulta.setTipoDOI(tipoDOISeleccionado);
		consulta.setIdProducto(Util.validarId(productoSeleccionado));
		consulta.setSubProducto(Util.obtenerDescripcion(subProductos, subProductoSeleccionado));
		//consulta.setTipoOferta(Util.obtenerDescripcion(tipoOfertas, tipoOfertaSeleccionado));
		if(tipoOfertaSeleccionado!=null && !tipoOfertaSeleccionado.equals("-1") && !tipoOfertaSeleccionado.equals(""))
			consulta.setTipoOferta(tipoOfertaSeleccionado);
		
		LOG.info("consulta.getTipoOferta()::::"+consulta.getTipoOferta());
		consulta.setSegmento(Util.obtenerDescripcion(segmentos, segmentoSeleccionado));
		consulta.setIdTerritorio(Util.validarId(territorioSeleccionado));
		consulta.setIdOficina(Util.validarId(oficinaSeleccionado));
		consulta.setEstado(Util.obtenerDescripcion(estados, estadoSeleccionado));
		if (fechaFin!=null) {
		    consulta.setFechaFin(Util.addDaysToDate(fechaFin, 1));
		}
		consulta.setFechaInicio(fechaInicio);
		
		consulta.setConsiderarUsuarios(false);
		
		// Para que en la búsqueda considere el universo a los expedientes que no tienen
		// tareas activas
		
		consulta.setTipoConsulta(1);
		
		RemoteUtils tareasBDelegate = new RemoteUtils();
		
		//List<ExpedienteTCWPSWeb> lista = tareasBDelegate.obtenerInstanciasTareasPorUsuario(consulta);
		List<ExpedienteTCWPSWeb> lista = tareasBDelegate.obtenerListaTareasBandBusqueda(consulta);
		
	//	for(ExpedienteTCWPS expediente : lista){
		//	LOG.info("ESTADO DE EXPEDIENTE RESULTANTE DE CONSULTA ===> " + expediente.getEstado());
			/*if(expediente.getTaskID().equals(ID_NO_EXISTE_TAREA)){
				expediente.setEstado("En proceso");
			}*/
	//	}
		
		/*Carga Lista de Territorio*/
		//territorios = Util.crearItems(territorioBean.buscarTodos(), true, "id", "descripcion");
		/*
		if(lista!=null && lista.size()>0)
			
			
			
			for(int i=0;i<lista.size();i++){			
			String codTerr = lista.get(i).getIdTerritorio();			
			Territorio terr = territorioBean.buscarPorId(Integer.parseInt(codTerr));
			String descTerr = terr.getDescripcion();			
			lista.get(i).setDescTerritorio(descTerr);
		}		
		
		*/
		
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		if(lista!=null && lista.size()>0){
			LOG.info("Tamaño lista ... "+lista.size());
		}else
			LOG.info("lista es nula o vacia");
		
		if (consulta.getCodigoExpediente()!=null){ // solo expediente
			if (lista!=null && lista.size()==0){
				Historial hist = historialBean.buscarMasRecienPorId(Long.valueOf(codigoExpediente));
				if (hist != null){
					if (hist.getEstado().getId() == Constantes.ESTADO_CERRADO_TAREA_27.longValue()
							|| hist.getEstado().getId() == Constantes.ESTADO_CERRADO_TAREA_11.longValue()) {
						mostrarLinkExp=true;
						Mensajes mensajesSoloExpediente = new Mensajes();
						mensajesSoloExpediente=mensajeBean.buscarPorId(Long.valueOf(Constantes.ID_MENSAJES_BUSQUEDA_POR_EXPEDIENTE));
						if (mensajesSoloExpediente!=null && mensajesSoloExpediente.getContenido()!=null){
							try {
								textoMensajeSoloExpediente = new String(mensajesSoloExpediente.getContenido());
							} catch(Exception e) {
								LOG.error(e.getMessage(), e);
								textoMensajeSoloExpediente = null;
							}
						}
					} else {
						mostrarLinkExp=false;
						LOG.warn("Expediente no existe en Process pero no está Cerrado en BD.");
						ExpedienteTCWPSWeb objTCWPS = new ExpedienteTCWPSWeb();
						objTCWPS.setTaskID("-1");
						objTCWPS.setIdTarea("");
						objTCWPS.setCodigo(String.valueOf(hist.getExpediente().getId()));
						objTCWPS.setEstado(hist.getEstado().getDescripcion());
						objTCWPS.setAccion(hist.getAccion());
						objTCWPS.setVerificacionDomiciliaria(hist.getVerifDom());
						objTCWPS.setSegmento(hist.getClienteNatural().getSegmento().getDescripcion());
						objTCWPS.setIdTipoOferta(String.valueOf(hist.getTipoOferta().getId()));
						objTCWPS.setTipoOferta(hist.getTipoOferta().getDescripcion());
						objTCWPS.setCliente(new ClienteWeb());
						objTCWPS.getCliente().setTipoDOI(hist.getClienteNatural().getTipoDoi().getDescripcion());
						objTCWPS.getCliente().setNumeroDOI(hist.getClienteNatural().getNumDoi());
						objTCWPS.getCliente().setApPaterno(hist.getClienteNatural().getApePat());
						objTCWPS.getCliente().setApMaterno(hist.getClienteNatural().getApeMat());
						objTCWPS.getCliente().setNombre(hist.getClienteNatural().getNombre());
						objTCWPS.setProducto(new ProductoWeb());
						objTCWPS.getProducto().setIdProducto(String.valueOf(hist.getProducto().getId()));
						objTCWPS.getProducto().setProducto(hist.getProducto().getDescripcion());
						objTCWPS.getProducto().setSubProducto(hist.getSubproducto().getDescripcion());
				    	objTCWPS.setMoneda(hist.getTipoMonedaSol().getDescripcion());
						objTCWPS.setLineaCredito(hist.getLineaCredSol());
						objTCWPS.setMontoAprobado(hist.getLineaCredAprob());
						objTCWPS.setDesOficina(hist.getOficina().getDescripcion());
						objTCWPS.setDesTerritorio(hist.getOficina().getTerritorio().getDescripcion());
						objTCWPS.setCodigoRVGL(hist.getRvgl());
						objTCWPS.setNumeroContrato(hist.getNroContrato());
						objTCWPS.setObservacion(hist.getComentario());
						objTCWPS.setIdOficina(String.valueOf(hist.getOficina().getId()));
						objTCWPS.setFlagRetraer(hist.getFlagRetraer());
						objTCWPS.setIdGrupoSegmento(String.valueOf(hist.getClienteNatural().getSegmento().getGrupoSegmento().getId()));
						if (hist.getExpediente().getExpedienteTC().getNroDevoluciones() != null) {
							objTCWPS.setNumeroDevoluciones(hist.getExpediente().getExpedienteTC().getNroDevoluciones().intValue());
						} else {
							objTCWPS.setNumeroDevoluciones(0);
						}
						lista = new ArrayList<ExpedienteTCWPSWeb>();
						lista.add(objTCWPS);
						
						addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
					}
				}
			}
		}
		
		// busqueda otros criterios.
		if (consulta.getCodigoExpediente()==null && (lista==null || lista.size()==0)){
			Mensajes mensajesOtrosCriterios = new Mensajes();
			mensajesOtrosCriterios=mensajeBean.buscarPorId(Long.valueOf(Constantes.ID_MENSAJES_BUSQUEDA_OTROS_CRITERIOS));
			if (mensajesOtrosCriterios!=null && mensajesOtrosCriterios.getContenido()!=null){
				try{
					//textoMensajeOtrosCriterios = new String(mensajesOtrosCriterios.getContenido(), "UTF-8");
					textoMensajeOtrosCriterios = new String(mensajesOtrosCriterios.getContenido());
				}catch(Exception e){
					LOG.error(e.getMessage(), e);
					textoMensajeOtrosCriterios = null;
				}
			}
		}
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		TablaBandejaPendMB tablaBandejaPend = (TablaBandejaPendMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaPend");
		tablaBandejaPend.actualizarLista();
		tablaBandejaPend.ordenarPorDefecto();
	}

	public boolean valida() {
		String idMsgError = "frmBandejaBusqueda";
		//Verificar que ingrese al menos un valor de busqueda
		if(codigoExpediente.equals(Constantes.VALOR_VACIO) && 
				tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&				
				numeroDOI.equals(Constantes.VALOR_VACIO)&&
				apPaterno.equals(Constantes.VALOR_VACIO)&&
				apMaterno.equals(Constantes.VALOR_VACIO)&&
				nombres.equals(Constantes.VALOR_VACIO)&&
				fechaInicio == null &&
				fechaFin == null &&				
				productoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				subProductoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				segmentoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				tipoOfertaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				territorioSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				oficinaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				estadoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				numContrato.equals(Constantes.VALOR_VACIO)&&
				codigoRVGL.equals(Constantes.VALOR_VACIO)&&
				codigoPreEvaluador.equals(Constantes.VALOR_VACIO)){
			
				addMessageError(idMsgError + ":datoBusqueda", "com.ibm.bbva.common.buscarBandejaHist.msgIngreseDatoBusqueda");
				return false;
		}

		// Se verifican valores ingresados
		if (tipoDOISeleccionado!=null && !tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) &&
		   (numeroDOI==null || numeroDOI.trim().equals(""))) {		
			addMessageError(idMsgError + ":idNumDoi", 
			"com.ibm.bbva.common.buscarBandeja.msg.numero");
			return false;
		}
		if (numeroDOI!=null && !numeroDOI.trim().equals("") &&
			(tipoDOISeleccionado==null || tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))	) {
			addMessageError(idMsgError + ":idTipoDoi", 
			"com.ibm.bbva.common.buscarBandeja.msg.seleccion.tipoDOI");
			return false;
		}
		// Cuando los valores de los campos son ingresados se valida la consistencia entre ambos
		if (tipoDOISeleccionado!=null && !tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) &&
			numeroDOI!=null && !numeroDOI.trim().equals("") ) {

			TipoDoi tipoDOI = tipoDoiBean.buscarPorId(Long.parseLong(tipoDOISeleccionado));
			String codTipoDoi = tipoDOI.getCodigo();
			
			if (codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI) && numeroDOI.length()!=CARACTERES_DNI) {
				addMessageError(idMsgError + ":idNumDoi",
						"com.ibm.bbva.common.buscarBandeja.msg.caracteres", "DNI", CARACTERES_DNI);
				return false;
			} else if (codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC) && numeroDOI.length()!=CARACTERES_RUC) {
				addMessageError(idMsgError + ":idNumDoi",
						"com.ibm.bbva.common.buscarBandeja.msg.caracteres", "RUC", CARACTERES_RUC);
				return false;
			} else if (!codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI) &&
					!codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC) &&
					numeroDOI.length() > MAX_CARACTERES_OTROS){
				addMessageError(idMsgError + ":idNumDoi",
						"com.ibm.bbva.common.buscarBandeja.msg.maxCaracteres", MAX_CARACTERES_OTROS);
				return false;
			}
		}
		return true;
	}
	
	public void limpiar () {		
		this.codigoExpediente = "";
		this.numeroDOI = "";
		this.apPaterno = "";
		this.apMaterno = "";
		this.nombres = "";
		this.codigoRVGL = "";
		this.codigoPreEvaluador = "";
		this.numContrato = "";
		this.fechaInicio = null;
		this.fechaFin = null;
		
		this.tipoDOISeleccionado = "";
		this.productoSeleccionado = "";
		this.subProductoSeleccionado = "";
		this.segmentoSeleccionado = "";
		this.tipoOfertaSeleccionado = "";
		this.territorioSeleccionado = "";
		this.oficinaSeleccionado = "";
		this.estadoSeleccionado = "";
	}
	
	/*INICIO INCIDENCIA 295 17.12.2014*/
	public List<HistorialDetalle> obtenerDetalle(List<Historial> listaHistorial){
		List<HistorialDetalle> listaDet = new ArrayList<HistorialDetalle>();
		HistorialDetalle historialDetalle = new HistorialDetalle();
		for (Historial hist: listaHistorial) {
			historialDetalle = new HistorialDetalle();
			historialDetalle.setId(hist.getId());
			historialDetalle.setIdExpediente(hist.getExpediente().getId());
			historialDetalle.setEstado(hist.getEstado().getDescripcion()); 
			historialDetalle.setTipoOferta(hist.getTipoOferta()==null?"":hist.getTipoOferta().getDescripcion());
			LOG.info("hist.getTipoMonedaSol().getDescripcion()  "+hist.getTipoMonedaSol().getDescripcion());

			historialDetalle.setTipoMonedaSolicitada(hist.getTipoMonedaSol().getDescripcion());
			historialDetalle.setTipoMonedaAprobada(hist.getTipoMonedaAprob()==null?"":hist.getTipoMonedaAprob().getDescripcion());
			historialDetalle.setNumeroDOI(hist.getClienteNatural().getNumDoi());
			historialDetalle.setNombre(hist.getClienteNatural().getNombre());
			historialDetalle.setApPaterno(hist.getClienteNatural().getApePat());
			historialDetalle.setApMaterno(hist.getClienteNatural().getApeMat());
			historialDetalle.setTipoDOI(hist.getClienteNatural().getTipoDoi().getDescripcion());
			historialDetalle.setSegmento(hist.getClienteNatural().getSegmento().getDescripcion());
			historialDetalle.setProducto(hist.getProducto()==null?"":hist.getProducto().getDescripcion());
			historialDetalle.setSubProducto(hist.getSubproducto()==null?"":hist.getSubproducto().getDescripcion());
			historialDetalle.setLineaCreditoSolicitado(hist.getLineaCredSol());
			historialDetalle.setLineaCreditoAprobado(hist.getLineaCredAprob());
			historialDetalle.setCodigoRGVL(hist.getRvgl());
			historialDetalle.setOficina(hist.getOficina()==null?"":hist.getOficina().getDescripcion());
			historialDetalle.setTerritorio(hist.getEmpleadoResp()==null?"":hist.getEmpleadoResp().getOficina().getTerritorio().getDescripcion());
			historialDetalle.setNumeroContrato(hist.getNroContrato());
			if(hist.getComentario()==null || !hist.getComentario().trim().equals("")){
				historialDetalle.setObservacion(Constantes.OBSERVACION_NO_REGISTRADA);
			}else{
				historialDetalle.setObservacion(Constantes.OBSERVACION_REGISTRADA);
			}
			SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss a"); 
			historialDetalle.setFechaRegistro(sdf.format(hist.getFecRegistro()));
			historialDetalle.setCorreo(hist.getClienteNatural()==null?"":hist.getClienteNatural().getCorreo());		 
			historialDetalle.setCelular(hist.getClienteNatural()==null?"":hist.getClienteNatural().getCelular());
		    listaDet.add(historialDetalle);
		}
		return listaDet;
	}

	public String seleccionaFila() {
		String codExpediente = getRequestParameter("codExpediente");
		String idHistorial="";
		long idHist=0L;
		Historial hist = new Historial();
		hist=historialBean.buscarMasRecienPorId(Long.valueOf(codExpediente));
		idHist=hist.getId();
		idHistorial=Long.toString(idHist);
		Expediente expediente = obtenerExpediente(idHistorial);
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
		addObjectSession(Constantes.NOMBRE_BANDEJA_SESION, getNombreJSPPrincipal());
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		return "/visualizarExpediente/formVisualizarExpediente?faces-redirect=true";
	}
	
	private Expediente obtenerExpediente(String historial) {
		LOG.info("Metodo obtenerExpediente, historial = "+historial);
		long idHistorial = Long.parseLong(historial);
//		return ConvertExpediente.convertToExpedienteVO(historialBean.buscarPorId(idHistorial));
		Historial historialBD = historialBean.buscarPorId(idHistorial);
		Expediente expedienteBD = expedienteBean.buscarPorId(historialBD.getExpediente().getId());
		Expediente expedienteHistorico =  ConvertExpediente.convertToExpedienteVO(historialBD, expedienteBD);
		
		/**
		 * Obtiene URL Documento CM - 21 Julio 2014 (Maydeline chanchari)
		 * */
//		LOG.info("Consulta CM");
//		objDocumentoExpTc= new DocumentoExpTc();
//		objDocumentoExpTc.setExpediente(new Expediente());
//		objDocumentoExpTc.getExpediente().setClienteNatural(new ClienteNatural());
//		objDocumentoExpTc.getExpediente().getClienteNatural().setNumDoi(historialBD.getClienteNatural().getNumDoi()); // solo debe traer los documentos del cliente
//	
//		try{
//			listaDocumentosCM = facade.obtenerListaDocumentoCM(objDocumentoExpTc);
//		}catch(Exception e){
//			LOG.error(e.getMessage(), e);
//		}
//		Map<String, Object> mapListDocumentosCM =new TreeMap<String, Object> ();
//		if(listaDocumentosCM!=null)
//			for(Documento objDocumento: listaDocumentosCM){
//				mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
//			}
//		else
//			LOG.info("listaDocumentosCM es nulo");
		addObjectSession(Constantes.EXPEDIENTE_SESION_HISTORICO, expedienteHistorico );
//		addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM, mapListDocumentosCM);
		return expedienteHistorico;
	}
	
	/*FIN INCIDENCIA 295 17.12.2014*/
	
	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public String getApPaterno() {
		return apPaterno;
	}

	public void setApPaterno(String apPaterno) {
		this.apPaterno = apPaterno;
	}

	public String getApMaterno() {
		return apMaterno;
	}

	public void setApMaterno(String apMaterno) {
		this.apMaterno = apMaterno;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getCodigoRVGL() {
		return codigoRVGL;
	}

	public void setCodigoRVGL(String codigoRVGL) {
		this.codigoRVGL = codigoRVGL;
	}

	public String getCodigoPreEvaluador() {
		return codigoPreEvaluador;
	}

	public void setCodigoPreEvaluador(String codigoPreEvaluador) {
		this.codigoPreEvaluador = codigoPreEvaluador;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public List<SelectItem> getTiposDOI() {
		return tiposDOI;
	}

	public void setTiposDOI(List<SelectItem> tiposDOI) {
		this.tiposDOI = tiposDOI;
	}

	public String getTipoDOISeleccionado() {
		return tipoDOISeleccionado;
	}

	public void setTipoDOISeleccionado(String tipoDOISeleccionado) {
		this.tipoDOISeleccionado = tipoDOISeleccionado;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getSegmentos() {
		return segmentos;
	}

	public void setSegmentos(List<SelectItem> segmentos) {
		this.segmentos = segmentos;
	}

	public List<SelectItem> getTipoOfertas() {
		return tipoOfertas;
	}

	public void setTipoOfertas(List<SelectItem> tipoOfertas) {
		this.tipoOfertas = tipoOfertas;
	}

	public String getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(String productoSeleccionado) {
		LOG.info("SETANDO VALOOR DE PRODUCTO");
		this.productoSeleccionado = productoSeleccionado;
	}

	public String getSegmentoSeleccionado() {
		return segmentoSeleccionado;
	}

	public void setSegmentoSeleccionado(String segmentoSeleccionado) {
		this.segmentoSeleccionado = segmentoSeleccionado;
	}

	public String getTipoOfertaSeleccionado() {
		return tipoOfertaSeleccionado;
	}

	public void setTipoOfertaSeleccionado(String tipoOfertaSeleccionado) {
		this.tipoOfertaSeleccionado = tipoOfertaSeleccionado;
	}

	public List<SelectItem> getSubProductos() {
		return subProductos;
	}

	public void setSubProductos(List<SelectItem> subProductos) {
		this.subProductos = subProductos;
	}

	public String getSubProductoSeleccionado() {
		return subProductoSeleccionado;
	}

	public void setSubProductoSeleccionado(String subProductoSeleccionado) {
		this.subProductoSeleccionado = subProductoSeleccionado;
	}

	public List<SelectItem> getTerritorios() {
		return territorios;
	}

	public void setTerritorios(List<SelectItem> territorios) {
		this.territorios = territorios;
	}

	public List<SelectItem> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<SelectItem> oficinas) {
		this.oficinas = oficinas;
	}

	public List<SelectItem> getEstados() {
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public String getTerritorioSeleccionado() {
		return territorioSeleccionado;
	}

	public void setTerritorioSeleccionado(String territorioSeleccionado) {
		this.territorioSeleccionado = territorioSeleccionado;
	}

	public String getOficinaSeleccionado() {
		return oficinaSeleccionado;
	}

	public void setOficinaSeleccionado(String oficinaSeleccionado) {
		this.oficinaSeleccionado = oficinaSeleccionado;
	}

	public String getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

	public String getNumContrato() {
		return numContrato;
	}

	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}

	public String getTextoMensajeSinFiltro() {
		return textoMensajeSinFiltro;
	}

	public void setTextoMensajeSinFiltro(String textoMensajeSinFiltro) {
		this.textoMensajeSinFiltro = textoMensajeSinFiltro;
	}

	public TipoDoiBeanLocal getTipoDoiBean() {
		return tipoDoiBean;
	}

	public void setTipoDoiBean(TipoDoiBeanLocal tipoDoiBean) {
		this.tipoDoiBean = tipoDoiBean;
	}

	public String getTextoMensajeSoloExpediente() {
		return textoMensajeSoloExpediente;
	}

	public void setTextoMensajeSoloExpediente(String textoMensajeSoloExpediente) {
		this.textoMensajeSoloExpediente = textoMensajeSoloExpediente;
	}

	public String getTextoMensajeOtrosCriterios() {
		return textoMensajeOtrosCriterios;
	}

	public void setTextoMensajeOtrosCriterios(String textoMensajeOtrosCriterios) {
		this.textoMensajeOtrosCriterios = textoMensajeOtrosCriterios;
	}

	public List<Historial> getLstHistorial() {
		return lstHistorial;
	}

	public void setLstHistorial(List<Historial> lstHistorial) {
		this.lstHistorial = lstHistorial;
	}

	public List<HistorialDetalle> getListaDet() {
		return listaDet;
	}

	public void setListaDet(List<HistorialDetalle> listaDet) {
		this.listaDet = listaDet;
	}

	public boolean isMostrarLinkExp() {
		return mostrarLinkExp;
	}

	public void setMostrarLinkExp(boolean mostrarLinkExp) {
		this.mostrarLinkExp = mostrarLinkExp;
	}

}