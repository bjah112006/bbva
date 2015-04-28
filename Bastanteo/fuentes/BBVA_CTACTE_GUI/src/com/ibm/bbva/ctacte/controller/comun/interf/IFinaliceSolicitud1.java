package com.ibm.bbva.ctacte.controller.comun.interf;

import com.ibm.bbva.ctacte.controller.comun.FinaliceSolicitud1MB;

public interface IFinaliceSolicitud1 {

	void setFinaliceSolicitud1(FinaliceSolicitud1MB finaliceSolicitud1MB);
	
	/**
	 * Copia los datos al expediente
	 * @return true si no existe error al copiar los datos
	 */
	void copiarDatos ();

	boolean esValido();	
	
	boolean hayIndicaciones();
	
	void guardarExpediente (int idEstadoExp, int idTarea, int idEstado, String accion);
	
	void grabarReenviarExpediente(String accion);

}
