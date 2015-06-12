package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Columna;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ColumnaBeanLocal;

/**
 * Session Bean implementation class ColumnaBean
 */
@Stateless
@Local(ColumnaBeanLocal.class)
public class ColumnaBean extends AbstractFacade<Columna> implements ColumnaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ColumnaBean() {
        super(Columna.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Columna> buscarPorIdTabla(long idTabla) {
		List<Columna> resultList = em.createNamedQuery("Columna.findByIdTabla").setParameter("idTabla", idTabla).getResultList();
		return resultList;
	}

}
