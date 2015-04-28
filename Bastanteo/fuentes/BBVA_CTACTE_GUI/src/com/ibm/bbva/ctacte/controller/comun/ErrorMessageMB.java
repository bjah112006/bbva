package com.ibm.bbva.ctacte.controller.comun;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "ErrorMessageMB")
@ViewScoped
public class ErrorMessageMB {

	private String mensajeError;
	
	public ErrorMessageMB()
	{
		
	}
	
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
}
