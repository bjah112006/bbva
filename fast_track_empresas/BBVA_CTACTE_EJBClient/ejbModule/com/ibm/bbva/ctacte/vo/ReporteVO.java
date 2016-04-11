
package com.ibm.bbva.ctacte.vo;

import java.io.Serializable;
import java.util.Date;

public class ReporteVO implements Serializable {

	private static final long serialVersionUID = 487380015792437182L;

	private Date fechaInicio;
	private Date fechaFin;
	private String codigoCentral;
	private String numeroExpediente;
	private int idTarea;
	private int idEstadoTarea;
	private int idOperacion;
	private String responsable;
	private String razonSocial;
	private int idEstadoExpediente;
	private int idOficina;
	private int idTerritorio;
	

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

	

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getNumeroExpediente() {
		return numeroExpediente;
	}

	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public int getIdEstadoTarea() {
		return idEstadoTarea;
	}

	public void setIdEstadoTarea(int idEstadoTarea) {
		this.idEstadoTarea = idEstadoTarea;
	}

	public int getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(int idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public int getIdEstadoExpediente() {
		return idEstadoExpediente;
	}

	public void setIdEstadoExpediente(int idEstadoExpediente) {
		this.idEstadoExpediente = idEstadoExpediente;
	}

	public int getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(int idOficina) {
		this.idOficina = idOficina;
	}

	public int getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(int idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

}
