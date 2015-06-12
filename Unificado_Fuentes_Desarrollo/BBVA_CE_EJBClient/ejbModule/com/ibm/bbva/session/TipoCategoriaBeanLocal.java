package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoCategoria;

public interface TipoCategoriaBeanLocal {
	
	public List<TipoCategoria> buscarTodos();

	public TipoCategoria buscarPorId(long id);

}
