package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Tarea;

public interface TareaDAO extends IGenericDAO<Tarea, Integer> {

	public List<Tarea> findById (Integer codigoTarea);
	public Tarea findById (int codigoTarea);
	public List<Tarea> findByPerfil(Integer idPerfil);
	
}
