package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.TareaAccion;

public interface TareaAccionBeanLocal {

	public List<TareaAccion> buscarPorIdTarea(long idTarea);
	public List<TareaAccion> buscarPorIdAccion(long idAccion);
}
