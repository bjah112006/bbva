package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.HorarioOficina;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.HorarioOficinaDAO;

/**
 * Session Bean implementation class HorarioOficinaBean
 */
@Stateless
@Local(HorarioOficinaDAO.class)
public class HorarioOficinaDAOImpl extends GenericDAO<HorarioOficina, Integer> implements HorarioOficinaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public HorarioOficinaDAOImpl() {
        super(HorarioOficina.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<HorarioOficina> findByOficina(Integer codigoOficina) {
		Query query = em.createQuery(
 			"select o from HorarioOficina o where o.id=:codigoOficina");
		query.setParameter("codigoOficina", codigoOficina);
		List<HorarioOficina> horarioOficina = query.getResultList();
		return horarioOficina;
	}

}
