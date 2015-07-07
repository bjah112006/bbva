package pe.ibm.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/*
 http://192.168.1.113:9080/bpmrest-ui
*/

public class RestBPMImpl implements RestBPM{
	
	private static final Logger log = LoggerFactory.getLogger(RestBPMImpl.class);

	private static final String REST_BPM_WLE_V1_PROCESS = "/rest/bpm/wle/v1/process?";
	private static final String REST_BPM_WLE_V1_SERVICE = "/rest/bpm/wle/v1/service/";
	private static final String REST_BPM_WLE_V1_TASK = "/rest/bpm/wle/v1/task/";
	private static final String REST_BPM_WLE_V1_BULK = "/rest/bpm/wle/v1/bulk";
	private static final String WEB_RESOURCE_TYPE = "application/json"; 
	private static final String REST_TASK_QUERY = "/rest/bpm/wle/v1/tasks/query/";
	private static final String REST_BPM_WLE_V1_SYSTEMS = "/rest/bpm/wle/v1/systems";
	private static final String REST_BPM_WLE_V1_CONSULTA_BBVA ="/rest/bpm/wle/v1/service/";
	
	private static final String HOST = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("hostServidorProcess");
	private static final String PORT = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("puertoServidorProcess");
	private static final String USUARIO_ADMIN = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("usuarioAdminProcess");
	private static final String PASSWORD_ADMIN = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("passwordAdminProcess");
	private static final String BPD_ID_CUENTA_CORRIENTE_PROCESS = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("idCuentaCorrienteProcess");
	private static final String BPD_ID_REASIGNAR_TAREA = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("idReasignarTarea");
	private static final String BPD_ID_RECIBIR_ENVIAR_MENSAJE = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("idRecibirEnviarMensaje");
	private static final String PROCESS_APP_ID = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("processAppId");
	private static final String SNAPSHOP_ID = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("snapshotId");
	private static final String SERVICE_ID = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("serviceId");

	public void crearProcessInstance(){
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
				 	  "action=start" +
				 	  "&bpdId=" + BPD_ID_CUENTA_CORRIENTE_PROCESS +
				 	  "&snapshotId=" +SNAPSHOP_ID+
				 	  "&processAppId=" + PROCESS_APP_ID +
				 	  "&parts=all"; 
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

//        System.out.println(output);
         
	}
	
	public String crearProcessInstance(String json){
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
				 	  "action=start" +
				 	  "&bpdId=" + BPD_ID_CUENTA_CORRIENTE_PROCESS;
		if (SNAPSHOP_ID != null && !SNAPSHOP_ID.equalsIgnoreCase(""))
			url = url + "&snapshotId=" +SNAPSHOP_ID;
		url = url + "&processAppId=" + PROCESS_APP_ID +
				 	  "&params="+json +
				 	  "&parts=all"; 
		
		log.info("URL crearProcessInstance:::::"+url);
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        JsonElement jsonElement = new JsonParser().parse(output);
		String piid = jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("piid").toString();
		
//        System.out.println("PIIID::::"+output);
        
        return piid;
         
	}
	
	public void completeProcess(String piid){
		
		
		String url = "http://"+HOST+":"+PORT+"/rest/bpm/wle/v1/process/"+piid+"?"+
						"action=terminate"+ 
						"&parts=all"; 
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

//        System.out.println(output);
         
	}
	
	public void completeTask(String idTarea){
		
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+idTarea+"?"+
						"action=finish"+ 
						"&parts=all"; 
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

//        System.out.println(output);
         
	}
	
	public void completeTask(String idTask,String json) {
		
//		String idTask = "33";
		
		String strJSON=	"{\"user\":\""+USUARIO_ADMIN+"\",\"taskId\":\""+idTask+"\"}";
		
		log.info("transferirTarea strJSON::::"+strJSON);
		try {
			//reassignTask(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			assignTask(idTask,USUARIO_ADMIN);
		} catch (Exception e) {
			log.error("", e);
		}
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+idTask+"?"+
						"action=finish"+ 
						"&params=" + json +
						"&parts=all"; 
        
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        log.info("completeTask:::"+output);
         
	}
	
	/*
	public void assingToMe(String idTarea){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+idTarea+"?"+
				"action=assign&toMe=true&parts=none";

		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		 
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
		
		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		System.out.println(output);		
	}
	
	
	public void assignTask(String taskId,String userAssing){
		 
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+taskId+"?"+
						"action=assign"+ 
						"&toUser="+ userAssing +
						"&parts=all"; 
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        System.out.println(output);
         
	}
	
	public void reAssignTask(String taskId,String userAssing){
		assignTask(taskId,userAssing);
	}
	*/
	
	public void setDataTask(String taskId,String json){

		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_SERVICE+taskId+"?"+
						"action=setData"+ 
						"&params="+json; 
		
		log.info("URL setDataTask:::::"+url);
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

//        System.out.println(output);
         
	}
	
	public void listarTask(String taskId,String userAssing){
		
//		String taskId = "32";
//		String userAssing = "rchac";
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+taskId+"?"+
						"action=assign"+ 
						"&toUser="+ userAssing +
						"&parts=all"; 
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

//        System.out.println(output);
         
	}

	@Override
	public String searchTemplate(String nameSearch,String filtroUser){
		
		String url = "http://"+HOST+":"+PORT+REST_TASK_QUERY+nameSearch;
	
		if(filtroUser !=null && !"".equals(filtroUser))
			url = url + "?queryFilter=OWNER%20in%20("+ filtroUser+")";

		log.info("url::::"+url);
		
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		 
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).get(ClientResponse.class);
		
		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
				
//		System.out.println(output);
		return output;
	}
	
		
	@Override
	public String getTaksDetails(String taskId){
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+taskId+"?"+
				"&parts=all"; 
		
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		 
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).get(ClientResponse.class);
		
		String output = response.getEntity(String.class);
		
		if (response.getStatus() == 404) {
			return "0";
		}
		
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
				
//		System.out.println(output);
		return output;
	}
	
	@Override
	public String getTaksDetailsBulk(List<String> lstIdTask) {
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_BULK;
		
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		 
		WebResource webResource = client.resource(url);
		MultivaluedMap formData = new MultivaluedMapImpl();
		for (String idTask: lstIdTask) {
			formData.add("command", "GET:"+REST_BPM_WLE_V1_TASK+idTask+"?&parts=all");
		}
		ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
		
		String output = response.getEntity(String.class);
		
		if (response.getStatus() == 404) {
			return "0";
		}
		
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		return output;
	}
	
	public void completeWaitingOperation(String json){


		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
			 	  "action=start" +
			 	  "&bpdId=" + BPD_ID_RECIBIR_ENVIAR_MENSAJE +
			 	  "&snapshotId=" +SNAPSHOP_ID+
			 	  "&processAppId=" + PROCESS_APP_ID +
			 	  "&params="+json +
			 	  "&parts=all"; 

		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);

		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

	}

	public void reassignTask(String json){
		
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
			 	  "action=start" +
			 	  "&bpdId=" + BPD_ID_REASIGNAR_TAREA +
			 	  "&snapshotId=" +SNAPSHOP_ID+
			 	  "&processAppId=" + PROCESS_APP_ID +
			 	  "&params="+json +
			 	  "&parts=all"; 
	
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		   
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
		  
		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
//		System.out.println(output);
         
	}
	
	
	
	public Date getDateServer(){
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_SYSTEMS;
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);

		return webResource.head().getResponseDate();
         
	}
	
	public String getListTaskBBVA(String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_CONSULTA_BBVA+SERVICE_ID;

		log.info("url JSON::::"+url);

		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		MultivaluedMap formData = new MultivaluedMapImpl();
		formData.add("action", "start");
		formData.add("params", json);
		formData.add("snapshotId", SNAPSHOP_ID);
		formData.add("createTask", "false");
		formData.add("parts", "all");

		ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);

		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

//		System.out.println(output);
		return output;
	}
	
	public void assignTask(String taskId,String userAssing){

		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+taskId+ 
						"?action=assign"+ 
						"&toUser="+ userAssing +
						"&parts=all"; 
		
		log.info("url assignTask::"+url);
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);

		String output = response.getEntity(String.class);

        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
//        log.info(output);
	}

}
