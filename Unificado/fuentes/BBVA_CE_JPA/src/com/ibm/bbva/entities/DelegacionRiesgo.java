package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_DELEGACION_RIESGOS database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DELEGACION_RIESGOS", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="DelegacionRiesgo.findAll", query="SELECT d FROM DelegacionRiesgo d"),
	@NamedQuery(name = "DelegacionRiesgo.findById", query = "SELECT e FROM DelegacionRiesgo e WHERE e.tipoCategoria.id = :idTipoCategoria and e.producto.id = :idProducto and e.tipoMoneda.id= :idMoneda")
})
public class DelegacionRiesgo implements Serializable {
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private DelegacionRiesgoPK id;

	@Column(name="LINEA_CRED_APROB")
	private double lineaCredAprob;

	private String pep;

	@Column(name="PORCENTAJE_ENDEUDAMIENTO_UD")
	private double porcentajeEndeudamientoUd;

	@Column(name="PRCTJE_ENDEUDAMIENTO_VIP_MAX")
	private double prctjeEndeudamientoVipMax;

	@Column(name="PRCTJE_ENDEUDAMIENTO_VIP_MIN")
	private double prctjeEndeudamientoVipMin;

	@Column(name="RIESGO_CLIENTE")
	private double riesgoCliente;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//uni-directional many-to-one association to TipoBuro
	@ManyToOne
	@JoinColumn(name="ID_TIPO_BURO_INI_FK")
	private TipoBuro tipoBuroIni;

	//uni-directional many-to-one association to TipoBuro
	@ManyToOne
	@JoinColumn(name="ID_TIPO_BURO_FK")
	private TipoBuro tipoBuro;

	//uni-directional many-to-one association to TipoCategoria
	@Id
	@ManyToOne
	@JoinColumn(name="ID_TIPO_CATEGORIA_FK")
	private TipoCategoria tipoCategoria;

	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPO_MONEDA_FK")
	private TipoMoneda tipoMoneda;

	public DelegacionRiesgo() {
	}
/*
	public DelegacionRiesgoPK getId() {
		return this.id;
	}

	public void setId(DelegacionRiesgoPK id) {
		this.id = id;
	}
*/
	public double getLineaCredAprob() {
		return this.lineaCredAprob;
	}

	public void setLineaCredAprob(double lineaCredAprob) {
		this.lineaCredAprob = lineaCredAprob;
	}

	public String getPep() {
		return this.pep;
	}

	public void setPep(String pep) {
		this.pep = pep;
	}

	public double getPorcentajeEndeudamientoUd() {
		return this.porcentajeEndeudamientoUd;
	}

	public void setPorcentajeEndeudamientoUd(double porcentajeEndeudamientoUd) {
		this.porcentajeEndeudamientoUd = porcentajeEndeudamientoUd;
	}

	public double getPrctjeEndeudamientoVipMax() {
		return this.prctjeEndeudamientoVipMax;
	}

	public void setPrctjeEndeudamientoVipMax(double prctjeEndeudamientoVipMax) {
		this.prctjeEndeudamientoVipMax = prctjeEndeudamientoVipMax;
	}

	public double getPrctjeEndeudamientoVipMin() {
		return this.prctjeEndeudamientoVipMin;
	}

	public void setPrctjeEndeudamientoVipMin(double prctjeEndeudamientoVipMin) {
		this.prctjeEndeudamientoVipMin = prctjeEndeudamientoVipMin;
	}

	public double getRiesgoCliente() {
		return this.riesgoCliente;
	}

	public void setRiesgoCliente(double riesgoCliente) {
		this.riesgoCliente = riesgoCliente;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoBuro getTipoBuroIni() {
		return this.tipoBuroIni;
	}

	public void setTipoBuroIni(TipoBuro tipoBuroIni) {
		this.tipoBuroIni = tipoBuroIni;
	}

	public TipoBuro getTipoBuro() {
		return this.tipoBuro;
	}

	public void setTipoBuro(TipoBuro tipoBuro) {
		this.tipoBuro = tipoBuro;
	}

	public TipoMoneda getTipoMoneda() {
		return this.tipoMoneda;
	}

	public void setTipoMoneda(TipoMoneda tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public TipoCategoria getTipoCategoria() {
		return tipoCategoria;
	}
	public void setTipoCategoria(TipoCategoria tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

}