package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.PlazoSubsanacion;

public interface PlazoSubsanacionDAO extends IGenericDAO<PlazoSubsanacion, Integer> {

	public List<PlazoSubsanacion> findDentroPlazoSubsanacion (long lngDias);
	
}
