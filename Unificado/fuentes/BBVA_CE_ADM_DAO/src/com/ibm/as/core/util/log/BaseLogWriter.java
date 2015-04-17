package com.ibm.as.core.util.log;

import java.util.Date;

import com.ibm.as.core.config.CoreProperties;
import com.ibm.as.core.config.CoreProperties.Property;
import com.ibm.as.core.util.DateUtils;
import com.ibm.as.core.util.log.BaseLogger.Level;

public class BaseLogWriter implements LogWriter {

	private static Level currentLevel=null;

	private void initLogger() {
		try {
			String levelName = CoreProperties.getProperty( Property.DEFAULT_LOG_LEVEL ).toUpperCase();

			currentLevel = Level.valueOf(levelName);
		}
		catch (Exception e) {
			currentLevel = Level.ERROR;

			writeException(this.getClass(), "initLogger", "An error ocurred initializing the logging subsystem. Default level: "+currentLevel, e);
		}
	}

	public void writeException(Class classContext, String method, String message, Exception e) {

		if ( currentLevel==null )
			initLogger();

		String context = getContext(classContext);

		if ( method==null )
			method = "?";

		writeException(context+"."+method, message, e);
	}

	public void writeException(String context, String message, Exception e) {

		if ( currentLevel==null )
			initLogger();

		Level level = Level.ERROR;
		String exCode = DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS");

		writeMessage(context, level, message);
		System.err.println( formatMessage(context, level, "[-- Init stack trace "+exCode+" --] "+e.getClass()) );
		
		e.printStackTrace( System.err );

		System.err.println( formatMessage(context, level, "[-- Finished stack trace "+exCode+" --] "+e.getClass()) );

		writeMessage(context, level, "Stack trace ["+exCode+"] logged in error stream");
	}

	public void writeMessage(String context, Level level, String message) {

		if ( currentLevel==null )
			initLogger();

		if ( currentLevel.getWeight()<level.getWeight() )
			return;

		System.out.println( formatMessage(context, level, message) );
	}

	public void writeMessage(Class classContext, String method, Level level, String message) {

		if ( currentLevel==null )
			initLogger();

		if ( currentLevel.getWeight()<level.getWeight() )
			return;

		if ( method==null )
			method = "?";

		String context = getContext(classContext);
		
		writeMessage(context+"."+method, level, message);
	}

	private String getContext(Class classContext) {

		if ( classContext==null )
			return "?";

		String context = "";

		switch (currentLevel) {
			case ERROR:
			case WARNING:
			case INFO:
				String className = classContext.getName();
				try {
					context = className.substring( className.lastIndexOf(".")+1 ); break;
				} catch (Exception e) { context = className; break; }
			case DEBUG:
			case TRACE:
				context = classContext.getCanonicalName(); break;
			default:
				context = "?";
		}

		return context;
	}
	
	private String getLevelCode(Level level) {
		String levelCode = "?";

		switch (level) {
			case ERROR:
				levelCode = "E"; break;
			case WARNING:
				levelCode = "W"; break;
			case INFO:
				levelCode = "I"; break;
			case DEBUG:
				levelCode = "D"; break;
			case TRACE:
				levelCode = "T"; break;
		}

		return levelCode;
	}

	private String formatMessage(String context, Level level, String message) {
		return "["+context+"] "+getLevelCode(level)+" "+message;		
	}
	
	public static void main(String[] a) {
		new BaseLogWriter().writeMessage("BPEL:BLAH", Level.TRACE, "TRACE en BPEL");
		new BaseLogWriter().writeMessage(BaseLogger.class, "metodoA", Level.INFO, "Este es un mensaje informativo");
		new BaseLogWriter().writeMessage(BaseLogger.class, "metodoB", Level.TRACE, "parte del codigo");
		new BaseLogWriter().writeMessage(BaseLogger.class, "metodoC", Level.WARNING, "...una advertencia");
		new BaseLogWriter().writeMessage(BaseLogger.class, "metodoD", Level.DEBUG, "Y este es un mensaje de debug");
		new BaseLogWriter().writeMessage(BaseLogger.class, "metodoE", Level.ERROR, "Y aqui uno de error");
		new BaseLogWriter().writeMessage("BPEL:BLAH", Level.ERROR, "Error en BPEL");

		new BaseLogWriter().writeException(BaseLogger.class, "metodoE", "Una exception!!!!!", new NullPointerException());
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}
}
