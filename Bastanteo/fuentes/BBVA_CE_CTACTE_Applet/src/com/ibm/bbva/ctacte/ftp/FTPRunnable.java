package com.ibm.bbva.ctacte.ftp;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Queue;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ibm.bbva.ctacte.applet.FTPSingle;
import com.ibm.bbva.ctacte.applet.Util;
import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.log.SimpleLogger;

public class FTPRunnable implements Runnable {

	private static final SimpleLogger LOG = SimpleLogger.getLogger(FTPRunnable.class);
	
	private FTPListener ftp;
	private HashSet<String> avanzados;
	private String servicioPDF;
	private boolean esReenvio;
	
	public FTPRunnable(FTPListener ftp, HashSet<String> avanzados, String servicioPDF) {
		this.ftp = ftp;
		this.avanzados = avanzados;
		this.servicioPDF = servicioPDF;
		this.esReenvio = false;
	}
	
	public FTPRunnable(FTPListener ftp) {
		this.ftp = ftp;
		this.avanzados = null;
		this.servicioPDF = null;
		this.esReenvio = true;
	}
	
	public void run() {
		LOG.info("run()");
		Queue<FTPFileWrapper> cola = FTPArchivos.colaArchivos;
		int tamanio = 0;
		for (FTPFileWrapper file : cola) {
			File archivo = file.getFile();
			tamanio += (int)archivo.length();
		}
		ftp.setTamanio(tamanio);
		LOG.info("Numero de archivos "+cola.size());
		
		while (!cola.isEmpty()) {
			FTPFileWrapper wrapper = cola.poll();
			LOG.info("archivo : "+wrapper.getRemoteFile());
			ftp.nuevoArchivo(wrapper.getRemoteFile());
			//wrapper.setReintentos(wrapper.getReintentos()-1);
			FTPClient fc = new FTPClient();
			LOG.info("FTPClient fc = new FTPClient();");
			InputStream is = null;
			try {
				fc.connect(wrapper.getHost());
				if (!FTPReply.isPositiveCompletion(fc.getReplyCode())) {
					LOG.error("Falló la conexión al FTP. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				boolean loginSuccess = fc.login(wrapper.getUser(), wrapper.getPassword());
				if (!loginSuccess) {
					LOG.error("Falló el login al FTP. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				boolean changeWDSuccess = fc.changeWorkingDirectory(wrapper.getCarpetaFTP());
				if (!changeWDSuccess) {
					LOG.error("Falló el cambio a la carpeta '"+wrapper.getCarpetaFTP()+"'. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				fc.setBufferSize(wrapper.getTasaKBytes() * 1024);
				String nombreArchivo = wrapper.getRemoteFile();
				byte[] mybyteArray = Util.fileToByteArray(wrapper.getFile());
				LOG.info("mybyteArray : "+mybyteArray);
//				for (String str : avanzados) {
//					if (nombreArchivo.contains(str)) {
//						LOG.info("es avanzado");
//						URL url = new URL (servicioPDF);
//						LOG.info("url : "+url);
//						ConvertirArchivosService cas = new ConvertirArchivosService (url);
//						ConvertirArchivos convertirArchivos = cas.getConvertirArchivos();
//						mybyteArray = convertirArchivos.convertirPDF(mybyteArray, nombreArchivo);
//						LOG.info("mybyteArray-avanzado : "+mybyteArray);
//					}
//				}
				is = new FTPInputStream (mybyteArray, wrapper.getPeriodo(), ftp);
				boolean setFileTypeSuccess = fc.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				if (!setFileTypeSuccess) {
					LOG.error("No se pudo cambiar el tipo a binario. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				boolean storeFileSuccess = fc.storeFile(wrapper.getRemoteFile(), is);
				if (!storeFileSuccess) {
					LOG.error("Ocurrió un eror al transferir el archivo. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				LOG.info("RESPUESTA STORE : " + storeFileSuccess + " - " + fc.getReplyString() + " CODE : " + fc.getReplyCode());
				
				if (!esReenvio) {
					String nombreNuevo = renombraArch(nombreArchivo);
					boolean renameSuccess = fc.rename(wrapper.getRemoteFile(), nombreNuevo);
					if (!renameSuccess) {
						LOG.error("Ocurrió un eror al renombrar el archivo. Nombre: '"+nombreNuevo+"'. Código de respuesta: "+fc.getReplyCode());
						reintentarEnvio(wrapper, cola);
						continue;
					}
					
					ftp.exitoArchivo(wrapper.getRemoteFile());
					// copio el archivo a carpeta de backup
					Archivo.getInstance().copiarArchivoABackup(wrapper.getFile(), nombreNuevo);
				}
				
				is.close();
				fc.logout();
				
				if (!esReenvio) {
					String rutaArchivo = wrapper.getFile().getAbsolutePath();
					if (!wrapper.getFile().delete()) {
						wrapper.getFile().deleteOnExit();
					}
					// cuando se envió un solo archivo también hay que eliminar el histórico.
					if (ftp instanceof FTPSingle) {
						rutaArchivo = rutaArchivo.substring(0, rutaArchivo.lastIndexOf(".")) + "-HIST" + rutaArchivo.substring(rutaArchivo.lastIndexOf("."));
						LOG.info("Ruta del archivo Histórico: "+rutaArchivo);
						File fileHist = new File(rutaArchivo);
						if (fileHist.exists()) {
							if (!fileHist.delete()) {
								fileHist.deleteOnExit();
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("La transferencia al FTP falló con excepción.", ex);
				reintentarEnvio(wrapper, cola);
			} finally {
				try {
					fc.disconnect();
					if (is != null) {
						is.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			LOG.info("Quedan "+cola.size()+" archivos");
		}
		ftp.cerrarVentana();
		
	}
	
	private String renombraArch(String nombre) {
		String cadenaFinal = "";
		int posCad1 = 0;
		int posCad2 = 0;
		
		posCad1 = nombre.indexOf("HIST");
		posCad2 = nombre.indexOf("AN");
		if (posCad1 == -1 && posCad2 == -1) {
			cadenaFinal = nombre + "-available-CC";		    
		}else if (posCad1 >= 0) {
			int posCad3 = nombre.indexOf(".");
		    String cadenaTemp = nombre.substring(posCad1 - 1, posCad3 - 1);
		    cadenaFinal = nombre.substring(0, posCad1 - 1) + ".pdf-available-CC" + cadenaTemp;
		}else if (posCad2 >= 0) {
			cadenaFinal = nombre.substring(0, posCad2 - 1) + ".pdf-available-CC-AN";
		}
		for (String str : avanzados) {
			if (nombre.contains(str) && !cadenaFinal.contains("-AN")) {
				cadenaFinal = cadenaFinal + "-VA";
			}
		}
		return cadenaFinal;
	}
	
	private void reintentarEnvio(FTPFileWrapper wrapper, Queue<FTPFileWrapper> cola) {
		ftp.errorArchivo(wrapper.getRemoteFile(), wrapper.getReintentos());
		if (wrapper.getReintentos() > 0) {
			wrapper.setReintentos(wrapper.getReintentos()-1);
			cola.offer(wrapper);
			LOG.error("Se vuelve a añadir el archivo a la cola");
		} else {
			LOG.error("Se terminaron los reintentos.");
		}
	}
}
