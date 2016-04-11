package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.RangoCC;
import com.ibm.bbva.ctacte.vo.HorarioVO;

public interface RangoCCDAO extends IGenericDAO<RangoCC, Integer> {
	
	public List<RangoCC> obtenerRangos(HorarioVO horarioVO) throws DataAccessException;

	public List<RangoCC> findByHorarioCC(Integer id)throws DataAccessException;

	public List<RangoCC> obtenerRangosPorPeriodo(Integer id, Integer id2,
			int dayFrom, int dayTo);

}
