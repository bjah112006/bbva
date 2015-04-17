package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: LineaMaxima
 *
 */
@Entity
@Table(name="TBL_CE_IBM_LINEA_MAXIMA", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="LineaMaxima.findAll", query="SELECT c FROM LineaMaxima c"),
	@NamedQuery(name = "LineaMaxima.find", query = "SELECT e FROM LineaMaxima e WHERE e.producto.id = :idProducto and e.territorio.id=:idTerritorio")
})

public class LineaMaxima implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_LINEA_MAXIMA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_LINEA_MAXIMA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_LINEA_MAXIMA_ID_GENERATOR")
	private long id;
	
	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;
	
	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_TIPO_MONEDA_FK")
	private TipoMoneda tipoMoneda;
	
	private String simbolo;
	
	private double monto;	
	
	//uni-directional many-to-one association to TipoScoring
	@ManyToOne
	@JoinColumn(name="ID_TIPO_SCORING_FK")
	private TipoScoring tipoScoring;
	
	@Column(name="FLAG_DELEGACION_GERENTE")
	private String flagDelegacion;
   
	@Column(name="FLAG_SUBROGADO")
	private String flagSubrogado;
	
	//uni-directional many-to-one association to TipoMoneda
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_FK")
	private Perfil perfil;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public TipoMoneda getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(TipoMoneda tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public TipoScoring getTipoScoring() {
		return tipoScoring;
	}

	public void setTipoScoring(TipoScoring tipoScoring) {
		this.tipoScoring = tipoScoring;
	}

	public String getFlagDelegacion() {
		return flagDelegacion;
	}

	public void setFlagDelegacion(String flagDelegacion) {
		this.flagDelegacion = flagDelegacion;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFlagSubrogado() {
		return flagSubrogado;
	}

	public void setFlagSubrogado(String flagSubrogado) {
		this.flagSubrogado = flagSubrogado;
	}	
	
	
}
