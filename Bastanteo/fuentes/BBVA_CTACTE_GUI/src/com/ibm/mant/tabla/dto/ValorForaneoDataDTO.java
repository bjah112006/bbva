package com.ibm.mant.tabla.dto;



import com.ibm.as.core.dto.AbstractDTO;

public class ValorForaneoDataDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer valor;
	private String nombre;
	
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	public Integer getValor() {
		return valor;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}
	
	
	
	
	
	
	
	
}
