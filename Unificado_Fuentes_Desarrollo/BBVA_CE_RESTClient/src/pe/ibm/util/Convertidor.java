package pe.ibm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Cliente;
import pe.ibm.bean.ClienteWeb;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.ContentManager;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.Producto;
import pe.ibm.bean.ProductoWeb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Convertidor {
	
	private static final Logger LOG = LoggerFactory.getLogger(Convertidor.class);

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
	private static final String NODE_DESCRIPCION_ERRORUSU = "descripcionErrorUsu";
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
		jsonElement.getAsJsonObject().remove(NODE_DESCRIPCION_ERRORUSU);
		jsonElement.getAsJsonObject().remove(NODE_DESCRIPCION_TAREA_ANT); 
		jsonElement.getAsJsonObject().remove(NODE_CANT_DOCUMENTOS);
		jsonElement.getAsJsonObject().remove(NODE_FECHA_RESTAURACION);
		jsonElement.getAsJsonObject().remove(NODE_FECHA_CANCELACION);
		jsonElement.getAsJsonObject().remove(NODE_TIPO_ERROR);
		jsonElement.getAsJsonObject().remove(NODE_FECHA_INCIDENCIA);
		LOG.info("fromExpedienteTCtoJSON ::::"+jsonElement.toString());
		return jsonElement.toString();
	}
	
	public static ExpedienteTCWPS fromJSONtoExpedienteTC_Mantenimiento (String strJSON, JsonElement elementoA, String tipoDato) {
		JsonElement jsonElement = new JsonParser().parse(strJSON);
		String dato = jsonElement.getAsJsonObject().get(NODE_EXPEDIENTE).toString();
		
		ExpedienteTCWPS expedienteTCWPS = fromJSONtoExpedienteTC(dato,elementoA,"1");
		
		dato = jsonElement.getAsJsonObject().get(NODE_DESCRIPCION_ERROR) == null ? "" : jsonElement.getAsJsonObject().get(NODE_DESCRIPCION_ERROR).toString();
		expedienteTCWPS.setDescripcionError(dato);
		
		dato = jsonElement.getAsJsonObject().get(NODE_DESCRIPCION_ERRORUSU) == null ? "" : jsonElement.getAsJsonObject().get(NODE_DESCRIPCION_ERRORUSU).toString();
		expedienteTCWPS.setDescripcionErrorUsu(dato);		
		
		dato = jsonElement.getAsJsonObject().get(NODE_NRO_REINTENTO) == null ? "" : jsonElement.getAsJsonObject().get(NODE_NRO_REINTENTO).toString();
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
				LOG.info("TIEMPO !!!!!"+ elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
		    	Date fechaStart = readFormat.parse(elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
				cal.setTime(fechaStart);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
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

	    LOG.info("********************** fromJSONtoExpedienteTC **********************");

	    LOG.info("Codigo Exp :::"+expedienteTC.getCodigo());
	    LOG.info("expTC activado::::"+expedienteTC.getActivado().getTime());

	    if ("1".equals(tipoDato)){
		    LOG.info("Perfil usuario actual :::"+expedienteTC.getPerfilUsuarioActual());
		    LOG.info("codigo Actividad:::"+elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
		    LOG.info("tiempo de inicio:::"+elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
			LOG.info("task ID BPD:::"+expedienteTC.getTaskID());
			LOG.info("Codigo Usuario Actual:::"+expedienteTC.getCodigoUsuarioActual());
	    }
	    LOG.info("    ");

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
		LOG.info("salidaaaa!!!!!"+strJSON);
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
			LOG.info("TIEMPO !!!!!"+ elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
	    	Date fechaStart = readFormat.parse(elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
			cal.setTime(fechaStart);
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
		}

	    expedienteTC.setActivado(cal);

	    LOG.info("********************** fromJSONtoExpedienteTC **********************");
	    LOG.info("Perfil usuario actual :::"+expedienteTC.getPerfilUsuarioActual());
	    LOG.info("codigo Actividad:::"+elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
	    LOG.info("tiempo de inicio:::"+elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
		LOG.info("task ID BPD:::"+expedienteTC.getTaskID());
	    LOG.info("expTC activado::::"+expedienteTC.getActivado().getTime());
	    LOG.info("    ");

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
		
		if (consulta.getTipoConsulta() == 0 ){ // para B. pendientes
			List<String> listaUsuarios = consulta.getUsuarios();
			String cadenaUsuarios = "";
			for (String user : listaUsuarios) {
				cadenaUsuarios = cadenaUsuarios + "|" + user;
			}
			consultaServicio.setUsuario(cadenaUsuarios);
		}else if(consulta.getTipoConsulta() == 1){ //para B. busqueda
			if (consulta.getCodUsuarioActual() != null && consulta.getCodUsuarioActual() != "") consultaServicio.setUsuario("|"+consulta.getCodUsuarioActual());
		}
		
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
		
		LOG.info("TIPO CONSULTA ===> " + consultaServicio.getTipoConsulta());

		return consultaServicio;
	}

	public static Cliente fromClienteWebToCliente(ClienteWeb cli){
		Cliente objTemp=new Cliente();
		objTemp.setTipoDOI(cli.getTipoDOI());
		objTemp.setNumeroDOI(cli.getNumeroDOI());
		return objTemp;
	}
	
	public static Producto fromProductoWebToProducto(ProductoWeb pro){
		Producto objTemp=new Producto();
		objTemp.setIdProducto(pro.getIdProducto());
		return objTemp;
	}
	
	public static ExpedienteTCWPS fromExpedienteTCWPSWebToExpedienteTCWPS(ExpedienteTCWPSWeb obj) {
		ExpedienteTCWPS objTemp = new ExpedienteTCWPS();
		
			objTemp.setAccion(obj.getAccion());
			objTemp.setActivado(obj.getActivado());
			objTemp.setCantDocumentos(obj.getCantDocumentos());
			objTemp.setCliente(fromClienteWebToCliente(obj.getCliente()));
			objTemp.setCodigo(obj.getCodigo());
			objTemp.setCodigoEmpleadoResponsable(obj.getCodigoEmpleadoResponsable());
		//	objTemp.setCodigoPreEvaluador(obj.getCodigoPreEvaluador());
		//	objTemp.setCodigoRVGL(obj.getCodigoRVGL());
			objTemp.setCodigoUsuarioActual(obj.getCodigoUsuarioActual());
			objTemp.setCodigoUsuarioAnterior(obj.getCodigoUsuarioAnterior());
			objTemp.setDescripcionError(obj.getDescripcionError());
		//	objTemp.setDesOficina(obj.getDesOficina());
			objTemp.setDesTarea(obj.getDesTarea());
			objTemp.setDesTareaAnterior(obj.getDesTareaAnterior());
	//		objTemp.setDesTerritorio(obj.getDesTerritorio());
			objTemp.setDevueltoPor(obj.getDevueltoPor());
			objTemp.setEstado(obj.getEstado());
			objTemp.setEstadoAnterior(obj.getEstadoAnterior());
			objTemp.setFechaCancelacion(obj.getFechaCancelacion());
			objTemp.setFechaDocumento(obj.getFechaDocumento());
			objTemp.setFechaIncidencia(obj.getFechaIncidencia());
			objTemp.setFechaRestauracion(obj.getFechaRestauracion());
			objTemp.setFlagEnProcesoTimer(obj.getFlagEnProcesoTimer());
			objTemp.setFlagEnvioContent(obj.getFlagEnvioContent());
		//	objTemp.setFlagProvincia(obj.getFlagProvincia());
			objTemp.setFlagRetraer(obj.getFlagRetraer());
			objTemp.setFlagSubrogacion(obj.getFlagSubrogacion());
	//		objTemp.setIdGrupoSegmento(obj.getIdGrupoSegmento());
			objTemp.setIdOficina(obj.getIdOficina());
			objTemp.setIdPerfilUsuarioActual(obj.getIdPerfilUsuarioActual());
			objTemp.setIdPerfilUsuarioAnterior(obj.getIdPerfilUsuarioAnterior());
			objTemp.setIdTarea(obj.getIdTarea());
			objTemp.setIdTareaAnterior(obj.getIdTareaAnterior());
			objTemp.setIdTerritorio(obj.getIdTerritorio());
			objTemp.setIdTipoOferta(obj.getIdTipoOferta());
		//..	objTemp.setLineaCredito(obj.getLineaCredito()==null?0.0:obj.getLineaCredito());
			objTemp.setModificacionScoring(obj.getModificacionScoring());
	//	..	objTemp.setMoneda(obj.getMoneda());
	//..		objTemp.setMontoAprobado(obj.getMontoAprobado());
			objTemp.setNombreNavegacionWeb(obj.getNombreNavegacionWeb());
			objTemp.setNombreUsuarioActual(obj.getNombreUsuarioActual());
			objTemp.setNombreUsuarioAnterior(obj.getNombreUsuarioAnterior());
			objTemp.setNroReintentos(obj.getNroReintentos());
		//..	objTemp.setNumeroContrato(obj.getNumeroContrato());
	//..		objTemp.setNumeroDevoluciones(obj.getNumeroDevoluciones());
	//..		objTemp.setObservacion(obj.getObservacion());
			objTemp.setPerfilUsuarioActual(obj.getPerfilUsuarioActual());
			objTemp.setPerfilUsuarioAnterior(obj.getPerfilUsuarioAnterior());
			objTemp.setProducto(fromProductoWebToProducto(obj.getProducto()));
	//..		objTemp.setScoringAprobado(obj.getScoringAprobado());
	//..		objTemp.setSegmento(obj.getSegmento());
			objTemp.setTaskID(obj.getTaskID());
			objTemp.setTipoError(obj.getTipoError());
			objTemp.setTipoOferta(obj.getTipoOferta());
			objTemp.setVerificacionDomiciliaria(obj.getVerificacionDomiciliaria());
			
			return objTemp;
		}	
	
	public static boolean validarLong(String cad){
		Long valor;
		try{
			valor=new Long(cad);
			return true;
		}catch(NumberFormatException e){
			LOG.error(e.getMessage(), e);
			return false;
		}
	}	
	
	public static ClienteWeb fromClienteToClienteWeb(Cliente cli){
		ClienteWeb objTemp=new ClienteWeb();
		if(cli!=null){
		objTemp.setTipoDOI(cli.getTipoDOI());
		objTemp.setNumeroDOI(cli.getNumeroDOI());
		}
		return objTemp;
	}
	
	public static ProductoWeb fromProductoToProductoWeb(Producto pro){
		ProductoWeb objTemp=new ProductoWeb();
		if(pro!=null){
			objTemp.setIdProducto(pro.getIdProducto());
		}
		
		return objTemp;
	}
	public static ExpedienteTCWPSWeb fromObjExpedienteTCWPSToObjExpedienteTCWPSWeb(ExpedienteTCWPS obj, String opcion) {
			if(opcion.equals("1")){
				if(obj.getIdTarea()!=null && !obj.getIdTarea().equals("") && validarLong(obj.getIdTarea()))
					LOG.info("Existe tarea");
				else{
					LOG.info("isTarea es nula para exp "+obj.getCodigo());
					
				}
			}
			
			LOG.info("Pasando valores para exp:"+obj.getCodigo());
				ExpedienteTCWPSWeb objTemp=new ExpedienteTCWPSWeb();
				objTemp.setIdTarea(obj.getIdTarea());
				objTemp.setAccion(obj.getAccion());
				objTemp.setActivado(obj.getActivado());
				objTemp.setCantDocumentos(obj.getCantDocumentos());
				objTemp.setCliente(fromClienteToClienteWeb(obj.getCliente()));
				objTemp.setCodigo(obj.getCodigo());
				objTemp.setCodigoEmpleadoResponsable(obj.getCodigoEmpleadoResponsable());
			//..	objTemp.setCodigoPreEvaluador(obj.getCodigoPreEvaluador());
		//..		objTemp.setCodigoRVGL(obj.getCodigoRVGL());
				objTemp.setCodigoUsuarioActual(obj.getCodigoUsuarioActual());
				objTemp.setCodigoUsuarioAnterior(obj.getCodigoUsuarioAnterior());
				objTemp.setDescripcionError(obj.getDescripcionError());
		//..		objTemp.setDesOficina(obj.getDesOficina());
				objTemp.setDesTarea(obj.getDesTarea());
				objTemp.setDesTareaAnterior(obj.getDesTareaAnterior());
		//..		objTemp.setDesTerritorio(obj.getDesTerritorio());
				objTemp.setDevueltoPor(obj.getDevueltoPor());
				objTemp.setEstado(obj.getEstado());
				objTemp.setEstadoAnterior(obj.getEstadoAnterior());
				objTemp.setFechaCancelacion(obj.getFechaCancelacion());
				objTemp.setFechaDocumento(obj.getFechaDocumento());
				objTemp.setFechaIncidencia(obj.getFechaIncidencia());
				objTemp.setFechaRestauracion(obj.getFechaRestauracion());
				objTemp.setFlagEnProcesoTimer(obj.getFlagEnProcesoTimer());
				objTemp.setFlagEnvioContent(obj.getFlagEnvioContent());
			//..	objTemp.setFlagProvincia(obj.getFlagProvincia());
				objTemp.setFlagRetraer(obj.getFlagRetraer());
				objTemp.setFlagSubrogacion(obj.getFlagSubrogacion());
			//..	objTemp.setIdGrupoSegmento(obj.getIdGrupoSegmento());
				objTemp.setIdOficina(obj.getIdOficina());
				objTemp.setIdPerfilUsuarioActual(obj.getIdPerfilUsuarioActual());
				objTemp.setIdPerfilUsuarioAnterior(obj.getIdPerfilUsuarioAnterior());
				objTemp.setIdTareaAnterior(obj.getIdTareaAnterior());
				objTemp.setIdTerritorio(obj.getIdTerritorio());
				objTemp.setIdTipoOferta(obj.getIdTipoOferta());
		//..		objTemp.setLineaCredito(obj.getLineaCredito());
				objTemp.setModificacionScoring(obj.getModificacionScoring());
		//..		objTemp.setMoneda(obj.getMoneda());
		//..		objTemp.setMontoAprobado(obj.getMontoAprobado());
				objTemp.setNombreNavegacionWeb(obj.getNombreNavegacionWeb());
				objTemp.setNombreUsuarioActual(obj.getNombreUsuarioActual());
				objTemp.setNombreUsuarioAnterior(obj.getNombreUsuarioAnterior());
				objTemp.setNroReintentos(obj.getNroReintentos());
		//..		objTemp.setNumeroContrato(obj.getNumeroContrato());
		//..		objTemp.setNumeroDevoluciones(obj.getNumeroDevoluciones());
		//..		objTemp.setObservacion(obj.getObservacion());
				objTemp.setPerfilUsuarioActual(obj.getPerfilUsuarioActual());
				objTemp.setPerfilUsuarioAnterior(obj.getPerfilUsuarioAnterior());
				objTemp.setProducto(fromProductoToProductoWeb(obj.getProducto()));
		//..		objTemp.setScoringAprobado(obj.getScoringAprobado());
			//..	objTemp.setSegmento(obj.getSegmento());
				objTemp.setTaskID(obj.getTaskID());
				objTemp.setTipoError(obj.getTipoError());
				objTemp.setTipoOferta(obj.getTipoOferta());
				objTemp.setVerificacionDomiciliaria(obj.getVerificacionDomiciliaria());	
				

	
		return objTemp;
	}
	
	public static List<ExpedienteTCWPSWeb> fromExpedienteTCWPSToExpedienteTCWPSWeb(List<ExpedienteTCWPS> listProcess, String opcion) {
		List<ExpedienteTCWPSWeb> listWeb = new ArrayList<ExpedienteTCWPSWeb>();
		
		for(ExpedienteTCWPS obj: listProcess){
			
			if(opcion.equals("1")){
				if(obj.getIdTarea()!=null && !obj.getIdTarea().equals("") && validarLong(obj.getIdTarea()))
					LOG.info("Existe tarea");
				else{
					LOG.info("isTarea es nula para exp "+obj.getCodigo());
					continue;
				}
			}
			
			LOG.info("Pasando valores para exp:"+obj.getCodigo());
				ExpedienteTCWPSWeb objTemp=new ExpedienteTCWPSWeb();
				objTemp.setIdTarea(obj.getIdTarea());
				objTemp.setAccion(obj.getAccion());
				objTemp.setActivado(obj.getActivado());
				objTemp.setCantDocumentos(obj.getCantDocumentos());
				objTemp.setCliente(fromClienteToClienteWeb(obj.getCliente()));
				objTemp.setCodigo(obj.getCodigo());
				objTemp.setCodigoEmpleadoResponsable(obj.getCodigoEmpleadoResponsable());
			//..	objTemp.setCodigoPreEvaluador(obj.getCodigoPreEvaluador());
		//..		objTemp.setCodigoRVGL(obj.getCodigoRVGL());
				objTemp.setCodigoUsuarioActual(obj.getCodigoUsuarioActual());
				objTemp.setCodigoUsuarioAnterior(obj.getCodigoUsuarioAnterior());
				objTemp.setDescripcionError(obj.getDescripcionError());
		//..		objTemp.setDesOficina(obj.getDesOficina());
				objTemp.setDesTarea(obj.getDesTarea());
				objTemp.setDesTareaAnterior(obj.getDesTareaAnterior());
		//..		objTemp.setDesTerritorio(obj.getDesTerritorio());
				objTemp.setDevueltoPor(obj.getDevueltoPor());
				objTemp.setEstado(obj.getEstado());
				objTemp.setEstadoAnterior(obj.getEstadoAnterior());
				objTemp.setFechaCancelacion(obj.getFechaCancelacion());
				objTemp.setFechaDocumento(obj.getFechaDocumento());
				objTemp.setFechaIncidencia(obj.getFechaIncidencia());
				objTemp.setFechaRestauracion(obj.getFechaRestauracion());
				objTemp.setFlagEnProcesoTimer(obj.getFlagEnProcesoTimer());
				objTemp.setFlagEnvioContent(obj.getFlagEnvioContent());
			//..	objTemp.setFlagProvincia(obj.getFlagProvincia());
				objTemp.setFlagRetraer(obj.getFlagRetraer());
				objTemp.setFlagSubrogacion(obj.getFlagSubrogacion());
			//..	objTemp.setIdGrupoSegmento(obj.getIdGrupoSegmento());
				objTemp.setIdOficina(obj.getIdOficina());
				objTemp.setIdPerfilUsuarioActual(obj.getIdPerfilUsuarioActual());
				objTemp.setIdPerfilUsuarioAnterior(obj.getIdPerfilUsuarioAnterior());
				objTemp.setIdTareaAnterior(obj.getIdTareaAnterior());
				objTemp.setIdTerritorio(obj.getIdTerritorio());
				objTemp.setIdTipoOferta(obj.getIdTipoOferta());
		//..		objTemp.setLineaCredito(obj.getLineaCredito());
				objTemp.setModificacionScoring(obj.getModificacionScoring());
		//..		objTemp.setMoneda(obj.getMoneda());
		//..		objTemp.setMontoAprobado(obj.getMontoAprobado());
				objTemp.setNombreNavegacionWeb(obj.getNombreNavegacionWeb());
				objTemp.setNombreUsuarioActual(obj.getNombreUsuarioActual());
				objTemp.setNombreUsuarioAnterior(obj.getNombreUsuarioAnterior());
				objTemp.setNroReintentos(obj.getNroReintentos());
		//..		objTemp.setNumeroContrato(obj.getNumeroContrato());
		//..		objTemp.setNumeroDevoluciones(obj.getNumeroDevoluciones());
		//..		objTemp.setObservacion(obj.getObservacion());
				objTemp.setPerfilUsuarioActual(obj.getPerfilUsuarioActual());
				objTemp.setPerfilUsuarioAnterior(obj.getPerfilUsuarioAnterior());
				objTemp.setProducto(fromProductoToProductoWeb(obj.getProducto()));
		//..		objTemp.setScoringAprobado(obj.getScoringAprobado());
			//..	objTemp.setSegmento(obj.getSegmento());
				objTemp.setTaskID(obj.getTaskID());
				objTemp.setTipoError(obj.getTipoError());
				objTemp.setTipoOferta(obj.getTipoOferta());
				objTemp.setVerificacionDomiciliaria(obj.getVerificacionDomiciliaria());	
				
				listWeb.add(objTemp);
				

			
		}

		return listWeb;
	}
	

	public static String fromConsultaServicioToJSON(ConsultaServicio consultaServicio) {
		Gson gson = new GsonBuilder().setDateFormat(CONS_FORMATO_FECHA_1).serializeNulls().create();
		String strJSON = gson.toJson(consultaServicio);
		return strJSON;
	}
	
}
