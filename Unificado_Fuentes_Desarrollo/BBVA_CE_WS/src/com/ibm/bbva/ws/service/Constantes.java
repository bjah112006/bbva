package com.ibm.bbva.ws.service;

public interface Constantes {
	/**
	 * Id de soltero en la tabla ESTADO CIVIL
	 */
	Integer ESTADO_CIVIL_SOLTERO = 1;		
	/**
	 * Id de Casado en la tabla ESTADO CIVIL
	 */
	Integer ESTADO_CIVIL_CASADO = 2;
	/**
	 * Id de Casado en la tabla CONVIVIENTE
	 */
	Integer ESTADO_CIVIL_CONVIVIENTE = 5;

	String TIPO_SEGMENTO_VIP="VIP";
	String CADENA_VACIA="";
	
	String FLAG_ACTIVO="1";
	String FLAG_INACTIVO="0";
	/**
	 * Codigo de Oferta en la tabla TIPO_OFERTA
	 */
	String CODIGO_OFERTA_APROBADO = "1";
	String CODIGO_OFERTA_REGULAR = "2";	

	/**
	 * Delegacion Gerente
	 */
	String CON_DELEGACION_GERENTE = "1";
	String SIN_DELEGACION_GERENTE = "0";	
	
	/**
	 * Simbolos
	 */
	String IGUAL="=";
	String MAYOR=">";
	String MAYOR_QUE=">=";
	String MENOR="<";
	String MENOR_QUE="<=";
	String DIFERENTE="<>";
	
	/**
	 * Perfil por defecto Linea Maxima
	 */
	
	long PERFIL_DEFECTO=8;
	
	String FLAG_ACTIVO_PERFIL="1";
	
	/**
	 * Codigo de Aplicativo en la tabla Producto
	 */	
	int ID_APLICATIVO_TC = 1;
	int ID_APLICATIVO_PLD = 2;
	
	/**
	 * Codigo de Flag Subrogado
	 */
	String CODIGO_FLAG_SUBROGADO_ACTIVO = "1";
	String CODIGO_FLAG_SUBROGADO_INACTIVO = "0";
	
	/**
	 * ID USUARIO ADMINISTRADOR 
	 */	
	Integer ID_USUARIO_ADMINISTRADOR = 1;
	
	
	/**
	 * Perfiles Gerente
	 */	
	Integer ID_PERFIL_GERENTE = 1;
	/**
	 * Perfiles para validar carterizacion id =2 
	 */	
	Integer ID_PERFIL_FORMALIZADOR = 2;
	/**
	 * Perfiles para validar carterizacion id =3
	 */	
	Integer ID_PERFIL_ANALISIS_Y_ALTAS = 3;
	/**
	 * Perfiles para validar carterizacion id =4 
	 */	
	Integer ID_PERFIL_SUPERIOR_DE_RIESGOS = 4;
	/**
	 * Perfiles para validar carterizacion id =5 
	 */	
	Integer ID_PERFIL_MESA_DE_CONTROL = 5;
	/**
	 * Perfiles para validar carterizacion id =6 
	 */	
	Integer ID_PERFIL_EJECUTIVO = 6;
	/**
	 * Perfiles para validar carterizacion id =7 
	 */	
	Integer ID_PERFIL_EJECUTIVO_VIP = 7;
	/**
	 * Perfiles para validar carterizacion id =8 
	 */	
	Integer ID_PERFIL_ANALISIS_DE_RIESGOS = 8;
	
	/**
	 * Perfiles para validar carterizacion id =9 
	 */	
	Integer ID_PERFIL_CALIFICACION_CONTROL = 9;
	/**
	 * Perfiles para validar carterizacion id =10 
	 */	
	Integer ID_PERFIL_CONTROLLER = 10;
	/**
	 * Perfiles para validar carterizacion id =11 
	 */	
	Integer ID_PERFIL_SUPERVISOR = 11;
	/**
	 * Perfiles para validar carterizacion id =12 
	 */	
	Integer ID_PERFIL_JEFE_EQUIPO_CPM = 12;
	/**
	 * Perfiles para validar carterizacion id =13 
	 */	
	Integer ID_PERFIL_SUBGERENTE_OFICINA = 13;
	/**
	 * Perfiles para validar carterizacion id =11 
	 */	
	Integer ID_PERFIL_ADMINISTRADOR = 14;
	
	/**
	 * Codigo de Tipo de Cambio
	 */
	String CODIGO_TIPO_CAMBIO_SOLES = "1";
	
	String CODIGO_TIPO_CAMBIO_DOLARES = "2";
	
	
	/**
	 * Contiene una instancia de la clase EmpleadoVO
	 */
	String USUARIO_SESION = "usrSesion";
	
	
	/**
	 * Contiene los parametros de configuracion para el envio de correo
	 */
	int COD_ENVIOMAIL_HOST = 11;
	int COD_ENVIOMAIL_PUERTO = 12; 
	int COD_ENVIOMAIL_REMITENTE = 13;
	String NOMBRE_VARIABLE_MAILHOST = "ENVIO_MAIL_HOST";
	String NOMBRE_VARIABLE_MAILPUERTO = "ENVIO_MAIL_PUERTO";
	String NOMBRE_VARIABLE_MAILREMITENTE = "ENVIO_MAIL_REMITENTE";
	
}