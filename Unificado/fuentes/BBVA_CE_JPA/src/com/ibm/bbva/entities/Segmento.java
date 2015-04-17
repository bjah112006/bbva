package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_SEGMENTO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_SEGMENTO", schema = "CONELE")

@NamedQueries({
	
	@NamedQuery(name="Segmento.findAll", query="SELECT s FROM Segmento s Order by s.descripcion asc"),
	@NamedQuery(name = "Segmento.findById", query = "SELECT s FROM Segmento s WHERE s.id = :id"), 		
	@NamedQuery(name = "Segmento.findByCodigo", query = "SELECT s FROM Segmento s WHERE s.codigo = :codigo"),
	@NamedQuery(name = "Segmento.findByCodigoSegmento", query = "SELECT s FROM Segmento s WHERE s.codigoSegmento = :codigoSegmento") 
})

public class Segmento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_SEGMENTO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_SEGMENTO", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_SEGMENTO_ID_GENERATOR")
	private long id;

	private String codigo;

	@Column(name="CODIGO_SEGMENTO")
	private String codigoSegmento;
	
	//bi-directional many-to-one association to GrupoSegmento
	@ManyToOne
	@JoinColumn(name="ID_GRUPO_SEGMENTO_FK")
	private GrupoSegmento grupoSegmento;

	private String descripcion;

	public Segmento() {
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

	public String getCodigoSegmento() {
		return this.codigoSegmento;
	}

	public void setCodigoSegmento(String codigoSegmento) {
		this.codigoSegmento = codigoSegmento;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public GrupoSegmento getGrupoSegmento() {
		return grupoSegmento;
	}

	public void setGrupoSegmento(GrupoSegmento grupoSegmento) {
		this.grupoSegmento = grupoSegmento;
	}

}