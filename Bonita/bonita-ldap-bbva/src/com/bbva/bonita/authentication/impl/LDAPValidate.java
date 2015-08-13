package com.bbva.bonita.authentication.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

import pe.com.bbva.ws.ldap.servidor.Usuario;
import pe.com.bbva.ws.ldap.servidor.WSLDAPServiceImplService;

public class LDAPValidate {

	private static Logger logger = Logger.getLogger("LDAPValidate");
	private Properties props;
	private static LDAPValidate instance;

	static {
		instance = new LDAPValidate();
	}
	
	public LDAPValidate() {
	}

    public String getProperty(String key) {
		try {
			logger.log(Level.INFO, "btm.root ======>" + System.getProperty("btm.root"));
			logger.log(Level.INFO, "bonita.home ===>" + System.getProperty("bonita.home"));
			props = new Properties();
			props.load(new FileInputStream(System.getProperty("btm.root").replace("\\", "/") + "/conf/configWFFastPyme.properties"));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "getProperty", e);
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
		} catch(Exception e){
			logger.log(Level.SEVERE, "checkCredentials", e);
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
        	logger.log(Level.SEVERE, "obtenerUsuario", "No se pudo acceder al webservice");
        	logger.log(Level.SEVERE, "obtenerUsuario", e);
        }
        
        return usuario;
	}
	
	public static LDAPValidate getInstance() {
		return instance;
	}
}
