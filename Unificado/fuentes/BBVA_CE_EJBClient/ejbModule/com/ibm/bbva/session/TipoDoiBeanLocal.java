package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoDoi;

public interface TipoDoiBeanLocal {

	public List<TipoDoi> buscarTodos();
	public TipoDoi buscarPorId(long id);
	
}
