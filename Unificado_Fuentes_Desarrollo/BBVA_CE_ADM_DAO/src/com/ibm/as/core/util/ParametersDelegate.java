package com.ibm.as.core.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.bbva.resources.Properties;
import com.ibm.as.core.util.log.Logger;
import com.ibm.as.core.util.log.BaseLogger.Level;





public class ParametersDelegate {

	private static ParametersDelegate pd = null;
	private static Properties p = null;

	private static String BBVA_RESOURCE_ENVIRONMENT_ENTRY = "rsc/bbvaEnv";
	
	private ParametersDelegate(){}
	
	public static ParametersDelegate getInstance(){
		if (pd == null){
			pd = new ParametersDelegate();
		}
		return pd;
	}
	
	public String getValue(String key){
		Logger.log(this.getClass(), "getValue", Level.TRACE, "> key -> "+key);

		String value = null;

		if (p == null) {
			
			Object o = null;
			try {
				Context ctxLocal = new InitialContext();
				o = ctxLocal.lookup(BBVA_RESOURCE_ENVIRONMENT_ENTRY);
			} catch (NamingException e) {
				Logger.log(this.getClass(), "getValue", "", e);
			}

			Logger.log(this.getClass(), "getValue", Level.TRACE, "o -> "+o);
			if (o == null){
				Logger.log(this.getClass(), "getValue", Level.DEBUG, "Imposible obtener el recurso asociado al JNDI: " + BBVA_RESOURCE_ENVIRONMENT_ENTRY);
				value = null;
			}
			p = (Properties)o;
		}
		if (p != null)
			value =  p.getProperty(key);
		else 
			value = null;

		Logger.log(this.getClass(), "getValue", Level.TRACE, "value -> "+value);

		return value;
	}
}

