package com.ibm.bbva.session;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.LogErrores;

@Local
public interface LogErroresBeanLocal {
	
	public List<LogErrores> buscarLogErrores(Date fechaInicio, Date fechaFin, String idPerfil, String idEmpleado, String idTarea, String idEstado, String codigoExpediente);
	
	public LogErrores create(LogErrores logErrores);
	
	public void update(LogErrores logErrores);

}
