package com.ibm.mant.mantenimiento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.DatosCorreo;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.DatosCorreoDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="busquedaCorreo")
@ViewScoped
public class BusquedaCorreoUI extends AbstractSortPagDataTableMBean {
	
	private static final long serialVersionUID = 8680858789558780436L;
	
	private static final Logger LOG = LoggerFactory.getLogger(BusquedaCorreoUI.class);
	
	private List<SelectItem> perfiles;
	private List<SelectItem> tareas;
	private String perfilSeleccionado = ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO;
	private String tareaSeleccionada = ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO;
	private List<DatosCorreo> listaDatosCorreo;
	
	@EJB
	private PerfilDAO perfilDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private DatosCorreoDAO datosCorreoDAO;
	
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		
		perfiles = Util.crearItems(perfilDAO.buscarPerfilesConTareaAsignada(), true, "id", "descripcion");
		tareas = Util.crearItems(null, true, "id", "descripcion");
		
		load();
		limpiar();
	}
	
	public void seleccionarPerfil(AjaxBehaviorEvent event) {
		if (!ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(perfilSeleccionado)) {
			tareas = Util.crearItems(tareaDAO.findByPerfil(Integer.parseInt(perfilSeleccionado)), true, "id", "descripcion");
		} else {
			tareas = Util.crearItems(null, true, "id", "descripcion");
		}
	}
	
	public void load() {
		listaDatosCorreo = datosCorreoDAO.buscarTodos();
	}
	
	public String limpiar() {
		perfilSeleccionado = ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionada = ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO;
		tareas = Util.crearItems(null, true, "id", "descripcion");

		for (DatosCorreo datosCorreo : listaDatosCorreo) {
			datosCorreo.setSeleccionado(false);
		}
		
		removeObjectSession(Constantes.MANT_CORREO_SESION);
		
		return null;
	}
	
	public String buscar() {
		if (!ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(tareaSeleccionada)) {
			listaDatosCorreo = datosCorreoDAO.buscarPorTarea(Integer.parseInt(tareaSeleccionada));
		} else if (!ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(perfilSeleccionado)) {
			List<Integer> listaIdTareas = new ArrayList<Integer>();
			for (SelectItem item : tareas) {
				if (!item.getValue().toString().equals(ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO)) {
					listaIdTareas.add(Integer.parseInt(item.getValue().toString()));
				}
			}
			if (listaIdTareas.isEmpty()) {
				listaDatosCorreo = Collections.EMPTY_LIST; // no hay tareas para el perfil seleccionado
			} else {
				listaDatosCorreo = datosCorreoDAO.buscarPorTareas(listaIdTareas);
			}
		} else {
			load();
		}
		pageFirst(null);
		
		return null;
	}
	
	public String agregar() {
		removeObjectSession(Constantes.MANT_CORREO_SESION);
		return "/mantenimiento/correo/actualizacion?faces-redirect=true";
	}
	
	public String actualizar() {
		boolean itemSeleccionado = false;
		DatosCorreo objSeleccionado = null;
		for (DatosCorreo datosCorreo : listaDatosCorreo) {
			if (datosCorreo.isSeleccionado()) {
				objSeleccionado = datosCorreo;
				itemSeleccionado = true;
				break;
			}
		}
		if (itemSeleccionado) {
			addObjectSession(Constantes.MANT_CORREO_SESION, objSeleccionado);
			return "/mantenimiento/correo/actualizacion?faces-redirect=true";
		} else {
			removeObjectSession(Constantes.MANT_CORREO_SESION);
			super.addComponentMessage(null, "Debe seleccionar un registro de la tabla.");
			
			return null;
		}
	}
	
	public String eliminar() {
		boolean itemSeleccionado = false;
		DatosCorreo objSeleccionado = null;
		for (DatosCorreo datosCorreo : listaDatosCorreo) {
			if (datosCorreo.isSeleccionado()) {
				itemSeleccionado = true;
				objSeleccionado = datosCorreo;
				datosCorreo.setSeleccionado(false);
				listaDatosCorreo.remove(datosCorreo);
				break;
			}
		}
		if (itemSeleccionado) {
			try {
				datosCorreoDAO.eliminar(objSeleccionado.getId());
			} catch (Throwable t) {
				LOG.error(t.getMessage(), t);
				super.addComponentMessage(null, "Error en BBDD al intentar ejecutar la operación.");
			}
			
			removeObjectSession(Constantes.MANT_CORREO_SESION);
		}
		return null;
	}
	
	public String cancelar() {
		removeObjectSession(Constantes.MANT_CORREO_SESION);
		
		return "/mantenimiento/seleccionarTabla?faces-redirect=true";
	}

	@Override
	public void ordenar(ActionEvent event) {
	}

	public List<SelectItem> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<SelectItem> perfiles) {
		this.perfiles = perfiles;
	}

	public List<SelectItem> getTareas() {
		return tareas;
	}

	public void setTareas(List<SelectItem> tareas) {
		this.tareas = tareas;
	}

	public String getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(String perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public String getTareaSeleccionada() {
		return tareaSeleccionada;
	}

	public void setTareaSeleccionada(String tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}

	public List<DatosCorreo> getListaDatosCorreo() {
		return listaDatosCorreo;
	}

	public void setListaDatosCorreo(List<DatosCorreo> listaDatosCorreo) {
		this.listaDatosCorreo = listaDatosCorreo;
	}

}
