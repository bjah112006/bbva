package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.bean.HorarioOficina;

public interface HorarioOficinaDAO {
	
	public List<HorarioOficina> findByOficina (Integer codigoOficina);

}
