package com.ibm.bbva.tabla.util.vo;

import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.Perfil;

public class DestinatariosVO {
	
	private boolean seleccion;
	//private DatosCorreoDestin detalleCorreo;
	private String strPerfil;
	private String strCodigoEmpleado;
	private String strCorreo;
	private String idDestinatarios;
	public boolean isSeleccion() {
		return seleccion;
	}
	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	
//	public DatosCorreoDestin getDetalleCorreo() {
//		return detalleCorreo;
//	}
//	public void setDetalleCorreo(DatosCorreoDestin detalleCorreo) {
//		this.detalleCorreo = detalleCorreo;
//	}
	public String getStrPerfil() {
		return strPerfil;
	}
	public void setStrPerfil(String strPerfil) {
		this.strPerfil = strPerfil;
	}
	public String getStrCodigoEmpleado() {
		return strCodigoEmpleado;
	}
	public void setStrCodigoEmpleado(String strCodigoEmpleado) {
		this.strCodigoEmpleado = strCodigoEmpleado;
	}
	public String getStrCorreo() {
		return strCorreo;
	}
	public void setStrCorreo(String strCorreo) {
		this.strCorreo = strCorreo;
	}
	public String getIdDestinatarios() {
		return idDestinatarios;
	}
	public void setIdDestinatarios(String idDestinatarios) {
		this.idDestinatarios = idDestinatarios;
	}
	
	
	
	
	

}
