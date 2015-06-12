/**
 * 
 */
package com.ibm.as.core.view;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;

import com.ibm.as.core.ejb.IRemoteHome;
import com.ibm.as.core.exception.ServiceLocationException;

/**
 * 
 * @author gsalinas, lbeltran
 * 
 */
public abstract class AbstractBusinessDelegate {

	protected abstract <T extends EJBLocalObject> T getLocalBean(String localHomeJNDI)
			throws ServiceLocationException;

	protected abstract <T extends EJBObject, H extends IRemoteHome> T getRemoteBean(
			String remoteHomeJNDI, Class<H> classType)
			throws ServiceLocationException;

}
