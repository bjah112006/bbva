package com.ibm.bbva.resources;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class DefaultPropertyFactory implements ObjectFactory {

	private static Properties properties = null;
	
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {

		if ( properties==null ) {
			properties = new Properties();

			Reference ref = (Reference) obj;

			Enumeration<RefAddr> refs = ref.getAll();

			RefAddr ra = null;

			while (refs.hasMoreElements()) {
				ra = refs.nextElement();

				properties.setProperty(ra.getType(), ra.getContent().toString());
			}
		}

		return properties;
	}

}
