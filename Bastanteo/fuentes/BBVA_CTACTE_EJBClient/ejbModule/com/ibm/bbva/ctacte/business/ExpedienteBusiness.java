package com.ibm.bbva.ctacte.business;

import java.util.Date;

import com.ibm.bbva.ctacte.bean.Expediente;

public interface ExpedienteBusiness {

	public void guardarExpediente (Expediente expediente, int idEstadoExp, int idTarea, int idEstadoTarea, String accion, Date fechaRegistro);
	public int dentroPlazoSubsanacion (Date dtFechaRegistroExpediente, Date dtFechaUltimoBastanteo);
	
}
