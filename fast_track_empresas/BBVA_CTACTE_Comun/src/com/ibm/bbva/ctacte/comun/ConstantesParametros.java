package com.ibm.bbva.ctacte.comun;

public interface ConstantesParametros {

	String SERVIDOR_SERV_WEB = "hostServidorWeb";
	
	String PUERTO_SERV_WEB = "puertoServidorWeb";
	
	String CARPETA_CLIENTE_DESCARGADOS = "appletCarpetaDescargados";
	
	String CARPETA_CLIENTE_TRANSFERENCIAS = "appletCarpetaTransferencias";
	
	String CARPETA_CLIENTE_LOG = "appletCarpetaLog";
	
	String SERVIDOR_FTP = "hostServidorFTP";
	
	String USUARIO_FTP = "usuarioFTP";
	
	String PASSWORD_FTP = "passwordFTP";
	
	String PERIODO_TRANSFERENCIA_FTP = "periodoTranfFTP";
	
	String TASA_KBYTES_FTP = "tasaKBytes";
	
	String WEB_SFP = "webSFP";
	
	String PUERTO_FTP = "puertoFTP";
	
	String SERVIDOR_CONTENT = "hostServidorContent";
	
	String PUERTO_CONTENT = "puertoServidorContent";

	String SERVIDOR_PROCESS = "servidorProcess";
	
	String PUERTO_PROCESS = "puertoProcess";

	String SERVIDOR_CONV_PDF = "hostServidorConvPDF";
	
	String PUERTO_CONV_PDF = "puertoServidorConvPDF";

	String SERVICIO_SFP = "endpointServicioSFP";

	String SERVICIO_CORE = "endpointServicioCore";

	String SERVICIO_PAGO_SERVICIO = "endpointServicioPagoServicio";

	String SERVICIO_QSP5 = "endpointServicioQSP5";
	
	String SERVICIO_CONTENT = "endpointServicioContent";
	
	String SERVICIO_CONTENT_PID = "endpointServicioContentPid";

	String PAGINA_ASOCIACION_FIRMAS = "paginaAsociacionFirmas";

	String PAGINA_REALIZAR_BASTANTEO = "paginaRealizarBastanteo";
	
	String PAGINA_CONSULTA_PODERES = "paginaConsultaPoderes";
	
	String RUTA_CONVERSION = "rutaConversion";
	
	String URL_ESCANEO_WEB = "paginaEscaneoWeb";
	
	String EMAIL_MESA_DOCUMENTOS = "emailMesaDocumentos";
	
	String EMAIL_MESA_FIRMAS = "emailMesaFirmas";
	
	String HOST_SERVIDOR_CORREO = "hostServidorCorreo";
	
	String PUERTO_SERVIDOR_CORREO = "puertoServidorCorreo";
	
	String EMAIL_REMITENTE = "emailRemitente";
	
	String URL_MANUAL_USUARIO = "paginaManualUsuario";
	
	String CODIGO_TIPO_PJ_MIGRACION = "codigoTipoPJMigracion";
	
	String CARPETA_FTP = "carpetaFTP";
	
	String TAMANO_MAXIMO_ARCHIVO_MB = "appletTamanoMaxArchivoMB";
	
	String NUMERO_REINTENTOS = "appletNumReintentos";
	
	String JUNCTION_NAME_CTACTE = "junctionNameCtaCte";

	String PAGINA_INICIO_CTACTE = "paginaInicioCtaCte";
	
	String INTENTOS_WS_CTACTE = "intentosWSCtaCte";
	
	/** Par�metros - LDAP_CARGA **/

	String FRECUENCIA_DIAS = "empleados.frecuenciaDias";

	String HORA_EJECUCION = "empleados.horaEjecucion";
	
	String LDAP_CARGA_FLAG_ACTIVO = "empleados.ldapCargaActivo";
	
	
	String CARGA_LDAP_WS = "cargaEmpleadosSW";
	
	String CARGOS_LDAP = "cargosLdapAdmitidos";
	
	String LISTA_NEGRA_CARGA = "listaNegraEmpleados";
	
	String CODIGO_PRODUCTO_BASTANTEO = "codigoProductoBastanteo";
	
	String ID_MOTIVO = "idMotivoReasignacion";
	
	String CANTIDAD_REGISTROS = "cantidadRegistros";
	
	String URL_CARGA_LDAP_SERVLET = "cargaLdapServlet";
	
	String TAREAS_REASIGNACION_EXPEDIENTE = "tareasReasignarExpediente";
	
	String UNICO_PERFIL = "unicoPerfilConExpedientes";
	
	String RUTA_PLANTILLA_REPORTE = "rutaPlantillaReporte";
	
	/** Par�metros - CIERRE AUTOMATICO EXPEDIENTE **/
		
	String CIERRE_AUTO_FLAG_ACTIVO = "cierreAuto.jobActivo";

	String CIERRE_AUTO_DIAS_ANTIGUEDAD = "cierreAuto.numDiasAntiguedad";
	
	/** Parametros Consulta y Reclamo**/
	String CODIGO_CONF="CONF";
	String LISTA_CLASIFICACION = "listaClasificacion";

	String TAMANO_ARCHIVO_RC ="tamanoArchivoRC";
	String LINK_SFP_RC ="linkSFP";
}
