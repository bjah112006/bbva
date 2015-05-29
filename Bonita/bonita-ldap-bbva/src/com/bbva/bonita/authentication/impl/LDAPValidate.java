package com.bbva.bonita.authentication.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

import org.bonitasoft.engine.log.technical.TechnicalLogSeverity;
import org.bonitasoft.engine.log.technical.TechnicalLoggerService;

import pe.com.bbva.ws.ldap.servidor.Usuario;
import pe.com.bbva.ws.ldap.servidor.WSLDAPServiceImplService;

public class LDAPValidate {

	private TechnicalLoggerService logger;
	private Properties props;
	private static LDAPValidate instance;

	static {
		instance = new LDAPValidate();
	}
	
	public LDAPValidate() {
	}

	public TechnicalLoggerService getLogger() {
		return logger;
	}

	public void setLogger(TechnicalLoggerService logger) {
		this.logger = logger;
	}

	public String getProperty(String key) {
		try {
			logger.log(this.getClass(), TechnicalLogSeverity.INFO, "===>" + System.getProperty("btm.root"));
			// logger.log(this.getClass(), TechnicalLogSeverity.INFO, "===>" + System.getProperty("bonita.home"));
			props = new Properties();
			props.load(new FileInputStream(System.getProperty("btm.root").replace("\\", "/") + "/conf/configWFFastPyme.properties"));
		} catch (IOException e) {
			logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
		}
		return props.getProperty(key);
	}
	
	public boolean checkCredentials(String user, String password) {
		boolean isCheck = false;

		try {
			String providerURL = DBUtil.obtenerParametro(DBUtil.URL_LDAP);
			String securityPrincipal = "cn=" + user+ "," + getProperty("webseal.empleados");
			InitialDirContext ctx = null;
			Properties env = System.getProperties();
			Properties envLDAP = (Properties) env.clone();
			envLDAP.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			envLDAP.put(Context.PROVIDER_URL, providerURL);
			envLDAP.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
			envLDAP.put(Context.SECURITY_CREDENTIALS, password);
			ctx = new InitialDirContext(envLDAP);
			ctx.close();
			
			isCheck = true;
		} catch(AuthenticationException e){
			logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
		} catch(Exception e){
			logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
		}
		
		return isCheck;
	}

	public Usuario obtenerUsuario(String codigoUsuario) {
		Usuario usuario = null;

        try {
        	URL url = new URL(getProperty("wsdl.ldap"));
        	WSLDAPServiceImplService proxy = new WSLDAPServiceImplService(url);        	
        	List<Usuario> usuarios = proxy.getWSLDAPServiceImplPort().obtenerUsuarios(Collections.singletonList(codigoUsuario));
        	
        	if(usuarios != null && !usuarios.isEmpty()) {
        		usuario = usuarios.get(0);
        	}
        } catch (Exception e) {
        	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "No se pudo acceder al webservice");
        	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
        }
        
        return usuario;
	}
	
	public static LDAPValidate getInstance(TechnicalLoggerService logger) {
		instance.setLogger(logger);
		return instance;
	}
}
