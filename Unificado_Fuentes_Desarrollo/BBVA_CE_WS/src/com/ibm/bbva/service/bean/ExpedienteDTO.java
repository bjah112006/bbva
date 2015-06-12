package com.ibm.bbva.service.bean;

public class ExpedienteDTO {
	private Long codigoExpediente;
	private Long codigoNivel;
	private Long codigoTipoScoring;
	private Long codigoProducto;
	private Long codigoTipoMonedaSol;
	private Long codigoEstadoCivilTitular;
	private Long clasificacionBanco;
	private double clasificacionSbs;	
	private Long bancoConyuge;
	private double sbsConyuge;
	private double lineaConsumo;
	private double lineaCredAprob;
	private double lineaCredSol;
	private double porcentajeEndeudamiento;
	private double riesgoCliente;
	private String perExpPub;
	private String plazoSolicitado;
	private String flagSubRogado;
	
	public ExpedienteDTO(){
		
	}
	
	public Long getCodigoExpediente() {
		return codigoExpediente;
	}
	public void setCodigoExpediente(Long codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}
	public Long getCodigoNivel() {
		return codigoNivel;
	}
	public void setCodigoNivel(Long codigoNivel) {
		this.codigoNivel = codigoNivel;
	}
	public Long getCodigoTipoScoring() {
		return codigoTipoScoring;
	}
	public void setCodigoTipoScoring(Long codigoTipoScoring) {
		this.codigoTipoScoring = codigoTipoScoring;
	}
	public Long getCodigoProducto() {
		return codigoProducto;
	}
	public void setCodigoProducto(Long codigoProducto) {
		this.codigoProducto = codigoProducto;
	}
	public Long getCodigoTipoMonedaSol() {
		return codigoTipoMonedaSol;
	}
	public void setCodigoTipoMonedaSol(Long codigoTipoMonedaSol) {
		this.codigoTipoMonedaSol = codigoTipoMonedaSol;
	}
	public Long getCodigoEstadoCivilTitular() {
		return codigoEstadoCivilTitular;
	}
	public void setCodigoEstadoCivilTitular(Long codigoEstadoCivilTitular) {
		this.codigoEstadoCivilTitular = codigoEstadoCivilTitular;
	}
	public Long getClasificacionBanco() {
		return clasificacionBanco;
	}
	public void setClasificacionBanco(Long clasificacionBanco) {
		this.clasificacionBanco = clasificacionBanco;
	}
	public double getClasificacionSbs() {
		return clasificacionSbs;
	}
	public void setClasificacionSbs(double clasificacionSbs) {
		this.clasificacionSbs = clasificacionSbs;
	}
	public Long getBancoConyuge() {
		return bancoConyuge;
	}
	public void setBancoConyuge(Long bancoConyuge) {
		this.bancoConyuge = bancoConyuge;
	}
	public double getSbsConyuge() {
		return sbsConyuge;
	}
	public void setSbsConyuge(double sbsConyuge) {
		this.sbsConyuge = sbsConyuge;
	}
	public double getLineaConsumo() {
		return lineaConsumo;
	}
	public void setLineaConsumo(double lineaConsumo) {
		this.lineaConsumo = lineaConsumo;
	}
	public double getLineaCredAprob() {
		return lineaCredAprob;
	}
	public void setLineaCredAprob(double lineaCredAprob) {
		this.lineaCredAprob = lineaCredAprob;
	}
	public double getLineaCredSol() {
		return lineaCredSol;
	}
	public void setLineaCredSol(double lineaCredSol) {
		this.lineaCredSol = lineaCredSol;
	}
	public double getPorcentajeEndeudamiento() {
		return porcentajeEndeudamiento;
	}
	public void setPorcentajeEndeudamiento(double porcentajeEndeudamiento) {
		this.porcentajeEndeudamiento = porcentajeEndeudamiento;
	}
	public double getRiesgoCliente() {
		return riesgoCliente;
	}
	public void setRiesgoCliente(double riesgoCliente) {
		this.riesgoCliente = riesgoCliente;
	}
	public String getPerExpPub() {
		return perExpPub;
	}
	public void setPerExpPub(String perExpPub) {
		this.perExpPub = perExpPub;
	}
	public String getPlazoSolicitado() {
		return plazoSolicitado;
	}
	public void setPlazoSolicitado(String plazoSolicitado) {
		this.plazoSolicitado = plazoSolicitado;
	}

	public String getFlagSubRogado() {
		return flagSubRogado;
	}

	public void setFlagSubRogado(String flagSubRogado) {
		this.flagSubRogado = flagSubRogado;
	}
	
}
