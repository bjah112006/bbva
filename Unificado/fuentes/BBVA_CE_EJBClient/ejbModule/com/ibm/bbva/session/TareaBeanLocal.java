package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.Tarea;

@Local
public interface TareaBeanLocal {

	public Tarea buscarPorId(long id);

	List<Tarea> buscarTodos();

	List<Tarea> buscarPorPerfil(long idPerfil);
	
	public List<Tarea> buscarPorProducto(long idProducto);
	
}
