package com.ibm.bbva.ctacte.exepciones;

public class ConexionServicioException extends Exception {

	private static final long serialVersionUID = -2668890608366652202L;

	public enum Tipo {CORE, SPF};
	private Tipo tipo;
	
	public ConexionServicioException(Tipo tipo) {
		super();
		this.tipo = tipo;
	}

	public ConexionServicioException(String message, Throwable cause, Tipo tipo) {
		super(message, cause);
		this.tipo = tipo;
	}

	public ConexionServicioException(String message, Tipo tipo) {
		super(message);
		this.tipo = tipo;
	}

	public ConexionServicioException(Throwable cause, Tipo tipo) {
		super(cause);
		this.tipo = tipo;
	}

	public Tipo getTipo() {
		return tipo;
	}

}
