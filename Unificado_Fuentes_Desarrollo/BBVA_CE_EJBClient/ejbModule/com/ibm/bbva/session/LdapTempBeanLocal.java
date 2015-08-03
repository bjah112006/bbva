package com.ibm.bbva.session;

import javax.ejb.Local;

import com.ibm.bbva.entities.LdapTemp;


@Local
public interface LdapTempBeanLocal 
{

	public LdapTemp create(LdapTemp ldapTemp);
	
	public void clean();
	
}

