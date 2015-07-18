package com.pe.bbva.pyme.dao.impl;

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
    public static final String URL_REPORTE_OUTPUT = "004";
    public static final String MAX_RESULT_INSTANCES = "005";
    public static final String MAX_RESULT_TASKS = "006";
    public static final String MAX_TRACE_DAYS = "007";

    public static String obtenerParametro(String key) {
        String value = "";
        try {
            InitialContext ic = new InitialContext();
            Connection cn = null;
            //
            // Class.forName("org.h2.Driver");
            // String url = "jdbc:h2:tcp://localhost:9091/bonita_journal.db";
            // cn=java.sql.DriverManager.getConnection(url, "sa", "");

            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT VAL_COLUMN1 FROM TBL_PYME_PARAMETER WHERE ID_TABLE='10' AND ID_COLUMN='" + key + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                value = rs.getString("VAL_COLUMN1");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "obtenerParametro", e);
        }
        
        return value;
    }
}
