package com.ibm.bbva.session;

import java.math.BigDecimal;
import java.util.List;

import com.ibm.bbva.entities.MotivoDevolucion;

public interface MotivoDevolucionBeanLocal {
	
	public MotivoDevolucion buscarPorId(long id);
	public List<MotivoDevolucion> buscarPorIdTarea(long idTarea);
	public List<MotivoDevolucion> buscarPorIdTareaYAccion(long idTarea, BigDecimal accion);
	List<MotivoDevolucion> buscarPorIdMotivoIdTareaFlagOtros(long idMotivo,
			long idTarea, String flagOtros);
	List<MotivoDevolucion> buscarPorIdTareaDevolver(long idTarea);

}
