package com.ibm.bbva.ctacte.bean.servicio.sfp;

import java.io.Serializable;

public class RepresentanteSFP implements Serializable {

	private static final long serialVersionUID = 2599938297895330608L;
	
	private String tipoDOI;
	private String numeroDOI;
	private String codigoCentral;
	private String nombres;
	private String apellidoMaterno;
	private String apellidoPaterno;
	private String indFirma;
	private String situacion;
	private String cargo;
	private String vigencia;

	public String getTipoDOI() {
		return tipoDOI;
	}

	public void setTipoDOI(String tipoDOI) {
		this.tipoDOI = tipoDOI;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	public String getVigencia() {
		return vigencia;
	}

	public void setIndFirma(String indFirma) {
		this.indFirma = indFirma;
	}

	public String getIndFirma() {
		return indFirma;
	}

	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public String getSituacion() {
		return situacion;
	}

}
