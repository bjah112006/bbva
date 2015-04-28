package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Expediente;

public interface ClienteDAO extends IGenericDAO<Cliente, Integer> {
	
	public List<Cliente> findByExpediente(Expediente expediente);
	public Cliente findByCodigCentral(String codigoCentral);
	public Cliente findByNumeroDoi(String numeroDoi);

}
