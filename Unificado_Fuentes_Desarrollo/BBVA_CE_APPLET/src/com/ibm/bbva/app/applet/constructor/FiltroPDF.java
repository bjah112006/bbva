package com.ibm.bbva.app.applet.constructor;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroPDF extends FileFilter {

	public boolean accept(File fichero) {
		if (fichero.isDirectory() || fichero.getName().toLowerCase().endsWith(Archivo.EXTENSION_ARCHIVO))
			return true;
		else
			return false;
	}

	public String getDescription() {
		return ("PDF");
	}

}
