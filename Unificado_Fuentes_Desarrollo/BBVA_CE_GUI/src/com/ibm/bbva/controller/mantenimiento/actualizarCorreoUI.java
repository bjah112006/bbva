package com.ibm.bbva.controller.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.swing.text.TabableView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Accion;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TareaAccion;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.session.AccionBeanLocal;
import com.ibm.bbva.session.DatosCorreoBeanLocal;
import com.ibm.bbva.session.DatosCorreoDestinBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TareaAccionBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.tabla.util.vo.DestinatariosVO;
import com.ibm.bbva.util.Util;

@ManagedBean (name="actualizarCorreo")
@SessionScoped
public class actualizarCorreoUI  extends AbstractMBean{
	
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private DatosCorreoDestinBeanLocal datosCorreoDestinBean;
	@EJB
	private DatosCorreoBeanLocal datosCorreoBean;
	@EJB
	private AccionBeanLocal accionBean;
	@EJB
	private PerfilBeanLocal perfilBean;
	@EJB 
	private TareaAccionBeanLocal tareaAccionBean;
	@EJB
	private TareaPerfilBeanLocal tareaPerfilBean;
	
	private DatosCorreo datosCorreo;
	private DatosCorreoDestin datosCorreoDestin;
	private List<SelectItem> tiposTareas; 
	private List<SelectItem> tiposEstados;
	private List<SelectItem> tipoProductos;
	private List<SelectItem> tipoAccion;
	private List<SelectItem> tipoPerfil;
	private List<DestinatariosVO> listDestinatarios;
	private List<DatosCorreoDestin> listaDestinatarios;
	private List<DatosCorreoDestin> listaDestin;
	private List<Perfil> listaAuxDetalle;
	private String accionSeleccionado;
	private String productoSeleccionado;
	private String tareaSeleccionada;
	private String perfilSeleccionado;
	private String estadoSeleccionado;
	private String asunto;
	private String strCuerpo;
	private String strAccion="";
	private Long idCabecera;
	private Long idDatosCorreoDestin;
	private boolean desAccion;
	private boolean desTarea;
	private boolean deshabilitarProducto; // booleano que oculta el Producto para actualizar.
	private boolean desProducto; // booleano que oculta el Producto para crear.
	private boolean rendTarea;
	
	private List<SelectItem> lstAuxDestinatarios; 
	private String destinoSeleccionado;
	private HtmlInputText htmlListaDestinoValores;
	private String strListaDestinoValores;
	private List<DatosCorreoDestin> listaRecuperada;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(actualizarCorreoUI.class);
	
	/**
	 * Constructor 
	 */
	public actualizarCorreoUI(){
		//super();
		LOG.info("Entra al Constructor");
		//obtenerDatos();
	}
	
	@PostConstruct
	public void init(){
		tipoAccion=Util.crearItems(accionBean.buscarTodos(),true,"id","descripcion");
		tipoProductos=Util.crearItems(productoBean.buscarTodos(),true,"id","descripcion");
		/***Inicio Req-281***/
		//tipoPerfil=Util.crearItems(perfilBean.buscarTodos(),true,"id","descripcion");
		tipoPerfil=Util.crearItems(perfilBean.buscarPorProceso(),true,"id","descripcion");
		/***Fin Req-281***/
		lstAuxDestinatarios= new ArrayList<SelectItem>();
		inicializarControles();
	}

	public void obtenerDatos(){
		LOG.info("Ingresar a Obtener Datos");
		FacesContext ctx =  FacesContext.getCurrentInstance();
		strListaDestinoValores="";
		datosCorreo=(DatosCorreo)getObjectSession(Constantes.DATOS_CORREO_SESION);
		datosCorreoDestin=(DatosCorreoDestin)getObjectSession(Constantes.DATOS_CORREO_DETALLE_SESION);
		rendTarea=true;
		/***Inicio Req-281***/
		//tipoPerfil2=Util.crearItems(perfilBean.buscarTodos(),true,"id","descripcion");
		tipoPerfil=Util.crearItems(perfilBean.buscarTodos(),true,"id","descripcion");
		/***Fin Req-281***/
		
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
		
		tiposTareas=Util.crearItems(auxTareas, true,"id","descripcion");
		
		//tipoAccion2=Util.crearItems(accionBean.buscarTodos(), true,"id","descripcion");
		if (datosCorreo!=null){ // inicializa cuando ya existe.
			LOG.info("Entra al init de actualizar datos existentes");
			LOG.info("Id Cabecera" + datosCorreo.getId());
			desAccion=true;
			desTarea=true;
			idCabecera=datosCorreo.getId();
			asunto=datosCorreo.getAsunto();
			LOG.info("asunto" + asunto);
			byte[] byteCuerpo=datosCorreo.getCuerpo();
			strCuerpo= byteCuerpo==null ? null : new String (byteCuerpo);
			if (datosCorreo.getProducto().getId()!=0){
				productoSeleccionado=String.valueOf(datosCorreo.getProducto().getId());
				LOG.info("productoSeleccionado " + productoSeleccionado);
			}
			if (datosCorreo.getTarea().getId()!=0){
				tareaSeleccionada=String.valueOf(datosCorreo.getTarea().getId());
				LOG.info("tareaSeleccionada2 " + tareaSeleccionada);
				buscarAccionAlObtenerDatos(tareaSeleccionada);
			}
			
			List<TareaPerfil> lstTareaPerfil= new ArrayList<TareaPerfil>();
			lstTareaPerfil=tareaPerfilBean.buscarPorIdTarea(datosCorreo.getTarea().getId());
			
			for (TareaPerfil item : lstTareaPerfil){
				perfilSeleccionado=item.getPerfil().getCodigo();
				buscarTareaAlObtenerDatos(perfilSeleccionado);
			}
			
			if (datosCorreo.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO)){
				estadoSeleccionado="1";
			}else{
				estadoSeleccionado="0";
			}
			if (datosCorreo.getAccion().getId()!=0){
				accionSeleccionado=String.valueOf(datosCorreo.getAccion().getId());
				LOG.info("accionSeleccionado2 " + accionSeleccionado);
			}
			ctx = FacesContext.getCurrentInstance();
			tablaDestinatariosUI destinatario = (tablaDestinatariosUI)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaDestinatarios");
			//destinatario.recuperarListas(datosCorreo);
			destinatario.inicio(datosCorreo);
		}else{ // para registrar una nueva configuracion de correo
			inicializarControles();
		}
	}
	
	public void buscarTarea(AjaxBehaviorEvent event){
		LOG.info("perfil Seleccionado " + perfilSeleccionado);
		accionSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		List<Tarea> listPT= new ArrayList<Tarea>();
		if (perfilSeleccionado!=null && !perfilSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info("Busqueda por 1 solo producto ... "+perfilSeleccionado);
			//listPT=productoTareaBean.buscarTareasByProducto(Long.valueOf(productoSeleccionado));
			listPT=tareaBean.buscarPorPerfil(Long.valueOf(perfilSeleccionado));
		}else{
			tiposTareas=Util.listaVacia();
		}
		
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
		
		List<Tarea> auxListPT = new ArrayList<Tarea>();
					
		for(Tarea tarea : listPT){
			if(tarea.getCodigo() != null){
				if(!tarea.getCodigo().equals("26")){
					auxListPT.add(tarea);
				}
			}
		}
		
		tiposTareas=Util.crearItems(auxListPT,true, "id", "descripcion");
		
		if(tiposTareas!=null && tiposTareas.size()>0){
			LOG.info("tamaño tiposTareas = "+tiposTareas.size());
		}
		else{
			LOG.info("tiposTareas list es nulo o vacío");
		}	
	}
	
	
	public void buscarTarea2(AjaxBehaviorEvent event){
		LOG.info("perfil Seleccionado2 " + perfilSeleccionado);
		accionSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tipoAccion=Util.listaVacia();
		tareaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		List<Tarea> listPT= new ArrayList<Tarea>();
		if (perfilSeleccionado!=null && !perfilSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info("Busqueda por 1 solo producto ... "+perfilSeleccionado);
			//listPT=productoTareaBean.buscarTareasByProducto(Long.valueOf(productoSeleccionado));
			listPT=tareaBean.buscarPorPerfil(Long.valueOf(perfilSeleccionado));
		}else{
			tiposTareas=Util.listaVacia();
		}
		
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
		
		List<Tarea> auxListPT = new ArrayList<Tarea>();
					
		for(Tarea tarea : listPT){
			if(tarea.getCodigo() != null){
				if(!tarea.getCodigo().equals("26")){
					auxListPT.add(tarea);
				}
			}
		}
				
		tiposTareas=Util.crearItems(auxListPT,true, "id", "descripcion");
		
		if(tiposTareas!=null && tiposTareas.size()>0){
			LOG.info("tamaño tiposTareas = "+tiposTareas.size());
		}
		else{
			LOG.info("tiposTareas2 list es nulo o vacío");
		}	
	}
	
	public void buscarAccion(AjaxBehaviorEvent event){
		LOG.info("tareaSeleccionada " + tareaSeleccionada);
		List<Accion> listAccion= new ArrayList<Accion>();
		List<TareaAccion> listTareaAccion= new ArrayList<TareaAccion>();
		if (tareaSeleccionada!=null && !tareaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			listTareaAccion=tareaAccionBean.buscarPorIdTarea(Long.valueOf(tareaSeleccionada));
			for(int i=0; i<listTareaAccion.size();i++){
				listAccion.add(listTareaAccion.get(i).getAccion());
			}
		}else{
			tipoAccion=Util.listaVacia();
		}
		tipoAccion=Util.crearItems(listAccion,true, "id", "descripcion");
		if(tipoAccion!=null && tipoAccion.size()>0){
			LOG.info("tamaño tiposTareas = "+tipoAccion.size());
		}
		else{
			LOG.info("tipoAccion list es nulo o vacío");
		}	
	}
	
	
	public void buscarAccion2(AjaxBehaviorEvent event){
		LOG.info("tareaSeleccionada " + tareaSeleccionada);
		List<Accion> listAccion= new ArrayList<Accion>();
		List<TareaAccion> listTareaAccion= new ArrayList<TareaAccion>();
		if (tareaSeleccionada!=null && !tareaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			listTareaAccion=tareaAccionBean.buscarPorIdTarea(Long.valueOf(tareaSeleccionada));
			for(int i=0; i<listTareaAccion.size();i++){
				listAccion.add(listTareaAccion.get(i).getAccion());
			}
		}else{
			tipoAccion=Util.listaVacia();
		}
		tipoAccion=Util.crearItems(listAccion,true, "id", "descripcion");
		if(tipoAccion!=null && tipoAccion.size()>0){
			LOG.info("tamaño tiposTareas = "+tipoAccion.size());
		}
		else{
			LOG.info("tipoAccion list es nulo o vacío");
		}	
	}
	
	public void inicializarControles(){
		datosCorreo=new DatosCorreo();
		datosCorreoDestin=new DatosCorreoDestin();
		asunto="";
		strCuerpo="";
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance();
		tablaDestinatariosUI destinatarios = (tablaDestinatariosUI)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaDestinatarios");
		destinatarios.init();
	}
	
	public void inicio(){
		/***Inicio Req-281***/
		//lstAuxDestinatarios=Util.crearItems(perfilBean.buscarTodos(),true,"id","descripcion");
		tipoPerfil=Util.crearItems(perfilBean.buscarPorProceso(),true,"id","descripcion");
		lstAuxDestinatarios=Util.crearItems(perfilBean.buscarPorProceso(),true,"id","descripcion");
		/***Fin Req-281***/
		datosCorreo=new DatosCorreo();
		datosCorreoDestin=new DatosCorreoDestin();
		productoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		perfilSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;;
		accionSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;;
		asunto="";
		strCuerpo="";
		rendTarea=false;
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance();
		tablaDestinatariosUI destinatarios = (tablaDestinatariosUI)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaDestinatarios");
		destinatarios.inicio(datosCorreo);
	}
	
	public String grabar(){
		Long idDatosCorreo;
		LOG.info("Tamaño lstAuxDestinatarios" + lstAuxDestinatarios.size());
		FacesContext ctx = FacesContext.getCurrentInstance();
		LOG.info("Actualizar los datos del correo");
		String ruta="";
		try{
			if (esValido(datosCorreo)){
				if (datosCorreo!=null){
					idDatosCorreo=datosCorreo.getId();
				}else{
					idDatosCorreo=0L;
				}
				if (idDatosCorreo==0){ 
					crear(); // CREAR
				}else{
					actualizar(idDatosCorreo);//ACTUALIZAR
				}
				ctx = FacesContext.getCurrentInstance();
				buscarCorreoUI buscarCorreoUI = (buscarCorreoUI)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarCorreo");
				//consultarTabla.buscar();
				buscarCorreoUI.actualizarLista();
				removeObjectSession(Constantes.DATOS_CORREO_SESION);
				removeObjectSession(Constantes.DATOS_CORREO_DETALLE_SESION);
				ruta= "/mantenimiento/formManConsultaCorreos?faces-redirect=true";
			}
			}catch(Exception ex){
				LOG.error(ex.getMessage(), ex);
				ruta= null;
			}
		return ruta;
		}
		
	
	public boolean esValido(DatosCorreo datosCorreo){
		boolean existeError = false;
		long cantidadExistentes;
		String idComponente = "formRegistroCorreo";
		Long idDatosCorreo=datosCorreo.getId();
		
		if (productoSeleccionado.equals("-1")){
			addMessageError(idComponente+":cmbProducto",
					"com.ibm.bbva.common.TablaEnvioCorreo.msg.producto");
			existeError=true;
		}
		
		if (idDatosCorreo==0){  // si es nuevo
			cantidadExistentes=datosCorreoBean.buscarExistentes(Long.valueOf(tareaSeleccionada),Long.valueOf(accionSeleccionado),
																Long.valueOf(productoSeleccionado));
			if (cantidadExistentes>0){
				addMessageError(idComponente+":grabarbr",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.YaExiste");
				existeError=true;
			}
			if (perfilSeleccionado.equals("-1")){
				addMessageError(idComponente+":cmbPerfil",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.perfil");
				existeError=true;
			}
			if (tareaSeleccionada.equals("-1")){
				addMessageError(idComponente+":cmbTarea",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.tarea");
				existeError=true;
			}
			if (accionSeleccionado.equals("-1")) {
				addMessageError(idComponente + ":cmbAccion",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.accion");
				existeError = true;
			}
			
			if (strListaDestinoValores.length()==0){
				addMessageError(idComponente+":grabarbr",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.DestinatariosNulos");
				existeError=true;
			}
			
		}
		if (idDatosCorreo>0){  // si es antiguo
//			cantidadExistentes=datosCorreoBean.buscarExistentes(Long.valueOf(tareaSeleccionada2),Long.valueOf(accionSeleccionado),
//																Long.valueOf(productoSeleccionado));
//			if (cantidadExistentes>0){
//				addMessageError(idComponente+":grabarbr",
//						"com.ibm.bbva.common.TablaEnvioCorreo.msg.YaExiste");
//				existeError=true;
//			}
			if (perfilSeleccionado.equals("-1")){
				addMessageError(idComponente+":cmbPerfil2",
					"com.ibm.bbva.common.TablaEnvioCorreo.msg.perfil");
				existeError=true;
			}
			if (tareaSeleccionada.equals("-1")){
				addMessageError(idComponente+":cmbTarea2",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.tarea");
				existeError=true;
			}
			if (accionSeleccionado.equals("-1")) {
				addMessageError(idComponente + ":cmbAccion2",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.accion");
				existeError = true;
			}
			
			
		}
		
		if (asunto == null || "".equals(asunto)) {
			addMessageError(idComponente + ":asunto",
					"com.ibm.bbva.common.TablaEnvioCorreo.msg.asunto");
			existeError = true;
		}
		if (strCuerpo == null || "".equals(strCuerpo)|| "<p>&nbsp;</p>".equals(strCuerpo)) {
			addMessageError(idComponente + ":idCuerpo",
					"com.ibm.bbva.common.TablaEnvioCorreo.msg.cuerpo");
			existeError = true;
		}
		
		return !existeError;
		
	}
	
	public String cancelar(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		buscarCorreoUI buscarCorreo = (buscarCorreoUI)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarCorreo");
		buscarCorreo.limpiar();
		removeObjectSession(Constantes.DATOS_CORREO_SESION);
		removeObjectSession(Constantes.DATOS_CORREO_DETALLE_SESION);
		removeObjectSession(Constantes.LISTA_AUX_DETALLE);
		return "/mantenimiento/formManConsultaCorreos?faces-redirect=true";
	}
	
		public TareaBeanLocal getTareaBean() {
			return tareaBean;
	}
		
	public void actualizar(Long idCabecera){
		try{
			DatosCorreo datosCorreo = new DatosCorreo();
			datosCorreo.setId(idCabecera);
			datosCorreo.setAsunto(asunto);
			//datosCorreo.setCuerpo(strCuerpo==null ? null : strCuerpo.getBytes());
			String strAux="";
			strAux=strCuerpo;
			strAux=strAux.replaceAll("<.*?>"," ");
			datosCorreo.setAuxCuerpo(strAux);
			datosCorreo.setCuerpo(strCuerpo==null ? null : strCuerpo.getBytes());
			
			datosCorreo.setFlagActivo(estadoSeleccionado);
			
			Tarea tarea = new Tarea();
			tarea.setId(Long.valueOf(tareaSeleccionada));
			datosCorreo.setTarea(tarea);
			
			Accion accion = new Accion();
			accion.setId(Long.valueOf(accionSeleccionado));
			datosCorreo.setAccion(accion);
			
			Producto producto = new Producto();
			producto=productoBean.buscarPorId(Long.valueOf(productoSeleccionado));
			datosCorreo.setProducto(producto);
			
			datosCorreoBean.update(datosCorreo);
			LOG.info("ID despues" + datosCorreo.getId());
			//List<DatosCorreoDestin> listaRecuperada=new ArrayList<DatosCorreoDestin>();
			listaRecuperada=new ArrayList<DatosCorreoDestin>();
			listaRecuperada=datosCorreoDestinBean.buscarPorIdDatosCorreo(datosCorreo.getId());
			List<Perfil> lstPerfilesDestinoRecuperada=new ArrayList<Perfil>();
			if (listaRecuperada.size()>0){
				for (int i=0 ; i<listaRecuperada.size();i++){
					Perfil perfil = new Perfil();
					perfil.setId(Long.valueOf(listaRecuperada.get(i).getPerfil().getCodigo()));
					perfil.setCodigo(listaRecuperada.get(i).getPerfil().getCodigo());
					perfil.setDescripcion(listaRecuperada.get(i).getPerfil().getDescripcion());
					lstPerfilesDestinoRecuperada.add(perfil);
				}
			}
			List<Perfil> lstAuxPerfilesActualizar=new ArrayList<Perfil>();
				//listaAuxDetalle=new ArrayList<Perfil>();
			
			String[] valuesDestino = strListaDestinoValores.split(",");
			for (String values: valuesDestino ){
				Perfil perfil = new Perfil();
				perfil=perfilBean.buscarPorId(Long.valueOf(values));
				lstAuxPerfilesActualizar.add(perfil);
			}
			actualizarListas(lstPerfilesDestinoRecuperada,lstAuxPerfilesActualizar);
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
		
	}
	
	public void actualizarListas(List<Perfil> listaBD, List<Perfil> listaActualizada){
		// elimino lo anterior
		for (int i=0; i<listaBD.size();i++){
			DatosCorreoDestin detalleDestino = new DatosCorreoDestin();
			Perfil perfil = new Perfil();
			if (listaBD.get(i).getCodigo()!=null){
				perfil.setId(Long.valueOf(listaBD.get(i).getCodigo()));
			}
			if (listaBD.get(i).getCodigo()!=null){
				perfil.setCodigo(listaBD.get(i).getCodigo());
			}
			if (listaBD.get(i).getDescripcion()!=null){
				perfil.setDescripcion(listaBD.get(i).getDescripcion());
			}
			long idDetalleDestino=obtenerIdDetalle(listaBD.get(i).getCodigo(), datosCorreo.getId());
			detalleDestino.setId(idDetalleDestino);
			detalleDestino.setPerfil(perfil);
			detalleDestino.setDatosCorreo(datosCorreo);
			datosCorreoDestinBean.delete(detalleDestino);
		}
		// creo solamente con la lista actualizada
		if (listaActualizada!=null){
			if (listaActualizada.size()>0){
				for (int i=0; i<listaActualizada.size();i++){
					DatosCorreoDestin detalleDestino = new DatosCorreoDestin();
					Perfil perfil = new Perfil();
					perfil.setId(Long.valueOf(listaActualizada.get(i).getCodigo()));
					perfil.setCodigo(listaActualizada.get(i).getCodigo());
					perfil.setDescripcion(listaActualizada.get(i).getDescripcion());
					detalleDestino.setPerfil(perfil);
					detalleDestino.setDatosCorreo(datosCorreo);
					datosCorreoDestinBean.create(detalleDestino);
				}
			}
		}
	}
	
	public long obtenerIdDetalle(String codigo, long idCabecera){
		long id=0;
		DatosCorreoDestin aux = new DatosCorreoDestin();
		aux=datosCorreoDestinBean.buscarPorIdPerfilAndCabecera(codigo,idCabecera);
		id=aux.getId();
		return id;
	} 
	
	public void crear(){
		try{
			DatosCorreo datosCorreo = new DatosCorreo();
			// CREACION DE LA CABECERA EN BD
			datosCorreo.setAsunto(asunto);
			String strAux="";
			strAux=strCuerpo;
			strAux=strAux.replaceAll("<.*?>"," ");
			datosCorreo.setAuxCuerpo(strAux);
			datosCorreo.setCuerpo(strCuerpo==null ? null : strCuerpo.getBytes());
			datosCorreo.setId(0);
			Tarea tarea = new Tarea();
			tarea.setId(Long.valueOf(tareaSeleccionada));
			datosCorreo.setTarea(tarea);
			
			Accion accion = new Accion();
			accion.setId(Long.valueOf(accionSeleccionado));
			datosCorreo.setAccion(accion);
			datosCorreo.setFlagActivo(estadoSeleccionado);
			
			Producto producto = new Producto();
			producto=productoBean.buscarPorId(Long.valueOf(productoSeleccionado));
			datosCorreo.setProducto(producto);
			datosCorreo=datosCorreoBean.create(datosCorreo);
			// CREACION DE DETALLE EN BD
			listaAuxDetalle=new ArrayList<Perfil>();
			String[] valuesDestino = strListaDestinoValores.split(",");
			for (String values: valuesDestino ){
				Perfil perfil = new Perfil();
				perfil=perfilBean.buscarPorId(Long.valueOf(values));
				listaAuxDetalle.add(perfil);
			}
			if (listaAuxDetalle.size()>0 || listaAuxDetalle!=null){
				for (int i=0; i<listaAuxDetalle.size();i++){
					DatosCorreoDestin detalleDestino = new DatosCorreoDestin();
					Perfil perfil = new Perfil();
					perfil.setId(Long.valueOf(listaAuxDetalle.get(i).getCodigo()));
					perfil.setCodigo(listaAuxDetalle.get(i).getCodigo());
					perfil.setDescripcion(listaAuxDetalle.get(i).getDescripcion());
					detalleDestino.setPerfil(perfil);
					detalleDestino.setDatosCorreo(datosCorreo);
					datosCorreoDestinBean.create(detalleDestino);
					}
					
				}
				removeObjectSession(Constantes.LISTA_AUX_DETALLE);
		}catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}
	}

	
	public void buscarAccionAlObtenerDatos(String strTareaSeleccionada){
		LOG.info("strTareaSeleccionada " + tareaSeleccionada);
		List<Accion> lstAccion= new ArrayList<Accion>();
		List<TareaAccion> listTarAcc= new ArrayList<TareaAccion>();
		if (strTareaSeleccionada!=null && !strTareaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			listTarAcc=tareaAccionBean.buscarPorIdTarea(Long.valueOf(strTareaSeleccionada));
			for(int i=0; i<listTarAcc.size();i++){
				lstAccion.add(listTarAcc.get(i).getAccion());
			}
		}else{
			tipoAccion=Util.listaVacia();
		}
		tipoAccion=Util.crearItems(lstAccion,true, "id", "descripcion");
		if(tipoAccion!=null && tipoAccion.size()>0){
			LOG.info("tamaño tiposTareas = "+tipoAccion.size());
		}
		else{
			LOG.info("tipoAccion list es nulo o vacío");
		}	
	}
	
	public void buscarTareaAlObtenerDatos(String strPerfilSeleccionado){
		LOG.info("strPerfilSeleccionado " + strPerfilSeleccionado);
		List<Tarea> lstPT= new ArrayList<Tarea>();
		if (strPerfilSeleccionado!=null && !strPerfilSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info("Busqueda por 1 solo producto ... "+strPerfilSeleccionado);
			lstPT=tareaBean.buscarPorPerfil(Long.valueOf(strPerfilSeleccionado));
		}else{
			tiposTareas=Util.listaVacia();
		}
		
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
		
		List<Tarea> auxListPT = new ArrayList<Tarea>();
					
		for(Tarea tarea : lstPT){
			if(tarea.getCodigo() != null){
				if(!tarea.getCodigo().equals("26")){
					auxListPT.add(tarea);
				}
			}
		}
				
		tiposTareas=Util.crearItems(auxListPT,true, "id", "descripcion");
		
		if(tiposTareas!=null && tiposTareas.size()>0){
			LOG.info("tamaño tiposTareas = "+tiposTareas.size());
		}
		else{
			LOG.info("tiposTareas2 list es nulo o vacío");
		}	
	}
	
	
	

	public void setTareaBean(TareaBeanLocal tareaBean) {
		this.tareaBean = tareaBean;
	}


	public ProductoBeanLocal getProductoBean() {
		return productoBean;
	}


	public void setProductoBean(ProductoBeanLocal productoBean) {
		this.productoBean = productoBean;
	}


	public List<SelectItem> getTiposTareas() {
		return tiposTareas;
	}


	public void setTiposTareas(List<SelectItem> tiposTareas) {
		this.tiposTareas = tiposTareas;
	}	

	public String getProductoSeleccionado() {
		return productoSeleccionado;
	}


	public void setProductoSeleccionado(String productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}


	public String getTareaSeleccionada() {
		return tareaSeleccionada;
	}


	public void setTareaSeleccionada(String tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}


	public String getAsunto() {
		return asunto;
	}


	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	
	
	public String getStrCuerpo() {
		return strCuerpo;
	}

	public void setStrCuerpo(String strCuerpo) {
		this.strCuerpo = strCuerpo;
	}

	public DatosCorreoDestinBeanLocal getDatosCorreoDestinBean() {
		return datosCorreoDestinBean;
	}

	public void setDatosCorreoDestinBean(
			DatosCorreoDestinBeanLocal datosCorreoDestinBean) {
		this.datosCorreoDestinBean = datosCorreoDestinBean;
	}


	public DatosCorreoDestin getDatosCorreoDestin() {
		return datosCorreoDestin;
	}


	public void setDatosCorreoDestin(DatosCorreoDestin datosCorreoDestin) {
		this.datosCorreoDestin = datosCorreoDestin;
	}


	public DatosCorreoBeanLocal getDatosCorreoBean() {
		return datosCorreoBean;
	}


	public void setDatosCorreoBean(DatosCorreoBeanLocal datosCorreoBean) {
		this.datosCorreoBean = datosCorreoBean;
	}


	public DatosCorreo getDatosCorreo() {
		return datosCorreo;
	}


	public void setDatosCorreo(DatosCorreo datosCorreo) {
		this.datosCorreo = datosCorreo;
	}

	public Long getIdDatosCorreoDestin() {
		return idDatosCorreoDestin;
	}


	public void setIdDatosCorreoDestin(Long idDatosCorreoDestin) {
		this.idDatosCorreoDestin = idDatosCorreoDestin;
	}


	public List<DestinatariosVO> getListDestinatarios() {
		return listDestinatarios;
	}


	public void setListDestinatarios(List<DestinatariosVO> listDestinatarios) {
		this.listDestinatarios = listDestinatarios;
	}


	public Long getIdCabecera() {
		return idCabecera;
	}


	public void setIdCabecera(Long idCabecera) {
		this.idCabecera = idCabecera;
	}


	public boolean isDesAccion() {
		return desAccion;
	}


	public void setDesAccion(boolean desAccion) {
		this.desAccion = desAccion;
	}


	public boolean isDesTarea() {
		return desTarea;
	}


	public void setDesTarea(boolean desTarea) {
		this.desTarea = desTarea;
	}


	public boolean isDeshabilitarProducto() {
		return deshabilitarProducto;
	}


	public void setDeshabilitarProducto(boolean deshabilitarProducto) {
		this.deshabilitarProducto = deshabilitarProducto;
	}

	public boolean isDesProducto() {
		return desProducto;
	}


	public void setDesProducto(boolean desProducto) {
		this.desProducto = desProducto;
	}

	public List<DatosCorreoDestin> getListaDestinatarios() {
		return listaDestinatarios;
	}

	public void setListaDestinatarios(List<DatosCorreoDestin> listaDestinatarios) {
		this.listaDestinatarios = listaDestinatarios;
	}

	
	public List<SelectItem> getTipoAccion() {
		return tipoAccion;
	}

	public void setTipoAccion(List<SelectItem> tipoAccion) {
		this.tipoAccion = tipoAccion;
	}

	public String getAccionSeleccionado() {
		return accionSeleccionado;
	}

	public void setAccionSeleccionado(String accionSeleccionado) {
		this.accionSeleccionado = accionSeleccionado;
	}

	public String getStrAccion() {
		return strAccion;
	}

	public void setStrAccion(String strAccion) {
		this.strAccion = strAccion;
	}

	public List<SelectItem> getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(List<SelectItem> tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public String getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(String perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public List<SelectItem> getTipoProductos() {
		return tipoProductos;
	}

	public void setTipoProductos(List<SelectItem> tipoProductos) {
		this.tipoProductos = tipoProductos;
	}

	public List<SelectItem> getTiposEstados() {
		return tiposEstados;
	}

	public void setTiposEstados(List<SelectItem> tiposEstados) {
		this.tiposEstados = tiposEstados;
	}

	public String getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}
	public boolean isRendTarea() {
		return rendTarea;
	}

	public void setRendTarea(boolean rendTarea) {
		this.rendTarea = rendTarea;
	}	
	public List<DatosCorreoDestin> getListaDestin() {
		return listaDestin;
	}

	public void setListaDestin(List<DatosCorreoDestin> listaDestin) {
		this.listaDestin = listaDestin;
	}

	public List<Perfil> getListaAuxDetalle() {
		return listaAuxDetalle;
	}

	public void setListaAuxDetalle(List<Perfil> listaAuxDetalle) {
		this.listaAuxDetalle = listaAuxDetalle;
	}

	public List<SelectItem> getLstAuxDestinatarios() {
		return lstAuxDestinatarios;
	}

	public void setLstAuxDestinatarios(List<SelectItem> lstAuxDestinatarios) {
		this.lstAuxDestinatarios = lstAuxDestinatarios;
	}

	public String getDestinoSeleccionado() {
		return destinoSeleccionado;
	}

	public void setDestinoSeleccionado(String destinoSeleccionado) {
		this.destinoSeleccionado = destinoSeleccionado;
	}

	public HtmlInputText getHtmlListaDestinoValores() {
		return htmlListaDestinoValores;
	}

	public void setHtmlListaDestinoValores(HtmlInputText htmlListaDestinoValores) {
		this.htmlListaDestinoValores = htmlListaDestinoValores;
	}

	public String getStrListaDestinoValores() {
		return strListaDestinoValores;
	}

	public void setStrListaDestinoValores(String strListaDestinoValores) {
		this.strListaDestinoValores = strListaDestinoValores;
	}

	public List<DatosCorreoDestin> getListaRecuperada() {
		return listaRecuperada;
	}

	public void setListaRecuperada(List<DatosCorreoDestin> listaRecuperada) {
		this.listaRecuperada = listaRecuperada;
	}

	
}
