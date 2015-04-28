package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TBL_CE_IBM_CASO_NEGOCIO_CC database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_CASO_NEGOCIO_CC", schema = "CONELE")
public class CasoNegocio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqCasoNegocio", sequenceName="SEQ_CE_IBM_CASO_NEGOCIO_CC", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqCasoNegocio")
	@Column(unique=true, nullable=false, precision=10)
	private Integer id;

	@Column(nullable=false, length=200)
	private String descripcion;

    @Lob()
	@Column(name="TEXTO_AYUDA")
	private byte[] textoAyuda;

    public CasoNegocio() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte[] getTextoAyuda() {
		return this.textoAyuda;
	}

	public void setTextoAyuda(byte[] textoAyuda) {
		this.textoAyuda = textoAyuda;
	}
	
	public String getTextoAyudaStr() {
		try {
			return new String(textoAyuda, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

}