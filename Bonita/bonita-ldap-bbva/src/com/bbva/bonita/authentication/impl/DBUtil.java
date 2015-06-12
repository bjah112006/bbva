package com.bbva.bonita.authentication.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBUtil {
	
	private static final Logger logger = Logger.getLogger("DBUtil");
	public static final String FLAG_ACTIVACION_LDAP = "001";
	public static final String URL_LDAP = "002";
	public static final String HORAS_TRASNCURRIDAS = "003";
	
	public static String obtenerParametro(String key) {
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
			Connection cn = ds.getConnection();
	    	PreparedStatement ps = cn.prepareStatement("SELECT VAL_COLUMN1 FROM PUBLIC.TBL_PYME_PARAMETER WHERE ID_TABLE='10' AND ID_COLUMN='" + key + "'");
	    	ResultSet rs = ps.executeQuery();
	    	
	    	if(rs.next()) {
	    		return rs.getString("VAL_COLUMN1");
	    	}
		} catch(Exception e) {
			logger.log(Level.SEVERE, "actualizarMembresia", e);
		}
    	return "";
	}
}