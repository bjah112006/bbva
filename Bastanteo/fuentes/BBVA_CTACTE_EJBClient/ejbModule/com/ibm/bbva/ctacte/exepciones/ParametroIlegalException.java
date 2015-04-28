package com.ibm.bbva.ctacte.exepciones;

public class ParametroIlegalException extends Exception {

	private static final long serialVersionUID = 3900454299714131777L;

	public ParametroIlegalException() {
		super();
	}

	public ParametroIlegalException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParametroIlegalException(String message) {
		super(message);
	}

	public ParametroIlegalException(Throwable cause) {
		super(cause);
	}

}
