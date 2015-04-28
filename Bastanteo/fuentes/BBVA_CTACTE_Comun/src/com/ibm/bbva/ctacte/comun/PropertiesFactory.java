package com.ibm.bbva.ctacte.comun;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class PropertiesFactory implements ObjectFactory {

	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		Properties properties = new Properties();

		Reference reference = (Reference) obj;
		Enumeration<RefAddr> refs = reference.getAll();

		RefAddr refAddr = null;

		while (refs.hasMoreElements()) {
			refAddr = refs.nextElement();
			properties.setProperty(refAddr.getType(), refAddr.getContent()
					.toString());
		}

		return properties;
	}

}
