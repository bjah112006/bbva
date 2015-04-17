package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_TIPO_CATEGORIA database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_TIPO_CATEGORIA", schema = "CONELE")
@NamedQueries({
@NamedQuery(name="TipoCategoria.findAll", query="SELECT t FROM TipoCategoria t"),
@NamedQuery(name="TipoCategoria.findById", query="SELECT t FROM TipoCategoria t where t.id=:id")
})
public class TipoCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_TIPO_CATEGORIA_ID_GENERATOR", sequenceName="SEQ_CE_IBM_TIPO_CATEGORIA", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_TIPO_CATEGORIA_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	@Column(name="FLAG_SUPERIOR")
	private String flagSuperior;

	public TipoCategoria() {
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

	public String getFlagSuperior() {
		return this.flagSuperior;
	}

	public void setFlagSuperior(String flagSuperior) {
		this.flagSuperior = flagSuperior;
	}

}