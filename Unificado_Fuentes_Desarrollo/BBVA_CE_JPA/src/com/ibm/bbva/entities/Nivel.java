package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_NIVEL database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_NIVEL", schema = "CONELE")
@NamedQueries({
@NamedQuery(name="Nivel.findAll", query="SELECT n FROM Nivel n"),
@NamedQuery(name="Nivel.findById", query="SELECT n FROM Nivel n where n.id=:id")
})
public class Nivel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_NIVEL_ID_GENERATOR", sequenceName="SEQ_CE_IBM_NIVEL", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_NIVEL_ID_GENERATOR")
	private long id;

	private String codigo;

	private String descripcion;

	public Nivel() {
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