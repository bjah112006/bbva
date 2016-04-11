package com.ibm.bbva.ctacte.exepciones;

public class ErrorObteniendoDatosException extends Exception {

	private static final long serialVersionUID = -2971126822302019716L;

	public ErrorObteniendoDatosException() {
		super();
	}

	public ErrorObteniendoDatosException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorObteniendoDatosException(String message) {
		super(message);
	}

	public ErrorObteniendoDatosException(Throwable cause) {
		super(cause);
	}

}
