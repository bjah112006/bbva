package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.ViewPesoParticipeExpediente;

public interface ViewPesoParticipeExpedienteDAO {
	
	public List<ViewPesoParticipeExpediente> findListaPesoParticipesxExpediente (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea);

}
