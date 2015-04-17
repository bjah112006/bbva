package com.ibm.bbva.app.log;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {

	private static SimpleLog simpleLog;
	private static MessageFormat messageFormat;
	private static SimpleDateFormat simpleDateFormat;
	private static boolean desarrollo;

	private String clase;

	private SimpleLogger(String clase) {
		this.clase = clase;
	}

	public static SimpleLogger getLogger(Class<?> clase) {
		SimpleLogger simpleLogger = new SimpleLogger(clase.getName());
		return simpleLogger;
	}

	public static void iniciar(File carpeta, boolean desarrollo) {
		SimpleLogger.desarrollo = desarrollo;
		simpleLog = SimpleLog.getInstance();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		simpleLog.iniciar(carpeta, desarrollo, 1000000);
		messageFormat = new MessageFormat("{0}  -  [{1}] {2} : {3}");
	}

	public void info(Object mensaje) {
		info(mensaje.toString());
	}

	public void info(String mensaje) {
		if (desarrollo) {
			simpleLog
					.println(messageFormat.format(createData("INFO", mensaje)));
		}
		System.out.println("INFO  " + mensaje);
	}

	public void info(String mensaje, Throwable th) {
		if (desarrollo) {
			simpleLog.println(
					messageFormat.format(createData("INFO", mensaje)), th);
		}
		System.out.println("INFO  " + mensaje);
		th.printStackTrace(System.out);
	}

	public void error(String mensaje) {
		simpleLog.println(messageFormat.format(createData("ERROR", mensaje)));
		System.out.println("ERROR  " + mensaje);
	}

	public void error(String mensaje, Throwable th) {
		simpleLog.println(messageFormat.format(createData("ERROR", mensaje)),
				th);
		System.out.println("ERROR  " + mensaje);
		th.printStackTrace(System.out);
	}

	private Object[] createData(String nivel, String mensaje) {
		Object[] result = new Object[4];
		result[0] = simpleDateFormat.format(new Date());
		result[1] = nivel;
		result[2] = clase;
		result[3] = mensaje;
		return result;
	}
}
