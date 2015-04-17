package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_CLASIF_BANCO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_CLASIF_BANCO", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="ClasifBanco.findAll", query="SELECT c FROM ClasifBanco c"),
	@NamedQuery(name="ClasifBanco.findById", query="SELECT c FROM ClasifBanco c WHERE c.id = :id"),
	@NamedQuery(name="ClasifBanco.findByProducto", query="SELECT c FROM ClasifBanco c WHERE c.producto.id = :idProducto ORDER BY c.descripcion")
})

public class ClasifBanco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String descripcion;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	public ClasifBanco() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}