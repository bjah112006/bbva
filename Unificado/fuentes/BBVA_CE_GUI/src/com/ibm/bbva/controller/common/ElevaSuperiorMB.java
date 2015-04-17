package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.EjecutarEvalCrediticiaMB;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.TipoCategoria;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.TipoCategoriaBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "elevaSuperior")
@RequestScoped
public class ElevaSuperiorMB extends AbstractMBean {
	@EJB
	private TipoCategoriaBeanLocal tipoCategoriaBean;
	@EJB
	private EmpleadoBeanLocal empleadoBean;

	private boolean mostrarDialogo = false; // inicialmente no se muestra
	private List<SelectItem> listaTipos;
	private String tipoSeleccionado;
	private String titulo;
	private HtmlPanelGroup dialogoModal;
	private String nombreFormPadre;
	
	private static final Logger LOG = LoggerFactory.getLogger(ElevaSuperiorMB.class);
	
	public ElevaSuperiorMB() {
	}
	
	@PostConstruct
	public void init() {
		String nombJSP = getNombreJSPPrincipal();
		nombreFormPadre = nombJSP.replaceFirst("form", "frm");
		
		/*Obtener tipos de categorias*/
		List<TipoCategoria> listTipoCategoria = tipoCategoriaBean.buscarTodos();
		List<Empleado> lista = new ArrayList<Empleado>();
		
		/*Cargar lista de Superiores*/
		for (TipoCategoria tipoCatego : listTipoCategoria) {
			if (tipoCatego.getFlagSuperior().equals(Constantes.FLAG_SUPERIOR_1)) {
				List<Empleado> listEmpleado = empleadoBean
						.buscarPorIdTipoCategoria(tipoCatego.getId(), Constantes.ID_PERFIL_RIESGOS_SUPERIOR);
				if (listEmpleado != null && !listEmpleado.isEmpty()) {
					LOG.info("Tamaño de listEmpleado con idCategoria "+tipoCatego.getId()+" es "+listEmpleado.size());
					lista.addAll(listEmpleado);
				}else
					LOG.info("listEmpleado con idCategoria "+tipoCatego.getId()+" es vacío");
			}
		}
		if(lista!=null)
			LOG.info("Tamaño de lista es "+lista.size());
		else
			LOG.info("Lista es nulo");
		
		listaTipos = Util.crearItems(lista, true, "id", "nombresCompletos");
	}

	public boolean isMostrarDialogo() {
		return mostrarDialogo;
	}

	public void setMostrarDialogo(boolean mostrarDialogo) {
		this.mostrarDialogo = mostrarDialogo;
	}

	public List<SelectItem> getListaTipos() {
		return listaTipos;
	}

	public void setListaTipos(List<SelectItem> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public String getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(String tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
		LOG.info("tipoSeleccionado = "+this.tipoSeleccionado);
	}
	
	private boolean esValido () {
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formEjecutarEvalCrediticia")) {
			formulario = "frmEjecutarEvalCrediticia";
		}
		
		boolean existeError = false;
		if (tipoSeleccionado==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(tipoSeleccionado)) {
			addMessageError(formulario + ":elevaSuperior", 
					"com.ibm.bbva.common.elevaSuperior.msg.elevaSuperior");
			existeError = true;
		}
		return !existeError;
	}
	
	public String aceptar () {
		if (!esValido ()) {
			mostrarDialogo = true;
		} else {
			String jspPrinc = getNombreJSPPrincipal();
			if (jspPrinc.equals("formEjecutarEvalCrediticia")) {				
				// se ejecuta la accion para guardar los datos
				ELContext elContext = FacesContext.getCurrentInstance().getELContext();
				EjecutarEvalCrediticiaMB ejecutarEvalCrediticiaMB = (EjecutarEvalCrediticiaMB) FacesContext
						.getCurrentInstance().getApplication().getELResolver()
						.getValue(elContext, null, "ejecutarEvalCrediticia");
				LOG.info("tipoSeleccionado = "+tipoSeleccionado);
				String result=ejecutarEvalCrediticiaMB.guardarElevaSup(tipoSeleccionado);
				mostrarDialogo = false;
				LOG.info("result::::"+result);
				if(result!=null)
					return result;
			}
			return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		}
		return null;
	}
	
	public String cancelar () {
		mostrarDialogo = false;
		return null;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public HtmlPanelGroup getDialogoModal() {
		return dialogoModal;
	}

	public void setDialogoModal(HtmlPanelGroup dialogoModal) {
		this.dialogoModal = dialogoModal;
	}

	public String getNombreFormPadre() {
		return nombreFormPadre;
	}

	public void setNombreFormPadre(String nombreFormPadre) {
		this.nombreFormPadre = nombreFormPadre;
	}
}
