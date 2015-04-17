package com.ibm.bbva.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.bbva.tabla.dao.ParametrosConfDAO;



public class EJBLocator {
	
	public static Object lookupEJBLocal(Class o) {
		try {
			return new InitialContext().lookup("ejblocal:" + o.getCanonicalName());
		} catch (NamingException e) {
			return null;
		}
	}

	
	/*
	public static HorarioDAO getHorarioDAO() {
		return (HorarioDAO) lookupEJBLocal(HorarioDAO.class);
	}
	*/
	
	
	public static ParametrosConfDAO getParametrosConfDAO() {
		return (ParametrosConfDAO) lookupEJBLocal(ParametrosConfDAO.class);
	}

}
