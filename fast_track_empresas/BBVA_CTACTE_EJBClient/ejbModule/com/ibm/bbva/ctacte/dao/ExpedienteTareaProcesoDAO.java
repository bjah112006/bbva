package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;

public interface ExpedienteTareaProcesoDAO extends IGenericDAO<ExpedienteTareaProceso, Integer> {
	
	public ExpedienteTareaProceso findExpedienteTareaProceso(Integer idExpediente, Integer idEmpleado, Integer idTarea);
	public List<ExpedienteTareaProceso> findByIdExpIdTarea(Integer idExpediente, Integer idTarea);
	public void eliminarAnterioresByIdExp(Integer idExpediente);

}
