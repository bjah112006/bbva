package com.ibm.bbva.controller;

import java.text.SimpleDateFormat;
 
public interface ConstantesAdmin {
	
	String PARAMETRO_MENSAJE = "parammsg";
	
	String EXTENSION_ARCHIVO_PDF = ".pdf";
	
	String EXPEDIENTE_SESION = "objExpSesion";
	
	String EMPLEADO_SESION = "objEmplSesion";
	
	String EXPEDIENTE_PROCESO_SESION = "ExpedienteCC";

	String NOMBRE_FORMULARIO = "form";
	
	String CODIGO_CAMPO_VACIO = "-1";

	int ID_OPERACION_NUEVO_BASTANTEO = 1;
	
	Character FLAG_VERDADERO = '1';
	
	Character FLAG_FALSO = '0';
	
	String PARAMETRO_TIPO_PRE_REGISTRO = "tipoPreRegistro";
	
	String SUJITO_HISTORIAL = "HIST";
	
	// Acciones de los botones
	String ACCION_GENERAR_PRE_REGISTRO = "GenerarPreRegistro";
	String ACCION_REGISTRAR_ENVIAR_EXPEDIENTE = "RegistrarEnviarExpediente";
	String NOMBRE_TABLA="nombreTabla";
	String CODIGO_TABLA_SELECCIONADA="codigoTablaSeleccionada";
	
	// Paginas
	
	String FORM_VERIFICAR_CALIDAD_DOCUMENTOS = "verificarCalidadDocumentos/formVerificarCalidadDocumentos";
	String FORM_AYUDA_MEMORIA = "ayudaMemoria/formAyudaMemoria";
	String FORM_HISTORIAL_EXPEDIENTE = "historial/formHistorial";
	String FORM_LOG = "log/formLog";
	String FORM_OBSERVACIONES_EXPEDIENTE = "observaciones/formObservaciones";
	String FORM_GESTIONAR_COBRO_COMISION = "formGestionarCobroComision";	
	String FORM_REVISAR_APROBAR_BASTANTEO="formRevisarAprobarBastanteo";
	String FORM_APROBAR_REVOCATORIA="formAprobarRevocatoria";	
	String FORM_SUBSANAR_DOCUMENTOS="formSubsanarDocumentos";
	String FORM_SUBSANAR_FIRMAS="formSubsanarFirmas";
	String FORM_EJECUTAR_CONFIRMAR_MODIFICATORIA="formEjecutarConfirmarModificatoria";
	String FORM_VERIFICAR_RESULTADO_TRAMITE="formVerificarResultadoTramite";
	String FORM_VERIFICAR_CALIDAD_FIRMA="formVerificarCalidadFirma";	
	String FORM_REVISAR_EJECUTAR_REVOCATORIA="formRevisarEjecutarRevocatoria";
	String FORM_VERIFICAR_REALIZAR_BASTANTEO="formVerificarRealizarBastanteo";
	
	/**
	 * Codigo para el campo vacio de la lista generada en la clase 
	 * com.ibm.bbva.util.Util
	 */
	String CODIGO_CODIGO_CAMPO_VACIO = "-1";
	/**
	 * Contiene el tipo de tabla
	 */
	String TIPO_TABLA_SESION = "tipoTablaMantenimiento";
	/**
	 * Valor cuando el Horario esta activo = 1
	 */
	String HORARIO_ACTIVO = "1";
	
	int NO_TIENE_MINUTOS = -1;
	
	int COD_TABLA_GUIA_DOCUMENTARIA=5;
	
	String TEXTO_FORMATO_HORA = "HH:mm";
	
	SimpleDateFormat FORMATO_HORA = new SimpleDateFormat (TEXTO_FORMATO_HORA);
	/**
	 * Codigo para el campo no seleccionado de la lista
	 */
	String CODIGO_NO_SELECCIONADO = "0";
	
	/*Codigo de Semaforo Verde*/
	String CODIGO_VERDE = "verde.png";
	/*Codigo de Semaforo Amarillo*/
	String CODIGO_AMARRILLO = "amarillo.png";
	/*Codigo de Semaforo Rojo*/
	String CODIGO_ROJO = "rojo.png";
	/*Codigo de Semaforo Verde*/
	String DESCRIPCION_VERDE = "VERDE";
	/*Codigo de Semaforo Amarillo*/
	String DESCRIPCION_AMARRILLO = "AMARILLO";
	/*Codigo de Semaforo Rojo*/
	String DESCRIPCION_ROJO = "ROJO";	
	
	/**
	 * Id de operacion de revocatoria = 6
	 * */
	String	ID_OPERACION_REVOCATORIA = "6";
	/**
	 * Flag Bandeja Mantenimiento
	 */
	String FLAG_BANDEJA_MANTENIMIENTO = "1";
	
	String RESPONSABLE_SESION = "objResponsableSesion";
	
	/*Retornar a ventana Mantenimiento*/
	
	String RETORNA_PANTALA_MANTENIMIENTO="retornaPantallaMantenimiento";
		
	String DATOS_CLIENTE_SESION = "objClienteSesion";
	String FLAG_AYUDA_MEMORIA_SESION = "editarAyudaMemoria";
	String AYUDA_MEMORIA_SESION2 = "editarAyudaMemoria2";
	String TABLA_ESTADO_TAREA = "56";
	String LISTA_ESTADO_TAREA = "listEstadoXTarea";

}