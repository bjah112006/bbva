package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.HistorialLog;

@Local
public interface HistorialLogBeanLocal {

	public List<HistorialLog> buscarPorIdExpediente(long idExpediente);
	
	public HistorialLog create(HistorialLog historialLog);
	
	
}
