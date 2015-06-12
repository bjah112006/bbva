package com.ibm.bbva.service.bean;


public class PerfilDTO {
	
	private long id;

	private String codigo;

	private String descripcion;

	private String flagAdministracion;

	private String flagAsignacion;

	private String flagRegistraAyu;

	private String flagRegistraExp;

	private String listaCorreoJefes;

	private long idPerfil;

	private String nombrePerfil;

	public PerfilDTO(long id, String codigo, String descripcion, String flagAdministracion, String flagAsignacion, 
			String flagRegistraAyu, String flagRegistraExp, String listaCorreoJefes, long idPerfil, String nombrePerfil){
		this.id=id;
		this.codigo=codigo;
		this.descripcion=descripcion;
		this.flagAdministracion=flagAdministracion;
		this.flagAsignacion=flagAsignacion;
		this.flagRegistraAyu=flagRegistraAyu;
		this.flagRegistraExp=flagRegistraExp;
		this.listaCorreoJefes=listaCorreoJefes;
		this.idPerfil=idPerfil;
		this.nombrePerfil=nombrePerfil;
	}
	
	public PerfilDTO(long id, String codigo, String descripcion, String flagAdministracion, String flagAsignacion, 
			String flagRegistraAyu, String flagRegistraExp, String listaCorreoJefes){
		this.id=id;
		this.codigo=codigo;
		this.descripcion=descripcion;
		this.flagAdministracion=flagAdministracion;
		this.flagAsignacion=flagAsignacion;
		this.flagRegistraAyu=flagRegistraAyu;
		this.flagRegistraExp=flagRegistraExp;
		this.listaCorreoJefes=listaCorreoJefes;
	}
	
	public PerfilDTO(){
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFlagAdministracion() {
		return flagAdministracion;
	}

	public void setFlagAdministracion(String flagAdministracion) {
		this.flagAdministracion = flagAdministracion;
	}

	public String getFlagAsignacion() {
		return flagAsignacion;
	}

	public void setFlagAsignacion(String flagAsignacion) {
		this.flagAsignacion = flagAsignacion;
	}

	public String getFlagRegistraAyu() {
		return flagRegistraAyu;
	}

	public void setFlagRegistraAyu(String flagRegistraAyu) {
		this.flagRegistraAyu = flagRegistraAyu;
	}

	public String getFlagRegistraExp() {
		return flagRegistraExp;
	}

	public void setFlagRegistraExp(String flagRegistraExp) {
		this.flagRegistraExp = flagRegistraExp;
	}

	public String getListaCorreoJefes() {
		return listaCorreoJefes;
	}

	public void setListaCorreoJefes(String listaCorreoJefes) {
		this.listaCorreoJefes = listaCorreoJefes;
	}

	public long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getNombrePerfil() {
		return nombrePerfil;
	}

	public void setNombrePerfil(String nombrePerfil) {
		this.nombrePerfil = nombrePerfil;
	}

	
}
