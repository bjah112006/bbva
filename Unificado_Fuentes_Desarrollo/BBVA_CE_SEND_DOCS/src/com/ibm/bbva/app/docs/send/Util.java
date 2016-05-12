package com.ibm.bbva.app.docs.send;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ibm.bbva.app.log.SimpleLogger;


public class Util {
	public static final String[] dirs = { "Log_TC", "Descargados_TC",
	"Transferencias_TC", "Lib_TC" };
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


	public static String getFileNameWithoutExtension(File file) {
		String fileName = "";
		int index = file.getName().lastIndexOf('.');
		if (index > 0 && index <= file.getName().length() - 2) {
			fileName = file.getName().substring(0, index);
			//System.out.println("Filename without Extension: " + fileName);
		}
		return fileName;
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

}
