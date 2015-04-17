/**
 * 
 */
package com.ibm.as.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import com.ibm.as.core.ejb.ILocalHome;
import com.ibm.as.core.ejb.IRemoteHome;
import com.ibm.as.core.exception.ServiceLocationException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.as.core.util.log.BaseLogger.Level;

/**
 * @author gsalinas
 * 
 */
public abstract class AbstractServiceLocator extends AbstractResourceLocator {

	protected Context ctx = null;
	protected Context ctxRemote = null;
	
	private Map<String, Object> locatorCache;

	protected AbstractServiceLocator() {
		locatorCache = Collections
				.synchronizedMap(new HashMap<String, Object>());
	}

	protected AbstractServiceLocator(Context initialContext) {
		this();
		this.ctx = initialContext;
	}
	
	public static AbstractServiceLocator getInstance(Context initialContext) {
		return new AbstractServiceLocator(initialContext) {

			protected Context getInitialContext(boolean isLocal)
					throws ServiceLocationException {
				return ctx;
			}
		};
	}

	/**
	 * 
	 * @param jndiName
	 * @throws ServiceLocationException
	 * 
	 * @author lbeltran
	 */
	@SuppressWarnings("unchecked")
	public <T extends EJBObject> T getEJB(String jndiName, Class ejbClass) throws ServiceLocationException {
		BaseLogger.log(this.getClass(), "getEJB", Level.TRACE, "init");
		BaseLogger.log(this.getClass(), "getEJB", Level.DEBUG, "> jndiName -> "+jndiName);
		
		try {
			IRemoteHome remoteHome = getRemoteHome(jndiName, ejbClass);

			EJBObject ejbO = remoteHome.create();

			BaseLogger.log(this.getClass(), "getEJB", Level.TRACE, "exit");

			return (T)ejbO;
		}
		catch (Exception e) {
			throw new ServiceLocationException(e);
		}
	}

	/**
	 * 
	 * @param jndiName
	 * @throws ServiceLocationException
	 * 
	 * @author lbeltran
	 */
	public <T extends EJBLocalObject> T getLocalEJB(String jndiName) throws ServiceLocationException {
		try {
			return getLocalHome(jndiName).create();
		}
		catch (Exception e) {
			throw new ServiceLocationException(e);
		}
	}
	
	public EJBLocalHome getEJBLocalHome(String jndiHomeName)
			throws ServiceLocationException {

		return (EJBLocalHome) getHome(jndiHomeName, true, EJBLocalHome.class);
	}

	public ILocalHome getLocalHome(String jndiHomeName)
			throws ServiceLocationException {

		return (ILocalHome) getEJBLocalHome(jndiHomeName);
	}

	public <T extends IRemoteHome> T getRemoteHome(String jndiHomeName,
			Class<T> classType) throws ServiceLocationException {
		return (T) getRemoteEJBHome(jndiHomeName, classType);
	}

	@SuppressWarnings("unchecked")
	public <T extends EJBHome> T getRemoteEJBHome(String jndiHomeName,
			Class<T> classType) throws ServiceLocationException {
		return (T) getHome(jndiHomeName, false, classType);
	}

	private void printInfoContext(Context ctx){
//		BaseLogger.log(this.getClass(), "printInfoContext", Level.DEBUG, "Se obtuvo el contexto: ");
//		
//		try{BaseLogger.log(this.getClass(), "printInfoContext", Level.DEBUG, "getNameInNamespace " + ctx.getNameInNamespace());}catch(Exception e){}
//		try{BaseLogger.log(this.getClass(), "printInfoContext", Level.DEBUG, "toString " + ctx.toString());}catch(Exception e){}
//		try{BaseLogger.log(this.getClass(), "printInfoContext", Level.DEBUG, "PROVIDER_URL " + Context.PROVIDER_URL);}catch(Exception e){}
//		try{BaseLogger.log(this.getClass(), "printInfoContext", Level.DEBUG, "DNS_URL " + Context.DNS_URL);}catch(Exception e){}

	}
	
	@SuppressWarnings("unchecked")
	private Object getHome(String jndiHomeName, boolean isLocal,
			Class classType) throws ServiceLocationException {

		BaseLogger.log(this.getClass(), "getHome", Level.TRACE, "init");

		Object home = null; boolean cacheEnabled = true;
		try {
			if ( cacheEnabled && locatorCache.containsKey(jndiHomeName) ) {
				//BaseLogger.log(this.getClass(), "getHome", Level.DEBUG, "Reference found in cache");

				home = locatorCache.get(jndiHomeName);
			} else {
				BaseLogger.log(this.getClass(), "getHome", Level.DEBUG, "Performing lookup");

				Context contexto = getInitialContext(isLocal);

				printInfoContext(contexto);

				Object objref = contexto.lookup(jndiHomeName);

				if (!isLocal) {
					objref = PortableRemoteObject.narrow(objref, classType);
				}

				BaseLogger.log(this.getClass(), "getHome", Level.TRACE, "objref -> "+objref);
				
				home = objref;

				BaseLogger.log(this.getClass(), "getHome", Level.TRACE, "Adding reference to cache");
				locatorCache.put(jndiHomeName, home);
			}
		} catch (NamingException ne) {
			BaseLogger.log(this.getClass(), "getHome", "An error ocurred looking up "+jndiHomeName, ne);
			throw new ServiceLocationException(ne);
		} catch (Exception e) {
			BaseLogger.log(this.getClass(), "getHome", "An error ocurred looking up "+jndiHomeName, e);
			throw new ServiceLocationException(e);
		}
		BaseLogger.log(this.getClass(), "getHome", Level.TRACE, "end");
		return home;
	}

	public DataSource getDataSource(String dataSourceName)
			throws ServiceLocationException {
		
		return (DataSource) getResource(dataSourceName);
	}
}	