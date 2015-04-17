package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Estado;

public interface EstadoBeanLocal {

	public Estado buscarPorId(long id);
	public List<Estado> buscarTodos();
	public List<Estado> buscarPorIdPerfil(long idPerfil) ;
	List<Estado> buscarPorIdTarea(long idTarea);
}
