package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.EstadoCivil;

public interface EstadoCivilBeanLocal {
	
	public List<EstadoCivil> buscarTodos();
	public EstadoCivil buscarPorId(long id);

}
