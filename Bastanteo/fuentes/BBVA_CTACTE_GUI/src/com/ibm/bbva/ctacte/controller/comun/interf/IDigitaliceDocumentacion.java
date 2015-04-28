package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;

public interface IDigitaliceDocumentacion {
	
	void setDigitaliceDocumentacion(
			DigitaliceDocumentacionMB digitaliceDocumentacion);

	void actualizarSubidaDocumentos(Character flagDocumentos);
	
	void habilitarBoton(boolean habReasignar);
	
}
