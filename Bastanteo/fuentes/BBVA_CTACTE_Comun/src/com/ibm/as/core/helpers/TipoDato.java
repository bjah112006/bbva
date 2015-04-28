package com.ibm.as.core.helpers;

/**
 * Enumeración de los posibles tipos de datos de las columnas de las tablas
 * paramétricas
 */
public enum TipoDato {

	STRING("STRING", "Cadena de Caracteres",null), 
	ALFANUMERICO("ALFANUMERICO", "Cadena de Caracteres",null), 
	LISTA("LISTA", "Lista de Valores",null), 
	SHORT("SHORT", "Entero", "Mínimo " + Short.MIN_VALUE + " Máximo " + Short.MAX_VALUE),
	INTEGER("INTEGER", "Entero", "Mínimo " + Integer.MIN_VALUE + " Máximo " + Integer.MAX_VALUE), 
	DOUBLE("DOUBLE", "Decimal", "Mínimo " + Double.MIN_VALUE + " Máximo " + Double.MAX_VALUE), 
	FLOAT("FLOAT", "Decimal", "Mínimo " + Float.MIN_VALUE + " Máximo " + Float.MAX_VALUE),
	LONG("LONG","Entero", "Mínimo " + Long.MIN_VALUE + " Máximo " + Long.MAX_VALUE), 
	DATE("DATE", "Fecha","Fecha (válida según calendario)"),
	BOOLEAN("BOOLEAN", "Valores 1 ó 0", null),
	BLOB("BLOB","Estructura XML",null);

	//private final String nombre;
	private final String nombreMostrar;
	private final String mensajeValidez;

	private TipoDato(String nombre, String nombreMostrar, String mensajeValidez) {
		//this.nombre = nombre;
		this.nombreMostrar = nombreMostrar;
		this.mensajeValidez = mensajeValidez;
	}
	
//	public String toString() {
//		return this.nombre;
//	}

	/*
	 * Retorna el String asociado al tipo numerico de base de datos. 
	 * No se incluye en la lista del enumeration debido a que no se debe ver reflejado en las tablas paramétricas.
	 */
	public static String getNumberString(){
		return "NUMBER";
	}
	
	public String getNombreMostrar() {
		return this.nombreMostrar;
	}
	
	public String getMensajeValidez() {
		return this.mensajeValidez;
	}
}
