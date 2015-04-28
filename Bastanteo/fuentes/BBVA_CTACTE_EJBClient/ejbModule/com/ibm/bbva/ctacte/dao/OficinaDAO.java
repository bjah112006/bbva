package com.ibm.bbva.ctacte.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Oficina;

public interface OficinaDAO extends IGenericDAO<Oficina, Integer> {
	
	public List<Oficina> findByIdTerritorio (Integer codigoTerritorio);

	public Oficina findByCodigo(String codofi) throws NoResultException, NonUniqueResultException;
	
	public List<Oficina> findAllOrderedByCodigo (boolean tipo);

}
