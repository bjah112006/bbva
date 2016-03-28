package com.ibm.bbva.controller.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.GuiaDocumentariaBeanLocal;
import com.ibm.bbva.session.PersonaBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.SubproductoBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.util.Util;

@ManagedBean (name="actualizaGuiaDocumentaria")
@SessionScoped
public class actualizaGuiaDocumentariaUI extends AbstractMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(actualizarCorreoUI.class);
	@EJB
	private TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private SubproductoBeanLocal subproductoBean;
	@EJB
	private TipoOfertaBeanLocal tipoOfertaBean;
	@EJB
	private CategoriaRentaBeanLocal categoriaRentaBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private PersonaBeanLocal personaBean;
	@EJB 
	private GuiaDocumentariaBeanLocal guiaDocumentariaBean;
	
	private GuiaDocumentaria datosGuiaDocumentaria;
	private List<SelectItem> documentos;
	private String tipoDocumentoSeleccionado;
	private List<SelectItem> productos;
	private List<SelectItem> subProductos;
	private String productoSeleccionado;
	private String subProductoSeleccionado;
	private List<SelectItem> tipoOferta;
	private String tipoOfertaSeleccionado;
	private List<SelectItem> categoriaRenta;
	private String categoriaRentaSeleccionado;
	private List<SelectItem> persona;
	private String personaSeleccionado;
	private List<SelectItem> tarea;
	private String tareaSeleccionado;
	private boolean flagObligatorio;
	//private boolean flagClienteNuevo;
	private String flagClienteNuevo;
	//private boolean flagPagoHabiente;
	private String flagPagoHabiente;
	//private boolean flagTasaEspecial;
	private String flagTasaEspecial;
	//private boolean flagVeriDomicil;
	private String flagVeriDomicil;
	//private boolean flagVeriLaboral;
	private String flagVeriLaboral;
	//private boolean flagDPS;
	private String flagDPS;
	private boolean flagComiteRiesgo;
	private boolean flagActivo;
	private String descripcion;
	private Long idGuiaDocumentaria;
	
	private List<SelectItem> listaGenerica;
		

	/**
	 * Constructor 
	 */
	public actualizaGuiaDocumentariaUI(){
		//super();
		LOG.info("Entra al Constructor");
	}
	@PostConstruct
	public void init() {
		productos = Util.listaVacia();
		subProductos = Util.listaVaciaSubProducto();
		tipoOferta = Util.listaVacia();
		categoriaRenta = Util.listaVacia();
		persona = Util.listaVacia();
		tarea = Util.listaVacia();
		documentos = Util.listaVacia();
		listaGenerica = new ArrayList<SelectItem>();
		
		inicializarControles();
		
		/*Carga Lista de Tipo de Productos*/
		productos=Util.crearItems(productoBean.buscarTodos(),true,"id","descripcion");
				
		/*Carga Lista de Tipo de Sub Productos*/
		/*if(productoSeleccionado!=null && !productoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("codProducto para listaSubProducto="+productoSeleccionado);
			List<Subproducto> listTempSubProductos= subproductoBean.buscarPorIdProd(Util.validarIdInteger(productoSeleccionado));
			subProductos=new ArrayList<SelectItem>();
			
			subProductos.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));	
			if(subProductos.size()>0){
				for(Subproducto value : listTempSubProductos){
					subProductos.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
				}
			}
		}else{
			subProductos.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));			
		}*/
		
		
		/*Carga Lista de Tipo de Ofertas*/
		//tipoOferta=Util.crearItems(tipoOfertaBean.buscarTodos(),true,"id","descripcion");
		List<TipoOferta> listTempTipoOfertas= tipoOfertaBean.buscarTodos();
		tipoOferta=new ArrayList<SelectItem>();
		
		if(tipoOfertaSeleccionado!=null && !tipoOfertaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("codTipoOferta="+tipoOfertaSeleccionado);
		}else{
			tipoOferta.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));			
		}
		if(listTempTipoOfertas.size()>0){
			for(TipoOferta value : listTempTipoOfertas){
				tipoOferta.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
			}
		}
		
		/*Carga Lista de Categoria Rentas*/
		//categoriaRenta=Util.crearItems(categoriaRentaBean.buscarTodos(),true,"id","descripcion");
		List<CategoriaRenta> listTempCategoriaRenta= categoriaRentaBean.buscarTodos();
		categoriaRenta=new ArrayList<SelectItem>();
		
		if(categoriaRentaSeleccionado!=null && !categoriaRentaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("codTipoOferta="+categoriaRentaSeleccionado);
		}else{
			categoriaRenta.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));			
		}
		if(listTempCategoriaRenta.size()>0){
			for(CategoriaRenta value : listTempCategoriaRenta){
				categoriaRenta.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
			}
		}
		
		/*Carga Lista de Persona*/
		persona=Util.crearItems(personaBean.buscarTodos(),true,"id","descripcion");
		/*List<Persona> listTempPersona= personaBean.buscarTodos();
		persona=new ArrayList<SelectItem>();
		
		if(personaSeleccionado!=null && !personaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("codTipoOferta="+personaSeleccionado);
		}else{
			persona.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));			
		}
		if(listTempPersona.size()>0){
			for(Persona value : listTempPersona){
				persona.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
			}
		}*/
		
		/*Carga Lista de Tareas*/
				
		List<Tarea> tareas = tareaBean.buscarTodos();
		
		/*
		 * 16/12/14
		 * 
		 * Quitar la tarea 26 de la lista de tareas.
		 * 
		 * "La tarea 26 ya no participa del flujo y no debe ser referenciada en ninguna parte 
		 * del sistema (ejemplo filtros de búsqueda de tareas y reportes), actualmente aparece en varias
		 * opciones del sistema, ver evidencias. Verificar que no sea visible en todo el sistema."
		 *  
		 */
		
		List<Tarea> auxTareas = new ArrayList<Tarea>();
		
		for(Tarea tarea : tareas){
			if(tarea.getCodigo() != null){
				if(!tarea.getCodigo().equals("26")){
					auxTareas.add(tarea);
				}
			}
		}
		
		/* Carga Lista de Tareas */
		tarea = Util.crearItems(auxTareas, true, "id",
				"descripcion");
		
		/*Carga Lista de Tipo de Documentos*/
		documentos=Util.crearItems(tipoDocumentoBean.buscar(),true,"id","descripcion");
		
		//carga de la lista generica
		listaGenerica.add(new SelectItem("-1",Constantes.DEFECTO_COMBO_TODOS));
		listaGenerica.add(new SelectItem("1","SI"));
		listaGenerica.add(new SelectItem("0","NO"));
	}
	
	public void cambiarSubProductos(AjaxBehaviorEvent event) {
		LOG.info("cambiarSubProductos");
		FacesContext ctx = FacesContext.getCurrentInstance();
						
		String codProd = ctx.getExternalContext().getRequestParameterMap().get("codProd") !=null?
				ctx.getExternalContext().getRequestParameterMap().get("codProd"):"";
	
		//String codigo = String.valueOf(guiaDocumentaria.getProductoSeleccionado());
		
		/*Carga Lista de Tipo de Sub Productos*/
		//if(guiaDocumentaria.getProductoSeleccionado() !=null && !guiaDocumentaria.getProductoSeleccionado() .equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
		//	LOG.info("codProducto para listaSubProducto= "+guiaDocumentaria.getProductoSeleccionado() );
		if(!codProd.equals("") && !codProd .equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			LOG.info("codProducto para listaSubProducto= "+codProd );
			
			List<Subproducto> listTempSubProductos= subproductoBean.buscarPorIdProd(Util.validarIdInteger(codProd));
			subProductos=new ArrayList<SelectItem>();
			
			subProductos.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));	
			if(subProductos.size()>0){
				for(Subproducto value : listTempSubProductos){
					subProductos.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
				}
			}
			
			if (datosGuiaDocumentaria.getSubproducto()!=null){
				subProductoSeleccionado=String.valueOf(this.getSubProductoSeleccionado());
			}else{
				subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
		}else{
			subProductos.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));	
			subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		}
	
	}

	public String grabar(){
		Long idDatosGuiaDocumentaria;
		FacesContext ctx = FacesContext.getCurrentInstance();
		LOG.info("en el metodo grabar guia documentaria");
		String ruta="";
		try{
			if (esValido()){
				if (datosGuiaDocumentaria!=null){
					idDatosGuiaDocumentaria=datosGuiaDocumentaria.getId();
				}else{
					idDatosGuiaDocumentaria=0L;
				}
				if (idDatosGuiaDocumentaria==0){ 
					crear(); // CREAR
				}else{
					actualizar(idDatosGuiaDocumentaria);//ACTUALIZAR
				}
				ctx = FacesContext.getCurrentInstance();
				buscarGuiaDocumentaria buscarGuia = (buscarGuiaDocumentaria) 
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarGuia");
				//consultarTabla.buscar();
				buscarGuia.actualizarLista();
				removeObjectSession(Constantes.GUIA_DOCUMENTARIA_SESION);
				ruta= "/mantenimiento/formManGuiaDocumentaria?faces-redirect=true";
			}
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
			ruta= null;
		}
		return ruta;
	}
	
	public String cancelar(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		buscarGuiaDocumentaria buscarGuia = (buscarGuiaDocumentaria)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarGuia");
		buscarGuia.limpiar();
		removeObjectSession(Constantes.GUIA_DOCUMENTARIA_SESION);
		return "/mantenimiento/formManGuiaDocumentaria?faces-redirect=true";
	}
	
	public void actualizar(Long idCabecera){
		GuiaDocumentaria datosGuiaDocumentariaActualizar = new GuiaDocumentaria();
		try{	
			setearDatosGrabar();
			datosGuiaDocumentariaActualizar = this.datosGuiaDocumentaria;
			datosGuiaDocumentariaActualizar.setId(idCabecera);
			guiaDocumentariaBean.update(datosGuiaDocumentariaActualizar);
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
	}	
	
	public void crear(){
		GuiaDocumentaria datosGuiaDocumentariaCrear = new GuiaDocumentaria();
		try{
			setearDatosGrabar();
			datosGuiaDocumentariaCrear = this.datosGuiaDocumentaria;
			datosGuiaDocumentariaCrear.setId(0);
			guiaDocumentariaBean.create(datosGuiaDocumentariaCrear);
		} catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}	
	}
	
	public void setearDatosGrabar(){
		if (!tipoDocumentoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento=tipoDocumentoBean.buscarPorId(Long.valueOf(tipoDocumentoSeleccionado));
			datosGuiaDocumentaria.setTipoDocumento(tipoDocumento);
		}else{
			datosGuiaDocumentaria.setTipoDocumento(null);
		}
			
		if (flagObligatorio){
			datosGuiaDocumentaria.setObligatorio(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setObligatorio(Constantes.CHECK_NO_SELECCIONADO);
		}
			
		if (!productoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			Producto producto = new Producto();
			producto=productoBean.buscarPorId(Long.valueOf(productoSeleccionado));
			datosGuiaDocumentaria.setProducto(producto);
		}else{
			datosGuiaDocumentaria.setProducto(null);
		}
		
		if (!subProductoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			Subproducto subProducto = new Subproducto();
			subProducto=subproductoBean.buscarPorId(Long.valueOf(subProductoSeleccionado));
			datosGuiaDocumentaria.setSubproducto(subProducto);
		}else{
			datosGuiaDocumentaria.setSubproducto(null);
		}
		
		if (!tipoOfertaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			TipoOferta tipoOferta = new TipoOferta();
			tipoOferta.setId(Long.valueOf(tipoOfertaSeleccionado));
			datosGuiaDocumentaria.setTipoOferta(tipoOferta);
		}else{
			datosGuiaDocumentaria.setTipoOferta(null);
		}
			
		/*if (flagClienteNuevo){
			datosGuiaDocumentaria.setFlagCliente(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagCliente(Constantes.CHECK_NO_SELECCIONADO);
		}*/
		if (!flagClienteNuevo.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			datosGuiaDocumentaria.setFlagCliente(flagClienteNuevo);
		}else{
			datosGuiaDocumentaria.setFlagCliente(null);
		}
		
		/*if (flagPagoHabiente){
			datosGuiaDocumentaria.setFlagPagoHab(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagPagoHab(Constantes.CHECK_NO_SELECCIONADO);
		}*/
		if (!flagPagoHabiente.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			datosGuiaDocumentaria.setFlagPagoHab(flagPagoHabiente);
		}else{
			datosGuiaDocumentaria.setFlagPagoHab(null);
		}
		
		if (!categoriaRentaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			CategoriaRenta categoriaRenta = new CategoriaRenta();
			categoriaRenta= categoriaRentaBean.buscarPorId(Long.valueOf(categoriaRentaSeleccionado));
			datosGuiaDocumentaria.setCategoriaRenta(categoriaRenta);
		}else{
			datosGuiaDocumentaria.setCategoriaRenta(null);
		}
		
		if (!personaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			Persona persona = new Persona();
			persona=personaBean.buscarPorId(Long.valueOf(personaSeleccionado));
			datosGuiaDocumentaria.setPersona(persona);
		}else{
			datosGuiaDocumentaria.setPersona(null);
		}
		
		/*if (flagTasaEspecial){
			datosGuiaDocumentaria.setFlagTasaEsp(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagTasaEsp(Constantes.CHECK_NO_SELECCIONADO);
		}*/
		if (!flagTasaEspecial.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			datosGuiaDocumentaria.setFlagTasaEsp(flagTasaEspecial);
		}else{
			datosGuiaDocumentaria.setFlagTasaEsp(null);
		}
		
		if (!tareaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			Tarea tarea = new Tarea();
			tarea=tareaBean.buscarPorId(Long.valueOf(tareaSeleccionado));
			datosGuiaDocumentaria.setTarea(tarea);
		}else{
			datosGuiaDocumentaria.setTarea(null);
		}
		
		/*if (flagVeriDomicil){
			datosGuiaDocumentaria.setFlagVerifDom(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagVerifDom(Constantes.CHECK_NO_SELECCIONADO);
		}*/
		if (!flagVeriDomicil.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			datosGuiaDocumentaria.setFlagVerifDom(flagVeriDomicil);
		}else{
			datosGuiaDocumentaria.setFlagVerifDom(null);
		}
		
		/*if (flagVeriLaboral){
			datosGuiaDocumentaria.setFlagVerifLab(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagVerifLab(Constantes.CHECK_NO_SELECCIONADO);
		}*/
		if (!flagVeriLaboral.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			datosGuiaDocumentaria.setFlagVerifLab(flagVeriLaboral);
		}else{
			datosGuiaDocumentaria.setFlagVerifLab(null);
		}
		
		/*if (flagDPS){
			datosGuiaDocumentaria.setFlagDps(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagDps(Constantes.CHECK_NO_SELECCIONADO);
		}*/
		if (!flagDPS.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			datosGuiaDocumentaria.setFlagDps(flagDPS);
		}else{
			datosGuiaDocumentaria.setFlagDps(null);
		}
		
		if (flagComiteRiesgo){
			datosGuiaDocumentaria.setFlagComiteRiesgo(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagComiteRiesgo(Constantes.CHECK_NO_SELECCIONADO);
		}
		
		datosGuiaDocumentaria.setDescripcion(descripcion);
		
		datosGuiaDocumentaria.setFlagPep("0");
		
		if (flagActivo){
			datosGuiaDocumentaria.setFlagActivo(Constantes.CHECK_SELECCIONADO);
		}else{
			datosGuiaDocumentaria.setFlagActivo(Constantes.CHECK_NO_SELECCIONADO);
		}
	}
		
	public boolean esValido(){
		boolean existeError = false;
		String idComponente = "formActualizaGuiaDocumentaria";
		if (tipoDocumentoSeleccionado.equals("-1")){
			addMessageError(idComponente + ":cmbTipoDocumento",
					"com.ibm.bbva.common.GuiaDocumentaria.msg.TipoDocumento");
			existeError=true;
		}
		/*if (!flagObligatorio) {
			addMessageError(idComponente + ":idFlgObligatorio",
					"com.ibm.bbva.common.GuiaDocumentaria.msg.Obligatorio");
			existeError = true;
		}
		if (!flagActivo) {
			addMessageError(idComponente + ":idFlgActivo",
					"com.ibm.bbva.common.GuiaDocumentaria.msg.FlagActivo");
			existeError = true;
		}*/
		if(personaSeleccionado.equals("-1")){
			addMessageError(idComponente + ":cmbPersona",
					"com.ibm.bbva.common.GuiaDocumentaria.msg.Persona");
			existeError=true;
		}
		return !existeError;
	}

	public List<SelectItem> getDocumentos() {
		return documentos;
	}


	public void setDocumentos(List<SelectItem> documentos) {
		this.documentos = documentos;
	}

	public String getTipoDocumentoSeleccionado() {
		return tipoDocumentoSeleccionado;
	}

	public void obtenerDatos(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		datosGuiaDocumentaria=(GuiaDocumentaria)getObjectSession(Constantes.GUIA_DOCUMENTARIA_SESION);
		if (datosGuiaDocumentaria!=null){ // inicializa cuando ya existe.
			LOG.info("Entra al init de actualizar datos existentes");
			LOG.info("Id GuiaDocumentaria" + datosGuiaDocumentaria.getId());
			
			idGuiaDocumentaria=datosGuiaDocumentaria.getId();
			
			if (datosGuiaDocumentaria.getTipoDocumento()!=null){
				tipoDocumentoSeleccionado=String.valueOf(datosGuiaDocumentaria.getTipoDocumento().getId());
			}else{
				tipoDocumentoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			if (datosGuiaDocumentaria.getObligatorio().equals(Constantes.CHECK_SELECCIONADO)){
				flagObligatorio=true;
			}else{
				flagObligatorio=false;
			}
			
			if (datosGuiaDocumentaria.getProducto()!=null){
				productoSeleccionado=String.valueOf(datosGuiaDocumentaria.getProducto().getId());
				
				/*Carga Lista de Tipo de Sub Productos*/
				if(productoSeleccionado!=null && !productoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
					LOG.info("codProducto para listaSubProducto= "+productoSeleccionado);
					List<Subproducto> listTempSubProductos= subproductoBean.buscarPorIdProd(Util.validarIdInteger(productoSeleccionado));
					subProductos=new ArrayList<SelectItem>();
					
					subProductos.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));	
					if(subProductos.size()>0){
						for(Subproducto value : listTempSubProductos){
							subProductos.add(new SelectItem(value.getCodigo(),value.getDescripcion()));
						}
					}
					
					if (datosGuiaDocumentaria.getSubproducto()!=null){
						subProductoSeleccionado=String.valueOf(datosGuiaDocumentaria.getSubproducto().getId());
					}else{
						subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
					}
					
				}else{
					subProductos.add(new SelectItem(Constantes.CODIGO_CODIGO_CAMPO_VACIO,Constantes.DEFECTO_COMBO_TODOS));	
					subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
				}
				
				
				
			}else{
				productoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
				subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
				
			}
			
			/*if (datosGuiaDocumentaria.getSubproducto()!=null){
				subProductoSeleccionado=String.valueOf(datosGuiaDocumentaria.getSubproducto().getId());
			}else{
				subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}*/
			
			if (datosGuiaDocumentaria.getTipoOferta()!=null){
				tipoOfertaSeleccionado=String.valueOf(datosGuiaDocumentaria.getTipoOferta().getId());
			}else{
				tipoOfertaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			/*if (datosGuiaDocumentaria.getFlagCliente().equals(Constantes.CHECK_SELECCIONADO)){
				flagClienteNuevo=true;
			}else{
				flagClienteNuevo=false;
			}*/
			if (datosGuiaDocumentaria.getFlagCliente()!=null){
				flagClienteNuevo=datosGuiaDocumentaria.getFlagCliente();
			}else{
				flagClienteNuevo=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			/*if (datosGuiaDocumentaria.getFlagPagoHab().equals(Constantes.CHECK_SELECCIONADO)){
				flagPagoHabiente=true;
			}else{
				flagPagoHabiente=false;
			}*/
			if (datosGuiaDocumentaria.getFlagPagoHab()!=null){
				flagPagoHabiente=datosGuiaDocumentaria.getFlagPagoHab();
			}else{
				flagPagoHabiente=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			if (datosGuiaDocumentaria.getCategoriaRenta()!=null){
				categoriaRentaSeleccionado=String.valueOf(datosGuiaDocumentaria.getCategoriaRenta().getId());
			}else{
				categoriaRentaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			if (datosGuiaDocumentaria.getPersona()!=null){
				personaSeleccionado=String.valueOf(datosGuiaDocumentaria.getPersona().getId());
			}else{
				personaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			/*if (datosGuiaDocumentaria.getFlagTasaEsp().equals(Constantes.CHECK_SELECCIONADO)){
				flagTasaEspecial=true;
			}else{
				flagTasaEspecial=false;
			}*/
			if (datosGuiaDocumentaria.getFlagTasaEsp()!=null){
				flagTasaEspecial=datosGuiaDocumentaria.getFlagTasaEsp();
			}else{
				flagTasaEspecial=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			if (datosGuiaDocumentaria.getTarea()!=null){
				tareaSeleccionado=String.valueOf(datosGuiaDocumentaria.getTarea().getId());
			}else{
				tareaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			/*if (datosGuiaDocumentaria.getFlagVerifDom().equals(Constantes.CHECK_SELECCIONADO)){
				flagVeriDomicil=true;
			}else{
				flagVeriDomicil=false;
			}*/
			if (datosGuiaDocumentaria.getFlagVerifDom()!=null){
				flagVeriDomicil=datosGuiaDocumentaria.getFlagVerifDom();
			}else{
				flagVeriDomicil=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
				
			/*if (datosGuiaDocumentaria.getFlagVerifLab().equals(Constantes.CHECK_SELECCIONADO)){
				flagVeriLaboral=true;
			}else{
				flagVeriLaboral=false;
			}*/
			if (datosGuiaDocumentaria.getFlagVerifLab()!=null){
				flagVeriLaboral=datosGuiaDocumentaria.getFlagVerifLab();
			}else{
				flagVeriLaboral=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}

			/*if (datosGuiaDocumentaria.getFlagDps().equals(Constantes.CHECK_SELECCIONADO)){
				flagDPS=true;
			}else{
				flagDPS=false;
			}*/
			if (datosGuiaDocumentaria.getFlagDps()!=null){
				flagDPS=datosGuiaDocumentaria.getFlagDps();
			}else{
				flagDPS=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			}
			
			if (datosGuiaDocumentaria.getFlagComiteRiesgo().equals(Constantes.CHECK_SELECCIONADO)){
				flagComiteRiesgo=true;
			}else{
				flagComiteRiesgo=false;
			}
			
			if (datosGuiaDocumentaria.getDescripcion()!=null){
				descripcion=datosGuiaDocumentaria.getDescripcion();
			}else{
				descripcion="";
			}
			
			if (datosGuiaDocumentaria.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO)){
				flagActivo=true;
			}else{
				flagActivo=false;
			}
			
		}else{ // para un nuevo registro
			inicializarControles();
		}
	}
	
	public void inicializarControles(){
		LOG.info("inicializarControles::::");
		tipoDocumentoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		flagObligatorio=false;
		productoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		subProductoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tipoOfertaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//flagClienteNuevo=false;
		flagClienteNuevo=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//flagPagoHabiente=false;
		flagPagoHabiente=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		categoriaRentaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		personaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//flagTasaEspecial=false;
		flagTasaEspecial=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//flagVeriDomicil=false;
		flagVeriDomicil=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//flagVeriLaboral=false;
		flagVeriLaboral=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//flagDPS=false;
		flagDPS=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		flagComiteRiesgo=false;
		descripcion="";
		flagActivo=false;
		datosGuiaDocumentaria= new GuiaDocumentaria();
	}
	
	public void setTipoDocumentoSeleccionado(String tipoDocumentoSeleccionado) {
		this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public String getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(String productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}

	public List<SelectItem> getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(List<SelectItem> tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public String getTipoOfertaSeleccionado() {
		return tipoOfertaSeleccionado;
	}

	public void setTipoOfertaSeleccionado(String tipoOfertaSeleccionado) {
		this.tipoOfertaSeleccionado = tipoOfertaSeleccionado;
	}

	public List<SelectItem> getCategoriaRenta() {
		return categoriaRenta;
	}

	public void setCategoriaRenta(List<SelectItem> categoriaRenta) {
		this.categoriaRenta = categoriaRenta;
	}

	public String getCategoriaRentaSeleccionado() {
		return categoriaRentaSeleccionado;
	}

	public void setCategoriaRentaSeleccionado(String categoriaRentaSeleccionado) {
		this.categoriaRentaSeleccionado = categoriaRentaSeleccionado;
	}

	public List<SelectItem> getPersona() {
		return persona;
	}

	public void setPersona(List<SelectItem> persona) {
		this.persona = persona;
	}

	public String getPersonaSeleccionado() {
		return personaSeleccionado;
	}

	public void setPersonaSeleccionado(String personaSeleccionado) {
		this.personaSeleccionado = personaSeleccionado;
	}

	public List<SelectItem> getTarea() {
		return tarea;
	}

	public void setTarea(List<SelectItem> tarea) {
		this.tarea = tarea;
	}

	public String getTareaSeleccionado() {
		return tareaSeleccionado;
	}

	public void setTareaSeleccionado(String tareaSeleccionado) {
		this.tareaSeleccionado = tareaSeleccionado;
	}

	public boolean isFlagObligatorio() {
		return flagObligatorio;
	}

	public void setFlagObligatorio(boolean flagObligatorio) {
		this.flagObligatorio = flagObligatorio;
	}

	/*public boolean isFlagClienteNuevo() {
		return flagClienteNuevo;
	}

	public void setFlagClienteNuevo(boolean flagClienteNuevo) {
		this.flagClienteNuevo = flagClienteNuevo;
	}*/
	
	public String getFlagClienteNuevo() {
		return flagClienteNuevo;
	}

	public void setFlagClienteNuevo(String flagClienteNuevo) {
		this.flagClienteNuevo = flagClienteNuevo;
	}

	/*public boolean isFlagPagoHabiente() {
		return flagPagoHabiente;
	}

	public void setFlagPagoHabiente(boolean flagPagoHabiente) {
		this.flagPagoHabiente = flagPagoHabiente;
	}*/
	
	public String getFlagPagoHabiente() {
		return flagPagoHabiente;
	}

	public void setFlagPagoHabiente(String flagPagoHabiente) {
		this.flagPagoHabiente = flagPagoHabiente;
	}

	/*public boolean isFlagTasaEspecial() {
		return flagTasaEspecial;
	}

	public void setFlagTasaEspecial(boolean flagTasaEspecial) {
		this.flagTasaEspecial = flagTasaEspecial;
	}*/
	
	public String getFlagTasaEspecial() {
		return flagTasaEspecial;
	}

	public void setFlagTasaEspecial(String flagTasaEspecial) {
		this.flagTasaEspecial = flagTasaEspecial;
	}

	/*public boolean isFlagVeriDomicil() {
		return flagVeriDomicil;
	}

	public void setFlagVeriDomicil(boolean flagVeriDomicil) {
		this.flagVeriDomicil = flagVeriDomicil;
	}*/
	
	public String getFlagVeriDomicil() {
		return flagVeriDomicil;
	}

	public void setFlagVeriDomicil(String flagVeriDomicil) {
		this.flagVeriDomicil = flagVeriDomicil;
	}

	/*public boolean isFlagVeriLaboral() {
		return flagVeriLaboral;
	}

	public void setFlagVeriLaboral(boolean flagVeriLaboral) {
		this.flagVeriLaboral = flagVeriLaboral;
	}*/
	
	public String getFlagVeriLaboral() {
		return flagVeriLaboral;
	}

	public void setFlagVeriLaboral(String flagVeriLaboral) {
		this.flagVeriLaboral = flagVeriLaboral;
	}

	/*public boolean isFlagDPS() {
		return flagDPS;
	}

	public void setFlagDPS(boolean flagDPS) {
		this.flagDPS = flagDPS;
	}*/
	
	public String getFlagDPS() {
		return flagDPS;
	}

	public void setFlagDPS(String flagDPS) {
		this.flagDPS = flagDPS;
	}

	public boolean isFlagComiteRiesgo() {
		return flagComiteRiesgo;
	}

	public void setFlagComiteRiesgo(boolean flagComiteRiesgo) {
		this.flagComiteRiesgo = flagComiteRiesgo;
	}

	public boolean isFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public GuiaDocumentariaBeanLocal getGuiaDocumentariaBean() {
		return guiaDocumentariaBean;
	}
	
	public void setGuiaDocumentariaBean(
			GuiaDocumentariaBeanLocal guiaDocumentariaBean) {
		this.guiaDocumentariaBean = guiaDocumentariaBean;
	}
	
	public GuiaDocumentaria getDatosGuiaDocumentaria() {
		return datosGuiaDocumentaria;
	}
	
	public void setDatosGuiaDocumentaria(GuiaDocumentaria datosGuiaDocumentaria) {
		this.datosGuiaDocumentaria = datosGuiaDocumentaria;
	}
	
	public Long getIdGuiaDocumentaria() {
		return idGuiaDocumentaria;
	}
	
	public void setIdGuiaDocumentaria(Long idGuiaDocumentaria) {
		this.idGuiaDocumentaria = idGuiaDocumentaria;
	}
	
	public List<SelectItem> getListaGenerica() {
		return listaGenerica;
	}

	public void setListaGenerica(List<SelectItem> listaGenerica) {
		this.listaGenerica = listaGenerica;
	}
	
	public String getSubProductoSeleccionado() {
		return subProductoSeleccionado;
	}
	public void setSubProductoSeleccionado(String subProductoSeleccionado) {
		this.subProductoSeleccionado = subProductoSeleccionado;
	}
	public List<SelectItem> getSubProductos() {
		return subProductos;
	}
	public void setSubProductos(List<SelectItem> subProductos) {
		this.subProductos = subProductos;
	}
				
}
