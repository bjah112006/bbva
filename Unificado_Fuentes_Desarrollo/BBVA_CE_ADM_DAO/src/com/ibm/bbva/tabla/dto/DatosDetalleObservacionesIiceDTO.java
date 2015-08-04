package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosDetalleObservacionesIiceDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;

	private String nroObservacion;
	private String codUsuario;
	private String nombreUsuario;
	private String estadoExpediente;
	private String fechaHora;
	private String terminal;
	private String detalleObservacion;
	private String nroExpediente;
	private String tipo;
	private String nombrePerfil;
	private String nombreEmpleado;
	private String apePatEmpleado;
	private String apeMatEmpleado;
	
	public String getNroObservacion() {
		return nroObservacion;
	}
	public void setNroObservacion(String nroObservacion) {
		this.nroObservacion = nroObservacion;
	}
	public String getCodUsuario() {
		return codUsuario;
	}
	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getEstadoExpediente() {
		return estadoExpediente;
	}
	public void setEstadoExpediente(String estadoExpediente) {
		this.estadoExpediente = estadoExpediente;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getDetalleObservacion() {
		return detalleObservacion;
	}
	public void setDetalleObservacion(String detalleObservacion) {
		this.detalleObservacion = detalleObservacion;
	}
	public String getNroExpediente() {
		return nroExpediente;
	}
	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombrePerfil() {
		return nombrePerfil;
	}
	public void setNombrePerfil(String nombrePerfil) {
		this.nombrePerfil = nombrePerfil;
	}
	public String getNombreEmpleado() {
		return nombreEmpleado;
	}
	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}
	public String getApePatEmpleado() {
		return apePatEmpleado;
	}
	public void setApePatEmpleado(String apePatEmpleado) {
		this.apePatEmpleado = apePatEmpleado;
	}
	public String getApeMatEmpleado() {
		return apeMatEmpleado;
	}
	public void setApeMatEmpleado(String apeMatEmpleado) {
		this.apeMatEmpleado = apeMatEmpleado;
	}
	
	
	
}
