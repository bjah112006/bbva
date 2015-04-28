package com.ibm.mant.tabla.service;

public interface Constantes {

	/**
	 * Para indicar que se ubico en la busqueda
	 */
	int CODIGO_UBICADO = 0;//antes 0
	/**
	 * Para indicar que no se ubico
	 */
	int CODIGO_NO_UBICADO = 10;
	/**
	 * Para indicar error en la conexion en el servicio
	 */
	int CODIGO_ERROR_CONEXION = 20;
	/**
	 * Para indicar que existe error en la trama de retorno
	 */
	int CODIGO_ERROR_TRAMA = 30;
	/**
	 * Para indicar que el codigo del error se desconoce
	 */
	int CODIGO_ERROR_CODIGO_DESCONOCIDO = 40;
	
	
	/**
	 * Codigo para el campo vacio de la lista
	 */
	int CODIGO_CODIGO_CAMPO_VACIO = -1;	
	}
