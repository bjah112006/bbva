package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;

public interface CuentaDAO extends IGenericDAO<Cuenta, Integer> {

	public List<Cuenta> findByCliente(Cliente cliente);
	public List<Cuenta> findByClienteActivos(Cliente cliente);
	public Cuenta findByNumeroContrato(Cliente cliente, String numeroContrato);
	
}
