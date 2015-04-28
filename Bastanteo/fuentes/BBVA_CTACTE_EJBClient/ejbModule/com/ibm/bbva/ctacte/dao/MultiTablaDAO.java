package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.MultiTabla;

public interface MultiTablaDAO extends IGenericDAO<MultiTabla, Long> {

	MultiTabla obtener(Long idPadre, String codigo, String texto);
	MultiTabla obtener(Long id);
}
