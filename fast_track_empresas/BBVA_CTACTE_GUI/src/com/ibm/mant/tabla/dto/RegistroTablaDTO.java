package com.ibm.mant.tabla.dto;

import java.util.Map;

import com.ibm.as.core.dto.AbstractDTO;


public class RegistroTablaDTO extends AbstractDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3632572517652174846L;
	
	private Map<String,Object> datosRegistro;

	/**
	 * @return the datosRegistro
	 */
	public Map<String,Object> getDatosRegistro() {
		return datosRegistro;
	}

	/**
	 * @param datosRegistro the datosRegistro to set
	 */
	public void setDatosRegistro(Map<String,Object> datosRegistro) {
		this.datosRegistro = datosRegistro;
	}
}
