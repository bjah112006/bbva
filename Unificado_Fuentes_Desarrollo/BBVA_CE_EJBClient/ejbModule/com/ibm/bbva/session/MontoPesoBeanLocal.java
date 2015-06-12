package com.ibm.bbva.session;

import java.math.BigDecimal;

import com.ibm.bbva.entities.MontoPeso;

public interface MontoPesoBeanLocal {
	public MontoPeso buscarPorIdProducto(long idProducto, BigDecimal monto);
	public MontoPeso buscarPorIdProductoIdTipoOfertaMonto(long idProducto, double monto, long idTipoMoneda);
	
}
