package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Log;

public interface LogDAO extends IGenericDAO<Log, Integer> {
	
	public List<Log> findByExpdiente(int idExpediente);

}
