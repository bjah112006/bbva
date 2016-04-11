package com.ibm.bbva.cm.support.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.support.ICMCallback;
import com.ibm.bbva.cm.support.ICMOperations;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class ICMTemplate implements ICMOperations {
	
	private static final Logger logger = LoggerFactory.getLogger(ICMTemplate.class);

	private ICMTemplateHelper helper = new ICMTemplateHelper();

	public ICMTemplate() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#store(com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public void store(final ICMDocument icmDocument) {
		executeInTransaction(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				helper.store(dsICM, icmDocument);
				return null;
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#store(com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public String store_PID(final ICMDocument icmDocument) {
		return String.valueOf(executeInTransaction(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				return helper.store_PID(dsICM, icmDocument);
				//return null;
			}
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#store(java.util.List)
	 */
	public void store(final List<ICMDocument> icmDocuments) {
		executeInTransaction(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				for (ICMDocument icmDocument : icmDocuments) {
					helper.store(dsICM, icmDocument);
				}
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#retrieve(com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public ICMDocument retrieve(final ICMDocument filter) {
		return (ICMDocument) execute(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				return helper.retrieve(dsICM, filter);
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#retrieve(com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public List<ICMDocument> getDocuments(final ICMDocument filter) {
		return (List<ICMDocument>) execute(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				return helper.getDocuments(dsICM, filter);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#remove(com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public void remove(final ICMDocument icmDocument) {
		executeInTransaction(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				helper.remove(dsICM, icmDocument);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.ICMOperations#remove(java.util.List)
	 */
	public void remove(final List<ICMDocument> icmDocuments) {
		executeInTransaction(new ICMCallback() {
			public Object doInCallback(DKDatastoreICM dsICM) {
				for (ICMDocument icmDocument : icmDocuments) {
					helper.remove(dsICM, icmDocument);
				}
				return null;
			}
		});
	}

	/**
	 * Maneja la ejecucion de acciones en ICM, obteniendo previamente una conexion y retornandola al finalizar su uso.
	 * 
	 * @param action
	 *            accion a realizar en ICM
	 * @return resultado de la accion en ICM
	 */
	private Object execute(ICMCallback action) {
		DKDatastoreICM dsICM = ICMDataSource.getInstance().getConnection();
		Object result = null;

		try {
			result = action.doInCallback(dsICM);
		} catch (RuntimeException e) {
			logger.error("Error execute()", e);
			throw e;
		} catch (Error e) {
			logger.error("Error execute()", e);
			throw e;
		} finally {
			ICMDataSource.getInstance().returnConnection(dsICM);
		}

		return result;
	}

	/**
	 * Maneja la ejecucion de acciones en ICM, obteniendo previamente una conexion y retornandola al finalizar su uso. Ademas, ejecuta las
	 * acciones en una transaccion, invocando commit en caso de exito y rollback en presencia de una excepcion.
	 * 
	 * @param action
	 *            accion a realizar en ICM
	 * @return resultado de la accion en ICM
	 */
	private Object executeInTransaction(ICMCallback action) {
		DKDatastoreICM dsICM = ICMDataSource.getInstance().getConnection();
		Object result = null;

		try {
			startTransaction(dsICM);
			try {
				result = action.doInCallback(dsICM);
			} catch (RuntimeException e) {
				logger.error("Error executeInTransaction()", e);
				rollback(dsICM);
				throw e;
			} catch (Error e) {
				logger.error("Error executeInTransaction()", e);
				rollback(dsICM);
				throw e;
			}
			commit(dsICM);
		} finally {
			ICMDataSource.getInstance().returnConnection(dsICM);
		}

		return result;
	}

	/**
	 * Inicia una transaccion en ICM.
	 * 
	 * @param dsICM
	 *            conexion a ICM
	 */
	private void startTransaction(DKDatastoreICM dsICM) {
		try {
			logger.debug("Inicio de transaccion ICM");
			dsICM.startTransaction();
		} catch (Exception e) {
			logger.error("Error startTransaction()", e);
			throw new ICMException(e);
		}
	}

	/**
	 * Ejecuta un commit a la transaccion en ICM.
	 * 
	 * @param dsICM
	 *            conexion a ICM
	 */
	private void commit(DKDatastoreICM dsICM) {
		try {
			logger.debug("Commit de transaccion ICM");
			dsICM.commit();
		} catch (Exception e) {
			logger.error("Error commit()", e);
			throw new ICMException(e);
		}
	}

	/**
	 * Ejecuta un rollback a la transaccion en ICM.
	 * 
	 * @param dsICM
	 *            conexion a ICM
	 */
	private void rollback(DKDatastoreICM dsICM) {
		try {
			logger.debug("Rollback de transaccion ICM");
			dsICM.rollback();
		} catch (Exception e) {
			logger.error("Error rollback()", e);
			throw new ICMException(e);
		}
	}

}
