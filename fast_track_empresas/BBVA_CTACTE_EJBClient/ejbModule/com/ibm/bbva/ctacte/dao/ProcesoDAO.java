package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Proceso;

public interface ProcesoDAO extends IGenericDAO<Proceso, Integer>{

	List<Proceso> listarUltimosProcesos(int cantidad);

	List<Proceso> getProcesosSinTerminar();

}
