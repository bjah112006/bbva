package com.ibm.bbva.ctacte.servicio.sfp.bean;

import com.ibm.bbva.ctacte.bean.ResponseBaseBean;

public class IBMActivacionParticipeResponse extends ResponseBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6369794823907268472L;
	private String codCentral;
	private String indicadorAsociacion;
	
	public IBMActivacionParticipeResponse() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public IBMActivacionParticipeResponse(boolean success) {
		super(success);
	}
	
	public String getCodCentral() {
		return codCentral;
	}

	public void setCodCentral(String codCentral) {
		this.codCentral = codCentral;
	}

	public String getIndicadorAsociacion() {
		return indicadorAsociacion;
	}

	public void setIndicadorAsociacion(String indicadorAsociacion) {
		this.indicadorAsociacion = indicadorAsociacion;
	}
	
}
