package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ListaCerrada;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ListaCerradaDAO;

/**
 * Session Bean implementation class ListaCerradaBean
 */
@Stateless
@Local(ListaCerradaDAO.class)
public class ListaCerradaDAOImpl extends GenericDAO<ListaCerrada, Integer> implements ListaCerradaDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ListaCerradaDAOImpl() {
        super(ListaCerrada.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public ListaCerrada findByCodigoCentral(String codigoCentral) {
		try {
			Query query = em.createQuery(
	 			"select o from ListaCerrada o where o.codigoCentral = :codCent");
			query.setParameter("codCent", codigoCentral);
			ListaCerrada listaCerrada = (ListaCerrada) query.getSingleResult();
			return listaCerrada;
		} catch (NoResultException e) {
			return null;
		}
	}

}
