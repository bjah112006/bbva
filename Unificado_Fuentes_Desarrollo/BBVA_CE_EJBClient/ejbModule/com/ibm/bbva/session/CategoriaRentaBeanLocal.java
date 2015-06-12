package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.CategoriaRenta;

public interface CategoriaRentaBeanLocal {
	
	public List<CategoriaRenta> buscarTodos();
	public CategoriaRenta buscarPorId(long id);

}
