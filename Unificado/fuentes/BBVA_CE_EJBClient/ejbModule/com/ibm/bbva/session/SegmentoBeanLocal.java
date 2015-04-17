package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Segmento;

public interface SegmentoBeanLocal {

	public Segmento buscarPorId(long id);
	public Segmento buscarPorCodigo(String codigo);
	public Segmento buscarPorCodigoSegmento(String codigoSegmento);
	public List<Segmento> buscarTodos();
}
