package com.ibm.bbva.ctacte.applet.constructor;

import java.io.IOException;

import javax.swing.JApplet;

import com.ibm.bbva.ctacte.log.SimpleLogger;

public class ArchivoApplet extends JApplet {

	private static final long serialVersionUID = 1L;
	
	private static final SimpleLogger LOG = SimpleLogger.getLogger(ArchivoApplet.class);

	public void init () {
    	super.init();
    	String carpLog = getParameter(Parametros.CARPETA_CLIENTE_LOG);
    	SimpleLogger.iniciar(Archivo.crearDirectorio(carpLog), true);
    	try {
    		LOG.info(Parametros.CLEAN_TRANSFERDIR+": "+getParameter(Parametros.CLEAN_TRANSFERDIR));
			Archivo.iniciar(
					getParameter(Parametros.CARPETA_CLIENTE_DESCARGADOS),
					getParameter(Parametros.CARPETA_CLIENTE_TRANSFERENCIAS),
					getParameter(Parametros.CLEAN_TRANSFERDIR));
		} catch (IOException e1) {
			e1.printStackTrace();
			LOG.error("No se puede modificar las carpetas", e1);
		}
	}
}
