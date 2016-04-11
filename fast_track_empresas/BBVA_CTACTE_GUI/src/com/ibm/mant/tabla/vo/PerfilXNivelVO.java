package com.ibm.mant.tabla.vo;

public class PerfilXNivelVO {
	
	private boolean seleccion;
	private Long tipoNivel;
	private String nivelDesc;
	private String valor;
	private Integer perfilId;
	private String perfilDesc;
	private boolean flagActivo;
	
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	public Long getTipoNivel() {
		return tipoNivel;
	}
	public void setTipoNivel(Long tipoNivel) {
		this.tipoNivel = tipoNivel;
	}
	public String getNivelDesc() {
		return nivelDesc;
	}
	public void setNivelDesc(String nivelDesc) {
		this.nivelDesc = nivelDesc;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Integer getPerfilId() {
		return perfilId;
	}
	public void setPerfilId(Integer perfilId) {
		this.perfilId = perfilId;
	}
	public String getPerfilDesc() {
		return perfilDesc;
	}
	public void setPerfilDesc(String perfilDesc) {
		this.perfilDesc = perfilDesc;
	}
	public boolean isFlagActivo() {
		return flagActivo;
	}
	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}

}
