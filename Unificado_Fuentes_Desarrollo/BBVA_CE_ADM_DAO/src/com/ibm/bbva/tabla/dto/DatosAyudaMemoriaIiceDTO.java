package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosAyudaMemoriaIiceDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;

	
	private String nroExpediente;
	private String idAyuda;
	private String detalleAyuda;
	private String codUsuario;
	private String nombreUsuario;
	private String estadoExpediente;
	private String fechaHora;
	private String perfilUsuario;
	
	public String getNroExpediente() {
		return nroExpediente;
	}
	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}
	public String getIdAyuda() {
		return idAyuda;
	}
	public void setIdAyuda(String idAyuda) {
		this.idAyuda = idAyuda;
	}
	public String getDetalleAyuda() {
		return detalleAyuda;
	}
	public void setDetalleAyuda(String detalleAyuda) {
		this.detalleAyuda = detalleAyuda;
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
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
	
}
