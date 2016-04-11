package com.ibm.mant.tabla.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistroTablaVO {
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistroTablaVO.class);

	private Boolean seleccionado = new Boolean(false);
	private String nombreTabla;
	private Map<String, Object> datosRegistro;
	private Collection<ColumnaVO> columnas;

	/**
	 * Constructor
	 */
	public RegistroTablaVO() { 
		super();
		this.seleccionado = new Boolean(false);
		this.datosRegistro = new HashMap<String, Object>();
	}

	/**
	 * Constructor
	 * @param datosRegistro
	 */
	public RegistroTablaVO(Map<String, Object> datosRegistro) {
		super();
		this.datosRegistro = datosRegistro;
	}

	/**
	 * @return the datosRegistro
	 */
	public Map<String, Object> getDatosRegistro() {
		return datosRegistro;
	}

	/**
	 * @param datosRegistro the datosRegistro to set
	 */
	public void setDatosRegistro(Map<String, Object> datosRegistro) {
		this.datosRegistro = datosRegistro;
	}

	/**
	 * @return the seleccionado
	 */
	public Boolean getSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado the seleccionado to set
	 */
	public void setSeleccionado(Boolean seleccionado) {
		LOG.info("Seleccionado = "+seleccionado);
		this.seleccionado = seleccionado;
	}

	/**
	 * @return the nombreTabla
	 */
	public String getNombreTabla() {
		return nombreTabla;
	}

	/**
	 * @param nombreTabla the nombreTabla to set
	 */
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	/**
	 * @return the columnas
	 */
	public Collection<ColumnaVO> getColumnas() {
		return columnas;
	}

	/**
	 * @param columnas the columnas to set
	 */
	public void setColumnas(Collection<ColumnaVO> columnas) {
		this.columnas = columnas;
	}
}
