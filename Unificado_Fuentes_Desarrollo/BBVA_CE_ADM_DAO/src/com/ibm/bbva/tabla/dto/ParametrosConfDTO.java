package com.ibm.bbva.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class ParametrosConfDTO extends AbstractDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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