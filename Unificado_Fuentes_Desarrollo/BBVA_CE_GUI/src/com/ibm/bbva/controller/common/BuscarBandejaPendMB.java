package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bpd.RemoteUtils;


import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.Log;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.SegmentoBeanLocal;
import com.ibm.bbva.session.SubproductoBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarBandejaPend")
@RequestScoped
public class BuscarBandejaPendMB extends AbstractMBean {
	
	@EJB
	private TipoDoiBeanLocal tipoDoiBean;
	@EJB
	private ProductoBeanLocal productoBean;	
	@EJB
	private SubproductoBeanLocal subproductoBean;
	@EJB
	private TipoOfertaBeanLocal tipoOfertaBean;
	@EJB
	private SegmentoBeanLocal segmentoBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	@EJB
	private EstadoBeanLocal estadoBean;	
	@EJB
	private EstadoTareaCEBeanLocal estadoTareaCEBean;	
	@EJB
	private MensajesBeanLocal mensajeBean;
	
	private List<SelectItem> tiposDOI;
	private List<SelectItem> productos;
	private List<SelectItem> subProductos;
	private List<SelectItem> segmentos;
	private List<SelectItem> tipoOfertas;
	private List<SelectItem> estados;

	
	private String tipoDOISeleccionado;
	private String productoSeleccionado;
	private String subProductoSeleccionado;
	private String segmentoSeleccionado;
	private String tipoOfertaSeleccionado;
	private String estadoSeleccionado;	
	
	private String textoMensajeSinFiltro;
	
	private String codigoExpediente;
	private String apPaterno;
	private String apMaterno;
	private String nombres;
	private String codigoRVGL;
	private String numeroDOI;
	private String pendientes;
	
	private static final int CARACTERES_DNI = 8;
	private static final int CARACTERES_RUC = 11;
	private static final int MAX_CARACTERES_OTROS = 10;	
	
	private boolean mostrarNroPendientes=false;
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarBandejaPendMB.class);

	
	public BuscarBandejaPendMB() {

	}
	
	@PostConstruct
	public void init() {
		
		removeObjectSession("listaBD");
		
		subProductos = Util.listaVacia();
		
		/*Carga Lista de Tipo Documento*/
		//tiposDOI = Util.crearItems(tipoDoiBean.buscarTodos(), true, "id", "descripcion");
		tiposDOI= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_TIPDOI);
		if(tiposDOI==null || tiposDOI.size()<=0){
			tiposDOI = Util.crearItems(tipoDoiBean.buscarTodos(), true, "id", "descripcion");
			addObjectSession(Constantes.BAND_LIST_TIPDOI, tiposDOI);
		}
		
		/*Carga Lista de Tipo Producto*/
		//productos = Util.crearItems(productoBean.buscarTodos(), true, "id", "descripcion");
		productos= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_PROD);
		if(productos==null || productos.size()<=0){
			productos = Util.crearItems(productoBean.buscarTodos(), true, "id", "descripcion");
			addObjectSession(Constantes.BAND_LIST_PROD, productos);
		}
		
		/*Carga Lista de Tipo segmento*/
		segmentos= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_SEG);
		if(segmentos==null || segmentos.size()<=0){
			List<Segmento> ls = segmentoBean.buscarTodos();
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
		
	/*	List<Segmento> ls = segmentoBean.buscarTodos();
		List<Segmento> segmentoLista = new ArrayList<Segmento>();
		String des="", desAux="";
		for (Segmento s : ls) {	
			des=s.getDescripcion();
			if (!desAux.equals(des)){
				desAux=s.getDescripcion();
				segmentoLista.add(s);
			}				
		}
		
		segmentos = Util.crearItems(segmentoLista, true, "id", "descripcion");*/
		
		/*Carga Lista de Tipo Oferta*/
		//tipoOfertas = Util.crearItems(tipoOfertaBean.buscarTodos(), true, "id", "descripcion");
		tipoOfertas= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_TIPOFERTA);
		if(tipoOfertas==null || tipoOfertas.size()<=0){
			tipoOfertas = Util.crearItems(tipoOfertaBean.buscarTodos(), true, "id", "descripcion");
			addObjectSession(Constantes.BAND_LIST_TIPOFERTA, tipoOfertas);
		}
		
		/*Carga Lista de EstadoCE*/
		//estados = Util.crearItems(estadoCEBean.buscarTodos(), true, "id", "descripcion");	
		//estados = Util.eliminarCamposRepetidos(Util.crearItems(estadoCEBean.buscarTodosMenosEnDesuso(), true, "id", "descripcion"));
		estados= (List<SelectItem>) getObjectSession(Constantes.BAND_LIST_EST);
		if(estados==null || estados.size()<=0){
			estados = Util.eliminarCamposRepetidos(Util.crearItems(estadoCEBean.buscarTodosMenosEnDesuso(), true, "id", "descripcion"));
			addObjectSession(Constantes.BAND_LIST_EST, estados);
		}
		/*
		  List<EstadoCE> listaEstadoMaestro = new ArrayList<EstadoCE>();
			listaEstadoMaestro= estadoCEBean.buscarTodos();
			LOG.info("Tamaño listaEstadoMaestro::::"+listaEstadoMaestro.size());
			for(EstadoCE es: listaEstadoMaestro){
				LOG.info("lISTA: ID_ESTADOMaestro: "+es.getId()+" descripcion_EstadoMaestro: "+es.getDescripcion());
			}*/
		String nombJSP = getNombreJSPPrincipal();         
		if (nombJSP.equals("formBandejaPendientes")) {
			mostrarNroPendientes=true;
			
			FacesContext ctx = FacesContext.getCurrentInstance();
			TablaBandejaPendMB tablaBandejaPend = (TablaBandejaPendMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaPend");
			List<ExpedienteTCWrapper> listTempWrapper= (List<ExpedienteTCWrapper> ) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
			String initPendiente= (String) getObjectSession(Constantes.INICIO_BAND_PEND);
			
			if(listTempWrapper==null && initPendiente!=null && initPendiente.equals("1")){
				tablaBandejaPend.actualizarLista();
				
				removeObjectSession(Constantes.INICIO_BAND_PEND);
			}else{
				//LOG.info("Lista listTempWrapper:::"+listTempWrapper.size());
			}
			
			if(tablaBandejaPend.getListTabla()!=null && tablaBandejaPend.getListTabla().size()>=0){
				this.setPendientes(String.valueOf(tablaBandejaPend.getListTabla().size()));
			}else
			{
				LOG.info("Lista tabla null");
			}
				
				
			
			
 		}
	}	

	public void cambiarSubProductos(ValueChangeEvent vce) {
		Object codigo = vce.getNewValue();
		productoSeleccionado = (String)codigo;
		if (productoSeleccionado==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(productoSeleccionado)) {
			subProductos = Util.listaVacia();
		} else {
			crearListaSubProductos (Long.parseLong(productoSeleccionado));
		}
		LOG.info("Eliminando LISTAS EXPEDINETES SESION");
		//addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, null);
		//addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, null);	
		
	}

	private void crearListaSubProductos(long idProducto) {
		List<Subproducto> lista = subproductoBean.buscarPorIdProducto(idProducto);
		subProductos = Util.crearItems(lista, true, "id", "descripcion");
	}
		
	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
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

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public void buscar(){
		Consulta consulta = new Consulta();
		consulta.setCodigoExpediente(Util.validarCampo (codigoExpediente));
		consulta.setApPaterno(Util.validarCampo (apPaterno));
		consulta.setApMaterno(Util.validarCampo (apMaterno));
		consulta.setNombres(Util.validarCampo (nombres));
		consulta.setCodRVGL(Util.validarCampo (codigoRVGL));
		consulta.setNumeroDOI(Util.validarCampo (numeroDOI));
		consulta.setTipoDOI(Util.obtenerDescripcion(tiposDOI, tipoDOISeleccionado));
		consulta.setIdProducto(Util.validarId(productoSeleccionado));
		consulta.setSubProducto(Util.obtenerDescripcion(subProductos, subProductoSeleccionado));
		consulta.setSegmento(Util.obtenerDescripcion(segmentos, segmentoSeleccionado));
		//consulta.setTipoOferta(Util.obtenerDescripcion(tipoOfertas, tipoOfertaSeleccionado));
		if(tipoOfertaSeleccionado!=null && !tipoOfertaSeleccionado.equals("-1") && !tipoOfertaSeleccionado.equals(""))
			consulta.setTipoOferta(tipoOfertaSeleccionado);
		
		LOG.info("consulta.getTipoOferta()::::"+consulta.getTipoOferta());
		consulta.setEstado(Util.obtenerDescripcion(estados, estadoSeleccionado));
		LOG.info("ESTADO::: "+consulta.getEstado());
		/**
		 * Se agrego 13 marzo 2014
		 * */
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo()); 
		consulta.setUsuarios(listUsuarios);
		consulta.setConsiderarUsuarios(true);		
		/**
		 * fin de cambios
		 * */
		RemoteUtils tareasBDelegate = new RemoteUtils();
		//List<ExpedienteTCWPS> lista = tareasBDelegate.obtenerInstanciasTareasPorUsuario(consulta);
		List<ExpedienteTCWPSWeb> lista = tareasBDelegate.obtenerListaTareasBandPendiente(consulta);
				
		this.setPendientes(String.valueOf(lista.size()));		
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);	
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, null);
		FacesContext ctx = FacesContext.getCurrentInstance();
		TablaBandejaPendMB tablaBandejaPend = (TablaBandejaPendMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaPend");
		tablaBandejaPend.actualizarLista();
		tablaBandejaPend.ordenarPorDefecto();
	}
	
	public boolean valida() {
		String idMsgError = "frmBandejaPendientes";
		
		if(codigoExpediente.equals(Constantes.VALOR_VACIO) && 
				tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&				
				numeroDOI.equals(Constantes.VALOR_VACIO)&&
				apPaterno.equals(Constantes.VALOR_VACIO)&&
				apMaterno.equals(Constantes.VALOR_VACIO)&&
				nombres.equals(Constantes.VALOR_VACIO)&&
				productoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				subProductoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				segmentoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				tipoOfertaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) &&
				estadoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) &&
				codigoRVGL.equals(Constantes.VALOR_VACIO)){
			Mensajes mensajeSinFiltro = new Mensajes();
			mensajeSinFiltro=mensajeBean.buscarPorId(Long.valueOf(Constantes.ID_MENSAJES_SIN_FILTRO));
			if (mensajeSinFiltro!=null && mensajeSinFiltro.getContenido()!=null){
				try{
					//textoMensajeSinFiltro = new String(mensajeSinFiltro.getContenido(), "UTF-8");
					textoMensajeSinFiltro = new String(mensajeSinFiltro.getContenido());
					return false;
				}catch(Exception e){
					LOG.error(e.getMessage(), e);
					textoMensajeSinFiltro = null;
				}
			}
			
//				addMessageError(idMsgError + ":datoBusquedaHist", "com.ibm.bbva.common.buscarBandejaHist.msgIngreseDatoBusqueda");
//				return false;
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
	
	public String limpiar() {
		this.codigoExpediente = "";
		this.numeroDOI = "";
		this.apPaterno = "";
		this.apMaterno = "";
		this.nombres = "";
		this.codigoRVGL = "";
		
		this.tipoDOISeleccionado = "";
		this.productoSeleccionado = "";
		this.subProductoSeleccionado = "";   
		this.segmentoSeleccionado = "";
		this.tipoOfertaSeleccionado = "";
		return null;
	}

	public String getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

	public List<SelectItem> getEstados() {
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public String getPendientes() {
		return pendientes;
	}

	public void setPendientes(String pendientes) {
		this.pendientes = pendientes;
	}
	
	public boolean isMostrarNroPendientes() {
		return mostrarNroPendientes;
	}

	public void setMostrarNroPendientes(boolean mostrarNroPendientes) {
		this.mostrarNroPendientes = mostrarNroPendientes;
	}

	public String getTextoMensajeSinFiltro() {
		return textoMensajeSinFiltro;
	}

	public void setTextoMensajeSinFiltro(String textoMensajeSinFiltro) {
		this.textoMensajeSinFiltro = textoMensajeSinFiltro;
	}
	
	
	

}