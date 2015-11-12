/**
 * Created on 13/11/2009
 * Created by mmedina
 */
package com.ibm.bbva.cm.exception;

/**
 * Excepcion general de las operaciones en ICM.
 * 
 * @author mmedina
 * @version 1.0
 * 
 */
public class ICMException extends RuntimeException {
	private static final long serialVersionUID = -3607397792109145195L;

	/**
	 * 
	 */
	public ICMException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ICMException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ICMException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ICMException(Throwable cause) {
		super(cause);
	}

}
