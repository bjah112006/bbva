package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ParametrosConf;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;

/**
 * Session Bean implementation class ParametrosConfDAOImpl
 */
@Stateless
@Local(ParametrosConfDAO.class)
public class ParametrosConfDAOImpl extends GenericDAO<ParametrosConf, Integer> implements ParametrosConfDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ParametrosConfDAOImpl() {
    	super(ParametrosConf.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

    public ParametrosConf obtener(String codigoModulo, String nombreVariable) {
		Query query = em.createQuery("select o from ParametrosConf o where o.codigoModulo=:codigoModulo and o.nombreVariable=:nombreVariable");
		query.setParameter("codigoModulo", codigoModulo);
		query.setParameter("nombreVariable", nombreVariable);
		return (ParametrosConf) query.getSingleResult();
    }
}
