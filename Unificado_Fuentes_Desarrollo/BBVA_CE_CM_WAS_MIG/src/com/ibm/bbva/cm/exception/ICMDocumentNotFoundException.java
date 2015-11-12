/**
 * Created on 13/11/2009
 * Created by mmedina
 */
package com.ibm.bbva.cm.exception;

/**
 * Excepcion producida cuando no se encuentra un documento en ICM.
 * 
 * @author mmedina
 * @version 1.0
 * 
 */
public class ICMDocumentNotFoundException extends ICMException {
	private static final long serialVersionUID = 4917487290401376998L;

	/**
	 * 
	 */
	public ICMDocumentNotFoundException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ICMDocumentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ICMDocumentNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ICMDocumentNotFoundException(Throwable cause) {
		super(cause);
	}
}
