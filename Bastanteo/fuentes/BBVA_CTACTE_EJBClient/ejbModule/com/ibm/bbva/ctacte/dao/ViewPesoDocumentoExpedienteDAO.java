package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.ViewPesoDocumentoExpediente;

public interface ViewPesoDocumentoExpedienteDAO {

	public List<ViewPesoDocumentoExpediente> findListaPesoDocumentoxExpediente (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea);
	public List<ViewPesoDocumentoExpediente> findListaPesoDocumentoxExpedientePorEstudio (Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea, Integer intIdEstudio);
	
}
