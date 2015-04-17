package com.ibm.bbva.log;

import java.util.Properties;

import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

import com.ibm.bbva.service.Constantes;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

public class LogServlet extends HttpServlet {

	private static final long serialVersionUID = 2850081110278688824L;

	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	
	public void init () {		

		String log4j_1  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_ROOT_LOGGER").getValorVariable();
		String log4j_2  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_CONSOLA").getValorVariable();
		String log4j_3  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_CONSOLA_LAYOUT").getValorVariable();
		String log4j_4  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_CONSOLA_LAYOUT_CONVERSION_PATTERN").getValorVariable();
		String log4j_5  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_ARCHIVO").getValorVariable();
		String log4j_6  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_ARCHIVO_FILE").getValorVariable();
		String log4j_7  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_ARCHIVO_MAXFILESIZE").getValorVariable();
		String log4j_8  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_ARCHIVO_MAXBACKUPINDEX").getValorVariable();
		String log4j_9  = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_ARCHIVO_LAYOUT").getValorVariable();
		String log4j_10 = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "LOG4J_APPENDER_ARCHIVO_LAYOUT_CONVERSION_PATTERN").getValorVariable();
				
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
	}
}