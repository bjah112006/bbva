package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Log;

public interface LogBeanLocal {

	public List<Log> buscarPorIdExpediente(long idExpediente);
	
}
