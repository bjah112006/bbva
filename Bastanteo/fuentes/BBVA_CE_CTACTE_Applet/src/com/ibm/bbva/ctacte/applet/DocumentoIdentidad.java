package com.ibm.bbva.ctacte.applet;

import java.awt.BorderLayout;
import java.io.File;

import com.ibm.bbva.ctacte.applet.constructor.AppApplet;
import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.applet.constructor.Parametros;
import com.ibm.bbva.ctacte.pdf.VisorPDF;

public class DocumentoIdentidad extends AppApplet {

	private static final long serialVersionUID = 5338142604325244869L;
	
	private VisorPDF visorPDF;
	private String urlDocumento;
	
	public DocumentoIdentidad() {
		visorPDF = new VisorPDF ();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(visorPDF, BorderLayout.CENTER);
	}
	
	public void init () {
    	super.init();
//    	boolean iniciar = Boolean.parseBoolean(getParameter(Parametros.INICIAR_APPLET));
//    	if (iniciar) {
//    		descargarArchivos ();
//    		iniciar = false;
//    	}
    	String codigoDocumento = getParameter(Parametros.DOCUMENTO_IDENTIDAD);
    	if (codigoDocumento != null) {
    		try {
    			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Entro al try, cargara el archivo");
    			File pdf = Archivo.getInstance().ubicarArchivo(codigoDocumento);
    			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("PDF absolutePath: "+pdf.getAbsolutePath());
    			visorPDF.cargarPDF(pdf.getAbsolutePath(), 1F);  
    			
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}
	
	


	public String getUrlDocumento() {
		return urlDocumento;
	}

	public void setUrlDocumento(String urlDocumento) {
		this.urlDocumento = urlDocumento;
	}
	
	

}
