package com.ibm.bbva.tabla.reporte.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class DatosPorcentajeDTO  extends AbstractDTO{

	static final long serialVersionUID = 1243526157593L;
	
	private String valorRol;
	private String valorPorcentaje;
	private String valorFlujo;
	
	public String getValorRol() {
		return valorRol;
	}
	public void setValorRol(String valorRol) {
		this.valorRol = valorRol;
	}
	public String getValorPorcentaje() {
		return valorPorcentaje;
	}
	public void setValorPorcentaje(String valorPorcentaje) {
		this.valorPorcentaje = valorPorcentaje;
	}
	public String getValorFlujo() {
		return valorFlujo;
	}
	public void setValorFlujo(String valorFlujo) {
		this.valorFlujo = valorFlujo;
	}
	
}
