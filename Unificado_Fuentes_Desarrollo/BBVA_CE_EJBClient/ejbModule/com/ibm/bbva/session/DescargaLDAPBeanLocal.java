package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.DescargaLDAPCarteriz;
import com.ibm.bbva.entities.Empleado;

@Local
public interface DescargaLDAPBeanLocal {

	public void edit(DescargaLDAP descargaLDAP);
	
	public DescargaLDAP create(DescargaLDAP descargaLDAP);
	
	public List<DescargaLDAP> buscar(String tipo, String codigo, String caracterizacion, String estado);
	
	public DescargaLDAP buscarPorId(long id);

	
}
