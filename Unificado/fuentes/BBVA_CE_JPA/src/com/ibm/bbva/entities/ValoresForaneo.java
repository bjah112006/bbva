package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_VALORES_FORANEOS database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_VALORES_FORANEOS", schema = "CONELE")
@NamedQuery(name="ValoresForaneo.findAll", query="SELECT v FROM ValoresForaneo v")
public class ValoresForaneo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_VALORES_FORANEOS_ID_GENERATOR", sequenceName="SEQ_CE_IBM_VALORES_FORANEOS", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_VALORES_FORANEOS_ID_GENERATOR")
	private long id;

	@Column(name="NOMBRE_COLUMNA")
	private String nombreColumna;

	@Column(name="NOMBRE_TABLA")
	private String nombreTabla;

	@Column(name="VALOR_COLUMNA")
	private String valorColumna;

	//bi-directional many-to-one association to Columna
	@ManyToOne
	@JoinColumn(name="ID_COLUMNA")
	private Columna columna;

	public ValoresForaneo() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreColumna() {
		return this.nombreColumna;
	}

	public void setNombreColumna(String nombreColumna) {
		this.nombreColumna = nombreColumna;
	}

	public String getNombreTabla() {
		return this.nombreTabla;
	}

	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getValorColumna() {
		return this.valorColumna;
	}

	public void setValorColumna(String valorColumna) {
		this.valorColumna = valorColumna;
	}

	public Columna getColumna() {
		return this.columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}

}