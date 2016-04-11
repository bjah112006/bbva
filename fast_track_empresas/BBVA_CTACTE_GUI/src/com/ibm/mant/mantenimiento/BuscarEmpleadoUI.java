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
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.mant.tabla.vo.EmpleadoVO;

@ManagedBean(name="buscarEmpleado")
@SessionScoped
public class BuscarEmpleadoUI extends AbstractSortPagDataTableMBean{

	private static final long serialVersionUID = -1670459584966461384L;

	private static final Logger LOG = LoggerFactory.getLogger(BuscarEmpleadoUI.class);
	
	@EJB
	private EmpleadoDAO empleadoDAO;
	
	@EJB
	private PerfilDAO perfilDAO;
	
	@EJB
	private OficinaDAO oficinaDAO;
	
	@EJB
	private EstudioAbogadoDAO estudioAbogadoDAO;

	private List<SelectItem> lstPerfiles;
	private List<SelectItem> lstOficinas;
	private List<SelectItem> lstEstudioAbogados;
	
	private String perfilSeleccionado;
	private String oficinaSeleccionada;
	private String estudioAbogadoSeleccionado;
	private String codigoEmpleado;
	private String flagActivo = "1";

	private List<Perfil> perfiles;
	private List<Oficina> oficinas;
	private List<EstudioAbogado> estudioAbogados; 

	
	private boolean seleccion = false;
	private boolean habBotonActualizar;
	private boolean habBotonEliminar; 
	
	private HtmlDataTable tablaBinding;
	private String numRegistros;
	
	
	private List<Empleado> listaEmpleados;
	private List<EmpleadoVO> listaEmpleadosVO;
	
	
	private Empleado empleado;
	
	private List<Empleado> listaAuxEmpleado;


	private boolean itemSeleccionado;
	
	public BuscarEmpleadoUI(){
		super();
	};
	
	@PostConstruct
	public void init() {
		
		/*Carga Lista de Productos*/
		lstPerfiles = Util.crearItems(perfilDAO.findAll(), true,"id","descripcion");
		lstOficinas = Util.crearItems(oficinaDAO.findAllOrderedByCodigo(true),true,"id","codigo,descripcion", "{0} {1}", true);
		lstEstudioAbogados = Util.crearItems(estudioAbogadoDAO.findAll(), true,"id","descripcion");
		
		LOG.info("Lista de perfiles : " + lstPerfiles.toString() + " de tamaño " + lstPerfiles.size());
		
		habBotonActualizar=true;
		habBotonEliminar=false;
		
		load();
		limpiar();
	}
	
	/**
	 * Cargar tabla de resultados, por defecto
	 * carga todos los empleados
	 */
	
	public void load(){
		listaEmpleados = empleadoDAO.findAll();
		mostrarTabla(listaEmpleados);
	}
	
	/**
	 * Clear
	 */
	
	public String limpiar(){
		
		this.perfilSeleccionado = null;
		this.oficinaSeleccionada = null;
		this.estudioAbogadoSeleccionado = null;
		this.codigoEmpleado = null;
		this.flagActivo = "true";
		
		for(EmpleadoVO item : listaEmpleadosVO){
			item.setSeleccion(false);
		}
		
		return "/mantenimiento/empleados/consultaTabla?faces-redirect=true";
	}
	
	/**
	 * Buscar
	 */
	
	public String buscar(){
		
		LOG.debug("buscar()");
		
		Empleado empleado = new Empleado();
		
		if(perfilSeleccionado != null){
			Perfil perfil = perfilDAO.load(Integer.parseInt(perfilSeleccionado));
			empleado.setPerfil(perfil);
		}
		
		if(oficinaSeleccionada != null){
			Oficina oficina = oficinaDAO.load(Integer.parseInt(oficinaSeleccionada));
			empleado.setOficina(oficina);
		}
		
		if(estudioAbogadoSeleccionado != null){
			EstudioAbogado estudioAbogado = estudioAbogadoDAO.load(Integer.parseInt(estudioAbogadoSeleccionado));
			empleado.setEstudio(estudioAbogado);
		}
		
		if(codigoEmpleado != null){
			empleado.setCodigo(codigoEmpleado);
		}
		
		if(flagActivo != null){
			empleado.setFlagActivo(flagActivo);
		}
		
		listaEmpleados = empleadoDAO.search(empleado);
		mostrarTabla(listaEmpleados);
		
		return "/mantenimiento/empleados/consultaTabla?faces-redirect=true";
		
	}
	
	/**
	 * Mostrar tabla de empleados, convierte
	 * las entidades a VO
	 * 
	 * @param List<Empleado> listaEmpleados
	 */
	
	public void mostrarTabla(List<Empleado> listaEmpleados) {
		
 		itemSeleccionado=false;
 		
		if (listaEmpleados.size() > 0) {
			listaEmpleadosVO = new ArrayList<EmpleadoVO>();
			
			for (int i = 0; i < listaEmpleados.size(); i++) {
				
				Empleado empleado = listaEmpleados.get(i);
				EmpleadoVO empleadoVO = new EmpleadoVO();
				
				if (empleado != null) {
					
					//guiaDocumentaria.setSeleccion(false);
					empleadoVO.setId(Long.valueOf(empleado.getId()));
					
					empleadoVO.setCodigo(empleado.getCodigo());
					empleadoVO.setNombres(empleado.getNombres());
					empleadoVO.setApepat(empleado.getApepat());
					empleadoVO.setApemat(empleado.getApemat());
					
					LOG.debug("PERFIL ===> " + empleado.getPerfil());
					
					if(empleado.getPerfil() != null)
						empleadoVO.setPerfil(empleado.getPerfil().getDescripcion());
					
					if(empleado.getOficina() != null)
						empleadoVO.setOficina(empleado.getOficina().getDescripcion());
					
					if(empleado.getEstudio() != null)
						empleadoVO.setEstudioAbogado(empleado.getEstudio().getDescripcion());
					
					empleadoVO.setNombresCompletos(empleado.getNombresCompletos());
					empleadoVO.setCorreo(empleado.getCorreo());

					empleadoVO.setFlagActivo(empleado.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO));
					
					if(empleado.getFlagAbogado() != null)
						empleadoVO.setFlagAbogado(empleado.getFlagAbogado().equals(Constantes.CHECK_SELECCIONADO));
					
				}
				listaEmpleadosVO.add(empleadoVO);
			}
		} else {
			listaEmpleadosVO = new ArrayList<EmpleadoVO>();
			setListaEmpleadosVO(listaEmpleadosVO);
		}
	};
	
	/**
	 * Acción de botón 
	 * de cancelar
	 * 
	 * @return
	 */
	public String cancelar() {
		return "/mantenimiento/seleccionarTabla?faces-redirect=true";
	}
	
	/**
	 * Carga un empleado al ser
	 * seleccionado
	 * 
	 * @param idGuiaDocumentaria
	 * @param seleccion
	 */
	
	//public void cargarEmpleado(Long idEmpleado, boolean seleccion) {
	public void cargarEmpleado(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		int idEmpleado = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.id}", Integer.class).intValue();
		boolean seleccion = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.seleccion}", Boolean.class).booleanValue();
		LOG.info("idEmpleado: " + idEmpleado);
		LOG.info("seleccion: " + seleccion);
		
		itemSeleccionado = seleccion;
		
		if (seleccion){
			LOG.info("Entró al metodo Ajax");
			Empleado empleado = new Empleado();
			empleado = empleadoDAO.load(idEmpleado);
			LOG.info("Carga Empleado");
//			LOG.info("Id Empleado " + empleado.getId());
			addObjectSession(Constantes.EMPLEADO_SESION, empleado);
		} else {
			removeObjectSession(Constantes.EMPLEADO_SESION);
		}
	}
	
	/**
	 * Acción de botón de agregar,
	 * muestra el formulario para crear un nuevo empleado
	 * 
	 * @return
	 */
	public String agregar() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance();
		
		EditarEmpleadoUI editarEmpleadoUI = (EditarEmpleadoUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "editarEmpleado");
		editarEmpleadoUI.clear();
		editarEmpleadoUI.init();
		
		return "/mantenimiento/empleados/actualizaTabla?faces-redirect=true";
	}
	
	/**
	 * Acción de botón de actualizar,
	 * muestra el formulario para actualizar un empleado
	 */
	public String actualizar(){
		String idComponente="formManGuiaDocumentaria";
		if (itemSeleccionado){
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			EditarEmpleadoUI editarEmpleadoUI = (EditarEmpleadoUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "editarEmpleado");
			editarEmpleadoUI.init();
			return "/mantenimiento/empleados/actualizaTabla?faces-redirect=true";
		}else{	
			addComponentMessageFromPropertiesFile(idComponente + ":idTablaGuiaDocumentaria", "com.ibm.bbva.common.GuiaDocumentaria.msg.FailActualizar");
			return null;
		}
	}
	
	@Override
	public void ordenar(ActionEvent event) {
		
	}

	public PerfilDAO getPerfilDAO() {
		return perfilDAO;
	}

	public void setPerfilDAO(PerfilDAO perfilDAO) {
		this.perfilDAO = perfilDAO;
	}

	public OficinaDAO getOficinaDAO() {
		return oficinaDAO;
	}

	public void setOficinaDAO(OficinaDAO oficinaDAO) {
		this.oficinaDAO = oficinaDAO;
	}

	public EstudioAbogadoDAO getEstudioAbogadoDAO() {
		return estudioAbogadoDAO;
	}

	public void setEstudioAbogadoDAO(EstudioAbogadoDAO estudioAbogadoDAO) {
		this.estudioAbogadoDAO = estudioAbogadoDAO;
	}

	public List<SelectItem> getLstPerfiles() {
		return lstPerfiles;
	}

	public void setLstPerfiles(List<SelectItem> lstPerfiles) {
		this.lstPerfiles = lstPerfiles;
	}

	public List<SelectItem> getLstOficinas() {
		return lstOficinas;
	}

	public void setLstOficinas(List<SelectItem> lstOficinas) {
		this.lstOficinas = lstOficinas;
	}

	public List<SelectItem> getLstEstudioAbogados() {
		return lstEstudioAbogados;
	}

	public void setLstEstudioAbogados(List<SelectItem> lstEstudioAbogados) {
		this.lstEstudioAbogados = lstEstudioAbogados;
	}

	public String getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(String perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public List<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public List<Oficina> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public List<EstudioAbogado> getEstudioAbogados() {
		return estudioAbogados;
	}

	public void setEstudioAbogados(List<EstudioAbogado> estudioAbogados) {
		this.estudioAbogados = estudioAbogados;
	}

	public boolean isSeleccion() {
		return seleccion;
	}

	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}

	public boolean isHabBotonActualizar() {
		return habBotonActualizar;
	}

	public void setHabBotonActualizar(boolean habBotonActualizar) {
		this.habBotonActualizar = habBotonActualizar;
	}

	public boolean isHabBotonEliminar() {
		return habBotonEliminar;
	}

	public void setHabBotonEliminar(boolean habBotonEliminar) {
		this.habBotonEliminar = habBotonEliminar;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOficinaSeleccionada() {
		return oficinaSeleccionada;
	}

	public void setOficinaSeleccionada(String oficinaSeleccionada) {
		this.oficinaSeleccionada = oficinaSeleccionada;
	}

	public String getEstudioAbogadoSeleccionado() {
		return estudioAbogadoSeleccionado;
	}

	public void setEstudioAbogadoSeleccionado(String estudioAbogadoSeleccionado) {
		this.estudioAbogadoSeleccionado = estudioAbogadoSeleccionado;
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}
	
	/**
	 * Lista empleados a mostrarse
	 * en tabla
	 * 
	 */
	
	public List<Empleado> getListaEmpleados() {
		return listaEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listaEmpleados = listaEmpleados;
	}

	public List<EmpleadoVO> getListaEmpleadosVO() {
		return listaEmpleadosVO;
	}

	public void setListaEmpleadosVO(List<EmpleadoVO> listaEmpleadosVO) {
		this.listaEmpleadosVO = listaEmpleadosVO;
	}
	
	
}
