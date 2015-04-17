package com.ibm.as.core.view;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;

import com.ibm.as.core.ejb.IRemoteHome;
import com.ibm.as.core.exception.ServiceLocationException;
import com.ibm.as.core.util.DefaultServiceLocator;
import com.ibm.as.core.view.AbstractBusinessDelegate;


public class BaseBusinessDelegate extends AbstractBusinessDelegate {

	protected <T extends EJBLocalObject> T getLocalBean(String jndiName)
			throws ServiceLocationException {
		return DefaultServiceLocator.getInstance().getLocalEJB(jndiName);
	}

	protected <T extends EJBObject, H extends IRemoteHome> T getRemoteBean(
			String jndiName, Class<H> classType)
			throws ServiceLocationException {
		return DefaultServiceLocator.getInstance().getEJB(jndiName, classType);
	}
}
