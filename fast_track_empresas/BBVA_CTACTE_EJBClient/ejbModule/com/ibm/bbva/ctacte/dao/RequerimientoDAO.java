package com.ibm.bbva.ctacte.dao;

import java.sql.SQLException;
import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Requerimiento;
import com.ibm.bbva.ctacte.model.RequerimientoModel;

public interface RequerimientoDAO extends IGenericDAO<Requerimiento, Integer> {

	List<RequerimientoModel> listaRequerimiento(RequerimientoModel filtro)throws SQLException;
	RequerimientoModel obtener(Long id)throws SQLException;
		
}
