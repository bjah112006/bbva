package com.ibm.mant.tabla.dto;

import com.ibm.as.core.dto.AbstractDTO;

public class ColumnaDTO extends AbstractDTO {
	static final long serialVersionUID = 1249706814562L;

	private Integer id;
	private String nombre;
	private Boolean esLlavePrimaria;
	private Integer idTabla;
	private Boolean esRequerido;
	private Integer longitudMaxima;
	private String nombreMostrar;
	private String tipoDato;
	private Boolean esLlaveForanea;
	private String valorRegistro;
	private String busqueda;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean isEsLlavePrimaria() {
		return esLlavePrimaria;
	}

	public void setEsLlavePrimaria(Boolean esLlavePrimaria) {
		this.esLlavePrimaria = esLlavePrimaria;
	}

	public Integer getIdTabla() {
		return idTabla;
	}

	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}

	public Boolean isEsRequerido() {
		return esRequerido;
	}

	public void setEsRequerido(Boolean esRequerido) {
		this.esRequerido = esRequerido;
	}

	public Integer getLongitudMaxima() {
		return longitudMaxima;
	}

	public void setLongitudMaxima(Integer longitudMaxima) {
		this.longitudMaxima = longitudMaxima;
	}

	public String getNombreMostrar() {
		return nombreMostrar;
	}

	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}

	public String getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	public void setEsLlaveForanea(Boolean esLlaveForanea) {
		this.esLlaveForanea = esLlaveForanea;
	}

	public Boolean isEsLlaveForanea() {
		return esLlaveForanea;
	}

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}

	public String getValorRegistro() {
		return valorRegistro;
	}

	public void setValorRegistro(String valorRegistro) {
		this.valorRegistro = valorRegistro;
	}
	
	
}
