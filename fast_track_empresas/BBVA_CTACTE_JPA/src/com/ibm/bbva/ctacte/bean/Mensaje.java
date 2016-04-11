package com.ibm.bbva.ctacte.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_MENSAJE_CC", schema = "CONELE")
public class Mensaje implements java.io.Serializable {
	
	private static final long serialVersionUID = -4282674540264217040L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private Integer id;
	
	@Column(name = "DESCRIPCION", nullable = false, length = 150)
	private String descripcion;

	@Lob()
	@Column(name = "CONTENIDO")
	private byte[] contenido;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public String getContenidoStr() {
		try {
			return new String(contenido, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}
	
}
