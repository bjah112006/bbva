package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class ExpedienteCuentaBean
 */
@Stateless
@Local(ExpedienteCuentaDAO.class)
public class ExpedienteCuentaDAOImpl extends GenericDAO<ExpedienteCuenta, Integer> implements ExpedienteCuentaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ExpedienteCuentaDAOImpl() {
    	super(ExpedienteCuenta.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<ExpedienteCuenta> findListaExpedienteCuenta(
			Integer intIdExpediente) {
		Query query = em.createQuery(
 			"select o from ExpedienteCuenta o where o.idExpediente=:idExpediente");
		query.setParameter("idExpediente", intIdExpediente);
		List<ExpedienteCuenta> listExpedienteCuenta = new ArrayList<ExpedienteCuenta>(query.getResultList());
		return listExpedienteCuenta;
	}

}
