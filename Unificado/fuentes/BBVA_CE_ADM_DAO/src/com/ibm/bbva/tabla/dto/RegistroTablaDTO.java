package com.ibm.bbva.tabla.dto;

import java.util.ArrayList;
import java.util.Map;

import com.ibm.as.core.dto.AbstractDTO;


public class RegistroTablaDTO extends AbstractDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3632572517652174846L;
	
	private Map<String,Object> datosRegistro;
	private Map<String, ArrayList<Object>> datosRegistros;

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

	public Map<String, ArrayList<Object>> getDatosRegistros() {
		return datosRegistros;
	}

	public void setDatosRegistros(Map<String, ArrayList<Object>> datosRegistros) {
		this.datosRegistros = datosRegistros;
	}
}
