package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Columna;

public interface ColumnaBeanLocal {

	public List<Columna> buscarPorIdTabla(long idTabla);
}
