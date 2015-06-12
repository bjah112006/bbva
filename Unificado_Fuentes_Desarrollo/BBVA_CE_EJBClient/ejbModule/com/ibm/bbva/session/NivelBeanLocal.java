package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Nivel;

public interface NivelBeanLocal {

	Nivel sinNivel(long idNivel);
	public List<Nivel> buscarTodos();
	public Nivel buscarPorId(long id);

}
