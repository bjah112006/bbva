package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.AyudaMemoria;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.AyudaMemoriaDAO;

/**
 * Session Bean implementation class AyudaMemoriaBean
 */
@Stateless
@Local(AyudaMemoriaDAO.class)
public class AyudaMemoriaDAOImpl extends GenericDAO<AyudaMemoria, Integer> implements
		AyudaMemoriaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public AyudaMemoriaDAOImpl() {
		super(AyudaMemoria.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<AyudaMemoria> findByExpediente(int idExpediente) {
		Query query = em
				.createQuery("select o from AyudaMemoria o where o.expediente.id=:idExp ");
		query.setParameter("idExp", idExpediente);
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("idExp-->"+idExpediente);
		List<AyudaMemoria> ayudaMemoria = query.getResultList();
		return ayudaMemoria;
	}

}
