package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Log;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.LogDAO;

/**
 * Session Bean implementation class LogBean
 */
@Stateless
@Local(LogDAO.class)
public class LogDAOImpl extends GenericDAO<Log, Integer> implements LogDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public LogDAOImpl() {
        super(Log.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Log> findByExpdiente(int idExpediente) {
		Query query = em.createQuery(
 			"select o from Log o where o.expediente.id=:idExp order by o.fecRegistro");
		query.setParameter("idExp", idExpediente);
		List<Log> logs = query.getResultList();
		return logs;
	}

}
