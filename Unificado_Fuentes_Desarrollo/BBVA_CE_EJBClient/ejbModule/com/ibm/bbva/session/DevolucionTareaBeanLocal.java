package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.DevolucionTarea;

public interface DevolucionTareaBeanLocal {

	public DevolucionTarea create(DevolucionTarea devolucionTarea);	
	
	public List<DevolucionTarea> buscarPorIdExpedienteIdTarea(long idExpediente , long idTarea);
}
