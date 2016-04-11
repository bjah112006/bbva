package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.MultiTabla;

public interface MultiTablaDAO extends IGenericDAO<MultiTabla, Long> {

	public MultiTabla obtener(Long idPadre, String codigo, String texto);
	public MultiTabla obtener(Long id);
	public List<MultiTabla> listaHijos(Long idPadre);
	public MultiTabla obtener(String nombre);
	public MultiTabla obtenerxEntero(Long entero);
	
}
