package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_CE_IBM_PRODUCTO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_PRODUCTO_CE", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p ORDER BY p.descripcion"),
	@NamedQuery(name="Producto.findById", query="SELECT p FROM Producto p WHERE p.id = :id")
})
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PRODUCTO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PRODUCTO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PRODUCTO_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	private BigDecimal peso;

	//bi-directional many-to-one association to MontoPeso
	@OneToMany(mappedBy="producto")
	private List<MontoPeso> montoPesos;

	//bi-directional many-to-one association to Subproducto
	@OneToMany(mappedBy="producto")
	private List<Subproducto> subproductos;

	public Producto() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPeso() {
		return this.peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public List<MontoPeso> getMontoPesos() {
		return this.montoPesos;
	}

	public void setMontoPesos(List<MontoPeso> montoPesos) {
		this.montoPesos = montoPesos;
	}

	public MontoPeso addMontoPeso(MontoPeso montoPeso) {
		getMontoPesos().add(montoPeso);
		montoPeso.setProducto(this);

		return montoPeso;
	}

	public MontoPeso removeMontoPeso(MontoPeso montoPeso) {
		getMontoPesos().remove(montoPeso);
		montoPeso.setProducto(null);

		return montoPeso;
	}

	public List<Subproducto> getSubproductos() {
		return this.subproductos;
	}

	public void setSubproductos(List<Subproducto> subproductos) {
		this.subproductos = subproductos;
	}

	public Subproducto addSubproducto(Subproducto subproducto) {
		getSubproductos().add(subproducto);
		subproducto.setProducto(this);

		return subproducto;
	}

	public Subproducto removeSubproducto(Subproducto subproducto) {
		getSubproductos().remove(subproducto);
		subproducto.setProducto(null);

		return subproducto;
	}

}