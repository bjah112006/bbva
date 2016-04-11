package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.ResultadoRevisionMB;

public interface IResultadoRevision {

	
	void setResultadoRevision(ResultadoRevisionMB resultadoRevision);
	
	void aprobarDocumentacion(String accion);
	
	void rechazarDocumentacion(String accion);
	
	void actualizarBtnAprobarRechazarbyErrores();
}
