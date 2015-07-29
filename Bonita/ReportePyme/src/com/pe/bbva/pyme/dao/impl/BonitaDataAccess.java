package com.pe.bbva.pyme.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.LoginException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pe.bbva.pyme.utils.ConstantesEnum;

public class BonitaDataAccess {
	
	private static final Logger LOG = LoggerFactory.getLogger(BonitaDataAccess.class);
		
	private APISession apiSession;
	private ProcessAPI processAPI;
	private IdentityAPI identityAPI;
	private ProcessDefinition processDefinition;
	
	private String userName;
	private String password;
	private String idProceso;
	private String serverUrl;
	private String applicationName;
		
	public BonitaDataAccess() throws Exception{
		super();	
		this.userName = BonitaClientRest.getProperty(ConstantesEnum.PARAM_USER_NAME_BONITA.getNombre());
		this.password = BonitaClientRest.getProperty(ConstantesEnum.PARAM_PASSWORD_BONITA.getNombre());
		this.idProceso = BonitaClientRest.getProperty(ConstantesEnum.PARAM_ID_PROCESO.getNombre());
		this.serverUrl = BonitaClientRest.getProperty(ConstantesEnum.PARAM_SERVER_NAME.getNombre());
		this.applicationName = BonitaClientRest.getProperty(ConstantesEnum.PARAM_APPLICATION_NAME.getNombre());
		this.setApiSession(obtenerAccesoBonita(this.serverUrl, this.applicationName, this.userName, this.password));
		this.setProcessAPI(TenantAPIAccessor.getProcessAPI(getApiSession()));
		this.setIdentityAPI(TenantAPIAccessor.getIdentityAPI(getApiSession()));
//		this.setProcessDefinition(getProcessAPI().getProcessDefinition(new Long(this.idProceso)));
		LOG.debug("...acceso autorizado a BD Bonita");
	}	
	
	public static APISession obtenerAccesoBonita(String serverUrl,String applicationName,String username, String password) throws BonitaHomeNotSetException,ServerAPIException, UnknownAPITypeException, LoginException {
		Map<String, String> apiTypeManagerParams = new HashMap<String, String>();
		apiTypeManagerParams.put(ConstantesEnum.PROP_SERVER_NAME.getNombre(), serverUrl);
		apiTypeManagerParams.put(ConstantesEnum.PROP_APPLICATION_NAME.getNombre(), applicationName);
		APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP,apiTypeManagerParams);
		LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
		APISession session = loginAPI.login(username,password);
		return session;
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

	public String getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(String idProceso) {
		this.idProceso = idProceso;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}	
}