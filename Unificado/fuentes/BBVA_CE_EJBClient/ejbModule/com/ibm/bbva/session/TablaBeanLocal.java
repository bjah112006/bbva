package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Tabla;

public interface TablaBeanLocal {

	public Tabla buscarPorId(long id);
	public List<Tabla> buscarTodos();
}
