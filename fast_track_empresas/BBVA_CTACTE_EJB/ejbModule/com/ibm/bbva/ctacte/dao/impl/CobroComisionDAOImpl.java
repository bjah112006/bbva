package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.CobroComision;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.CobroComisionDAO;

/**
 * Session Bean implementation class CobroComisionBean
 */
@Stateless
@Local(CobroComisionDAO.class)
public class CobroComisionDAOImpl extends GenericDAO<CobroComision, Integer> implements
		CobroComisionDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public CobroComisionDAOImpl() {
		super(CobroComision.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
