package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.PieVerificarRealizarBastanteoMB;

public interface IPieVerificarRealizarBastanteo {
	
	void setVerificarRealizarBastanteo(PieVerificarRealizarBastanteoMB verificarRealizarBastanteo);
	
	boolean esValido();
	
	boolean ingresarDictamen();
	
	boolean siHayDevuelto();
	
}
