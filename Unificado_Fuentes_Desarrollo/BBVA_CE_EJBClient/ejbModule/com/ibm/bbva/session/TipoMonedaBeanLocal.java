package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoMoneda;

public interface TipoMonedaBeanLocal {

	public List<TipoMoneda> buscarTodos();
	
	public TipoMoneda buscarPorId(long id);
}
