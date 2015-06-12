package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the TBL_CE_IBM_MONTO_PESO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_MONTO_PESO", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name="MontoPeso.findAll", query="SELECT m FROM MontoPeso m")
})
public class MontoPeso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_MONTO_PESO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_MONTO_PESO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_MONTO_PESO_ID_GENERATOR")
	private long id;

	@Column(name="MONTO_MAXIMO")
	private BigDecimal montoMaximo;

	@Column(name="MONTO_MINIMO")
	private BigDecimal montoMinimo;

	private BigDecimal peso;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPO_MONEDA_FK")
	private TipoMoneda tipoMoneda;

	public MontoPeso() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getMontoMaximo() {
		return this.montoMaximo;
	}

	public void setMontoMaximo(BigDecimal montoMaximo) {
		this.montoMaximo = montoMaximo;
	}

	public BigDecimal getMontoMinimo() {
		return this.montoMinimo;
	}

	public void setMontoMinimo(BigDecimal montoMinimo) {
		this.montoMinimo = montoMinimo;
	}

	public BigDecimal getPeso() {
		return this.peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoMoneda getTipoMoneda() {
		return this.tipoMoneda;
	}

	public void setTipoMoneda(TipoMoneda tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

}