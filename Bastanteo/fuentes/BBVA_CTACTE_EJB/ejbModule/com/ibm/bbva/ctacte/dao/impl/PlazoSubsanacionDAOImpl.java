package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.PlazoSubsanacion;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.PlazoSubsanacionDAO;

/**
 * Session Bean implementation class PlazoSubsanacionBean
 */
@Stateless
@Local(PlazoSubsanacionDAO.class)
public class PlazoSubsanacionDAOImpl extends GenericDAO<PlazoSubsanacion, Integer> implements PlazoSubsanacionDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public PlazoSubsanacionDAOImpl() {
        super(PlazoSubsanacion.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<PlazoSubsanacion> findDentroPlazoSubsanacion(long lngDias) {
		Query query = em.createQuery(
 			"select o from PlazoSubsanacion o where o.plazoDias>=:intDias");
		query.setParameter("intDias", lngDias);
		List<PlazoSubsanacion> plazoSubsanacion = query.getResultList();
		return plazoSubsanacion;
	}

}
