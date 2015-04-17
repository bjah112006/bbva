package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.VistaExpedienteComplejidad;

public interface VistaExpedienteComplejidadBeanLocal {
	public List<VistaExpedienteComplejidad> buscarPorIdProdIdTerrIdPer(long idProducto, long idTerritorio, long idPerfil);

}
