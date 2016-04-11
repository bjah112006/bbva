package com.ibm.bbva.ctacte.dao;

import java.util.Date;
import java.util.List;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.HorarioCC;
import com.ibm.bbva.ctacte.vo.HorarioVO;

public interface HorarioCCDAO extends IGenericDAO<HorarioCC, Integer>{

	List<HorarioCC> obtenerFeriados(HorarioVO horarioVO) throws DataAccessException;

	HorarioCC getDayOFTheWeek(int idPerfil, int idOficina, int idDiaSeleccionado)throws DataAccessException;

	List<HorarioCC> obtenerFeriadosPorPeriodo(Integer id, Integer id2,
			int dayFrom, int dayTo, int year);
	
	List<HorarioCC> obtenerFeriadosPorPeriodo(Date fechaIni, Date fechaFin, int idPerfil, int idOficina);

}
