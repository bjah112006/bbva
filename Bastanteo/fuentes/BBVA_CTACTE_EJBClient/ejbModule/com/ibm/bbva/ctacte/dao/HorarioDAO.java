package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.Horario;

public interface HorarioDAO {
	
	public List<Horario> findByIdActivo (Integer codigoHorario, String activo);

}
