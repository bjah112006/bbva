package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.PosibleValor;

public interface PosibleValorBeanLocal {

	List<PosibleValor> buscarPorIdColumna(long idColumna);

	List<PosibleValor> buscarPorIdColumnaIdValor(long idColumna, String idValor);

}
