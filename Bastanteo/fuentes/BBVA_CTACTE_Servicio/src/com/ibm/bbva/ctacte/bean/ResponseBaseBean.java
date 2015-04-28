package com.ibm.bbva.ctacte.bean;

import java.io.Serializable;

import com.ibm.bbva.ctacte.servicio.util.Constantes;

public class ResponseBaseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7370952116549674806L;
	private String errorCode;
	private String errorDesc;
	
	public ResponseBaseBean() {
		// TODO Apéndice de constructor generado automáticamente
	}
	
	public ResponseBaseBean(boolean success) {
		if(success){
			errorCode = Constantes.ERROR_COD_SUCCESS;
			errorDesc = Constantes.ERROR_DESC_SUCCESS;
		}else{
			errorCode = Constantes.ERROR_COD_ERROR;
			errorDesc = Constantes.ERROR_DESC_ERROR;
		}
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	
}
