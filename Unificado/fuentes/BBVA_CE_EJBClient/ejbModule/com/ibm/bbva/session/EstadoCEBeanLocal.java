package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.EstadoCE;


public interface EstadoCEBeanLocal {

	public EstadoCE buscarPorId(long id);
	public List<EstadoCE> buscarTodos();
	public List<EstadoCE> buscarPorIdPerfil(long idPerfil) ;
	List<EstadoCE> buscarPorIdEstado(long idEstado);
	public List<EstadoCE> buscarTodosMenosEnDesuso();
}
