package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.ClienteNatural;

public interface ClienteNaturalBeanLocal {
	
	public ClienteNatural buscarPorId(long id);
	
	public List<ClienteNatural> buscarPorTipoNumDoi(ClienteNatural clienteNatural);
	
	public ClienteNatural create(ClienteNatural clienteNatural);
	
	public void edit(ClienteNatural clienteNatural);
}
