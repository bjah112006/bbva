package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.ProductoEtiqueta;

public interface ProductoEtiquetaBeanLocal {

	List<ProductoEtiqueta> buscarTodos();

	ProductoEtiqueta buscarPorId(long id);

	List<ProductoEtiqueta> buscarPorIdProducto(long idProducto);

}
