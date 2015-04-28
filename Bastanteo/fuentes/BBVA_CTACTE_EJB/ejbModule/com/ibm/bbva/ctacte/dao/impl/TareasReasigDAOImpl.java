package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.TareasReasig;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.TareasReasigDAO;

/**
 * Session Bean implementation class TareasReasigBean
 */
@Stateless
@Local(TareasReasigDAO.class)
public class TareasReasigDAOImpl extends GenericDAO<TareasReasig, Integer> implements TareasReasigDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public TareasReasigDAOImpl() {
        super(TareasReasig.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
