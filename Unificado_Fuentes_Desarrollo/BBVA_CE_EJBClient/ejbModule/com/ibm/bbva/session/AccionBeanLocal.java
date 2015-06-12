package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Accion;
import com.ibm.bbva.entities.Tarea;

public interface AccionBeanLocal {
	public List<Accion> buscarTodos();
	public Accion buscarPorId(long id);
	public Accion buscarPorAccion(String strAccion);
}
