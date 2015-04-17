package pe.ibm.bpd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pe.ibm.bean.Constantes;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.ContentManager;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.rest.RestBPM;
import pe.ibm.rest.RestBPMImpl;
import pe.ibm.util.Convertidor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RemoteUtils {
	
	private RestBPM restBPM = new RestBPMImpl();
	private static final String BANDEJA_SEARCH_TEMPLATE_NAME = "BBVA_TC_BANDEJA";
	private static final String INICIO_CADENA_JSON = "{\"expediente\":";
	private static final String INICIO_CONSULTA_JSON = "{\"consulta\":";
	
	private static final String INICIO_CONSULTA_JSON_2 = "{\"consulta\":\"";
	private static final String FINAL_CADENA_JSON_2 = "\"}"; 
	private static final String NODE_CANTIDAD_EXP = "cantidadExpedientes"; 
	
	private static final String FINAL_CADENA_JSON = "}";
	private static final String NODE_DATA = "data";
	private static final String NODE_ITEMS = "items";
	private static final String NODE_RESULTADO = "resultado"; 
	private static final String NODE_ITEM_IDTASK = "idTask";
	private static final String NODE_ITEM_ASIGNEDUSER = "asignedUser";
	private static final String NODE_ITEM_APPATERNO = "apPaterno";
	private static final String NODE_ITEM_APMATERNO = "apMaterno";
	private static final String NODE_ITEM_NOMBRES = "nombre";
	private static final String NODE_TASKID = "tkiid";	
	private static final String NODE_VARIABLES = "variables";
	private static final String NODE_EXPEDIENTE = "expediente";
	private static final String NODE_RESPONSE = "response";
	private static HashMap CONSTANTE_TABLA_TAREAS = null;
	private static final String NODE_ITEM_IDPIID = "piid";	
	private static final String ID_NO_EXISTE_TAREA = "-1";
	private static final String NODE_PIID = "piid";
	
	public RemoteUtils() {
		super();
		CONSTANTE_TABLA_TAREAS = new HashMap();
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_1, Constantes.RUTA_NAVEGACION_WEB_TAREA_1); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_2, Constantes.RUTA_NAVEGACION_WEB_TAREA_2); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_3, Constantes.RUTA_NAVEGACION_WEB_TAREA_3); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_4, Constantes.RUTA_NAVEGACION_WEB_TAREA_4); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_5, Constantes.RUTA_NAVEGACION_WEB_TAREA_5);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_6, Constantes.RUTA_NAVEGACION_WEB_TAREA_6);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_7, Constantes.RUTA_NAVEGACION_WEB_TAREA_7);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_10, Constantes.RUTA_NAVEGACION_WEB_TAREA_10);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_11, Constantes.RUTA_NAVEGACION_WEB_TAREA_11);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_12, Constantes.RUTA_NAVEGACION_WEB_TAREA_12);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_13, Constantes.RUTA_NAVEGACION_WEB_TAREA_13);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_14, Constantes.RUTA_NAVEGACION_WEB_TAREA_14);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_15, Constantes.RUTA_NAVEGACION_WEB_TAREA_15);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_16, Constantes.RUTA_NAVEGACION_WEB_TAREA_16);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_17, Constantes.RUTA_NAVEGACION_WEB_TAREA_17);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_18, Constantes.RUTA_NAVEGACION_WEB_TAREA_18);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_19, Constantes.RUTA_NAVEGACION_WEB_TAREA_19);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_20, Constantes.RUTA_NAVEGACION_WEB_TAREA_20);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_26, Constantes.RUTA_NAVEGACION_WEB_TAREA_26);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_27, Constantes.RUTA_NAVEGACION_WEB_TAREA_27);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_32, Constantes.RUTA_NAVEGACION_WEB_TAREA_32);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_33, Constantes.RUTA_NAVEGACION_WEB_TAREA_33);		
		// TODO Apéndice de constructor generado automáticamente
	}

	public List<ExpedienteTCWPS> obtenerInstanciasTareasPorUsuario (Consulta consulta){
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();

		String cadenaUsuarios = null;
		String tkiid = "";
		String piid = "";

		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);

		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();

		List<String> lstIdTask = new ArrayList<String>();
		List<String> lstIdPiid = new ArrayList<String>();
		
		System.out.println("cadenaUsuarios = "+cadenaUsuarios);
		
		if(items!=null && items.size()>0){
			System.out.println("items = "+items.size());
		}else
			System.out.println("items es nulo o vacio ");
		
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
			items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;
			
			piid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).getAsString() : "";
					
//			System.out.println("tkiid:::"+tkiid);
//			System.out.println("piid:::"+piid);

			String asignedUser = "";

			if (!ID_NO_EXISTE_TAREA.equals(tkiid)) // si No es instancia en proceso
				asignedUser = !items.get(i).getAsJsonObject().get(NODE_ITEM_ASIGNEDUSER).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_ASIGNEDUSER).getAsString() : null;

			String apPaterno = !items.get(i).getAsJsonObject().get(NODE_ITEM_APPATERNO).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_APPATERNO).getAsString() : null;
			String apMaterno = !items.get(i).getAsJsonObject().get(NODE_ITEM_APMATERNO).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_APMATERNO).getAsString() : null;
			String nombre = !items.get(i).getAsJsonObject().get(NODE_ITEM_NOMBRES).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_NOMBRES).getAsString() : null;
			
			System.out.println("consulta.isConsiderarUsuarios():::"+consulta.isConsiderarUsuarios());
			
			if (consulta.isConsiderarUsuarios()) {         // Pendientes
				List<String> usuarios = consulta.getUsuarios();
				cadenaUsuarios = "";

				if (usuarios != null && usuarios.size() > 0){   //pendientes de usuarios dependientes
					//System.out.println("Agregando lstIdTask.. considerando usuarios");
					for (String usr : usuarios) {

						if (usr.equalsIgnoreCase(asignedUser)){
							//Agregado para busquedas en band. pendiente
							String apPaternoFilter = consulta.getApPaterno();

							String apMaternoFilter = consulta.getApMaterno();

							String nombreFilter = consulta.getNombres();
							
							if (apPaternoFilter != null && (apPaterno == null || !apPaterno.toUpperCase().contains(apPaternoFilter.toUpperCase())) ) continue;

							if (apMaternoFilter != null && (apMaterno == null || !apMaterno.toUpperCase().contains(apMaternoFilter.toUpperCase())) ) continue;

							if (nombreFilter != null && (nombre == null || !nombre.toUpperCase().contains(nombreFilter.toUpperCase())) ) continue;
														
							//fin de modificacion agregado para busquedas en band. pendiente

							lstIdTask.add(tkiid);
						}
							
					}

				}else{   // considerar usuario logueado
					//System.out.println("Agregando lstIdTask...");
					//System.out.println("usuarios es nulo o vacio ");
					//System.out.println("consulta.getCodUsuarioActual() = "+consulta.getCodUsuarioActual());
					if(consulta.getCodUsuarioActual() != null && consulta.getCodUsuarioActual().equalsIgnoreCase(asignedUser))
						lstIdTask.add(tkiid);
				}

			}else { //Busqueda
				String apPaternoFilter = consulta.getApPaterno();

				String apMaternoFilter = consulta.getApMaterno();

				String nombreFilter = consulta.getNombres();

				if (apPaternoFilter != null && (apPaterno == null || !apPaterno.toUpperCase().contains(apPaternoFilter.toUpperCase())) ) continue;

				if (apMaternoFilter != null && (apMaterno == null || !apMaterno.toUpperCase().contains(apMaternoFilter.toUpperCase())) ) continue;

				if (nombreFilter != null && (nombre == null || !nombre.toUpperCase().contains(nombreFilter.toUpperCase())) ) continue;
				
				if (ID_NO_EXISTE_TAREA.equals(tkiid))
					lstIdPiid.add(piid);
				else
					lstIdTask.add(tkiid);
			}
		}
	
		if (lstIdTask.size() != 0) {
			//Consulta de los detalles de las tareas
			System.out.println("lstIdTask.size():"+lstIdTask.size());
			
			respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
	
			for (int i = 0; i < items.size(); i++) {
	
				respuesta = items.get(i).toString();
	
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
	
				tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
				try{
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
						.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
				}catch(Exception ex){
					System.out.println("TC No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
							+ ex.getMessage());
					continue;
				}
	
				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC(respuesta,jsonElement,"1");
	
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 
	
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
				taskList.add(expedienteTC);
			}
		}
		
		if (lstIdPiid.size() != 0) {
			//Consulta por los piids para los casos de los expedientes en proceso

			System.out.println("lstIdPiid.size():"+lstIdPiid.size());

			respuesta = restBPM.getProcessDetailsBulk(lstIdPiid);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();

			for (int i = 0; i < items.size(); i++) {

				respuesta = items.get(i).toString();

				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);

				piid =  jsonElement.getAsJsonObject().get(NODE_PIID).getAsString();
				try{
					respuesta = jsonElement.getAsJsonObject().get(NODE_VARIABLES).
							getAsJsonObject().get(NODE_EXPEDIENTE).toString();
				}catch(Exception ex){
					System.out.println("TC No se pudo cargar el proceso con piid : "+ piid + "  "
							+ ex.getMessage());
					continue;
				}


				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC(respuesta,jsonElement,"0");

				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 

				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}

				taskList.add(expedienteTC);
			}
		}

		taskList = aplicarAjustes(taskList, consultaServicio);

		return taskList;
	}

	/***
	 * Obtiene un listados de tareas para la bandeja de reasignacion
	 * @param consulta
    */
	public List<ExpedienteTCWPS> listarTareasBandejaReasignacion (Consulta consulta){
		// Parametros de consulta considerados: 4
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();

		String cadenaUsuarios = null;
		String tkiid = "";
		

		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);

		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();

		List<String> lstIdTask = new ArrayList<String>();
				
		System.out.println("cadenaUsuarios = "+cadenaUsuarios);
		
		if(items!=null && items.size()>0){
			System.out.println("items = "+items.size());
		}else
			System.out.println("items es nulo o vacio ");
		
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
			items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;			
			
			lstIdTask.add(tkiid);
			
		}
	
		if (lstIdTask.size() != 0) {
			//Consulta de los detalles de las tareas
			System.out.println("lstIdTask.size():"+lstIdTask.size());
			
			respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
	
			for (int i = 0; i < items.size(); i++) {
	
				respuesta = items.get(i).toString();
	
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
	
				tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
				try{
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
						.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
				}catch(Exception ex){
					System.out.println("TC No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
							+ ex.getMessage());
					continue;
				}
	
				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC(respuesta,jsonElement,"1");
	
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 
	
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
				taskList.add(expedienteTC);
			}
		}	
					

		return taskList;
	}
	
	/***
	 * Obtiene un listados de tareas activas de Gestion en Process
	 * @param consulta
    */
	public List<ExpedienteTCWPS> obtenerListaTareasGestion(Consulta consulta){
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();
		
		String tkiid = "";
		
		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);
		System.out.println("respuesta:::"+respuesta);
		
		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();
		
		List<String> lstIdTask = new ArrayList<String>();
		
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;			
		
			System.out.println("tkiid:::"+tkiid);

			lstIdTask.add(tkiid);
		}
		
		if (lstIdTask.size() != 0) {
			//Consulta de los detalles de las tareas
			System.out.println("lstIdTask.size():"+lstIdTask.size());
			
			respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
			
			for (int i = 0; i < items.size(); i++) {
				
				respuesta = items.get(i).toString();
				
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
				
				tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
				
				try{				
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
							.getAsJsonObject().get(NODE_VARIABLES).toString();					
					
				}catch(Exception ex){
					System.out.println("TC No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
							+ ex.getMessage());
					continue;
				}				
				
				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC_Mantenimiento(respuesta,jsonElement,"1");
				
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 
	
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}

				taskList.add(expedienteTC);
			}
		}		
		
		return taskList;
	}
	
	public List<ExpedienteTCWPS> aplicarAjustes(List<ExpedienteTCWPS> taskList, ConsultaServicio consulta){
        
        //numeroContrato = "", codigoRVGL = "", codigoPreEvaluador = ""
        List<ExpedienteTCWPS> listaResultante = new ArrayList<ExpedienteTCWPS>();
        String campo = null;
        for (int j = 0; j < taskList.size(); j++) {                        
        
                campo = consulta.getNumeroContrato();
                System.out.println("campo 1::::"+campo);
                if (!"".equals(campo) && (taskList.get(j).getNumeroContrato() == null || "".equals(taskList.get(j).getNumeroContrato()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getNumeroContrato()) ) ) )continue;
                
                campo = consulta.getCodigoRVGL();
                System.out.println("campo 2::::"+campo);
                if (!"".equals(campo) && (taskList.get(j).getCodigoRVGL() == null || "".equals(taskList.get(j).getCodigoRVGL()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getCodigoRVGL()) ) ) )continue;
                
                campo = consulta.getCodigoPreEvaluador();
                System.out.println("campo 3::::"+campo);
                if (!"".equals(campo) && (taskList.get(j).getCodigoPreEvaluador() == null || "".equals(taskList.get(j).getCodigoPreEvaluador()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getCodigoPreEvaluador()) ) ) )continue;
        
                listaResultante.add(taskList.get(j));
    }
        System.out.println("tamaño::::"+listaResultante.size());
        return listaResultante;
}


	public String iniciarInstanciaProceso (String nombreInstanciaProceso, ExpedienteTCWPS expedienteTC){
		try {
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;		
			System.out.println(" iniciarInstanciaProceso strJSON:::"+strJSON);
//			restBPM.crearProcessInstance(strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
			restBPM.crearProcessInstance(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			return "";
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String iniciarInstanciaProcesoTask (String nombreInstanciaProceso, ExpedienteTCWPS expedienteTC){
		try {
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;		
			System.out.println(" iniciarInstanciaProceso strJSON:::"+strJSON);
//			restBPM.crearProcessInstance(strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
			return restBPM.crearProcessInstanceTask(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/***
	 * Actualiza los datos del BO una tarea
	 * @param tkiid
	 * @param expedienteTC
	 */
	public void enviarExpedienteTC (String tkiid, ExpedienteTCWPS expedienteTC){ 
		try{
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;
			System.out.println("enviarExpedienteTC strJSON::::"+strJSON);
			restBPM.setDataTask(tkiid,java.net.URLEncoder.encode(strJSON, "UTF-8"));
			//restBPM.completeTask(tkiid, strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String completarTarea (String tkiid, ExpedienteTCWPS expedienteTC){
		String cadena = "";
		try{
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;
			System.out.println("completarTarea strJSON::::"+strJSON);
			cadena = restBPM.completeTask(tkiid, java.net.URLEncoder.encode(strJSON, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			cadena = "";
			throw new RuntimeException(e);
		}
		return cadena;
	}
	
	/*public void completarTarea (String tkiid){
		/*try{
			restBPM.completeTask(tkiid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}*/
	
	//Comentar para ver donde se encuentra el error
	public void transferirTarea (String usuarioOrigen, String usuarioDestino, String tkiid) {
		try{
			//String strJSON = "{\"user\":\""+usuarioDestino+"\",\"taskId\":\""+tkiid+"\"}";
			//System.out.println("transferirTarea strJSON::::"+strJSON);
			//restBPM.reassignTask(strJSON.replace("\"", "%22").replace("{", "{").replace("}", "}").replace(" "," "));
			restBPM.assignTask(tkiid, usuarioDestino);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void ejecutarOperacionEspera (String codigoExp){
		try{
			String strJSON= "{\"codigo\":\""+codigoExp+"\"}";
			System.out.println("strJSON::::"+strJSON);
			restBPM.completeWaitingOperation(java.net.URLEncoder.encode(strJSON, "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Calendar obtenerTimestampServidorProcess() {
		try {
			System.out.println("Preparandose para obtener fechaActualSistemaPROCESS");
			Date fechaActualSistema = restBPM.getDateServer();
			System.out.println("fechaActualSistema PROCESS:::"+fechaActualSistema);
			//System.out.println("fechaActualWAS =====> " + (new Date()).toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(fechaActualSistema);
			return cal;
		} catch (Exception e) {
			System.out.println("Exception obtenerTimestampServidorProcess");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String consultaListaTareasTC (ConsultaServicio consultaServicio){ 
		try {
			//String strJSON=	INICIO_CONSULTA_JSON+json+FINAL_CADENA_JSON;

			//restBPM.getListTaskBBVA(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			String strJSON=	INICIO_CONSULTA_JSON+Convertidor.fromConsultaServicioToJSON(consultaServicio)+FINAL_CADENA_JSON;
			System.out.println("strJSON getListTaskTC:::"+strJSON);
			String data = restBPM.getListTaskTC(strJSON);
			//System.out.println("data consultaListaTareasTC::"+data);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}	
	
	public int countConsultaListaTareasTC (String usuario){ 
		try {

			if (usuario == null || "".equals(usuario))
				return 0;

			String strJSON=	INICIO_CONSULTA_JSON_2+usuario+FINAL_CADENA_JSON_2;
			String data = restBPM.getCountListTaskTC(strJSON);
			//System.out.println("data::"+data);

			JsonElement jsonElement = new JsonParser().parse(data);
			String cantidad = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject()
					.get(NODE_CANTIDAD_EXP).getAsString();

			System.out.println("cantidad exp ::::"+cantidad);

			return Integer.parseInt(cantidad);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
