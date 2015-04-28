package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.CasoNegocio;

public interface CasoNegocioDAO extends IGenericDAO<CasoNegocio, Integer> {
	
	public List<CasoNegocio> listaCasosNegocioGuia(String codTipoPj, Integer operacion);

}
