package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Garantia
 *
 */
@Entity
@Table(name="TBL_CE_IBM_GARANTIAS", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name = "Garantia.findAll", query = "SELECT s FROM Garantia s"),
	@NamedQuery(name = "Garantia.findById", query = "SELECT s FROM Garantia s WHERE s.id = :id"),
	@NamedQuery(name = "Garantia.buscarPorIdProducto", query = "SELECT s FROM Garantia s WHERE s.producto.id = :idProducto ORDER BY s.descripcion"),
	@NamedQuery(name = "Garantia.buscarSinGarantia", query = "SELECT s FROM Garantia s WHERE s.producto.id = :idProducto and s.flagSinGarantia = :flagSinGarantia")	
})
public class Garantia implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_GARANTIAS_ID_GENERATOR", sequenceName="SEQ_CE_IBM_GARANTIAS", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_GARANTIAS_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	@Column(name="FLAG_SIN_GARANTIA")
	private String flagSinGarantia;
	
	public Garantia() {
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getFlagSinGarantia() {
		return flagSinGarantia;
	}

	public void setFlagSinGarantia(String flagSinGarantia) {
		this.flagSinGarantia = flagSinGarantia;
	}	
   
	
}
