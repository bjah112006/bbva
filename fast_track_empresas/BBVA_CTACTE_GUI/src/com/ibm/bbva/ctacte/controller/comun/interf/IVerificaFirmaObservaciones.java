package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.VerificaFirmaObservacionesMB;

public interface IVerificaFirmaObservaciones {
	
	void setVerificaFirmaObservaciones(VerificaFirmaObservacionesMB verificaFirmaObservaciones);
	
	void habilitarBotonRechazar(boolean habBtnRechazar);

	void ejecutarRechazarDocumentacion(String accion);
	
	void activarSegunFirmaAsociada (String existeFirmaNoAsociada);
}
