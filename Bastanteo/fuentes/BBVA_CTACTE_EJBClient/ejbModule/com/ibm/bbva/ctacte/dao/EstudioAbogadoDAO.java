package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;

public interface EstudioAbogadoDAO extends IGenericDAO<EstudioAbogado, Integer> {

	public EstudioAbogado findByIdEstudio (Integer idEstudio);
	
}
