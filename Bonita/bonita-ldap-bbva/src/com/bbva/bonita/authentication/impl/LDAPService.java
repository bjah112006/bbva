package com.bbva.bonita.authentication.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;

import com.bbva.bonita.rest.RestAPIClient;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LDAPService {

    private static final Logger logger = Logger.getLogger("LDAPService");
    private String user;
    private String pwd;
    private String server;

    public LDAPService(LDAPValidate ldapValidate) {
        super();
        this.user = ldapValidate.getProperty("user_name_bonita");
        this.pwd = ldapValidate.getProperty("password_bonita");
        this.server = ldapValidate.getProperty("server_url") + "/bonita/";
    }
    
    public void verificarUsuario(String username) throws JsonProcessingException, IOException {
        logger.log(Level.SEVERE, "Registrando usuario");
        String entity = "{\"" + username + "\": \"" + username + "\"";

        RestAPIClient restApiClient = new RestAPIClient(server);
        restApiClient.loginAs(user, pwd);
        HttpResponse response = restApiClient.executePostRequest("API/extension/actualizarUsuario", entity);
        String result = restApiClient.consumeResponseIfNecessary(response);
        logger.log(Level.SEVERE, result);
        restApiClient.shutdown();
    }
}