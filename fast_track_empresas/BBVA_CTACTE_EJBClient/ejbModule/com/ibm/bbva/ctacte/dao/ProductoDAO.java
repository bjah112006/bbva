package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Producto;

public interface ProductoDAO extends IGenericDAO<Producto, Integer>{
	
	public Producto findByID(Long id);
	public Producto findByCodigo(String codigo);
	public Producto findProductoByCodigo (String codigo);
	//public List<Producto> listarProductos();
	
}
