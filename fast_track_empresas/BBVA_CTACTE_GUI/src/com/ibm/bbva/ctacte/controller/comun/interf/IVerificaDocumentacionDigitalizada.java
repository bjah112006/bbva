package com.ibm.bbva.ctacte.controller.comun.interf;


import java.util.List;

import com.ibm.bbva.ctacte.controller.comun.VerificaDocumentacionDigitalizadaMB;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;

public interface IVerificaDocumentacionDigitalizada {
	
	void setVerificaDocumentacionDigitalizada(
			VerificaDocumentacionDigitalizadaMB verificaDocumentacionDigitalizada);
	
	void aplicarCambioTipoDocumento(List<DocumentoExpWrapper> listaDocumentoExp);
	
	void deshabilitarBoton(boolean habBtnRechazar, boolean deshabBtnTerminar);
	
	void setErrorSeleccionarMotivoRechazo(boolean errorSeleccionarMotivoRechazo);
	
	void setExisteRechazoSeleccionado(boolean existeRechazoSeleccionado);
	
	void activarSegunArchivoRechazado (boolean archivoRechazado);
	
}
