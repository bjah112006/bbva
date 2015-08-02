package com.everis.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jndi.JndiObjectFactoryBean;

public class DBUtilSpring {

    private static final Logger LOGGER = Logger.getLogger("DBUtilSpring");
    private static boolean envDEV = false;
    private static DBUtilSpring instance = new DBUtilSpring();
    private Map<String, DataSource> dataSources;

    public DBUtilSpring() {
        super();
        dataSources = new HashMap<String, DataSource>();
    }

    public static boolean isEnvDEV() {
        return envDEV;
    }

    public static void setEnvDEV(boolean envDEV) {
        DBUtilSpring.envDEV = envDEV;
    }

    public static DBUtilSpring getInstance() {
        return instance;
    }

    public void setDataSource(String jndiName, DataSource dataSource) {
        dataSources.put(jndiName, dataSource);
    }

    private DataSource createDataSource(String jndiName) {
        JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
        jndiFactory.setJndiName(jndiName);
        jndiFactory.setResourceRef(true);
        jndiFactory.setLookupOnStartup(false);
        jndiFactory.setCache(true);
        jndiFactory.setProxyInterface(DataSource.class);
        try {
            jndiFactory.afterPropertiesSet();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "No se pudo crear el objeto DataSource", e);
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "No se encuentra registrado el nombre del jndi [" + jndiName + "]", e);
        }

        return (DataSource) jndiFactory.getObject();
    }

    public DataSource getDataSource(String jndiName) {
        DataSource dataSource = null;

        if (!dataSources.containsKey(jndiName)) {
            dataSource = createDataSource(jndiName);
            dataSources.put(jndiName, dataSource);
        } else {
            dataSource = dataSources.get(jndiName);
        }

        return dataSource;
    }

}
