/**
 * 
 */
package com.ibm.bbva.tabla.core.db;

import java.util.Properties;

import javax.sql.DataSource;

import com.ibm.as.core.db.AbstractConnectionManager;
import com.ibm.as.core.exception.GenericException;
import com.ibm.as.core.util.DefaultServiceLocator;


public class DefaultConnectionManager extends AbstractConnectionManager {

	private static final String PROPERTY_FILE_PATH = "com/ibm/bbva/tabla/core/config/connectionProperties.xml";
	
	private static Properties props = null;

	private static DefaultConnectionManager conMgr = null;

	public static DefaultConnectionManager getInstance() {
		if ( conMgr==null )
			conMgr = new DefaultConnectionManager();
		
		return conMgr;
	}

	protected Properties getProperties() throws GenericException {
		if (props == null) {
			try {
				props = new Properties();
				props.loadFromXML(DefaultConnectionManager.class.getClassLoader()
						.getResourceAsStream(PROPERTY_FILE_PATH));
				
			} catch (Exception e) {
				throw new GenericException(e);
			}
		}
		return props;
	}

	@Override
	protected DataSource getDataSource(String name) throws GenericException {
		//System.out.println("getDataSource : "+DefaultServiceLocator.getInstance().getDataSource(name));
		return DefaultServiceLocator.getInstance().getDataSource(name);
	}

}
