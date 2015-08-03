package com.ibm.bbva.session.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.entities.LdapTemp;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.LdapTempBeanLocal;


@Stateless
public class LdapTempBean extends AbstractFacade<LdapTemp> implements LdapTempBeanLocal{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    
    public LdapTempBean() {
    	super(LdapTemp.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public LdapTemp create(LdapTemp entity){
		return super.create(entity);
	}

	@Override
	public void clean() 
	{
		Query jpql = em.createQuery(" DELETE FROM LdapTemp ");
		jpql.executeUpdate();	
		
	}
	
}
