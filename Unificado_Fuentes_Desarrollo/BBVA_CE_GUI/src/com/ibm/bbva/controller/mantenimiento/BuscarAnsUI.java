package com.ibm.bbva.controller.mantenimiento;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.util.Util;

@ManagedBean(name = "buscarAns")
@ViewScoped
public class BuscarAnsUI extends AbstractSortPagDataTableMBean {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(BuscarAnsUI.class);
	
	private List<SelectItem> tareas;
	private String tareaSeleccionada = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
	private List<Ans> listaAns;
	
	@EJB
	private AnsBeanLocal ansBean;
	@EJB
	private TareaBeanLocal tareaBean;

	public BuscarAnsUI() {
		super();
	}
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		
		tareas = Util.crearItems(tareaBean.buscarTodos(), true, "id", "descripcion");
		
		load();
		limpiar();
	}
	
	public void load() {
		listaAns = ansBean.buscarTodos();
	}
	
	public String limpiar() {
		this.tareaSeleccionada = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		for (Ans ans : listaAns) {
			ans.setSeleccionado(false);
		}
		
		removeObjectSession(Constantes.MANT_ANS_SESION);
		
		return null;
	}
	
	public String buscar() {
		Ans objConsulta = new Ans();
		
		if (tareaSeleccionada != null && !tareaSeleccionada.trim().equals("") && !tareaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			objConsulta.setTarea(new Tarea());
			objConsulta.getTarea().setId(Long.parseLong(tareaSeleccionada));
		}
		
		listaAns = ansBean.buscarConsultaParametrizable(objConsulta);
		pageFirst(null);
		
		return null;
	}
	
	public String agregar() {
		removeObjectSession(Constantes.MANT_ANS_SESION);
		return "/mantenimiento/formManActualizaAns?faces-redirect=true";
	}
	
	public String actualizar() {
		boolean itemSeleccionado = false;
		Ans objSeleccionado = null;
		for (Ans ans : listaAns) {
			if (ans.isSeleccionado()) {
				objSeleccionado = ans;
				itemSeleccionado = true;
				break;
			}
		}
		if (itemSeleccionado) {
			addObjectSession(Constantes.MANT_ANS_SESION, objSeleccionado);
			return "/mantenimiento/formManActualizaAns?faces-redirect=true";
		} else {
			removeObjectSession(Constantes.MANT_ANS_SESION);
			super.addComponentMessage(null, "Debe seleccionar un registro de la tabla.");
			
			return null;
		}
	}
	
	public String eliminar() {
		boolean itemSeleccionado = false;
		Ans objSeleccionado = null;
		for (Ans ans : listaAns) {
			if (ans.isSeleccionado()) {
				itemSeleccionado = true;
				objSeleccionado = ans;
				ans.setSeleccionado(false);
				listaAns.remove(ans);
				break;
			}
		}
		if (itemSeleccionado) {
			try {
				ansBean.remove(objSeleccionado);
			} catch (Throwable t) {
				LOG.error(t.getMessage(), t);
				super.addComponentMessage(null, "Error en BBDD al intentar ejecutar la operación.");
			}
			
			removeObjectSession(Constantes.MANT_ANS_SESION);
		}
		return null;
	}
	
	public String cancelar() {
		removeObjectSession(Constantes.MANT_ANS_SESION);
		
		return "/mantenimiento/formManTablasMaestras?faces-redirect=true";
	}
	
	@Override
	public void ordenar(ActionEvent event) {
		// no se usa
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// no se usa
	}

	public List<SelectItem> getTareas() {
		return tareas;
	}

	public void setTareas(List<SelectItem> tareas) {
		this.tareas = tareas;
	}

	public String getTareaSeleccionada() {
		return tareaSeleccionada;
	}

	public void setTareaSeleccionada(String tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}

	public List<Ans> getListaAns() {
		return listaAns;
	}

	public void setListaAns(List<Ans> listaAns) {
		this.listaAns = listaAns;
	}

}
