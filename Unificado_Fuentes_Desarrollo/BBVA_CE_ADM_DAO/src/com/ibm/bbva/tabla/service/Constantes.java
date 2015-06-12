package com.ibm.bbva.tabla.service;

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
	 * Codigo del retorno del servicio BBVA cuando existe el cliente
	 */
	String RESPUESTA_BBVA_CLIENTE = "000";
	String RESPUESTA_BBVA_CLIENTE_2 = "003";
	/**
	 * Codigo del retorno del servicio BBVA cuando no existe el cliente
	 */
	String RESPUESTA_BBVA_NO_CLIENTE = "004";
	/**
	 * Codigo del retorno del servicio RENIEC cuando existe el numero del documento
	 */
	String RESPUESTA_RENIEC_EXISTE = "0000";
	/**
	 * Codigo del retorno del servicio RENIEC cuando no existe el numero del documento
	 */
	String RESPUESTA_RENIEC_NO_EXISTE = "5200";
	/**
	 * Separador
	 */
	String VV_SEPARADOR = "|";
	/**
	 * Separador Escaner
	 */
	String SEPARADOR_ESCANER = ";";
	/**
	 * Codigo para el campo vacio de la lista
	 */
	int CODIGO_CODIGO_CAMPO_VACIO = -1;	
	/**
	 * Codigo para indicar que se subio correctamente el documento
	 */
	String CODIGO_VALIDO_DOCUMENTO = "1";	
	/**
	 * Codigo para indicar que se subio erroneamente el documento
	 */
	String CODIGO_ERROR_DOCUMENTO = "0";
	/**
	 * Codigo para indicar que la tarea se realizo correctamente
	 */
	String CODIGO_VALIDO_TAREA = "1";	
	/**
	 * Codigo para indicar que la tarea no se realizo correctamente
	 */
	String CODIGO_ERROR_TAREA = "0";
	
	/**
	 * Constantes Reniec Cabecera
	 * **/
	String RENIEC_CABECERA_VERSION = "0001";
	Integer RENIEC_CABECERA_LONG_CABECERA = 128;
	String RENIEC_CABECERA_TIPO_SERVICIO = "000";
	Integer RENIEC_CABECERA_LONG_TOTAL_TRAMA = 143;
    String RENIEC_CABECERA_FRAGMENTACION ="                      ";
    Integer RENIEC_CABECERA_TTL = 0;
    String RENIEC_CABECERA_TIPO_CONSULTA = "7";
    String RENIEC_CABECERA_CARACVERIFICACION = "RENIECPERURENIEC";
    String RENIEC_CABECERA_CODINSTSOLICITANTE = "LD2019";
    String RENIEC_CABECERA_CODSERVRENIEC = "RENIEC001";
    String RENIEC_CABECERA_HOSTFINALINSTSOL = "BBVA";
    String RENIEC_CABECERA_RESERVADO = "          ";
	/**
	 * Constantes Reniec SubTrama
	 * **/    
    String RENIEC_SUBTRAMA_RESERVADO ="     ";
    String RENIEC_SUBTRAMA_CARACVERIFICACION = " ";
	String RENIEC_SUBTRAMA_TIPO_DOCUMENTO = " ";
	
	/**
	 * Flag de Dependencia 1= Tiene una lista dependendiente.
	 * **/
	String FLAG_DEPENDENCIA = "1";
	
	int ID_APLICATIVO_TC = 1;
	int ID_APLICATIVO_PLD = 2;
	
	String SCHEMA_TABLAS_MANTENIMIENTO = "CONELE";
	
	String TBL_CE_IBM_EMPLEADO_CE = "TBL_CE_IBM_EMPLEADO_CE";
	String CAMPO_CODIGO_TBL_CE_IBM_EMPLEADO_CE ="CODIGO";
	String CAMPO_ID_TBL_CE_IBM_EMPLEADO_CE = "ID";
	String CAMPO_FLAG_ACTIVO_TBL_CE_IBM_EMPLEADO_CE = "FLAG_ACTIVO";
	String CAMPO_ID_PERFIL_FK_TBL_CE_IBM_EMPLEADO_CE = "ID_PERFIL_FK";
		
	String TBL_CE_IBM_CART_EMPLEADO_CE = "TBL_CE_IBM_CART_EMPLEADO_CE";
	String CAMPO_ID_CARTERIZACION_FK_TBL_CE_IBM_CART_EMPLEADO_CE = "ID_CARTERIZACION_FK";
	String CAMPO_ID_EMPLEADO_FK_TBL_CE_IBM_CART_EMPLEADO_CE = "ID_EMPLEADO_FK";
	String CAMPO_FLAG_ACTIVO_TBL_CE_IBM_CART_EMPLEADO_CE = "FLAG_ACTIVO";
	
	String TBL_CE_IBM_TOE_PERFIL_ESTADO = "TBL_CE_IBM_TOE_PERFIL_ESTADO";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_PRODUCTO = "ID_PRODUCTO_FK";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_TAREA = "ID_TAREA_FK";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_PERFIL = "ID_PERFIL_FK";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_ESTADO = "ID_ESTADO_FK";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_OFERTA = "ID_TIPO_OFERTA_FK";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_FLUJO = "TIPO_FLUJO";
	String CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_COLUMNA = "NOM_COLUMNA";
	
	String FLAG_ACTIVO_UNO = "1";
	String FLAG_ACTIVO_CERO = "0";
	
	int COD_TABLA_GUIA_DOCUMENTARIA=5;
	int COD_TABLA_OFICINA=17;
	int COD_TABLA_EMPLEADO=18;
	int COD_TABLA_CARTERIZACION_TERRITORIO=58;
	int COD_TABLA_CARTERIZACION_EMPLEADO=59;
	
}
