package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.ViewNumeroExpedientesEmpleado;

public interface ViewNumeroExpedientesEmpleadoDAO {
	
	public List<ViewNumeroExpedientesEmpleado> findListaNumeroExpedientesxEmpleado (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea);
	public List<ViewNumeroExpedientesEmpleado> findListaNumeroExpedientesxEmpleadoPorEstudio (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea, Integer intIdEstudio);
	public List<ViewNumeroExpedientesEmpleado> findListaEmpleadosSinExpedientes (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea);
	public List<ViewNumeroExpedientesEmpleado> findListaEmpleadosSinExpedientesPorEstudio (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea, Integer intIdEstudio);

}
