package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.ProductoCE;

public interface ProductoCEDAO extends IGenericDAO<ProductoCE,Integer>{
		
	//public ProductoCE buscarPorID(Long id);
	public ProductoCE findByID(Long id);
	public ProductoCE findByCodigo(String codigo);
	//public ProductoCE findProductoByCodigo (String codigo);

}
