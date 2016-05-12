package com.ibm.bbva.app.applet.constructor;

import java.applet.Applet;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;

import netscape.javascript.JSObject;

import org.apache.commons.io.FileUtils;
import org.icepdf.core.pobjects.Document;

import com.ibm.bbva.app.applet.ConstantesApplet;
import com.ibm.bbva.app.log.SimpleLogger;
import com.ibm.bbva.cm.bean.Documento;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

public class Archivo {
	
	private static final SimpleLogger LOG = SimpleLogger.getLogger(Archivo.class);

	public static final String EXTENSION_ARCHIVO = ".pdf";
	private static Archivo archivo;
	
	private static File descargados;
	private static File transferencias;
	private static File escaneados;
	public static String strDescargados;
	public static String strTransferencias;
	public static String separador;
	public static String raizTransferencias;
	
	private Archivo () {
	}
	
	static void iniciar (String dirDescargados, String dirTransferencias, String cleanTransferDir) throws IOException {
		descargados = crearDirectorio(dirDescargados);
		transferencias = crearDirectorio(dirTransferencias);
		strDescargados = descargados.getAbsolutePath();
		strTransferencias = transferencias.getAbsolutePath();
		separador = File.separator;
		escaneados = crearEscaneados(descargados);
		
		if (cleanTransferDir!= null && cleanTransferDir.equals("1")) {
			limpiarDirectorios();
		}
	}
	
	private static void limpiarDirectorios() {
		try {
			if (descargados.exists()) {
				for (File archivo : descargados.listFiles()) {
					archivo.delete();
				}
			}
			//if (transferencias.exists()) {
			//	for (File archivo : transferencias.listFiles()) {
			//		archivo.delete();
			//	}
			//}
			if (escaneados.exists()) {
				for (File archivo : escaneados.listFiles()) {
					archivo.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void limpiarDirectoriosPasados(String pRaizTransferencias) {
		try {
			//Fecha actual
			Calendar fecha = Calendar.getInstance();
			int mes = fecha.get(Calendar.MONTH) + 1;
			String fechaAhora =String.valueOf(fecha.get(Calendar.YEAR)+"-"+mes+"-"+fecha.get(Calendar.DAY_OF_MONTH));
			LOG.info("FECHA ACTUAL:::" + fechaAhora);
			
			File raizTransferencias = new File(pRaizTransferencias+"\\");
			
			if (raizTransferencias.exists() && raizTransferencias.isDirectory()) {
				LOG.info("EXISTE DIRECTORIO:::" + raizTransferencias);
				for (File carpeta : raizTransferencias.listFiles()) {
					LOG.info("CARPETA " + carpeta);
					if(carpeta.getName().length()>=21){
						String [] cad = carpeta.getName().split("_");
						if(cad.length > 1 ){
							if(!cad[1].equals(fechaAhora)){
								File tempFile = new File(carpeta.getAbsoluteFile().toString());
								if (tempFile.exists()) {
									for (File archivo : tempFile.listFiles()) {
										archivo.delete();
										LOG.info("ARCHIVO " + archivo.getName() + " ELIMINADO");
									}
								}
								tempFile.delete();
								LOG.info("CARPETA " + carpeta.getName() + " ELIMINADA");
							}
						}else{
							File tempFile = new File(carpeta.getAbsoluteFile().toString());
							if (tempFile.exists()) {
								for (File archivo : tempFile.listFiles()) {
									archivo.delete();
									LOG.info("ARCHIVO " + archivo.getName() + " ELIMINADO");
								}
							}
							tempFile.delete(); 
							LOG.info("CARPETA " + carpeta.getName() + " ELIMINADA");
							}
					}else{
						File tempFile = new File(carpeta.getAbsoluteFile().toString()); 
						if (tempFile.exists()) {
							for (File archivo : tempFile.listFiles()) {
								archivo.delete();
								LOG.info("ARCHIVO " + archivo.getName() + " ELIMINADO");
							}
						}
						tempFile.delete(); 
						LOG.info("CARPETA " + carpeta.getName() + " ELIMINADA");
						}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public static File crearDirectorio (String directorio) {
		File dir = new File(directorio);
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	public void limpiarDescargados () throws IOException {
		FileUtils.deleteDirectory(descargados);
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
		System.out.println(temp.getName());
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
	
	public void moverArchivoATranferencias (File direccion, String nombre) throws IOException {
		FileUtils.moveFile(direccion, new File (transferencias, nombre + EXTENSION_ARCHIVO));
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
	
	public File obtenerArchivo(String codigo) {
		File origen = new File (escaneados, codigo + EXTENSION_ARCHIVO);
		return origen;
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
	
	public void escanearWeb (URL urlEscaneo, Applet app) {
		eliminarArchivosCarpeta (escaneados);
		try {
			JSObject window = JSObject.getWindow(app);
			window.eval("window.open('"+urlEscaneo+"','VentanaAPINAE','toolbar=no, scrollbars=yes, resizable=no, top=0, left=0, width=220, height=80')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void guardarArchivo (String codigoDocumento, byte[] data) throws IOException {
		FileUtils.writeByteArrayToFile(new File (descargados, codigoDocumento + EXTENSION_ARCHIVO), data);
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
		System.out.println("renombrar "+nInicial+"  -  "+nFinal);
		File fInicial = new File (transferencias, nInicial);
		System.out.println("transferencias "+transferencias);
		System.out.println("fInicial.exists() "+fInicial.exists());
		if (fInicial.exists()) {
			System.out.println("new File(transferencias, nFinal) "+new File(transferencias, nFinal));
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
	
	public String obtenerListaDocsTransferencias() {
		try {
			StringBuilder arch = new StringBuilder();
			File[] archs = transferencias.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".pdf");
				}
			});
			if (archs != null && archs.length > 0) {
				for (File f : archs) {
					arch.append(f.getName().substring(0, f.getName().lastIndexOf(".")));
					arch.append(",");
				}
			}
			if (!arch.toString().trim().equals("")) {
				return arch.substring(0, arch.length() - 1);
			}
			return null;
		} catch (Exception e) {
			return null;
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

	public static void main(String[] args) {
		String str = "<html><head></head><body><html><head></head><body>Dictamen 1 <SPAN style=\"TEXT-ALIGN: justify; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); TEXT-INDENT: 0px; DISPLAY: inline !important; FONT: 12px/20px Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; FLOAT: none; LETTER-SPACING: normal; COLOR: rgb(39,39,39); WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">Click the<SPAN class=Apple-converted-space>&nbsp;</SPAN></SPAN><STRONG style=\"TEXT-ALIGN: justify; LINE-HEIGHT: 20px; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); FONT-VARIANT: normal; FONT-STYLE: normal; TEXT-INDENT: 0px; FONT-FAMILY: Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; LETTER-SPACING: normal; COLOR: rgb(101,125,140); FONT-SIZE: 12px; WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">Begin</STRONG><SPAN style=\"TEXT-ALIGN: justify; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); TEXT-INDENT: 0px; DISPLAY: inline !important; FONT: 12px/20px Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; FLOAT: none; LETTER-SPACING: normal; COLOR: rgb(39,39,39); WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\"><SPAN class=Apple-converted-space>&nbsp;</SPAN>button to start your conference. Those of your attendees who have web-cams and microphones installed on their computers can&nbsp;</SPAN><STRONG style=\"TEXT-ALIGN: justify; LINE-HEIGHT: 20px; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); FONT-VARIANT: normal; FONT-STYLE: normal; TEXT-INDENT: 0px; FONT-FAMILY: Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; LETTER-SPACING: normal; COLOR: rgb(101,125,140); FONT-SIZE: 12px; WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">raise their hands</STRONG><SPAN style=\"TEXT-ALIGN: justify; WIDOWS: 2; TEXT-TRANSFORM: none; BACKGROUND-COLOR: rgb(255,255,255); TEXT-INDENT: 0px; DISPLAY: inline !important; FONT: 12px/20px Verdana, Geneva, sans-serif; WHITE-SPACE: normal; ORPHANS: 2; FLOAT: none; LETTER-SPACING: normal; COLOR: rgb(39,39,39); WORD-SPACING: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px\">. If you allow them to broadcast they will become your co-presenters and will be able to comment along with the webinar in voice. Please, when the video is not necessary, turn off your webcam and communicate in voice.</SPAN></body></html></body></html>";
		guardarPDF("c://testpdf.pdf", str);
	}
}
