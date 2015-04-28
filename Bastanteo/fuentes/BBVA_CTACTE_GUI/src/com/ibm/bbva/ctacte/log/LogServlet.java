package com.ibm.bbva.ctacte.log;

import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

import com.ibm.bbva.ctacte.util.ParametrosSistema;

public class LogServlet extends HttpServlet {

	private static final long serialVersionUID = 2850081110278688824L;

	public void init () {
		ParametrosSistema parametros = ParametrosSistema.getInstance();
		Properties properties = parametros.getProperties(ParametrosSistema.LOG);
		//properties.put("log4j.logger.org.hibernate", "ERROR");
		
		/*Properties properties = new Properties();
		properties.put("log4j.rootLogger", "DEBUG, consola, archivo");
		
		properties.put("log4j.appender.consola", "org.apache.log4j.ConsoleAppender");
		properties.put("log4j.appender.consola.layout", "org.apache.log4j.PatternLayout");
		properties.put("log4j.appender.consola.layout.ConversionPattern", "[%t] (%F:%L) %-5p %c - %m%n");
		
		properties.put("log4j.appender.archivo", "org.apache.log4j.RollingFileAppender");
		properties.put("log4j.appender.archivo.file", "C:\\log\\server_ctacte.log");
		properties.put("log4j.appender.archivo.MaxFileSize", "1000KB");
		properties.put("log4j.appender.archivo.MaxBackupIndex", "1");
		properties.put("log4j.appender.archivo.layout", "org.apache.log4j.PatternLayout");
		properties.put("log4j.appender.archivo.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");*/
		 
		PropertyConfigurator.configure(properties);
	}

}
