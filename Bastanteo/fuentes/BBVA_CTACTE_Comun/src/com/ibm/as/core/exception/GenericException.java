/**
 * 
 */
package com.ibm.as.core.exception;

/**
 * @author gsalinas
 * 
 */
public class GenericException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4262123069352690537L;

	public GenericException() {
		super();
	}
	
	public GenericException(String message) {
		super(message);
	}

	public GenericException(Throwable ex) {
		super(ex);
	}


	public GenericException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
