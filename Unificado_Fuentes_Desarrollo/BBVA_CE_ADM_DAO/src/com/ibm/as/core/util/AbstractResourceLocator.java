package com.ibm.as.core.util;

import java.util.HashMap;

import javax.naming.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.ServiceLocationException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.as.core.util.log.BaseLogger.Level;

/**
 * 
 * @author lbeltran
 * 
 * @version 1.0
 */
public abstract class AbstractResourceLocator {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceLocator.class);

	protected abstract Context getInitialContext(boolean isLocal) throws ServiceLocationException;

	private HashMap<String,Object> hm = null;
	
	/**
	 * Returns any local JNDI published resource
	 * @param jndiName The JNDI name of the resource to be located
	 * @return A local resource
	 * @throws ServiceLocationException If an exception occurs
	 * @author lbeltran
	 */
	public Object getResource(String jndiName) throws ServiceLocationException {

		if (hm == null) hm = new HashMap<String, Object>();
		
		//1. Revisar si el objeto ya fue encontrado
		Object o = hm.get(jndiName);
		if (o != null){ //El objeto ya fue obtenido anteriormente
			BaseLogger.log(this.getClass(), "getResource", Level.TRACE, "El recurso " + jndiName + " ya fue recuperado anteriormente. Se obtiene la instancia anterior.");
			return o;
		}
		
		try {
			o = getInitialContext(true).lookup(jndiName);
			hm.put(jndiName, o);
			BaseLogger.log(this.getClass(), "getResource", Level.TRACE, "El recurso " + jndiName + " se recuperó exitosamente y será almacenado en cache.");
		}
		catch (Exception e) {
			LOG.info("Error en Get Resource");
			e.printStackTrace(System.out);
			throw new ServiceLocationException("An error ocurred locating resource '"+jndiName+"'", e);
		}
		return o;
	}
}
