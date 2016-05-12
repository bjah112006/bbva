package com.ibm.bbva.app.docs.send.constructor;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import com.ibm.bbva.app.log.SimpleLogger;

//public class AppDoscSend extends ArchivoDocsSend {
public class AppDoscSend {

	private static final long serialVersionUID = 1L;
	private static final SimpleLogger LOG = SimpleLogger.getLogger(AppDoscSend.class);
	private static final String URL_JAVA_SCRIPT = "javascript:";
	
	private enum Accion {ELIMINAR_DOCUMENTO, ABRIR_DOCUMENTO, RENOMBRAR_DOCUMENTO}
	
	private Timer timer;
	private Accion accion;
	private String codigoDocumento;
	private String strCodigos;
	private String servidor;
	private String puerto;
	
	public AppDoscSend() {
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
				//Archivo.getInstance().abrirArchivo(codigoDocumento);
			} catch (Exception e) {
				e.printStackTrace();
			}
			codigoDocumento = null;
		} else if (Accion.ELIMINAR_DOCUMENTO.equals(accion)) {
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
				
			}
			strCodigos = null;
		}
	}
	
	public void init () {
    	//super.init();
    	//servidor = getParameter(Parametros.SERVIDOR_SERV_WEB);
    	//puerto = getParameter(Parametros.PUERTO_SERV_WEB);
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
