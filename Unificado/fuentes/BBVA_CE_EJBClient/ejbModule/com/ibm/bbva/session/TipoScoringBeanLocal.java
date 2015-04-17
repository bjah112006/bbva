package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoScoring;

public interface TipoScoringBeanLocal {
	
	public List<TipoScoring> buscarTodos();
	public TipoScoring buscarPorId(long id);
	
}
