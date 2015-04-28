package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Motivo;

public interface MotivoDAO extends IGenericDAO<Motivo, Integer> {
	
	public List<Motivo> findByTarea(int idTarea,String strTipoMotivo);
	public List<Motivo> getMotivos(String tipoMotivo);

}
