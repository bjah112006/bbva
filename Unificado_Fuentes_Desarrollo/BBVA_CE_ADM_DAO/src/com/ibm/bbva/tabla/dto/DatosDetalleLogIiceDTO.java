package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosDetalleLogIiceDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;

	private String nroLog;
	private String codUsuario;
	private String nombreUsuario;
	private String perfilUsuario;
	private String estadoExpediente;
	private String fechaHoraEnvio;
	private String fechaHoraAtencion;
	private String eventoRealizado;
	private String nroExpediente;
	private String terminal;
	private String nomrePerfil;
	
	public String getNroLog() {
		return nroLog;
	}
	public void setNroLog(String nroLog) {
		this.nroLog = nroLog;
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
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
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
	public String getEventoRealizado() {
		return eventoRealizado;
	}
	public void setEventoRealizado(String eventoRealizado) {
		this.eventoRealizado = eventoRealizado;
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
	public String getNomrePerfil() {
		return nomrePerfil;
	}
	public void setNomrePerfil(String nomrePerfil) {
		this.nomrePerfil = nomrePerfil;
	}
	
	
}
