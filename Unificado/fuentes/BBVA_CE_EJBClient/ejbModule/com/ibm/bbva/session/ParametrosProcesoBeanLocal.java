package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.ParametrosProceso;

public interface ParametrosProcesoBeanLocal {
	
	public List<ParametrosProceso> buscarTodos();
	public ParametrosProceso buscarPorVariable(String nombreVariable);
}
