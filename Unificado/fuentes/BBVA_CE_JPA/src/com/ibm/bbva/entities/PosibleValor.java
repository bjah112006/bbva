package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_POSIBLE_VALOR database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_POSIBLE_VALOR", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name="PosibleValor.findAll", query="SELECT p FROM PosibleValor p"),
	@NamedQuery(name = "PosibleValor.findByIdColumna", query = "SELECT p FROM PosibleValor p WHERE p.columna.id = :idColumna order by p.id"),
	@NamedQuery(name = "PosibleValor.findByIdColumnaIdValor", query = "SELECT p FROM PosibleValor p WHERE p.columna.id = :idColumna and trim(p.valor) like :idValor")
})
public class PosibleValor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_POSIBLE_VALOR_ID_GENERATOR", sequenceName="SEQ_CE_IBM_POSIBLE_VALOR", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_POSIBLE_VALOR_ID_GENERATOR")
	private long id;

	private String etiqueta;

	private String valor;

	//bi-directional many-to-one association to Columna
	@ManyToOne
	@JoinColumn(name="ID_COLUMNA")
	private Columna columna;

	public PosibleValor() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEtiqueta() {
		return this.etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Columna getColumna() {
		return this.columna;
	}

	public void setColumna(Columna columna) {
		this.columna = columna;
	}

}