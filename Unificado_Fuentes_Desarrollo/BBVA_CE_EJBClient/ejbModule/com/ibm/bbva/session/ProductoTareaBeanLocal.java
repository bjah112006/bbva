package com.ibm.bbva.session;

import javax.ejb.Local;

import com.ibm.bbva.entities.ProductoTarea;

@Local
public interface ProductoTareaBeanLocal {
	public ProductoTarea buscarPorIdTareaIdProducto(long idProducto, long idTarea);
}
