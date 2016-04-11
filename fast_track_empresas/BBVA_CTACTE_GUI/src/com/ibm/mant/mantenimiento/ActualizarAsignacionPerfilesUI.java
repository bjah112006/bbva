package com.ibm.mant.mantenimiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.PerfilXNivel;
import com.ibm.bbva.ctacte.bean.PerfilXNivelPK;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.mant.tabla.vo.PerfilXNivelVO;

@ManagedBean(name="actualizarAsignacionPerfiles")
@ViewScoped
public class ActualizarAsignacionPerfilesUI extends AbstractMBean {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ActualizarAsignacionPerfilesUI.class);
	
	private List<SelectItem> lstNiveles;
	private List<SelectItem> lstPerfiles;
	private PerfilXNivel perfilXNivel;
	private PerfilXNivelVO perfilXNivelVO;
	private boolean modoActualizar;
	
	@EJB
	private PerfilDAO perfilDAO;
	@EJB
	private PerfilXNivelDAO perfilXNivelDAO;
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		
		lstNiveles = new ArrayList<SelectItem>();
		lstNiveles.add(new SelectItem(ConstantesAdmin.CODIGO_CAMPO_VACIO, "--seleccione--"));
		lstNiveles.add(new SelectItem("1", "Usuario"));
		lstNiveles.add(new SelectItem("2", "Puesto"));
		lstNiveles.add(new SelectItem("3", "Oficina"));
		
		lstPerfiles = Util.crearItems(perfilDAO.findAll(), true,"id","descripcion");
		
		load();
	}
	
	public void load() {
		perfilXNivel = (PerfilXNivel) getObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
		perfilXNivelVO = new PerfilXNivelVO();
		
		if (perfilXNivel != null){ // inicializa cuando ya existe.
			perfilXNivelVO.setTipoNivel(perfilXNivel.getId().getTipoNivel());
			perfilXNivelVO.setValor(perfilXNivel.getId().getValor());
			perfilXNivelVO.setPerfilId(perfilXNivel.getPerfil().getId());
			perfilXNivelVO.setFlagActivo(perfilXNivel.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO));
			
			modoActualizar = true;
		} else {
			perfilXNivelVO.setTipoNivel(Long.parseLong(ConstantesAdmin.CODIGO_CAMPO_VACIO));
			perfilXNivelVO.setValor("");
			perfilXNivelVO.setPerfilId(Integer.parseInt(ConstantesAdmin.CODIGO_CAMPO_VACIO));
			perfilXNivelVO.setFlagActivo(false);
			
			modoActualizar = false;
		}
	}
	
	public String guardar() {
		if(!esValido()) return null;
		
		try {
			PerfilXNivel perfilXNivelTmp;
			if (modoActualizar) {
				perfilXNivelTmp = perfilXNivelDAO.load(perfilXNivel.getId());
			} else {
				PerfilXNivelPK id = new PerfilXNivelPK();
				id.setTipoNivel(perfilXNivelVO.getTipoNivel().longValue());
				id.setValor(perfilXNivelVO.getValor());
				
				perfilXNivelTmp = new PerfilXNivel();
				perfilXNivelTmp.setId(id);
			}
			perfilXNivelTmp.setPerfil(perfilDAO.load(perfilXNivelVO.getPerfilId()));
			perfilXNivelTmp.setFlagActivo(perfilXNivelVO.isFlagActivo() ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			perfilXNivelTmp.setFechaRegistro(new Date());
			perfilXNivelTmp.setEmpleado((Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION));
			
			if (modoActualizar) {
				perfilXNivelTmp = perfilXNivelDAO.update(perfilXNivelTmp);
			} else {
				perfilXNivelTmp = perfilXNivelDAO.save(perfilXNivelTmp);
			}
			
			perfilXNivelDAO.actualizarPerfilEmpleados(perfilXNivelTmp, false);
		} catch(Throwable ex) {
			Throwable nestedException = ex;
			boolean entityExists = false;
			while (nestedException != null) {
				if (nestedException instanceof org.apache.openjpa.persistence.EntityExistsException) {
					entityExists = true;
					break;
				}
				nestedException = nestedException.getCause();
			}
			LOG.error("Error : " + ex.getMessage(), ex);
			if (entityExists) {
				mensajeError(null, "Ya existe una asignación para el mismo nivel y valor.");
			} else {
				mensajeError(null, "Error en BBDD al intentar ejecutar la operación.");
			}
			return null;
		}
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		BusquedaAsignacionPerfilesUI busquedaAsignacionPerfiles = (BusquedaAsignacionPerfilesUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "busquedaAsignacionPerfiles");
		busquedaAsignacionPerfiles.limpiar();
		busquedaAsignacionPerfiles.init();
		removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
		
		return "/mantenimiento/asignacionPerfiles/busqueda?faces-redirect=true";
	}
	
	public String cancelar(){
		LOG.info("cancelar()");
		FacesContext ctx = FacesContext.getCurrentInstance();
		BusquedaAsignacionPerfilesUI busquedaAsignacionPerfiles = (BusquedaAsignacionPerfilesUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "busquedaAsignacionPerfiles");
		busquedaAsignacionPerfiles.init();
		removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
		
		return "/mantenimiento/asignacionPerfiles/busqueda?faces-redirect=true";
	}
	
	public boolean esValido() {
		boolean isValid = true;
		String idFormulario = "form";
		
		if (perfilXNivelVO.getTipoNivel().longValue() == Long.parseLong(ConstantesAdmin.CODIGO_CAMPO_VACIO)) {
			addComponentMessageFromPropertiesFile(idFormulario + ":nivel", "com.ibm.bbva.common.AsigPerfil.msg.TipoNivel.Obligatorio");
			isValid = false;
		}
		if (perfilXNivelVO.getValor() == null || perfilXNivelVO.getValor().trim().equals("")) {
			addComponentMessageFromPropertiesFile(idFormulario + ":valor", "com.ibm.bbva.common.AsigPerfil.msg.Valor.Obligatorio");
			isValid = false;
		}
		if (perfilXNivelVO.getPerfilId().intValue() == Integer.parseInt(ConstantesAdmin.CODIGO_CAMPO_VACIO)) {
			addComponentMessageFromPropertiesFile(idFormulario + ":perfil", "com.ibm.bbva.common.AsigPerfil.msg.Perfil.Obligatorio");
			isValid = false;
		}
		LOG.info("esValido="+isValid);
		
		return isValid;
	}

	public List<SelectItem> getLstNiveles() {
		return lstNiveles;
	}

	public void setLstNiveles(List<SelectItem> lstNiveles) {
		this.lstNiveles = lstNiveles;
	}

	public List<SelectItem> getLstPerfiles() {
		return lstPerfiles;
	}

	public void setLstPerfiles(List<SelectItem> lstPerfiles) {
		this.lstPerfiles = lstPerfiles;
	}

	public PerfilXNivel getPerfilXNivel() {
		return perfilXNivel;
	}

	public void setPerfilXNivel(PerfilXNivel perfilXNivel) {
		this.perfilXNivel = perfilXNivel;
	}

	public PerfilXNivelVO getPerfilXNivelVO() {
		return perfilXNivelVO;
	}

	public void setPerfilXNivelVO(PerfilXNivelVO perfilXNivelVO) {
		this.perfilXNivelVO = perfilXNivelVO;
	}

	public boolean isModoActualizar() {
		return modoActualizar;
	}

	public void setModoActualizar(boolean modoActualizar) {
		this.modoActualizar = modoActualizar;
	}
	
}
