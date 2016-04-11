package com.ibm.mant.mantenimiento;

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
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Carterizacion;
import com.ibm.bbva.ctacte.bean.CarterizacionId;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.ProductoCE;
import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.dao.CarterizacionDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ProductoCEDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.mant.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.mant.tabla.vo.CarterizacionVO;
import com.ibm.mant.tabla.vo.RegistroTablaVO;


@ManagedBean (name="consultarCarterizacion")
@SessionScoped
public class ConsultarCarterizacionUI extends AbstractSortPagDataTableMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(ConsultarCarterizacionUI.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private TablaBDelegate tablaBDelegate;
	private List<Carterizacion> listaDatosCarterizacion;
	private List<CarterizacionVO> listaCarterizacion;
	
	private Integer codigoTablaSeleccionada = -1;
	private RegistroTablaVO registroSeleccionado;
		
	private boolean modoGuardar;
	private boolean modoActualizar;
	private boolean modoConsultar;	
	private boolean botonBuscar;
	private boolean habBotonAgregar;
	private boolean habBotonActualizar;
	private boolean stadoCheck = false;
	private boolean habBotonEliminar;
	private TablaFacadeBean tablaFacadeBean = null;
	private String msjPaginacion="Página ";
	private String msjPaginacionError="";
	private String txtIrAPag="";
	
	private String productoSeleccionado = "";
	private String territorioSeleccionado = "";
	private String codigoCarterizacionSeleccionado = "";
	private String codigoEmpleadoSeleccionado = "";
	
	private boolean itemSeleccionado;
	private boolean seleccion = false;

	private HtmlDataTable tablaBinding;
	private String numRegistros;
	
	@EJB
	//private ProductoCEDAO productoCEDAO;
	private ProductoCEDAO productoCEDAO;
	@EJB
	private TerritorioDAO territorioDAO ;
	@EJB
	private CarterizacionDAO carterizacionDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;
	
	public List<SelectItem> productos;
	public List<SelectItem> territorios;
	//public List<SelectItem> territorioItems;
	public List<SelectItem> codigosCarterizacion;
	
	public List<ProductoCE> listaProductos;
	public List<Territorio> listaTerritorios;	
	public List<Carterizacion> listaCodigosCarterizacion;
	
	private List<Territorio> listaTerritorio;
	
	/**
	 * Constructor 
	 */
	public ConsultarCarterizacionUI(){
		super();
		
	}
	
	@PostConstruct
	public void init(){
		
		productos = Util.listaVacia();
		territorios = Util.listaVacia();
		
		/*Carga Lista de Tipo de Productos*/
		productos = Util.crearItems(productoCEDAO.findAll(),true,"id","descripcion");
		/*Carga Lista de Territorios*/
		territorios = Util.crearItems(territorioDAO.findAll(),true,"id","descripcion");
		/*Carga Lista de Codigos de Carterizacion*/
			
		codigosCarterizacion = Util.crearSimpleItems(carterizacionDAO.obtenerListaCodigos(),true, true);
		
		load();
		limpiar();
		
	}
	
	public void load(){
		
		listaDatosCarterizacion = carterizacionDAO.buscarTodos();
		mostrarTabla(listaDatosCarterizacion);

	}
	
	public String limpiar(){
		
		this.productoSeleccionado = "-1";
		this.territorioSeleccionado = "-1";
		this.codigoEmpleadoSeleccionado = "";
		this.codigoCarterizacionSeleccionado = "-1";
		
		if(listaCarterizacion != null){
			for(CarterizacionVO item : listaCarterizacion){
				item.setSeleccion(false);
			}
		}
		return null;
	}	
	
	public String buscarCarterizaciones() {
		
		listaDatosCarterizacion = new ArrayList<Carterizacion>();
		listaCarterizacion = new ArrayList<CarterizacionVO>();
		Carterizacion carterizacion = new Carterizacion();
		
		ProductoCE productoCE = new ProductoCE();
		if (productoSeleccionado != null && !productoSeleccionado.equals("") && !productoSeleccionado.equals("-1")) {
			productoCE.setId(Integer.parseInt(productoSeleccionado));
			productoCE = productoCEDAO.findByID(Long.valueOf(productoSeleccionado));
			carterizacion.setProducto(productoCE);
		}
		
		Territorio territorio = new Territorio();
		if (territorioSeleccionado != null && !territorioSeleccionado.equals("") && !territorioSeleccionado.equals("-1")) {
			territorio = territorioDAO.findByID(Long.valueOf(territorioSeleccionado));
			carterizacion.setTerritorio(territorio);
		}		

		Empleado empleado = new Empleado();
		if (codigoEmpleadoSeleccionado != null && !codigoEmpleadoSeleccionado.equals("")) {
			empleado = empleadoDAO.findByCodigo(codigoEmpleadoSeleccionado);
			carterizacion.setEmpleado(empleado);
		}
				
		if (codigoCarterizacionSeleccionado != null && !codigoCarterizacionSeleccionado.equals("") && !codigoCarterizacionSeleccionado.equals("-1")) {
			carterizacion.setCodigo(codigoCarterizacionSeleccionado);
		}
		
		if (carterizacion != null) {
			listaDatosCarterizacion = carterizacionDAO.resultadoBusqueda(carterizacion);
			mostrarTabla(listaDatosCarterizacion);
		}
				
		return "/mantenimiento/carterizacion/consultaCarterizacion?faces-redirect=true";
	}
	
	public void mostrarTabla (List<Carterizacion> listaDatosCarterizacion){
		if (listaDatosCarterizacion.size() > 0) {
			listaCarterizacion = new ArrayList<CarterizacionVO>();
			for (int i = 0; i < listaDatosCarterizacion.size(); i++) {
				Carterizacion detalle = listaDatosCarterizacion.get(i);
				CarterizacionVO carterizacion = new CarterizacionVO();
				if (detalle != null) {
					if (detalle.getProductoCE()!= null) {
						
						LOG.debug("GET PRODUCTO : " + detalle.getProductoCE().getDescripcion());
						
						carterizacion.setStrProducto(detalle.getProductoCE().getDescripcion().toString());
					}
					if (detalle.getTerritorio()!= null) {
						
						LOG.debug("GET TERRITORIO : " + detalle.getTerritorio().getDescripcion());
						
						carterizacion.setStrTerritorio(detalle.getTerritorio().getDescripcion().toString());
					}						
					if (detalle.getEmpleado()!= null) {
						
						LOG.debug("GET EMPLEADO : " + detalle.getEmpleado().getNombresCompletos().toString());
						
						carterizacion.setStrEmpleado(detalle.getEmpleado().getNombresCompletos().toString());
						carterizacion.setCodigoEmpleado(detalle.getEmpleado().getCodigo().trim());
						carterizacion.setNombreEmpleado(detalle.getEmpleado().getNombresCompletos().trim());
					}
											
					carterizacion.setCodigo(detalle.getCodigo().trim());
					carterizacion.setDescripcion(detalle.getDescripcion().trim());
					carterizacion.setIdProducto(detalle.getId().getIdProductoFk());
					carterizacion.setIdTerritorio(detalle.getId().getIdTerritorioFk());
					carterizacion.setIdEmpleado(detalle.getId().getIdEmpleadoFk());
										
					listaCarterizacion.add(carterizacion);
				}else {
					listaCarterizacion = new ArrayList<CarterizacionVO>();
					setListaCarterizacion(listaCarterizacion);
				}
			}
		}
	}
	

	public String agregar() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance();
		
		ActualizarCarterizacionUI actualizarCarterizacionUI = (ActualizarCarterizacionUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizarCarterizacion");
		actualizarCarterizacionUI.clear();
		actualizarCarterizacionUI.init();
		
		return "/mantenimiento/carterizacion/actualizarCarterizacion?faces-redirect=true";
	}
	
	
	public String actualizar(){
//		String idComponente="actualizarCarterizacion";
		if (itemSeleccionado){
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			ActualizarCarterizacionUI actualizarCarterizacionUI = (ActualizarCarterizacionUI)
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizarCarterizacion");
			actualizarCarterizacionUI.load();
			
			return "/mantenimiento/carterizacion/actualizarCarterizacion?faces-redirect=true";
		}else{	
			//addMessageError(idComponente + ":idTablaGuiaDocumentaria", "com.ibm.bbva.common.GuiaDocumentaria.msg.FailActualizar");
			return null;
		}
	}
	
	public String eliminar(){
		LOG.info("Ingresa al metodo del eliminar");
		if (itemSeleccionado){
			Carterizacion carterizacion = (Carterizacion) getObjectSession(Constantes.CARTERIZACION_SESION);
			carterizacionDAO.delete(carterizacion);
			
			if (listaCarterizacion != null) {
				int i = 0;
				for (CarterizacionVO item : listaCarterizacion){
					if (item.isSeleccion()) {
						item.setSeleccion(false);
						break;
					}
					i++;
				}
				listaCarterizacion.remove(i);
			}
			
			removeObjectSession(Constantes.CARTERIZACION_SESION);
		}
		return null;
	}
	
	//public void cargarCarterizacion(int idProducto, int idTerritorio, int idEmpleado, boolean seleccion) {
	public void cargarCarterizacion(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		int idProducto = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.idProducto}", Integer.class).intValue();
		int idTerritorio = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.idTerritorio}", Integer.class).intValue();
		int idEmpleado = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.idEmpleado}", Integer.class).intValue();
		boolean seleccion = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.seleccion}", Boolean.class).booleanValue();
		LOG.info("idProducto: " + idProducto);
		LOG.info("idTerritorio: " + idTerritorio);
		LOG.info("idEmpleado: " + idEmpleado);
		LOG.info("seleccion: " + seleccion);
		
		itemSeleccionado = seleccion;
		
		if (seleccion){
			LOG.info("Entró al metodo Ajax");
			Carterizacion carterizacion = new Carterizacion();
			CarterizacionId id = new CarterizacionId();
			
			id.setIdProductoFk(idProducto);
			id.setIdTerritorioFk(idTerritorio);
			id.setIdEmpleadoFk(idEmpleado);
			
			carterizacion = carterizacionDAO.load(id);
			LOG.info("Carga Carterizacion");
//			LOG.info("Id Producto " + String.valueOf(carterizacion.getId().getIdProductoFk()));
//			LOG.info("Id Territorio " + String.valueOf(carterizacion.getId().getIdTerritorioFk()));
//			LOG.info("Id Empleado " + String.valueOf(carterizacion.getId().getIdEmpleadoFk()));
			
			addObjectSession(Constantes.CARTERIZACION_SESION, carterizacion);
		} else {
			removeObjectSession(Constantes.CARTERIZACION_SESION);
		}
	}
	
	public String cancelar() {
		return "/mantenimiento/seleccionarTabla?faces-redirect=true";
	}
	
	
	public Integer getCodigoTablaSeleccionada() {
		return codigoTablaSeleccionada;
	}

	public void setCodigoTablaSeleccionada(Integer codigoTablaSeleccionada) {
		this.codigoTablaSeleccionada = codigoTablaSeleccionada;
	}

	public RegistroTablaVO getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(RegistroTablaVO registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public boolean isModoGuardar() {
		return modoGuardar;
	}

	public void setModoGuardar(boolean modoGuardar) {
		this.modoGuardar = modoGuardar;
	}

	public boolean isModoActualizar() {
		return modoActualizar;
	}

	public void setModoActualizar(boolean modoActualizar) {
		this.modoActualizar = modoActualizar;
	}

	public boolean isModoConsultar() {
		return modoConsultar;
	}

	public void setModoConsultar(boolean modoConsultar) {
		this.modoConsultar = modoConsultar;
	}

	public boolean isBotonBuscar() {
		return botonBuscar;
	}

	public void setBotonBuscar(boolean botonBuscar) {
		this.botonBuscar = botonBuscar;
	}

	public boolean isHabBotonAgregar() {
		return habBotonAgregar;
	}

	public void setHabBotonAgregar(boolean habBotonAgregar) {
		this.habBotonAgregar = habBotonAgregar;
	}

	public boolean isHabBotonActualizar() {
		return habBotonActualizar;
	}

	public void setHabBotonActualizar(boolean habBotonActualizar) {
		this.habBotonActualizar = habBotonActualizar;
	}

	public boolean isStadoCheck() {
		return stadoCheck;
	}

	public void setStadoCheck(boolean stadoCheck) {
		this.stadoCheck = stadoCheck;
	}

	public boolean isHabBotonEliminar() {
		return habBotonEliminar;
	}

	public void setHabBotonEliminar(boolean habBotonEliminar) {
		this.habBotonEliminar = habBotonEliminar;
	}

	public TablaFacadeBean getTablaFacadeBean() {
		return tablaFacadeBean;
	}

	public void setTablaFacadeBean(TablaFacadeBean tablaFacadeBean) {
		this.tablaFacadeBean = tablaFacadeBean;
	}

	public String getMsjPaginacion() {
		return msjPaginacion;
	}

	public void setMsjPaginacion(String msjPaginacion) {
		this.msjPaginacion = msjPaginacion;
	}

	public String getMsjPaginacionError() {
		return msjPaginacionError;
	}

	public void setMsjPaginacionError(String msjPaginacionError) {
		this.msjPaginacionError = msjPaginacionError;
	}

	public String getTxtIrAPag() {
		return txtIrAPag;
	}

	public void setTxtIrAPag(String txtIrAPag) {
		this.txtIrAPag = txtIrAPag;
	}

	public String getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(String productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}

	public String getTerritorioSeleccionado() {
		return territorioSeleccionado;
	}

	public void setTerritorioSeleccionado(String territorioSeleccionado) {
		this.territorioSeleccionado = territorioSeleccionado;
	}

	public String getCodigoCarterizacionSeleccionado() {
		return codigoCarterizacionSeleccionado;
	}

	public void setCodigoCarterizacionSeleccionado(
			String codigoCarterizacionSeleccionado) {
		this.codigoCarterizacionSeleccionado = codigoCarterizacionSeleccionado;
	}

	public String getCodigoEmpleadoSeleccionado() {
		return codigoEmpleadoSeleccionado;
	}

	public void setCodigoEmpleadoSeleccionado(String codigoEmpleadoSeleccionado) {
		this.codigoEmpleadoSeleccionado = codigoEmpleadoSeleccionado;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getTerritorios() {
		return territorios;
	}

	public void setTerritorios(List<SelectItem> territorios) {
		this.territorios = territorios;
	}

	public List<SelectItem> getCodigosCarterizacion() {
		return codigosCarterizacion;
	}

	public void setCodigosCarterizacion(List<SelectItem> codigosCarterizacion) {
		this.codigosCarterizacion = codigosCarterizacion;
	}

	public List<ProductoCE> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<ProductoCE> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public List<Territorio> getListaTerritorios() {
		return listaTerritorios;
	}

	public void setListaTerritorios(List<Territorio> listaTerritorios) {
		this.listaTerritorios = listaTerritorios;
	}

	public List<Carterizacion> getListaCodigosCarterizacion() {
		return listaCodigosCarterizacion;
	}

	public void setListaCodigosCarterizacion(
			List<Carterizacion> listaCodigosCarterizacion) {
		this.listaCodigosCarterizacion = listaCodigosCarterizacion;
	}

	public List<Territorio> getListaTerritorio() {
		return listaTerritorio;
	}

	public void setListaTerritorio(List<Territorio> listaTerritorio) {
		this.listaTerritorio = listaTerritorio;
	}

	public List<Carterizacion> getListaDatosCarterizacion() {
		return listaDatosCarterizacion;
	}

	public void setListaDatosCarterizacion(
			List<Carterizacion> listaDatosCarterizacion) {
		this.listaDatosCarterizacion = listaDatosCarterizacion;
	}

	public List<CarterizacionVO> getListaCarterizacion() {
		return listaCarterizacion;
	}

	public void setListaCarterizacion(List<CarterizacionVO> listaCarterizacion) {
		this.listaCarterizacion = listaCarterizacion;
	}

	@Override
	public void ordenar(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
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
	

	
}
