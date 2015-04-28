package com.ibm.bbva.controller;


public interface Constantes {

	
	/**
	 * Valor cuando el check es seleccionado
	 */
	String CHECK_SELECCIONADO = "1";
	/**
	 * Valor cuando el check no es seleccionado
	 */
	String CHECK_NO_SELECCIONADO = "0";
	
	/**
	 * Sesión para guardar objetos respecto
	 * al empleado en mantenimiento
	 */
	
	String EMPLEADO_SESION="empleadoSesion";
	
	/**
	 * Codigo para el campo vacio de la lista generada en la clase 
	 * com.ibm.bbva.util.Util
	 */
	String CODIGO_CODIGO_CAMPO_VACIO = "-1";
	
	/**
	 * Sesión usada por AbstractSortPagDataTableMBean
	 */
	String PAGINA_SESSION = "pagSession";
	
	String CARTERIZACION_SESION="carterizacionSesion";
	
	String PERFIL_X_NIVEL_SESION="perfilXNivelSesion";
	
}