package com.ibm.bbva.ctacte.constantes;

import java.text.SimpleDateFormat;
import java.util.Properties;

import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

public interface ConstantesBusiness {

	SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyyMMddHHmmss");

	SimpleDateFormat FORMATO_FECHA_2 = new SimpleDateFormat("dd/MM/yyyy");

	int CARACTERES_CODIGO_CENTRAL = 8;
	
	int CARACTERES_NUMERO_DOI = 11;

	int MIN_CARACTERES_NUMERO_DOI = 8;

	int CUMPLE_VALIDACION_HOST = 1;

	int NO_CUMPLE_VALIDACION_HOST = 0;

	int CUMPLE_VALIDACION_SFP = 1;

	int NO_CUMPLE_VALIDACION_SFP = 0;

	String ESTADO_CUENTA_ACTIVO = "ACT";

	int ID_DOCUMENTO_DOI = 2;

	int ID_DOCUMENTO_FRF = 3;

	int DENTRO_PLAZO_SUBSANACION = 1;

	int FUERA_PLAZO_SUBSANACION = 0;
	String MENSAJE_FUERA_PLAZO_SUBSANACION = "SUBSANACION FUERA DE PLAZO";

	String CODIGO_SUBPRODUCTO_NEGOCIO_PNN_PEN = "0067";
	String CODIGO_SUBPRODUCTO_NEGOCIO_PJ_PEN = "0059";
	String CODIGO_SUBPRODUCTO_NEGOCIO_PJ_USD = "0060";

	String CODIGO_SUBPRODUCTO_E_EMPRESARIO_PJ_PEN = "0065";
	String CODIGO_SUBPRODUCTO_E_EMPRESARIO_PJ_USD = "0066";

	// el valor del estado inactivo es I
	String ESTADO_CLIENTE_SFP_ACTIVO = "A";
	String ESTADO_CLIENTE_SFP_DESC_ACTIVO = "ACTIVO";

	// String codigo Documento DOI Content

	String DOCUMENTO_IDENTIDAD = "DOID0";

	// no definido CU 0(8-03)
	// Integer ESTADO_EXPEDIENTE_TERMINADO = 3;

	String CODIGO_NUEVO_BASTANTEO = "01";

	String CODIGO_MODIFICATORIA_BASTANTEO = "02";

	String CODIGO_CONSULTA_BASTANTEO = "03";

	String CODIGO_CAMBIO_FIRMAS = "04";

	String CODIGO_SUBSANACION_BASTANTEO = "05";

	String CODIGO_REVOCATORIA_ESPECIFICA = "06";
	
	String DESC_SUBSANACION_BASTANTEO = "SUBSANACI�N DE BASTANTEO";

	String TIPO_MOTIVO_RECHAZO_DOCUMENTO = "RECHAZO DOCUMENTO";

	String TIPO_MOTIVO_REASIGNACION_BANDEJA = "REASIGNACION BANDEJA";

	String TIPO_MOTIVO_CANCELAR_PROCESO = "CANCELAR PROCESO";

	String APROBADO = "Aprobado";

	String RECHAZADO = "Rechazado";

	String REVOCATORIA_APROBADA = "1";

	String REVOCATORIA_RECHAZADA = "0";

	String CODIGO_DOCUMENTO_COPIA_LITERAL = "CLPE0";

	String CODIGO_DOCUMENTO_OFICIAL_IDENTIDAD = "DOID0";

	String CODIGO_CARTA_REVOCATORIA="CARC0";
	
	String CODIGO_DOCUMENTO_FICHA_REGISTRO_FIRMAS = "FRFI0";
	
	String CODIGO_DOCUMENTO_DICTAMEN = "DCTA0";
	
	String CODIGO_DOCUMENTO_INSTRUCCIONES = "INST0";
	
	String TIPO_DOCUMENTO_LEGAL = "Documentos SSJJ";

	String EMPLEADO_TIPO_ABOGADO = "1";

	String MOTIVO_REASIGNADOS = "REASIGNACION";

	String FLAG_EXONERADO_COBRO_COMISION = "0";

	String FLAG_COBRO_COMISION = "1";

	String FLAG_EJECUTO_COBRO_COMISION = "1";
	
	String FLAG_FACULTADES_ESPECIALES="1";
	
	String FLAG_INDICADOR_GRABO_BASTANTEO ="1"; // Se realizo con �xito el bastanteo en SFP
	
	String FLAG_NO_FACULTADES_ESPECIALES="0";
	
	String FLAG_NO_EJECUTO_COBRO_COMISION = "0";
	
	String FLAG_INDICADOR_EJECUTO_COBRO_COMISION_INMEDIATO = "0"; // Se realiz� con �xito el cobro comisi�n inmediato
	
	String FLAG_INDICADOR_FALLO_COBRO_COMISION_INMEDIATO = "1";

	String LISTA_CERRADA_ACTIVA = "1";

	String FLAG_ESCANEADO = "1";

	String FLAG_NO_ESCANEADO = "0";

	String FLAG_ESTA_EN_CONTENT = "1";

	String FLAG_NO_REQUIERE_ESCANEO = "0";

	String FLAG_REQUIERE_ESCANEO = "1";

	String FLAG_OBLIGATORIO = "1";
	
	String FLAG_NO_OBLIGATORIO = "0";

	String FLAG_TIENE_CORREO = "1";

	String FLAG_NO_TIENE_CORREO = "0";

	String FLAG_TIENE_SMS = "1";

	String FLAG_NO_TIENE_SMS = "0";

	Character FLAG_EXISTE_FRF = '1';

	Character FLAG_NO_EXISTE_FRF = '0';

	Character FLAG_EXISTE_DOI = '1';

	Character FLAG_NO_EXISTE_DOI = '0';

	String FLAG_FIRMA_ASOCIADA = "1";

	String FLAG_VIGENCIA_DOCUMENTO = "1";

	String FLAG_EMPLEADO_ACTIVO = "1";

	String FLAG_EMPLEADO_INACTIVO = "0";

	String EXISTE_FIRMA_ASOCIADA = "1";

	String NO_EXISTE_FIRMA_ASOCIADA = "0";

	String EXISTE_FIRMA_NO_ASOCIADA = "1";

	String NO_EXISTE_FIRMA_NO_ASOCIADA = "0";

	String EXISTE_FACULTADES_ESPECIALES = "1";

	String NO_EXISTE_FACULTADES_ESPECIALES = "0";

	String FLAG_EXISTE_MODIFICATORIA = "1";

	String FLAG_NO_EXISTE_MODIFICATORIA = "0";

	// Estados de Expediente
	// int COD_ESTADO_EXP_PRE_REGISTRO = 1;
	// int COD_ESTADO_EXP_CANCELADO = 3;
	// int COD_ESTADO_EXP_PENDIENTE_SUBSANAR_DOCUMENTO = 4;
	// int COD_ESTADO_EXP_FINALIZADO_VERIFICAR_CALIDAD_DOCUMENTOS = 5;
	// int COD_ESTADO_EXP_PENDIENTE_REVISION=6;
	// int COD_ESTADO_EXP_PENDIENTE_REVISAR_EJECUTAR_REVOCATORIA=7;
	// int COD_ESTADO_EXP_PENDIENTE_VERIFICAR_RESULTADO_TRAMITE=8;
	// int COD_ESTADO_EXP_VERIFICAR_RESULTADO_TRAMITE=9;
	// int COD_ESTADO_EXP_DEVUELTO_REVISAR_APROBAR_BASTANTEO=10;
	// int
	// COD_ESTADO_EXP_PENDIENTE_VERIFICAR_RESULTADO_TRAMITE_REALIZAR_BASTANTEO=11;
	int ID_ESTADO_EXPEDIENTE_PREREGISTRO = 1;
	int ID_ESTADO_EXPEDIENTE_ENCURSO = 2;
	int ID_ESTADO_EXPEDIENTE_TERMINADO = 3;
	int ID_ESTADO_EXPEDIENTE_CANCELADO = 4;

	// Estado Tarea
	int ID_ESTADO_TAREA_COMPLETADO = 3;
	int ID_ESTADO_TAREA_CANCELADO = 4;
	int ID_ESTADO_TAREA_PREREGISTRO = 1;
	int ID_ESTADO_TAREA_PENDIENTE = 2;

	// Descripcion Estado Tarea
	String DESC_ESTADO_TAREA_COMPLETADO = "Completado";
	String DESC_ESTADO_TAREA_CANCELADO = "Cancelado";

	// Tareas
	int ID_TAREA_REGISTRAR_EXPEDIENTE = 1;
	int ID_TAREA_SUBSANAR_FIRMA = 3;
	int ID_TAREA_SUBSANAR_DOCUMENTO = 4;
	int ID_TAREA_PRE_REGISTRO = 7;
	int ID_TAREA_VERIFICAR_RESULTADO_TRAMITE = 10;
	int ID_TAREA_VERIFICAR_CALIDAD_DOCUMENTOS = 11;
	int ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO = 12;
	int ID_TAREA_REVISAR_APROBAR_BASTANTEO = 13;
	int ID_TAREA_EJECUTAR_CONFIRMAR_MODIFICATORIAS = 14;
	int ID_TAREA_VERIFICAR_CALIDAD_FIRMAS = 15;
	int ID_TAREA_REVISAR_EJECUTAR_REVOCATORIA = 29;
	int ID_TAREA_GESTIONAR_COBRO_COMISION = 23;
	int ID_TAREA_APROBAR_REVOCATORIA = 27;
	int ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO_2 = 30; // igual que la original pero asociado al perfil de Pool de migracion
	
	// Mensajes
	int ID_MENSAJE_TIENE_FIRMAS = 1;
	int ID_MENSAJE_ADVERTENCIA_DOCUMENTO_COMBINADO = 2;
	
	// Caso Negocio
	int ID_CASO_NEGOCIO_NINGUNO = 1;

	String CODIGO_TIPO_CUENTA_TEMP_CUALQUIERA = "-2";
	String CODIGO_TIPO_CUENTA_CORRIENTE = "01";// 00110
	String CODIGO_TIPO_CUENTA_AHORROS = "02";
	String CODIGO_TIPO_CUENTA_PLAZO = "03";
	String CODIGO_MONEDA_NACIONAL = "PEN";
	String CODIGO_MONEDA_EXTRANJERA = "USD";

	String CODIGO_DOCUMENTO_VOUCHER_PAGO_COMISION_BASTANTEO = "VCBP0";

	int CAMPO_VACIO = 0;

	String STR_CAMPO_VACIO = "";

	String MOTIVO_DEVOLUCION = "DEVOLUCION";

	// Acciones del Expediente
	String ACCION_APROBAR_DOCUMENTACION = "APROBAR_DOCUMENTACION";
	String ACCION_RECHAZAR_DOCUMENTACION = "RECHAZAR_DOCUMENTACION";
	String ACCION_DEVOLVER_A_MESA_SSJJ = "DEVOLVER_A_MESA_SSJJ";
	String ACCION_FINALIZAR_BASTANTEO = "FINALIZAR_BASTANTEO";
	String ACCION_TERMINAR_ATENCION_CONFORME = "TERMINAR_ATENCION_CONFORME";
	String ACCION_DEVOLVER_INSTRUCCION = "DEVOLVER_INSTRUCCION";
	String ACCION_REASIGNAR_EXPEDIENTE = "REASIGNAR_EXPEDIENTE";

	String ACCION_EXPEDIENTE_APROBAR = "Aprobar";
	String ACCION_EXPEDIENTE_APROBAR_NO_APROBAR = "No Aprobado";
	String ACCION_EXPEDIENTE_RECHAZAR = "Rechazar";
	String ACCION_EXPEDIENTE_DEVOLVER = "Devolver";
	String ACCION_EXPEDIENTE_CONFORME = "Terminar Conforme";
	String ACCION_EXPEDIENTE_FINALIZAR = "Finalizar";
	String ACCION_EXPEDIENTE_REASIGNAR = "Reasignar";
	String ACCION_EXPEDIENTE_TERMINAR = "Terminar";
	String ACCION_EXPEDIENTE_CANCELADO = "CANCELADO";

	// Acciones Resultado Expediente
	String ACCION_EXPEDIENTE_APROBADO = "Aprobado";
	String ACCION_EXPEDIENTE_APROBADO_PARCIAL = "Aprobado Parcial";
	String ACCION_EXPEDIENTE_OBSERVADO = "Observado";
	String OPERACION_REVOCATORIA_SFP = "3";
	String OPERACION_REVOCATORIA_IBM = "6";
	String OPERACION_REVOCATORIA_SFP_STR = "03";
	String OPERACION_REVOCATORIA_IBM_STR = "06";

	// Codigo Resultado Bastanteo para SFP
	String CODIGO_BASTANTEO_APROBADO="01";
	String CODIGO_BASTANTEO_APROBADO_PARCIAL="02";
	String CODIGO_BASTANTEO_OBSERVADO="03";
	
	// Numero de documentos que se deben obtener de CM
	int NUM_DOC_HISTORICOS_RECIBIDOS_CM = 3;

	// Paginas del Sistema SFP
	String PAGINA_ASOCIACION_FIRMAS = "webservices_firmas/jsp/firmas/lectorparametros.jsp";
	// String PAGINA_ASOCIACION_FIRMAS_IBM =
	Properties PROPIEDADES = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
	//String PAGINA_ASOCIACION_FIRMAS_IBM = "http://118.180.34.15:9080/SFPWS/jsp/firmas/lectorparametros.jsp";
	String PAGINA_ASOCIACION_FIRMAS_IBM = PROPIEDADES.getProperty(ConstantesParametros.PAGINA_ASOCIACION_FIRMAS);
	String PAGINA_REALIZAR_BASTANTEO = "/webservices_firmas/jsp/bastanteo/lectorparametros.jsp";
	// String PAGINA_REALIZAR_BASTANTEO_IBM = "ProyectoDos/carga_2.jsp";
	//String PAGINA_REALIZAR_BASTANTEO_IBM = "http://118.180.34.15:9080/SFPWS/jsp/bastanteo/lectorparametros.jsp";
	String PAGINA_REALIZAR_BASTANTEO_IBM = PROPIEDADES.getProperty(ConstantesParametros.PAGINA_REALIZAR_BASTANTEO);
	//String RUTA_CM_VISOR = "http://9.6.98.125:9080/BBVA_CE_VisorContentManager/VisorCM.jsp?document=";
	//String RUTA_CM_VISOR = "http://118.180.60.70:9080/BBVA_CE_VisorContentManager/VisorCM.jsp?document=";
	String RUTA_CM_VISOR = "http://"+PROPIEDADES.getProperty(ConstantesParametros.SERVIDOR_CONTENT)+":"+
			PROPIEDADES.getProperty(ConstantesParametros.PUERTO_CONTENT)+"/BBVA_CE_CM_WAS/VisorCM.jsp?document=";
	String PAGINA_CONSULTA_PODERES_IBM = PROPIEDADES.getProperty(ConstantesParametros.PAGINA_CONSULTA_PODERES);
	
	//Parametro tipo PJ Migracion
	String CODIGO_TIPO_PJ_MIGRACION = (PROPIEDADES.getProperty(ConstantesParametros.CODIGO_TIPO_PJ_MIGRACION)==null)?"26":PROPIEDADES.getProperty(ConstantesParametros.CODIGO_TIPO_PJ_MIGRACION);
	

	// Parametros del Sistema SFP
	String SFP_PARAMETRO_EXPEDIENTE = "e=";
	String SFP_PARAMETRO_USUARIO = "r=";
	String SFP_PARAMETRO_ALEATORIO = "a=";
	int SFP_PARAMETRO_MINIMO_ALEATORIO = 1;
	int SFP_PARAMETRO_MAXIMO_ALEATORIO = 99999;

	String CODIGO_PERFIL_GESTOR = "15";
	String CODIGO_PERFIL_MESA_DOCUMENTOS = "17";
	String CODIGO_PERFIL_MESA_FIRMAS = "16";
	String CODIGO_PERFIL_ABOGADO_BASTANTEO = "18";
	String CODIGO_PERFIL_ABOGADO_REVISION = "19";
	String CODIGO_PERFIL_DIGITADOR = "20";
	String CODIGO_PERFIL_GERENTE_OFICINA = "22"; // falta ingresar en BD
	String CODIGO_PERFIL_JEFE_BASTANTEO = "23";
	String CODIGO_PERFIL_SUPERVISOR_FIRMA = "24"; // falta ingresar en BD
	String CODIGO_PERFIL_SUPERVISOR_DIGITADOR = "25"; // falta ingresar en BD
	String CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO = "21";
	String CODIGO_PERFIL_ADMINISTRADOR = "14";
	String DESCRIPCION_OPERACION_REVOCATORIA = "Revocatoria espec�fica";
	String CODIGO_PERFIL_MIGRADOR = "26";
	String CODIGO_PERFIL_CONSULTA = "13";

	String TIPO_VISOR_AVANZADO = "A";
	String CODIGO_OK_SERVICIO_SCE_QSP5 = "0000";
	
	Character SIN_DOCUMENTOS = '1';
	Character FALTA_DOCUMENTOS = '2';
	Character TODO_DOCUMENTOS = '3';
	
	String CODIGO_ERROR_POR_FIRMAS_SERVICIO_SFP = "01";
	String CODIGO_ERROR_POR_PODERES_SERVICIO_SFP = "02";
	String CODIGO_ERROR_POR_SISTEMA_SERVICIO_SFP = "03";
	
	String ASUNTO_CORREO_ACTIVACION_FIRMAS = "DETALLE ACTIVACI�N FIRMAS Y PODERES";

	// [Begin]-[15.04.06]-[Exoneracion cobro de comision por tipo de subproducto, cuando es un nuevo bastanteo]
	Character SI = 'S';
	Long CODIGO_PADRE_PRODUCTO_EXONERADOS = 101L;
	// [End]-[15.04.06]-[Exoneracion cobro de comision por tipo de subproducto, cuando es un nuevo bastanteo]
	
	// [Begin]-[15.04.08]-[Habilitaci�n del bot�n Finalizar Proceso seg�n configuraci�n]
	String CODIGO_MODULO_CONF = "CONF";
	String CODIGO_FLAG_HABILITAR_FINALIZAR_BASTANTEO = "flagFinalizarBastanteo";
	String CODIGO_FLAG_HABILITAR_TERMINAR_VINCULACION = "flagTerminarVinculacion";
	// [End]-[15.04.08]-[Habilitaci�n del bot�n Finalizar Proceso seg�n configuraci�n]
}
