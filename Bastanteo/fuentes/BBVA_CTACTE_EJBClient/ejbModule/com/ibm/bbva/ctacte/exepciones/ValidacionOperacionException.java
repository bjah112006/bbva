package com.ibm.bbva.ctacte.exepciones;

public class ValidacionOperacionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public enum Tipo {SFP, CORE};
	
	private Tipo tipo;

	public ValidacionOperacionException(Tipo tipo) {
		super();
		this.tipo = tipo;
	}

	public ValidacionOperacionException(String message, Throwable cause, Tipo tipo) {
		super(message, cause);
		this.tipo = tipo;
	}

	public ValidacionOperacionException(String message, Tipo tipo) {
		super(message);
		this.tipo = tipo;
	}

	public ValidacionOperacionException(Throwable cause, Tipo tipo) {
		super(cause);
		this.tipo = tipo;
	}

	public Tipo getTipo () {
		return tipo;
	}
}
