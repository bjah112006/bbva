package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.AuditoriaCriteriosSupervision;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaCriteriosSupervisionDAO;

/**
 * Session Bean implementation class AuditoriaCriteriosSupervisionBean
 */
@Stateless
@Local(AuditoriaCriteriosSupervisionDAO.class)
public class AuditoriaCriteriosSupervisionDAOImpl extends
		GenericDAO<AuditoriaCriteriosSupervision, Integer> implements
		AuditoriaCriteriosSupervisionDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public AuditoriaCriteriosSupervisionDAOImpl() {
		super(AuditoriaCriteriosSupervision.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public AuditoriaCriteriosSupervision findObtenerCriterioSupervision(
			Integer idCriterioSupervision) {
		Query query = em
				.createQuery("select o from AuditoriaCriteriosSupervision o where o.id=:idCriterioSupervision");
		query.setParameter("idCriterioSupervision", idCriterioSupervision);
		List<AuditoriaCriteriosSupervision> listAuditoriaCriteriosSupervision = query
				.getResultList();
		AuditoriaCriteriosSupervision auditoriaCriteriosSupervision = new AuditoriaCriteriosSupervision();
		auditoriaCriteriosSupervision = listAuditoriaCriteriosSupervision
				.get(0);
		return auditoriaCriteriosSupervision;
	}

}
