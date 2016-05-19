package com.ibm.bbva.app.docs.send.ftp;

import java.io.File;
import java.io.InputStream;
import java.util.Queue;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ibm.bbva.app.docs.send.ConstantesDocsSend;
import com.ibm.bbva.app.docs.send.Util;
import com.ibm.bbva.app.log.SimpleLogger;

public class FTPRunnable implements Runnable {

	private static final SimpleLogger LOG = SimpleLogger.getLogger(FTPRunnable.class);
	
	private FTPListener ftp;
	
	public FTPRunnable(FTPListener ftp) {
		this.ftp = ftp;

	}
	
	public void run() {
		LOG.info("run()");
		Queue<FTPFileWrapper> cola = FTPArchivos.colaArchivos;
		int tamanio = 0; 
		for (FTPFileWrapper file : cola) {
			File archivo = file.getFile();
			tamanio += (int)archivo.length();
		}
		//ftp.setTamanio(tamanio);
		LOG.info("Numero de archivos "+cola.size());
		int cantArchivos = 0;
		boolean errorTransfer = false;
		while (!cola.isEmpty()) {
			cantArchivos = cola.size();
			FTPFileWrapper wrapper = cola.poll();
			LOG.info("archivo : "+wrapper.getRemoteFile());
			//ftp.nuevoArchivo(wrapper.getRemoteFile());
			FTPClient fc = new FTPClient();
			LOG.info("FTPClient fc = new FTPClient();");
			InputStream is = null;
			boolean storeFileSuccess = false;
			try {
				LOG.info("Entro al try");
				fc.connect(wrapper.getHost());
				if (!FTPReply.isPositiveCompletion(fc.getReplyCode())) {
					LOG.error("Falló la conexión al FTP. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				LOG.info("Entro al try");
				LOG.info("user: "+ wrapper.getUser());
				LOG.info("Password: "+ wrapper.getPassword());
				boolean loginSuccess = fc.login(wrapper.getUser(), wrapper.getPassword());
				if (!loginSuccess) {
					LOG.error("Falló el login al FTP. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				boolean changeWDSuccess = fc.changeWorkingDirectory(ConstantesDocsSend.CARPETA_FTP);
				if (!changeWDSuccess) {
					LOG.error("Falló el cambio a la carpeta '" + ConstantesDocsSend.CARPETA_FTP + "'. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}				
				fc.setBufferSize(wrapper.getTasaKBytes() * 1024);
				LOG.info("fc.setBufferSize(f.getTasaKBytes() * 1024);");
				String nombreArchivo = wrapper.getRemoteFile();
				byte[] mybyteArray = Util.fileToByteArray(wrapper.getFile());
				LOG.info("mybyteArray : "+mybyteArray);
				/*for (String str : avanzados) {
					if (nombreArchivo.contains(str)) {
						LOG.info("es avanzado");
						URL url = new URL (servicioPDF);
						LOG.info("url : "+url);
						ConvertirArchivosService cas = new ConvertirArchivosService (url);
						ConvertirArchivos convertirArchivos = cas.getConvertirArchivos();
						mybyteArray = convertirArchivos.convertirPDF(mybyteArray, nombreArchivo);
						LOG.info("mybyteArray-avanzado : "+mybyteArray);
					}
				}*/
				is = new FTPInputStream (mybyteArray, wrapper.getPeriodo(), ftp);
				boolean setFileTypeSuccess = fc.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				if (!setFileTypeSuccess) {
					LOG.error("No se pudo cambiar el tipo a binario. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				
				storeFileSuccess = fc.storeFile(wrapper.getRemoteFile(), is);
				if (!storeFileSuccess) {
					LOG.error("Ocurrió un eror al transferir el archivo. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}
				LOG.info("RESPUESTA STOR : " + storeFileSuccess + " - " + fc.getReplyString() + " CODE : " + fc.getReplyCode());
				//fc.rename(wrapper.getRemoteFile(), wrapper.getRemoteFile() + "-available-CC");
				
				String nombreNuevo = renombraArch(nombreArchivo);
				boolean renameSuccess = fc.rename(wrapper.getRemoteFile(),nombreNuevo);
				if (!renameSuccess) {
					LOG.error("Ocurrió un eror al renombrar el archivo. Nombre: '"+nombreNuevo+"'. Código de respuesta: "+fc.getReplyCode());
					reintentarEnvio(wrapper, cola);
					continue;
				}				
				//ftp.exitoArchivo(wrapper.getRemoteFile());
			
				is.close();
				fc.logout();
				if (storeFileSuccess) {															
					if (!wrapper.getFile().delete()) {
						wrapper.getFile().deleteOnExit();
					}					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("Ocurrio una excepion y se cancelara el envio.(Ejecutanto el metodo errorEnvioFTP)", ex);
				reintentarEnvio(wrapper, cola);
				errorTransfer = true;
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
		ftp.eliminarCarpetaTemporal();
		if(cantArchivos > 0 && !(errorTransfer)){
			LOG.info("ANTES DE MOSTRAR EL MENSAJE DE FINAL DE CARGA DE DOCUMENTOS");
			ftp.mostrarMensaje();
		}else{LOG.info("NO HUBO ARCHIVOS PARA SUBIR AL FTP");}
		//ftp.cerrarVentana();
	}
	
	private String renombraArch(String nombre) {
		String cadenaFinal = ""; //cadenaTemp = "";
		/*int posCad1 = 0, posCad2;
		
		posCad1 = nombre.indexOf("HIST");
		if (posCad1 == -1) {
			cadenaFinal = nombre + "-available-CC";		    
		}else{
			posCad2 = nombre.indexOf(".");
		    cadenaTemp = nombre.substring(posCad1 - 1, posCad2 - 1);
		    cadenaFinal = nombre.substring(0, posCad1 - 1) + ".pdf-available-CC" + cadenaTemp;
		}
		for (String str : avanzados) {
			if (nombre.contains(str)) {
				cadenaFinal = cadenaFinal + "-VA";
			}
		}*/
		cadenaFinal = nombre + "-available-UN";
		return cadenaFinal;
	}
	
	private void reintentarEnvio(FTPFileWrapper wrapper, Queue<FTPFileWrapper> cola) {
		//ftp.errorArchivo(wrapper.getRemoteFile(), wrapper.getReintentos());
		if (wrapper.getReintentos() > 0) {
			wrapper.setReintentos(wrapper.getReintentos()-1);
			cola.offer(wrapper);
			LOG.error("Se vuelve a añadir el archivo a la cola");
		} else {
			LOG.error("Se terminaron los reintentos.");
		}
	}
}
