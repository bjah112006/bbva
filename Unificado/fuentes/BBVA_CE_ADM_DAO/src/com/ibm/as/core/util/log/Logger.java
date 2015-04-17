package com.ibm.as.core.util.log;

import com.ibm.as.core.util.log.BaseLogger;

public class Logger extends BaseLogger {

	public enum Level {ERROR, WARNING, INFO, DEBUG, TRACE};

	public static void log(String context, Level level, String message) {

		BaseLogger.Level blevel = translate(level);

		BaseLogger.log(context, blevel, message);
	}

	public static void log(Class callingClass, String method, Level level, String message) {
		BaseLogger.Level blevel = translate(level);

		BaseLogger.log(callingClass, method, blevel, message);
	}

	public static void log(Class callingClass, String method, String message, Exception e) {

		BaseLogger.log(callingClass, method, message, e);
	}
	
	public static void log(String context, String message, Exception e) {
		
		BaseLogger.log(context, message, e);
	}

	public static boolean isEnabled(Level level) {

		return BaseLogger.isEnabled( translate(level) );
	}
	
	private static BaseLogger.Level translate(Level level) {
		BaseLogger.Level blevel = null;

		switch (level) {
			case ERROR:
				blevel = BaseLogger.Level.ERROR; break;
			case WARNING:
				blevel = BaseLogger.Level.WARNING; break;
			case INFO:
				blevel = BaseLogger.Level.INFO; break;
			case DEBUG:
				blevel = BaseLogger.Level.DEBUG; break;
			case TRACE:
				blevel = BaseLogger.Level.TRACE; break;
		}

		return blevel;
	}
}
