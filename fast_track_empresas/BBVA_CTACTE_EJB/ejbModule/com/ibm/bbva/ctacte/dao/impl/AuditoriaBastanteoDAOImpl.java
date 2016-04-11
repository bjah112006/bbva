package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.AuditoriaBastanteo;
import com.ibm.bbva.ctacte.dao.AuditoriaBastanteoDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class AuditoriaBastanteoBean
 */
@Stateless
@Local(AuditoriaBastanteoDAO.class)
public class AuditoriaBastanteoDAOImpl extends GenericDAO<AuditoriaBastanteo, Integer>
		implements AuditoriaBastanteoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public AuditoriaBastanteoDAOImpl() {
		super(AuditoriaBastanteo.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public boolean findExisteResultadoBastanteo(String strResultadoBastanteo) {
		boolean blnExisteResultadoBastanteo = false;
		Query query = em
				.createQuery("select o from AuditoriaBastanteo o where upper(o.resultadoBastanteo)=upper(:resultadoBastanteo)");
		query.setParameter("resultadoBastanteo", strResultadoBastanteo);
		List<AuditoriaBastanteo> listAuditoriaBastanteo = query.getResultList();
		if (listAuditoriaBastanteo.size() > 0) {
			blnExisteResultadoBastanteo = true;
		} else {
			blnExisteResultadoBastanteo = false;
		}
		return blnExisteResultadoBastanteo;
	}

}
