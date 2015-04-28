package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.SubsanarDigitaliceDocumentacionMB;

public interface ISubsanarDigitaliceDocumentacion {
	
	void setSubsanarDigitaliceDocumentacion(
			SubsanarDigitaliceDocumentacionMB subsanarDigitaliceDocumentacion);
	
	void escanear();
	
	void actualizar();

}
