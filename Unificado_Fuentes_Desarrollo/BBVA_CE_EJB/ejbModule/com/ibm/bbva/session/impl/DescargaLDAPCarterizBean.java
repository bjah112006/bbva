package com.ibm.bbva.session.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.DescargaLDAPCarteriz;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DescargaLDAPCarterizBeanLocal;

@Stateless
public class DescargaLDAPCarterizBean extends AbstractFacade<DescargaLDAPCarteriz> implements DescargaLDAPCarterizBeanLocal
{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public DescargaLDAPCarterizBean() {
    	super(DescargaLDAPCarteriz.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DescargaLDAPCarteriz create(DescargaLDAPCarteriz entity){
		return super.create(entity);
	}
	
}
