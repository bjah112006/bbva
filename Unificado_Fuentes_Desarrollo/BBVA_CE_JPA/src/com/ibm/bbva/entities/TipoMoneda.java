package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_MONEDA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_MONEDA", schema = "CONELE")

@NamedQueries({
	@NamedQuery(name="TipoMoneda.findAll", query="SELECT t FROM TipoMoneda t"),
	@NamedQuery(name="TipoMoneda.findById", query="SELECT t FROM TipoMoneda t WHERE t.id = :id")
})

public class TipoMoneda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TIPO_MONEDA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TIPO_MONEDA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TIPO_MONEDA_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	public TipoMoneda() {
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

}