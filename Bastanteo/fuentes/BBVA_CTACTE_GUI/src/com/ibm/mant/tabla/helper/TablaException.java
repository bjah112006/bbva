package com.ibm.mant.tabla.helper;

import com.ibm.as.core.exception.GenericException;

public class TablaException extends GenericException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4751697914478014264L;

	public TablaException(Exception ex) {
		super(ex);
	}
}
