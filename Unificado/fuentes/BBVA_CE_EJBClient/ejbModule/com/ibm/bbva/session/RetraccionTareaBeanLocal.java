package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.RetraccionTarea;

public interface RetraccionTareaBeanLocal {
	public RetraccionTarea buscarPorParametros(long idAccion, Long salida, Long llegada);
	public List<RetraccionTarea> buscarPorFlagRetraer(long idTareaActual, long idTareaAnterior);
	public List<RetraccionTarea> buscarTodos();
	List<RetraccionTarea> buscarPorFlagRetraerTareaAnt(long idTareaAnterior);
}
