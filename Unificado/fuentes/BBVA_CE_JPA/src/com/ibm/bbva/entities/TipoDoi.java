package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_DOI database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_DOI", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name = "TipoDoi.findAll", query = "SELECT t FROM TipoDoi t ORDER BY t.descripcion"),
	@NamedQuery(name = "TipoDoi.findById", query = "SELECT t FROM TipoDoi t WHERE t.id = :id ORDER BY t.descripcion"),	
})
public class TipoDoi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TIPO_DOI_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TIPO_DOI", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TIPO_DOI_ID_GENERATOR")
	private long id;

	private String codigo;

	@Column(name="CODIGO_TIPO_DOI")
	private String codigoTipoDoi;

	private String descripcion;

	private BigDecimal longitud;

	public TipoDoi() {
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

	public String getCodigoTipoDoi() {
		return this.codigoTipoDoi;
	}

	public void setCodigoTipoDoi(String codigoTipoDoi) {
		this.codigoTipoDoi = codigoTipoDoi;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getLongitud() {
		return this.longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

}