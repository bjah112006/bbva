package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.GrupoSegmento;

public interface GrupoSegmentoBeanLocal {
	
	public List<GrupoSegmento> buscarTodos();
	public GrupoSegmento buscarPorId(long id);

}
