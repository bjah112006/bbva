package com.ibm.bbva.ctacte.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CARGA_PROCESO", schema = "CONELE")
public class Proceso implements java.io.Serializable {

	private static final long serialVersionUID = -579538871579948858L;

	@Id
	@SequenceGenerator(name = "seqProceso", sequenceName = "SEQ_CE_IBM_PROCESO", allocationSize = 1, schema = "CONELE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProceso")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private int id;

	@Column(name = "NOMBRE", length = 50)
	private String nombre;

	@Column(name = "FECHA_INICIO")
	private Date fechaInicio;

	@Column(name = "FECHA_FIN")
	private Date fechaFin;

	@Column(name = "ESTADO", length = 50)
	private String estado;

	@Column(name = "DESCRIPCION", length = 2000)
	private String descripcion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
