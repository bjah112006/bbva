package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.AyudaMemoria;

public interface AyudaMemoriaDAO extends IGenericDAO<AyudaMemoria, Integer> {
	
	public List<AyudaMemoria> findByExpediente(int idExpediente);

}
