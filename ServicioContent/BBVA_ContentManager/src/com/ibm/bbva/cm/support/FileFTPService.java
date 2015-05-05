package com.ibm.bbva.cm.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.util.Constantes;
import com.ibm.bbva.cm.util.ParametrosConfUtil;

public class FileFTPService {

	private static final Logger logger = LoggerFactory.getLogger(FileFTPService.class);

	public static FTPFile[] listarArchivosEnRepo() {

		FTPClient cliente = new FTPClient();
		try {
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			String carpetaFTP = leerPropiedad("app.carpetaFTP");
			logger.info("carpetaFTP: " + carpetaFTP);
			boolean blnChange = cliente.changeWorkingDirectory(carpetaFTP);
			logger.info("carpetaFTP: result changeWorkingDirectory = {}", blnChange);
			return cliente.listFiles();
		} catch (Exception e) {
			logger.error("Error listarArchivosEnRepo()", e);
			return null;
		}

	}

	public static void deleteFTPFile(String name) {
		FTPClient cliente = new FTPClient();
		try {
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			String carpetaFTP = leerPropiedad("app.carpetaFTP");
			logger.info("carpetaFTP: " + carpetaFTP);
			boolean blnChange = cliente.changeWorkingDirectory(carpetaFTP);
			logger.info("carpetaFTP: result changeWorkingDirectory = {}", blnChange);
			cliente.deleteFile(name);
		} catch (IOException e) {
			logger.error("Error deleteFTPFile()", e);
		} finally {
			try {
				cliente.disconnect();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	public static boolean storeFTPFile(String remote, ByteArrayInputStream local) {
		FTPClient cliente = new FTPClient();
		boolean resultado = false;
		try {
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			String carpetaFTP = leerPropiedad("app.carpetaFTP");
			logger.info("carpetaFTP: " + carpetaFTP);
			boolean blnChange = cliente.changeWorkingDirectory(carpetaFTP);
			logger.info("carpetaFTP: result changeWorkingDirectory = {}", blnChange);
			cliente.setFileType(FTPClient.BINARY_FILE_TYPE);
			resultado = cliente.storeFile(remote, local);
		} catch (IOException e) {
			resultado = false;
			logger.error("Error deleteFTPFile()", e);
		} finally {
			try {
				local.close();
				cliente.disconnect();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return resultado;
	}

	public static byte[] getContentFTPFile(String nomFile) {
		byte[] retorno = null;
		ByteArrayOutputStream os = null;
		FTPClient cliente = new FTPClient();
		try {
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			String carpetaFTP = leerPropiedad("app.carpetaFTP");
			logger.info("carpetaFTP: " + carpetaFTP);
			boolean blnChange = cliente.changeWorkingDirectory(carpetaFTP);
			logger.info("carpetaFTP: result changeWorkingDirectory = {}", blnChange);
			os = new ByteArrayOutputStream();
			cliente.setFileType(FTP.BINARY_FILE_TYPE);
			boolean resultado = cliente.retrieveFile(nomFile, os);
			logger.info("Resultado de obtener el archivo del FTP: " + resultado);
			logger.info("Codigo de respuesta de la conexion al FTP: "
					+ cliente.getReplyCode());
			if (resultado) {
				retorno = os.toByteArray();
			} else {
				retorno = null;
				logger.info("El archivo " + nomFile
						+ " no se pudo leer. No se enviará al content.");
			}
		} catch (Exception e) {
			logger.error("Error getContentFTPFile()", e);
			retorno = null;
		} finally {
			try {
				cliente.disconnect();
				os.close();
			} catch (IOException e) {
				logger.error("", e);
			}

		}
		return retorno;
	}

	public static void conectarClienteFtp(FTPClient cliente)
			throws SocketException, IOException {
		if (!cliente.isConnected())
			cliente.connect(leerPropiedad(Constantes.FTP_HOST));
		// cliente.connect("9.6.97.71");
	}

	public static void autenticarCliente(FTPClient cliente) throws IOException {
		cliente.login(leerPropiedad(Constantes.FTP_USER),
				leerPropiedad(Constantes.FTP_PASS));
		// cliente.login("ibmuser","1234");
	}

	public static boolean renombrarFTPFile(String fromName, String toName) {
		FTPClient cliente = new FTPClient();
		boolean resultado = false;
		try {
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			String carpetaFTP = leerPropiedad("app.carpetaFTP");
			logger.info("carpetaFTP: " + carpetaFTP);
			boolean blnChange = cliente.changeWorkingDirectory(carpetaFTP);
			if (blnChange) {
				resultado = cliente.rename(fromName, toName);
				if (!resultado) {
					logger.info("Codigo de respuesta de la conexion al FTP: "
							+ cliente.getReplyCode());
					logger.info("Cadena de respuesta de la conexion al FTP: "
							+ cliente.getReplyString());
				}
			} else {
				logger.warn("No se pudo cambiar el directorio a " + carpetaFTP);
				resultado = false;
			}
		} catch (Exception e) {
			logger.error(
					"Ocurrió un error al renombrar el archivo " + fromName, e);
			resultado = false;
		}
		return resultado;
	}

	// public static String leerPropiedad(String nombreProp) {
	// Properties properties = new Properties();
	// InputStream is = null;
	// try {
	// is = new FileInputStream("/appContext.properties");
	// properties.load(is);
	// } catch (IOException e) {
	// logger.error("", e);
	// } finally {
	// if (null != is)
	// try {
	// is.close();
	// } catch (IOException e) {
	// logger.error("", e);
	// }
	// }
	//
	// return (String) properties.get(nombreProp);
	// }

	public static String leerPropiedad(String nombreProp) {
		// return ParametersDelegate.getInstance().getValue(nombreProp);
		// return ResourceBundle.getBundle("/appContext").getString(nombreProp);
		// //FIXME
		return ParametrosConfUtil.getInstance().getValue(nombreProp);
	}

}