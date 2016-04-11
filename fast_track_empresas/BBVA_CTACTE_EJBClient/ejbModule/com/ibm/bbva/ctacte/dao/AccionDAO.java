package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Accion;

public interface AccionDAO extends IGenericDAO<Accion, Integer> {
	
	public List<Accion> findByTarea(Integer idTarea);

}
