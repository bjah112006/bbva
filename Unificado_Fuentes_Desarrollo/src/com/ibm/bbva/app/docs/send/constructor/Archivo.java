package com.ibm.bbva.app.docs.send.constructor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import com.ibm.bbva.app.log.SimpleLogger;

public class Archivo {
	
	private static final SimpleLogger LOG = SimpleLogger.getLogger(Archivo.class);

	public static final String EXTENSION_ARCHIVO = ".pdf";
	private static Archivo archivo;
	
	private static File descargados;
	private static File transferencias;
	private static File escaneados;
	public static String strDescargados;
	public static String strTransferencias;
	public static String separador;
	
	public Archivo () {
	}
	
	public static void iniciar (String dirDescargados, String dirTransferencias, String cleanTransferDir) throws IOException {
		descargados = crearDirectorio(dirDescargados);
		transferencias = crearDirectorio(dirTransferencias);
		strDescargados = descargados.getAbsolutePath();
		strTransferencias = transferencias.getAbsolutePath();
		separador = File.separator;
		//escaneados = crearEscaneados(descargados);
		
		if (cleanTransferDir!= null && cleanTransferDir.equals("1")) {
			limpiarDirectorios();
		}
	}
	
	private static void limpiarDirectorios() {
		try {
			if (descargados.exists()) {
				for (File archivo : descargados.listFiles()) {
					archivo.delete();
				}
			}
			if (transferencias.exists()) {
				for (File archivo : transferencias.listFiles()) {
					archivo.delete();
				}
			}
			if (escaneados.exists()) {
				for (File archivo : escaneados.listFiles()) {
					archivo.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File crearDirectorio (String directorio) {
		File dir = new File(directorio);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	public static File crearDirectorioJAR (String directorio) {
		File dir = new File(directorio);
		LOG.info("RUTA PARA CREAR EL DIRECTORIO PARA EL LOG DEL JAR :::" + dir);
		if (!dir.exists()) {
			dir.mkdirs();
			LOG.info("SE CREO EL DIRECTORIO PARA EL LOG DEL JAR :::" + dir);
		}
		return dir;
	}
		
	public File ubicarArchivo (String codigoDocumento) {
		String archivo = codigoDocumento + EXTENSION_ARCHIVO;
//		 CGC: Busca primero en transferencias porque para el CU11 
//		 pueden existir un doc con el mismo nombre en ambas carpetas, en ese caso 
//		 debe devolver primero el de transferencias 
		File temp = new File (transferencias, archivo);
		if (!temp.exists()) {
			temp = new File (descargados, archivo);
		}
		System.out.println(temp.getName());
		return temp;
	}
	
	public File crearArchivo(String codigoDocumento) throws IOException {
		return new File (transferencias, codigoDocumento + EXTENSION_ARCHIVO);
	}
	
	public void copiarArchivoATranferencias (File direccion, String nombre) throws IOException {
		FileUtils.copyFile(direccion, new File (transferencias, nombre + EXTENSION_ARCHIVO));
	}
	
	public void moverArchivoATranferencias (File direccion, String nombre) throws IOException {
		FileUtils.moveFile(direccion, new File (transferencias, nombre + EXTENSION_ARCHIVO));
	}
	
	public File obtenerArchivo(String codigo) {
		File origen = new File (escaneados, codigo + EXTENSION_ARCHIVO);
		return origen;
	}
	
	public void eliminarArchivosCarpeta (File direccion) {
		for (File archivo : direccion.listFiles()) {
			archivo.delete();
		}
	}

	public void guardarArchivo (String codigoDocumento, byte[] data) throws IOException {
		FileUtils.writeByteArrayToFile(new File (descargados, codigoDocumento + EXTENSION_ARCHIVO), data);
	}

	public static synchronized Archivo getInstance () {
		if (archivo == null) {
			archivo = new Archivo ();
		}
		return archivo;
	}
	
	public void renombrar (String nInicial, String nFinal) {
		System.out.println("renombrar "+nInicial+"  -  "+nFinal);
		File fInicial = new File (transferencias, nInicial);
		System.out.println("transferencias "+transferencias);
		System.out.println("fInicial.exists() "+fInicial.exists());
		if (fInicial.exists()) {
			System.out.println("new File(transferencias, nFinal) "+new File(transferencias, nFinal));
			fInicial.renameTo(new File(transferencias, nFinal));
		}
	}
	
	public String obtenerListaDocsTransferencias() {
		try {
			StringBuilder arch = new StringBuilder();
			File[] archs = transferencias.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".pdf");
				}
			});
			if (archs != null && archs.length > 0) {
				for (File f : archs) {
					arch.append(f.getName().substring(0, f.getName().lastIndexOf(".")));
					arch.append(",");
				}
			}
			if (!arch.toString().trim().equals("")) {
				return arch.substring(0, arch.length() - 1);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
