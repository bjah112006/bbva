package com.ibm.as.core.helpers;

/**
 * Enumeraci�n de los posibles tipos de datos de las columnas de las tablas
 * param�tricas
 */
public enum TipoDato {

	STRING("STRING", "Cadena de Caracteres",null), 
	ALFANUMERICO("ALFANUMERICO", "Cadena de Caracteres",null), 
	LISTA("LISTA", "Lista de Valores",null), 
	SHORT("SHORT", "Entero", "M�nimo " + Short.MIN_VALUE + " M�ximo " + Short.MAX_VALUE),
	INTEGER("INTEGER", "Entero", "M�nimo " + Integer.MIN_VALUE + " M�ximo " + Integer.MAX_VALUE), 
	DOUBLE("DOUBLE", "Decimal", "M�nimo " + Double.MIN_VALUE + " M�ximo " + Double.MAX_VALUE), 
	FLOAT("FLOAT", "Decimal", "M�nimo " + Float.MIN_VALUE + " M�ximo " + Float.MAX_VALUE),
	LONG("LONG","Entero", "M�nimo " + Long.MIN_VALUE + " M�ximo " + Long.MAX_VALUE), 
	DATE("DATE", "Fecha","Fecha (v�lida seg�n calendario)"),
	BOOLEAN("BOOLEAN", "Valores 1 � 0", null),
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
	 * No se incluye en la lista del enumeration debido a que no se debe ver reflejado en las tablas param�tricas.
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
