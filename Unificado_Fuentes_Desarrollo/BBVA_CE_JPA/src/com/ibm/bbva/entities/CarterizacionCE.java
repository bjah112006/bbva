package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_CARTERIZACION_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_CARTERIZACION_CE", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name = "CarterizacionCE.findAll", query = "select c from CarterizacionCE c"),
	@NamedQuery(name = "CarterizacionCE.findByCodigo", query = "SELECT c FROM CarterizacionCE c WHERE c.codigo = :codigo")
})
public class CarterizacionCE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBL_CE_IBM_CARTERIZACION_CE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_CARTERIZACION_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_CARTERIZACION_CE_ID_GENERATOR")
	
	private long id;

	private String codigo;

	private String descripcion;
	
	@Column(name="FLAG_ACTIVO")
	private String flagActivo;

	public CarterizacionCE() {
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

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

}