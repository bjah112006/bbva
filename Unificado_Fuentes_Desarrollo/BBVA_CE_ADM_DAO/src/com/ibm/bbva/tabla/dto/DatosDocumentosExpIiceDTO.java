package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosDocumentosExpIiceDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;

	private String correlativoDocExp;
	private String correlativoDoc;
	private String correlativoTipoDoc;
	private String fechaCreacion;
	private String fechaModificacion;
	private String fechaVencimiento;
	private String tipoDocumentoCodigo;
	private String tipoDocumentoDescripcion;
	private String tipoDocumentoNombre;
	private String usuarioExpediente;
	private String perfilUsuarioExpediente;
	private String correlativoEstadoExpediente;
	private String flagAdjunto;
	
	
	public String getPerfilUsuarioExpediente() {
		return perfilUsuarioExpediente;
	}
	public void setPerfilUsuarioExpediente(String perfilUsuarioExpediente) {
		this.perfilUsuarioExpediente = perfilUsuarioExpediente;
	}
	public String getTipoDocumentoNombre() {
		return tipoDocumentoNombre;
	}
	public void setTipoDocumentoNombre(String tipoDocumentoNombre) {
		this.tipoDocumentoNombre = tipoDocumentoNombre;
	}
	public String getCorrelativoDocExp() {
		return correlativoDocExp;
	}
	public void setCorrelativoDocExp(String correlativoDocExp) {
		this.correlativoDocExp = correlativoDocExp;
	}
	public String getCorrelativoDoc() {
		return correlativoDoc;
	}
	public void setCorrelativoDoc(String correlativoDoc) {
		this.correlativoDoc = correlativoDoc;
	}
	public String getCorrelativoTipoDoc() {
		return correlativoTipoDoc;
	}
	public void setCorrelativoTipoDoc(String correlativoTipoDoc) {
		this.correlativoTipoDoc = correlativoTipoDoc;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public String getTipoDocumentoCodigo() {
		return tipoDocumentoCodigo;
	}
	public void setTipoDocumentoCodigo(String tipoDocumentoCodigo) {
		this.tipoDocumentoCodigo = tipoDocumentoCodigo;
	}
	public String getTipoDocumentoDescripcion() {
		return tipoDocumentoDescripcion;
	}
	public void setTipoDocumentoDescripcion(String tipoDocumentoDescripcion) {
		this.tipoDocumentoDescripcion = tipoDocumentoDescripcion;
	}
	public String getUsuarioExpediente() {
		return usuarioExpediente;
	}
	public void setUsuarioExpediente(String usuarioExpediente) {
		this.usuarioExpediente = usuarioExpediente;
	}
	public String getCorrelativoEstadoExpediente() {
		return correlativoEstadoExpediente;
	}
	public void setCorrelativoEstadoExpediente(String correlativoEstadoExpediente) {
		this.correlativoEstadoExpediente = correlativoEstadoExpediente;
	}
	public String getFlagAdjunto() {
		return flagAdjunto;
	}
	public void setFlagAdjunto(String flagAdjunto) {
		this.flagAdjunto = flagAdjunto;
	}
	
	
}
