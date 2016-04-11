package com.ibm.bbva.ctacte.ftp;


public interface FTPListener {

	void nuevoArchivo (String archivo);
	
	void errorArchivo(String archivo, int reintentos);
	
	void exitoArchivo (String archivo);
	
	void setTamanio (int tamanio);
	
	void setAvance (int bytesLeidos);
	
	void cerrarVentana ();
	
	void limpiarTransferDir ();
	
}
