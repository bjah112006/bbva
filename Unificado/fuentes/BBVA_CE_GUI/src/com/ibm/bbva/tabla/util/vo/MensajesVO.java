package com.ibm.bbva.tabla.util.vo;

import java.sql.Blob;

public class MensajesVO {
	private String idMensajes;
	private boolean seleccion;
	private String strDescripcion;
	private String strContenido;
	
	
	public String getIdMensajes() {
		return idMensajes;
	}
	public void setIdMensajes(String idMensajes) {
		this.idMensajes = idMensajes;
	}
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	public String getStrDescripcion() {
		return strDescripcion;
	}
	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}
	public String getStrContenido() {
		return strContenido;
	}
	public void setStrContenido(String strContenido) {
		this.strContenido = strContenido;
	}
	
	
}
