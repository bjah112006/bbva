package pe.ibm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.ContentManager;
import pe.ibm.bean.ExpedienteCC;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Convertidor {
	
	private static final Logger log = LoggerFactory.getLogger(Convertidor.class);
	
	private static final String NODE_TASKID = "tkiid";
	private static final String NODE_DESCRIPCION = "description";
	private static final String NODE_START_TIME = "startTime";
	private static final String CONS_FORMATO_FECHA_1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String CONS_FORMATO_TIME_ZONE = "GMT-5:00:00";
	private static final String NODE_EXP_FECHA_REGISTRO = "fechaRegistro";
	private static final String NODE_EXP_FECHA_ULTIMO_BASTANTEO = "fechaUltimoBastanteo";
	private static final String NODE_EXP_FECHA_SERVIDOR_P = "fechaServidorP";

	private static final String NODE_EXP_TASK_ID = "taskID";
	private static final String NODE_EXP_ACTIVADO = "activado";
	private static final String NODE_DATOSFLUJO_NOMBRE_TAREA = "nombreTarea";
	private static final String NODE_EXP_COD_SEMAFORO = "codSemaforo";
	private static final String NODE_DATOSFLUJO = "datosFlujoCtaCte";


	public static String fromExpedienteCCtoJSON (ExpedienteCC expediente) {


		Gson gson = new Gson();
		String strJSON = gson.toJson(expediente);
//		System.out.println("salidaaaa!!!!!"+strJSON);

        JsonElement jsonElement = new JsonParser().parse(strJSON);
		jsonElement.getAsJsonObject().remove(NODE_EXP_FECHA_REGISTRO);
		jsonElement.getAsJsonObject().remove(NODE_EXP_FECHA_ULTIMO_BASTANTEO);
		jsonElement.getAsJsonObject().remove(NODE_EXP_FECHA_SERVIDOR_P);

		jsonElement.getAsJsonObject().remove(NODE_EXP_TASK_ID);
		jsonElement.getAsJsonObject().remove(NODE_EXP_ACTIVADO);
		jsonElement.getAsJsonObject().get(NODE_DATOSFLUJO).getAsJsonObject().remove(NODE_DATOSFLUJO_NOMBRE_TAREA);
		jsonElement.getAsJsonObject().remove(NODE_EXP_COD_SEMAFORO);


		//Cambiando el formato de las fechas que el metodo de REST API recibirá (solo acepta  ISO-8601 standard YYYY-MM-DDTHH:mm:ss.sssZ)
		SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
		String strFechaModificada = "";

		if(expediente.getFechaRegistro() !=null){
			strFechaModificada = readFormat.format(expediente.getFechaRegistro());
			jsonElement.getAsJsonObject().addProperty(NODE_EXP_FECHA_REGISTRO, strFechaModificada);
		}

		if(expediente.getFechaUltimoBastanteo()!=null){
			strFechaModificada = readFormat.format(expediente.getFechaUltimoBastanteo());
			jsonElement.getAsJsonObject().addProperty(NODE_EXP_FECHA_ULTIMO_BASTANTEO, strFechaModificada);
	//		System.out.println("salidaaaa!!!!!"+strJSON);
		}


		return jsonElement.toString();
	}
	
	public static ExpedienteCC fromJSONtoExpedienteCC (String strJSON, JsonElement elementoA) {
		Gson gson = new Gson();
		ExpedienteCC expediente = gson.fromJson(strJSON, ExpedienteCC.class);

		expediente.setTaskID(elementoA.getAsJsonObject().get(NODE_TASKID).getAsString());
		
		String codigoTarea = (elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString() == ""?"0":elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
		expediente.getDatosFlujoCtaCte().setIdTarea(codigoTarea);
		
		Calendar cal = parseJSONDate(elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
		
	    expediente.setActivado(cal);
	    
	    log.info("********************** fromJSONtoexpedienteCCWPS **********************");
	    log.info("datos/idTare:::"+expediente.getDatosFlujoCtaCte().getIdTarea());
	    log.info("codigo Actividad:::"+elementoA.getAsJsonObject().get(NODE_DESCRIPCION).getAsString());
	    log.info("tiempo de inicio:::"+elementoA.getAsJsonObject().get(NODE_START_TIME).getAsString());
	    log.info("task ID BPD:::"+expediente.getTaskID());
	    log.info("expTC activado::::"+expediente.getActivado().getTime());
	    log.info("    ");
		
		return expediente;
	}
	
	public static Calendar parseJSONDate(String strDate) {
		Calendar cal = Calendar.getInstance();

	    try { //2013-10-28T18:47:34Z
			TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
			SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
			readFormat.setTimeZone(utc);
	    	Date fechaStart = readFormat.parse(strDate);
			cal.setTime(fechaStart);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
	    return cal;
	}
	
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
	
	public static ConsultaServicio fromConsultaCCToConsultaServicio(ConsultaCC consulta) {
		ConsultaServicio consultaServicio = new ConsultaServicio();
		//Numero de Expediente
		String codigoExpFilter = consulta.getCodigoExpediente();
		if (codigoExpFilter != null) consultaServicio.setCodigoExpediente(codigoExpFilter);
		
		//Numero de Tarea
		String numeroTareaFilter = consulta.getNumeroTarea();
		if (numeroTareaFilter != null) consultaServicio.setIdTarea(numeroTareaFilter);
		
		//Nombre Tarea
		String nombreTareaFilter = consulta.getNombreTarea();
		if (nombreTareaFilter != null) consultaServicio.setIdTarea(nombreTareaFilter);
		
		//Codigo Central
		String codCentralClienteFilter = consulta.getCodCentralCliente();
		if (codCentralClienteFilter != null) consultaServicio.setCodCentralCliente(codCentralClienteFilter);
		
		//Codigo Operacion
		String codOperacionFilter = consulta.getIdOperacion();
		if (codOperacionFilter != null) consultaServicio.setIdOperacion(codOperacionFilter);
		
		//Numero DOI
		String numeroDOIFilter = consulta.getNumDOICliente();
		if (numeroDOIFilter != null) consultaServicio.setNumDOICliente(numeroDOIFilter);

		//Razon Social
		String razonSocialClienteFilter = consulta.getRazonSocialCliente();		
		if (razonSocialClienteFilter != null) consultaServicio.setRazonSocialCliente(razonSocialClienteFilter);
		
		//Responsable
		String codUsuarioActualFilter = consulta.getCodUsuarioActual();
		if (codUsuarioActualFilter != null) consultaServicio.setCodUsuarioActual(codUsuarioActualFilter);

		//OficinaExpediente
		String oficinaFilter = consulta.getCodOficina();
		if (oficinaFilter != null) consultaServicio.setCodOficina(oficinaFilter);

		//TerritorioExpediente
		String territorioFilter = consulta.getIdTerritorio();
		if (territorioFilter != null) consultaServicio.setIdTerritorio(territorioFilter);
		
		//OficinaTarea
		String oficinaTareaFilter = consulta.getCodOficinaTarea();
		if (oficinaTareaFilter != null) consultaServicio.setCodOficina(oficinaTareaFilter);

		//TerritorioTarea
		String territorioTareaFilter = consulta.getIdTerritorioTarea();
		if (territorioTareaFilter != null) consultaServicio.setIdTerritorio(territorioTareaFilter);
		
		//Abogado
		String abogadoFilter = consulta.getCodUsuarioAbogado();
		if (abogadoFilter != null) consultaServicio.setCodUsuarioAbogado(abogadoFilter);
						
		//Gestor
		String codUsuarioResponsableFilter = consulta.getCodUsuarioResponsable();
		if (codUsuarioResponsableFilter != null) consultaServicio.setCodUsuarioResponsable(codUsuarioResponsableFilter);

		//Estudio
		String estudioFilter = consulta.getCodEstudioAbogado();
		if (estudioFilter != null) consultaServicio.setCodEstudioAbogado(estudioFilter);

		
		//Fecha de Asignacion
		Date fechaAsignacionInf = consulta.getFechaAsignacionInf();
		Date fechaAsignacionSup = null;
		Calendar fecha = Calendar.getInstance();
		if (consulta.getFechaAsignacionSup()!=null) {
			Calendar fechaAsignacionSupC = Calendar.getInstance();				
			fechaAsignacionSupC.setTime(consulta.getFechaAsignacionSup());
			fechaAsignacionSupC.add(Calendar.DAY_OF_MONTH, 1);
//			fechaAsignacionSupC.set(Calendar.HOUR_OF_DAY, 23);
//			fechaAsignacionSupC.set(Calendar.MINUTE, 59);
//			fechaAsignacionSupC.set(Calendar.SECOND, 59);
			fechaAsignacionSup = fechaAsignacionSupC.getTime();
		}
		consultaServicio.setFechaActivacionIni(fechaAsignacionInf);
		consultaServicio.setFechaActivacionFin(fechaAsignacionSup);
		
		//Fecha de Registro
		Date fechaRegistroInf = consulta.getFechaInicioInf();
		Date fechaRegistroSup = null;		
		if (consulta.getFechaInicioSup()!=null) {
			Calendar fechaInicioSupC = Calendar.getInstance();				
			fechaInicioSupC.setTime(consulta.getFechaInicioSup());
			fechaInicioSupC.add(Calendar.DAY_OF_MONTH, 1);
//			fechaInicioSupC.set(Calendar.HOUR_OF_DAY, 23);
//			fechaInicioSupC.set(Calendar.MINUTE, 59);
//			fechaInicioSupC.set(Calendar.SECOND, 59);
			fechaRegistroSup = fechaInicioSupC.getTime();
		}
		consultaServicio.setFechaRegistroIni(fechaRegistroInf);
		consultaServicio.setFechaRegistroFin(fechaRegistroSup);
		
		return consultaServicio;
	}
	
	public static String fromConsultaServicioToJSON(ConsultaServicio consultaServicio) {
		Gson gson = new GsonBuilder().setDateFormat(CONS_FORMATO_FECHA_1).serializeNulls().create();
		String strJSON = gson.toJson(consultaServicio);
		return strJSON;
	}
	
}
