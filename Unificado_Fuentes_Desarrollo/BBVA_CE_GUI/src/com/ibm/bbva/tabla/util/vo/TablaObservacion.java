package com.ibm.bbva.tabla.util.vo;

public class TablaObservacion {
	private String codigoUsuario;
	private String perfilUsuario;
	private String fecha;
	private String hora;	
	private String comentario;
	private String comentario_motivo;
	private String comentario_rechazo;
	
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFecha() {
		return fecha;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getHora() {
		return hora;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getComentario() {
		return comentario;
	}
	public String getComentario_motivo() {
		return comentario_motivo;
	}
	public void setComentario_motivo(String comentario_motivo) {
		this.comentario_motivo = comentario_motivo;
	}
	public String getComentario_rechazo() {
		return comentario_rechazo;
	}
	public void setComentario_rechazo(String comentario_rechazo) {
		this.comentario_rechazo = comentario_rechazo;
	}
	
	
	
	
	
}
