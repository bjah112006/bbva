package com.ibm.bbva.ctacte.bean;
// default package
// Generated 21/05/2012 01:06:39 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Motivo generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_MOTIVO_CC", schema = "CONELE")
public class Motivo implements java.io.Serializable {

	private static final long serialVersionUID = -4614294438149979646L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TAREA_FK")
	private Tarea tarea;
	
	@Column(name = "DESCRIPCION", nullable = false, length = 200)
	private String descripcion;
	
	@Column(name = "TIPO_MOTIVO", length = 100)
	private String tipoMotivo;
	
	@Column(name = "FLAG_ACTIVO", length = 1)
	private String flagActivo;
	
	@Column(name = "CODIGO_MOTIVO", length = 5)
	private String codigoMotivo;
	
//	@OneToOne(fetch = FetchType.EAGER, mappedBy = "motivo")
//	private Historial historial;

	public Motivo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoMotivo() {
		return tipoMotivo;
	}

	public void setTipoMotivo(String tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public String getCodigoMotivo() {
		return codigoMotivo;
	}

	public void setCodigoMotivo(String codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}
	
}
