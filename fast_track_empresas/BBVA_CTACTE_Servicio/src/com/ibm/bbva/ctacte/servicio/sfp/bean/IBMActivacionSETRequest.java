package com.ibm.bbva.ctacte.servicio.sfp.bean;

public class IBMActivacionSETRequest {

	private String codCentral;
	private String indicadorActivacion;
	private String descripcionError;
	
	public IBMActivacionSETRequest() {
		// TODO Ap�ndice de constructor generado autom�ticamente
	}

	public String getCodCentral() {
		return codCentral;
	}

	public void setCodCentral(String codCentral) {
		this.codCentral = codCentral;
	}

	public String getIndicadorActivacion() {
		return indicadorActivacion;
	}

	public void setIndicadorActivacion(String indicadorActivacion) {
		this.indicadorActivacion = indicadorActivacion;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	
	
}
