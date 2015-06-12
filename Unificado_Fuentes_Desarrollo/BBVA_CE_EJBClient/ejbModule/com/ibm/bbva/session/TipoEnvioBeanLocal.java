package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoEnvio;

public interface TipoEnvioBeanLocal {
	public List<TipoEnvio> buscarTodos();
	
	public TipoEnvio buscarPorId(long id);

}
