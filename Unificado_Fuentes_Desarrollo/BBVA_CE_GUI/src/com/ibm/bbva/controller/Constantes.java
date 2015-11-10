package com.ibm.bbva.controller;

import java.text.SimpleDateFormat;

public interface Constantes {

	/**
	 * Contiene una instancia de la clase EmpleadoVO
	 */
	String USUARIO_SESION = "usrSesion";
	
	String USUARIO_AD_SESION = "usrAdSesion";
	
	/**
	 * Es la oficina del usuario que es una instancia de la clase OficinaVO
	 */
	String OFICINA_SESION = "ofiSesion";
	/**
	 * Codigo para el campo vacio de la lista generada en la clase 
	 * com.ibm.bbva.util.Util
	 */
	String CODIGO_CODIGO_CAMPO_VACIO = "-1";
	/**
	 * Codigo del cliente cuando no se encuentra en el Sistema Nacar
	 */
	String CODIGO_CLIENTE_NUEVO = "9999";
	/**
	 * Codigo del DNI en la tabla TIPO_DOI
	 */
	String CODIGO_TIPO_DOI_DNI = "3";
	/**
	 * Codigo del RUC en la tabla TIPO_DOI
	 */
	String CODIGO_TIPO_DOI_RUC = "6";
	/**
	 * Ruta api GMD
	 */
	String RUTA_GMD = "C:\\Archivos de Programa\\GMD S.A\\API GMD\\API_GMD.exe";
	/**
	 * Codigo del segmento NORMAL en la Tabla SEGMENTO
	 */
	String CODIGO_SEGMENTO_NORMAL = "1";
	/**
	 * Codigo del segmento SIN ASIGNAR en la Tabla SEGMENTO
	 */
	String CODIGO_SEGMENTO_SIN_ASIGNAR = "3";
	/**
	 * Codigo de cliente nuevo en la tabla TIPO_CLIENTE 
	 */
	String CODIGO_TIPO_CLIENTE_NUEVO = "1";
	/**
	 * Codigo de cliente BBVA en la tabla TIPO_CLIENTE
	 */
	String CODIGO_TIPO_CLIENTE_BBVA = "2";
	/**
	 * Valor cuando el check es seleccionado
	 */
	String CHECK_SELECCIONADO = "1";
	/**
	 * Valor cuando el check no es seleccionado
	 */
	String CHECK_NO_SELECCIONADO = "0";
	/**
	 * Codigo de Verificacion Laboral en la tabla TIPO_VERIFICACION
	 */
	String CODIGO_VERIFICACION_LABORAL = "1";
	/**
	 * Codigo de Verificacion Domiciliaria en la tabla TIPO_VERIFICACION
	 */
	String CODIGO_VERIFICACION_DOMICILIARIA = "2";
	/**
	 * Codigo de Zona Peligrosa en la tabla TIPO_VERIFICACION
	 */
	String CODIGO_ZONA_PELIGROSA = "3";
	/**
	 * Codigo de Verificacion Domiciliaria en la tabla TIPO_VERIFICACION
	 */
	String CODIGO_VERIFICACION_DPS = "4";	
	/**
	 * Objeto de tipo ExpedeinteVO almacenado en el ambito de sesion
	 */
	String EXPEDIENTE_SESION = "expSesion";
	String LISTA_PEND= "listPendSession";
	
	/**
	 * FIX ERIKA ABREGU
	 * Objeto de tipo ExpedeinteVO Antiguo almacenado en el ambito de sesion
	 */
	String EXPEDIENTE_SESION_ANTIGUO = "expSesionAntiguo";
	/**
	 * FIX ERIKA ABREGU
	 * Indicador si el Historial viene desde el Antiguo o el Unificado
	 */
	String EXPEDIENTE_ANTIGUO = "Antigua CS PLD";
	String EXPEDIENTE_UNIFICADO= "Nueva CS Unificado";
	long CODIGO_TAREA_EJEC_EVAL_CREDITICIA =12;
	long CODIGO_TAREA_REVISAR_REGIST_DICTAMEN =19;
	
	/**
	 * FIX ERIKA ABREGU
	 * Indicador si el Historial viene desde el Antiguo o el Unificado
	 */
	String OTROS_DOCS_SUSTENTATORIOS_ANALISTA_RIESGOS = "36";
	long CONDICION_IMPORTE = 11;
	long CONDICION_PLAZO = 12;

	
	
	/**
	 * Objeto de tipo ExpedeinteVO almacenado en el ambito de sesion
	 */
	String MOTIVO_DEVOLUCION_TAREA_SESION = "motivoDevolucionTareaSesion";
	/**
	 * Objeto de tipo ExpedeinteVO almacenado en el ambito de sesion
	 */
	//String EXPEDIENTE_LISTA_DOCUMENTO_CM = "expListaDocCM";	
	/**
	 * Objeto de tipo ExpedeinteVO almacenado en el ambito de sesion
	 */
	String EXPEDIENTE_SESION_ENTRANTE = "expSesionEntrante";	
	/**
	 * Objeto de tipo ExpedeinteVO. Es el expediente histï¿½rico guardado en bd
	 */
	String EXPEDIENTE_SESION_HISTORICO = "expSesionHistorico";
	
	/**
	 * FIX ERIKA ABREGU
	 * Objeto de tipo ExpedeinteVO. Es el expediente antiguo histï¿½rico guardado en bd
	 */
	String EXPEDIENTE_SESION_HISTORICO_ANTIGUO = "expSesionHistoricoAntiguo";
	
	
	/**
	 * Nombre de la accion para el boton Aceptar. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_REGISTRAR = "REGISTRAR";
	/**
	 * Nombre de la accion para el boton Grabar. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_GRABAR = "GRABAR";
	/**
	 * Flag Resultado Validad tipo de verificacion = -1
	 */
	String RESULTADO_TIPO_VERIFICACION_SELECCIONE = ">>Seleccione<<";
	/**
	 * Flag Resultado Validad tipo de verificacion = 0
	 */
	String RESULTADO_TIPO_VERIFICACION_APROBADO = "Aprobado";
	/**
	 * Flag Resultado Validad tipo de verificacion = 1
	 */
	String RESULTADO_TIPO_VERIFICACION_OBSERVADO = "Observado";
	/**
	 * Codigo de Scoring Aprobado en la tabla TIPO_SCORING
	 */
	String CODIGO_SCORING_APROBADO = "1";
	/**
	 * Codigo de Oferta en la tabla TIPO_OFERTA
	 */
	String CODIGO_OFERTA_APROBADO = "1";
	String CODIGO_OFERTA_REGULAR = "2";
	/**
	 * Accion del boton Aprobado con Modificacion
	 */
	String ACCION_BOTON_APROBADO_CON_MOD_OBS = "APROBADO_CON_MOD_OBS";	
	/**
	 * Accion del boton Aprobado sin Modificacion
	 */
	String ACCION_BOTON_APROBADO_SIN_MODIFICACION = "APROBADO_SIN_MODIFICACION";
	/**
	 * Accion del boton Aprobado con Modificacion Verificacion Domiciliaria
	 */
	String ACCION_BOTON_APROBAR_OPERACION = "APROBAR_OPERACION";
    /**
	 * Nombre de la accion para el boton Enviar. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_ELEVAR_A_RIESGOS = "ELEVAR_A_RIESGOS";
    /**
     * Valor seleccionado en los motivos de Devolucion
    */
    String CHECK_VALOR_OTROS = "Otros";
    /**
	 * Nombre de la accion para el boton Resolver. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_RESOLVER = "RESOLVER";
    /**
	 * Nombre de la accion para el boton En espera. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_EN_ESPERA = "EN_ESPERA";
    /**
	 * Nombre de la accion para el boton Devolver Gestor. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_DEVOLVER_GESTOR = "DEVOLVER_A_GESTOR";
	/**
	 * Codigo de la accion para el boton Devolver Gestor. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	Integer CODIGO_BOTON_DEVOLVER_GESTOR_CU4 = 41;
	/**
	 * Codigo de la accion para el boton Devolver Analisis y Alta. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	Integer CODIGO_BOTON_DEVOLVER_AA_CU4 = 42;
	/**
	 * Codigo de la accion para el boton Devolver Gestor. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	Integer CODIGO_BOTON_DEVOLVER_GESTOR_CU12 = 121;
    /**
	 * Nombre de la accion para el boton Devolver. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_DEVOLVER = "DEVOLVER";
	/**
	 * Codigo de Expediente Subsanado en la tabla ESTADO
	 */
	String CODIGO_EXPEDIENTE_SUBSANADO= "3";
	/**
	 * Codigo de En Espera en la tabla ESTADO
	 */
	String CODIGO_EN_ESPERA_RESPUESTA_TAREA5= "15";
	/**
	 * Codigo de En Espera en la tabla ESTADO
	 */
	String CODIGO_EN_ESPERA_RESPUESTA_TAREA26= "61";
	/**
	 * Flag Resultado Validad tipo de verificacion = >>Seleccione<<
	 */
	String RESULTADO_TIPO_VERIFICACION_SELECCIONE_ID = "-1";
	/**
	 * Flag Resultado Validad tipo de verificacion = Aprobado
	 */
	String RESULTADO_TIPO_VERIFICACION_APROBADO_ID = "0";
	/**
	 * Flag Resultado Validad tipo de verificacion = Observado
	 */
	String RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID = "1";
    /**
	 * Nombre de la accion para el boton Cancelar Operacion. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_RECHAZAR_EXPEDIENTE = "RECHAZAR";	
	//String ACCION_BOTON_RECHAZAR_EXPEDIENTE = "RECHAZAR_EXPEDIENTE";	
	/**
	 * Nombre de la accion para el boton Tarjeta Entregada. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_TARJETA_ENTREGADA = "TARJETA_ENTREGADA";
	/**
	 * Codigo del tipo de envio Oficina Gestora
	 */
	String CODIGO_TIPO_ENVIO_OFICINA_GESTORA = "3";
	/**
	 * Nombre jsp Historico
	 */
	String NOMBRE_JSP_HISTORICO = "formAprobarExpediente";
    /**
	 * Nombre de la accion para el boton Devolver a Cpm. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_DEVOLVER_CPM = "DEVOLVER_A_CPM";
	/**
	 * Codigo de la accion para el boton Devolver a Cpm. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	Integer CODIGO_BOTON_DEVOLVER_CPM_CU12 = 122;

    /**
	 * Nombre de la accion para el boton Denegar. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_DENEGAR = "DENEGAR";
    /**
	 * Nombre de la accion para el boton Elevar a Superior. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_ELEVAR_SUPERIOR = "ELEVAR_A_SUPERIOR";
    /**
	 * Nombre de la accion para el boton Devolver a Riesgos. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_DEVOLVER_RIESGOS = "DEVOLVER_A_RIESGOS";
	/**
	 * Nombre temporal para elevar a Superior, Gerente
	 */
	String CODIGO_ELEVA_GERENTE_ID = "1";
	/**
	 * Nombre temporal para elevar a Superior, Jefe
	 */
	String CODIGO_ELEVA_JEFE_ID = "2";
	/**
	 * Nombre jsp Observacion Expediente
	 */
	String NOMBRE_JSP_OBSERVACION_EXPEDIENTE = "formObservacionExpediente";
	/**
	 * Nombre jsp Historial Expediente
	 */
	String NOMBRE_JSP_HISTORIAL_EXPEDIENTE = "formHistorialExpediente";	
	/**
	 * Nombre temporal para elevar a Superior, "1"
	 */
	String CODIGO_ELEVA_GERENTE = "Gerente";
	/**
	 * Nombre temporal para elevar a Superior, "2"
	 */
	String CODIGO_ELEVA_JEFE = "Jefe";
	/**
	 * Nombre jsp Ayuda Memoria Expediente
	 */
	String NOMBRE_JSP_AYUDA_MEMORIA_EXPEDIENTE = "formAyudaMemoriaExpediente";
	/**
	 * Nombre jsp Log Expediente
	 */
	String NOMBRE_JSP_LOG_EXPEDIENTE = "formLogExpediente";
	/**
	 * Nombre de la accion para el boton Aprobar. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_APROBAR = "APROBAR";
	/**
	 * Objeto de tipo AyudaMemoriaVO almacenado en el ambito de sesion
	 */
	String AYUDA_MEMORIA_SESION = "ayuMemSession";
	/**
	 * Objeto de tipo AyudaMemoriaVO almacenado en el ambito de sesion, es la lista que se muestra . 
	 */
	String AYUDA_MEMORIA_SESION3 = "ayuMemSession3";
	/**
	 * Objeto de tipo AyudaMemoriaVO almacenado en el ambito de sesion
	 */
	String FLAG_NACAR_SESION = "ayuNacarSession";	
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 1
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA1 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 10
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA10 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 15
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA15 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 16
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA16 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 20
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA20 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 21
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA21 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 23
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA23 = "2";
	/**
	 * Cï¿½digo para el prestamo formalizado de la tarea 25
	 */
	String CODIGO_PRESTAMO_FORMALIZADO_TAREA25 = "2";
	/**
	 * Objeto para guia documentaria
	 * tipo de persona = Titular
	 */
	String CODIGO_TIPO_PERSONA_TITULAR = "1";
	/**
	 * Objeto para guia documentaria
	 * tipo de persona = Conyugue
	 */
	String CODIGO_TIPO_PERSONA_CONYUGUE = "2";
	/**
	 * Objeto para guia documentaria
	 * tipo de persona = Aval
	 */
	String CODIGO_TIPO_PERSONA_AVAL = "3";
	/**
	 * Objeto para guia documentaria
	 * documento = Obligatorio
	 */
	String CODIGO_DOCUMENTO_OBLIGATORIO = "1";
	/**
	 * Objeto para guia documentaria
	 * documento = Opcional
	 */
	String CODIGO_DOCUMENTO_OPCIONAL = "0";
	/**
	 * Formato numerico para salida de campo
	 */	
	String FORMATO_DECIMAL_DECENAS = "##.00";
	/**
	 * Formato numerico para salida de campo
	 */
	String FORMATO_DECIMAL_CENTENAS = "###.00";
	/**
	 * Formato numerico para salida de campo
	 */
	String FORMATO_DECIMAL_MILES = "###,###.00";
	/**
	 * Formato numerico para salida de campo
	 */
	String FORMATO_DECIMAL_MILLON = "###,###,###.00";	
	/**
	 * Flag de Documento Reutilizable
	 */
	String FLAG_DOCUMENTO_REUTILIZABLE = "1";
	/**
	 * Documento = Copia DNI
	 */
	String CODIGO_COPIA_DNI = "4";
	/**
	 * Constante para restar fechas
	 */
	long MILISEGUNDOS_POR_DIA = 24 * 60 * 60 * 1000;
	/**
	 * Para los mensajes que se mostraran en la tarea 1 
	 */
	String TIPO_MENSAJE = "tipoMensaje";
	/**
	 * Para el tipo de mensaje registrado
	 */
	int TIPO_MENSAJE_REGISTRADO = 1;
	/**
	 * Para el tipo de mensaje pre registrado
	 */
	int TIPO_MENSAJE_PREREGISTRADO = 2;
	/**
	 * Para el tipo de mensaje cancelado
	 */
	int TIPO_MENSAJE_CANCELADO = 3;
	/**
	 * Para el tipo de mensaje pendiente
	 */
	int TIPO_MENSAJE_PENDIENTE = 4;
	/**
	 * Para el numero de filas que se mostrara en las tablas de Ayuda Memoeria,
	 * Historial, Observaciones y Log
	 */
	int FILAS_TABLA = 20;
	/**
	 * Para almacenar en sesion la pagina actual del historial
	 */
	String PAGINA_ACTUAL_HISTORIAL = "pagActHist";
	/**
	 * Para almacenar en sesion la pagina actual de la ayuda memoria
	 */
	String PAGINA_ACTUAL_AYUDA_MEMORIA = "pagActAyuMen";
	/**
	 * Para almacenar en sesion la pagina actual de las Observaciones
	 */
	String PAGINA_ACTUAL_OBSERVACIONES = "pagActObs";
	/**
	 * Para almacenar en sesion la pagina actual del Log
	 */
	String PAGINA_ACTUAL_LOG = "pagActLog";
	/**
	 * Para almacenar en sesion la pagina actual del Log
	 */
	String NOMBRE_BANDEJA_SESION = "nombre_bandeja_sesion";
	/**
	 * Para almacenar en sesion la ruta del app GMD
	 */
	String COMANDO_SESION = "sessionComando";
	/**
	 * Para almacenar en sesion los parametros 
	 */
	String PARAMETROS_SESION = "sessionParametros";
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String EXPEDIENTE_PROCESO_SESION = "expProcSesion";
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String PANEL_SESSION = "panelSession";	
	/**
	 * Objeto de Control de almacenado en el ambito de sesion
	 */
	String OBS_SESSION = "obsSession";		
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String LISTA_EXPEDIENTE_PROCESO_SESION = "listExpProSes";
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String LISTA_CART_X_PRODUCTO_USUARIO_SESION = "listcartXProdXUsu";
	
	/**
	 * Objeto de tipo ExpedienteTC almacenado en el ï¿½mbito de sesiï¿½n.
	 * Elementos seleccionados de la tabla de bï¿½squeda, ï¿½por quï¿½ no se
	 *  corrigiï¿½ la palabra 'ExpedeinteTC'?
	 */
	
	String LISTA_EXPEDIENTE_SELECCIONADOS_PROCESO_SESION = "listExpSeleccionadosProSes";
	
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String LISTA_USUARIOS_ROL_ASIG = "listUsuAsig";
	String PROV_BAND_ASIG = "ProvAsigBandAsig";
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	/*Inicio Bernabe Incidencia 198*/
	String LISTA_USUARIOS_ASIGNADOS="listaUsuariosAsignar";  // 20.06.2014
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */	
	String LISTA_SESION="listSession";
	
	String CODIGO_USUARIO_ASIGNADO="usuAsignado";
	String CODIGO_ESTADO_ASIGNADO="estadoAsignado";
	String CODIGO_USU_ASIGNADO_EXP="usuAsignadoExp";
	String CODIGO_TAREA_ASIGNADO="tareaAsignado";
	/*Fin Bernabe Incidencia 198*/
	
	String LISTA_USUARIOS_TAREA_ASIG = "listUsuTareaAsig";	
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String LISTA_TAREA_ESTADO_ASIG = "listTareaEstadoAsig";	
	/**
	 * Para el tipo de mensaje coordinar con cliente y subsanar
	 */
	int TIPO_MENSAJE_COORDINAR_CLIENTE_SUBSANAR = 5;
	/**
	 * Para el tipo de mensaje regularizar y escanear documentos
	 */
	int TIPO_MENSAJE_REGULARIZAR_ESCANEAR_DOCUMENTOS = 6;
	/**
	 * Para el tipo de mensaje registrar expediente cu23
	 */
	int TIPO_MENSAJE_REGISTRAR_EXPEDIENTE_CU23 = 7;	
	/**
	 * Para el tipo de mensaje registrar expediente cu25
	 */
	int TIPO_MENSAJE_REGISTRAR_EXPEDIENTE_CU25 = 8;	
	/**
	 * Para el tipo de mensaje registrar expediente cu25
	 */
	int TIPO_MENSAJE_CONSULTAR_CLIENTE_MODIFICACIONES = 9;	
	/**
	 * Para el tipo de mensaje registrar expediente cu25
	 */
	int TIPO_MENSAJE_EVALUAR_DEVOLUCION_RIESGOS = 10;	
	/**
	 * Para el tipo de mensaje registrar expediente cu25
	 */
	int TIPO_MENSAJE_GESTIONAR_SUBSANAR_OPERACION = 11;	
	/**
	 * Para el directorio de doc escaneados Subida   complete
	 */
	String DIRECTORIO_DOC_ESCANEADOS = "C:\\ContratacionElectronica\\Transferencias_TC"; 
	/**
	 * Para el directorio de doc escaneados bajada
	 */
	String DIRECTORIO_DOC_ESCANEADOS_BAJADA = "C:\\ContratacionElectronica\\Descargados_TC";
	/**
	 * Para el directorio de log de doc escaneados
	 */
	String DIRECTORIO_LOG_DOC_ESCANEADOS = "C:\\ContratacionElectronica\\Log_TC";
	/**
	 * Path Escaneo
	 */
	String PATH_ESCANEO = "C:\\ContratacionElectronica\\Escaneados";
	/**
	 * Para la extension del los archivos escanedos
	 */
	String EXTENSION_DOC_ESCANEADOS = ".pdf";
	/**
	 * Objeto de tipo Registrar Expediente almacenado en el ambito de sesion
	 */
	String REGISTRAR_EXPEDIENTE_SESION = "regExpSesion";
	/**
	 * Flag Registra Expediente
	 */
	String FLAG_REGISTRA_EXPEDIENTE = "1";
	/**
	 * Flag Reasigna Tarea
	 */
	String FLAG_REASIGNA_TAREA = "1";
	/**
	 * Flag Bandeja Pendientes
	 */
	String FLAG_BANDEJA_PENDIENTES = "1";	
	/**
	 * Estados de las tareas
    */
	Integer ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_1=1;
	Integer ESTADO_EN_REGISTRO_TAREA_1=63;
	Integer ESTADO_EXPEDIENTE_REGISTRADO_TAREA_1=64;
	Integer ESTADO_EXPEDIENTE_POR_CERRAR_TAREA_1=65;
	Integer ESTADO_POR_REALIZAR_ALTA_TAREA_2=5;
	Integer ESTADO_EN_REGISTRO_TAREA_2=6;	
	Integer ESTADO_APROBADO_TAREA_3=7;
	Integer ESTADO_POR_VALIDAR_MESA_CONTROL_TAREA_3=72; 
	Integer ESTADO_POR_APROBAR_TAREA_3=74;
	Integer ESTADO_EN_ANALISIS_Y_RIESGOS_TAREA_3=75;
	Integer ESTADO_EN_VERIFICACION_TAREA_3=76;
	Integer ESTADO_APROBADO_CON_MODIFICACION_TAREA_4=8;	
	Integer ESTADO_APROBADO_TAREA_4=9;
	Integer ESTADO_POR_SUBSANAR_TAREA_4=10;
	Integer ESTADO_RECHAZADO_TAREA_4=11;
	Integer ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_4=12;	
	Integer ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_4=73;	
	Integer ESTADO_APROBADO_CON_OBSERVACION_TAREA_4=84;	
	Integer ESTADO_MODIFICACION_DE_ALTA_TAREA_5=13;	
	Integer ESTADO_EN_ESPERA_DE_RESPUESTA_TAREA_5=15;
	Integer ESTADO_RESUELTO_TAREA_5=16;	
	Integer ESTADO_POR_SUBSANAR_TAREA_5=77;
	Integer ESTADO_RECHAZADO_TAREA_5=78;	
	Integer ESATDO_POR_REALIZAR_ALTA_TAREA_5=85;
	Integer ESTADO_DESEMBOLSO_DEL_PRESTAMO_TAREA_5=86;	
	Integer ESTADO_RESUELTO_TAREA_TC_TAREA_6=14;	
	Integer ESTADO_MODIFICACION_DE_ALTA_TAREA_6=66;
	Integer ESTADO_RESUELTO_TAREA_DPS_TAREA_6=82;	
	Integer ESTADO_POR_REALIZAR_ALTA_TAREA_6=87;	
	Integer ESTADO_TARJETA_EN_ELABORACION_ENVIO_TAREA_7=17;
	Integer ESTADO_APROBADO_TAREA_7=18;	
	Integer ESTADO_RECHAZADO_TAREA_10=22;
	Integer ESTADO_EN_VERIFICACION_TAREA_10=23;	
	Integer ESTADO_CERRADO_TAREA_11=24;	
	Integer ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_12=25;
	Integer ESTADO_POR_SUBSANAR_TAREA_12=26;
	Integer ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_12=27;
	Integer ESTADO_APROBADO_TAREA_12=28;
	Integer ESTADO_APROBADO_CON_MODIFICACION_TAREA_12=29;
	Integer ESTADO_RECHAZADO_TAREA_12=30;	
	Integer ESTADO_APROBADO_CON_OBSERVACION_TAREA_12=88;	
	//Integer ESTADO_EXPEDIENTE_OBSERVADO_TAREA_13=31;
	Integer ESTADO_POR_REALIZAR_ALTA_TAREA_13=32;	
	Integer ESTADO_POR_REALIZAR_ALTA_TAREA_14=33;
	//Integer ESTADO_EXPEDIENTE_OBSERVADO_TAREA_14=34;	
	//Integer ESTADO_POR_REALIZAR_ALTA_TAREA_14=71;	
	Integer ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_15=35;
	Integer ESTADO_RECHAZADO_TAREA_15=36;
	Integer ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_15=37;
	Integer ESTADO_POR_APROBAR_TAREA_15=79;
	Integer ESTADO_CAMBIO_A_EN_TRAMITE_TAREA_16=38;
	Integer ESTADO_EN_VERIFICACIONES_TAREA_16=39;
	Integer ESTADO_RECHAZADO_TAREA_16=40;
	//Integer ESTADO_EXPEDIENTE_OBSERVADO_TAREA_17=41;
	Integer ESTADO_POR_REALIZAR_ALTA_TAREA_17=42;
	Integer ESTADO_APROBADO_CON_MODIFICACION_TAREA_18=43;
	//Integer ESTADO_EXPEDIENTE_OBSERVADO_TAREA_18=44;
	Integer ESTADO_APROBADO_CON_MODIFICACION_TAREA_19=45;
	Integer ESTADO_EN_VERIFICACION_TAREA_19=46;
	Integer ESTADO_APROBADO_CON_OBSERVACION_TAREA_19=47;
	Integer ESTADO_RECHAZADO_TAREA_19=48;
	Integer ESTADO_EN_ANALISIS_DE_RIESGOS_TAREA_19=49;	
	Integer ESTADO_POR_SUBSANAR_TAREA_19=69;
	Integer ESTADO_APROBADO_TAREA_19=89;	
	Integer ESTADO_POR_VALIDAR_MESA_DE_CONTROL_TAREA_20=50;
	Integer ESTADO_RECHAZADO_TAREA_20=51;
	Integer ESTADO_EN_ESPERA_DE_RESPUESTA_TAREA_26=61;
	Integer ESTADO_CERRADO_TAREA_27=62;		
	Integer ESTADO_EN_VERIFICACION_TAREA_32=83;
	Integer ESTADO_APROBADO_TAREA_33=80;	
	
	/*Integer ESTADO_PRESTAMO_FORMALIZADO_TAREA_8=19;
	Integer ESTADO_EXPEDIENTE_A_CANCELAR_TAREA_8=67;
	Integer ESTADO_TARJETA_ENTREGADA_TAREA_8=68;
	Integer ESTADO_EXPEDIENTE_POR_ARCHIVAR_TAREA_9=20;
	Integer ESTADO_EXPEDIENTE_OBSERVADO_TAREA_9=21;
	Integer ESTADO_EXPEDIENTE_A_CANCELAR_TAREA_21=52;
	Integer ESTADO_REEVALUACION_DE_SCORING_TAREA_21=53;
	Integer ESTADO_POR_APROBACION_DE_GERENTE_TAREA_21=54;
	Integer ESTADO_EXPEDIENTE_A_CANCELAR_TAREA_22=55;
	Integer ESTADO_RECEPCION_RIESGOS_TAREA_22=56;
	Integer ESTADO_REINGRESO_DE_OPERACION_TAREA_22=57;
	Integer ESTADO_REINGRESO_DE_EXPEDIENTE_TAREA_23=2;
	Integer ESTADO_EXPEDIENTE_A_CANCELAR_TAREA_23=70;	
	Integer ESTADO_RECEPCION_RIESGOS_TAREA_24=58;
	Integer ESTADO_REEVALUACION_DE_SCORING_TAREA_24=59;
	Integer ESTADO_EXPEDIENTE_OBSERVADO_TAREA_24=60;
	Integer ESTADO_EXPEDIENTE_SUBSANADO_TAREA_25=3;
	Integer ESTADO_EXPEDIENTE_A_CANCELAR_TAREA_25=4;*/
	
	/**
	 * Nombre de la accion para el boton Resolver. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_NO_CONFORME = "NO_CONFORME";	
	
	/**
	 * id de Perfil analista de riesgos
	 */
	int ID_ANALISTA_RIESGOS = 8;
	/**
	 * Id de la primera tarea (Registrar Expediente)
	 */
	Integer ID_TAREA_1 = 1;
	/**
	 * Id de la primera tarea (Registrar Expediente)
	 */
	Integer ID_TAREA_5 = 5;
	/**
	 * Id de la primera tarea (Registrar Expediente)
	 */
	Integer ID_TAREA_6 = 6;		
	Integer ID_TAREA_11 = 11;
	Integer ID_TAREA_4 = 4;
	Integer ID_TAREA_12 = 12;
	Integer ID_TAREA_19 = 19;
	
	/**
	 * Almacena el id del empleado del que registro el estado anterior
	 */
	String ID_EMPLEADO_ESTADO_ANTERIOR_SESION = "idEmpEstAnt";
	/**
	 * RespuestaExitosa
    */
	String CADENA_OK = "ok";	
	/**
	 * Constante ftp
    */
	String CONSTANTE_FTP_HOST = "bbva.ftp.hostname";
	String CONSTANTE_FTP_USER = "bbva.ftp.username";
	String CONSTANTE_FTP_PASS = "bbva.ftp.password";
	String CONSTANTE_FTP_PERIODO = "bbva.ftp.periodo";
	String CONSTANTE_FTP_TASA = "bbva.ftp.tasaKB";
	String CONSTANTE_HOSTWS = "bbva.hostws";
	/**
	 * Objeto de tipo flag que indica si copia archivos almacenado en el ambito de sesion
	 */
	String FLAG_COPIA_ARCHIVO_SESION = "flCopiaArchivoSesion";
	/**
	 * Para permitir copiar los archivos
	 */
	String FLAG_COPIA_ARCHIVO = "1";
	/**
	 * Flag Bandeja Mantenimiento
	 */
	String FLAG_BANDEJA_MANTENIMIENTO = "1";
	/**
	 * Perfil Ayuda Memoria Gestor Plataforma = 6  * 
	 */
	Integer PERFIL_GESTOR_PLATAFORMA = 6;
	/**
	 * Perfil Ayuda Memoria Ejecutivo Vip = 7  * 
	 */
	Integer PERFIL_EJECUTIVO_VIP = 7;
	/**
	 * Perfil Ayuda Memoria Ejecutivo Vip = 7  * 
	 */
	Integer PERFIL_ADMINISTRADOR = 14;	
	/**
	 * Perfil Ayuda Memoria Gerente Oficina = 1  * 
	 */
	Integer PERFIL_GERENTE_OFICINA = 1;
	/**
	 * Perfil Ayuda Memoria Sub Gerente Oficina = 13  * 
	 */	
	Integer PERFIL_SUB_GERENTE_OFICINA = 13;
	/**
	 * Flag si registra Ayuda Memoria = 1  * 
	 */	
	String FLAG_REGISTRA_AYUDA_MEMORIA = "1";
	/**
	 * Tipo Visualizacion Doc Escaneado = 1  * 
	 */	
	String ESCANEADO_TIPO_DOCUMENTOS_CASO1 = "1";
	/**
	 * Tipo Visualizacion Doc Escaneado = 2  * 
	 */	
	String ESCANEADO_TIPO_DOCUMENTOS_CASO2 = "2";
	/**
	 * Tipo Visualizacion Doc Escaneado = 3  * 
	 */	
	String ESCANEADO_TIPO_DOCUMENTOS_CASO3 = "3";	
	/**
	 * Flag de cliente nuevo en la tabla GUIA DOCUMENTARIA
	 */
	String FLAG_CLIENTE_NUEVO_0 = "0";
	/**
	 * Flag de cliente nuevo en la tabla GUIA DOCUMENTARIA
	 */
	String FLAG_CLIENTE_NUEVO_1 = "1";
	/**
	 * Id de Casado en la tabla ESTADO CIVIL
	 */
	Integer ESTADO_CIVIL_CASADO = 2;
	/**
	 * Id de Casado en la tabla CONVIVIENTE
	 */
	Integer ESTADO_CIVIL_CONVIVIENTE = 5;
	/**
	 * Flag Superior en la tabla TIPO CATEGORIA = 1
	 */
	String FLAG_SUPERIOR_1 = "1";
	/**
	 * Id de soltero en la tabla ESTADO CIVIL
	 */
	Integer ESTADO_CIVIL_SOLTERO = 1;	
	/**
	 * Existe los doc. escaneados en la carpeta
	 */
	String VALIDA_DOC_ESC_EXISTE = "1";
	/**
	 * Id Oficina desplazada
	 */
	String ID_OFICINA_DESPLAZADA = "1";
	/**
	 * Comparador Oficina Desplazada
	 */
	int OFICINA_DESPLAZADA = 0;
	/**
	 * Objeto de tipo id_expediente en el ambito de sesion
	 */
	String ID_EXPEDIENTE_SESION = "idExpedienteSesion";	
	/**
	 * Objeto de tipo Lista Documentos Reutilizables
	 */
	String LISTA_DOC_REUTILIZABLES = "listaDocReutilizables";	
	
	String TEXTO_FORMATO_HORA = "HH:mm";
	
	SimpleDateFormat FORMATO_HORA = new SimpleDateFormat (TEXTO_FORMATO_HORA);
	
	String TEXTO_FORMATO_FECHA = "dd/MM/yyyy HH:mm";
	
	SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat (TEXTO_FORMATO_FECHA);
	
	int NO_TIENE_MINUTOS = -1;
	/**
	 * Objeto de Control Transferencia Archivos
	 */
	String CLEANTRANSFERDIR = "cleanTransferDir";
	/**
	 * ID de la tarea cuando se va a mostrar el estado de la tarjeta 
	 */
	String ID_TAREA_MOSTRAR_ESTADO_TARJETA = "8";	
	/**
	 * ID de la tarea cuando existe error al enviar archivos 
	 */
	String ID_TAREA_ERROR_ENVIO_ARCHIVOS = "28";
	
	String LISTA_DOC_EXP_ADJ = "listaDocExpAdj";
	String LISTA_AYUDA_PANEL_DOCUMENTOS = "listaAyudaPanelDocumentos";
	
	String LISTA_AYUDA_AGR_PANEL_DOCUMENTOS = "listaPersonasPD";
	/**
	 * Para el directorio de log de doc escaneados
	 */
	String EXISTEN_TRANSFERENCIAS = "existenTransferencias";
	
	String FLAG_EXISTE_TRANSFERENCIAS_UNO = "1";
	String FLAG_EXISTE_TRANSFERENCIAS_CERO = "0";
	String VERIFICAR_NUMERO_CONTRATO_OK = "00";
	
	/**
	 * Ruta mdb GMD
	 */
	String RUTA_MDB = "C:\\Archivos de Programa\\GMD S.A\\API GMD\\Configuracion\\Configuracion.mdb";	
	/**
	 * Objeto de Control Transferencia Archivos Valor cero
	 */
	String CLEANTRANSFERDIR_CERO = "0";
	/**
	 * Objeto de Control Transferencia Archivos Valor uno
	 */
	String CLEANTRANSFERDIR_UNO = "1";
	
	public static final String SEGMENTO_NINGUNO_DESCRIPCION = "SIN ASIGNAR";
	
	/**
	 * Flag de documentos Reutillizables = 0
	 */
	String FLAG_DOCUMENTO_REUTILIZABLE_CERO = "0";
	/**
	 * Flag de documentos Reutillizables = 1
	 */
	String FLAG_DOCUMENTO_REUTILIZABLE_UNO = "1";
	/*
	 *Documentos reutilizables
	 * */
	String DOCUMENTO_REUTILIZABLE_APPLET = "docReutilizables";
	/**
	 * Para indicar error en la conexion en el servicio
	 */
	int CODIGO_ERROR_CONEXION = 20;
	/**
	 * Perfiles para validar carterizacion id =2 
	 */
	Integer ID_PERFIL_FORMALIZADOR = 2;
	/**
	 * Perfiles para validar carterizacion id =3 
	 */	
	Integer ID_PERFIL_ANALISIS_Y_ALTAS = 3;
	/**
	 * Perfiles para validar carterizacion id =5 
	 */	
	Integer ID_PERFIL_MESA_DE_CONTROL = 5;
	/**
	 * Perfiles para validar carterizacion id =8 
	 */	
	Integer ID_PERFIL_ANALISIS_DE_RIESGOS = 8;
	/**
	* Perfil para Superior de riesgos = 11
	*/	
	Integer ID_PERFIL_SUPERIOR_RIESGOS = 11;
	/**
	* Perfil para Riesgos Superior = 4
	*/	
	Integer ID_PERFIL_RIESGOS_SUPERIOR = 4;
	/**
	 * Perfiles para validar carterizacion id =9 
	 */	
	Integer ID_PERFIL_CALIFICACION_Y_CONTROL = 9;
	/**
	 * Perfiles para validar carterizacion id =10 
	 */	
	Integer ID_PERFIL_CONTROLLER = 10;
	/**
	 * Perfiles para validar carterizacion id =10 
	 */	
	Integer ID_PERFIL_EJECUTIVO = 6;	
	/**
	 * Perfiles para validar carterizacion id =10 
	 */	
	Integer ID_PERFIL_SUB_GERENTE = 13;	
	
	Integer ID_PERFIL_GERENTE = 1;
	/*Para los mensajes que se mostraran en la tarea 1*/
	int TIPO_MENSAJE_NO_EXISTE_ROLES = 12;
	/**
	 * Para los mensajes que se mostraran en la tarea 1 
	 */
	String DESCRIPCION_MENSAJE = "descripcionMensaje";
	/**
	 * Codigo del Producto Tarjeta de Credito 1
	 */
	int CODIGO_ID_PRODUCTO = 1;
	/**
	 * Contiene el tipo de tabla
	 */
	String TIPO_TABLA_SESION = "tipoTablaMantenimiento";
	
	String CARPETA_FTP = "docsce";
	
	/**
	 * Para estado de la tarjeta 
	 */
	String ESTADO_TARJETA = "estadoTarjeta";
	
	String TIPO_MONEDA_SOLES = "1";
	
	String DEFECTO_COMBO = ">>Seleccione<< ";
	
	String DEFECTO_COMBO_TODOS="TODOS";
	/**
	 * Nombre de variable de sesiï¿½n utilizada para inicializar ObservacionRechazoMB
	 */
	String CODIGO_ACCION_DEVOLVER_ELEGIDO = "codigoAccionDevolverElegido";
	/**
	 * Flag Otros
	 */
	String FLAG_OTROS_1 = "1";
	/**
	 * Codigo estado civil casado
	 */
	String EST_CIVIL_CASADO = "2";	
	/**
	 * Codigo estado civil conviviente
	 */
	String EST_CIVIL_CONVIVIENTE = "5";	
	
	String OP_DOC_OBLIGATORIO="1";
	
	String OP_DOC_NO_OBLIGATORIO="0";
	
	String DESCRIP_DOC_OBLIGATORIO="Obligatorios";
	
	String DESCRIP_DOC_NO_OBLIGATORIO="Opcionales";

	String COL_ELIMINAR_TIP_DOC=" Eliminar ";
	
	String COL_MODIFICAR_TIP_DOC=" Modificar ";
	
	String COL_OBSERVAR_TIP_DOC=" Observar ";
	
	/**
	 * Accion del boton confirmar validacion
	 */
	String ACCION_BOTON_CONFIRMAR_VALIDACION = "CONFIRMAR_VALIDACION";
	
	/**
	 * Accion del boton enviar aprobacion
	 */
	String ACCION_BOTON_ENVIAR_APROBACION = "ENVIAR_APROBACION";
	
	/**
	 * Accion del boton confirmar correccion
	 */
	String ACCION_BOTON_CONFIRMAR_CORRECCION_33 = "APROBADO";	
	/**
	 * Nombre de la accion para el boton confirmar correcciï¿½n
	 */
	String ACCION_BOTON_CONFIRMAR_CORRECCION_32 = "EN_VERIFICACION";		
	/**
	 * Accion del boton solicitar actualizacion de scoring
	 */
	String ACCION_BOTON_SOLICITAR_ACTUALIZACION_SCORING = "SOLICITAR_ACTUALIZACION_SCORING";
	/**
	 * Nombre de la accion para el boton aprobar con modificacion.
	 */
	//String ACCION_BOTON_APROBAR_CON_MODIFICACION = "APROBAR_CON_MODIFICACION";
	/**
	 * Nombre de la accion para el boton aprobar con observacion.
	 */
	String ACCION_BOTON_APROBAR_CON_OBSERVACION = "APROBAR_CON_OBSERVACION";	
	/**
	 * Nombre de la accion para el boton cerrar expediente.
	 */
	String ACCION_BOTON_CERRAR_EXPEDIENTE = "CERRAR_EXPEDIENTE";		
	/**
	 * Nombre de la accion para el boton cerrar expediente.
	 */
	String ACCION_BOTON_MODIFICAR_ALTA = "MODIFICAR_ALTA";
	/**
	 * Nombre de la accion para el boton actualizar expediente.
	 */
	String ACCION_BOTON_ACTUALIZAR_EXPEDIENTE = "ACTUALIZAR_EXPEDIENTE";	
	/**
	 * Nombre de la accion para el boton solicitar nueva verificacion.
	 */
	String ACCION_BOTON_SOLICITAR_NUEVA_VERIFICACION = "SOLICITAR_NUEVA_VERIFICACION";
	/**
	 * Nombre de la accion para el boton cerrar contrato y alta.
	 */
	String ACCION_BOTON_CERRAR_CONTRATO_Y_ALTA = "CERRAR_CONTRATO_Y_ALTA";
	/**
	 * Perfil Jefe de Equipo CPM 12
	 */
	Integer PERFIL_JEFE_EQUIPO_CPM = 12;	
	/**
	 * Perfil Supervisor de Riesgos 4
	 */
	Integer PERFIL_SUPERVISOR_RIESGOS = 4;	
	/**
	 * Flag Territorio Lima = 0
	 */
	String FLAG_TERRITORIO_LIMA = "0";	
	/**
	 * Flag Territorio Provincia = 1
	 */
	String FLAG_TERRITORIO_PROVINCIA = "1";	
	/**
	 * Lista de Empleados a Asignar almacenado en el ambito de sesion
	 */
	String LISTA_ASIGNAR_USUARIO_SESION = "listaAsignarUsuarioSesion";
	/**
	 * lista usuarios asignados almacenado en el ambito de sesion
	 */
	String LISTA_USUARIOS_ASIG = "listaUsuariosAsig";
	String ROL_SELECCIONADO = "rolSeleccionadoBandAsig";
	
	/**
	 * Flag Devolucion = 1
	 */
	String FLAG_DEVOLUCION = "1";	
	/**
	 * Flag Regular = 0
	 */
	String FLAG_REGULAR = "0";
	/**
	 * Nombre de la accion para el boton Actualizar scoring. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_ACTUALIZAR_SCORING = "ACTUALIZAR_SCORING";
	/**
	 * Nombre de la accion para el boton Observar Verificacion. 
	 * Se utiliza en el metodo expedienteVO.setAccion
	 */
	String ACCION_BOTON_OBSERVAR_VERIFICACION = "OBSERVAR_VERIFICACION";
	/**
	 * Valor de un minuto 
	 */
	double UNMINUTO = 60;
	/**
	 * Valor de cero minutos 
	 */	
	double CEROMINUTO = 0;
	/**
	 * Valor uno del flag_retraer 
	 */
	String FLAGRETRAERUNO = "1";
	/*Flag de retraer N=Registro Normal*/
	String FLAGRETRAERN = "N";
	/*Flag de retraer X=Registro original (N) Retraido*/
	String FLAGRETRAERX = "X";
	/*Flag de retraer D=Registro generado al enviarlo a la sgte tarea despues de la retraccion*/
	String FLAGRETRAERD = "D";
	/*Flag de retraer R=Registro generado por la retraccion*/
	String FLAGRETRAERR = "R";
		
	String ACCION_RETRAER = "RETRAER";
	
	int ID_APLICATIVO_TC = 1;
	int ID_APLICATIVO_PLD = 2;
	
	Integer ID_POSIBLE_VALOR_TOE=236;
	String ID_POSIBLE_VALOR_PERFIL_ESTADO="235";
	String ID_POSIBLE_VALOR_PERFIL_TIPO_FECHA="350";
	String ID_TOE_GENERAL="0";
	String ID_TOE_ESPECIFICO="1";

	String REPORTE_PERFIL = "repPerfil";
	
	String REPORTE_ESTADO = "repEstado";	
	String REPORTE_OFICINA = "repOficina";
	String REPORTE_TERRITORIO = "repTerritorio";
	
	String VALOR_SEPARADOR = "-";
	String VALOR_VACIO = "";
	String VALOR_CPM = "CPM";
	String VALOR_RIESGOS = "RIESGO";
	String VALOR_SEPARADOR_TITULO = " - ";
	
	String VALOR_TIEMPO_TE = "TE";
	String VALOR_TIEMPO_TC = "TC";
	
	String ID_TIEMPO_OBJ = "o";
	String ID_TIEMPO_PRE = "p";
	String ID_TIEMPO_REAL = "r";
	String ID_TIEMPO_TE = "e";
	String ID_TIEMPO_TC = "c";
	
	String DESCRIPCION_TIEMPO_OBJETIVO = "T. Objetivo";
	String DESCRIPCION_TIEMPO_PRELIMINAR = "T. Preliminar";
	String DESCRIPCION_TIEMPO_REAL = "T. Real";
	String DESCRIPCION_PORCENTAJE_TOE = "TOE";
	String DESCRIPCION_TOE_UNIDAD = "TOE POR UNIDAD";
	String DESCRIPCION_TOE_CPM = "TOE CPM";
	String DESCRIPCION_TOE_RIESGOS = "TOE RIESGOS";
	String DESCRIPCION_TITULO_REPORTE_TOE = "REPORTE TABLERO DE CONTROL";
	String DESCRIPCION_TITULO_REPORTE_TOE_ESPECIFICO = "REPORTE TOE POR ROL Y ESTADO";
	String DESCRIPCION_SUBTITULO_1 = "RANGO DE FECHAS : ";
	String DESCRIPCION_SUBTITULO_1_1 = "PRODUCTO : ";
	String DESCRIPCION_SUBTITULO_2 = "TIPO DE OFERTA : ";
	String DESCRIPCION_SUBTITULO_3 = "FLUJO : ";
	String DESCRIPCION_SUBTITULO_4 = " AL ";
	String DESCRIPCION_HOJA_EXCEL = "Reporte TOE";
	String DESCRIPCION_HOJA_EXCEL2 = "Reporte Consolidado";
	String DESCRIPCION_TIPO_PRODUCTO_TC="TC";
	String NOMBRE_TIPO_PRODUCTO_TC="TARJETA DE CREDITO";
	String DESCRIPCION_TIPO_PRODUCTO_PLD="PLD";
	String NOMBRE_TIPO_PRODUCTO_PLD="PRESTAMO DE LIBRE DISPONIBILIDAD";
	String DESCRIPCION_TIPO_OFERTA_APROBADO="APROBADO";
	String DESCRIPCION_TIPO_OFERTA_REGULAR="REGULAR";
	String DESCRIPCION_TIPO_FLUJO_REPROCESO="REPROCESO";
	String DESCRIPCION_TIPO_FLUJO_REGULAR="REGULAR";
	/**
	 * Objeto de tipo codigo Producto
	 */
	String COD_PRODUCTO_SESION = "codSesion";
	
	/**
	 * Etiqueta Producto
	 * */
	String CAMPO_ENVIO_TARJETA = "ENVIO_TARJETA";
	String CAMPO_PLAZO_SOLICITADO = "PLAZO_SOLICITADO";
	String CAMPO_PLAZO_SOLICITADO_APR = "PLAZO_SOLICITADO_APROBADO";
	String CAMPO_SOL_TASA_ESP = "SOL_TASA_ESP";
	String CAMPO_GARANTIA = "GARANTIA";
	String CAMPO_DPS = "DPS";
	String CAMPO_OPCION_VISIBLE = "1";
	String CAMPO_OPCION_NO_VISIBLE = "0";
	
	/**
	 * ï¿½CU4 ï¿½ Aprobar Expedienteï¿½, 
	 * ï¿½CU12 ï¿½ Ejecutar evaluaciï¿½n crediticiaï¿½ 
	 * ï¿½CU19 ï¿½ Revisar y registrar dictamenï¿½
	 * */
	String CU_TAREA_4 = "4";
	String CU_TAREA_12 = "12";
	String CU_TAREA_19 = "19";
	
	Double PORCENTAJE_ENDEUDAMIENTO_MAX = 999.99;
	Double PORCENTAJE_ENDEUDAMIENTO_MIN = 0.00;
	
	String CODIGO_OFICINA = "2";
	String CODIGO_EXITO_HAREC = "1";
	String CODIGO_EXITO_RENIEC = "0000";
	
	/**
	 * Codigo de Flujo en la tabla POSIBLE_VALOR
	 */
	String VALOR_FLUJO_REGULAR = "R";	
	String VALOR_FLUJO_REPROCESO = "P";
	
	String ACCION_BUSQUEDA="B";
	String ACCION_GENERAR="G";
	String RENIEC_CANAL = "S_C_";
	String RENIEC_CODIGOAPP = "CSPLDTC";
	String RENIEC_IDEMPRESA = "RENI";
	String RENIEC_CPERRENXDNI = "CPERRENXDNI";
	String RENIEC_TIPOAPP = "E";
	String RENIEC_INDCONSULTADATOS = "S";
	String RENIEC_INDCONSULTAFOTO= "N";
	String RENIEC_INDCONSULTAFIRMA = "N";
	
	String ID_TC_HISTORICO="0";		//JBTA
	String ID_TC_CONSOLIDADO="1";	//JBTA
	String TIPO_REPORTE_TC = "tipReporteTc"; /*JBTA*/
	String TITULO_REPORTE_TC = "titReporteTc"; /*JBTA*/
	String TITULO_REPORTE_TC_HISTORICO = "Reporte Histórico"; /*JBTA*/
	String TITULO_REPORTE_TC_CONSOLIDADO = "Reporte Consolidado"; /*JBTA*/	
	
	
	String FLAG_INACTIVO="0";
	String FLAG_ACTIVO="1";
	/**
	 * Codigo de Flag Subrogado
	 */
	String CODIGO_FLAG_SUBROGADO_ACTIVO = "1";
	
	
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
	
	/**
	 * Nivel por defecto Sin Nivel
	 */
	
	long NIVEL_SIN_NIVEL=10;	
	
	
	String PAGINA_ESCANEO_WEB = "paginaEscaneoWeb";
	String ID_EMPRESA= "idEmpresa";
	String ID_SISTEMA = "idSistema";
	
	String FLAG_CM = "1";
	
	String REGISTRAR_EXPEDIENTE_TAREA_1="1";
	
	String VERIFICACION_RESULTADO_VERIF_DOMICILIARIA="5";
	String ID_TAREA_APROBAR_EXPEDIENTE="4";
	String ID_TAREA_EJECUTAR_EVALUACION_CREDITICIA="12";
	String ID_TAREA_REVISAR_REGISTRAR_DICTAMEN="19";
	String ID_TAREA_REGISTRAR_APROBACION="6";
	String ID_TAREA_CAMBIAR_SITUACION_EXPEDIENTE="13";
	String ID_TAREA_ANULAR_MODIFICACION_14="14";
	String ID_TAREA_ANULAR_MODIFICACION_18="18";
	String ID_TAREA_REALIZAR_ALTA="7";
	
	
	String LISTA_DOCUMENTOS_ADJUNTOS = "lstDocsAdj";
	
	String COD_USUARIO_ASIGNAR="codAsignar";
	
	
	/**CONSTANTES PARA ENVIO DE CORREO POR TAREA***/
	
	String ENVIO_CORREO_TO="bernabe.yanac@gmail.com";
	
	
	String CODIGO_EXITO_NUM_CONTRATO="00";
	
	/**
	 * Codigo de Tipo de Cambio
	 */
	String CODIGO_TIPO_CAMBIO_SOLES = "1";
	
	String CODIGO_TIPO_CAMBIO_DOLARES = "2";
	
	/**
	 * Objeto de tipo Accion almacenado en el ambito de sesion
	 */
	String ACCION_SESION = "accionSesion";
	
	String CONSTANTE_ELIMINACION="SI";
	String LIST_DOC_TRANSF = "strListaDocsTransferencias";
	
	String CODIGO_ENTIDAD_NUMERO_CUENTA="0011";
	String	AYUDA_MEMORIA_NUEVO = "nuevoAyudaMemoria";
	String AYUDA_MEMORIA_CANT_LIST_INICIAL="ayudaMemoriaListInicial";
	String AYUDA_MEMORIA_PRIMER_INGRESO="ayudaMemoriaPrimerIngreso";
	
	String	VERIFICACION_LABORAL 	  = "selectedItemsResultVl3";
	String	VERIFICACION_DOMICILIARIA = "selectedItemsResultVd3";
	String OBSERVACION_REGISTRADA ="Si";
	String OBSERVACION_NO_REGISTRADA ="NO";
	
	/**
	 * Codigo para el campo flag otros de motivo de devolucion 
	 * com.ibm.bbva.util.Util
	 */
	String CODIGO_FLAG_OTROS_ACTIVO = "1";
	
	

	
	String EXISTE_CARTERIZACION = "1";
	String NO_EXISTE_CARTERIZACION = "0";
	int TIPO_MENSAJE_NO_EXISTE_CARTEIZACION = 13;

	
	String JUNCTION_NAME_UN = "junctionNameUN";
	
	String PAGINA_INICIO_UN = "paginaInicioUN";
	
	String INTENTOS_WS_UN = "intentosWSUN";
	
	String RESOLUCION_CON_MODIFICACION = "C";
	String RESOLUCION_SIN_MODIFICACION = "S";
	
	String FLAG_OBS_DOCEXPTC_ACTIVO = "1";
	
	String FLAG_OBS_DOCEXPTC_NOACTIVO = "0";
	
	String DATOS_CORREO_SESION = "correoSesion";
	String DATOS_CORREO_DETALLE_SESION ="detalleCorreoSesion";
	String LISTA_AUX_DETALLE="lstAux";
	
	String GUIA_DOCUMENTARIA_SESION="guiaDocumentariaSesion";
	
	String MANT_ANS_SESION="mantAnsSesion";
	
	String MENSAJES_SESION="mensajesSesion";
	
	String TAMANO_MAXIMO_ARCHIVO_MB = "appletTamanoMaxArchivoMBUN";
	
	String MENSAJE_DATOS_SESION="mensajeSesion";
	
	/**
	 * Reutilizacion de guia documentaria.
	 */
	String FLAG_REUGUIADOC_EXP_EXISTE = "existe";
	
	String FLAG_REUGUIADOC_EXP_CREAR = "guardar";
	
	/**
	 * Expediente antes trabajado en session
	 */
	String EXPEDIENTE_ESTADO = "expEstado";
	/**
	 * Flag de estados para tener en cuenta en la reasignaciï¿½n de expedientes
	 * */
	String FLAG_ESTADO_ACTIVO_EXPEDIENTE ="1";
	String FLAG_ESTADO_INACTIVO_EXPEDIENTE ="0";
	
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String CONSULTA_BANDASIGN = "estadoVisualizacionExp";		
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String CONSULTA_BANDASIGN_EXP = "estadoConsultaExp";	
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String CONSULTA_BANDASIGN_ASIGNA = "asignaConsultaExp";	
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String BOTON_BANDASIGN_ASIGNA = "asignaBotonExp";
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String BOTON_BANDASIGN_CARTPROD = "asignaBotonCartProd";
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String BOTON_BANDASIGN_REFRESH = "asignaBotonRefresh";	
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String ACT_BANDASIGN_ASIGNA = "actnaBotonExp";	
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String MSJ_BANDASIGN_ASIGNA = "asignaMsjExp";
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String MSJ_BANDASIGN_ASIGNA_CARTPROD = "asignaMsjExpCartProd";	
	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String LIS_BANDASIGN_ASIGNA = "asignalisExp";
	/**
	 * Objeto de tipo ExpedeinteTC almacenado en el ambito de sesion
	 */
	String PAGINA_SESSION = "pagSession";
	
	String LISTA_USUARIOS_CARGADOS = "listUsuCargados";	

	
	String LISTA_OFICINAS_CARGADAS = "listOficinasCargadas";

	/**
	 * Objeto de tipo boolean almacenado en el ambito de sesion
	 */
	String USUARIO_ASIGNADO = "usuasignadoSelecc";	
	
	/**
	 * Objeto de tipo boolean menu visible por perfil
	 */
	String OPCION_MENU_VISIBLE = "1";
	/**
	 * Mensajes personalizados
	 */
	String ID_MENSAJES_SIN_FILTRO="1";
	
	String ID_MENSAJES_BUSQUEDA_POR_EXPEDIENTE="2";
	
	String ID_MENSAJES_BUSQUEDA_OTROS_CRITERIOS="3";
	
	String ID_MENSAJE_EXPEDIENTE_REGISTRADO = "4";
	
	String ID_MENSAJE_EXPEDIENTE_PRE_REGISTRADO = "5";
	
	String ID_MENSAJE_EXPEDIENTE_CORREGIDO = "6";
	
	String ID_MENSAJE_MANT_CART_EMP_PERFIL = "10";
	
	String ID_MENSAJE_MANT_CART_EMP_ACTIVO = "11";
	
	long PACKPYME_RESPUESTA_CONTRATO_NO_CORRESPONDE_CLIENTE_MSG = 7;
	
	long PACKPYME_RESPUESTA_CONTRATO_NO_ENCONTRADO_MSG = 8;
	
	long PACKPYME_RESPUESTA_ERROR_ACCESO_MSG = 9;
	
	long ID_MENSAJE_USUARIO_INACTIVO = 12;
	
	
	/**
	 * lista de objetos TablaMonitoreo almacenada en el ambito de sesion
	 */
	String LISTA_BANDEJA_MONITOREO = "listaMonitoreo";
	
	/**
	 * Objeto que almacena el tipo de busqueda en la bandeja de monitoreo
	 */
	String TIPO_BUSQUEDA_BM = "tipoBusquedaMonitoreo";
	
	/**
	 * Acciones para el process desde la bandeja de monitoreo
	 */
	String ACCION_CONTINUAR_MANUAL = "CONTINUAR_MANUAL";
	
	String ACCION_REINTENTAR_MANUAL = "REINTENTAR_MANUAL";

	String ACCION_CANCELAR_MANUAL = "CANCELAR_MANUAL";

	/**
	 * Nuevas tareas desde la bandeja de monitoreo
	 */
	String TAREA_AD_CONTENT = "50";

	String TAREA_AD_PROCESS = "51";
	
	String CODIGO_ESTADO_ERROR = "22";
	
	/**
	 * Flag que identifica que documentos se subieron (o se intentaron subir) al content
	 */
	String FLAG_ESCANEADO = "1";
	
	/**
	 * Mensaje para registrar o grabar expediente
	 */
	String DESCRIPCION_MENSAJE_REGISTRADO = "mensajeRegistrado";
	String DESCRIPCION_MENSAJE_PRE_REGISTRADO = "mensajePreRegistrado";
	String DESCRIPCION_MENSAJE_CORREGIDO = "mensajeCorregido";

	String DATOS_MONITOREO = "bandejaMonitoreo";
	/**
	 * Para verificar que es un nuevo cliente
	 */
	String NUEVO_CLIENTE = "nuevoClienteFlujo";
	String LISTA_MOT_RECHAZO="listMotRechazo";
	String SELECT_MOT_RECHAZO="selectMotRechazo";
	String CONSIDERAR_TAREA_1="1";
	
	String BAND_LIST_PROD = "banListProd";
	String BAND_LIST_TIPDOI = "banListTipDoi";
	String BAND_LIST_SEG = "banListSeg";
	String BAND_LIST_TIPOFERTA = "banListTipOfer";
	String BAND_LIST_TERR = "banListTerr";
	String BAND_LIST_EST = "banListEst";
	
	String LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO="listExpeWnuevo";
	String LISTA_HORARIO_OFICINA="listHorarioOficinaTmp";
	String INICIO_BAND_PEND="initBandPend";
	String NUM_REG_BAND_PEND="numRegBandPend";
	
	String DESCRIPCION_ERROR="descripErrorObtenerUsu";
	/**
	 * Parï¿½metros
	 */
	Long PARAMETRO_TIPO_DESCARGA_LDAP = 108L;
	Long PARAMETRO_ESTADO_DESCARGA_LDAP = 112L;
	Long PARAMETRO_ESTADO_OFICINA_TEMPORAL = 115L;
	Long PARAMETRO_CORREO_OFICINA_TEMPORAL = 118L;
	Long PARAMETRO_OFICINAS_SINCRONIZABLES = 119L;
	
	/**
	 * Datos Jobs
	 */
	String JOB_NAME = "CONELE_JOB_CARGA_LDAP";
	String TRIGGER_NAME = "CONELE_TRIGGER_CARGA_LDAP";
	String GROUP_NAME = "CONELE";
	
	/**
	 * Parï¿½metros Proceso LDAP
	 */
	int CODIGO_APLICATIVO_PROCESO_LDAP = 900;
	String LDAP_WEB_SERVICE_ENDPOINT = "LDAP_WEBS_SERVICE_ENDPOINT";
	String LDAP_WEB_SERVICE_EXT_ENDPOINT = "LDAP_WEB_SERVICE_EXT_ENDPOINT";
	String CRON_SCHEDULE_PROCESO_CARGA_LDAP = "CRON_SCHEDULE_PROCESO_CARGA_LDAP";	
	String JOB_CARGA_LDAP_HABILITADO = "JOB_CARGA_LDAP_HABILITADO";
	String JOB_CARGA_LDAP_DESACTIVACION = "JOB_CARGA_LDAP_DESACTIVACION";
	
}