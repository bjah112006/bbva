package com.ibm.bbva.ctacte.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.EmpleadoLdap;

public interface EmpleadoLdapDAO extends IGenericDAO<EmpleadoLdap, Integer> {

	public List<EmpleadoLdap> getAllNotInEmpleado();

	public EmpleadoLdap findByCodigoUsuario(String codigoUsuario)throws NoResultException, NonUniqueResultException;
	
	public int deleteAll();
}
