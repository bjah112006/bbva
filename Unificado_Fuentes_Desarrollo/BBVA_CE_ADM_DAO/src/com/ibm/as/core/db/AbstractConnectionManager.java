package com.ibm.as.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import com.ibm.as.core.exception.GenericException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.as.core.util.log.BaseLogger.Level;


/**
 * @author lbeltran
 *
 */
public abstract class AbstractConnectionManager {

	private static WorkMode workMode;

	private class ConnThreadLocal extends ThreadLocal<Connection> {
		private Connection con = null;

		protected synchronized Connection initialValue() {
			try {
				Connection con = getNewConnection();

				con.setAutoCommit(false);

				return con;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		public Connection getNew(boolean newConnection) {
			if ( newConnection && con==null )
				con = get();

			return con;
		}

		public void set(Connection con) {
			this.con = con;
		}
	}

	private ConnThreadLocal con = new ConnThreadLocal(); 
	
	protected enum WorkMode {

		LOCAL {
			public String toString() {
				return "LOCAL";
			}
		},
		SERVER {
			public String toString() {
				return "SERVER";
			}
		};

		public static WorkMode getWorkMode(String keyValue) {
			if (LOCAL.toString().equals(keyValue)) {
				return LOCAL;
			}

			if (SERVER.toString().equals(keyValue)) {
				return SERVER;
			}

			throw new IllegalArgumentException("WorkMode not Supported : " + keyValue);
		}
	}

	protected AbstractConnectionManager() {
		try {
			workMode = WorkMode.getWorkMode( getProperties().getProperty("config.db.connection.workmode") );
		}
		catch (GenericException e) {
		}
	}
	
	public Connection getConnection() throws GenericException {
		return this.getConnection(true);
	}

	/**
	 * Returns a connection for this thread. The <code>createNew</code> parameters
	 * instructs the method whether to create a new connection or not in case
	 * one has not been internally created yet
	 * 
	 * @param createNew whether to create a new connection if it has not been done yet
	 * @return The current connection for this thread if already exists or if <code>createNew</code> is true, 
	 * otherwise null   
	 * @throws GenericException
	 */
	public Connection getConnection(boolean createNew) throws GenericException {
		Connection c = null;
		try {
			if (workMode.equals(WorkMode.SERVER)){
				//BaseLogger.log(this.getClass(), "getConnection", Level.DEBUG, "Solicita conexión DB al servidor.");
				if (createNew) c = getNewConnection(); 
			}else {
				BaseLogger.log(this.getClass(), "getConnection", Level.DEBUG, "Solicita conexión DB de forma local.");
				c = con.getNew(createNew);
			}
		}
		catch (RuntimeException e) {
			BaseLogger.log(this.getClass(), "getConnection", "An error ocurred while trying to get a connection", e);
			throw new GenericException(e);
		}
		return c;
	}
	
	public Connection getConnection(String dataSourceName) throws GenericException {
		try {
			return getNewConnection(dataSourceName);
		}
		catch (RuntimeException e) {
			BaseLogger.log(this.getClass(), "getConnection", "An error ocurred while trying to get a connection for data source \""+dataSourceName+"\"", e);
			throw new GenericException(e.getCause());
		}
	}

	public void refreshConnection() throws GenericException {
		con.set( getNewConnection() );
	}
	
	protected Connection getNewConnection() throws GenericException {
		return getNewConnection(null);
	}

	protected Connection getNewConnection(String dataSourceName) throws GenericException {
		try {
			Connection con = null;
			
			if (WorkMode.LOCAL.equals(workMode))
				con = getLocalConnection();
			else {
				String dsProperty = "config.db.connection.";

				dsProperty += dataSourceName==null? "defaultDatasource" : dataSourceName;
				
				String dsJndiName = getProperties().getProperty( dsProperty );
				//LOG.info("dsJndiNamedsJndiName  "+dsJndiName);
				if ( dsJndiName==null )
					throw new GenericException("Can´t get property "+dsProperty);
				
				DataSource ds = getDataSource(dsJndiName);
				//TODO: Revisar el manejo de las conexiones
				con = ds.getConnection();
			}
			
			con.setAutoCommit(false);

			return con;
		}
		catch (Exception e) {
			throw new GenericException(e);
		}
	}
	
	protected Connection getLocalConnection() throws GenericException {
		try {
			String className = getProperties().getProperty("config.db.connection.driver");

			Class.forName(className).newInstance();

			String url = getProperties().getProperty("config.db.connection.url");
			String user = getProperties().getProperty("config.db.connection.user");
			String pass = getProperties().getProperty("config.db.connection.pass");

			return DriverManager.getConnection(url, user, pass);
		}
		catch (Exception e) {
			throw new GenericException(e);
		}
	}

	public void closeConnection() {
		try {
			Connection actCon = null;

			if ( (actCon=this.getConnection(false))!=null ) {
				//BaseLogger.log(this.getClass(), "closeConnection", Level.DEBUG, "Closing active connection: "+actCon);
				if (actCon != null) 
					actCon.close();

				con.set(null);
			}
		}
		catch (Exception e) {
			BaseLogger.log(this.getClass(), "closeConnection", Level.DEBUG, "FAILED. Closing active connection");
		}
	}

	public void closeConnection(Connection con) {
		try {
			if (con!=null)
				con.close();
		}
		catch (Exception e) {
		}
	}

	protected abstract Properties getProperties() throws GenericException;
	
	protected abstract DataSource getDataSource(String name) throws GenericException;
}
