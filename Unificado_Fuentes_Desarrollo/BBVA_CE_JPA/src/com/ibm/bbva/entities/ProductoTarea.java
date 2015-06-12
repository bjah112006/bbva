package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: ProductoTarea
 *
 */
@Entity
@Table(name="TBL_CE_IBM_PRODUCTO_TAREA", schema = "CONELE")
@NamedQueries({	
@NamedQuery(name = "ProductoTarea.findAll", query="SELECT e FROM ProductoTarea e"),
@NamedQuery(name = "ProductoTarea.findByIdProducto", query = "SELECT d FROM ProductoTarea d WHERE d.producto.id = :idProducto")
})
public class ProductoTarea implements Serializable {

	
	private static final long serialVersionUID = 1L;

	//uni-directional many-to-one association to Producto
	@Id
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;
	
	//uni-directional many-to-one association to Tarea
	@ManyToOne
	@JoinColumn(name="ID_TAREA_FK")
	private Tarea tarea;
	
	@Column(name="FLAG_VALIDACION")
    private String flagValidacion;
	
	public ProductoTarea() {
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public String getFlagValidacion() {
		return flagValidacion;
	}

	public void setFlagValidacion(String flagValidacion) {
		this.flagValidacion = flagValidacion;
	}
}
