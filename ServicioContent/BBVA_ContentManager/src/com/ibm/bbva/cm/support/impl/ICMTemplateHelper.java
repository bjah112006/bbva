package com.ibm.bbva.cm.support.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.exception.ICMDocumentNotFoundException;
import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.support.ICMAccess;
import com.ibm.bbva.cm.support.ICMCheckedCallback;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class ICMTemplateHelper implements ICMAccess {
	private static final Logger logger = LoggerFactory.getLogger(ICMTemplateHelper.class);

	private ICMAccess icmAccess = new ICMSimpleAccess();

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#store(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public void store(final DKDatastoreICM dsICM, final ICMDocument icmDocument) {
		execute(new ICMCheckedCallback() {
			public Object doInCallback() throws Exception {
				icmAccess.store(dsICM, icmDocument);
				return null;
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#store(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public String store_PID(final DKDatastoreICM dsICM, final ICMDocument icmDocument) {
		return String.valueOf(execute(new ICMCheckedCallback() {
			public Object doInCallback() throws Exception {
				return icmAccess.store_PID(dsICM, icmDocument);
				//return null;
			}
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#remove(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public void remove(final DKDatastoreICM dsICM, final ICMDocument icmDocument) {
		execute(new ICMCheckedCallback() {
			public Object doInCallback() throws Exception {
				icmAccess.remove(dsICM, icmDocument);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#retrieve(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public ICMDocument retrieve(final DKDatastoreICM dsICM, final ICMDocument filter) {
		return (ICMDocument) execute(new ICMCheckedCallback() {
			public Object doInCallback() throws Exception {
				return icmAccess.retrieve(dsICM, filter);
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.framework.icm.core.support.ICMAccess#retrieve(com.ibm.mm.sdk.server.DKDatastoreICM, com.bbvabancocontinental.vv.framework.icm.core.ICMDocument)
	 */
	public List<ICMDocument> getDocuments(final DKDatastoreICM dsICM, final ICMDocument filter) {
		return (List<ICMDocument>) execute(new ICMCheckedCallback() {
			public Object doInCallback() throws Exception {
				return icmAccess.getDocuments(dsICM, filter);
			}
		});
	}

	/**
	 * Maneja la ejecucion de las acciones en ICM devolviendo excepciones runtime en caso de error.
	 * 
	 * @param action
	 *            accion en ICM a ejecutar
	 * @return resultado de la accion en ICM
	 */
	private Object execute(ICMCheckedCallback action) {
		Object result = null;
		logger.debug("action : "+action);
		try {
			result = action.doInCallback();
		} catch (ICMDocumentNotFoundException e) {
			logger.error("Error execute()", e);
			throw e;
		} catch (ICMException e) {
			logger.error("Error execute()", e);
			throw e;
		} catch (DKUsageError e) {
			logger.error("Error execute()", e);
			throw new ICMException(e);
		} catch (DKException e) {
			logger.error("Error execute()", e);
			throw new ICMException(e);
		} catch (Exception e) {
			logger.error("Error execute()", e);
			throw new ICMException(e);
		}

		return result;
	}

}
