package pe.ibm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.ContentManager;
import pe.ibm.bean.ExpedienteTCWPS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Convertidor {

	private static final String NODE_TASKID = "tkiid";
//	private static final String NODE_NRO_DEVOLUCIONES = "BDNUMERO_DEVOLUCIONES";
	private static final String NODE_DESCRIPCION = "description";
	private static final String NODE_USER = "assignedTo";
	private static final String NODE_START_TIME = "startTime";
	private static final String CONS_FORMATO_FECHA_1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String CONS_FORMATO_TIME_ZONE = "GMT-5:00:00";
	private static final String NODE_EXP_TASK_ID = "taskID";
	private static final String NODE_EXP_ACTIVADO = "activado";
	private static final String NODE_EXP_NOMBRE_NAVEGACION_WEB = "nombreNavegacionWeb";
	private static final String NODE_EXP_ID_TAREA = "idTarea";
	private static final String NODE_EXP_TASK_DES = "desTarea";
	
	private static final String NODE_NRO_REINTENTO = "nroReintentos";
	private static final String NODE_DESCRIPCION_ERROR = "descripcionError";
	private static final String NODE_EXPEDIENTE = "expediente";
	private static final String NODE_DESCRIPCION_TAREA_ANT = "desTareaAnterior"; 
	private static final String NODE_CANT_DOCUMENTOS = "cantDocumentos";
	private static final String NODE_FECHA_RESTAURACION = "fechaRestauracion";
	private static final String NODE_FECHA_CANCELACION = "fechaCancelacion";
	private static final String NODE_TIPO_ERROR = "tipoError";
	private static final String NODE_FECHA_INCIDENCIA = "fechaIncidencia"; 
	
	private static final String ID_NO_EXISTE_TAREA = "-1";
	
	public static String fromExpedienteTCtoJSON (ExpedienteTCWPS expedienteTCWPS) {
		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().setDateFormat(CONS_FORMATO_FECHA_1).serializeNulls().create();
		String strJSON = gson.toJson(expedienteTCWPS);
		JsonElement jsonElement = new JsonParser().parse(strJSON);
		jsonElement.getAsJsonObject().remove(NODE_EXP_TASK_ID);
		jsonElement.getAsJsonObject().remove(NODE_EXP_ACTIVADO);
		jsonElement.getAsJsonObject().remove(NODE_EXP_ID_TAREA);
		jsonElement.getAsJsonObject().remove(NODE_EXP_NOMBRE_NAVEGACION_WEB);
		jsonElement.getAsJsonObject().remove(NODE_EXP_TASK_DES);
		jsonElement.getAsJsonObject().remove(NODE_NRO_REINTENTO);
		jsonElement.getAsJsonObject().remove(NODE_DESCRIPCION_ERROR);
		jsonElement.getAsJsonObject().remove(NODE_DESCRIPCION_TAREA_ANT); 
		jsonElement.getAsJsonObject().remove(NODE_CANT_DOCUMENTOS);
		jsonElement.getAsJsonObject().remove(NODE_FECHA_RESTAURACION);
		jsonElement.getAsJsonObject().remove(NODE_FECHA_CANCELACION);
		jsonElement.getAsJsonObject().remove(NODE_TIPO_ERROR);
		jsonElement.getAsJsonObject().remove(NODE_FECHA_INCIDENCIA);
		System.out.println("fromExpedienteTCtoJSON ::::"+jsonElement.toString());
		return jsonElement.toString();
	}
	
	public static ExpedienteTCWPS fromJSONtoExpedienteTC_Mantenimiento (String strJSON, JsonElement elementoA, String tipoDato) {
		JsonElement jsonElement = new JsonParser().parse(strJSON);
		String dato = jsonElement.getAsJsonObject().get(NODE_EXPEDIENTE).toString();
		
		ExpedienteTCWPS expedienteTCWPS = fromJSONtoExpedienteTC(dato,elementoA,"1");
		
		dato = jsonElement.getAsJsonObject().get(NODE_DESCRIPCION_ERROR).toString();
		expedienteTCWPS.setDescripcionError(dato);
		
		dato = jsonElement.getAsJsonObject().get(NODE_NRO_REINTENTO).toString();
		expedienteTCWPS.setNroReintentos(dato);
		return expedienteTCWPS;		
	}
	
	public static ExpedienteTCWPS fromJSONtoExpedienteTC (String strJSON, JsonElement elementoA, String tipoDato) {
		Gson gson = new Gson();
		ExpedienteTCWPS expedienteTC = gson.fromJson(strJSON, ExpedienteTCWPS.class);
		
		if ("1".equals(tipoDato)){
			expedienteTC.setTaskID(elementoA.getAsJsonObject().get(NODE_TASKID).getAsString());
			String codigoTarea = (elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString() == ""?"0":elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
			expedienteTC.setIdTarea(codigoTarea);
			expedienteTC.setNombreNavegacionWeb("");
			//expedienteTC.setCodigoUsuarioActual(elementoA.getAsJsonObject().get(NODE_USER).getAsString());
	
			Calendar cal = Calendar.getInstance();
	
		    try { //2013-10-28T18:47:34Z
				TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
				SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
				readFormat.setTimeZone(utc);
				System.out.println("TIEMPO !!!!!"+ elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
		    	Date fechaStart = readFormat.parse(elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
				cal.setTime(fechaStart);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	
		    expedienteTC.setActivado(cal);
		    
		}else {
			expedienteTC.setTaskID(ID_NO_EXISTE_TAREA);
			expedienteTC.setDesTarea("");
			
			Calendar cal = Calendar.getInstance();

		     //2013-10-28T18:47:34Z
			TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
			SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
			readFormat.setTimeZone(utc);
	    	Date fechaStart = new Date();
			cal.setTime(fechaStart);
			expedienteTC.setActivado(cal);
		}

	    System.out.println("********************** fromJSONtoExpedienteTC **********************");

	    System.out.println("Codigo Exp :::"+expedienteTC.getCodigo());
	    System.out.println("expTC activado::::"+expedienteTC.getActivado().getTime());

	    if ("1".equals(tipoDato)){
		    System.out.println("Perfil usuario actual :::"+expedienteTC.getPerfilUsuarioActual());
		    System.out.println("codigo Actividad:::"+elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
		    System.out.println("tiempo de inicio:::"+elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
			System.out.println("task ID BPD:::"+expedienteTC.getTaskID());
	    }
	    System.out.println("    ");

		return expedienteTC;
	}
	
	/*
	public static String fromExpedienteTCtoJSON (ExpedienteTCWPS expedienteTCWPS) {

		ExpedienteTC expediente = new ExpedienteTC();		
		expediente.setAccion(expedienteTCWPS.getAccion());
		
		if (expedienteTCWPS.getCliente()==null)
			expediente.setCliente(new Cliente());
		else
			expediente.setCliente(expedienteTCWPS.getCliente());
		
		expediente.setCodigo(expedienteTCWPS.getCodigo());
		expediente.setCodigoEmpleadoResponsable(expedienteTCWPS.getCodigoEmpleadoResponsable());
		expediente.setCodigoPreEvaluador(expedienteTCWPS.getCodigoPreEvaluador());
		expediente.setCodigoRVGL(expedienteTCWPS.getCodigoRVGL());
		expediente.setCodigoUsuarioActual(expedienteTCWPS.getCodigoUsuarioActual());
		expediente.setCodigoUsuarioAnterior(expedienteTCWPS.getCodigoUsuarioAnterior());
		expediente.setDevueltoPor(expedienteTCWPS.getDevueltoPor());
		expediente.setEnvioContent(expedienteTCWPS.getEnvioContent());
		expediente.setEstado(expedienteTCWPS.getEstado());
		expediente.setIdOficina(expedienteTCWPS.getIdOficina());
		expediente.setIdPerfilUsuarioActual(expedienteTCWPS.getIdPerfilUsuarioActual());
		expediente.setPerfilUsuarioActual(expedienteTCWPS.getPerfilUsuarioActual());
		expediente.setIdTerritorio(expedienteTCWPS.getIdTerritorio());
		expediente.setFlagProvincia(expedienteTCWPS.getFlagProvincia());
		expediente.setLineaCredito(expedienteTCWPS.getLineaCredito());
		
		expediente.setModificacionScoring(expedienteTCWPS.getModificacionScoring());
		expediente.setMoneda(expedienteTCWPS.getMoneda());
		expediente.setNombreUsuarioAnterior(expedienteTCWPS.getNombreUsuarioAnterior());
		expediente.setNumeroContrato(expedienteTCWPS.getNumeroContrato());
		expediente.setPerfilUsuarioAnterior(expedienteTCWPS.getPerfilUsuarioAnterior());
		
		if (expedienteTCWPS.getProducto()==null)
			expediente.setProducto(new Producto());
		else
			expediente.setProducto(expedienteTCWPS.getProducto());
		
		expediente.setScoringAprobado(expedienteTCWPS.getScoringAprobado());
		expediente.setSegmento(expedienteTCWPS.getSegmento());
		expediente.setTipoOferta(expedienteTCWPS.getTipoOferta());
		expediente.setVerificacionDomiciliaria(expedienteTCWPS.getVerificacionDomiciliaria());
		expediente.setFlagRetraer(expedienteTCWPS.getFlagRetraer());
		expediente.setIdPerfilUsuarioAnterior(expedienteTCWPS.getIdPerfilUsuarioAnterior());
		expediente.setFlagSubrogacion(expedienteTCWPS.getFlagSubrogacion());
		
		expediente.setNumeroDevoluciones(expedienteTCWPS.getNumeroDevoluciones());
		
		Gson gson = new Gson();
		String strJSON = gson.toJson(expediente);
		System.out.println("salidaaaa!!!!!"+strJSON);
		return strJSON;
	}
	
	public static ExpedienteTCWPS fromJSONtoExpedienteTC (String strJSON, JsonElement elementoA) {
		ExpedienteTCWPS expedienteTC = new ExpedienteTCWPS();

		Gson gson = new Gson();
		ExpedienteTC expediente = gson.fromJson(strJSON, ExpedienteTC.class);

		expedienteTC.setAccion(expediente.getAccion());
		expedienteTC.setCliente(expediente.getCliente());
		expedienteTC.setCodigo(expediente.getCodigo());
		expedienteTC.setCodigoEmpleadoResponsable(expediente.getCodigoEmpleadoResponsable());
		expedienteTC.setCodigoPreEvaluador(expediente.getCodigoPreEvaluador());
		expedienteTC.setCodigoRVGL(expediente.getCodigoRVGL());
		expedienteTC.setCodigoUsuarioActual(expediente.getCodigoUsuarioActual());
		expedienteTC.setCodigoUsuarioAnterior(expediente.getCodigoUsuarioAnterior());
		expedienteTC.setDevueltoPor(expediente.getDevueltoPor());
		expedienteTC.setEnvioContent(expediente.getEnvioContent());
		expedienteTC.setEstado(expediente.getEstado());
		expedienteTC.setIdOficina(expediente.getIdOficina());
		expedienteTC.setIdPerfilUsuarioActual(expediente.getIdPerfilUsuarioActual());
		expedienteTC.setPerfilUsuarioActual(expediente.getPerfilUsuarioActual());
		expedienteTC.setIdTerritorio(expediente.getIdTerritorio());
		expedienteTC.setLineaCredito(new Double(expediente.getLineaCredito()));
		expedienteTC.setFlagProvincia(expediente.getFlagProvincia());
		expedienteTC.setModificacionScoring(expediente.getModificacionScoring());
		expedienteTC.setMoneda(expediente.getMoneda());
		expedienteTC.setNombreUsuarioAnterior(expediente.getNombreUsuarioAnterior());
		expedienteTC.setNumeroContrato(expediente.getNumeroContrato());
		expedienteTC.setPerfilUsuarioAnterior(expediente.getPerfilUsuarioAnterior());
		expedienteTC.setProducto(expediente.getProducto());
		expedienteTC.setScoringAprobado(expediente.getScoringAprobado());
		expedienteTC.setSegmento(expediente.getSegmento());
		expedienteTC.setTipoOferta(expediente.getTipoOferta());
		expedienteTC.setVerificacionDomiciliaria(expediente.getVerificacionDomiciliaria());

		expedienteTC.setTaskID(elementoA.getAsJsonObject().get(NODE_TASKID).getAsString());
		expedienteTC.setNumeroDevoluciones(expediente.getNumeroDevoluciones()); //elementoA.getAsJsonObject().get(NODE_NRO_DEVOLUCIONES).getAsInt());


		String codigoTarea = (elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString() == ""?"0":elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
		expedienteTC.setIdTarea(codigoTarea);
		expedienteTC.setNombreNavegacionWeb("");
		expedienteTC.setCodigoUsuarioActual(elementoA.getAsJsonObject().get(NODE_USER).getAsString());
		expedienteTC.setFlagRetraer(expediente.getFlagRetraer());
		expedienteTC.setIdPerfilUsuarioAnterior(expediente.getIdPerfilUsuarioAnterior());
		expedienteTC.setFlagSubrogacion(expediente.getFlagSubrogacion());

		Calendar cal = Calendar.getInstance();

	    try { //2013-10-28T18:47:34Z
			TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
			SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
			readFormat.setTimeZone(utc);
			System.out.println("TIEMPO !!!!!"+ elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
	    	Date fechaStart = readFormat.parse(elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
			cal.setTime(fechaStart);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    expedienteTC.setActivado(cal);

	    System.out.println("********************** fromJSONtoExpedienteTC **********************");
	    System.out.println("Perfil usuario actual :::"+expedienteTC.getPerfilUsuarioActual());
	    System.out.println("codigo Actividad:::"+elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
	    System.out.println("tiempo de inicio:::"+elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
		System.out.println("task ID BPD:::"+expedienteTC.getTaskID());
	    System.out.println("expTC activado::::"+expedienteTC.getActivado().getTime());
	    System.out.println("    ");

		return expedienteTC;
	}
	*/
	
	public static ContentManager fromJSONtoContentManager (String strJSON) {
		Gson gson = new Gson();
		ContentManager contentManager = gson.fromJson(strJSON, ContentManager.class);
		return contentManager;
	}
	
	public static String fromContentManagerToJSON(ContentManager contentManager) {
		String strJSON = "";
		Gson gson = new Gson();
		strJSON = gson.toJson(contentManager);
		return strJSON;
	}
	
	public static ConsultaServicio fromConsultaCCToConsultaServicio(Consulta consulta) {
		ConsultaServicio consultaServicio = new ConsultaServicio();
		
		consultaServicio.setTipoConsulta(consulta.getTipoConsulta());
		
		if (consulta.getCodigoExpediente() != null) consultaServicio.setCodigo(consulta.getCodigoExpediente());

		if (consulta.getTipoDOI() != null) consultaServicio.setTipoDOI(consulta.getTipoDOI());

		if (consulta.getNumeroDOI() != null) consultaServicio.setNumeroDOI(consulta.getNumeroDOI());

		if (consulta.getIdProducto() != null) consultaServicio.setIdProducto(consulta.getIdProducto());

		if (consulta.getSubProducto() != null) consultaServicio.setSubProducto(consulta.getSubProducto());

		if (consulta.getSegmento() != null) consultaServicio.setSegmento(consulta.getSegmento());

		if (consulta.getTipoOferta() != null) consultaServicio.setTipoOferta(consulta.getTipoOferta());

		if (consulta.getIdTerritorio() != null) consultaServicio.setIdTerritorio(consulta.getIdTerritorio());

		if (consulta.getIdOficina() != null) consultaServicio.setIdOficina(consulta.getIdOficina());

		if (consulta.getEstado() != null) consultaServicio.setEstado(consulta.getEstado());

		if (consulta.getNumeroContrato() != null) consultaServicio.setNumeroContrato(consulta.getNumeroContrato());

		if (consulta.getCodRVGL() != null) consultaServicio.setCodigoRVGL(consulta.getCodRVGL());

		if (consulta.getCodPreEvaluador() != null) consultaServicio.setCodigoPreEvaluador(consulta.getCodPreEvaluador());

		if (consulta.getIdTarea() != null) consultaServicio.setIdTarea(consulta.getIdTarea());
		
		if (consulta.getIdPerfilUsuarioActual() != null) consultaServicio.setIdPerfil(consulta.getIdPerfilUsuarioActual());
		
		if (consulta.getCodUsuarioActual() != null) consultaServicio.setUsuario(consulta.getCodUsuarioActual());
		
		//Fecha de Asignacion
		Date fechaAsignacionInf = consulta.getFechaInicio();
		Date fechaAsignacionSup = null;
		if (consulta.getFechaFin()!=null) {
			Calendar fechaAsignacionSupC = Calendar.getInstance();				
			fechaAsignacionSupC.setTime(consulta.getFechaFin());
			fechaAsignacionSupC.add(Calendar.DAY_OF_MONTH, 1);
//			fechaAsignacionSupC.set(Calendar.HOUR_OF_DAY, 23);
//			fechaAsignacionSupC.set(Calendar.MINUTE, 59);
//			fechaAsignacionSupC.set(Calendar.SECOND, 59);
			fechaAsignacionSup = fechaAsignacionSupC.getTime();
		}
		consultaServicio.setFechaActivacionIni(fechaAsignacionInf);
		consultaServicio.setFechaActivacionFin(fechaAsignacionSup);
		
		System.out.println("TIPO CONSULTA ===> " + consultaServicio.getTipoConsulta());

		return consultaServicio;
	}

	public static String fromConsultaServicioToJSON(ConsultaServicio consultaServicio) {
		Gson gson = new GsonBuilder().setDateFormat(CONS_FORMATO_FECHA_1).serializeNulls().create();
		String strJSON = gson.toJson(consultaServicio);
		return strJSON;
	}
	
}
