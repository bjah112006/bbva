package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CE_IBM_PARAMETROS_CONF_CC", schema = "CONELE")
public class ParametrosConf implements Serializable {
	
	private static final long serialVersionUID = -1397868924882976894L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	private Integer id;

	@Column(name="CODIGO_MODULO")
	private String codigoModulo;

	@Column(name="NOMBRE_VARIABLE")
	private String nombreVariable;

	@Column(name="VALOR_VARIABLE")
	private String valorVariable;

    public ParametrosConf() {
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoModulo() {
		return codigoModulo;
	}

	public void setCodigoModulo(String codigoModulo) {
		this.codigoModulo = codigoModulo;
	}

	public String getNombreVariable() {
		return nombreVariable;
	}

	public void setNombreVariable(String nombreVariable) {
		this.nombreVariable = nombreVariable;
	}

	public String getValorVariable() {
		return valorVariable;
	}

	public void setValorVariable(String valorVariable) {
		this.valorVariable = valorVariable;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ParametrosConf [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (codigoModulo != null) {
			builder.append("codigoModulo=");
			builder.append(codigoModulo);
			builder.append(", ");
		}
		if (nombreVariable != null) {
			builder.append("nombreVariable=");
			builder.append(nombreVariable);
			builder.append(", ");
		}
		if (valorVariable != null) {
			builder.append("valorVariable=");
			builder.append(valorVariable);
		}
		builder.append("]");
		return builder.toString();
	}

}
