package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.ClasifBanco;

public interface ClasifBancoBeanLocal {
	public List<ClasifBanco> buscarTodos();
	public ClasifBanco buscarPorId(long id);
	public List<ClasifBanco> buscarPorIdProducto(long idProducto);
}
