package com.ibm.bbva.controller.mantenimiento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.session.DatosCorreoBeanLocal;
import com.ibm.bbva.session.DatosCorreoDestinBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConsultaCorreoVO;
import com.ibm.bbva.util.Util;

import javax.faces.model.SelectItem;
import javax.sql.rowset.serial.SerialException;

@ManagedBean (name="buscarCorreo")
@SessionScoped
public class buscarCorreoUI extends AbstractSortPagDataTableMBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private DatosCorreoDestinBeanLocal datosCorreoDestinBean;
	@EJB
	private DatosCorreoBeanLocal datosCorreoBean;
	@EJB
	private PerfilBeanLocal perfilBeanLocal;
	@EJB
	private TareaPerfilBeanLocal tareaPerfilBeanLocal;
	
	private DatosCorreo datCabecera;
	private List<SelectItem> tiposTareas;
	private List<SelectItem> tiposProducto;
	private List<SelectItem> tiposPerfil;
	private String perfilSeleccionado;
	private String productoSeleccionado;
	private String tareaSeleccionada="-1";
	private static final Logger LOG = LoggerFactory.getLogger(buscarCorreoUI.class);
	private List<DatosCorreo> listaDatosCorreo;
	private List<DatosCorreoDestin> listDetalle; // para obtener el detalle a Eliminar
	private List<DatosCorreo> listCabecera; // cabecera a eliminar.  
	private List<ConsultaCorreoVO> listEnvioCorreo=null;
	private boolean seleccion=false;
	private boolean habBotonEliminar=true; 
	private boolean itemSeleccionado;
	
	private HtmlDataTable tablaBinding;
	private String numRegistros;
	/**
	 * Constructor 
	 */
	public buscarCorreoUI(){
		super();
	}
	
	@PostConstruct
	public void init() {
		/*Carga Lista de Tareas*/
		//tiposTareas = Util.crearItems(tareaBean.buscarTodos(),true,"id","descripcion");
		tiposTareas=Util.listaVacia();
		/*Carga Lista de Productos*/
		tiposProducto=Util.crearItems(productoBean.buscarTodos(),true,"id","descripcion");
		/***Inicio Req-281***/
		//tiposPerfil=Util.crearItems(perfilBeanLocal.buscarTodos(),true,"id","descripcion");
		tiposPerfil=Util.crearItems(perfilBeanLocal.buscarPorProceso(),true,"id","descripcion");
		/***Fin Req-281***/
	}
		
	public void limpiarTabla(){
		listEnvioCorreo = new ArrayList<ConsultaCorreoVO>();
		setListEnvioCorreo(listEnvioCorreo);
	}
	
	public void actualizarLista(){
		this.tareaSeleccionada="";
		this.productoSeleccionado="";
		this.perfilSeleccionado="";
		listaDatosCorreo=datosCorreoBean.buscarTodos();
		mostrarTabla(listaDatosCorreo);
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
	}
	
	public void limpiar(){
		this.tareaSeleccionada="";
		this.productoSeleccionado="";
		this.perfilSeleccionado="";
		listaDatosCorreo = datosCorreoBean.buscarTodos();
		mostrarTabla(listaDatosCorreo);
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
		habBotonEliminar=true;
	}
	
	public void limpiarPerfilTarea(AjaxBehaviorEvent event){
		perfilSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
	}
	
	public void buscarTarea(AjaxBehaviorEvent event){
		LOG.info("perfil Seleccionado " + productoSeleccionado);
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
		 * del sistema (ejemplo filtros de b√∫squeda de tareas y reportes), actualmente aparece en varias
		 * opciones del sistema, ver evidencias. Verificar que no sea visible en todo el sistema."
		 *  
		 */
		
		List<Tarea> auxListPT = new ArrayList<Tarea>();
		
		if (listPT.size()>0){
			for(Tarea tarea : listPT){
				if(tarea.getCodigo() != null){
					if(!tarea.getCodigo().equals("26")){
						auxListPT.add(tarea);
					}
				}
			}
			
			tiposTareas=Util.crearItems(auxListPT,true, "id", "descripcion");
				
			if(tiposTareas!=null && tiposTareas.size()>0){
				LOG.info("tama√±o tiposTareas = "+tiposTareas.size());
			}
			else{
				LOG.info("tiposTareas list es nulo o vac√≠o");
			}	
		}else{
			tiposTareas=Util.listaVacia();
			tareaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		}
	}
	
	public String buscar() throws SerialException, SQLException{
		String idComponente = "frmConsultarCorreos";
		listaDatosCorreo=new ArrayList<DatosCorreo>();
		listEnvioCorreo = new ArrayList<ConsultaCorreoVO>();
		if (esValido()){
			DatosCorreo datosCorreo = new DatosCorreo();
			TareaPerfil tareaPerfil = new TareaPerfil();
			
			Producto producto = new Producto();
			if (productoSeleccionado != null && !productoSeleccionado.equals("") && !productoSeleccionado.equals("-1")) {
				producto.setId(Util.validarCampoLong(productoSeleccionado));
				datosCorreo.setProducto(producto);
			}
			
			if (perfilSeleccionado != null && !perfilSeleccionado.equals("") && !perfilSeleccionado.equals("-1")){
				List<TareaPerfil> lstTareaPerfil = new ArrayList<TareaPerfil>();
				lstTareaPerfil = tareaPerfilBeanLocal.buscarPorIdPerfil(Long.valueOf(perfilSeleccionado));
				if (lstTareaPerfil.size()>0){
					tareaPerfil = lstTareaPerfil.get(0);
				}else{
					Perfil perfilAux = new Perfil();
					perfilAux = perfilBeanLocal.buscarPorId(Long.valueOf(perfilSeleccionado));
					if (perfilAux != null){
						tareaPerfil.setPerfil(perfilAux);
					}
				}
			}
			
			Tarea tarea = new Tarea();
			if (tareaSeleccionada != null && !tareaSeleccionada.equals("") && !tareaSeleccionada.equals("-1")) {
				tarea.setId(Util.validarCampoLong(tareaSeleccionada));
				datosCorreo.setTarea(tarea);
			}

			if (datosCorreo != null && datosCorreo.getProducto()!=null && tareaPerfil!=null) {
				listaDatosCorreo = datosCorreoBean.resultadoBusqueda(datosCorreo, tareaPerfil);
				if (listaDatosCorreo.size()>0){
					mostrarTabla(listaDatosCorreo);
					if(super.dataTable != null){
						ordenarPorDefecto();
					}
				}
			}
			
			if (tareaPerfil!=null && tareaPerfil.getPerfil()!=null) {
				listaDatosCorreo = datosCorreoBean.resultadoBusqueda(datosCorreo, tareaPerfil);
				if (listaDatosCorreo.size()>0){
					mostrarTabla(listaDatosCorreo);
					if(super.dataTable != null){
						ordenarPorDefecto();
					}
				}
			}
			
			if (listaDatosCorreo.size()==0){
				addMessageError(idComponente + ":idTablaEnvioCorreo",
						"com.ibm.bbva.common.TablaEnvioCorreo.msg.busquedaVacia");
			}
		}
		habBotonEliminar=true;
		return "/mantenimiento/formManConsultaCorreos?faces-redirect=true";
	}
	
	public boolean esValido(){
		boolean existeError = false;
		String idComponente = "formManConsultaCorreos";
				
		if (perfilSeleccionado.equals("-1") && productoSeleccionado.equals("-1") && tareaSeleccionada.equals("-1")){
			addMessageError(idComponente + ":cmbTarea",
					"com.ibm.bbva.common.TablaEnvioCorreo.msg.SinFiltroBusqueda");
			existeError=true;
		}
		return !existeError;
	}
	
	public String eliminar(){
		LOG.info("Ingresa al metodo del eliminar ");
		if (itemSeleccionado){
			datCabecera=(DatosCorreo)getObjectSession(Constantes.DATOS_CORREO_SESION);
			try{
				listDetalle=new ArrayList();
				long idCab=datCabecera.getId();
				listDetalle=datosCorreoDestinBean.buscarPorIdDatosCorreo(idCab);
				LOG.info("Tama√±o detalle a eliminar " + listDetalle.size());
				if (listDetalle.size()>0){
					for (int i=0; i<listDetalle.size();i++){
						DatosCorreoDestin aux=new DatosCorreoDestin();
						listDetalle.get(i).setDatosCorreo(datCabecera);
						aux.setId(listDetalle.get(i).getId());
						aux.setPerfil(listDetalle.get(i).getPerfil());
						datosCorreoDestinBean.delete(aux);
					}
				}
				datosCorreoBean.delete(datCabecera);
			}catch(Exception ex){
				ex.printStackTrace();
				return null;
			}
			seleccion=false;
			actualizarLista();
			return "/mantenimiento/formManConsultaCorreos?faces-redirect=true";
		}else{
			return null;
		}
	}
	
	protected void mostrarTabla(List<DatosCorreo> listaDatosCorreo){
		itemSeleccionado=false;
		if (listaDatosCorreo.size()>0){
				listEnvioCorreo = new ArrayList<ConsultaCorreoVO>();
				for (int i=0; i<listaDatosCorreo.size();i++){
					DatosCorreo detalle=listaDatosCorreo.get(i);
					ConsultaCorreoVO consultaCorreo= new ConsultaCorreoVO();
					DatosCorreo cabecera= new DatosCorreo();
					consultaCorreo.setIdDatosCorreo(String.valueOf(detalle.getId()));
					if (detalle!=null){
						consultaCorreo.setSeleccion(false);
						if (detalle.getProducto() != null) {
							consultaCorreo.setProducto(detalle.getProducto().getDescripcion().toString());
						}	
						if (detalle.getTarea() != null) {
							consultaCorreo.setTarea(detalle.getTarea().getDescripcion().toString());
						}
						if (detalle.getAccion()!= null) {
							consultaCorreo.setAccion(detalle.getAccion().getDescripcion().toString());
						}
						consultaCorreo.setAsunto(detalle.getAsunto());
						cabecera.setAsunto(detalle.getAsunto());
						
						if(detalle.getAuxCuerpo()!=null){
							String cuerpo=detalle.getAuxCuerpo();
							consultaCorreo.setCuerpo(cuerpo);
						}
						
//						if (detalle.getCuerpo()!=null){
//							String cuerpo= detalle.getCuerpo()==null ? null : new String (detalle.getCuerpo());
//							consultaCorreo.setCuerpo(cuerpo);
//						}
						
						if (detalle.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO)) {
							consultaCorreo.setFlagActivo(true);
						} else {
							consultaCorreo.setFlagActivo(false);
						}

					}
					consultaCorreo.setIdDatosCorreo(String.valueOf(detalle.getId()));
					listEnvioCorreo.add(consultaCorreo);
				}
		}else{
			listEnvioCorreo = new ArrayList<ConsultaCorreoVO>();
			setListEnvioCorreo(listEnvioCorreo);
		}
	}
	
	public void cargarCorreos(String idCabecera,boolean seleccion) {
		LOG.info("Entro al metodo Ajax");
		LOG.info("seleccion " + seleccion);
		itemSeleccionado=seleccion;
		if (seleccion){
			habBotonEliminar=false;
			LOG.info("habBotonEliminar "+habBotonEliminar);
			DatosCorreo cabecera= new DatosCorreo();
			cabecera=datosCorreoBean.buscarPorId(Long.valueOf(idCabecera));
			LOG.info("carga Correos");
			LOG.info("Id Cabecera Correo" + cabecera.getId());
			addObjectSession(Constantes.DATOS_CORREO_SESION, cabecera);
		}else{
			habBotonEliminar=true;
			LOG.info("habBotonEliminar "+habBotonEliminar);
			removeObjectSession(Constantes.DATOS_CORREO_SESION);
		}
	}
	
	public String cancelar(){
		return "/mantenimiento/formManTablasMaestras?faces-redirect=true"; 
	}

	public String editar(){
		if (itemSeleccionado){
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			actualizarCorreoUI actualizarCorreo = (actualizarCorreoUI)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizarCorreo");
			actualizarCorreo.obtenerDatos();
			return "/mantenimiento/formManActualizaCorreo?faces-redirect=true";
		} else{
			return null;
		}
	}
	
	public String agregar(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance();
		actualizarCorreoUI actualizarCorreo = (actualizarCorreoUI)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizarCorreo");
		actualizarCorreo.inicio();
		return "/mantenimiento/formManActualizaCorreo?faces-redirect=true";
	}
	
	@Override
	public void ordenar(ActionEvent event) {
		LOG.info("ordenar::::");
		String sortFieldAttribute = getAttribute(event, "sortField");
		if (sortFieldAttribute != null) {
			if (sortField != null && sortField.equals(sortFieldAttribute)) {
				sortAscending = !sortAscending;
			} else {
				sortField = sortFieldAttribute;
				sortAscending = true;
			}
			// regresa a la primera p√°gina si cambia el orden
			dataTable.setFirst(0);
		} // si no viene el atributo sortField no cambia el ordenamiento

		String columna = this.sortField;
	}
	
	public List<SelectItem> getTiposTareas() {
		return tiposTareas;
	}

	public void setTiposTareas(List<SelectItem> tiposTareas) {
		this.tiposTareas = tiposTareas;
	}

	public List<SelectItem> getTiposProducto() {
		return tiposProducto;
	}

	public void setTiposProducto(List<SelectItem> tiposProducto) {
		this.tiposProducto = tiposProducto;
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

	public List<ConsultaCorreoVO> getListEnvioCorreo() {
		return listEnvioCorreo;
	}

	public void setListEnvioCorreo(List<ConsultaCorreoVO> listEnvioCorreo) {
		this.listEnvioCorreo = listEnvioCorreo;
	}

	public boolean isSeleccion() {
		return seleccion;
	}

	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	public List<DatosCorreo> getListaDatosCorreo() {
		return listaDatosCorreo;
	}

	public void setListaDatosCorreo(List<DatosCorreo> listaDatosCorreo) {
		this.listaDatosCorreo = listaDatosCorreo;
	}

	public DatosCorreo getDatCabecera() {
		return datCabecera;
	}

	public void setDatCabecera(DatosCorreo datCabecera) {
		this.datCabecera = datCabecera;
	}

	public List<DatosCorreoDestin> getListDetalle() {
		return listDetalle;
	}

	public void setListDetalle(List<DatosCorreoDestin> listDetalle) {
		this.listDetalle = listDetalle;
	}

	public boolean isHabBotonEliminar() {
		return habBotonEliminar;
	}

	public void setHabBotonEliminar(boolean habBotonEliminar) {
		this.habBotonEliminar = habBotonEliminar;
	}

	public List<DatosCorreo> getListCabecera() {
		return listCabecera;
	}

	public void setListCabecera(List<DatosCorreo> listCabecera) {
		this.listCabecera = listCabecera;
	}

	public List<SelectItem> getTiposPerfil() {
		return tiposPerfil;
	}

	public void setTiposPerfil(List<SelectItem> tiposPerfil) {
		this.tiposPerfil = tiposPerfil;
	}

	public String getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(String perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public String getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}

	public boolean isItemSeleccionado() {
		return itemSeleccionado;
	}

	public void setItemSeleccionado(boolean itemSeleccionado) {
		this.itemSeleccionado = itemSeleccionado;
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO ApÈndice de mÈtodo generado autom·ticamente
		
	}
	
	
}
