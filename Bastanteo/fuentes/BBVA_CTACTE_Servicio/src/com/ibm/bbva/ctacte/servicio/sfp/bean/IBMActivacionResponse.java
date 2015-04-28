package com.ibm.bbva.ctacte.servicio.sfp.bean;

import com.ibm.bbva.ctacte.bean.ResponseBaseBean;

public class IBMActivacionResponse extends ResponseBaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7023284954586929323L;
	private String tipoOperacion;
	private String resultadoBastanteo;
	private IBMActivacionParticipeResponse[] participes;
	
	public IBMActivacionResponse() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public IBMActivacionResponse(boolean success) {
		super(success);
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getResultadoBastanteo() {
		return resultadoBastanteo;
	}

	public void setResultadoBastanteo(String resultadoBastanteo) {
		this.resultadoBastanteo = resultadoBastanteo;
	}

	public IBMActivacionParticipeResponse[] getParticipes() {
		return participes;
	}

	public void setParticipes(IBMActivacionParticipeResponse[] participes) {
		this.participes = participes;
	}
	
	
}
