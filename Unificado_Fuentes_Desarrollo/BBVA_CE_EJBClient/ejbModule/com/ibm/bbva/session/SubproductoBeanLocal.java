package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Subproducto;

public interface SubproductoBeanLocal {

	public Subproducto buscarPorId(long id);
	public List<Subproducto> buscarPorIdProducto(long idProducto);
	public List<Subproducto> buscarTodos();
	
}
