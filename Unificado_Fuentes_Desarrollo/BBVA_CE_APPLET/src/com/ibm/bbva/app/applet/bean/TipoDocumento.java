package com.ibm.bbva.app.applet.bean;

public class TipoDocumento implements Comparable {

	private String codigoDocumento;
	private String descripcion;
	private boolean obligatorio;

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

	public boolean isObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String toString() {
		return descripcion;
	}

	/* (sin Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return this.codigoDocumento.equals(((TipoDocumento)obj).getCodigoDocumento());
	}

	@Override
	public int compareTo(Object obj) {
		return this.getDescripcion().compareTo(((TipoDocumento)obj).getDescripcion());
	}

}
