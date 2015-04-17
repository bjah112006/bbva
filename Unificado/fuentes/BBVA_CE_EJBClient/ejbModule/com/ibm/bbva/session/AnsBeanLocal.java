package com.ibm.bbva.session;

import com.ibm.bbva.entities.Ans;

public interface AnsBeanLocal {

	Ans buscarPorId(long idProducto, long idTarea, long idTipoOferta, long idGrupoSegmento);	
}
