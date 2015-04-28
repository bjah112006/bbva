package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.PieAprobarRevocatoriaMB;

public interface IPieAprobarRevocatoria {

	void setIPieAprobarRevocatoria(
			PieAprobarRevocatoriaMB pieAprobarRevocatoriaMB);

	boolean siHayResultado();
	
	boolean siHayObservaciones();
	
	boolean esValido();
	

}
