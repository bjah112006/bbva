package com.ibm.bbva.ctacte.applet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import com.ibm.bbva.ctacte.log.SimpleLogger;

public class Util {

	public static final String[] dirs = { "Log_TC", "Descargados_TC",
			"Transferencias_TC" };
	private static final SimpleLogger LOG = SimpleLogger.getLogger(Util.class);
	private static final char JAVA_MIN_VERSION = '6';

	public static String DIR_BASE() {
		File fileD = new File("D:\\ContratacionElectronica");
		if (fileD.exists()) {
			return "D:\\ContratacionElectronica";
		} else {
			return "C:\\ContratacionElectronica";
		}
	}

	public static void truncateTable(Table t) {
		t.reset();
		Map<String, Object> row = null;
		try {
			while (null != (row = t.getNextRow())) {
				try {
					t.deleteCurrentRow();
				} catch (IOException ex) {
					LOG.error("Excepcion en delete", ex);
				}
			}
		} catch (IOException ex) {
			LOG.error("Excepcion en While", ex);
		}
		t.reset();
	}

	public static String getFileNameWithoutExtension(File file) {
		String fileName = "";
		int index = file.getName().lastIndexOf('.');
		if (index > 0 && index <= file.getName().length() - 2) {
			fileName = file.getName().substring(0, index);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Filename without Extension: " + fileName);
		}
		return fileName;
	}

	public static void truncateTable(String pathmdb, String tableName) {
		LOG.info("BORRANDO LA TABLA : " + tableName);
		try {
			Database db = Database.open(new File(pathmdb));
			Table t = db.getTable(tableName);
			truncateTable(t);
			db.close();
		} catch (IOException ex) {
			LOG.error("", ex);
		}
		LOG.info("FIN BORRANDO LA TABLA : " + tableName);
	}

	public static void insertPipeToTable(String pathmdb, String tableName,
			String pipe) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("INSERTANDO EL PIPE");
		try {
			Database db = Database.open(new File(pathmdb));
			Table t = db.getTable(tableName);
			insertPipeToTable(t, pipe);
			db.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("FIN INSERTANDO EL PIPE");
	}

	public static void insertPipeToTable(Table t, String pipe) {
		String[] rowsPipe = pipe.split(";");
		List<Object[]> lista = new ArrayList<Object[]>();
		for (int i = 0; i < rowsPipe.length; i++) {
			try {
				t.addRow(rowsPipe[i].split("\\|"));
			} catch (IOException ex) {
				ex.printStackTrace();
				Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}
	}

	public static void doAccessTable(String pathmdb, String pipe) {
		truncateTable(pathmdb, "Tipos_Documento");
		insertPipeToTable(pathmdb, "Tipos_Documento", pipe);
	}

	public static byte[] fileToByteArray(File file) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		byte[] bytes = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			bytes = bos.toByteArray();
		} catch (Exception ex) {
			LOG.error("", ex);
		} finally {
			try {
				fis.close();
				bos.close();
			} catch (IOException ex) {
				LOG.error("", ex);
			}
		}
		return bytes;
	}
	
	public static void byteArrayToFile(File file, byte[] bytes) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream (new FileOutputStream(file));
			bos.write(bytes);
			bos.flush();
		} catch (Exception e) {
			LOG.error("No se pudo convertir el archivo", e);
		} finally {
			try {
				if (bos != null)
				bos.close();
			} catch (IOException ex) {
				LOG.error("", ex);
			}
		}
	}

	public static void builDirHerachy() {
		createBaseDir();
		createOtherDirs();
	}

	private static void createBaseDir() {
		File baseDir = new File(DIR_BASE());
		if (!baseDir.exists()) {
			baseDir.mkdir();
		}
	}

	private static void createOtherDirs() {
		for (String dir : dirs) {
			File dirN = new File(DIR_BASE() + File.separator + dir);
			if (!dirN.exists()) {
				dirN.mkdir();
			}
		}
	}

	/*public static void checkJavaVersion(FileBrowser applet) throws Exception {
		String version = System.getProperty("java.version");
		char ver = version.charAt(0);
		char minor = version.charAt(2);
		LOG.info("Version Java : " + version);
		if (ver == '1' && minor < JAVA_MIN_VERSION) {
			throw new Exception("Version no soportada(" + version
					+ "), actualizar el Java a " + JAVA_MIN_VERSION + "+");
		}
	}*/

}
