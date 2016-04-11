package pe.ibm.bpd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.DatosFlujoCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bean.OperacionesCC;
import pe.ibm.rest.RestBPM;
import pe.ibm.rest.RestBPMImpl;
import pe.ibm.util.Convertidor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;

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
		Calendar c1 = Calendar.getInstance();
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
		
		ExpedienteDAO expedienteDAO = EJBLocator.getExpedienteDAO();
		TareaDAO tareaDAO = EJBLocator.getTareaDAO();
		EmpleadoDAO empleadoDAO = EJBLocator.getEmpleadoDAO();
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get("idTask").isJsonNull() ? items.get(i).getAsJsonObject().get("idTask").getAsString() : null;
			String asignedUser = !items.get(i).getAsJsonObject().get("asignedUser").isJsonNull() ? items.get(i).getAsJsonObject().get("asignedUser").getAsString() : null;
			String razonSocial = !items.get(i).getAsJsonObject().get("razonSocial").isJsonNull() ? items.get(i).getAsJsonObject().get("razonSocial").getAsString() : null;
			String numeroTarea = !items.get(i).getAsJsonObject().get("idTarea").isJsonNull() ? items.get(i).getAsJsonObject().get("idTarea").getAsString() : null;
			if (numeroTarea.equals("Step: Gestionar Cobro Comision")) {
				numeroTarea = String.valueOf(ConstantesBusiness.ID_TAREA_GESTIONAR_COBRO_COMISION);
			}
			String idExpediente = !items.get(i).getAsJsonObject().get("codExpediente").isJsonNull() ? items.get(i).getAsJsonObject().get("codExpediente").getAsString() : null;
			Calendar fechaAsignacion = Convertidor.parseJSONDate(items.get(i).getAsJsonObject().get("fechaActivacion").getAsString());
			Calendar fechaRegistro = Convertidor.parseJSONDate(items.get(i).getAsJsonObject().get("fechaRegistro").getAsString());
			log.info("tkiid:::"+tkiid);
			log.info("idExpediente:::"+idExpediente);
			if (cadenaUsuarios != null && !cadenaUsuarios.trim().equals("")) {
				if (!(cadenaUsuarios.indexOf(asignedUser) > -1)) continue;
			} else {
				//Razon Social
				String razonSocialClienteFilter = consulta.getRazonSocialCliente();
				if (razonSocialClienteFilter != null && (razonSocial == null || !(razonSocial.toUpperCase().indexOf(razonSocialClienteFilter.toUpperCase()) > -1))) continue;
				
				//Numero de Tarea
				String numeroTareaFilter = consulta.getNumeroTarea();
				if (numeroTareaFilter != null && (numeroTarea == null || !numeroTareaFilter.equalsIgnoreCase(numeroTarea))) continue;
				
				//Nombre Tarea
				String nombreTareaFilter = consulta.getNombreTarea();
				if (nombreTareaFilter != null && (numeroTarea == null || !nombreTareaFilter.equalsIgnoreCase(numeroTarea))) continue;
			}
			// Si cumple con los filtros
			Expediente expediente = expedienteDAO.load(Integer.parseInt(idExpediente));
			if (expediente != null) {
				Tarea tarea = tareaDAO.load(Integer.parseInt(numeroTarea));
				if (tarea != null) {
					ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
					expedienteTarea.setTarea(tarea);
				}
				ExpedienteCC expedienteCC = new ExpedienteCC();
				copiarDatos(expediente, expedienteCC);
				expedienteCC.setTaskID(tkiid);
				if (tarea != null) {
					expedienteCC.getDatosFlujoCtaCte().setIdTarea(tarea.getId().toString());
					expedienteCC.getDatosFlujoCtaCte().setNombreTarea(tarea.getDescripcion());
				}
				Empleado empleado = empleadoDAO.findByCodigo(asignedUser);
				if (empleado != null) {
					expedienteCC.setCodUsuarioActual(empleado.getCodigo());
					expedienteCC.setNomUsuarioActual(empleado.getNombres().trim()+" "+empleado.getApepat().trim()+" "+empleado.getApemat().trim());
				}
				expedienteCC.setActivado(fechaAsignacion);
				expedienteCC.setFechaRegistro(fechaRegistro.getTime());
				expedienteCC.setFechaServidorP(new Date());
				
				taskList.add(expedienteCC);
			} else {
				log.warn("Expediente con código={} no existe.", idExpediente);
			}
		}
		Calendar c2 = Calendar.getInstance();
		log.debug("Búsqueda en Process demoró "+(c2.getTimeInMillis()-c1.getTimeInMillis())+" ms.");
		
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
	
	public ExpedienteCC obtenerDetalleExpediente(ExpedienteCC expedienteCC) {
//		Calendar c1 = Calendar.getInstance();
		String respuesta = getTaksDetails(expedienteCC.getTaskID());
		JsonElement jsonElement = new JsonParser().parse(respuesta);
		jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
		respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
				.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
		ExpedienteCC resultado = Convertidor.fromJSONtoExpedienteCC(respuesta,jsonElement);
		resultado.setTaskID(expedienteCC.getTaskID());
		resultado.getDatosFlujoCtaCte().setNombreTarea(jsonElement.getAsJsonObject().get(NODE_TASKNAME).getAsString());
		resultado.setFechaServidorP(expedienteCC.getFechaServidorP());
//		Calendar c2 = Calendar.getInstance();
//		log.debug("Obtener detalle del expediente demoró "+(c2.getTimeInMillis()-c1.getTimeInMillis())+" ms.");
		
		return resultado;
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
	
	public static ExpedienteCC copiarDatos(Expediente expediente, ExpedienteCC expedienteCC) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("copiarDatos ()");
//		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		log.info("EXPEDIENTE ID:"+expediente.getId());
		log.info("EXPEDIENTE OPERACION ID:"+expediente.getOperacion().getId());
		log.info("DESCRIPCION ESTADO EXPEDIENTE: "+expediente.getEstado().getDescripcion());
		
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Estado del expediente.."+expediente.getEstado().getId());
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Accion.."+expediente.getAccion());
		
		ExpedienteTarea expedienteTareaActual = expediente.getExpedienteTareas().iterator().next();
		
//		ExpedienteCC expedienteCC = (ExpedienteCC)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION);
		
		expedienteCC.setIdOperacion(String.valueOf(expediente.getOperacion().getId()));
		expedienteCC.setCodigoExpediente(String.valueOf(expediente.getId()));
		expedienteCC.setEstadoExpediente(String.valueOf(expediente.getEstado().getDescripcion()));
		expedienteCC.setIdEstadoExpediente(String.valueOf(expediente.getEstado().getId())); //**
		expedienteCC.setNumeroTarea(String.valueOf(expedienteTareaActual.getTarea().getId()));
		expedienteCC.setNombreTarea(expedienteTareaActual.getTarea().getDescripcion());
		expedienteCC.setEstadoTarea(String.valueOf(expedienteTareaActual.getEstadoTarea().getDescripcion()));  //**
		expedienteCC.setIdEstadoTarea(String.valueOf(expedienteTareaActual.getEstadoTarea().getId()));  //**
//		expedienteCC.setCodUsuarioActual(String.valueOf(expediente.getEmpleado().getId()));  						// se llena en el BPM - GestionOperacionesCtaCte
//		expedienteCC.setNomUsuarioActual(expediente.getEmpleado().getNombresCompletos());    						// no se usa en el BPM **
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getEmpleado" + expediente.getId());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getEmpleado" + expediente.getEmpleado().getCodigo());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getOficina" + expediente.getOficina().getCodigo());
		expedienteCC.setCodOficina(String.valueOf(expediente.getEmpleado().getOficina().getId()));  //***
		expedienteCC.setDesOficina(expediente.getEmpleado().getOficina().getDescripcion());
		expedienteCC.setDesTerritorio(expediente.getEmpleado().getOficina().getTerritorio().getDescripcion());
		expedienteCC.setCodOperacion(expediente.getOperacion().getCodigoOperacion());
		expedienteCC.setDesOperacion(expediente.getOperacion().getDescripcion());
		expedienteCC.setCodCentralCliente(expediente.getCliente().getCodigoCentral());
		expedienteCC.setNumDOICliente(expediente.getCliente().getNumeroDoi());
		expedienteCC.setRazonSocialCliente(expediente.getCliente().getRazonSocial());
		expedienteCC.setFechaRegistro(expediente.getFechaRegistro());
//		expedienteCC.setFechaAtencion(new Date());
//		expedienteCC.setMinAmarillo(expedienteTareaActual.getTarea().getAmarilloMinutos());
//		expedienteCC.setMinVerde(expedienteTareaActual.getTarea().getVerdeMinutos());		
		
		DatosFlujoCC datosFlujoCC = expedienteCC.getDatosFlujoCtaCte();
		if (datosFlujoCC == null) {
			datosFlujoCC = new DatosFlujoCC();
			expedienteCC.setDatosFlujoCtaCte(datosFlujoCC);
		}

		datosFlujoCC.setAccion(expediente.getAccion());    	
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Ayuda Expediente datosFlujoCC.getAccion().." + datosFlujoCC.getAccion());
		log.info("expediente.getFlagEjecutoCobroComision() : {}", expediente.getFlagEjecutoCobroComision());
		log.info("expediente.getFlagExoneracionComision() : {}", expediente.getFlagExoneracionComision());
		if (ConstantesBusiness.FLAG_EJECUTO_COBRO_COMISION.equals(expediente.getFlagEjecutoCobroComision())
				|| ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION.equals(expediente.getFlagExoneracionComision())) {
			//datosFlujoCC.setClienteExonerado(String.valueOf(expediente.getFlagExoneracionComision()));
			datosFlujoCC.setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_EXONERADO_COBRO_COMISION));
		} else {
			datosFlujoCC.setClienteExonerado(String.valueOf(ConstantesBusiness.FLAG_COBRO_COMISION));
		}
		//datosFlujoCC.setClienteExonerado("0");
		datosFlujoCC.setFlagExisteFacultadesEspeciales(String.valueOf(expediente.getFlagFacultadesEspeciales()));
		datosFlujoCC.setFlagExisteModificatoria(String.valueOf(expediente.getFlagModificatoria()));
//		datosFlujoCC.setFlagExisteFirmaPorActivar();  															// se llena en el BPM - GestionOperaciones								
//		datosFlujoCC.setCodUsuarioAbogado(expediente)       ?????
//		datosFlujoCC.setNomUsuarioAbogado(expediente)       ?????
//		datosFlujoCC.setIdTarea(expediente)                                                                     // se llena en BPM
//		datosFlujoCC.setCodUsuarioResponsable()             							                      // el user q inicia el flujo. Deberia consultarse solo la primera vez 
//		datosFlujoCC.setNomUsuarioResponsable(exp)															  // no se usa en el BPM **
		datosFlujoCC.setIdTerritorio(String.valueOf(expediente.getOficina().getTerritorio().getId()));
		if (expediente.getProducto()!=null) {
			datosFlujoCC.setIdProducto(String.valueOf(expediente.getProducto().getId()));
		}
		datosFlujoCC.setResultadoBastanteo(expediente.getResultadoBastanteo());
		
		OperacionesCC operacionesCC = expedienteCC.getOperacionesCtaCte();
		if (operacionesCC == null) {
			operacionesCC = new OperacionesCC();
			expedienteCC.setOperacionesCtaCte(operacionesCC);
		}
//		operacionesCC.setTipoFlujoOperacion()                                                            // se llena en el BPM
//		operacionesCC.setFlagCriterioAuditoriaInterna()                  							     // se llena en el BPM - GestionOperaciones
//		operacionesCC.setFlagEstadoBastanteo(flagEstadoBastanteo)										 // se deberia llenar en el BPM
//		operacionesCC.setFlagPlazoSubsanacion()															 // se llena en el BPM - GestionOperaciones
//		operacionesCC.setFlagCobroComision(flagCobroComision);											// se deberia llenar en el flujo 
	
		return expedienteCC;
	}

}
