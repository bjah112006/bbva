package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Territorio;

public interface TerritorioDAO extends IGenericDAO<Territorio, Integer> {
		
	public Territorio findByID(Long id);
	public List<Territorio> findAllOrderedByCodigo();

}
