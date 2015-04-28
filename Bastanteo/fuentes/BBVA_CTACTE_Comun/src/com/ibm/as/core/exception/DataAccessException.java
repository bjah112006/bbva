/**
 * 
 */
package com.ibm.as.core.exception;

/**
 * @author gsalinas
 *
 */
public class DataAccessException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8278194245003709640L;
	
	public DataAccessException(String message) {
		super(message);
	}
	
	public DataAccessException(Exception ex) {
		super(ex);
	}

}
