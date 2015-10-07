package com.ibm.bbva.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="TBL_CE_IBM_LDAP_TEMP", schema = "CONELE")
public class LdapTemp implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CODUSU")
	private String id;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="APEPAT")
	private String apellidoPaterno;
	
	@Column(name="APEMAT")
	private String apellidoMaterno;
	
	@Column(name="CORELEC")
	private String correoElectronico;
	
	@Column(name="CODCARGO")
	private String codigoCargo;
	
	@Column(name="CODOFI")
	private String codigoOficina;

	@Column(name="CODCARGOTEMP")
	private String codigoCargoTemp;
	
	@Column(name="CODOFITEMP")
	private String codigoOficinaTemp;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getCodigoCargo() {
		return codigoCargo;
	}

	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	public String getCodigoOficina() {
		return codigoOficina;
	}

	public void setCodigoOficina(String codigoOficina) {
		this.codigoOficina = codigoOficina;
	}

	public String getCodigoCargoTemp() {
		return codigoCargoTemp;
	}

	public void setCodigoCargoTemp(String codigoCargoTemp) {
		this.codigoCargoTemp = codigoCargoTemp;
	}

	public String getCodigoOficinaTemp() {
		return codigoOficinaTemp;
	}

	public void setCodigoOficinaTemp(String codigoOficinaTemp) {
		this.codigoOficinaTemp = codigoOficinaTemp;
	}
	
}

