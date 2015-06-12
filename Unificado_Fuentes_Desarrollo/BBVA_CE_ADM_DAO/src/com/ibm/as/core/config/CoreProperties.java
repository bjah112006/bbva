package com.ibm.as.core.config;

import java.util.Properties;

import com.ibm.as.core.util.log.BaseLogger;

public class CoreProperties {

	public enum Property {
		DEFAULT_LOG_WRITER("default.log.writer"),
		DEFAULT_LOG_LEVEL("default.log.level");
		
		private String key = null;

		private Property(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	private static final String APP_PROPS_FILE = "com/ibm/as/core/config/coreProperties.xml";

	private static Properties props = null;

	public static String getProperty(Property property) {
		if ( props==null ) {
			props = new Properties();

			try {
				props.loadFromXML(CoreProperties.class.getClassLoader().getResourceAsStream(APP_PROPS_FILE));
			}
			catch (Exception e) {
				BaseLogger.log(CoreProperties.class, "getProperty", "Ocurrió un error cargando las propiedades de la aplicación", e);
				return null;
			}			
		}

		return props.getProperty( property.getKey() );
	}
}
