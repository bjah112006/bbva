package com.ibm.bbva.tabla.util.vo;

import java.sql.Blob;

import com.ibm.bbva.entities.Accion;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Tarea;


public class ConsultaCorreoVO {
	private String idDatosCorreo;
	private String asunto;
	private String cuerpo;
	private String idTarea;
	private String producto;
	private String tarea;
	private String accion;
	private boolean flagActivo;
	private boolean seleccion;
	
	public String getIdDatosCorreo() {
		return idDatosCorreo;
	}
	public void setIdDatosCorreo(String idDatosCorreo) {
		this.idDatosCorreo = idDatosCorreo;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}	
	public String getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	public String getIdTarea() {
		return idTarea;
	}
	
	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
	}
	
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getTarea() {
		return tarea;
	}
	public void setTarea(String tarea) {
		this.tarea = tarea;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public boolean isFlagActivo() {
		return flagActivo;
	}
	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}
	
	
	
	
	
}
