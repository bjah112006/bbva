package com.ibm.bbva.ctacte.servicio.sfp.bean;

import java.io.Serializable;
import java.util.Date;

public class CuentaSTD implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2836086669226711851L;
	private String productoCod;
	private String productoDes;
	private String subproductoCod;
	private String subproductoDes;
	private String numeroContrato;
	private String monedaCod;
	private String monedaDes;
	private Date fechaCreacion;
	private String situacionCuenta;
	private ParticipeSTD[] participeXCuenta;
	
	public CuentaSTD() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public String getProductoCod() {
		return productoCod;
	}

	public void setProductoCod(String productoCod) {
		this.productoCod = productoCod;
	}

	public String getProductoDes() {
		return productoDes;
	}

	public void setProductoDes(String productoDes) {
		this.productoDes = productoDes;
	}

	public String getSubproductoCod() {
		return subproductoCod;
	}

	public void setSubproductoCod(String subproductoCod) {
		this.subproductoCod = subproductoCod;
	}

	public String getSubproductoDes() {
		return subproductoDes;
	}

	public void setSubproductoDes(String subproductoDes) {
		this.subproductoDes = subproductoDes;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getMonedaCod() {
		return monedaCod;
	}

	public void setMonedaCod(String monedaCod) {
		this.monedaCod = monedaCod;
	}

	public String getMonedaDes() {
		return monedaDes;
	}

	public void setMonedaDes(String monedaDes) {
		this.monedaDes = monedaDes;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getSituacionCuenta() {
		return situacionCuenta;
	}

	public void setSituacionCuenta(String situacionCuenta) {
		this.situacionCuenta = situacionCuenta;
	}

	public ParticipeSTD[] getParticipeXCuenta() {
		return participeXCuenta;
	}

	public void setParticipeXCuenta(ParticipeSTD[] participeXCuenta) {
		this.participeXCuenta = participeXCuenta;
	}
	
	
	
}
