package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Multitabla;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.MutitablaBeanLocal;


@Stateless
@Local(MutitablaBeanLocal.class)
public class MultitablaBean extends AbstractFacade<Multitabla> implements MutitablaBeanLocal
{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public MultitablaBean() {
        super(Multitabla.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

		
	@Override
	public List<Multitabla> buscarPorPadre(Long idPadre) {
		List<Multitabla> resultList;
		try {			
			resultList = em.createNamedQuery("Multitabla.findByPadre")
							.setParameter("idPadre", idPadre)
							.getResultList();
			return resultList;
		} catch (Exception e) {			
			return null;
		}
	}
}
