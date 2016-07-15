package com.ibm.bbva.app.applet.constructor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;

import com.ibm.bbva.app.applet.Util;
import com.ibm.bbva.app.log.SimpleLogger;
import com.ibm.bbva.app.servicio.web.WEBCEService;
import com.ibm.bbva.app.servicio.web.WEBCEServiceService;
import com.ibm.bbva.cm.bean.Documento;

public class AppApplet extends ArchivoApplet {

	private static final long serialVersionUID = 1L;
	private static final SimpleLogger LOG = SimpleLogger.getLogger(AppApplet.class);
	private static final String URL_JAVA_SCRIPT = "javascript:";
	
	private enum Accion {ELIMINAR_DOCUMENTO, ABRIR_DOCUMENTO, RENOMBRAR_DOCUMENTO}
	
	private Timer timer;
	private Accion accion;
	private String codigoDocumento;
	private String strCodigos;
	private String servidor;
	private String puerto;
	private String transferencias;
	private String rutaOrigen;
	private String nombreArchivo;
	private String extencionArchivo;
	private String rutaDestino;
	private String proxyIP;
	private String proxyPuerto;
	
	public AppApplet() {
		timer = new Timer ();
		timer.schedule(new TimerTask (){
			@Override
			public void run() {
				if ((codigoDocumento!=null && !"".equals(codigoDocumento)) || 
						(getStrCodigos()!=null && !"".equals(getStrCodigos()))) {
					ejecutarAccion ();
				}
			}}, 0, 5);
	}
	
	private synchronized void ejecutarAccion () {
		if (Accion.ABRIR_DOCUMENTO.equals(accion)) {
			try {
				Archivo.getInstance().abrirArchivo(codigoDocumento);
			} catch (Exception e) {
				e.printStackTrace();
			}
			codigoDocumento = null;
		} else if (Accion.ELIMINAR_DOCUMENTO.equals(accion)) {
			try {
				System.out.println("anter de ELIMINAR el archivo-->"+codigoDocumento);
				Archivo.getInstance().eliminarArchivo(codigoDocumento);
				if(Archivo.getInstance().obtenerListaDocsTransferencias() != null){
					ejecutarFuncion("actualizarAppletYPanelDocumentos", Archivo.getInstance().obtenerListaDocsTransferencias());
				}
				else{
					ejecutarFuncion("actualizarAppletYPanelDocumentos");
				}
			} catch (IOException e) {
				mensaje("No se puede eliminar el archivo.");
			}
			codigoDocumento = null;
		} else if (Accion.RENOMBRAR_DOCUMENTO.equals(accion)) {
			try {
	        	if (strCodigos!=null) {
	    	    	StringTokenizer tokenizer = new StringTokenizer (strCodigos, ";");
	    	    	while (tokenizer.hasMoreTokens()) {
	    	    		String parDocumento = tokenizer.nextToken();
	    	    		String nombreArchivo = parDocumento.substring(0,parDocumento.indexOf(":"));
	    	    		String nombreArchivoNuevo = parDocumento.substring(parDocumento.indexOf(":")+1,parDocumento.length());
	    	    		Archivo.getInstance().renombrar(nombreArchivo, nombreArchivoNuevo);
	    	    	}
	        	}
			} catch (Exception e1) {
				LOG.error("No se puede renombrar el archivo", e1);
				mensaje("No se puede renombrar el archivo");
			}
			strCodigos = null;
		}
	}
	
	public void init () {
    	super.init();
    	servidor = getParameter(Parametros.SERVIDOR_SERV_WEB);
    	LOG.info("servidor: " + servidor);
    	puerto = getParameter(Parametros.PUERTO_SERV_WEB);
    	LOG.info("puerto: " + puerto);
    	transferencias = getParameter(Parametros.CARPETA_CLIENTE_TRANSFERENCIAS);
    	LOG.info("transferencias: " + transferencias);
    	rutaOrigen = getParameter("rutaOrigenDescargaJar");
    	LOG.info("rutaOrigen: " + rutaOrigen);
    	rutaDestino = getParameter("rutaDestinoDescargaJar");
    	LOG.info("rutaDestino: " + rutaDestino);
    	nombreArchivo = getParameter("nombreJar");
    	LOG.info("nombreArchivo: " + nombreArchivo);
    	extencionArchivo = getParameter("extencionJar");
    	LOG.info("extencionArchivo: " + extencionArchivo);
    	proxyIP = getParameter("proxyEverisIP");
    	LOG.info("proxyIP: " + proxyIP);
    	proxyPuerto = getParameter("proxyEverisPuerto");
    	LOG.info("proxyPuerto: " + proxyPuerto);
    	
    	//LOG.info("Iniciando el applet");
//    	String abrirArchivo = getParameter(Parametros.ABRIR_ARCHIVO);
//    	if (abrirArchivo != null && !abrirArchivo.equals("")) {
//	    	try {
//	    		LOG.info("abrir archivo : "+abrirArchivo);
//				Archivo.getInstance().abrirArchivo(abrirArchivo);
//			} catch (IOException e) {
//				mensaje("No se puede abrir el PDF");
//				LOG.error("No se puede abrir el PDF", e);
//			}
//			abrirArchivo = null;
//    	}
    	
    	//Descargar el Jar de transferncia 
		String resultado="";
		try {
			LOG.info("ANTES DE DESCARGAR EL JAR A LA RUTA LOCAL:::");
			resultado = downloadFile(rutaOrigen, nombreArchivo, extencionArchivo, rutaDestino, proxyIP, proxyPuerto);
		} catch (Exception e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}
		LOG.info("EL ARCHIVO SE DESCARGO EN RUTA::: " +resultado);
		
		LOG.info("Applet iniciado");
    }
	
	private String downloadFile(String rutaOrigen, String filenamePrefix, String fileExtension, String rutaDestino, String proxyIP, String proxyPuerto) throws Exception{
		LOG.info("ENTRO AL downloadFile:::");	
		
        if (rutaOrigen.trim().equals("")) throw new NullPointerException("RUTA ORIGEN NO SE PUDO OBTENER O NO EXISTE JAR EN ESA RUTA");
        
        //rutaOrigen="http://118.180.188.18/BBVA_CE_GUI/applet/";
		//filenamePrefix="BBVASendDocsFTP"; 
	    //fileExtension=".jar";
		//rutaDestino="D:\\ContratacionElectronica\\Lib_TC\\";
		File downloadedFile = null;
        
        File dir = new File(rutaDestino);
		
		if (!dir.exists()) {
			dir.mkdirs();
			LOG.info("SE CREO LA CARPETA DE LIB_TC:::" + dir);	
		}
 
        URL fileToDownload = null;
		//File fileToDownload = null;
		try {
			fileToDownload = new URL(rutaOrigen+filenamePrefix+fileExtension);
			LOG.info("DIRECCION URL::: "+ fileToDownload);
			//File downloadedFile = new File(rutaDestino + fileToDownload.getFile().replaceFirst("/|\\\\", ""));
			//fileToDownload = new File(rutaOrigen+filenamePrefix+fileExtension);
			//FileInputStream in = new FileInputStream(fileToDownload);
			downloadedFile = new File(rutaDestino + filenamePrefix+fileExtension);
			//downloadedFile = new File("D:\\ContratacionElectronica\\Lib_TC\\" + filenamePrefix+fileExtension);
			LOG.info("downloadedFile:::"+ downloadedFile);
	        if (downloadedFile.canWrite() == false) downloadedFile.setWritable(true);
	        
	        
	        /*
	         * El APPLET RECIBE EL PARAMETRO DESDE CONTRATACION SENCILLA QUE 
	         * CONTIENE EL NOMBRE DE LA ULTIMA VERSION DEL JAR DE CARGA
	         * SE COMPARA SI EL JAR CON ESE NOMBRE YA EXISTE EN LA CARPETA DE DESCARGA DEL JAR,
	         * EN CASO NO EXISTE SE DEBE DESCARGAR ESE JAR DE LA ULTIMA VERSION,
	         * SI YA EXISTE NO SE HACE NADA
	         * CABE MENSIONAR QUE EL NOMBRE DE LA ULTIMA VERSION DE JAR SE PARAMETRIZA EN TABLA TBL_CE_IBM_PARAMETROS_CONF
	         * Y SE DEBE GENERAR EL EAR DE CS CON EL JAR CON EL MISMO NOMBRE QUE SE PARAMETRIZO EN LA TABLA
	         * EL JAR LUEGO DE HABERSE GENERADO DEBE SER COPIADO EN LA RUTA
	         * http://IP_WAS/BBVA_CE_GUI/applet/
	         * */
	        if (!downloadedFile.exists()) {
	        	File downloadedDirectory = new File(rutaDestino);
		    	if (downloadedDirectory.exists() && downloadedDirectory.isDirectory()) {
					LOG.info("EXISTE DIRECTORIO:::" + downloadedDirectory);
					for (File archivo : downloadedDirectory.listFiles()) {
						archivo.delete();
						LOG.info("ARCHIVO " + archivo.getName() + " ELIMINADO");
					}
				}
		    	
	        	LOG.info("EL JAR NO EXISTE Y SE PROCEDE A DESCARGARLO");
	        	HttpClient client = new DefaultHttpClient();
		        BasicHttpContext localContext = new BasicHttpContext();
		        
		        /*DefaultHttpClient client = new DefaultHttpClient();
		        Choose BASIC over DIGEST for proxy authentication
		        List<String> authpref = new ArrayList<String>();
		        authpref.add(AuthPolicy.BASIC);
		        authpref.add(AuthPolicy.DIGEST);
		        client.getParams().setParameter(AuthPNames.CREDENTIAL_CHARSET , authpref);*/
		        
		        /*HttpParams params = new BasicHttpParams();
		        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
		        paramsBean.setVersion(HttpVersion.HTTP_1_1);
		        paramsBean.setContentCharset("UTF-8");
		        paramsBean.setUseExpectContinue(true);*/
		        
		        //HttpHost proxy = new HttpHost("118.180.55.220", 8080);
		        HttpHost proxy = new HttpHost(proxyIP, Util.validarIdIntegerApplet(proxyPuerto));
		        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		        		        
		        HttpGet httpget = null;
		        httpget = new HttpGet(fileToDownload.toURI());
		        HttpParams httpRequestParameters = httpget.getParams();
		        httpRequestParameters.setParameter(ClientPNames.HANDLE_REDIRECTS, true);
		        httpget.setParams(httpRequestParameters);
		        
		        
		        LOG.info("Sending GET request for: " + httpget.getURI());
		        HttpResponse response = null;
		        
		        response = client.execute(httpget, localContext);
		        
		        LOG.info("Downloading file: " + downloadedFile.getName());
		        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
		        //FileUtils.copyInputStreamToFile(in, downloadedFile);
		        response.getEntity().getContent().close();
		        client.getConnectionManager().shutdown();
	        }else{
	        	LOG.info("EL JAR YA EXISTIA EN LA CARPETA LOG");
	        
	        }
	        
		} catch (Exception e) {
			// TODO Bloque catch generado automáticamente
			LOG.info("ERROR AL DESCARGAR JAR: " + e.getMessage());
			e.printStackTrace();
		}
        		
	        if (downloadedFile.exists()) {
	        	LOG.info("JAR SE DESCARGO LOCALMENTE Y SE COPIA A  ::: " +rutaDestino);
			}
	        
	        
	        return downloadedFile.getAbsolutePath();
		
	}
	
	protected void findDocumento(Integer idExpediente, String codDocumento) throws Exception {
		System.out.println("findDocumento()");
		String url = "http://"+servidor+":"+puerto+"/BBVA_CE_CTACTE_Servicio/services/WEBCEService";
		System.out.println("url: "+url);
		WEBCEServiceService servicio = new WEBCEServiceService(new URL(url));
		WEBCEService port1 = servicio.getWEBCEService();
		Documento documento = port1.cmObtenerDocumentoxCodigo(codDocumento, idExpediente);
		System.out.println(documento.getCodCliente());
		Archivo.getInstance().guardarArchivo(codDocumento, documento.getContenido());
	}
	
	public void findDocumentoNombrar(Integer idExpediente, String codDocumento,String strNombreDoc) throws Exception {
		System.out.println("findDocumento()");
		String url = "http://"+servidor+":"+puerto+"/BBVA_CE_CTACTE_Servicio/services/WEBCEService";
		System.out.println("url: "+url);
		WEBCEServiceService servicio = new WEBCEServiceService(new URL(url));
		WEBCEService port1 = servicio.getWEBCEService();
		Documento documento = port1.cmObtenerDocumentoxCodigo(codDocumento, idExpediente);
		System.out.println(documento.getCodCliente());
		Archivo.getInstance().guardarArchivo(strNombreDoc, documento.getContenido());
	}
	
	
	public void findDocumentoNombreArch(String tipoDocumento, String nombreArchivo, Integer idCM) throws Exception {
		System.out.println("findDocumento()");
		String url = "http://"+servidor+":"+puerto+"/BBVA_CE_CTACTE_Servicio/services/WEBCEService";
		System.out.println("url: "+url);
		WEBCEServiceService servicio = new WEBCEServiceService(new URL(url));
		WEBCEService port1 = servicio.getWEBCEService();
		Documento documento = port1.cmObtenerDocumentoxNombreArchivo(tipoDocumento, nombreArchivo, idCM);
		System.out.println(documento.getCodCliente());
		Archivo.getInstance().guardarArchivo(tipoDocumento+"-HIST", documento.getContenido());
	}
	
	public String dirDescPDF (String idContent) {
		LOG.info("dirDescPDF (String idContent)");
		return direccionPDF (Archivo.strDescargados, idContent);
	}
	
	public String dirTransfPDF (String idContent) {
		LOG.info("dirTransfPDF (String idContent)");
		return direccionPDF (Archivo.strTransferencias, idContent);
	}
	
	private String direccionPDF (String carpeta, String idContent) {
		LOG.info("direccionPDF (String carpeta, String idContent)");
		String nuevoSep = Archivo.separador + Archivo.separador;
		String direccion = carpeta+Archivo.separador+idContent+Archivo.EXTENSION_ARCHIVO;
		LOG.info("direccion: "+direccion);
		LOG.info("separador: "+nuevoSep);
		direccion = direccion.replaceAll(nuevoSep, nuevoSep+nuevoSep);
		LOG.info("nueva direccion: "+direccion);
		return direccion;
	}
	
	public void ejecutarFuncion (String funcion, Object... parametros) {
		LOG.info("ejecutarFuncion (String funcion, Object... parametros)");
		try {
			StringBuilder builder = new StringBuilder (URL_JAVA_SCRIPT);
			builder.append(funcion).append("(");
			for (Object param : parametros) {
				if (param instanceof String) {
					builder.append("'").append(param).append("'");
				} else {
					builder.append(param);
				}
			}
			builder.append(")");
			String ejecFuncion = builder.toString();
			LOG.info("funcion: "+ejecFuncion);
			getAppletContext().showDocument(new URL(ejecFuncion));
		} catch (Exception e) {
			LOG.error("Error al ejecutar la funcion", e);
		}
	}
	
	public void mensaje (String texto) {
		ejecutarFuncion("mensajeApplet", texto);
	}
	
	public void errorConexionServicio () {
		mensaje("El applet no se puede conectar con el Servicio Web");
	}
	
	public void abrirDocumento (String codigoDocumento) {
		accion = Accion.ABRIR_DOCUMENTO;
		this.codigoDocumento = codigoDocumento;
	}
	
	public void eliminarDocumento (String codigoDocumento) {
		accion = Accion.ELIMINAR_DOCUMENTO;
		this.codigoDocumento = codigoDocumento;
	}
	
	public void renombrarArchivosApp(String strCodigos) {		
		//LOG.info("renombrarArchivosApp(String strCodigos)");
		accion = Accion.RENOMBRAR_DOCUMENTO;
		//this.strCodigos = strCodigos;
		setStrCodigos(strCodigos);
		System.out.println("--renombrarArchivosApp-strCodigos "+getStrCodigos());
	}

	public String getStrCodigos() {
		return strCodigos;
	}

	public void setStrCodigos(String strCodigos) {
		this.strCodigos = strCodigos;
	}
	
}
