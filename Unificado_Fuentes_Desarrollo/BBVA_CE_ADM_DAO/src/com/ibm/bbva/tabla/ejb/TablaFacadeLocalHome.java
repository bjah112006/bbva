package com.ibm.bbva.tabla.ejb;

import com.ibm.as.core.ejb.ILocalHome;


public interface TablaFacadeLocalHome extends ILocalHome {

	@SuppressWarnings("unchecked")
	public com.ibm.bbva.tabla.ejb.TablaFacadeLocal create()
		throws javax.ejb.CreateException;
}
