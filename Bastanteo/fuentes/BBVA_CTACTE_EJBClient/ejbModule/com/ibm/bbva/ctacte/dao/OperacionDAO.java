package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Operacion;

public interface OperacionDAO extends IGenericDAO<Operacion, Integer> {
	
	public List<Operacion> getOperaciones (List<String> codigos);
	public Operacion findByCodOperacion (String codOperacion);

}
