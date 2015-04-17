package com.ibm.bbva.tabla.dto;


import com.ibm.as.core.dto.AbstractDTO;

public class PosibleValorDTO extends AbstractDTO {

	static final long serialVersionUID = 1243526157593L;

	private Integer id;
	private String valor;
	private Integer idColumna;
	private String etiqueta;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getIdColumna() {
		return idColumna;
	}

	public void setIdColumna(Integer idColumna) {
		this.idColumna = idColumna;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
}
