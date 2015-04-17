package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_MENSAJE_CE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_MENSAJE_CE", schema = "CONELE")
@NamedQueries({	
	@NamedQuery(name="Mensajes.findAll", query="SELECT m FROM Mensajes m "),
	@NamedQuery(name="Mensajes.findById", query="SELECT m FROM Mensajes m WHERE m.id = :id")
})

public class Mensajes implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_MENSAJE_CE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_MENSAJE_CE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_MENSAJE_CE_ID_GENERATOR")
	private long id;

	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Lob()
	@Column(name = "CONTENIDO")
	private byte[] contenido;
	
	public Mensajes() {
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

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
}
