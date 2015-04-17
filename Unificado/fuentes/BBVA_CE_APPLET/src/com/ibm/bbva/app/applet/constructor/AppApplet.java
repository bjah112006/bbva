package com.ibm.bbva.app.applet.constructor;

import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

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
    	puerto = getParameter(Parametros.PUERTO_SERV_WEB);
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
		LOG.info("Applet iniciado");
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
