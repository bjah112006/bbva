package com.ibm.bbva.controller.common;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "reporteCSV")
@RequestScoped
public class ReporteCSVMB extends AbstractMBean {

	@EJB
	OficinaBeanLocal oficinaBean;
	
	private List<SelectItem> oficinas;
	private String oficinaSeleccionada;
	private Date fechaInicio;
	private Date fechaFin;
	private boolean datosCorrectos;

	private static final Logger LOG = LoggerFactory.getLogger(ReporteCSVMB.class);
	
	public ReporteCSVMB() {
		
	}
	
	@PostConstruct
	private void init() {
		oficinas = Util.crearItems(oficinaBean.buscarTodos(),true, "id", "descripcion");
	}

	public void crearReporte(ActionEvent ae) {
		if (esValido ()) {
			datosCorrectos = true;
		}
	}
	
	private boolean esValido () {
		boolean existeErrores = false;
		String formulario = "frmReporte";
		
		if (Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(oficinaSeleccionada)) {
			addMessageError(formulario + ":selProducto", "com.ibm.bbva.common.reporteCSV.msg.producto");
			existeErrores = true;
		}
		
		if (fechaInicio == null) {
			addMessageError(formulario + ":fecInicio", "com.ibm.bbva.common.reporteCSV.msg.fechaInicio");
			existeErrores = true;
		}
		
		if (fechaFin == null) {
			addMessageError(formulario + ":fecFin", "com.ibm.bbva.common.reporteCSV.msg.fechaFin");
			existeErrores = true;
		}
		
		if (fechaInicio!=null && fechaFin!=null && fechaFin.before(fechaInicio)){
			addMessageError(formulario + ":fechas", "com.ibm.bbva.common.reporteCSV.msg.fechas");
			existeErrores = true;
		}
		
		return !existeErrores;
	}

	public List<SelectItem> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<SelectItem> oficinas) {
		this.oficinas = oficinas;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getOficinaSeleccionada() {
		return oficinaSeleccionada;
	}

	public void setOficinaSeleccionada(String oficinaSeleccionada) {
		this.oficinaSeleccionada = oficinaSeleccionada;
	}

	public boolean isDatosCorrectos() {
		return datosCorrectos;
	}

	public void setDatosCorrectos(boolean datosCorrectos) {
		this.datosCorrectos = datosCorrectos;
	}

}
