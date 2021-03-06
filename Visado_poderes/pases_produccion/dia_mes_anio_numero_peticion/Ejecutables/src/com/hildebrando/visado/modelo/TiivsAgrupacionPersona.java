package com.hildebrando.visado.modelo;

// Generated 05/12/2012 02:50:20 PM by Hibernate Tools 3.4.0.CR1

/**
 * TiivsAgrupacionPersona generated by hbm2java
 */
public class TiivsAgrupacionPersona implements java.io.Serializable {

	private Integer idAgrupacion;
	private Integer idAgrupacionGrupo;
	private String codSoli;
	private Integer numGrupo;
	private Integer codPer;
	private String tipPartic;
	private String clasifPer;
	private TiivsSolicitudAgrupacion tiivsSolicitudAgrupacion;
	private TiivsPersona tiivsPersona;

	public TiivsAgrupacionPersona() {
	}

	public TiivsAgrupacionPersona(String codSoli,
								  Integer numGrupo,
								  Integer codPer,
								  String tipPartic,
								  String clasifPer,
							      TiivsSolicitudAgrupacion tiivsSolicitudAgrupacion,
								  TiivsPersona tiivsPersona) {
		
		this.codSoli=codSoli;
		this.numGrupo=numGrupo;
		this.codPer=codPer;
		this.tipPartic=tipPartic;
		this.clasifPer=clasifPer;
		this.tiivsSolicitudAgrupacion = tiivsSolicitudAgrupacion;
		this.tiivsPersona = tiivsPersona;
	}

	public TiivsSolicitudAgrupacion getTiivsSolicitudAgrupacion() {
		return this.tiivsSolicitudAgrupacion;
	}

	public void setTiivsSolicitudAgrupacion(
			TiivsSolicitudAgrupacion tiivsSolicitudAgrupacion) {
		this.tiivsSolicitudAgrupacion = tiivsSolicitudAgrupacion;
	}

	public TiivsPersona getTiivsPersona() {
		return this.tiivsPersona;
	}

	public void setTiivsPersona(TiivsPersona tiivsPersona) {
		this.tiivsPersona = tiivsPersona;
	}

	public Integer getIdAgrupacion() {
		return idAgrupacion;
	}

	public void setIdAgrupacion(Integer idAgrupacion) {
		this.idAgrupacion = idAgrupacion;
	}

	public String getCodSoli() {
		return codSoli;
	}

	public void setCodSoli(String codSoli) {
		this.codSoli = codSoli;
	}

	public Integer getNumGrupo() {
		return numGrupo;
	}

	public void setNumGrupo(Integer numGrupo) {
		this.numGrupo = numGrupo;
	}

	public Integer getCodPer() {
		return codPer;
	}

	public void setCodPer(Integer codPer) {
		this.codPer = codPer;
	}

	public String getTipPartic() {
		return tipPartic;
	}

	public void setTipPartic(String tipPartic) {
		this.tipPartic = tipPartic;
	}

	public String getClasifPer() {
		return clasifPer;
	}

	public void setClasifPer(String clasifPer) {
		this.clasifPer = clasifPer;
	}

	public Integer getIdAgrupacionGrupo() {
		return this.idAgrupacionGrupo;
	}

	public void setIdAgrupacionGrupo(Integer idAgrupacionGrupo) {
		this.idAgrupacionGrupo = idAgrupacionGrupo;
	}

	@Override
	public String toString() {
		return "TiivsAgrupacionPersona [idAgrupacion=" + this.idAgrupacion
				+ ", idAgrupacionGrupo=" + this.idAgrupacionGrupo
				+ ", codSoli=" + this.codSoli + ", numGrupo=" + this.numGrupo
				+ ", codPer=" + this.codPer + ", tipPartic=" + this.tipPartic
				+ ", clasifPer=" + this.clasifPer
				+ ", tiivsSolicitudAgrupacion=" + this.tiivsSolicitudAgrupacion
				+ ", tiivsPersona=" + this.tiivsPersona + "]";
	}

	
	
	
}
