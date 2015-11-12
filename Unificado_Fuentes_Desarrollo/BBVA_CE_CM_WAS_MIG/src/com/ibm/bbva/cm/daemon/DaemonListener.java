package com.ibm.bbva.cm.daemon;

import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.util.ParametrosConfUtil;

public class DaemonListener implements ServletContextListener {

	private static final long PERIODO_REPETICION_TIME_MS = 10000;
	private static Logger log = LoggerFactory.getLogger(DaemonListener.class);
	Timer timer;
	
    /**
     * Default constructor. 
     */
    public DaemonListener() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	initLog4j();
    	
    	log.info("Timer Iniciado...");
    	timer = new Timer();
    	
    	timer.schedule(new DaemonTask(), 0 , PERIODO_REPETICION_TIME_MS);
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	timer.cancel();
    	
    	//LogManager.shutdown();
    }
    
    private void initLog4j() {
    	ParametrosConfUtil params = ParametrosConfUtil.getInstance();
    	String log4j_1  = params.getValue("LOG4J_ROOT_LOGGER");
		String log4j_2  = params.getValue("LOG4J_APPENDER_CONSOLA");
		String log4j_3  = params.getValue("LOG4J_APPENDER_CONSOLA_LAYOUT");
		String log4j_4  = params.getValue("LOG4J_APPENDER_CONSOLA_LAYOUT_CONVERSION_PATTERN");
		String log4j_5  = params.getValue("LOG4J_APPENDER_ARCHIVO");
		String log4j_6  = params.getValue("LOG4J_APPENDER_ARCHIVO_FILE");
		String log4j_7  = params.getValue("LOG4J_APPENDER_ARCHIVO_MAXFILESIZE");
		String log4j_8  = params.getValue("LOG4J_APPENDER_ARCHIVO_MAXBACKUPINDEX");
		String log4j_9  = params.getValue("LOG4J_APPENDER_ARCHIVO_LAYOUT");
		String log4j_10 = params.getValue("LOG4J_APPENDER_ARCHIVO_LAYOUT_CONVERSION_PATTERN");
				
		Properties properties = new Properties();
		
		properties.put("log4j.rootLogger", log4j_1);
		
		properties.put("log4j.appender.consola", log4j_2);
		properties.put("log4j.appender.consola.layout", log4j_3);
		properties.put("log4j.appender.consola.layout.ConversionPattern", log4j_4);
		
		properties.put("log4j.appender.archivo", log4j_5);
		properties.put("log4j.appender.archivo.file", log4j_6);
		properties.put("log4j.appender.archivo.MaxFileSize", log4j_7);
		properties.put("log4j.appender.archivo.MaxBackupIndex", log4j_8);
		properties.put("log4j.appender.archivo.layout", log4j_9);
		properties.put("log4j.appender.archivo.layout.ConversionPattern", log4j_10);
		 
		PropertyConfigurator.configure(properties);
		log.info("Se cargó la configuración de log4j.");
    }
	
}
