package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Participe;

public interface ParticipeDAO extends IGenericDAO<Participe, Integer> {

	public List<Participe> findByExpediente (Expediente expediente);
	public List<Participe> findByExpedienteParticipes (Integer intIdExpediente);
	public List<Participe> findByCuentaParticipes (Integer intIdCuenta);
	public List<Participe> findByCuenta (Cuenta cuenta);
	public List<Participe> findByExpedienteParticipesUnicos (Integer intIdExpediente);
	public List<Participe> findByFirmaObservadas(Expediente expediente);
	
}
