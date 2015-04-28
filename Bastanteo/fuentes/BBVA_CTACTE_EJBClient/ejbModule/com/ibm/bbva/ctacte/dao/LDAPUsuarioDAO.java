package com.ibm.bbva.ctacte.dao;

import java.util.List;

import javax.ejb.Remote;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.LDAPUsuario;

public interface LDAPUsuarioDAO extends IGenericDAO<LDAPUsuario, Integer>{
	
	public LDAPUsuario findByCodigoUsuario(String codigoUsuario) throws NoResultException, NonUniqueResultException;
	
	public List<LDAPUsuario> getAllNotInEmpleado();
	public List<LDAPUsuario> getAllInEmpleado();
}
