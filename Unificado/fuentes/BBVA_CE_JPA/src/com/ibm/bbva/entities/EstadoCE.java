package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_ESTADO_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_ESTADO_CE", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "EstadoCE.findAll", query="SELECT e FROM EstadoCE e ORDER BY e.descripcion"),
	@NamedQuery(name = "EstadoCE.findById", query = "SELECT e FROM EstadoCE e WHERE e.id = :id ORDER BY e.descripcion")	 
})

public class EstadoCE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
 	private long id;
	private String descripcion;

	 
	public EstadoCE() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}