package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.CartTerritorioCE;

public interface CartTerritorioCEBeanLocal {
	//Emitson 
	public List<CartTerritorioCE> buscarPorIdTerr(long idTerritorio);
	public List<CartTerritorioCE> buscarPorIdProdIdTerr(long idProducto, long idTerritorio);	
}
