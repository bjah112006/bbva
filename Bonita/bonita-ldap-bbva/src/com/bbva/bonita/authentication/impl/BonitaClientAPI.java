package com.bbva.bonita.authentication.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

import com.bbva.bonita.util.ConstantesEnum;


public class BonitaClientAPI {

    private static final Logger LOGGER = Logger.getLogger("BonitaClientAPI");
        
    private APISession apiSession;
    private ProcessAPI processAPI;
    private IdentityAPI identityAPI;
    private ProcessDefinition processDefinition;
        
    public BonitaClientAPI() throws Exception {
        super();
        String username = LDAPValidate.getInstance().getProperty("user_name_bonita");
        String password = LDAPValidate.getInstance().getProperty("password_bonita");
        String serverUrl = LDAPValidate.getInstance().getProperty("server_url");
        String applicationName = LDAPValidate.getInstance().getProperty("application_name");
        
        // LOGGER.log(Level.INFO, "====> username: " + username);
        // LOGGER.log(Level.INFO, "====> password: " + password);
        // LOGGER.log(Level.INFO, "====> server: " + serverUrl);
        // LOGGER.log(Level.INFO, "====> application:" + applicationName);
                
        Map<String, String> apiTypeManagerParams = new HashMap<String, String>();
        apiTypeManagerParams.put(ConstantesEnum.PROP_SERVER_NAME.getNombre(), serverUrl);
        apiTypeManagerParams.put(ConstantesEnum.PROP_APPLICATION_NAME.getNombre(), applicationName);
        APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, apiTypeManagerParams);
        LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
        APISession session = loginAPI.login(username,password);
        
        this.setApiSession(session);
        this.setProcessAPI(TenantAPIAccessor.getProcessAPI(getApiSession()));
        this.setIdentityAPI(TenantAPIAccessor.getIdentityAPI(getApiSession()));
        LOGGER.log(Level.INFO, "====> Acceso autorizado a BD Bonita");
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
}
