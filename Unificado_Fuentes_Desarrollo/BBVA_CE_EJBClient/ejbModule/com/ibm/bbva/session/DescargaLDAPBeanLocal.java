package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.DescargaLDAP;

@Local
public interface DescargaLDAPBeanLocal {

	public void edit(DescargaLDAP descargaLDAP);
	
	public DescargaLDAP create(DescargaLDAP descargaLDAP);
	
	public List<DescargaLDAP> buscar(String tipo, String codigo, int caracterizacion, String estado);
	
}
