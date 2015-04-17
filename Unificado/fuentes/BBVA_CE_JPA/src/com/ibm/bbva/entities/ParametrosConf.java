package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_PARAMETROS_CONF database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_PARAMETROS_CONF", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "ParametrosConf.findAll", query="SELECT p FROM ParametrosConf p"),
	@NamedQuery(name = "ParametrosConf.findByVariable", query = "SELECT p FROM ParametrosConf p WHERE p.codigoAplicativo = :codigoAplicativo and p.nombreVariable = :nombreVariable"),
	@NamedQuery(name = "ParametrosConf.findByAplicativo", query = "SELECT p FROM ParametrosConf p WHERE p.codigoAplicativo = :codigoAplicativo")
})

public class ParametrosConf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PARAMETROS_CONF_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PARAMETROS_CONF", schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PARAMETROS_CONF_ID_GENERATOR")
	private long id;

	@Column(name="CODIGO_APLICATIVO")
	private Integer codigoAplicativo;

	@Column(name="NOMBRE_VARIABLE")
	private String nombreVariable;

	@Column(name="VALOR_VARIABLE")
	private String valorVariable;

    public ParametrosConf() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getCodigoAplicativo() {
		return this.codigoAplicativo;
	}

	public void setCodigoAplicativo(Integer codigoAplicativo) {
		this.codigoAplicativo = codigoAplicativo;
	}

	public String getNombreVariable() {
		return this.nombreVariable;
	}

	public void setNombreVariable(String nombreVariable) {
		this.nombreVariable = nombreVariable;
	}

	public String getValorVariable() {
		return this.valorVariable;
	}

	public void setValorVariable(String valorVariable) {
		this.valorVariable = valorVariable;
	}

}