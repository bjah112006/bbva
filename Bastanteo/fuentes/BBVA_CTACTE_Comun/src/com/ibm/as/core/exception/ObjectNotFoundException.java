package com.ibm.as.core.exception;

public class ObjectNotFoundException extends GenericException {

	private static final long serialVersionUID = -6513464279795410995L;
	
	public ObjectNotFoundException(){
		super();
	}
	
	public ObjectNotFoundException(String message){
		super(message);
	}

}
