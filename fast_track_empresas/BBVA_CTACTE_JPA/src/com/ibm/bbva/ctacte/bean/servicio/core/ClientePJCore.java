package com.ibm.bbva.ctacte.bean.servicio.core;

import java.io.Serializable;
import java.util.Date;

import com.ibm.bbva.ctacte.bean.servicio.sfp.DatosClientePJSFP;

public class ClientePJCore implements Serializable {

	private static final long serialVersionUID = 534590594040392429L;

	private String codigoCentral;
	private String tipoDOI;
	private String descripcionDOI;
	private String numeroDOI;
	private String razonSocial;
	private Date fechaConstitucion;
	private DatosClientePJSFP datosSFP;
	private DatosClientePJCore datosCore;

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getTipoDOI() {
		return tipoDOI;
	}

	public void setTipoDOI(String tipoDOI) {
		this.tipoDOI = tipoDOI;
	}

	public String getDescripcionDOI() {
		return descripcionDOI;
	}

	public void setDescripcionDOI(String descripcionDOI) {
		this.descripcionDOI = descripcionDOI;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Date getFechaConstitucion() {
		return fechaConstitucion;
	}

	public void setFechaConstitucion(Date fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}

	public DatosClientePJSFP getDatosSFP() {
		return datosSFP;
	}

	public void setDatosSFP(DatosClientePJSFP datosSFP) {
		this.datosSFP = datosSFP;
	}

	public DatosClientePJCore getDatosCore() {
		return datosCore;
	}

	public void setDatosCore(DatosClientePJCore datosCore) {
		this.datosCore = datosCore;
	}
	
}
