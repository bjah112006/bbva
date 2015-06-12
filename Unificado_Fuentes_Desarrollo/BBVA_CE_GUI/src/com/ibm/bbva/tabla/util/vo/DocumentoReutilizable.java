package com.ibm.bbva.tabla.util.vo;

public class DocumentoReutilizable {
	private Long id;
	private String descripcion;
	private boolean seleccion;
	private String codigoDocumento;
	private String idExpediente;
	private String idDocumentoExp;
	
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public String getIdExpediente() {
		return idExpediente;
	}
	public void setIdExpediente(String idExpediente) {
		this.idExpediente = idExpediente;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	public String getIdDocumentoExp() {
		return idDocumentoExp;
	}
	public void setIdDocumentoExp(String idDocumentoExp) {
		this.idDocumentoExp = idDocumentoExp;
	}
	
	
}
