/**
 * 
 */
package com.ibm.as.core.ejb;

import javax.ejb.EJBLocalObject;


public interface ILocalHome extends javax.ejb.EJBLocalHome {

	public <T extends EJBLocalObject> T create() throws javax.ejb.CreateException;
	
}
