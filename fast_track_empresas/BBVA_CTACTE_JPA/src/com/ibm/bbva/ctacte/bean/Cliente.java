package com.ibm.bbva.ctacte.bean;
// default package
// Generated 17/05/2012 03:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Cliente generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_CLIENTE_CC", schema = "CONELE")
public class Cliente implements java.io.Serializable {
	
	private static final long serialVersionUID = 1678278869359113033L;

	@Id
	@SequenceGenerator(name = "seqCliente", sequenceName = "SEQ_CE_IBM_CLIENTE_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqCliente")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Integer id;
	
	@Column(name = "RAZON_SOCIAL", length = 60)
	private String razonSocial;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CONSTITUCION", length = 7)
	private Date fechaConstitucion;
	
	@Column(name = "NUMERO_DOI", length = 8)
	private String numeroDoi;
	
	@Column(name = "TIPO_DOI", length = 1)
	private String tipoDoi;
	
	@Column(name = "CODIGO_CENTRAL", length = 8)
	private String codigoCentral;
	
	@Column(name = "NUMERO_CELULAR_1", length = 14)
	private String numeroCelular1;
	
	@Column(name = "NUMERO_CELULAR_2", length = 14)
	private String numeroCelular2;
	
	@Column(name = "TIPO_DOI_DES", length = 30)
	private String tipoDoiDes;
	
	@Column(name = "SECTOR_COD", length = 3)
	private String sectorCod;
	
	@Column(name = "SECTOR_DES", length = 60)
	private String sectorDes;
	
	@Column(name = "ACTIVIDAD_ECONOMICA_COD", length = 11)
	private String actividadEconomicaCod;
	
	@Column(name = "ACTIVIDAD_ECONOMICA_DES", length = 60)
	private String actividadEconomicaDes;
	
	@Column(name = "DIRECCION", length = 60)
	private String direccion;
	
	@Column(name = "UBICACION", length = 60)
	private String ubicacion;
	
	@Column(name = "DEPARTAMENTO_COD", length = 2)
	private String departamentoCod;
	
	@Column(name = "DEPARTAMENTO_DES", length = 30)
	private String departamentoDes;
	
	@Column(name = "PROVINCIA_COD", length = 2)
	private String provinciaCod;
	
	@Column(name = "PROVINCIA_DES", length = 30)
	private String provinciaDes;
	
	@Column(name = "DISTRITO_COD", length = 3)
	private String distritoCod;
	
	@Column(name = "DISTRITO_DES", length = 30)
	private String distritoDes;
	
	@Column(name = "CORREO_ELECTRONICO_1", length = 50)
	private String correoElectronico1;
	
	@Column(name = "CORREO_ELECTRONICO_2", length = 50)
	private String correoElectronico2;
	
	@Column(name = "FLAG_EXP_MIGRADO", length = 1)
	private String flagExpMigrado;

	@Column(name = "FLAG_ORIGEN_SFP", length = 1)
	private String flagOrigenSFP;
	
	public Cliente() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getNumeroDoi() {
		return numeroDoi;
	}

	public void setNumeroDoi(String numeroDoi) {
		this.numeroDoi = numeroDoi;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public String getCodigoCentral() {
		return codigoCentral;
	}

	public void setCodigoCentral(String codigoCentral) {
		this.codigoCentral = codigoCentral;
	}

	public String getNumeroCelular1() {
		return numeroCelular1;
	}

	public void setNumeroCelular1(String numeroCelular1) {
		this.numeroCelular1 = numeroCelular1;
	}

	public String getNumeroCelular2() {
		return numeroCelular2;
	}

	public void setNumeroCelular2(String numeroCelular2) {
		this.numeroCelular2 = numeroCelular2;
	}

	public String getTipoDoiDes() {
		return tipoDoiDes;
	}

	public void setTipoDoiDes(String tipoDoiDes) {
		this.tipoDoiDes = tipoDoiDes;
	}

	public String getSectorCod() {
		return sectorCod;
	}

	public void setSectorCod(String sectorCod) {
		this.sectorCod = sectorCod;
	}

	public String getSectorDes() {
		return sectorDes;
	}

	public void setSectorDes(String sectorDes) {
		this.sectorDes = sectorDes;
	}

	public String getActividadEconomicaCod() {
		return actividadEconomicaCod;
	}

	public void setActividadEconomicaCod(String actividadEconomicaCod) {
		this.actividadEconomicaCod = actividadEconomicaCod;
	}

	public String getActividadEconomicaDes() {
		return actividadEconomicaDes;
	}

	public void setActividadEconomicaDes(String actividadEconomicaDes) {
		this.actividadEconomicaDes = actividadEconomicaDes;
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

	public String getDepartamentoCod() {
		return departamentoCod;
	}

	public void setDepartamentoCod(String departamentoCod) {
		this.departamentoCod = departamentoCod;
	}

	public String getDepartamentoDes() {
		return departamentoDes;
	}

	public void setDepartamentoDes(String departamentoDes) {
		this.departamentoDes = departamentoDes;
	}

	public String getProvinciaCod() {
		return provinciaCod;
	}

	public void setProvinciaCod(String provinciaCod) {
		this.provinciaCod = provinciaCod;
	}

	public String getProvinciaDes() {
		return provinciaDes;
	}

	public void setProvinciaDes(String provinciaDes) {
		this.provinciaDes = provinciaDes;
	}

	public String getDistritoCod() {
		return distritoCod;
	}

	public void setDistritoCod(String distritoCod) {
		this.distritoCod = distritoCod;
	}

	public String getDistritoDes() {
		return distritoDes;
	}

	public void setDistritoDes(String distritoDes) {
		this.distritoDes = distritoDes;
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

	public String getFlagExpMigrado() {
		return flagExpMigrado;
	}

	public void setFlagExpMigrado(String flagExpMigrado) {
		this.flagExpMigrado = flagExpMigrado;
	}

	public String getFlagOrigenSFP() {
		return flagOrigenSFP;
	}

	public void setFlagOrigenSFP(String flagOrigenSFP) {
		this.flagOrigenSFP = flagOrigenSFP;
	}

}