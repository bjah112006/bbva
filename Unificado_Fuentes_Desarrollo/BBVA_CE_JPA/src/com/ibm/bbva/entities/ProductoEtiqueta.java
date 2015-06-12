package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: ProductoEtiqueta
 *
 */
@Entity
@Table(name="TBL_CE_IBM_PRODUCTO_ETIQUETA", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="ProductoEtiqueta.findAll", query="SELECT p FROM ProductoEtiqueta p"),
	@NamedQuery(name="ProductoEtiqueta.findById", query="SELECT p FROM ProductoEtiqueta p WHERE p.id = :id"),
	@NamedQuery(name = "ProductoEtiqueta.buscarPorIdProducto", query = "SELECT s FROM ProductoEtiqueta s WHERE s.producto.id = :idProducto")
})

public class ProductoEtiqueta implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PRODUCTO_ETIQUETA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PRODUCTO_ETIQUETA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PRODUCTO_ETIQUETA_ID_GENERATOR")
	private long id;
	
	private String etiqueta;

	private String visible;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	public ProductoEtiqueta() {
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	
	
}
