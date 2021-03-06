package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ProductoCE generated by hbm2java
 */
@Entity
@Table(name = "TBL_CE_IBM_PRODUCTO", schema = "CONELE")
public class ProductoCE implements java.io.Serializable {

	private static final long serialVersionUID = -9101513299580061937L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 5, scale = 0)
	private int id;
	
	@Column(name = "CODIGO", nullable = false, length = 5)
	private String codigo;
	
	@Column(name = "DESCRIPCION", nullable = false, length = 50)
	private String descripcion;
	
	@Column(name = "PESO", nullable = false, precision = 5, scale = 0)
	private int peso;

	public ProductoCE() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
}
