package com.ibm.bbva.ctacte.bean;
// default package
// Generated 14/05/2012 10:37:52 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Producto generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_PRODUCTO_CC", schema = "CONELE")
public class Producto implements java.io.Serializable {

	private static final long serialVersionUID = 8075775126699649309L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private Integer id;
	
	@Column(name = "CODIGO", nullable = false, length = 5)
	private String codigo;
	
	@Column(name = "DESCRIPCION", nullable = false, length = 50)
	private String descripcion;

	public Producto() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
