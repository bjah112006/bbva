package com.bbva.bonita.servlet.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageProcess extends AbstractJavaSamplerClient implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ManageProcess.class);
	
	private APISession apiSession;
	private ProcessAPI processAPI;
	private IdentityAPI identityAPI;
	private ProcessDefinition processDefinition;
	
	private String documento;
	private String processDefinitionId;
	private String urlBonita;
	
	public ManageProcess() {
		init();
	}
	
	private void init(){
		String username = "XP20261";
		String password = "XP20261";
		
		try {
			
			//TODO: LOGUE DE USUARIO
			Map<String, String> apiTypeManagerParams = new HashMap<String, String>();
			apiTypeManagerParams.put("server.url", getUrlBonita()); //http://118.180.34.207:8036
			apiTypeManagerParams.put("application.name", "bonita");
			APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP,apiTypeManagerParams);
			
			LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
			APISession session = loginAPI.login(username,password);
			setApiSession(session);
			
			this.setProcessAPI(TenantAPIAccessor.getProcessAPI(getApiSession()));
			this.setIdentityAPI(TenantAPIAccessor.getIdentityAPI(getApiSession()));
			
			Long processDefinitionID = Long.valueOf(getProcessDefinitionId()); //5669902879656320453L
			
			Long processInstanceID = getProcessAPI().startProcess(processDefinitionID, obtenerDataInstance()).getId();
			LOG.info("=== PROCESS INSTANCE ID: " + processInstanceID);
			
			//TODO: AGREGAMOS DOCUMENTOS AL CASO
			//TODO: DOCUMENTO
			byte[] documento = obtenerDocumento(new File(getDocumento()));
			getProcessAPI().attachDocument(processInstanceID, "file_sustento_cliente", "file_sustento_cliente.pdf", "application/pdf", documento);
			getProcessAPI().attachDocument(processInstanceID, "file_contrato", "file_contrato.pdf", "application/pdf", documento);
			getProcessAPI().attachDocument(processInstanceID, "file_reporte_credito", "file_reporte_credito.pdf", "application/pdf", documento);
			
			//TODO: CONSULTAMOS LAS TAREAS PENDIENTES DEL USUARIO
			List<HumanTaskInstance> pendingsTaks = getProcessAPI().getPendingHumanTaskInstances(getApiSession().getUserId(), 1, 100, ActivityInstanceCriterion.PRIORITY_DESC);
			LOG.info("=== CANTIDAD DE TAREAS PENDIENTES: " + pendingsTaks.size());
			
			for(HumanTaskInstance humanTask:pendingsTaks){
				//TODO: EJECUTAMOS LA NUEVA SOLICITUD CREADA
				if(processInstanceID == humanTask.getRootContainerId()){
					LOG.info("=== SE EJECUTARA TAREA DE SOLICITUD: " + processInstanceID);
					
					//TODO: 1. ASIGNAMOS LA TAREA AL USUARIO
					getProcessAPI().assignUserTask(humanTask.getId(), getApiSession().getUserId());
					
					//TODO: 2. EJECUTAMOS LA TAREA
					getProcessAPI().executeFlowNode(humanTask.getId());
					
					//TODO: 3. ACTUALIZAMOS LAS VARIABLES
					getProcessAPI().updateActivityInstanceVariables(humanTask.getId(), obtenerDataInstance());
					
					//TODO: 4. SALIMOS DEL BUCLE
					break;
				}
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}
	
	private byte[] obtenerDocumento(File file) throws FileNotFoundException, IOException{
		return IOUtils.toByteArray(new FileInputStream(file));
	}
	
	public static void main(String[] args) {
		new ManageProcess();
	}
	
	private Map<String, Serializable> obtenerDataInstance(){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("tipo_doi_cliente", "R");
		map.put("num_doi_cliente", "24242424242");
		map.put("nombre_cliente", "NSCH SAC PRUEB");
		map.put("num_rvgl", "2323232");
		map.put("codigo_centro_negocio", "ACN ARENALES");
		map.put("docs_conforme", "Si");
		map.put("ambito_registrante", "0736");
		map.put("const_nombre_app", "FAST NEGOCIOS");
		map.put("centro_negocio_origen", "ACN ARENALES");
		map.put("oferta_aprobada", "NINGUNO");
		map.put("producto", "PRESTAMO COMERCIAL");
		map.put("num_tramite", "0000999");
		map.put("ofi_registro", "0242-DAMERO GAMARRA");
		map.put("estado_solicitud", "ENVIADO ASIGNACION EVALUADOR");
		map.put("campania", "NINGUNA");
		map.put("clte_clasificacion", "NUEVO");
		map.put("oficina_gisat", "ACJ Arenales");
		return map;
	}
	
	public APISession getApiSession() {
		return apiSession;
	}
	public void setApiSession(APISession apiSession) {
		this.apiSession = apiSession;
	}
	public ProcessAPI getProcessAPI() {
		return processAPI;
	}
	public void setProcessAPI(ProcessAPI processAPI) {
		this.processAPI = processAPI;
	}
	public IdentityAPI getIdentityAPI() {
		return identityAPI;
	}
	public void setIdentityAPI(IdentityAPI identityAPI) {
		this.identityAPI = identityAPI;
	}
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	
	@Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument("URL_BONITA", "http://118.180.34.207:8036/bonita");
        defaultParameters.addArgument("PROCESSDEFINITIONID", "5669902879656320453");
        defaultParameters.addArgument("DOCUMENTO", "C://GRUPO 1 - DOCUMENTOS ECONOMICOS DEL CLIENTE.pdf");
        
        return defaultParameters;
    }
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		
		try {
			
			String documento = context.getParameter("DOCUMENTO");
			String processdefinitionid = context.getParameter("PROCESSDEFINITIONID");
			String urlBonita = context.getParameter("URL_BONITA");
			
			setDocumento(documento);
			setProcessDefinitionId(processdefinitionid);
			setUrlBonita(urlBonita);
			
			LOG.info("=== DOCUMENTO: " + getDocumento());
			LOG.info("=== PROCESSDEFINITION: " + getProcessDefinitionId());
			LOG.info("=== URL_BONITA: " + getUrlBonita());
			
			result.setResponseMessage( "Successfully performed action" );
			result.setResponseCodeOK(); // 200 code
			result.setDataType("text");
			
	        result.sampleStart(); // start stopwatch
			init();
			result.setSuccessful(true);
		} catch (Exception e) {
			LOG.error(e.toString());
            result.setSuccessful(false);
            result.setResponseMessage( "Exception: " + e );
            result.setDataType( org.apache.jmeter.samplers.SampleResult.TEXT );
            result.setResponseCode( "500" );
		} finally {
			result.sampleEnd();
		}
		
		return result;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getUrlBonita() {
		return urlBonita;
	}

	public void setUrlBonita(String urlBonita) {
		this.urlBonita = urlBonita;
	}
	
	

}
