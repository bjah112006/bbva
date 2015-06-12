package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Garantia;

public interface GarantiaBeanLocal {

	List<Garantia> buscarTodos();

	Garantia buscarPorId(long id);

	List<Garantia> buscarPorIdProducto(long idProducto);

	List<Garantia> buscarPorFlagSinGarantia(long idProducto,
			String flagSinGarantia);

}
