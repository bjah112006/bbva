package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosDetalleHistoricoIiceDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;

	private String nroHistorial;
	private String codUsuario;
	private String nombreUsuario;
	private String estadoExpediente;
	private String fechaHoraEnvio;
	private String fechaHoraAtencion;
	private String perfilUsuario;
	private String nroExpediente;
	private String terminal;
	private String nombrePerfil;
	private String nombreEmpleado;
	private String apePatEmpleado;
	private String apeMatEmpleado;
	private String fechaHoraTrabajo;
	private String flagDevolucion;
	private String codEstado;
	
	public String getNroHistorial() {
		return nroHistorial;
	}
	public void setNroHistorial(String nroHistorial) {
		this.nroHistorial = nroHistorial;
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
	public String getFechaHoraEnvio() {
		return fechaHoraEnvio;
	}
	public void setFechaHoraEnvio(String fechaHoraEnvio) {
		this.fechaHoraEnvio = fechaHoraEnvio;
	}
	public String getFechaHoraAtencion() {
		return fechaHoraAtencion;
	}
	public void setFechaHoraAtencion(String fechaHoraAtencion) {
		this.fechaHoraAtencion = fechaHoraAtencion;
	}
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
	public String getNroExpediente() {
		return nroExpediente;
	}
	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
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
	public String getFechaHoraTrabajo() {
		return fechaHoraTrabajo;
	}
	public void setFechaHoraTrabajo(String fechaHoraTrabajo) {
		this.fechaHoraTrabajo = fechaHoraTrabajo;
	}
	public String getFlagDevolucion() {
		return flagDevolucion;
	}
	public void setFlagDevolucion(String flagDevolucion) {
		this.flagDevolucion = flagDevolucion;
	}
	public String getCodEstado() {
		return codEstado;
	}
	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}
	
	
}
