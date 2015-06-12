package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.VistaOficinasExpedientePerfil;

public interface VistaOficinasExpedientePerfilBeanLocal {
	
	public List<VistaOficinasExpedientePerfil> buscarOfExpediente();
	public List<VistaOficinasExpedientePerfil> buscarOfExpPorPerfil(long idPerfil);

}
