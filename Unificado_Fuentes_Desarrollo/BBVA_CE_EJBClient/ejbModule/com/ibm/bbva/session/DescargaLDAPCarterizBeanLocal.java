package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.DescargaLDAPCarteriz;

@Local
public interface DescargaLDAPCarterizBeanLocal 
{

	public DescargaLDAPCarteriz create(DescargaLDAPCarteriz descargaLDAPCarteriz);
	
}
