package com.ibm.bbva.convert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.util.Constantes;
import com.ibm.bbva.cm.util.ParametrosConfUtil;

public class ConvertirArchivos {
	
	private static final Logger logger = LoggerFactory.getLogger(ConvertirArchivos.class);
	
	private String RUTA_DESCARGA_PDF = ParametrosConfUtil.getInstance().getValue(Constantes.RUTA_CONVERSION);
	private String EXTENSION_NOMBRE_TEMPORAL_PDF = "TMP";

	public byte[] convertirPDF(byte[] bytesPDF) {
		return convertir(bytesPDF, "pdfwrite");
	}

	public byte[] convertirTIFF(byte[] bytesPDF) {
		return convertir(bytesPDF, "tiffg4");
		//return convertir(bytesPDF, "tiff24nc");
	}

	private byte[] convertir(byte[] bytesPDF, String device) {
		byte[] fileReturn = null;
		try {
			// System.out.println("*****************try-ConvertirArchivos************* ");
			String nombreOut = String.valueOf(System.currentTimeMillis());
			Runtime rt = Runtime.getRuntime();
			String descargados = RUTA_DESCARGA_PDF;
			String nuevoNombre = EXTENSION_NOMBRE_TEMPORAL_PDF
					+ nombreOut;
			String nombreIn = nombreOut.replace(nombreOut, nuevoNombre);
			String fileFinal = descargados + File.separator + nombreIn;
			// System.out.println("fileFinal: "+fileFinal);
			File file = new File(fileFinal);
			FileOutputStream fin = new FileOutputStream(file);
			fin.write(bytesPDF);
			fin.close();

			/* Convertir */
			String comando = null;
			String so = System.getProperty("os.name").toLowerCase();
			if (so.indexOf("win") >= 0) {
				//comando = "gswin32c";
				comando = "gswin64c";
			} else {
				comando = "gs";
			}
			String convertir = comando + " -dNOPAUSE -dQUIET -r200 -sDEVICE="
					+ device + " -sOutputFile=" + descargados + File.separator
					+ nombreOut + " " + descargados + File.separator + nombreIn
					+ " -c quit";
			/*String convertir = comando + " -dNOPAUSE -dQUIET -r200 -sDEVICE="
					+ device + " -sOutputFile=" + descargados + File.separator
					+ nombreOut + " " + descargados + File.separator + nombreIn
					+ " -c quit"; //-sCompression=pack*/
			logger.info("convertir: " + convertir);
			Process pr = rt.exec(convertir);
			pr.waitFor();

			/* Retornar Archivo de Salida */
			File fileRetorna = new File(descargados + File.separator
					+ nombreOut);
			fileReturn = fileToByteArray(fileRetorna);

			/* Borra los archivos del directorio */
			file.delete();
			fileRetorna.delete();
		} catch (Exception e) {
			// System.out.println("*****************CATCH-ConvertirArchivos************* "+e.toString());
			logger.error("Error el convertir archivos.", e);
		}
		return fileReturn;

	}

	private byte[] fileToByteArray(File file) {

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
			logger.error("Error en fileToByteArray()", ex);
		} finally {
			try {
				fis.close();
				bos.close();
			} catch (IOException ex) {
				logger.warn("Error cerrando streams.", ex);
			}
		}
		return bytes;
	}

}
