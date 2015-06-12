package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TBL_CE_IBM_PARAMETROS_PROCESO database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_PARAMETROS_PROCESO", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "ParametrosProceso.findAll", query="SELECT p FROM ParametrosProceso p"),
	@NamedQuery(name = "ParametrosProceso.findByVariable", query = "SELECT p FROM ParametrosProceso p WHERE p.nombreVariable = :nombreVariable")	 
})

public class ParametrosProceso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_PARAMETROS_PROCESO_ID_GENERATOR", sequenceName="SEQ_CE_IBM_PARAMETROS_PROCESO", schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_PARAMETROS_PROCESO_ID_GENERATOR")
	private long id;

	@Column(name="NOMBRE_VARIABLE")
	private String nombreVariable;

	@Column(name="VALOR_VARIABLE")
	private String valorVariable;

    public ParametrosProceso() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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
