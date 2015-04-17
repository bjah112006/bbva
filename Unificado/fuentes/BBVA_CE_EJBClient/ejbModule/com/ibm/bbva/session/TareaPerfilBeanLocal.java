package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TareaPerfil;

public interface TareaPerfilBeanLocal {

	public List<TareaPerfil> buscarPorIdTarea(long idTarea);
	public List<TareaPerfil> buscarPorIdPerfil(long idPerfil);
}
