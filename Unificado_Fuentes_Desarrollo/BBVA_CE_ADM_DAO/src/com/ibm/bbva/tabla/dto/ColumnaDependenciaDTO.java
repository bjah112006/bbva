package com.ibm.bbva.tabla.dto;


import com.ibm.as.core.dto.AbstractDTO;

public class ColumnaDependenciaDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer idColumnaPadre;
	private Integer idTablaHijo;
	private Integer idColumnaHijo;
	private String nombreColumna;
	private String valorColumna;
	private String nombreColumnaBsq;
	private String flagDependenciaCruzada;
	private String nombreColumnaDependencia;
	private Integer idPadre;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}	
	public Integer getIdColumnaPadre() {
		return idColumnaPadre;
	}
	public void setIdColumnaPadre(Integer idColumnaPadre) {
		this.idColumnaPadre = idColumnaPadre;
	}
	public Integer getIdTablaHijo() {
		return idTablaHijo;
	}
	public void setIdTablaHijo(Integer idTablaHijo) {
		this.idTablaHijo = idTablaHijo;
	}
	public Integer getIdColumnaHijo() {
		return idColumnaHijo;
	}
	public void setIdColumnaHijo(Integer idColumnaHijo) {
		this.idColumnaHijo = idColumnaHijo;
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
	public String getNombreColumnaBsq() {
		return nombreColumnaBsq;
	}
	public void setNombreColumnaBsq(String nombreColumnaBsq) {
		this.nombreColumnaBsq = nombreColumnaBsq;
	}
	public String getFlagDependenciaCruzada() {
		return flagDependenciaCruzada;
	}
	public void setFlagDependenciaCruzada(String flagDependenciaCruzada) {
		this.flagDependenciaCruzada = flagDependenciaCruzada;
	}
	public String getNombreColumnaDependencia() {
		return nombreColumnaDependencia;
	}
	public void setNombreColumnaDependencia(String nombreColumnaDependencia) {
		this.nombreColumnaDependencia = nombreColumnaDependencia;
	}
	public Integer getIdPadre() {
		return idPadre;
	}
	public void setIdPadre(Integer idPadre) {
		this.idPadre = idPadre;
	}
}
