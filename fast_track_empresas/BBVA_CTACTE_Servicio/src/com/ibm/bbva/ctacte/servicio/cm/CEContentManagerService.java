package com.ibm.bbva.ctacte.servicio.cm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.DocumentoCM;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.DocumentoCMDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.servicio.cm.bean.DocumentoExpediente;
import com.ibm.bbva.ctacte.servicio.cm.util.ContentManagerUtil;
import com.ibm.bbva.ctacte.util.EJBLocator;

public class CEContentManagerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CEContentManagerService.class);
	
	public static final String EXTENSION_ARCHIVO_PDF = ".pdf";
	public static final String EXTENSION_ARCHIVO_TIF = ".tif";
	
	public DocumentoCM obtenerDocumentoCM (int idExpediente, int idDocumento){
		DocumentoCM documentoCM = new DocumentoCM();
		DocumentoCMDAO documentoCMDAO =  EJBLocator.getDocumentoCMDAO();
		try{
			documentoCM = documentoCMDAO.getDocumentoCM(idExpediente, idDocumento);
		} catch (Exception ex) {
			documentoCM = null;
		}
		return documentoCM;
	}
	
	public DocumentoCM obtenerDocumentoCMxCodigo (int idExpediente, String codigoDocumento){
		DocumentoCM documentoCM = new DocumentoCM();
		DocumentoCMDAO documentoCMDAO =  EJBLocator.getDocumentoCMDAO();
		try{
			documentoCM = documentoCMDAO.getDocumentoCMxCodigo(idExpediente, codigoDocumento);
		} catch (Exception ex) {
			documentoCM = null;
		}
		return documentoCM;
	}
	
	public String actualizarEstadoDocumento_CC (int idExpediente, int idDocumento, String flagCM, int idCM, String pidCM){
		String result = "OK";
		try {
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			documentoExp = documentoExpDAO.findByDocExp(idDocumento, idExpediente);
			documentoExp.setFlagCm(flagCM);
			documentoExp.setIdCm(idCM);
			documentoExp.setPidCM(pidCM);
			documentoExpDAO.update(documentoExp);
		} catch (Exception ex) {
			result = "KO";
			LOG.error("ERROR: actualizarEstadoDocumento_CC()", ex);
		} 
		return result;
	}
	
	public String actualizarEstadoDocumentoxCodigo_CC (int idExpediente, String codigoDocumento, String flagCM, int idCM, String pidCM){
		String result = "OK";
		try {
			LOG.info("idExpediente: "+idExpediente);
			LOG.info("codigoDocumento: "+codigoDocumento);
			LOG.info("flagCM: "+flagCM);
			LOG.info("idCM: "+idCM);
			LOG.info("pidCM: "+pidCM);
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
			documentoExp.setFlagCm(flagCM);
			if (idCM > 0)
				documentoExp.setIdCm(idCM);
			else
				documentoExp.setIdCm(null);
			documentoExp.setPidCM(pidCM);
			documentoExpDAO.update(documentoExp);
			
			if (flagCM != null && flagCM.equals("1")) {
				ExpedienteDAO expedienteDAO = EJBLocator.getExpedienteDAO();
				Expediente expediente = expedienteDAO.load(idExpediente);
				// si es pre-registro todavía no existe en el Process
				if (expediente.getEstado().getId().intValue() != ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO) {
					List<DocumentoExp> listaDocumentos = documentoExpDAO.findByExpdiente(idExpediente);
					if (listaDocumentos != null  && listaDocumentos.size() > 0) {
						boolean llamarProcess = false;
						for (DocumentoExp docExpTmp : listaDocumentos) {
							if (docExpTmp.getFlagEscaneado() != null && docExpTmp.getFlagEscaneado().equals("1")) {
								if (docExpTmp.getFlagCm() != null && docExpTmp.getFlagCm().equals("1")) {
									LOG.info("Documento Tipo "+docExpTmp.getDocumento().getCodigoDocumento()+" SI existe en el content.");
									llamarProcess = true;
								} else {
									LOG.info("Documento Tipo "+docExpTmp.getDocumento().getCodigoDocumento()+" NO existe en el content.");
									llamarProcess = false;
									break;
								}
							}
						}
						if (llamarProcess) {
							LOG.info("Se terminó de adjuntar el último documento. Llamo a ejecutarOperacionEspera().");
							RemoteUtils remoteUtils = new RemoteUtils();
							remoteUtils.ejecutarOperacionEspera(Integer.toString(idExpediente));
						} else {
							LOG.info("Todavía hay documentos en cola. No se invoca al Process.");
						}
					}
				} else {
					LOG.info("Es pre-registro. No se invoca al Process.");
				}
			}
		} catch (Exception ex) {
			result = "KO";
			LOG.error("ERROR: actualizarEstadoDocumentoxCodigo_CC()", ex);
		} 
		return result;
	}
	
	public String actualizarDocumentoExp_IdCMCopia (int idExpediente, String codigoDocumento, int idCMCopia, String pidCM){
		String result = "OK";
		try {
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
			documentoExp.setIdCmCopia(idCMCopia);
			documentoExp.setPidCM(pidCM);
			documentoExpDAO.update(documentoExp);
		} catch (Exception ex) {
			result = "KO";
			LOG.error("ERROR: actualizarDocumentoExp_IdCMCopia()", ex);
		} 
		return result;
	}
	
	public Integer obtenerDocumentoxCodigo_CC (int idExpediente, String codigoDocumento){
		int idCM;
	    DocumentoExp documentoExp = new DocumentoExp();
		DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
		try{
			//System.out.println("***************idExpediente**************"+idExpediente+" - codigoDocumento "+codigoDocumento);
			documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
			idCM = documentoExp.getIdCm();
			//System.out.println("***************documentoExp************+"+documentoExp.getIdCm() );
		} catch (Exception ex) {
			LOG.error("ERROR: obtenerDocumentoxCodigo_CC()", ex);
			idCM = 0;
		}
		return idCM;
	} 
	
	public DocumentoExpediente obtenerDocumentoExpediente_CC (int idExpediente, String codigoDocumento){
		
		DocumentoExp documentoExp = new DocumentoExp();
		DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
		DocumentoExpediente documentoExpediente = new DocumentoExpediente();
		ContentManagerUtil contentManagerUtil = new ContentManagerUtil();
		try{
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idExpediente "+idExpediente+" - codigoDocumento "+codigoDocumento);
			documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
			documentoExpediente = contentManagerUtil.copiarDocumentoExpediente(documentoExp);
			
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("documentoExpediente"+documentoExpediente.getIdCm() );
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("documentoExpediente "+documentoExpediente.getIdCmCopia() );
		} catch (Exception ex) {
			LOG.error("ERROR: obtenerDocumentoExpediente_CC()", ex);
			documentoExpediente = null;
		}
		return documentoExpediente;
	} 
	
	// no se migra creación de archivos TIFF
	/*public void crearArchivoTif(String nombre) {
		
		try {	
			//System.out.println("*****TRY-CEContentManagerService-crearArchivoTif(String nombre)******"+nombre);
			FTPClient cliente = new FTPClient();			
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			
			Runtime rt = Runtime.getRuntime();
			String inArch =  nombre+EXTENSION_ARCHIVO_PDF;
			String outArch = nombre+EXTENSION_ARCHIVO_TIF;
			String outArchFTP = nombre.replace(EXTENSION_ARCHIVO_PDF, EXTENSION_ARCHIVO_TIF);
			String descargados = Constantes.RUTA_DESCARGA_TIF;
			
			/*Recuperar de FTP* /
			byte[] data = getContentFTPFile(nombre, cliente);
			//File filePdf = new File (descargados, inArch);
			//FileUtils.writeByteArrayToFile(filePdf, data);
			
			/*Convertir de pdf a tiff* /
			
			ConvertirArchivosService convertirArchivosService = new ConvertirArchivosServiceLocator();
			URL url = new URL (Constantes.URL_CONVERTIR_ARCHIVOS);
			ConvertirArchivos convertirArchivos = convertirArchivosService.getConvertirArchivos(url);
			//System.out.println("*****ConvertirArchivos.convertirTIF(data, nombre)******"+data+" - "+nombre);
			byte[] bytes = convertirArchivos.convertirTIF(data, nombre);
			
			//String convertir = "gswin32c  -dNOPAUSE -dQUIET -r200 -sDEVICE=tiffg4 -sOutputFile=" + descargados+File.separator+outArch + " " + descargados+File.separator+inArch + " -c quit";
			////System.out.println("convertir: " + convertir);
			//Process pr = rt.exec(convertir);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Tif exec");
			//pr.waitFor();		
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Tif convertido");
		
			/*Enviar a FTP* /
			//File file = new File(descargados+File.separator+outArch);
			//FileInputStream fin = null;
			//fin = new FileInputStream(file);	
			//System.out.println("*****bytes******:"+bytes);
			ByteArrayInputStream fin = new ByteArrayInputStream(bytes);			
			conectarClienteFtp(cliente);
			autenticarCliente(cliente);
			cliente.changeWorkingDirectory(Constantes.CARPETA_FTP);
			cliente.setFileType(cliente.BINARY_FILE_TYPE);
			cliente.storeFile(outArchFTP, fin);
			fin.close();
			cliente.logout();
			cliente.disconnect();
			/*Borra los archivos del directorio* /
			//filePdf.delete();
			//file.delete();
			
			} catch(Exception e) {
				//System.out.println("*****CATCH-CEContentManagerService-crearArchivoTif(String nombre)******");
	            e.printStackTrace();
	        }
	}
	
	public static void conectarClienteFtp(FTPClient cliente)
			throws SocketException, IOException {
		if (!cliente.isConnected())
			cliente.connect(ParametersDelegate.getInstance().getValue("bbva.ftp.hostname"));	
	}

	public static void autenticarCliente(FTPClient cliente) throws IOException {
		cliente.login(ParametersDelegate.getInstance().getValue("bbva.ftp.username"),ParametersDelegate.getInstance().getValue("bbva.ftp.password"));
	}
	
	public static byte[] getContentFTPFile(String nomFile, FTPClient cliente){
		byte[] retorno = null;
		ByteArrayOutputStream 	os 	= null;
		
		try {			
			cliente.changeWorkingDirectory(Constantes.CARPETA_FTP);
			os = new ByteArrayOutputStream();
			cliente.setFileType(FTP.BINARY_FILE_TYPE);
			////System.out.println("Resultado de obtener el archivo del FTP: "+cliente.retrieveFile(nomFile, os));
			cliente.retrieveFile(nomFile, os);
			retorno = os.toByteArray();				
		} catch (Exception e) {
			e.printStackTrace();
		}finally {        
			try {            
				cliente.disconnect(); 	
				os.close();
			}catch (IOException e) { 			             
				e.printStackTrace(); 
			} 

		}
		return retorno;
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
			//LOG.error("", ex);
		} finally {
			try {
				fis.close();
				bos.close();
			} catch (IOException ex) {
				//LOG.error("", ex);
			}
		}
		return bytes;
	}*/

}
