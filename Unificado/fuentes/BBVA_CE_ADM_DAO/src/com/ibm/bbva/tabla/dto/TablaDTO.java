package com.ibm.bbva.tabla.dto;


import com.ibm.as.core.dto.AbstractDTO;

public class TablaDTO extends AbstractDTO {

	static final long serialVersionUID = 1243526157593L;

	private Integer id;
	private String nombre;
	private String nombreMostrar;
	private String tipo;
	private String botonAgregar;
	private String botonActualizar;
	private String botonEliminar;

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

	public String getNombreMostrar() {
		return nombreMostrar;
	}

	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getBotonAgregar() {
		return botonAgregar;
	}

	public void setBotonAgregar(String botonAgregar) {
		this.botonAgregar = botonAgregar;
	}

	public String getBotonActualizar() {
		return botonActualizar;
	}

	public void setBotonActualizar(String botonActualizar) {
		this.botonActualizar = botonActualizar;
	}

	public String getBotonEliminar() {
		return botonEliminar;
	}

	public void setBotonEliminar(String botonEliminar) {
		this.botonEliminar = botonEliminar;
	}
}
