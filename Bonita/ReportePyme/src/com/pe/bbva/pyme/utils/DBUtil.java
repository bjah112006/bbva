package com.pe.bbva.pyme.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.pe.bbva.pyme.listener.WFFastPymeServletContextListener;

public class DBUtil {

    private static final Logger logger = Logger.getLogger("DBUtil");
    public static final String FLAG_ACTIVACION_LDAP = "001";
    public static final String URL_LDAP = "002";
    public static final String HORAS_TRASNCURRIDAS = "003";
    public static final String ROLES_EXCLUIDOS = "009";
    public static final String FLAG_VERIFICAR_USUARIO = "011";

    private static Connection getConnection() throws SQLException {
        DriverManagerDataSource dataSource = WFFastPymeServletContextListener.getBean("dataSource");
        return dataSource.getConnection();
    }
    
    public static Map<String, String> obtenerDatosCliente(Long id) {
        Map<String, String> cliente = new LinkedHashMap<String, String>();

        try {
            Connection cn = getConnection();
            PreparedStatement ps = cn.prepareStatement("select * from fastpyme.fn_instance('', '', ?, '-1', '-1', '-1', '-1', 0, 'S');");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // cliente.put("tipoDOI", rs.getString("TIPO_DOI_CLIENTE"));
                cliente.put("numDOI", rs.getString("NUM_DOI_CLIENTE"));
                cliente.put("codigoCliente", rs.getString("CODIGO_CLIENTE"));
            } else {
                ps = cn.prepareStatement("select * from fastpyme.fn_arch_instance('', '', ?, '-1', '-1', '-1', '-1', 0, 'S');");
                ps.setLong(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    // cliente.put("tipoDOI", rs.getString("TIPO_DOI_CLIENTE"));
                    cliente.put("numDOI", rs.getString("NUM_DOI_CLIENTE"));
                    cliente.put("codigoCliente", rs.getString("CODIGO_CLIENTE"));
                }
            }
            
            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.error("obtenerAmbito", e);
        }
        return cliente;
    }
    
    public static String obtenerAmbito(String oficina) {
        String parametro = "";

        try {
            Connection cn = getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM TBL_PYME_OFICINA_AMBITO WHERE OFICINA = '" + oficina + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("AMBITO");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.error("obtenerAmbito", e);
        }
        return parametro;
    }

    public static String obtenerCentroNegocio(String territorio) {
        String parametro = "";

        try {
            Connection cn = getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM TBL_PYME_PARAMETER WHERE ID_TABLE=4 AND '|' || VAL_COLUMN2 || '|' LIKE '%|0' || right('" + territorio + "', 3) || '|%'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("VAL_COLUMN1");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.error("obtenerCentroNegocio", e);
        }
        return parametro;
    }

    public static String obtenerParametro(String table, String key) {
        String parametro = "";

        try {
            Connection cn = getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT VAL_COLUMN1 FROM TBL_PYME_PARAMETER WHERE ID_TABLE='" + table + "' AND ID_COLUMN='" + key + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("VAL_COLUMN1");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.error("obtenerParametro", e);
        }
        return parametro;
    }

    public static String obtenerParametroDetalle(String table, String key) {
        String parametro = "";

        try {
            parametro = obtenerParametroDetalle(getConnection(), table, key);
        } catch (Exception e) {
            logger.error("obtenerParametro", e);
        }
        return parametro;
    }

    public static String obtenerParametro(String key) {
        return obtenerParametro("10", key);
    }

    public static String obtenerParametroDetalle(Connection cn, String table, String key) {
        String parametro = "";

        try {
            PreparedStatement ps = cn.prepareStatement("SELECT VAL_COLUMN2 FROM TBL_PYME_PARAMETER WHERE ID_TABLE='" + table + "' AND ID_COLUMN='" + key + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                parametro = rs.getString("VAL_COLUMN2");
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.error("obtenerParametro", e);
        }
        return parametro;
    }
}
