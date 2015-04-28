package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.Campania;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.CampaniaDAO;

/**
 * Session Bean implementation class CampaniaBean
 */
@Stateless
@Local(CampaniaDAO.class)
public class CampaniaDAOImpl extends GenericDAO<Campania, Integer> implements
		CampaniaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public CampaniaDAOImpl() {
		super(Campania.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
