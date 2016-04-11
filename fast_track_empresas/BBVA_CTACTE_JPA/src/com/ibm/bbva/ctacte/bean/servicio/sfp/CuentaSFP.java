package com.ibm.bbva.ctacte.bean.servicio.sfp;

import java.io.Serializable;
import java.util.List;

import com.ibm.bbva.ctacte.bean.servicio.core.ParticipeCore;

public class CuentaSFP implements Serializable {

	private static final long serialVersionUID = -8022694179981441811L;
	
	private String fechaCreacion;
	private String producto;
	private String subProducto;
	private String numeroCuenta;
	private String situacion;
	private String moneda;
	private String indicadorPoder;
	private List<ParticipeCore> partícipes;

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getSubProducto() {
		return subProducto;
	}

	public void setSubProducto(String subProducto) {
		this.subProducto = subProducto;
	}

	public List<ParticipeCore> getPartícipes() {
		return partícipes;
	}

	public void setPartícipes(List<ParticipeCore> partícipes) {
		this.partícipes = partícipes;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public String getSituacion() {
		return situacion;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setIndicadorPoder(String indicadorPoder) {
		this.indicadorPoder = indicadorPoder;
	}

	public String getIndicadorPoder() {
		return indicadorPoder;
	} 

}
