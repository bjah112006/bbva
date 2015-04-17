package com.ibm.bbva.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TBL_CE_IBM_LOG_ERRORES database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_LOG_ERRORES", schema = "CONELE")
public class LogErrores implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_LOG_ERRORES_ID_GENERATOR", sequenceName="SEQ_CE_IBM_LOG_ERRORES", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_LOG_ERRORES_ID_GENERATOR")
	private long id;
	
	//uni-directional many-to-one association to Expediente
	@ManyToOne
	@JoinColumn(name="ID_EXPEDIENTE_FK")
	private Expediente expediente;
	
	//uni-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_ESTADO_ANTERIOR_FK")
	private EstadoCE estadoCE;

	//uni-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;

	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;
	
	//uni-directional many-to-one association to Empleado
	@ManyToOne
	@JoinColumn(name="ID_EMPLEADO_FK")
	private Empleado empleado;
	
	@Column(name="FECHA_INCIDENTE")
	private Timestamp fechaIncidente;
	
	@Column(name="NRO_REINTENTOS")
	private BigDecimal numeroReintentos;

	@Column(name="TIPO_ERROR")
	private String tipoError;
	
	@Column(name="DESCRIPCION_ERROR")
	private String descripcionError;
	
	@Column(name="CANT_DOCUMENTOS")
	private BigDecimal cantidadDocumentos;
	
	@Column(name="FECHA_RESTAURACION")
	private Timestamp fechaRestauracion;
	
	@Column(name="FECHA_CANCELACION")
	private Timestamp fechaCancelacion;
	
	public LogErrores() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public EstadoCE getEstadoCE() {
		return estadoCE;
	}

	public void setEstadoCE(EstadoCE estadoCE) {
		this.estadoCE = estadoCE;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Timestamp getFechaIncidente() {
		return fechaIncidente;
	}

	public void setFechaIncidente(Timestamp fechaIncidente) {
		this.fechaIncidente = fechaIncidente;
	}

	public BigDecimal getNumeroReintentos() {
		return numeroReintentos;
	}

	public void setNumeroReintentos(BigDecimal numeroReintentos) {
		this.numeroReintentos = numeroReintentos;
	}

	public String getTipoError() {
		return tipoError;
	}

	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public BigDecimal getCantidadDocumentos() {
		return cantidadDocumentos;
	}

	public void setCantidadDocumentos(BigDecimal cantidadDocumentos) {
		this.cantidadDocumentos = cantidadDocumentos;
	}

	public Timestamp getFechaRestauracion() {
		return fechaRestauracion;
	}

	public void setFechaRestauracion(Timestamp fechaRestauracion) {
		this.fechaRestauracion = fechaRestauracion;
	}

	public Timestamp getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Timestamp fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	
	
}
