package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_DELEGACION_RIESGOS database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DELEG_RIESG_CLASBCO", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="DelegacionRiesgoClasificacionBanco.findAll", query="SELECT d FROM DelegacionRiesgoClasificacionBanco d"),
	@NamedQuery(name = "DelegacionRiesgoClasificacionBanco.findById", query = "SELECT e FROM DelegacionRiesgoClasificacionBanco e WHERE e.tipoCategoria.id = :idTipoCategoria and e.producto.id = :idProducto ")
})
public class DelegacionRiesgoClasificacionBanco implements Serializable {
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private DelegacionRiesgoPK id;

	@Column(name="CLASIF_BCO_NORMAL")
	private double clasifNormal;

	@Column(name="CLASIF_BCO_CPP")
	private double clasifCpp;

	@Column(name="CLASIF_BCO_DEFICIENTE")
	private double clasifDeficiente;

	@Column(name="CLASIF_BCO_DUDOSO")
	private double clasifDudoso;
	
	@Column(name="CLASIF_BCO_PERDIDA")
	private double clasifPerdida;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//uni-directional many-to-one association to TipoCategoria
	@Id
	@ManyToOne
	@JoinColumn(name="ID_TIPO_CATEGORIA_FK")
	private TipoCategoria tipoCategoria;

	public DelegacionRiesgoClasificacionBanco() {
	}

	public double getClasifNormal() {
		return clasifNormal;
	}

	public void setClasifNormal(double clasifNormal) {
		this.clasifNormal = clasifNormal;
	}

	public double getClasifCpp() {
		return clasifCpp;
	}

	public void setClasifCpp(double clasifCpp) {
		this.clasifCpp = clasifCpp;
	}

	public double getClasifDeficiente() {
		return clasifDeficiente;
	}

	public void setClasifDeficiente(double clasifDeficiente) {
		this.clasifDeficiente = clasifDeficiente;
	}

	public double getClasifDudoso() {
		return clasifDudoso;
	}

	public void setClasifDudoso(double clasifDudoso) {
		this.clasifDudoso = clasifDudoso;
	}

	public double getClasifPerdida() {
		return clasifPerdida;
	}

	public void setClasifPerdida(double clasifPerdida) {
		this.clasifPerdida = clasifPerdida;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoCategoria getTipoCategoria() {
		return tipoCategoria;
	}
	public void setTipoCategoria(TipoCategoria tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}
	
	
}