package com.ibm.bbva.tabla.core.dao;

import java.sql.Connection;


import com.ibm.bbva.tabla.core.db.DefaultConnectionManager;
import com.ibm.bbva.tabla.core.dao.GenericDAO;
import com.ibm.as.core.exception.GenericException;

public abstract class BaseDAO extends GenericDAO {

	protected Connection getConnection() throws GenericException {
		return DefaultConnectionManager.getInstance().getConnection();
	}

	protected void refreshConnection() throws GenericException {
		DefaultConnectionManager.getInstance().refreshConnection();
	}

	public String getSchema() {
		return "";
	}

	
}
