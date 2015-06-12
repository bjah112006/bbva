package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Producto;

public interface ProductoBeanLocal {

	public List<Producto> buscarTodos();
	public Producto buscarPorId(long id);
	
}
