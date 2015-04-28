package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;

public interface ExpedienteTareaProcesoDAO extends IGenericDAO<ExpedienteTareaProceso, Integer> {
	
	public ExpedienteTareaProceso finExpedienteTareaProceso(Integer idExpediente, Integer idEmpleado, Integer idTarea);

}
