package com.ibm.bbva.tabla.util.vo;

public class OficinaTemporalVO 
{

	private String id;
	private String oficina;
	private String territorio;
	private String rangoFechas;
	private String usuario;
	private int expedientes;
	private boolean flagActivo;
	private boolean seleccion;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}
	public String getTerritorio() {
		return territorio;
	}
	public void setTerritorio(String territorio) {
		this.territorio = territorio;
	}
	public String getRangoFechas() {
		return rangoFechas;
	}
	public void setRangoFechas(String rangoFechas) {
		this.rangoFechas = rangoFechas;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public int getExpedientes() {
		return expedientes;
	}
	public void setExpedientes(int expedientes) {
		this.expedientes = expedientes;
	}
	public boolean isFlagActivo() {
		return flagActivo;
	}
	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
		
}
