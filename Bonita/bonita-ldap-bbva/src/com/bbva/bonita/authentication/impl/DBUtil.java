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
    public static final String ROLES_EXCLUIDOS = "009";
    public static final String FLAG_VERIFICAR_USUARIO = "011";

    public static String obtenerAmbito(String oficina) {
        String parametro = "";

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM TBL_PYME_OFICINA_AMBITO WHERE OFICINA = '" + oficina + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("AMBITO");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "obtenerAmbito", e);
        }
        return parametro;
    }

    public static String obtenerCentroNegocio(String territorio) {
        String parametro = "";

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM TBL_PYME_PARAMETER WHERE ID_TABLE=4 AND '|' || VAL_COLUMN2 || '|' LIKE '%|0' || right('" + territorio + "', 3) || '|%'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("VAL_COLUMN1");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "obtenerCentroNegocio", e);
        }
        return parametro;
    }

    public static String obtenerParametro(String table, String key) {
        String parametro = "";

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT VAL_COLUMN1 FROM TBL_PYME_PARAMETER WHERE ID_TABLE='" + table + "' AND ID_COLUMN='" + key + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("VAL_COLUMN1");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "obtenerParametro", e);
        }
        return parametro;
    }

    public static String obtenerParametroDetalle(String table, String key) {
        String parametro = "";

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT VAL_COLUMN2 FROM TBL_PYME_PARAMETER WHERE ID_TABLE='" + table + "' AND ID_COLUMN='" + key + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("VAL_COLUMN2");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "obtenerParametro", e);
        }
        return parametro;
    }

    public static String obtenerParametro(String key) {
        return obtenerParametro("10", key);
    }
}
