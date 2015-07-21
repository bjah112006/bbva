package com.pe.bbva.pyme.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbva.bonita.rest.RestAPIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BonitaClientRest {

	private static final Logger LOG = LoggerFactory.getLogger(BonitaClientRest.class);
	private RestAPIClient restApiClient;
	
    public static final String FLAG_ACTIVACION_LDAP = "001";
    public static final String URL_LDAP = "002";
    public static final String HORAS_TRASNCURRIDAS = "003";
    public static final String URL_REPORTE_OUTPUT = "004";
    public static final String MAX_RESULT_INSTANCES = "005";
    public static final String MAX_RESULT_TASKS = "006";
    public static final String MAX_TRACE_DAYS = "007";
    
	public String getProperty(String key) {
		String prop = "";
		try {
			LOG.info("===>" + System.getProperty("btm.root"));
			Properties props = new Properties();
			props.load(new FileInputStream(System.getProperty("btm.root").replace("\\", "/") + "/conf/configWFFastPyme.properties"));
			prop = props.getProperty(key);
		} catch (IOException e) {
			LOG.error("", e);
		}
		return prop;
	}
	
	public void init() {
		restApiClient = new RestAPIClient(getProperty("server_url") + "/bonita/");
        restApiClient.loginAs(getProperty("user_name_bonita"), getProperty("password_bonita"));
	}

	public String obtainValue(String key) throws JsonProcessingException, IOException { 
        HttpResponse response = restApiClient.executeGetRequest("API/extension/parametro/get?key=" + key);
        String result = restApiClient.consumeResponseIfNecessary(response);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = mapper.readTree(result);

        return jsonObj.get("value").textValue();
	}
	
	public void logout() {
		restApiClient.shutdown();
	}
}
