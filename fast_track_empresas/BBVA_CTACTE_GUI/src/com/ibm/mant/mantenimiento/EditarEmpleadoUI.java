package com.ibm.mant.mantenimiento;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.mant.tabla.vo.EmpleadoVO;

@ManagedBean(name="editarEmpleado")
@SessionScoped
public class EditarEmpleadoUI extends AbstractMBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -136804507367859259L;

	private static final Logger LOG = LoggerFactory.getLogger(EditarEmpleadoUI.class);
	
	@EJB
	private EmpleadoDAO empleadoDAO;
	
	@EJB
	private PerfilDAO perfilDAO;
	
	@EJB
	private OficinaDAO oficinaDAO;
	
	@EJB
	private EstudioAbogadoDAO estudioAbogadoDAO;
	
	@EJB
	private PerfilXNivelDAO perfilXNivelDAO;

	private List<SelectItem> lstPerfiles;
	private List<SelectItem> lstOficinas;
	private List<SelectItem> lstEstudioAbogados;
	
	private List<Perfil> perfiles;
	private List<Oficina> oficinas;
	private List<EstudioAbogado> estudioAbogados;  

	private boolean seleccion = false;
	
	private Empleado empleado;
	private EmpleadoVO empleadoVO;
	private Integer idEmpleado;
		
	/** SWITCHS **/
	
	private boolean fechaVigenciaDisabled = true;
	
	public EditarEmpleadoUI(){
		super();
	};
	
	@PostConstruct
	public void init() {
		
		/*Carga Lista de Productos*/
		lstPerfiles = Util.crearItems(perfilDAO.findAll(), true,"id","descripcion");
		lstOficinas = Util.crearItems(oficinaDAO.findAllOrderedByCodigo(true),true,"id","codigo,descripcion", "{0} {1}", true);
		lstEstudioAbogados = Util.crearItems(estudioAbogadoDAO.findAll(), true,"id","descripcion");
		
		LOG.info("Lista de perfiles : " + lstPerfiles.toString() + " de tamaño " + lstPerfiles.size());
		
		fechaVigenciaDisabled = true;
		seleccion = false;
		
		load();
	}
	
	/**
	 * Obtener datos
	 */
	
	public void load(){
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		empleado = (Empleado)getObjectSession(Constantes.EMPLEADO_SESION);
		empleadoVO = new EmpleadoVO();
		
		if (empleado != null){ // inicializa cuando ya existe.
			
			LOG.info("Entra al init de actualizar datos existentes");
			LOG.info("Id Empleado " + empleado.getId());
			
			idEmpleado = empleado.getId();
			
			if (empleado != null) {
				empleadoVO.setId(Long.valueOf(empleado.getId()));
				empleadoVO.setCodigo((empleado.getCodigo() != null)? empleado.getCodigo(): "");
				empleadoVO.setNombres((empleado.getNombres() != null)? empleado.getNombres(): "");
				empleadoVO.setApepat((empleado.getApepat() != null)? empleado.getApepat(): "");
				empleadoVO.setApemat((empleado.getApemat() != null)? empleado.getApemat(): "");
				empleadoVO.setPerfil((empleado.getPerfil() != null)? empleado.getPerfil().getId().toString() : Constantes.CODIGO_CODIGO_CAMPO_VACIO);
				empleadoVO.setOficina((empleado.getOficina() != null)? empleado.getOficina().getId().toString() : Constantes.CODIGO_CODIGO_CAMPO_VACIO);
				empleadoVO.setEstudioAbogado((empleado.getEstudio() != null)?empleado.getEstudio().getId().toString(): Constantes.CODIGO_CODIGO_CAMPO_VACIO);
				empleadoVO.setNombresCompletos((empleado.getNombresCompletos() != null)?empleado.getNombresCompletos(): "");
				empleadoVO.setCorreo((empleado.getCorreo() != null)? empleado.getCorreo() : "");
				if (empleado.getFlagActivo() != null)
					empleadoVO.setFlagActivo(empleado.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO));
				if (empleado.getFlagAbogado() != null)
					empleadoVO.setFlagAbogado(empleado.getFlagAbogado().equals(Constantes.CHECK_SELECCIONADO));
				empleadoVO.setNomcargo((empleado.getNomcargo() != null)? empleado.getNomcargo() : "");
				empleadoVO.setCodcargo((empleado.getCodcargo() != null)? empleado.getCodcargo() : "");
				empleadoVO.setFechaVigencia((empleado.getFechaVigencia() != null)? empleado.getFechaVigencia() : null);
			}
			
		}else{
			
			empleadoVO.setCodigo("");
			empleadoVO.setNombres("");
			empleadoVO.setApepat("");
			empleadoVO.setApemat("");
			empleadoVO.setPerfil(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			empleadoVO.setOficina(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			empleadoVO.setEstudioAbogado(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			empleadoVO.setNombresCompletos("");
			empleadoVO.setCorreo("");
			empleadoVO.setFlagActivo(false);
			empleadoVO.setFlagAbogado(false);
			empleadoVO.setNomcargo("");
			empleadoVO.setCodcargo("");
			empleadoVO.setFechaVigencia(null);
			
		}
		
		LOG.debug("FECHA VIGENCIA DISABLED : " + this.isFechaVigenciaDisabled());
		
		// 
	}
	
	/**
	 * Clear
	 */
	
	public void clear(){
		removeObjectSession(Constantes.EMPLEADO_SESION);
		
		empleadoVO = new EmpleadoVO();
		
		idEmpleado = null;

		empleadoVO.setCodigo("");
		empleadoVO.setNombres("");
		empleadoVO.setApepat("");
		empleadoVO.setApemat("");
		empleadoVO.setPerfil(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		empleadoVO.setOficina(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		empleadoVO.setEstudioAbogado(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		empleadoVO.setNombresCompletos("");
		empleadoVO.setCorreo("");
		empleadoVO.setFlagActivo(false);
		empleadoVO.setFlagAbogado(false);
		empleadoVO.setNomcargo("");
		empleadoVO.setCodcargo("");
		
		fechaVigenciaDisabled = true;
		seleccion = false;
	}
	
	/**
	 * Cancelar edición de 
	 * empleado.
	 * 
	 * @return
	 */
	public String cancelar(){
		LOG.debug("cancelar()");
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarEmpleadoUI buscarEmpleadoUI = (BuscarEmpleadoUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarEmpleado");
		buscarEmpleadoUI.init();
		removeObjectSession(Constantes.EMPLEADO_SESION);
		return "/mantenimiento/empleados/consultaTabla?faces-redirect=true";
	}
	
	/**
	 * Guardad entidad de
	 * empleado, si existe un idCargado entonces
	 * actualizar; sino, crearlo.
	 * 
	 * @param idCabecera
	 */
	public String guardar(){
		
		if(!esValido()) return "";
					
		LOG.debug("guardar()");
		
		Boolean error = false;
		
		if(empleado != null)
			LOG.debug("guardar() - Antigua oficina : " + empleado.getOficina().getId().toString());
		
		Empleado empleado = new Empleado();
		
		if(idEmpleado != null){
			// Existe un id cargado, preparar para 
			// actualización
			empleado = empleadoDAO.load(idEmpleado);
		}
		
		try{	
			
			empleado.setCodigo((empleadoVO.getCodigo() != null)? empleadoVO.getCodigo(): "");
			empleado.setNombres((empleadoVO.getNombres() != null)? empleadoVO.getNombres(): "");
			empleado.setApepat((empleadoVO.getApepat() != null)? empleadoVO.getApepat(): "");
			empleado.setApemat((empleadoVO.getApemat() != null)? empleadoVO.getApemat(): "");
			empleado.setCodcargo((empleadoVO.getCodcargo() != null)? empleadoVO.getCodcargo(): "");
			empleado.setNomcargo((empleadoVO.getNomcargo() != null)? empleadoVO.getNomcargo(): "");
			
//			if(!empleadoVO.getPerfil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
//				Perfil perfil = perfilDAO.load(Integer.parseInt(empleadoVO.getPerfil()));
//				empleado.setPerfil(perfil);
//			}else{
//				empleado.setPerfil(null);
//			}

			LOG.debug("guardar() - Obtener id de oficina seleccionado : " + empleadoVO.getOficina());
			
			if(!empleadoVO.getOficina().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
				
				LOG.debug("guardar() - Obtener objeto oficina ...");
				Oficina oficina = oficinaDAO.load(Integer.parseInt(empleadoVO.getOficina()));
				
				LOG.debug("guardar() - Establecer objeto oficina a empleado en edición ...");
				empleado.setOficina(oficina);
				
			}else{
				empleado.setOficina(null);
			}
			
			if(!empleadoVO.getEstudioAbogado().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
				EstudioAbogado estudioAbogado = estudioAbogadoDAO.load(Integer.parseInt(empleadoVO.getEstudioAbogado()));
				empleado.setEstudio(estudioAbogado);
			}else{
				empleado.setEstudio(null);
			}

			empleado.setNombresCompletos((empleadoVO.getNombresCompletos() != null)? empleadoVO.getNombresCompletos(): "");
			empleado.setCorreo((empleadoVO.getCorreo() != null)? empleadoVO.getCorreo() : "");
			
			empleado.setFlagActivo((empleadoVO.getFlagActivo())? "1":"0");
			empleado.setFlagAbogado((empleadoVO.isFlagAbogado())? "1":"0");
			
			empleado.setFechaVigencia((empleadoVO.getFechaVigencia() != null)? empleadoVO.getFechaVigencia() : null);
			
			// El perfil se obtiene de la tabla de asignación de perfiles
			Perfil perfil = perfilXNivelDAO.obtenerPerfilAsignado(empleado.getCodigo(), empleado.getCodcargo(), empleado.getOficina().getCodigo());
			empleado.setPerfil(perfil);
						
			if(idEmpleado != null)
				empleadoDAO.update(empleado);
			else
				empleadoDAO.save(empleado);
						
		}catch(Exception ex){
			LOG.error("Error : " + ex.getMessage(), ex);
			addErrorMessage(ex);
			error = true;
		}
		
		if(error){
			return "";
		}else{
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			BuscarEmpleadoUI buscarEmpleadoUI = (BuscarEmpleadoUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarEmpleado");
			buscarEmpleadoUI.limpiar();
			buscarEmpleadoUI.init();
		
			return "/mantenimiento/empleados/consultaTabla?faces-redirect=true";
		}
	}
	
	/**
	 * Validación de 
	 * campos
	 */
	
	public boolean esValido(){
		
		/**
		 * Revisar valores
		 */
		
		boolean isValid = true;
		
		// Código
		
		String idFormulario = "form";
		
		if(empleadoVO.getCodigo() == null){
			addComponentMessageFromPropertiesFile(idFormulario + ":codEmpleado", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
			isValid = false;
		}else{
			if(empleadoVO.getCodigo().trim().length() == 0){
				addComponentMessageFromPropertiesFile(idFormulario + ":codEmpleado", "com.ibm.bbva.common.Empleado.msg.Vacio");
				isValid = false;
			}
		}
		
		// Nombres
		
		if(empleadoVO.getNombres() == null){
			addComponentMessageFromPropertiesFile(idFormulario + ":nombres", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
			isValid = false;
		}else{
			if(empleadoVO.getNombres().trim().length() == 0){
				addComponentMessageFromPropertiesFile(idFormulario + ":nombres", "com.ibm.bbva.common.Empleado.msg.Vacio");
				isValid = false;
			}
		}
		
		// Apellido Paterno
		
		if(empleadoVO.getApepat() == null){
			addComponentMessageFromPropertiesFile(idFormulario + ":apePat", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
			isValid = false;
		}else{
			if(empleadoVO.getApepat().trim().length() == 0){
				addComponentMessageFromPropertiesFile(idFormulario + ":apePat", "com.ibm.bbva.common.Empleado.msg.Vacio");
				isValid = false;
			}
		}
		
		// Apellido Materno
		
		if(empleadoVO.getApemat() == null){
			addComponentMessageFromPropertiesFile(idFormulario + ":apeMat", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
			isValid = false;
		}else{
			if(empleadoVO.getApemat().trim().length() == 0){
				addComponentMessageFromPropertiesFile(idFormulario + ":apeMat", "com.ibm.bbva.common.Empleado.msg.Vacio");
				isValid = false;
			}
		}
		
		// Perfil
		
//		if(empleadoVO.getPerfil() == null || empleadoVO.getPerfil().equals("-1")){
//			addComponentMessageFromPropertiesFile(idFormulario + ":selectPerfil", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
//			isValid = false;
//		}
		
		// Oficina
		
		if(empleadoVO.getOficina() == null || empleadoVO.getOficina().equals("-1")){
			addComponentMessageFromPropertiesFile(idFormulario + ":selectOficinas", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
			isValid = false;
		}
		
		// Nombres completos
		
		if(empleadoVO.getNombresCompletos() == null){
			addComponentMessageFromPropertiesFile(idFormulario + ":nombresCompletos", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
			isValid = false;
		}else{
			if(empleadoVO.getNombresCompletos().trim().length() == 0){
				addComponentMessageFromPropertiesFile(idFormulario + ":nombresCompletos", "com.ibm.bbva.common.Empleado.msg.Vacio");
				isValid = false;
			}
		}
		
		// Flag Abogado
		
		// No es necesario validarlo, por defecto está deseleccionado y su valor es false.
					
		// Estudio Abogado (puede ser NULL si así lo deciden)
		
//		if(empleadoVO.getEstudioAbogado() == null || empleadoVO.getEstudioAbogado().equals("-1")){
//			addComponentMessageFromPropertiesFile(idFormulario + ":selectEstudioAbogados", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
//			isValid = false;
//		}
		
		// Fecha Vigencia
		
		if(!this.isFechaVigenciaDisabled()){
			if(empleadoVO.getFechaVigencia() == null){
				addComponentMessageFromPropertiesFile(idFormulario + ":fechaVigencia", "com.ibm.bbva.common.Empleado.msg.Obligatorio");
				isValid = false;
			}	
		}
				
		return isValid;
	}
	
	/**
	 * Eventos
	 * @return
	 */
	
	public void onChange_Oficina(String oficina){
		
		LOG.debug("EVENT : oficina cambiado ...");
		
		LOG.debug("OFICINA NUEVO : " + oficina);
		
		LOG.debug("OFICINA OLD : " + this.empleadoVO);
		
		if(this.empleadoVO != null){
			LOG.debug("OFICINA OLD 2 : " + this.empleadoVO.getOficina());
			
			if(this.empleadoVO.getOficina() != null){
				LOG.debug("OFICINA OLD 3 : " + this.empleadoVO.getOficina());
			}
		}
		
		if(empleado != null){
			if(!oficina.equals(empleado.getOficina().getId().toString()))
				this.setFechaVigenciaDisabled(false);
			else{
				this.empleadoVO.setFechaVigencia(empleado.getFechaVigencia());
				this.setFechaVigenciaDisabled(true);
			}
		}else{
			if(oficina.equals("-1")){
				this.empleadoVO.setFechaVigencia(null);
				this.setFechaVigenciaDisabled(true);
			}else{
				this.setFechaVigenciaDisabled(false);
			}
		}
	}
	
	/**
	 * GETTERS AND SETTERS
	 * @return
	 */

	public EmpleadoDAO getEmpleadoDAO() {
		return empleadoDAO;
	}

	public void setEmpleadoDAO(EmpleadoDAO empleadoDAO) {
		this.empleadoDAO = empleadoDAO;
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
	
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public EmpleadoVO getEmpleadoVO() {
		return empleadoVO;
	}

	public void setEmpleadoVO(EmpleadoVO empleadoVO) {
		this.empleadoVO = empleadoVO;
	}

	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public boolean isFechaVigenciaDisabled() {
		return fechaVigenciaDisabled;
	}

	public void setFechaVigenciaDisabled(boolean fechaVigenciaDisabled) {
		this.fechaVigenciaDisabled = fechaVigenciaDisabled;
	}	
}
