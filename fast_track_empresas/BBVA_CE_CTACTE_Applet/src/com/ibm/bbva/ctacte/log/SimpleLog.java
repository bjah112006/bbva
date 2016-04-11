package com.ibm.bbva.ctacte.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLog {

	private static SimpleLog simpleLog;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy(HH-mm-ss)");
	private File carpeta;
	private File archivo;
	private int tamArchivo;
	private PrintStream consola;
	private boolean desarrollo;
	private boolean iniciado;
	private StringBuilder builder;
	
	private SimpleLog (){}
	
	public static SimpleLog getInstance () {
		if (simpleLog==null) {
			simpleLog = new SimpleLog ();
		}
		return simpleLog;
	}

	public void iniciar (File carpeta, boolean desarrollo, int tamArchivo) {
		this.desarrollo = desarrollo;
		if (desarrollo) {
			consola = System.out;
		}
		this.carpeta = carpeta;
		this.tamArchivo = tamArchivo;
		iniciado = true;
	}

	public void println(String mensaje) {
		if (iniciado) {
			printlnArchivo(mensaje);
			printlnConsola(mensaje);
		} else {
			printlnBuffer(mensaje);
			printlnConsola(mensaje);
		}
	}

	private void printlnArchivo(String mensaje) {
		if (builder!=null) {
			escribirArchivo(false, builder.toString());
		}
		escribirArchivo(true, mensaje);
	}

	private void printlnConsola(String mensaje) {
		if (desarrollo) {
			consola.println(mensaje);
		}
	}

	private void printlnBuffer(String mensaje) {
		if (builder==null) {
			builder = new StringBuilder ();
		}
		builder.append(mensaje).append("\n");
	}
	
	public void println(String mensaje, Throwable th) {
		if (iniciado) {
			printlnArchivo(mensaje, th);
			printlnConsola(mensaje, th);
		} else {
			printlnBuffer(mensaje, th);
			printlnConsola(mensaje, th);
		}
	}

	private void printlnArchivo(String mensaje, Throwable th) {
		if (builder!=null) {
			escribirArchivo(false, builder.toString());
			builder = null;
		}
		escribirArchivo(true, mensaje, th);
	}

	private void printlnConsola(String mensaje, Throwable th) {
		if (desarrollo) {
			consola.println(mensaje);
			th.printStackTrace(consola);
		}
	}

	private void printlnBuffer(String mensaje, Throwable th) {
		if (builder==null) {
			builder = new StringBuilder ();
		}
		builder.append(mensaje).append("\n");
		builder.append(th.toString()).append("\n");
	}
	
	private void escribirArchivo (boolean salto, String texto) {
		escribirArchivo (salto, texto, null);
	}
	
	private void escribirArchivo (boolean salto, String texto, Throwable throwable) {
		try {
			if (archivo == null) {
				archivo = crearArchivo();
			}
			if(archivo.length() > tamArchivo){
				archivo = cambiarArchivo();
			}
			FileWriter fileWriter = new FileWriter(archivo, true);
			PrintWriter printWriter = new PrintWriter (fileWriter);
			if (salto) {
				printWriter.println(texto);
			} else {
				printWriter.print(texto);
			}
			if (throwable != null) {
				throwable.printStackTrace(printWriter);
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File cambiarArchivo (){
		File[] archs = carpeta.listFiles();
		if (archs.length > 3) {
			File archtemp = archs[0];
			for (int i=1; i<archs.length; i++){
				File archlog = archs[i];
				if (archlog.lastModified() < archtemp.lastModified()){
					archtemp = archlog;
				}				
			}
			archtemp.delete();
		}
		
		Date date = new Date();
		String dateTime = dateFormat.format(date);
		archivo.renameTo(new File(carpeta, dateTime+".log"));
		
		return crearArchivo ();
	}
	
	private File crearArchivo () {
		File file = new File(carpeta, "out.log");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
