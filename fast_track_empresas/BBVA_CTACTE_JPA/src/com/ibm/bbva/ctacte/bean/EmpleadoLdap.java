package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "EMPLEADO_LDAP_TMP", schema = "CONELE")
public class EmpleadoLdap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqEmpleadoLdap", sequenceName = "SEQ_CE_IBM_EMP_LDAP", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqEmpleadoLdap")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private int id;
	
	@Column(name = "COD_USUARIO", length = 10)
	private String codigoUsuario;

	@Column(name = "COD_OFICINA", length = 5)
	private String codigoOficina;

	@Column(name = "DESC_OFICINA", length = 100)
	private String descripcionOficina;

	@Column(name = "COD_ENT_USU", length = 100)
	private String codigoEntidad;

	@Column(name = "NOMB_EMPRESA", length = 100)
	private String nombreEmpresa;

	@Column(name = "FLAG_ESTADO", length = 100)
	private boolean estado;

	@Column(name = "NOMBRES", length = 100)
	private String nombres;

	@Column(name = "PRIMER_APE", length = 100)
	private String apellidoPaterno;

	@Column(name = "SEGUNDO_APE", length = 100)
	private String apellidoMaterno;

	@Column(name = "TIPO_DOCUMENTO", length = 3)
	private String tipoDocumento;

	@Column(name = "NRO_DOCUMENTO", length = 10)
	private String numeroDocumento;

	@Column(name = "DESC_PUESTO", length = 100)
	private String puesto;

	@Column(name = "COD_PUESTO", length = 5)
	private String codigoPuesto;

	@Column(name = "COD_FUN_PUESTO", length = 5)
	private String codigoFuncionalPuesto;

	@Column(name = "EMAIL", length = 100)
	private String email;

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getCodigoOficina() {
		return codigoOficina;
	}

	public void setCodigoOficina(String codigoOficina) {
		this.codigoOficina = codigoOficina;
	}

	public String getDescripcionOficina() {
		return descripcionOficina;
	}

	public void setDescripcionOficina(String descripcionOficina) {
		this.descripcionOficina = descripcionOficina;
	}

	public String getCodigoEntidad() {
		return codigoEntidad;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
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

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getCodigoPuesto() {
		return codigoPuesto;
	}

	public void setCodigoPuesto(String codigoPuesto) {
		this.codigoPuesto = codigoPuesto;
	}

	public String getCodigoFuncionalPuesto() {
		return codigoFuncionalPuesto;
	}

	public void setCodigoFuncionalPuesto(String codigoFuncionalPuesto) {
		this.codigoFuncionalPuesto = codigoFuncionalPuesto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
