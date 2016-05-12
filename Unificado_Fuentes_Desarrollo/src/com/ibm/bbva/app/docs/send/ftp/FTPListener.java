package com.ibm.bbva.app.docs.send.ftp;

public interface FTPListener {

	void limpiarTransferDir();
	
	void eliminarCarpetaTemporal();
	
	void mostrarMensaje();

}
