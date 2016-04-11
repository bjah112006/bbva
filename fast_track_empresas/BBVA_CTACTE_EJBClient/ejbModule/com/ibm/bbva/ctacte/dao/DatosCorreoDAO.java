package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.DatosCorreo;

public interface DatosCorreoDAO extends IGenericDAO<DatosCorreo, Integer> {
	
	public List<DatosCorreo> buscarTodos();
	public List<DatosCorreo> buscarPorTarea(Integer idTarea);
	public List<DatosCorreo> buscarPorTareas(List<Integer> idTareas);
	public DatosCorreo buscarPorIdTareaYNombreAccion(Integer idTarea, String nombreAccion);
	public void eliminar(Integer id);

}
