package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;

public interface ExpedienteCuentaDAO extends IGenericDAO<ExpedienteCuenta, Integer> {
	
	public List<ExpedienteCuenta> findListaExpedienteCuenta(Integer intIdExpediente);

}
