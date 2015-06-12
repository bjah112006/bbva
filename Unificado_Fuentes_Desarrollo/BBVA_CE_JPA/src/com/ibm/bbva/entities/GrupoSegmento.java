package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_GRUPO_SEGMENTO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_GRUPO_SEGMENTO", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="GrupoSegmento.findAll", query="SELECT n FROM GrupoSegmento n"),
	@NamedQuery(name="GrupoSegmento.findById", query="SELECT n FROM GrupoSegmento n WHERE n.id = :id")
})
public class GrupoSegmento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_GRUPO_SEGMENTO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_GRUPO_SEGMENTO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_GRUPO_SEGMENTO_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	public GrupoSegmento() {
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