package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;

public interface ExpedienteTareaDAO extends IGenericDAO<ExpedienteTarea, Integer> {
	
	public List<ExpedienteTarea> findByIdTarea(Integer idTarea);
	public List<ExpedienteTarea> findByIdExpediente(Integer idExpe);

}
