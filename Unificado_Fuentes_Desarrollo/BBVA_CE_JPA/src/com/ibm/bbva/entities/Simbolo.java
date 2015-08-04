package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_SIMBOLO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_SIMBOLO", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="Simbolo.findAll", query="SELECT d FROM Simbolo d"),
	@NamedQuery(name = "Simbolo.findById", query = "SELECT e FROM Simbolo e WHERE e.id = :id ")
})
public class Simbolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_SIMBOLO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_DELEG_RIESG_COND", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_SIMBOLO_ID_GENERATOR")
	private long id;

	@Column(name="DESCRIPCION")
	private String descripcion;

	
	
	public Simbolo() {
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}




	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



}