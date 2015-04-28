package com.ibm.bbva.ctacte.bean.servicio.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CuentaCore implements Serializable {

	private static final long serialVersionUID = 3417264869130558078L;
	
	private String codigoProducto; 
	private String descProducto;
	private String codigoSubProducto;
	private String descSubProducto;
	private String numeroContrato;
	private String monedaCodigo;
	private String monedaDescripcion;
	private Date fechCreacionCtaCte;
	private String situacionCuenta;
	private List<ParticipeCore> participes = new ArrayList<ParticipeCore>();

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getDescProducto() {
		return descProducto;
	}

	public void setDescProducto(String descProducto) {
		this.descProducto = descProducto;
	}

	public String getCodigoSubProducto() {
		return codigoSubProducto;
	}

	public void setCodigoSubProducto(String codigoSubProducto) {
		this.codigoSubProducto = codigoSubProducto;
	}

	public String getDescSubProducto() {
		return descSubProducto;
	}

	public void setDescSubProducto(String descSubProducto) {
		this.descSubProducto = descSubProducto;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getMonedaCodigo() {
		return monedaCodigo;
	}

	public void setMonedaCodigo(String monedaCodigo) {
		this.monedaCodigo = monedaCodigo;
	}

	public String getMonedaDescripcion() {
		return monedaDescripcion;
	}

	public void setMonedaDescripcion(String monedaDescripcion) {
		this.monedaDescripcion = monedaDescripcion;
	}

	public Date getFechCreacionCtaCte() {
		return fechCreacionCtaCte;
	}

	public void setFechCreacionCtaCte(Date fechCreacionCtaCte) {
		this.fechCreacionCtaCte = fechCreacionCtaCte;
	}

	public String getSituacionCuenta() {
		return situacionCuenta;
	}

	public void setSituacionCuenta(String situacionCuenta) {
		this.situacionCuenta = situacionCuenta;
	}

	public List<ParticipeCore> getParticipes() {
		return participes;
	}

	public void setParticipes(List<ParticipeCore> participes) {
		this.participes = participes;
	}

}
