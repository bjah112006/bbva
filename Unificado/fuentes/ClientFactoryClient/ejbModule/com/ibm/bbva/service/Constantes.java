package com.ibm.bbva.service;

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
	
	String SERVICIO_CONVERT = "http://9.6.99.46:9080/BBVA_ConvertFiles/services/ConvertirArchivos";
	String RUTA_DESCARGA_PDF = "c:\\temp";
	String EXTENSION_NOMBRE_TEMPORAL_PDF = "TMP";
	
	String VERIFICAR_NUMERO_CONTRATO_OK = "00";
	
	int ID_APLICATIVO_TC = 1;
	int ID_APLICATIVO_PLD = 2;
	
	int COD_APLICATIVO_WFTARJETAS = 6;
	int COD_APLICATIVO_SCE = 7;
	int COD_APLICATIVO_TIPOCAMBIO = 8;
	int COD_APLICATIVO_HAREC = 9;
	int COD_APLICATIVO_RENIEC = 10;
	int COD_ENVIOMAIL_HOST = 11;
	int COD_ENVIOMAIL_PUERTO = 12; 
	int COD_ENVIOMAIL_REMITENTE = 13;
	int COD_APLICATIVO_TABLAGENERAL = 14;
	int COD_APLICATIVO_PACKPYME= 15;
			
	
	String NOMBRE_VARIABLE_WFTARJETAS = "WFTARJETAS_SERVICE_URL";
	String NOMBRE_VARIABLE_SCE = "SCE_SERVICE_URL";
	String NOMBRE_VARIABLE_TIPOCAMBIO = "TIPOCAMBIO_SERVICE_URL";
	String NOMBRE_VARIABLE_HAREC = "HAREC_SERVICE_URL";
	String NOMBRE_VARIABLE_RENIEC = "RENIEC_SERVICE_URL";
	String NOMBRE_VARIABLE_TABLAGENERAL= "TABLAGENERAL_SERVICE_URL";
	String NOMBRE_VARIABLE_PACKPYME = "PACKPYME_SERVICE_URL";
	String NOMBRE_VARIABLE_MAILHOST = "ENVIO_MAIL_HOST";
	String NOMBRE_VARIABLE_MAILPUERTO = "ENVIO_MAIL_PUERTO";
	String NOMBRE_VARIABLE_MAILREMITENTE = "ENVIO_MAIL_REMITENTE";

	/**
	 * Constantes PackPyme Header
	 */
	String PACKPYME_CABECERA_CODEMPRESA = "CABECERA_CODEMPRESA";
	String PACKPYME_CABECERA_CODTERMINALEMPRESA = "CABECERA_CODTERMINALEMPRESA";
	String PACKPYME_CABECERA_CANAL = "CABECERA_CANAL";
	String PACKPYME_CABECERA_CODAPLICACION = "CABECERA_CODAPLICACION";
	String PACKPYME_CABECERA_IDPETICIONEMPRESA = "CABECERA_IDPETICIONEMPRESA";
	String PACKPYME_CABECERA_IDPETICIONBANCO = "CABECERA_IDPETICIONBANCO";
	String PACKPYME_CABECERA_IDOPERACION = "CABECERA_IDOPERACION";
	String PACKPYME_CABECERA_IDSERVICIO = "CABECERA_IDSERVICIO";
	String PACKPYME_CABECERA_IDINTERCONEXION = "CABECERA_IDINTERCONEXION";
	
	String PACKPYME_RESPUESTA_CONTRATO_NO_CORRESPONDE_CLIENTE= "10";
	String PACKPYME_RESPUESTA_CONTRATO_NO_ENCONTRADO= "20";
	String PACKPYME_RESPUESTA_ERROR_ACCESO= "99";
	String PACKPYME_RESPUESTA_CONTRATO_ENCONTRADO= "00";
}
