package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_DELEGACION_OFICINA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_DELEGACION_OFICINA", schema = "CONELE")
@NamedQuery(name="TblCeIbmDelegacionOficina.findAll", query="SELECT t FROM TblCeIbmDelegacionOficina t")

@NamedQueries({	
	@NamedQuery(name = "TblCeIbmDelegacionOficina.findAll", query = "SELECT t FROM TblCeIbmDelegacionOficina t"),
	@NamedQuery(name = "TblCeIbmDelegacionOficina.findByNivel", query = "SELECT t FROM TblCeIbmDelegacionOficina t WHERE t.nivel.id = :idNivel"),
	@NamedQuery(name = "TblCeIbmDelegacionOficina.findById", query = "SELECT e FROM TblCeIbmDelegacionOficina e " +
			" WHERE e.nivel.id = :idNivel and e.tipoMoneda.id = :idMoneda and e.producto.id = :idProducto"),
	@NamedQuery(name = "TblCeIbmDelegacionOficina.findByNivelProducto", query = "SELECT e FROM TblCeIbmDelegacionOficina e " +
					" WHERE e.nivel.id = :idNivel and e.producto.id = :idProducto")
})
public class TblCeIbmDelegacionOficina implements Serializable {
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private TblCeIbmDelegacionOficinaPK id;
	
	//uni-directional many-to-one association to Clasif_Banco
	@ManyToOne
	@JoinColumn(name="BANCO_CONYUGE")
	private ClasifBanco bancoConyuge;

	//uni-directional many-to-one association to Clasif_Banco
	@ManyToOne
	@JoinColumn(name="CLASIFICACION_BANCO")	
	private ClasifBanco clasificacionBanco;

	@Column(name="CLASIFICACION_SBS")
	private double clasificacionSbs;

	@Column(name="LIMITE_CONSUMO")
	private double limiteConsumo;

	private String pep;

	@Column(name="PORCENTAJE_ENDEUDAMIENTO")
	private double porcentajeEndeudamiento;

	@Column(name="RIESGO_CLIENTE")
	private double riesgoCliente;

	@Column(name="SBS_CONYUGE")
	private double sbsConyuge;

	@Column(name="FINANC_MAX")
	private double financiamientoMax;
	
	@Column(name="PLAZO_MAX")
	private String plazoMax;
	
	//uni-directional many-to-one association to Nivel
	@Id
	@ManyToOne
	@JoinColumn(name="ID_NIVEL_FK")
	private Nivel nivel;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPO_MONEDA_FK")
	private TipoMoneda tipoMoneda;

	//uni-directional many-to-one association to TipoScoring
	@ManyToOne
	@JoinColumn(name="ID_TIPO_SCORING_FK")
	private TipoScoring tipoScoring;

	@Column(name="SUBLIMITEXPROD")
	private double limiteProducto;
	
	public TblCeIbmDelegacionOficina() {
	}

	/*public TblCeIbmDelegacionOficinaPK getId() {
		return this.id;
	}

	public void setId(TblCeIbmDelegacionOficinaPK id) {
		this.id = id;
	}*/

	public ClasifBanco getBancoConyuge() {
		return this.bancoConyuge;
	}

	public void setBancoConyuge(ClasifBanco bancoConyuge) {
		this.bancoConyuge = bancoConyuge;
	}

	public ClasifBanco getClasificacionBanco() {
		return this.clasificacionBanco;
	}

	public void setClasificacionBanco(ClasifBanco clasificacionBanco) {
		this.clasificacionBanco = clasificacionBanco;
	}

	public double getClasificacionSbs() {
		return this.clasificacionSbs;
	}

	public void setClasificacionSbs(double clasificacionSbs) {
		this.clasificacionSbs = clasificacionSbs;
	}

	public double getLimiteConsumo() {
		return this.limiteConsumo;
	}

	public void setLimiteConsumo(double limiteConsumo) {
		this.limiteConsumo = limiteConsumo;
	}

	public String getPep() {
		return this.pep;
	}

	public void setPep(String pep) {
		this.pep = pep;
	}

	public double getPorcentajeEndeudamiento() {
		return this.porcentajeEndeudamiento;
	}

	public void setPorcentajeEndeudamiento(double porcentajeEndeudamiento) {
		this.porcentajeEndeudamiento = porcentajeEndeudamiento;
	}

	public double getRiesgoCliente() {
		return this.riesgoCliente;
	}

	public void setRiesgoCliente(double riesgoCliente) {
		this.riesgoCliente = riesgoCliente;
	}

	public double getSbsConyuge() {
		return this.sbsConyuge;
	}

	public void setSbsConyuge(double sbsConyuge) {
		this.sbsConyuge = sbsConyuge;
	}

	public Nivel getNivel() {
		return this.nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
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

	public TipoScoring getTipoScoring() {
		return this.tipoScoring;
	}

	public void setTipoScoring(TipoScoring tipoScoring) {
		this.tipoScoring = tipoScoring;
	}

	public double getFinanciamientoMax() {
		return financiamientoMax;
	}

	public void setFinanciamientoMax(double financiamientoMax) {
		this.financiamientoMax = financiamientoMax;
	}

	public String getPlazoMax() {
		return plazoMax;
	}

	public void setPlazoMax(String plazoMax) {
		this.plazoMax = plazoMax;
	}

	public double getLimiteProducto() {
		return limiteProducto;
	}

	public void setLimiteProducto(double limiteProducto) {
		this.limiteProducto = limiteProducto;
	}

}