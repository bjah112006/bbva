package com.ibm.bbva.ctacte.bean;
// default package
// Generated 02/08/2012 02:55:50 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EstudioAbogado generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_ESTUDIO_ABOGADO_CC", schema = "CONELE")
public class EstudioAbogado implements java.io.Serializable {

	private static final long serialVersionUID = -6052892767625722174L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Integer id;
	
	@Column(name = "DESCRIPCION", length = 30)
	private String descripcion;
	
	@Column(name = "PORCENTAJE_CARGA", precision = 5, scale = 0)
	private Integer porcentajeCarga;

	public EstudioAbogado() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getPorcentajeCarga() {
		return porcentajeCarga;
	}

	public void setPorcentajeCarga(Integer porcentajeCarga) {
		this.porcentajeCarga = porcentajeCarga;
	}

}
