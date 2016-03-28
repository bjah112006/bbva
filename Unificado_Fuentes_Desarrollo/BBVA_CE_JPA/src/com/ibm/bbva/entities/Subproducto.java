package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_SUBPRODUCTO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_SUBPRODUCTO", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name = "Subproducto.findAll", query = "SELECT s FROM Subproducto s"),
	@NamedQuery(name = "Subproducto.findById", query = "SELECT s FROM Subproducto s WHERE s.id = :id"),
	@NamedQuery(name = "Subproducto.buscarPorIdProd", query = "SELECT s FROM Subproducto s WHERE s.producto.id = :idProducto and s.flagActivo=:flag ORDER BY s.descripcion"),
	@NamedQuery(name = "Subproducto.buscarPorIdProducto", query = "SELECT s FROM Subproducto s WHERE s.producto.id = :idProducto ORDER BY s.descripcion")
})
public class Subproducto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_SUBPRODUCTO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_SUBPRODUCTO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_SUBPRODUCTO_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;
		
	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;
	
	@Column(name="FLAG_ACTIVO")
	private String flagActivo;
	
	//bi-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPO_MONEDA_FK")
	private TipoMoneda tipoMoneda;	

	public Subproducto() {
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

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

	public TipoMoneda getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(TipoMoneda tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
}