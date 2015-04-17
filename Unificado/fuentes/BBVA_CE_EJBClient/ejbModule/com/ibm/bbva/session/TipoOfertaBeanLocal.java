package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoOferta;

public interface TipoOfertaBeanLocal {

	public List<TipoOferta> buscarTodos();
	
	public TipoOferta buscarPorId(long id);
}
