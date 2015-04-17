package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Territorio;

public interface TerritorioBeanLocal {
	
	public Territorio buscarPorId(long id);
	public List<Territorio> buscarTodos();
	public List<Territorio> buscarPorFlagProvincia(String flagProv);
	public Territorio buscarPorCodigo(String codigo);
	public Territorio create(Territorio territorio);
	public void edit(Territorio territorio);
}
