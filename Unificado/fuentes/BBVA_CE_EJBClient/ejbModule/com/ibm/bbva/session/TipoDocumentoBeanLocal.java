package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TipoDocumento;

public interface TipoDocumentoBeanLocal {

	public TipoDocumento buscarPorId(long id);
	public TipoDocumento buscarPorCodigo(String codigo);
	public List<TipoDocumento> buscar();
	
}
