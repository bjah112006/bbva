package com.ibm.bbva.ctacte.bean.servicio.core;

import java.io.Serializable;
import java.util.List;

public class DatosClientePJCore implements Serializable {

	private static final long serialVersionUID = -5381702563359880709L;
	
	private String tipoDOI;
	private String descTipoDOI;
	private String numeroDOI;
	private String codigoCentral;
	private String nombreRazonSocial;
	private String fechaConstitucion;
	private String sectorCodigo;
	private String sectorDescripcion;
	private String codigoActEconomica;
	private String descActEconomica;
	private String direccion;
	private String ubicacion;
	private String codigoDepartamento;
	private String descDepartamento;
	private String codigoProvincia;
	private String descProvincia;
	private String codigoDistrito;
	private String descDistrito;
	private String nroCelular1;
	private String nroCelular2;
	private String correoElectronico1;
	private String correoElectronico2;
	private List<CuentaCore> cuentas;

	public String getTipoDOI() {
		return tipoDOI;
	}

	public void setTipoDOI(String tipoDOI) {
		this.tipoDOI = tipoDOI;
	}

	public String getDescTipoDOI() {
		return descTipoDOI;
	}

	public void setDescTipoDOI(String descTipoDOI) {
		this.descTipoDOI = descTipoDOI;
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

	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}

	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}

	public String getFechaConstitucion() {
		return fechaConstitucion;
	}

	public void setFechaConstitucion(String fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}

	public String getSectorCodigo() {
		return sectorCodigo;
	}

	public void setSectorCodigo(String sectorCodigo) {
		this.sectorCodigo = sectorCodigo;
	}

	public String getSectorDescripcion() {
		return sectorDescripcion;
	}

	public void setSectorDescripcion(String sectorDescripcion) {
		this.sectorDescripcion = sectorDescripcion;
	}

	public String getCodigoActEconomica() {
		return codigoActEconomica;
	}

	public void setCodigoActEconomica(String codigoActEconomica) {
		this.codigoActEconomica = codigoActEconomica;
	}

	public String getDescActEconomica() {
		return descActEconomica;
	}

	public void setDescActEconomica(String descActEconomica) {
		this.descActEconomica = descActEconomica;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getDescDepartamento() {
		return descDepartamento;
	}

	public void setDescDepartamento(String descDepartamento) {
		this.descDepartamento = descDepartamento;
	}

	public String getCodigoProvincia() {
		return codigoProvincia;
	}

	public void setCodigoProvincia(String codigoProvincia) {
		this.codigoProvincia = codigoProvincia;
	}

	public String getDescProvincia() {
		return descProvincia;
	}

	public void setDescProvincia(String descProvincia) {
		this.descProvincia = descProvincia;
	}

	public String getCodigoDistrito() {
		return codigoDistrito;
	}

	public void setCodigoDistrito(String codigoDistrito) {
		this.codigoDistrito = codigoDistrito;
	}

	public String getDescDistrito() {
		return descDistrito;
	}

	public void setDescDistrito(String descDistrito) {
		this.descDistrito = descDistrito;
	}

	public String getNroCelular1() {
		return nroCelular1;
	}

	public void setNroCelular1(String nroCelular1) {
		this.nroCelular1 = nroCelular1;
	}

	public String getNroCelular2() {
		return nroCelular2;
	}

	public void setNroCelular2(String nroCelular2) {
		this.nroCelular2 = nroCelular2;
	}

	public String getCorreoElectronico1() {
		return correoElectronico1;
	}

	public void setCorreoElectronico1(String correoElectronico1) {
		this.correoElectronico1 = correoElectronico1;
	}

	public String getCorreoElectronico2() {
		return correoElectronico2;
	}

	public void setCorreoElectronico2(String correoElectronico2) {
		this.correoElectronico2 = correoElectronico2;
	}

	public List<CuentaCore> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<CuentaCore> cuentas) {
		this.cuentas = cuentas;
	}
}
