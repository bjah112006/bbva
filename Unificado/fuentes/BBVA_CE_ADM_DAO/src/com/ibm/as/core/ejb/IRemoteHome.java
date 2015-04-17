/**
 * 
 */
package com.ibm.as.core.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

/**
 * @author gsalinas
 * 
 */
public interface IRemoteHome extends EJBHome {
	public <T extends EJBObject> T create() throws javax.ejb.CreateException,
			RemoteException;
}
