package com.ibm.bbva.ctacte.vo;

import java.io.Serializable;
import java.util.Date;

public class HorarioVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8752799454547444190L;
	public int idPerfil;
	public int idTerritorio;
	public int idOficina;
	public String descripcion;
	public int idDiaSeleccionado;
	public Date rangoInicio;
	public Date rangoFinal;
	public String rangoHoraInicial;
	public String rangoHoraFinal;
	public String rangoMinutoInicial;
	public String rangoMinutoFinal;
	public boolean estado;
	public int idRango;
	public Date fecha;
	public int idHorario;
	
	public boolean aplicaOficina;
	public boolean aplicaTodo;
	public boolean estadoHorario;
	public String titulo;
	
	public int getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public int getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(int idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public int getIdOficina() {
		return idOficina;
	}

	public void setIdOficina(int idOficina) {
		this.idOficina = idOficina;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdDiaSeleccionado() {
		return idDiaSeleccionado;
	}

	public void setIdDiaSeleccionado(int idDiaSeleccionado) {
		this.idDiaSeleccionado = idDiaSeleccionado;
	}

	public Date getRangoInicio() {
		return rangoInicio;
	}

	public void setRangoInicio(Date rangoInicio) {
		this.rangoInicio = rangoInicio;
	}

	public Date getRangoFinal() {
		return rangoFinal;
	}

	public void setRangoFinal(Date rangoFinal) {
		this.rangoFinal = rangoFinal;
	}

	public String getRangoHoraInicial() {
		return rangoHoraInicial;
	}

	public void setRangoHoraInicial(String rangoHoraInicial) {
		this.rangoHoraInicial = rangoHoraInicial;
	}

	public String getRangoHoraFinal() {
		return rangoHoraFinal;
	}

	public void setRangoHoraFinal(String rangoHoraFinal) {
		this.rangoHoraFinal = rangoHoraFinal;
	}

	public String getRangoMinutoInicial() {
		return rangoMinutoInicial;
	}

	public void setRangoMinutoInicial(String rangoMinutoInicial) {
		this.rangoMinutoInicial = rangoMinutoInicial;
	}

	public String getRangoMinutoFinal() {
		return rangoMinutoFinal;
	}

	public void setRangoMinutoFinal(String rangoMinutoFinal) {
		this.rangoMinutoFinal = rangoMinutoFinal;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public int getIdRango() {
		return idRango;
	}

	public void setIdRango(int idRango) {
		this.idRango = idRango;
	}

	public int getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}

	public boolean isAplicaTodo() {
		return aplicaTodo;
	}

	public void setAplicaTodo(boolean aplicaTodo) {
		this.aplicaTodo = aplicaTodo;
	}

	public boolean isEstadoHorario() {
		return estadoHorario;
	}

	public void setEstadoHorario(boolean estadoHorario) {
		this.estadoHorario = estadoHorario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isAplicaOficina() {
		return aplicaOficina;
	}

	public void setAplicaOficina(boolean aplicaOficina) {
		this.aplicaOficina = aplicaOficina;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	

}
