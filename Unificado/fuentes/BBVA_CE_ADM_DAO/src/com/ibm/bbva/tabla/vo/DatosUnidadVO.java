package com.ibm.bbva.tabla.vo;

public class DatosUnidadVO {

	static final long serialVersionUID = 1243526157593L;
	
	private String Tipo;
	private float aceptado;
	private float total;
	
	public String getTipo() {
		return Tipo;
	}
	public void setTipo(String tipo) {
		Tipo = tipo;
	}
	public float getAceptado() {
		return aceptado;
	}
	public void setAceptado(float aceptado) {
		this.aceptado = aceptado;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}


}
