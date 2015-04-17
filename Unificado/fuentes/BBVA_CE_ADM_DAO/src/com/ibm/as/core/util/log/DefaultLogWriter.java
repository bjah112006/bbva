package com.ibm.as.core.util.log;

import java.util.Date;

import com.ibm.as.core.util.DateUtils;
import com.ibm.as.core.util.log.BaseLogger.Level;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.bbva.tabla.vo.ParametrosConfVO;

public class DefaultLogWriter implements LogWriter {

	private static Level currentLevel=null;

	private static int retries = 0;
	
	private void initLogger() {
		try {
			/*
			 * Este if previene un potencial loop infinito
			 * debido a que el método getValue() también invoca
			 * a este LogWriter
			 */
			if ( retries++>=3 ) {
				currentLevel = Level.ERROR;
				retries = 0;
			}

			//String levelName = ParametersDelegate.getInstance().getValue("bbva.log.level").toUpperCase();
			//String levelName = ConstantesServidor.BBVA_LOG_LEVEL;

			TablaFacadeBean tablaFacadeBean = new TablaFacadeBean();					
			ParametrosConfVO param1 = tablaFacadeBean.getParametrosConf(Constantes.ID_APLICATIVO_TC, "BBVA_LOG_LEVEL");			
			String levelName = param1.getValorVariable();
			
			//System.out.println("levelName : "+levelName);
			
			currentLevel = Level.valueOf(levelName);
		}
		catch (Exception e) {
			currentLevel = Level.ERROR;

			writeException(this.getClass(), "initLogger", "Ocurrió un error inicializando el subsistema de logueo. Nivel por defecto: "+currentLevel, e);
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
		return "[WALD:"+context+"] "+getLevelCode(level)+" "+message;		
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public static void main(String[] a) {
		
		new DefaultLogWriter().writeMessage("BPEL:BLAH", Level.ERROR, "Error en BPEL");

		new DefaultLogWriter().writeException(BaseLogger.class, "metodoE", "Una exception!!!!!", new NullPointerException());
	}
}
