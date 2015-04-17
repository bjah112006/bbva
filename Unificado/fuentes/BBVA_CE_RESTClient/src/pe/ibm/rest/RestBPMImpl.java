package pe.ibm.rest;

import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.MultivaluedMap;

import pe.ibm.bean.Constantes;

import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/*
 http://192.168.1.113:9080/bpmrest-ui
*/

public class RestBPMImpl implements RestBPM{

    private ParametrosConfBeanLocal parametrosConfBean; 
    
	private static final String REST_BPM_WLE_V1_PROCESS = "/rest/bpm/wle/v1/process?";
	private static final String REST_BPM_WLE_V1_SERVICE = "/rest/bpm/wle/v1/service/";
	private static final String REST_BPM_WLE_V1_CONSULTA_BBVA ="/rest/bpm/wle/v1/service/";
	private static final String REST_BPM_WLE_V1_TASK = "/rest/bpm/wle/v1/task/";
	private static final String REST_BPM_WLE_V1_BULK = "/rest/bpm/wle/v1/bulk";
	private static final String WEB_RESOURCE_TYPE = "application/json"; 
	private static final String REST_TASK_QUERY = "/rest/bpm/wle/v1/tasks/query/";
	private static final String REST_BPM_WLE_V1_SYSTEMS = "/rest/bpm/wle/v1/systems";
	private static final String REST_BPM_WLE_V1_PROCESS2 = "/rest/bpm/wle/v1/process/";
	
	/*private static final String BPD_ID_TARJETA_CREDITO_PROCESS = "25.ecc66a04-4eef-4674-9465-3aed26b8bceb";
	private static final String BPD_ID_RECIBIR_ENVIAR_MENSAJE = "25.4c859075-3fc8-4f07-a2e2-00393853542c";
	private static final String BPD_ID_REASIGNAR_TAREA = "25.008dfe6d-c875-4e69-addd-7ec26d3fd571";*/
	/*private static final String PROCESS_APP_ID = "2066.bf3fc9cc-6dea-48dd-9768-ec332032b024";
	private static final String SNAPSHOP_ID = "2064.844f52f2-de54-424f-8e94-8d13e64f77d5";
	private static final String HOST = "9.6.104.213";//"Base-Win2k8x64";
	private static final String PORT = "9080";*/
	//private static final String USUARIO_ADMIN = "tw_admin";
	//private static final String PASSWORD_ADMIN = "tw_admin";
	//private static final String SERVICE_ID = "1.b4b89b46-cfe7-41a5-b0f9-2f8a2129f519";
	//private static final String SERVICE_COUNT_EXP_BY_USER_ID  = "1.8340abe6-357a-4adc-90f7-942134f205a7";
	
	private String PROCESS_APP_ID = "";
	private String SNAPSHOT_ID = "";
	private String HOST = "";
	private String PORT = "";
	
	private String ID_TARJETA_CREDITO_PROCESS = "";
	private String ID_RECIBIR_ENVIAR_MENSAJE = "";
	private String ID_REASIGNAR_TAREA = "";	
	private String USUARIO_ADMIN = "";	
	private String PASSWORD_ADMIN = "";	
	private String SERVICE_ID="";
	private String SERVICE_COUNT_EXP_BY_USER_ID  ="";
	
	public RestBPMImpl() {
		super();		
		try {
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			
			PROCESS_APP_ID = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_PROCESS_APP_ID").getValorVariable();
			SNAPSHOT_ID =parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_SNAPSHOP_ID").getValorVariable();
			HOST = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_HOST").getValorVariable();
			PORT = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_PORT").getValorVariable();			
			
			ID_TARJETA_CREDITO_PROCESS = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_ID_TARJETA_CREDITO_PROCESS").getValorVariable();
			ID_RECIBIR_ENVIAR_MENSAJE = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_ID_RECIBIR_ENVIAR_MENSAJE").getValorVariable();
			ID_REASIGNAR_TAREA = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_ID_REASIGNAR_TAREA").getValorVariable();
			USUARIO_ADMIN = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_USUARIO_ADMIN").getValorVariable();
			PASSWORD_ADMIN = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_PASSWORD_ADMIN").getValorVariable();
			
			SERVICE_ID = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_SERVICE_ID").getValorVariable();
			
			SERVICE_COUNT_EXP_BY_USER_ID = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "REST_SERVICE_COUNT_EXP_BY_USER_ID").getValorVariable();
			
			System.out.println("BPD_ID_TARJETA_CREDITO_PROCESS:::."+ID_TARJETA_CREDITO_PROCESS);			
			System.out.println("ID_RECIBIR_ENVIAR_MENSAJE:::."+ID_RECIBIR_ENVIAR_MENSAJE);	
			System.out.println("ID_REASIGNAR_TAREA:::."+ID_REASIGNAR_TAREA);	
			System.out.println("USUARIO_ADMIN:::."+USUARIO_ADMIN);	
			System.out.println("PASSWORD_ADMIN:::."+PASSWORD_ADMIN);	
			System.out.println("SERVICE_ID:::."+SERVICE_ID);
			System.out.println("SERVICE_COUNT_EXP_BY_USER_ID:::."+SERVICE_COUNT_EXP_BY_USER_ID);
			
			//System.out.println("PROCESS_APP_ID:::."+PROCESS_APP_ID);
		} catch (NamingException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}
	}
	
	public void crearProcessInstance(){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
				 	  "action=start" +
				 	  "&bpdId=" + ID_TARJETA_CREDITO_PROCESS +
				 	  "&snapshotId=" +SNAPSHOT_ID+
				 	  "&processAppId=" + PROCESS_APP_ID +
				 	  "&parts=all"; 
		
		System.out.println("url= "+url);
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        System.out.println(output);         
	}
	
	public void crearProcessInstance(String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
				 	  "action=start" +
				 	  "&bpdId=" + ID_TARJETA_CREDITO_PROCESS;
		
		if (SNAPSHOT_ID != null && !SNAPSHOT_ID.equalsIgnoreCase(""))
			url = url + "&snapshotId=" +SNAPSHOT_ID;
		
		url = url +
			 	  "&processAppId=" + PROCESS_APP_ID +
			 	  "&params="+json +
			 	  "&parts=all"; 
		
		System.out.println("URL crearProcessInstance:::::"+url);
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        System.out.println(output);         
	}
	
	public String crearProcessInstanceTask(String json){		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
				 	  "action=start" +
				 	  "&bpdId=" + ID_TARJETA_CREDITO_PROCESS;
		
		if (SNAPSHOT_ID != null && !SNAPSHOT_ID.equalsIgnoreCase(""))
			url = url + "&snapshotId=" +SNAPSHOT_ID;
		
		url = url +
			 	  "&processAppId=" + PROCESS_APP_ID +
			 	  "&params="+json +
			 	  "&parts=all"; 
		
		System.out.println("URL crearProcessInstance:::::"+url);
		
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        int posicion = output.indexOf("tkiid");
		output = output.substring(posicion+8, output.length());
		posicion = output.indexOf("\"");
		output = output.substring(0, posicion);
		
		return output;         
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

        System.out.println(output);         
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

        System.out.println(output);         
	}
	
	public String completeTask(String idTask, String json){
//		String idTask = "33";
//		String strJSON=	"{\"user\":\""+USUARIO_ADMIN+"\",\"taskId\":\""+idTask+"\"}";
//		System.out.println("transferirTarea strJSON::::"+strJSON);
		try {
//			reassignTask(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			assignTask(idTask,USUARIO_ADMIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+idTask+"?"+
						"action=finish"+ 
						"&params=" + json +
						"&parts=all"; 
		
		
        System.out.println("url : " + url); 
		Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
         
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).put(ClientResponse.class);
        
        String output = response.getEntity(String.class);
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        System.out.println("completeTask:::"+output);
        return String.valueOf(response.getStatus());         
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
	
	public void setDataTask(String taskId, String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_SERVICE+taskId+"?"+
						"action=setData"+ 
						"&params="+json; 
		
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
	
	public void listarTask(String taskId, String userAssing){
//		String taskId = "32";
//		String userAssing = "rchac";
		
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+taskId+"?"+
						"action=assign"+ 
						"&toUser="+ userAssing +
						"&parts=all"; 
		System.out.println("url= "+url);
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

	@Override
	public String searchTemplate(String nameSearch,String filtroUser){
		String url = "http://"+HOST+":"+PORT+REST_TASK_QUERY+nameSearch;
		System.out.println("url= "+url);
		if(filtroUser !=null && !"".equals(filtroUser))
			url = url + "?queryFilter=OWNER%20in%20("+ filtroUser+")";
		
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		WebResource webResource = client.resource(url);
		/**
		 * Cambio en reemplazo del comentario
		 * 
		 * */
		ClientResponse response=null;
		String output="";
		try{
			response = webResource.type(WEB_RESOURCE_TYPE).get(ClientResponse.class);
			output = response.getEntity(String.class);			
		} catch(Exception e){
			System.out.println(" error 1 = "+e.getMessage());
		}
		//ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).get(ClientResponse.class); 

		if(response!=null)
			if (response.getStatus() != 200) {
			     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
		
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
				
		System.out.println(output);
		return output;
	}
	
	public void completeWaitingOperation(String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
			 	  "action=start" +
			 	  "&bpdId=" + ID_RECIBIR_ENVIAR_MENSAJE;
		
		if (SNAPSHOT_ID != null && !SNAPSHOT_ID.equalsIgnoreCase(""))
			url = url + "&snapshotId=" +SNAPSHOT_ID;
		
			url = url +
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
		
		System.out.println(output);         
	}
	
	public void reassignTask(String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_PROCESS +
			 	  "action=start" +
			 	  "&bpdId=" + ID_REASIGNAR_TAREA;
		
		if (SNAPSHOT_ID != null && !SNAPSHOT_ID.equalsIgnoreCase(""))
			url = url + "&snapshotId=" +SNAPSHOT_ID;
		
			url = url +
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
		
		System.out.println(output);
	}
	
	public Date getDateServer(){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_SYSTEMS;
		System.out.println("URLLLLLLL::::::"+url);
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		System.out.println("getDateServer::::::");
		return webResource.head().getResponseDate();
	}
	
	public String getListTaskTC(String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_CONSULTA_BBVA+SERVICE_ID;

		System.out.println("url JSON:::: "+url);

		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		
		WebResource webResource = client.resource(url);
		MultivaluedMap formData = new MultivaluedMapImpl();
		formData.add("action", "start");
		formData.add("params", json);
		formData.add("snapshotId", SNAPSHOT_ID);
		formData.add("createTask", "false");
		formData.add("parts", "all");

		ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		return output;
	}	
	
	public void assignTask(String taskId,String userAssing){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_TASK+taskId+
						"?action=assign"+ 
						"&toUser="+ userAssing +
						"&parts=all"; 
		System.out.println("url assignTask::"+url);
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(WEB_RESOURCE_TYPE).post(ClientResponse.class);

		String output = response.getEntity(String.class);

        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        
        System.out.println(output);
	}

	@Override
	public String getTaksDetailsBulk(List<String> lstIdTask) {
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_BULK;

//		System.out.println("url getTaksDetailsBulk = "+url);
		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));
		System.out.println("usuario: "+USUARIO_ADMIN+" password: "+PASSWORD_ADMIN);
		
		WebResource webResource = client.resource(url);
		System.out.println("webResource fin");
		MultivaluedMap formData = new MultivaluedMapImpl();
		for (String idTask: lstIdTask) {
			formData.add("command", "GET:"+REST_BPM_WLE_V1_TASK+idTask+"?&parts=all");
					
			}
		System.out.println("fin for lstIdTask");		
		ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
		System.out.println("response fin");	
		String output = response.getEntity(String.class);
		if (response.getStatus() == 404) {
			return "0";
		}

		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		System.out.println("output "+output);
		return output;
	}	
	
	public String getCountListTaskTC(String json){
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_CONSULTA_BBVA+SERVICE_COUNT_EXP_BY_USER_ID ;

//		System.out.println("url JSON::::"+url);

		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		MultivaluedMap formData = new MultivaluedMapImpl();
		formData.add("action", "start");
		formData.add("params", json);
		formData.add("snapshotId", SNAPSHOT_ID);
		formData.add("createTask", "false");
		formData.add("parts", "all");

		ClientResponse response = webResource.accept("application/json").type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);

		String output = response.getEntity(String.class);
		if (response.getStatus() != 200) {
		     throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		return output;
	} 
	
	@Override  
	public String getProcessDetailsBulk(List<String> lstIdPiid) {
		String url = "http://"+HOST+":"+PORT+REST_BPM_WLE_V1_BULK;

		Client client = new Client();
		client.addFilter(new HTTPBasicAuthFilter(USUARIO_ADMIN, PASSWORD_ADMIN));

		WebResource webResource = client.resource(url);
		MultivaluedMap formData = new MultivaluedMapImpl();
		for (String piid: lstIdPiid) {
			System.out.println("piid::"+piid);
			formData.add("command", "GET:"+REST_BPM_WLE_V1_PROCESS2+piid+"?&parts=all");
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
}
