package com.bbva.content.manager.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaemonListener implements ServletContextListener {

	private static Logger log = LoggerFactory.getLogger(DaemonListener.class);
	
    public DaemonListener() {
    }

    public void contextInitialized(ServletContextEvent arg0) {
    	initLog4j();
    }

    public void contextDestroyed(ServletContextEvent arg0) {
    }
    
    private void initLog4j() {
    	
    	String rootCategory = "DEBUG,stdout,LOGFILE";
    	String fileLog = "/pr/conele/logs";
    	String sizeLog = "1024kb";
    	String maxFilesLog = "20";
    	
        Properties prop = new Properties();
        prop.setProperty("log4j.rootCategory", rootCategory);
        prop.setProperty("log4j.appender.LOGFILE", "org.apache.log4j.RollingFileAppender");
        prop.setProperty("log4j.appender.LOGFILE.file", fileLog + "/log_bbvaContentManager.log");
        prop.setProperty("log4j.appender.LOGFILE.MaxFileSize", sizeLog);
        prop.setProperty("log4j.appender.LOGFILE.MaxBackupIndex", maxFilesLog);
        prop.setProperty("log4j.appender.LOGFILE.append", "true");
        prop.setProperty("log4j.appender.LOGFILE.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.LOGFILE.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] - [%5p] (%C{1}.%M:%L) - %m%n");
        prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{HH:mm:ss}]%p - %C{1}.%M(%L)  %m%n");
        prop.setProperty("log4j.logger.org.hibernate.SQL", "DEBUG");
        prop.setProperty("log4j.logger.org.hibernate.TYPE", "TRACE");
		 
		PropertyConfigurator.configure(prop);
		log.info("Se cargó la configuración de log4j.");
    }
	
}
