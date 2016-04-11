package com.ibm.bbva.ctacte.applet.constructor;

import java.applet.Applet;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import netscape.javascript.JSObject;

import org.apache.commons.io.FileUtils;
import org.icepdf.core.pobjects.Document;

import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.ctacte.applet.ConstantesApplet;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

public class Archivo {

	public static final String EXTENSION_ARCHIVO = ".pdf";
	private static Archivo archivo;
	
	private static File descargados;
	private static File transferencias;
	private static File escaneados;
	private static File backup;
	public static String strDescargados;
	public static String strTransferencias;
	public static String strBackup;
	public static String separador;
	
	private Archivo () {
	}
	
	static void iniciar (String dirDescargados, String dirTransferencias, String cleanTransferDir) throws IOException {
		descargados = crearDirectorio(dirDescargados);
		transferencias = crearDirectorio(dirTransferencias);
		strDescargados = descargados.getAbsolutePath();
		strTransferencias = transferencias.getAbsolutePath();
		separador = File.separator;
		escaneados = crearEscaneados(descargados);
		backup = crearBackup(descargados);
		strBackup = backup.getAbsolutePath();
		if (cleanTransferDir!= null && cleanTransferDir.equals("1")) {
			//limpiarDirectorios();//TODO arreglar el limpiado
		}
	}
	
	private static void limpiarDirectorios() {
		if (descargados.exists()) {
			for (File archivo : descargados.listFiles()) {
				archivo.delete();
			}
		}
		if (transferencias.exists()) {
			for (File archivo : transferencias.listFiles()) {
				archivo.delete();
			}
		}
		if (escaneados.exists()) {
			for (File archivo : escaneados.listFiles()) {
				archivo.delete();
			}
		}
	}
	
	private static File crearEscaneados (File base) {
		String strBase = base.getAbsolutePath();
		String raiz = strBase.substring(0, strBase.lastIndexOf(separador));
		File dir = new File (raiz, ConstantesApplet.CARPETA_ESCANEADOS);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	private static File crearBackup (File base) {
		String strBase = base.getAbsolutePath();
		String raiz = strBase.substring(0, strBase.lastIndexOf(separador));
		File dir = new File (raiz, ConstantesApplet.CARPETA_BACKUP);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	public static File crearDirectorio (String directorio) {
		File dir = new File(directorio);
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	public void limpiarDescargados () throws IOException {
		//FileUtils.deleteDirectory(descargados);
		if (descargados.exists()) {
			for (File archivo : descargados.listFiles()) {
				archivo.delete();
			}
		}
	}
	
	public void abrirArchivo (String codigoDocumento) throws IOException {
		File archivo = ubicarArchivo (codigoDocumento);
		File archivoTemp = new File (FileUtils.getTempDirectory(), codigoDocumento+EXTENSION_ARCHIVO);
		FileUtils.copyFile(archivo, archivoTemp);
		Desktop.getDesktop().open(archivoTemp);
	}
	
	public File ubicarArchivo (String codigoDocumento) {
		String archivo = codigoDocumento + EXTENSION_ARCHIVO;
//		 CGC: Busca primero en transferencias porque para el CU11 
//		 pueden existir un doc con el mismo nombre en ambas carpetas, en ese caso 
//		 debe devolver primero el de transferencias 
		File temp = new File (transferencias, archivo);
		if (!temp.exists()) {
			temp = new File (descargados, archivo);
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println(temp.getName());
		return temp;
	}
	
	public void eliminarArchivo (String codigoDocumento) throws IOException {
		File temp = ubicarArchivo (codigoDocumento);
		FileUtils.forceDelete(temp);
	}

	public File crearArchivo(String codigoDocumento) throws IOException {
		return new File (transferencias, codigoDocumento + EXTENSION_ARCHIVO);
	}
	
	public void copiarArchivoATranferencias (File direccion, String nombre) throws IOException {
		FileUtils.copyFile(direccion, new File (transferencias, nombre + EXTENSION_ARCHIVO));
	}
	
	public void copiarArchivoABackup (File direccion, String nombre) throws IOException {
		FileUtils.copyFile(direccion, new File (backup, nombre + EXTENSION_ARCHIVO));
	}
	
	public boolean actualizarEscaneado (String codigo) {
		File origen = new File (escaneados, codigo + EXTENSION_ARCHIVO);
		try {
			if (origen.exists()) {
				FileUtils.copyFile(origen, new File (transferencias, codigo + EXTENSION_ARCHIVO));
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public void eliminarArchivosCarpeta (File direccion) {
		for (File archivo : direccion.listFiles()) {
			archivo.delete();
		}
	}
	
	public void escanear (String apiEscaneo) throws IOException {
		eliminarArchivosCarpeta (escaneados);
		Desktop.getDesktop().open(new File(apiEscaneo));
	}
	
	//public void escanearWeb (URL urlEscaneo) throws IOException, URISyntaxException {
	public void escanearWeb (URL urlEscaneo, Applet app) {
		eliminarArchivosCarpeta (escaneados);
		//Desktop.getDesktop().browse(urlEscaneo.toURI());
		try {
			JSObject window = JSObject.getWindow(app);
			//app.showDocument(new URL("javascript:window.open('"+urlEscaneo+"','_blank','toolbar=no, scrollbars=yes, resizable=no, top=0, left=0, width=220, height=80')"));
			window.eval("window.open('"+urlEscaneo+"','ventanaApinae','toolbar=no, scrollbars=yes, resizable=no, top=0, left=0, width=220, height=80')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void guardarArchivo (String codigoDocumento, byte[] data) throws IOException {
		FileUtils.writeByteArrayToFile(new File (descargados, codigoDocumento + EXTENSION_ARCHIVO), data);
	}
	
	public void descargarPDFContent(String codigoDocumento, String urlContenido){
		try {
			URL url = new URL(urlContenido);
	        FileOutputStream outStream;
	        ObjectOutputStream oStream;
	        try {
	            URLConnection con = url.openConnection();
	            InputStream inStream = con.getInputStream();
	            File file = new File(descargados, codigoDocumento + EXTENSION_ARCHIVO);
	            outStream = new FileOutputStream(file);
	            oStream = new ObjectOutputStream(outStream);
	            int bytesRead;
	            //int totalBytesRead = 0;
	            byte[] buffer = new byte[100000];
	            while((bytesRead = inStream.read(buffer)) > 0){
	                outStream.write(buffer, 0 , bytesRead);
	                buffer = new byte[100000];
	                //totalBytesRead += bytesRead;
	            }
	            //System.out.println("Total Bytes read are = " + totalBytesRead);
	            oStream.close();
	            outStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}
	
	public void guardarPDF (String codigoDocumento, Documento documento) throws Exception {
		Document document = new Document();
		byte[] doc = documento.getContenido();
		document.setByteArray(doc, 0, doc.length, null);
		FileOutputStream fos = new FileOutputStream (new File (descargados, codigoDocumento + EXTENSION_ARCHIVO));
		document.writeToOutputStream(fos);
		fos.close();
	}
	
	public static synchronized Archivo getInstance () {
		if (archivo == null) {
			archivo = new Archivo ();
		}
		return archivo;
	}
	
	public void renombrar (String nInicial, String nFinal) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("renombrar "+nInicial+"  -  "+nFinal);
		File fInicial = new File (transferencias, nInicial);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("transferencias "+transferencias);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("fInicial.exists() "+fInicial.exists());
		if (fInicial.exists()) {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("new File(transferencias, nFinal) "+new File(transferencias, nFinal));
			fInicial.renameTo(new File(transferencias, nFinal));
		}
	}
	
	public void guardarTextoPDF (String nombre, String texto) {
        try {
        	File archivo = new File (transferencias, nombre+EXTENSION_ARCHIVO);
        	com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();
            document.addAuthor("IBM-BBVA");
            document.addCreationDate();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(texto));
            document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void guardarPDF (String nombre, String texto) {
        try {
        	com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(nombre));
            document.open();
            document.addAuthor("IBM-BBVA");
            document.addCreationDate();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(texto));
            document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public File[] obtenerListaDocsBackup(final String numExpediente) {
		System.out.println("obtenerListaDocsBackup:"+numExpediente);
		try {
			File[] archs = backup.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.startsWith(numExpediente+"-") && name.endsWith(".pdf"));
				}
			});
			return archs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void abrirArchivoBackup(String nombreArchivo) throws IOException {
		File archivo = new File(backup, nombreArchivo);
		Desktop.getDesktop().open(archivo);
	}
	
	public static void main(String[] args) {
		String str = "<html><head></head><body><html><head></head><body>Dictamen 1 <SPAN style=\"TEXT-ALIGN: justify; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); TEXT-INDENT: 0px; DISPLAY: inline !important; FONT: 12px/20px Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; FLOAT: none; LETTER-SPACING: normal; COLOR: rgb(39,39,39); WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">Click the<SPAN class=Apple-converted-space>&nbsp;</SPAN></SPAN><STRONG style=\"TEXT-ALIGN: justify; LINE-HEIGHT: 20px; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); FONT-VARIANT: normal; FONT-STYLE: normal; TEXT-INDENT: 0px; FONT-FAMILY: Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; LETTER-SPACING: normal; COLOR: rgb(101,125,140); FONT-SIZE: 12px; WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">Begin</STRONG><SPAN style=\"TEXT-ALIGN: justify; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); TEXT-INDENT: 0px; DISPLAY: inline !important; FONT: 12px/20px Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; FLOAT: none; LETTER-SPACING: normal; COLOR: rgb(39,39,39); WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\"><SPAN class=Apple-converted-space>&nbsp;</SPAN>button to start your conference. Those of your attendees who have web-cams and microphones installed on their computers can&nbsp;</SPAN><STRONG style=\"TEXT-ALIGN: justify; LINE-HEIGHT: 20px; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); FONT-VARIANT: normal; FONT-STYLE: normal; TEXT-INDENT: 0px; FONT-FAMILY: Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; LETTER-SPACING: normal; COLOR: rgb(101,125,140); FONT-SIZE: 12px; WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">raise their hands</STRONG><SPAN style=\"TEXT-ALIGN: justify; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); TEXT-INDENT: 0px; DISPLAY: inline !important; FONT: 12px/20px Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; FLOAT: none; LETTER-SPACING: normal; COLOR: rgb(39,39,39); WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">. If you allow them to broadcast they will become your co-presenters and will be able to comment along with the webinar in voice. Please, when the video is not necessary, turn off your webcam and communicate in voice.</SPAN></body></html></body></html>";
		guardarPDF("c://testpdf.pdf", str);
	}
}
