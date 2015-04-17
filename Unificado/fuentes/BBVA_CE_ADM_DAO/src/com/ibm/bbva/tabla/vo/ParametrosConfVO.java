package com.ibm.bbva.tabla.vo;

import com.ibm.as.core.vo.AbstractVO;

public class ParametrosConfVO extends AbstractVO {

	static final long serialVersionUID = 1243526157593L;

	private Integer id;
	private Integer codigoAplicativo;
	private String nombreVariable;
	private String valorVariable;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoAplicativo() {
		return codigoAplicativo;
	}
	public void setCodigoAplicativo(Integer codigoAplicativo) {
		this.codigoAplicativo = codigoAplicativo;
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
}