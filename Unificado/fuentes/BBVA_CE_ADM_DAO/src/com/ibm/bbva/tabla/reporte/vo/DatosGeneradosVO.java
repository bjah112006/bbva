package com.ibm.bbva.tabla.reporte.vo;

public class DatosGeneradosVO {

	static final long serialVersionUID = 1243526157593L;
	
	private String valor;
	private String tipoConcepto; //TIPO CONCEPTO	private String te;
	private String te;
	private String tc;
	private String flujo;	
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTe() {
		return te;
	}
	public void setTe(String te) {
		this.te = te;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
	public String getTipoConcepto() {
		return tipoConcepto;
	}
	public void setTipoConcepto(String tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}
	public String getFlujo() {
		return flujo;
	}
	public void setFlujo(String flujo) {
		this.flujo = flujo;
	}
	
}
