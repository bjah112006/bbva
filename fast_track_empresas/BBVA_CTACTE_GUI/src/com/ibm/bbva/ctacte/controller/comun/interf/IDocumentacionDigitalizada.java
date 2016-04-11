package com.ibm.bbva.ctacte.controller.comun.interf;

import java.util.List;

import com.ibm.bbva.ctacte.controller.comun.DocumentacionDigitalizadaMB;
import com.ibm.bbva.ctacte.wrapper.DocumentoExpWrapper;

public interface IDocumentacionDigitalizada {
	void setDocumentacionDigitalizada(DocumentacionDigitalizadaMB docDigitalizadaMB);
	
	void aplicarCambioTipoDocumento(List<DocumentoExpWrapper> listaDocumentoExp);
	
	void escanearHistorico();
	
	void actualizarBtnAprobarRechazar(boolean deshBtnAprobar, boolean deshBtnRechazar);
	
	void actualizarBtnAprobarRechazarbyErrores();
	
	boolean isFaltaHistorialRequeridos();

	void setFaltaHistorialRequeridos(boolean faltaHistorialObligatorios);

    boolean isExisteRechazoSeleccionado();

	void setExisteRechazoSeleccionado(boolean existeRechazoSeleccionado);

	boolean isErrorValidacionFechaVigencia();

	void setErrorValidacionFechaVigencia(boolean errorValidacionFechaVigencia);

	boolean isErrorCambioTipoDocumento();

	void setErrorCambioTipoDocumento(boolean errorCambioTipoDocumento);

	void setErrorSeleccionarMotivoRechazo(boolean errorSeleccionarMotivoRechazou);
	
}
