package com.ibm.bbva.cm.support.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.util.Util;
import com.ibm.mm.sdk.common.DKDatastorePool;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class ICMDataSource {

	private static volatile ICMDataSource instance;
	private static final Logger logger = LoggerFactory.getLogger(ICMDataSource.class);
	private static final String DATASTORE_TYPE = "com.ibm.mm.sdk.server.DKDatastoreICM";
	private DKDatastorePool dsPool;
	private int minPoolSize;
	private int maxPoolSize;
	private String libraryServer;
	private String username;
	private String encryptedPassword;
	private String connectString;

	private ICMDataSource() {
		this.minPoolSize = 10; 
		this.maxPoolSize = 0;

		this.libraryServer = Util.leerPropiedad("cmserver");
		logger.info("libraryServer : " + this.libraryServer);

		this.username = Util.leerPropiedad("cmuserid");
		logger.info("username : " + this.username);
		logger.info("pass : " + Util.leerPropiedad("cmpassword"));

		this.encryptedPassword = Util.leerPropiedad("cmpassword");
		init();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		destroy();
	}

	public static ICMDataSource getInstance() {
		if (instance == null) {
			synchronized (ICMDataSource.class) {
				if (instance == null) {
					instance = new ICMDataSource();
				}
			}
		}
		return instance;
	}

	private void init() {
		try {
			logger.info("Iniciando el Pool de conexiones de Content");
			this.dsPool = new DKDatastorePool("com.ibm.mm.sdk.server.DKDatastoreICM");
			this.dsPool.setDatastoreName(this.libraryServer);
			this.dsPool.setTimeOut(5); // cada 5 minutos se destruyen las conexiones sin usar mayores a minPoolSize 
			this.dsPool.setMinAndMaxPoolSize(this.minPoolSize, this.maxPoolSize);
			this.dsPool.setReclaimUnusedConnection(true);

			if (StringUtils.isNotEmpty(this.connectString)) {
				this.dsPool.setConnectString(this.connectString);
			}
			logger.info("Se creó el Pool de conexiones de Content satisfactoriamente");
		} catch (Exception e) {
			logger.error("Error init()", e);
			throw new ICMException(e);
		}
	}

	private void destroy() {
		try {
			this.dsPool.destroy();
			logger.info("Se destruyó el Pool de conexiones de Content");
		} catch (Exception e) {
			logger.error("Error destroy()", e);
			throw new ICMException(e);
		}
	}

	public DKDatastoreICM getConnection() {
		DKDatastoreICM ds = null;
		try {
			ds = (DKDatastoreICM) this.dsPool.getConnection(this.username,
					this.encryptedPassword);
		} catch (Exception e) {
			logger.error("Error getConnection()", e);
			throw new ICMException(e);
		}

		return ds;
	}

	public void returnConnection(DKDatastoreICM dsICM) {
		try {
			this.dsPool.returnConnection(dsICM);
		} catch (Exception e) {
			logger.error("Error returnConnection()", e);
			throw new ICMException(e);
		}
	}
}