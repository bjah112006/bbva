package com.ibm.bbva.session;

import com.ibm.bbva.entities.DelegacionRiesgo;

public interface DelegacionRiesgoBeanLocal {
	public DelegacionRiesgo buscarPorId(long idCategoria, long idProducto,
			long idMoneda);
}
