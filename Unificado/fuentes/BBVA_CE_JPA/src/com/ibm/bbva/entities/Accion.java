package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Accion
 *
 */
@Entity
@Table(name="TBL_CE_IBM_ACCION", schema = "CONELE")
@NamedQueries({	
@NamedQuery(name = "Accion.findAll", query = "SELECT p FROM Accion p"),
@NamedQuery(name = "Accion.findById", query="SELECT DISTINCT d FROM Accion d WHERE d.id = :id")
})
public class Accion implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_ACCION_ID_GENERATOR", sequenceName="SEQ_CE_IBM_ACCION", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_ACCION_ID_GENERATOR")
	private long id;
   
	String descripcion;

	public long getId() {
		return id;
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
