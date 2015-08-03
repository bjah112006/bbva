package com.ibm.bbva.tabla.util.vo;

public class DescargaLDAPVO 
{
	private String id;
	private String tipo;
	private String codigo;
	private String carterizacion;
	private String perfil;
	private String oficina;
	private boolean flagActivo;
	private boolean seleccion;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCarterizacion() {
		return carterizacion;
	}
	public void setCarterizacion(String carterizacion) {
		this.carterizacion = carterizacion;
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
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getOficina() {
		return oficina;
	}
	public void setOficina(String oficina) {
		this.oficina = oficina;
	}		
}
