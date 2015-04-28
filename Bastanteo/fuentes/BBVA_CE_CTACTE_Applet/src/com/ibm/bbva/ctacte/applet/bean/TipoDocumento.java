package com.ibm.bbva.ctacte.applet.bean;

public class TipoDocumento {

	private String codigoDocumento;
	private String descripcion;

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String toString () {
		return descripcion;
	}
	
}
