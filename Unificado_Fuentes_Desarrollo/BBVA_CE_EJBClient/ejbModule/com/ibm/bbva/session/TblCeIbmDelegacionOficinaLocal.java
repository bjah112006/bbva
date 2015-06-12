package com.ibm.bbva.session;

import com.ibm.bbva.entities.TblCeIbmDelegacionOficina;

public interface TblCeIbmDelegacionOficinaLocal {
	TblCeIbmDelegacionOficina buscarPorIdNivelIdMonedaIdProducto(long idNivel,
			long idMoneda, long idProducto);
	
	TblCeIbmDelegacionOficina buscarPorIdNivelIdProducto(long idNivel, long idProducto);

	TblCeIbmDelegacionOficina buscarPorId(long idNivel);
}
