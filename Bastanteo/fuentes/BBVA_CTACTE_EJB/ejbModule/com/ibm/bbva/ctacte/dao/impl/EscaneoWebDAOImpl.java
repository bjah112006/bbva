package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.EscaneoWeb;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.EscaneoWebDAO;

/**
 * Session Bean implementation class EscaneoWebBean
 */
@Stateless
@Local(EscaneoWebDAO.class)
public class EscaneoWebDAOImpl extends GenericDAO<EscaneoWeb, Integer> implements EscaneoWebDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public EscaneoWebDAOImpl() {
        super(EscaneoWeb.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public EscaneoWeb findById(Integer id) {
		Query query = em.createQuery(
 			"select o from EscaneoWeb o where o.id=:id");
		query.setParameter("id", id);
		EscaneoWeb escaneoWeb;
		try {
			escaneoWeb = (EscaneoWeb) query.getResultList().get(0);
		} catch(IndexOutOfBoundsException iobe) {
			escaneoWeb = null;
		}
		return escaneoWeb;
	}

}
