package com.ibm.mant.tabla.vo;

import java.util.Date;

public class EmpleadoVO {
	
	private boolean seleccion;
	
	private Long id;
	
	private String codigo;
	private String nombres;
	private String apepat;
	private String apemat;
	private Long idPerfilFk;
	private Date fecing;
	private Date fecegr;
	private Long idOficinaFk;
	private String nombresCompletos;
	private Long idNivelFk;
	private Long idTipoCategoriaFk;
	private String correo;
	private boolean flagActivo;
	private boolean flagAbogado;
	private String codOficina;
	
	private String perfil;
	private String oficina;
	private String estudioAbogado;
	
	private String codcargo;
	private String nomcargo;
	
	private Date fechaVigencia;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApepat() {
		return apepat;
	}
	public void setApepat(String apepat) {
		this.apepat = apepat;
	}
	public String getApemat() {
		return apemat;
	}
	public void setApemat(String apemat) {
		this.apemat = apemat;
	}
	public Date getFecing() {
		return fecing;
	}
	public void setFecing(Date fecing) {
		this.fecing = fecing;
	}
	public Date getFecegr() {
		return fecegr;
	}
	public void setFecegr(Date fecegr) {
		this.fecegr = fecegr;
	}
	public String getNombresCompletos() {
		return nombresCompletos;
	}
	public void setNombresCompletos(String nombresCompletos) {
		this.nombresCompletos = nombresCompletos;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public boolean getFlagActivo() {
		return flagActivo;
	}
	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}
	public Long getIdPerfilFk() {
		return idPerfilFk;
	}
	public void setIdPerfilFk(Long idPerfilFk) {
		this.idPerfilFk = idPerfilFk;
	}
	public Long getIdOficinaFk() {
		return idOficinaFk;
	}
	public void setIdOficinaFk(Long idOficinaFk) {
		this.idOficinaFk = idOficinaFk;
	}
	public Long getIdNivelFk() {
		return idNivelFk;
	}
	public void setIdNivelFk(Long idNivelFk) {
		this.idNivelFk = idNivelFk;
	}
	public Long getIdTipoCategoriaFk() {
		return idTipoCategoriaFk;
	}
	public void setIdTipoCategoriaFk(Long idTipoCategoriaFk) {
		this.idTipoCategoriaFk = idTipoCategoriaFk;
	}
	public String getCodOficina() {
		return codOficina;
	}
	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
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
	public String getEstudioAbogado() {
		return estudioAbogado;
	}
	public void setEstudioAbogado(String estudioAbogado) {
		this.estudioAbogado = estudioAbogado;
	}
	public boolean isFlagAbogado() {
		return flagAbogado;
	}
	public void setFlagAbogado(boolean flagAbogado) {
		this.flagAbogado = flagAbogado;
	}
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	public String getCodcargo() {
		return codcargo;
	}
	public void setCodcargo(String codcargo) {
		this.codcargo = codcargo;
	}
	public String getNomcargo() {
		return nomcargo;
	}
	public void setNomcargo(String nomcargo) {
		this.nomcargo = nomcargo;
	}
	public Date getFechaVigencia() {
		return fechaVigencia;
	}
	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	
}
