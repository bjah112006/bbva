package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.AuditoriaCliente;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaClienteDAO;

/**
 * Session Bean implementation class AuditoriaClienteBean
 */
@Stateless
@Local(AuditoriaClienteDAO.class)
public class AuditoriaClienteDAOImpl extends GenericDAO<AuditoriaCliente, Integer>
		implements AuditoriaClienteDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public AuditoriaClienteDAOImpl() {
		super(AuditoriaCliente.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public boolean findExisteCodigoCentral(String strCodigoCentral) {
		boolean blnExisteCodigoCentral = false;
		Query query = em
				.createQuery("select o from AuditoriaCliente o where o.codigoCentral=:codigoCentral");
		query.setParameter("codigoCentral", strCodigoCentral);
		List<AuditoriaCliente> listAuditoriaCliente = query.getResultList();
		if (listAuditoriaCliente.size() > 0) {
			blnExisteCodigoCentral = true;
		} else {
			blnExisteCodigoCentral = false;
		}
		return blnExisteCodigoCentral;
	}

}
