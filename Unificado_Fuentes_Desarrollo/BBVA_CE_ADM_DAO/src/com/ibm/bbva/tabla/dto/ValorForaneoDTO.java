package com.ibm.bbva.tabla.dto;


import com.ibm.as.core.dto.AbstractDTO;

public class ValorForaneoDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer idColumna;
	private String nombreTabla;
	private String nombreColumna;
	private String valorColumna;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdColumna() {
		return idColumna;
	}
	public void setIdColumna(Integer idColumna) {
		this.idColumna = idColumna;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	public String getNombreColumna() {
		return nombreColumna;
	}
	public void setNombreColumna(String nombreColumna) {
		this.nombreColumna = nombreColumna;
	}
	public String getValorColumna() {
		return valorColumna;
	}
	public void setValorColumna(String valorColumna) {
		this.valorColumna = valorColumna;
	}
	
	
	
	

}
