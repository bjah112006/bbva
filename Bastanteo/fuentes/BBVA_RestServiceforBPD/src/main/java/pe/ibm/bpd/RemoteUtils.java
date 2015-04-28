package pe.ibm.bpd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.rest.RestBPM;
import pe.ibm.rest.RestBPMImpl;
import pe.ibm.util.Convertidor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RemoteUtils {
	
	private static final Logger log = LoggerFactory.getLogger(RemoteUtils.class);
	
	private RestBPM restBPM = new RestBPMImpl();
	private static final String BANDEJA_SEARCH_TEMPLATE_NAME = "BBVA_CC_BANDEJA";
	private static final String INICIO_CADENA_JSON = "{\"expediente\":";
	private static final String INICIO_CONSULTA_JSON = "{\"consulta\":";
	private static final String FINAL_CADENA_JSON = "}";
	private static final String NODE_DATA = "data";
	private static final String NODE_ITEMS = "items";
	private static final String NODE_TASKNAME = "name";
	private static final String NODE_TASKID_SEARCH_TEMPLATE = "TASK.TKIID";
	private static final String NODE_TASKID = "tkiid";
	private static final String NODE_VARIABLES = "variables";
	private static final String NODE_EXPEDIENTE = "expediente";
	private static final String NODE_RESPONSE = "response";

	public List<ExpedienteCC> obtenerInstanciasTareasPorUsuarioCC (ConsultaCC consulta){
		List<ExpedienteCC> taskList = new ArrayList<ExpedienteCC>();
		String tkiid = "";
		String cadenaUsuarios = null;
		
		if (consulta.isConsiderarUsuarios()) {  //traer por filtro de usuario(s)
			List<String> usuarios = consulta.getUsuarios();
			cadenaUsuarios = "";

			if (usuarios != null && usuarios.size() > 0) // considerar usuario de gestor y supervisor
			{
				log.info("USUARIOOOOS!!!!"+consulta.getUsuarios());

				StringBuilder sb = new StringBuilder ();
				boolean esPrimero = true;
				for (String usr : usuarios) {
					if (usr != null && !"".equals(usr)){
						if (!esPrimero) {
							sb.append(",");
						}
						sb.append("'").append(usr).append("'");
						esPrimero = false;
					}
				}

				cadenaUsuarios += sb.toString();
			}else{   // considerar usuario logueado
				if(consulta.getCodUsuarioActual() != null && !"".equals(consulta.getCodUsuarioActual()))
				{
					cadenaUsuarios = "'"+consulta.getCodUsuarioActual()+"'";
				}
			}

		}else{ // traer todo
			cadenaUsuarios = null;
		}	

		log.info("cadenaUsuarios:::"+cadenaUsuarios);
		
		if (consulta.isConsiderarUsuarios() && (cadenaUsuarios == null || cadenaUsuarios.trim().equals(""))) {
			log.info("El usuario supervisor o gestor no tiene empleados supervisados.");
			return taskList;
		}
		
		//String respuesta = restBPM.searchTemplate(BANDEJA_SEARCH_TEMPLATE_NAME,cadenaUsuarios);
		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaBBVA(consultaServicio);
		
		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get("resultado")
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();
		
		List<String> lstIdTask = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get("idTask").isJsonNull() ? items.get(i).getAsJsonObject().get("idTask").getAsString() : null;
			String asignedUser = !items.get(i).getAsJsonObject().get("asignedUser").isJsonNull() ? items.get(i).getAsJsonObject().get("asignedUser").getAsString() : null;
			String razonSocial = !items.get(i).getAsJsonObject().get("razonSocial").isJsonNull() ? items.get(i).getAsJsonObject().get("razonSocial").getAsString() : null;
			//tkiid = items.get(i).getAsString();
			//log.info("tkiid:::"+tkiid);
			if (cadenaUsuarios != null && !cadenaUsuarios.trim().equals("")) {
				if (cadenaUsuarios.indexOf(asignedUser) > -1) {
					lstIdTask.add(tkiid);
				}
			} else {
				String razonSocialClienteFilter = consulta.getRazonSocialCliente();
				if (razonSocialClienteFilter != null && razonSocial != null && razonSocial.toUpperCase().indexOf(razonSocialClienteFilter.toUpperCase()) > -1) {
					lstIdTask.add(tkiid);
				} else if (razonSocialClienteFilter == null) {
					lstIdTask.add(tkiid);
				}
			}
		}
		if (lstIdTask.size() == 0) {
			return taskList;
		} else {
			log.info("lstIdTask.size():"+lstIdTask.size());
		}
		
		respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
		jsonElement = new JsonParser().parse(respuesta);
		items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
		
		for (int i = 0; i < items.size(); i++) {
			 
			respuesta = items.get(i).toString();
				
			jsonElement = new JsonParser().parse(respuesta);
			jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
			
			tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
			log.info("tkiid:::"+tkiid);
			try{
			respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
					.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
			}catch(Exception ex){
				log.warn("No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
						+ ex.getMessage());
				continue;
			}
			
			ExpedienteCC expedienteCC = Convertidor.fromJSONtoExpedienteCC(respuesta,jsonElement);

//			//Numero de Expediente
//			String codigoExpFilter = consulta.getCodigoExpediente();
//			String codigoExp = expedienteCC.getCodigoExpediente();
//			if (codigoExpFilter != null && (codigoExp == null || !codigoExpFilter.equalsIgnoreCase(codigoExp))) continue;
//
//			//Estado de Expediente
//			String estadoExpFilter = consulta.getIdEstadoExpediente();
//			String estadoExp = expedienteCC.getIdEstadoExpediente();
//			if (estadoExpFilter != null && (estadoExp == null || !estadoExpFilter.equalsIgnoreCase(estadoExp))) continue;
//			
			//Numero de Tarea
			String numeroTareaFilter = consulta.getNumeroTarea();
			String numeroTarea = expedienteCC.getDatosFlujoCtaCte().getIdTarea();
			if (numeroTareaFilter != null && (numeroTarea == null || !numeroTareaFilter.equalsIgnoreCase(numeroTarea))) continue;
			
//			//Estado Tarea
//			String estadoTareaFilter = consulta.getIdEstadoTarea();
//			String estadoTarea = expedienteCC.getIdEstadoTarea();
//			if (estadoTareaFilter != null && (estadoTarea == null || !estadoTareaFilter.equalsIgnoreCase(estadoTarea))) continue;
//			
			//Nombre Tarea
			String nombreTareaFilter = consulta.getNombreTarea();
			String nombreTarea = expedienteCC.getDatosFlujoCtaCte().getIdTarea();
			if (nombreTareaFilter != null && (nombreTarea == null || !nombreTareaFilter.equalsIgnoreCase(nombreTarea))) continue;
			
//			//Codigo Central
//			String codCentralClienteFilter = consulta.getCodCentralCliente();
//			String codCentralCliente = expedienteCC.getCodCentralCliente();
//			if (codCentralClienteFilter != null && (codCentralCliente == null || !codCentralClienteFilter.equalsIgnoreCase(codCentralCliente))) continue;
//			
//			//Codigo Operacion
//			String codOperacionFilter = consulta.getIdOperacion();
//			String codOperacion = expedienteCC.getIdOperacion();
//			if (codOperacionFilter != null && (codOperacion == null || !codOperacionFilter.equalsIgnoreCase(codOperacion))) continue;
//			
//			//Numero DOI
//			String numeroDOIFilter = consulta.getNumDOICliente();
//			String numeroDOI = expedienteCC.getNumDOICliente();
//			if (numeroDOIFilter != null && (numeroDOI == null || !numeroDOIFilter.equalsIgnoreCase(numeroDOI))) continue;
//
//			//Razon Social
//			String razonSocialClienteFilter = consulta.getRazonSocialCliente();		
//			String razonSocialCliente = expedienteCC.getRazonSocialCliente();
//			if (razonSocialClienteFilter != null && (razonSocialCliente == null || razonSocialCliente.toUpperCase().indexOf(razonSocialClienteFilter.toUpperCase())<0)) continue;
//			
//			//Responsable
//			String codUsuarioActualFilter = consulta.getCodUsuarioActual();
//			String codUsuarioActual = expedienteCC.getCodUsuarioActual();				
//			if (codUsuarioActualFilter != null && (codUsuarioActual == null || !codUsuarioActualFilter.equalsIgnoreCase(codUsuarioActual))) continue;
//
//			//OficinaExpediente
//			String oficinaFilter = consulta.getCodOficina();
//			String oficinaActual = expedienteCC.getCodOficina();
//			if (oficinaFilter != null && (oficinaActual == null || !oficinaFilter.equalsIgnoreCase(oficinaActual))) continue;
//
//			//TerritorioExpediente
//			String territorioFilter = consulta.getIdTerritorio();
//			String territorioActual = expedienteCC.getDatosFlujoCtaCte().getIdTerritorio();
//			if (territorioFilter != null && (territorioActual == null || !territorioFilter.equalsIgnoreCase(territorioActual))) continue;
//			
//			//OficinaTarea
//			String oficinaTareaFilter = consulta.getCodOficinaTarea();
//			String oficinaTareaActual = expedienteCC.getCodOficina();
//			if (oficinaTareaFilter != null && (oficinaTareaActual == null || !oficinaTareaFilter.equalsIgnoreCase(oficinaTareaActual))) continue;
//
//			//TerritorioTarea
//			String territorioTareaFilter = consulta.getIdTerritorioTarea();
//			String territorioTareaActual = expedienteCC.getDatosFlujoCtaCte().getIdTerritorio();
//			if (territorioTareaFilter != null && (territorioTareaActual == null || !territorioTareaFilter.equalsIgnoreCase(territorioTareaActual))) continue;
//			
//			//Abogado
//			String abogadoFilter = consulta.getCodUsuarioAbogado();
//			String abogadoActual = expedienteCC.getDatosFlujoCtaCte().getCodUsuarioAbogado();
//			if (abogadoFilter != null && (abogadoActual == null || !abogadoFilter.equalsIgnoreCase(abogadoActual))) continue;
//							
//			//Gestor
//			String codUsuarioResponsableFilter = consulta.getCodUsuarioResponsable();
//			String codUsuarioResponsable = expedienteCC.getDatosFlujoCtaCte().getCodUsuarioResponsable();
//			if (codUsuarioResponsableFilter != null && (codUsuarioResponsable == null || !codUsuarioResponsableFilter.equalsIgnoreCase(codUsuarioResponsable))) continue;
//
//			//Estudio
//			String estudioFilter = consulta.getCodEstudioAbogado();
//			String estudioActual = expedienteCC.getDatosFlujoCtaCte().getCodEstudioAbogado();
//			if (estudioFilter != null && (estudioActual == null || !estudioFilter.equalsIgnoreCase(estudioActual))) continue;
//
//			
//			//Fecha de Asignacion
//			Date fechaAsignacionInf = consulta.getFechaAsignacionInf();
//			Date fechaAsignacionSup = null;
//			Calendar fecha = Calendar.getInstance();
//			if (consulta.getFechaAsignacionSup()!=null) {
//				Calendar fechaAsignacionSupC = Calendar.getInstance();				
//				fechaAsignacionSupC.setTime(consulta.getFechaAsignacionSup());
//				fechaAsignacionSupC.set(Calendar.HOUR_OF_DAY, 23);
//				fechaAsignacionSupC.set(Calendar.MINUTE, 59);
//				fechaAsignacionSupC.set(Calendar.SECOND, 59);
//				fechaAsignacionSup = fechaAsignacionSupC.getTime();
//			}
//			Date fechaActivado = null;
//			fecha = expedienteCC.getActivado();
//			fechaActivado = fecha.getTime();
//			
//			
//			if (fechaActivado == null || 
//					(fechaAsignacionInf!=null && fechaActivado.before(fechaAsignacionInf))
//					|| (fechaAsignacionSup!=null && fechaActivado.after(fechaAsignacionSup))) continue;
//			
//			//Fecha de Registro
//			Date fechaRegistroInf = consulta.getFechaInicioInf();
//			Date fechaRegistroSup = null;		
//			if (consulta.getFechaInicioSup()!=null) {
//				Calendar fechaInicioSupC = Calendar.getInstance();				
//				fechaInicioSupC.setTime(consulta.getFechaInicioSup());
//				fechaInicioSupC.set(Calendar.HOUR_OF_DAY, 23);
//				fechaInicioSupC.set(Calendar.MINUTE, 59);
//				fechaInicioSupC.set(Calendar.SECOND, 59);
//				fechaRegistroSup = fechaInicioSupC.getTime();
//			}
//
//			Date fechaRegistro = null;
//			fechaRegistro =  expedienteCC.getFechaRegistro();	
//			
//			if (fechaRegistro == null || 
//					(fechaRegistroInf!=null && fechaRegistro.before(fechaRegistroInf))
//					|| (fechaRegistroSup!=null && fechaRegistro.after(fechaRegistroSup))) continue;
		
			expedienteCC.setTaskID(tkiid);
			
			expedienteCC.getDatosFlujoCtaCte().setNombreTarea(jsonElement.getAsJsonObject().get(NODE_TASKNAME).getAsString());
			expedienteCC.setFechaServidorP(new Date());

			taskList.add(expedienteCC);
		}
		
		return taskList;
	
	}
	
	/***
	 * Inicia una instancia del proceso de Cta Cte
	 * @param nombreInstanciaProceso
	 * @param expedienteCC
	 */
	public String iniciarInstanciaProceso (String nombreInstanciaProceso, ExpedienteCC expediente){ //ajustar lo q devuelve
		try {
			String strJSON=	Convertidor.fromExpedienteCCtoJSON(expediente);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;		
			log.info("iniciarInstanciaProcesoCC strJSON:::"+strJSON);
			//return restBPM.crearProcessInstance(strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
			String piid = restBPM.crearProcessInstance(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			log.info("piid::::"+piid);
			return piid;
		} catch (Exception e) {
			log.error("Error en iniciarInstanciaProceso", e);
			throw new RuntimeException(e);
		}
	}
	
	/***
	 * Actualiza los datos del BO una tarea
	 * @param tkiid
	 * @param expedienteCC
	 */
	public void enviarExpediente (String tkiid, ExpedienteCC expediente){ 
		try{
			String strJSON=	Convertidor.fromExpedienteCCtoJSON(expediente);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;		
			log.info("enviarExpedienteCC strJSON::::"+strJSON);
			restBPM.setDataTask(tkiid,java.net.URLEncoder.encode(strJSON, "UTF-8"));
		} catch (Exception e) {
			log.error("Error en enviarExpediente", e);
			throw new RuntimeException(e);
		}
	}
	
	
	/***
	 * Actualiza los datos del BO una tarea y completa la tarea
	 * @param tkiid
	 * @param expedienteCC
	 */
	public void completarTarea (String tkiid, ExpedienteCC expediente){
		try{
			String strJSON=	Convertidor.fromExpedienteCCtoJSON(expediente);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;
			
			//strJSON =  strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20");
			log.info("completarTarea strJSOOOON::::"+strJSON);
			
			restBPM.completeTask(tkiid, java.net.URLEncoder.encode(strJSON, "UTF-8"));
		} catch (Exception e) {
			log.error("Error en completarTarea", e);
			throw new RuntimeException(e);
		}
	}
	
	public void completarTarea (String tkiid){
		try{
			restBPM.completeTask(tkiid);
		} catch (Exception e) {
			log.error("Error en completarTarea", e);
			throw new RuntimeException(e);
		}
	}
	
	public void transferirTarea (String usuarioOrigen, String usuarioDestino, String tkiid) 
	{
		try{
			//String strJSON=	"{\"user\":\""+usuarioDestino+"\",\"taskId\":\""+tkiid+"\"}";
			//log.info("transferirTarea strJSON::::"+strJSON);
			//restBPM.reassignTask(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			restBPM.assignTask(tkiid,usuarioDestino);
		} catch (Exception e) {
			log.error("Error en transferirTarea", e);
			throw new RuntimeException(e);
		}
	}
	
	public void ejecutarOperacionEspera (String codigoExp) {
		try{
			//String strJSON=	Convertidor.fromContentManagerToJSON(contentManager);
			String strJSON= "{\"codigo\":\""+codigoExp+"\"}";
			log.info("strJSON::::"+strJSON);
			restBPM.completeWaitingOperation(java.net.URLEncoder.encode(strJSON, "UTF-8"));

		} catch (Exception e) {
			log.error("Error en ejecutarOperacionEspera", e);
			throw new RuntimeException(e);
		}
	}
	
	public Calendar obtenerTimestampServidorProcess() {
		try {
			
			Date fechaActualSistema = restBPM.getDateServer();
			log.info("fechaActualSistema :::"+fechaActualSistema);
			Calendar cal = Calendar.getInstance();
			cal.setTime(fechaActualSistema);
			return cal;
		} catch (Exception e) {
			log.error("Error en obtenerTimestampServidorProcess", e);
			throw new RuntimeException(e);
		}
	}
	
	public String getTaksDetails(String tkiid) {
		try {
			
			String cadena = restBPM.getTaksDetails(tkiid);
			log.info("cadena :::"+cadena);
			return cadena;
		} catch (Exception e) {
			log.error("Error en getTaksDetails", e);
			throw new RuntimeException(e);
		}
	}
	
	public String consultaBBVA (ConsultaServicio consultaServicio){ 
		try {
			String strJSON=	INICIO_CONSULTA_JSON+Convertidor.fromConsultaServicioToJSON(consultaServicio)+FINAL_CADENA_JSON;
			log.info("strJSON:::"+strJSON);
			String data = restBPM.getListTaskBBVA(strJSON);
			log.info("data::"+data);
			return data;
		} catch (Exception e) {
			log.error("Error en consultaBBVA", e);
			throw new RuntimeException(e);
		}
	}

}
