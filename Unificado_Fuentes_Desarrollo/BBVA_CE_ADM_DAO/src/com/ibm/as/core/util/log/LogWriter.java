package com.ibm.as.core.util.log;

import com.ibm.as.core.util.log.BaseLogger.Level;

public interface LogWriter {

	public void writeException(String context, String message, Exception e);

	public void writeException(Class classContext, String method, String message, Exception e);

	public void writeMessage(String context, Level level, String message);

	public void writeMessage(Class classContext, String method, Level level, String message);

	public Level getCurrentLevel();
}
