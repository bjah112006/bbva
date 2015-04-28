/**
 * 
 */
package com.ibm.as.core.exception;

/**
 * @author gsalinas
 *
 */
public class ServiceLocationException extends GenericException {

	private static final long serialVersionUID = 2844842018561852415L;

	public ServiceLocationException() {
		super();
	}

	public ServiceLocationException(String message) {
		super(message);
	}
	
	public ServiceLocationException(Exception ex) {
		super(ex);
	}

	public ServiceLocationException(String message, Exception e) {
		super(message, e);
	}
}
