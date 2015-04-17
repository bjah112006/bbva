package com.ibm.as.core.util.log;

import com.ibm.as.core.config.CoreProperties;
import com.ibm.as.core.config.CoreProperties.Property;

public class BaseLogger {

	private static LogWriter writer = null;

	public enum Level {
		INFO(1), WARNING(5), ERROR(10), DEBUG(15), TRACE(20);

		private int weight;

		private Level(int weight){
			this.weight = weight;
		}

		public int getWeight(){
			return weight;
		}
	};

	private static void init() {
		try {
			String writerClassName = CoreProperties.getProperty( Property.DEFAULT_LOG_WRITER );

			writer = (LogWriter) Class.forName(writerClassName).newInstance();
		}
		catch (Exception e) {
			writer = new BaseLogWriter();
			log(BaseLogger.class, "init", "Could not load default log writer specified in core properties. Using base log writer", e);
		}
	}

	public static void log(String context, Level level, String message) {
		if ( writer==null ) init();

		writer.writeMessage(context, level, message);
	}

	public static void log(Class callingClass, String method, Level level, String message) {
		if ( writer==null ) init();

		writer.writeMessage(callingClass, method, level, message);
	}

	public static void log(Class callingClass, String method, String message, Exception e) {
		if ( writer==null ) init();

		writer.writeException(callingClass, method, message, e);
	}

	public static void log(String context, String message, Exception e) {
		if ( writer==null ) init();

		writer.writeException(context, message, e);
	}

	public static boolean isEnabled(Level level) {
		return writer.getCurrentLevel().weight==level.weight;
	}

	public static void main(String[] a) {
		BaseLogger.log("BPEL:BLAH", Level.TRACE, "TRACE en BPEL");
		BaseLogger.log(BaseLogger.class, "metodoA", Level.INFO, "Este es un mensaje informativo");
		BaseLogger.log(BaseLogger.class, "metodoB", Level.TRACE, "parte del codigo");
		BaseLogger.log(BaseLogger.class, "metodoC", Level.WARNING, "...una advertencia");
		BaseLogger.log(BaseLogger.class, "metodoD", Level.DEBUG, "Y este es un mensaje de debug");
		BaseLogger.log(BaseLogger.class, "metodoE", Level.ERROR, "Y aqui uno de error");
		BaseLogger.log("BPEL:BLAH", Level.ERROR, "Error en BPEL");

		BaseLogger.log(BaseLogger.class, "metodoE", "Una exception!!!!!", new NullPointerException());
	}
}
