package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoCliente;

public interface TipoClienteBeanLocal {

	public List<TipoCliente> buscarTodos();
	public TipoCliente buscarPorId(long id);
	public TipoCliente buscarPorCodigo(String codigo);
}
