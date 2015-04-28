package com.ibm.bbva.ctacte.servicio.sfp.bean;


import java.util.Date;

import com.ibm.bbva.ctacte.bean.ResponseBaseBean;

public class IBMRealizarBastanteoINResponse extends ResponseBaseBean{

	/**
	 * 
	 */
	
	private String tipoOperacion;//OPERACION_CC
	private Date fechaConstitucion;// CLIENTE_CC
	private String tipoDoi,numeroDoi,razonSocial,codigoCentral;//CLIENTE_CC
	private String tipoPJ;//EXPEDIENTE
	
	private CuentaSTD[] listaCuentas; //CUENTA_CC
	
	private static final long serialVersionUID = -7171967260270832168L;

	public IBMRealizarBastanteoINResponse() {
		// TODO Apéndice de constructor generado automáticamente
	}
	
	public IBMRealizarBastanteoINResponse(boolean success) {
		super(success);
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Date getFechaConstitucion() {
		return fechaConstitucion;
	}

	public void setFechaConstitucion(Date fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public String getNumeroDoi() {
		return numeroDoi;
	}

	public void setNumeroDoi(String numeroDoi) {
		this.numeroDoi = numeroDoi;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getTipoPJ() {
		return tipoPJ;
	}

	public void setTipoPJ(String tipoPJ) {
		this.tipoPJ = tipoPJ;
	}

	public CuentaSTD[] getListaCuentas() {
		return listaCuentas;
	}

	public void setListaCuentas(CuentaSTD[] listaCuentas) {
		this.listaCuentas = listaCuentas;
	}
	
	
}
