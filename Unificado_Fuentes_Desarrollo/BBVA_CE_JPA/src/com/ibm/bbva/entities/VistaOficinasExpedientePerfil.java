package com.ibm.bbva.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="VIEW_CE_IBM_OFICINA_EXP_PERFIL", schema = "CONELE")
@NamedQueries({
	@NamedQuery(name="VistaOficinasExpedientePerfil.findAll", query="SELECT v FROM VistaOficinasExpedientePerfil v"),
	@NamedQuery(name="VistaOficinasExpedientePerfil.findByIdPerfil", query="SELECT v FROM VistaOficinasExpedientePerfil v WHERE v.id_perfil = :id_perfil GROUP BY v.id_oficina, v.cod_oficina, v.descrip_oficina, v.id_perfil")
})
public class VistaOficinasExpedientePerfil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public VistaOficinasExpedientePerfil(){
	}
	
	@Id
	@Column(name="ID_OFICINA")
	private long id_oficina;
	@Column(name="COD_OFICINA")
	private String cod_oficina;
	@Column(name="DESCRIP_OFICINA")
	private String descrip_oficina;
	@Column(name="ID_PERFIL")
	private long id_perfil;

	public long getId_oficina() {
		return id_oficina;
	}
	public void setId_oficina(long id_oficina) {
		this.id_oficina = id_oficina;
	}
	public String getCod_oficina() {
		return cod_oficina;
	}
	public void setCod_oficina(String cod_oficina) {
		this.cod_oficina = cod_oficina;
	}
	public String getDescrip_oficina() {
		return descrip_oficina;
	}
	public void setDescrip_oficina(String descrip_oficina) {
		this.descrip_oficina = descrip_oficina;
	}
	public long getId_perfil() {
		return id_perfil;
	}
	public void setId_perfil(long id_perfil) {
		this.id_perfil = id_perfil;
	}

}
