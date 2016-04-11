package com.ibm.bbva.ctacte.dao;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.ParametrosConf;

public interface ParametrosConfDAO extends IGenericDAO<ParametrosConf, Integer> {

	public ParametrosConf obtener(String codigoModulo, String nombreVariable);
}
