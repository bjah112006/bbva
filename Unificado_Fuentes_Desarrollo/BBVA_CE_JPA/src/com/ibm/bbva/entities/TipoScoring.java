package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_SCORING database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_SCORING", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="TipoScoring.findAll", query="SELECT t FROM TipoScoring t ORDER BY t.descripcion"),
	@NamedQuery(name="TipoScoring.findById", query="SELECT t FROM TipoScoring t WHERE t.id = :id")
})
public class TipoScoring implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TIPO_SCORING_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TIPO_SCORING", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TIPO_SCORING_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	public TipoScoring() {
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