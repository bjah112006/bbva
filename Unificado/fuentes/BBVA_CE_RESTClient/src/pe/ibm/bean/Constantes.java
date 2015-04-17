package pe.ibm.bean;


public interface Constantes {

	String NOMBRE_PROCESS_TEMPLATE_TC = "CE_TC_EXP_";
	
	String PROCESS_TEMPLATE = "TarjetaCredito";
	
	String PROCESS_TEMPLATE_CM = "VerificacionContent";
	
	String NOMBRE_DATA_OBJECT_EXPEDIENTE = "ExpedienteTC";
	String NOMBRE_DATA_OBJECT_CM_SALIDA = "estadoFinalizado";
	
	String NOMBRE_DATA_OBJECT_CONTENT_MANAGER = "ContentManager";
	
	String CUSTOM_PROPERTY_NAVEGACION = "nombreNavegacionWeb";
	
	String CUSTOM_PROPERTY_ID_TAREA = "idTarea";
	
	String ESTADO_ANTERIOR = "estadoAnterior";
	
	String ID_TAREA_ERROR_ARCHIVOS = "28";
	
	// Expediente Tarjeta Credito
	
	String DATA_OBJECT_EXPEDIENTE_ACCION = "Accion";
	
	String DATA_OBJECT_EXPEDIENTE_CODIGO = "Codigo";
	
	String DATA_OBJECT_EXPEDIENTE_CONFORME = "Conforme";
	
	String DATA_OBJECT_EXPEDIENTE_DELEGACION = "Delegacion";
	
	String DATA_OBJECT_EXPEDIENTE_DEVOLVER = "Devolver";
	
	String DATA_OBJECT_EXPEDIENTE_DEVUELTO_POR = "DevueltoPor";
	
	String DATA_OBJECT_EXPEDIENTE_ESTADO = "Estado";
	
	String DATA_OBJECT_EXPEDIENTE_LINEA_CREDITO = "LineaCredito";
	
	String DATA_OBJECT_EXPEDIENTE_MODIFICACION_SCORING = "ModificacionScoring";
	
	String DATA_OBJECT_EXPEDIENTE_MONEDA = "Moneda";
	
	String DATA_OBJECT_EXPEDIENTE_SCORING_APROBADO = "ScoringAprobado";
	
	String DATA_OBJECT_EXPEDIENTE_SEGMENTO = "Segmento";

	String DATA_OBJECT_EXPEDIENTE_SUBSANA = "Subsana";

	String DATA_OBJECT_EXPEDIENTE_TIPO_OFERTA = "TipoOferta";

	String DATA_OBJECT_EXPEDIENTE_VERIFICACION_DOMICILIARIA = "VerificacionDomiciliaria";

	String DATA_OBJECT_EXPEDIENTE_CLIENTE = "Cliente";

	String DATA_OBJECT_EXPEDIENTE_PRODUCTO = "Producto";
	
	String DATA_OBJECT_EXPEDIENTE_CODIGO_RVGL = "CodigoRVGL";
	
	String DATA_OBJECT_EXPEDIENTE_CODIGO_PRE_EVALUADOR = "CodigoPreEvaluador";
	
	String DATA_OBJECT_EXPEDIENTE_ID_OFICINA = "IdOficina";
	
	String DATA_OBJECT_EXPEDIENTE_ID_TERRITORIO = "IdTerritorio";
	
	String DATA_OBJECT_EXPEDIENTE_CODIGO_EMPLEADO_RESPONSABLE = "CodigoEmpleadoResponsable";
	
	String DATA_OBJECT_EXPEDIENTE_CODIGO_USUARIO_ACTUAL = "CodigoUsuarioActual";
	
	String DATA_OBJECT_EXPEDIENTE_ID_PERFIL_USUARIO_ACTUAL = "IdPerfilUsuarioActual";

	String DATA_OBJECT_EXPEDIENTE_NOMBRE_USUARIO_ANTERIOR = "NombreUsuarioAnterior";
	
	String DATA_OBJECT_EXPEDIENTE_PERFIL_USUARIO_ANTERIOR = "PerfilUsuarioAnterior";
	
	String DATA_OBJECT_EXPEDIENTE_CODIGO_USUARIO_ANTERIOR = "CodigoUsuarioAnterior";
	
	String DATA_OBJECT_EXPEDIENTE_NUMERO_CONTRATO = "NumeroContrato";
	
	String DATA_OBJECT_EXPEDIENTE_ENVIO_CONTENT = "EnvioContent";
	
	// Cliente
	
	String DATA_OBJECT_CLIENTE_TIPO_DOI = "TipoDOI";
	
	String DATA_OBJECT_CLIENTE_NUMERO_DOI = "NumeroDOI";
	
	String DATA_OBJECT_CLIENTE_AP_PATERNO = "ApPaterno";
	
	String DATA_OBJECT_CLIENTE_AP_MATERNO = "ApMaterno";
	
	String DATA_OBJECT_CLIENTE_NOMBRE = "Nombre";
	
	String DATA_OBJECT_CLIENTE_CODIGO_CLIENTE = "CodigoCliente";
	
	String DATA_OBJECT_CLIENTE_CODIGO_TIPO_CLIENTE = "CodigoTipoCliente";  
	
	String DATA_OBJECT_CLIENTE_DESCRIPCION_TIPO_CLIENTE = "DescripcionTipoCliente";  
	
	// Producto
	
	String DATA_OBJECT_PRODUCTO_PRODUCTO = "Producto";
	
	String DATA_OBJECT_PRODUCTO_SUB_PRODUCTO = "SubProducto";
	
	String DATA_OBJECT_PRODUCTO_ID_PRODUCTO = "IdProducto";
	
	// Content Manager
	
	String DATA_OBJECT_CONTENT_MANAGER_CODIGO = "Codigo";
	
	String DATA_OBJECT_CONTENT_MANAGER_ESTADO = "Estado";
	
	// Proceso
	
	String USUARIO_ADMINISTRADOR_1 = "userAD";
	String USUARIO_ADMINISTRADOR_2 = "admin";
	String TIEMPO_ESPERA_CM_SEGUNDOS = "900";
	String TIEMPO_ARCHIVAR_EXPEDIENTE_SEGUNDOS = "600";
	
	// ****************************  CTA CORRIENTE *******************************************************************
	
	String CUSTOM_PROPERTY_ID_TAREA_CC = "idTarea";
	
	String CUSTOM_PROPERTY_CODUSUARIOACTUAL_CC = "codUsuarioActual";
	String CUSTOM_PROPERTY_NOMUSUARIOACTUAL_CC = "nomUsuarioActual";	
	String CUSTOM_PROPERTY_CODABOGADOACTUAL_CC = "codAbogadoActual";
	String CUSTOM_PROPERTY_NOMABOGADOACTUAL_CC = "nomAbogadoActual";
	String CUSTOM_PROPERTY_CODESTUDIOABOGADO_CC = "codEstudioAbogado";
	String CUSTOM_PROPERTY_NOMESTUDIOABOGADO_CC = "nomEstudioAbogado";
	
	
	String CUSTOM_PROPERTY_NAVEGACION_CC = "nombreNavegacionWeb";
	String NOMBRE_DATA_OBJECT_CM_SALIDA_CC = "estadoFinalizado";
	String ID_TAREA_ERROR_ARCHIVOS_CC = "28";
	String ESTADO_ANTERIOR_CC = "estadoAnterior";
	
	String PROCESS_TEMPLATE_CTACTEPJ = "CuentaCorrientePJ";
	String PROCESS_TEMPLATE_GEST_FIRM = "GestionFirmas";
	String PROCESS_TEMPLATE_GEST_DOC = "GestionDocumento";
	String PROCESS_TEMPLATE_GEST_BAST = "GestionBastanteo";
	String PROCESS_TEMPLATE_GEST_OPER_CC = "GestionOperacionesCtaCte";
	String PROCESS_TEMPLATE_GEST_COBRO_COM = "GestionCobroComisionBastanteo";
	String NOMBRE_PROCESS_TEMPLATE_CTACTE = "CE_CTACTE_EXP_";
	String NOMBRE_DATA_OBJECT_EXPEDIENTE_CC = "ExpedienteCC";
	
	//Expediente 
	String DATA_OBJECT_CODIGO_EXPEDIENTE = "codigoExpediente";
	String DATA_OBJECT_ESTADO_EXPEDIENTE = "estadoExpediente";
	String DATA_OBJECT_ID_ESTADO_EXPEDIENTE = "idEstadoExp";
	
	String DATA_OBJECT_NUMERO_TAREA = "numeroTarea";
	String DATA_OBJECT_NOMBRE_TAREA = "nombreTarea";
	String DATA_OBJECT_ESTADO_TAREA = "estadoTarea";
	String DATA_OBJECT_ID_ESTADO_TAREA = "idEstadoTarea";
	String DATA_OBJECT_COD_USUARIO_ACTUAL = "codUsuarioActual";
	String DATA_OBJECT_ID_USUARIO_ACTUAL = "idUsuarioActual";
	String DATA_OBJECT_NOMBRE_USUARIO_ACTUAL = "nomUsuarioActual";
	String DATA_OBJECT_CODIGO_OFICINA = "codOficina";
	String DATA_OBJECT_DES_OFICINA = "desOficina";
	String DATA_OBJECT_DES_TERRITORIO = "desTerritorio";
	String DATA_OBJECT_COD_OPERACION = "codOperacion";
	String DATA_OBJECT_ID_OPERACION = "idOperacion";
	String DATA_OBJECT_DES_OPERACION = "desOperacion";
	String DATA_OBJECT_COD_CENTRAL_CLIENTE = "codCentralCliente";
	String DATA_OBJECT_NUMERO_DOI_CLIENTE = "numDOICliente";
	String DATA_OBJECT_RAZON_SOCIAL_CLIENTE = "razonSocialCliente";
	String DATA_OBJECT_FECHA_REGISTRO = "FechaRegistro";
	String DATA_OBJECT_FECHA_ULTIMO_BASTANTEO = "FechaUltimoBastanteo";
	String DATA_OBJECT_DATOS_FLUJO_CTACTE = "datosFlujoCtaCte";
	String DATA_OBJECT_OPERACIONES_CTACTE = "operacionesCtaCte";
	//String DATA_OBJECT_FECHA_ATENCION = "fechaAtencion";
//	String DATA_OBJECT_MINUTOS_AMARILLO = "minutosAmarillo";
//	String DATA_OBJECT_MINUTOS_VERDE = "minutosVerde";
	// DatosFlujoCtaCte
	String DATA_OBJECT_ACCION = "accion";
	String DATA_OBJECT_CLIENTE_EXONERADO = "clienteExonerado";
	String DATA_OBJECT_EXISTE_FILE_FRF = "existeFileFRF";
	String DATA_OBJECT_EXISTE_FILE_DOI = "existeFileDOI";
	String DATA_OBJECT_ESTADO_DOCS = "estadoDocs";
	String DATA_OBJECT_FLAG_EXISTE_FIRMA_ASOCIADA = "flagExisteFirmaAsociada";
	String DATA_OBJECT_FLAG_EXISTE_FIRMA_NO_ASOCIADA = "flagExisteFirmaNoAsociada";
	String DATA_OBJECT_FLAG_EXISTE_FACULTADES_ESPECIALES = "flagExisteFacultadesEspeciales";
	String DATA_OBJECT_FLAG_EXISTE_MODIFICATORIA = "flagExisteModificatoria";
	String DATA_OBJECT_FLAG_REVOCATORIA_APROBADA = "flagRevocatoriaAprobada";
	String DATA_OBJECT_FLAG_EXISTE_POR_ACTIVAR = "flagExisteFirmaPorActivar";
	String DATA_OBJECT_COD_USUARIO_ABOGADO = "codUsuarioAbogado";
	String DATA_OBJECT_ID_USUARIO_ABOGADO = "idUsuarioAbogado";
	String DATA_OBJECT_NOM_USUARIO_ABOGADO = "nomUsuarioAbogado";
	String DATA_OBJECT_ID_TAREA = "idTarea";
	//String DATA_OBJECT_NOMBRE_TAREA2 = "nombreTarea";
	String DATA_OBJECT_COD_USUARIO_RESPONSABLE = "codUsuarioResponsable";
	String DATA_OBJECT_ID_USUARIO_RESPONSABLE = "idUsuarioResponsable";
	String DATA_OBJECT_NOM_USUARIO_RESPONSABLE = "nomUsuarioResponsable";
	String DATA_OBJECT_ID_TERRITORIO = "idTerritorio";
	String DATA_OBJECT_ID_PRODUCTO = "idProducto";
	String DATA_OBJECT_RESULTADO_BASTANTEO = "resultadoBastanteo";
	String DATA_OBJECT_NOM_ESTUDIO_ABOGADO = "nomEstudioAbogado"; 
	String DATA_OBJECT_COD_ESTUDIO_ABOGADO = "codEstudioAbogado";
	
	//OperacionesCtaCte
	String DATA_OBJECT_TIPO_FLUJO_OPERACION = "tipoFlujoOperacion";
	String DATA_OBJECT_FLAG_CRITERIO_AUDITORIA_INTERNA = "flagCriterioAuditoriaInterna";
	String DATA_OBJECT_FLAG_ESTADO_BASTANTEO = "flagEstadoBastanteo";
	String DATA_OBJECT_FLAG_PLAZO_SUBSANACION = "flagPlazoSubsanacion";
	String DATA_OBJECT_FLAG_COBRO_COMISION = "flagCobroComision";
	String DATA_OBJECT_ID_EMPLEADO_ACTUAL  = "idEmpleadoActual";
	
	//Proceso
	String USUARIO_ADMINISTRADOR_CC_1 = "userCCAD";
	
	/*Constante usuario Process*/
	String CONSTANTE_PROCESS_USER = "bbva.process.username";
	String CONSTANTE_PROCESS_PASS = "bbva.process.password";
	String CONSTANTE_PROCESS_CC_USER = "bbva.process.cc.username";
	String CONSTANTE_PROCESS_CC_PASS = "bbva.process.cc.password";
	
	
	String ID_TAREA_1 = "1";
	String ID_TAREA_2 = "2";
	String ID_TAREA_3 = "3";
	String ID_TAREA_4 = "4";
	String ID_TAREA_5 = "5";
	String ID_TAREA_6 = "6";
	String ID_TAREA_7 = "7";
	String ID_TAREA_10 = "10";
	String ID_TAREA_11 = "11";
	String ID_TAREA_12 = "12";
	String ID_TAREA_13 = "13";
	String ID_TAREA_14 = "14";
	String ID_TAREA_15 = "15";
	String ID_TAREA_16 = "16";
	String ID_TAREA_17 = "17";
	String ID_TAREA_18 = "18";
	String ID_TAREA_19 = "19";
	String ID_TAREA_20 = "20";
	String ID_TAREA_26 = "26";
	String ID_TAREA_27 = "27";
	String ID_TAREA_32 = "32";
	String ID_TAREA_33 = "33";		
	String RUTA_NAVEGACION_WEB_TAREA_1 = "/registrarExpediente/formRegistrarExpediente?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_2 = "/verificarConformidadExpediente/formVerificarConformidadExpediente?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_3 = "/registrarDatos/formRegistrarDatos?faces-redirect=true\";";
	String RUTA_NAVEGACION_WEB_TAREA_4 = "/aprobarExpediente/formAprobarExpediente?faces-redirect=true\";";
	String RUTA_NAVEGACION_WEB_TAREA_5 = "/verificarResultadoDomiciliaria/formVerificarResultadoDomiciliaria?faces-redirect=true\";";
	String RUTA_NAVEGACION_WEB_TAREA_6 = "/registrarAprobResolucion/formRegistrarAprobResolucion?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_7 = "/realizarAltaTarjeta/formRealizarAltaTarjeta?faces-redirect=true\";";
	String RUTA_NAVEGACION_WEB_TAREA_10 = "/coordinarClienteSubsanar/formCoordinarClienteSubsanar?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_11 = "/verificarExpDesestimados/formVerificarExpDesestimados?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_12 = "/ejecutarEvalCrediticia/formEjecutarEvalCrediticia?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_13 = "/cambiarSituacionExp/formCambiarSituacionExp?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_14 = "/anularModificarOpCu14/formAnularModificarOpCu14?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_15 = "/evaluarDevolucionRiesgos/formEvaluarDevolucionRiesgos?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_16 = "/consultarClienteModificaciones/formConsultarClienteModificaciones?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_17 = "/cambiarSituacionTramite/formCambiarSituacionTramite?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_18 = "/anularModificarOpCu18/formAnularModificarOpCu18?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_19 = "/revisarRegistrarDictamen/formRevisarRegistrarDictamen?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_20 = "/regularizarEscanearDocumentacion/formRegularizarEscanearDocumentacion?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_26 = "/solicitarVerificacionDomiciliaria/formSolicitarVerificacionDomiciliaria?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_27 = "/archivarExpediente/formArchivarExpediente?faces-redirect=true\";";
	String RUTA_NAVEGACION_WEB_TAREA_32 = "/corregirExpediente32/formCorregirExpediente32?faces-redirect=true";
	String RUTA_NAVEGACION_WEB_TAREA_33 = "/corregirExpediente33/formCorregirExpediente33?faces-redirect=true";

	
	int ID_APLICATIVO_TC = 1;
	int ID_APLICATIVO_PLD = 2;
}

