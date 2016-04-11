package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Horario;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.HorarioDAO;

/**
 * Session Bean implementation class HorarioBean
 */
@Stateless
@Local(HorarioDAO.class)
public class HorarioDAOImpl extends GenericDAO<Horario, Integer> implements HorarioDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public HorarioDAOImpl() {
        super(Horario.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Horario> findByIdActivo(Integer codigoHorario, String activo) {
		Query query = em.createQuery(
 			"select o from Horario o where o.id=:codigoHorario and o.activo=:activo");
		query.setParameter("codigoHorario", codigoHorario);
		query.setParameter("activo", activo);
		List<Horario> horario = query.getResultList();
		return horario;
	}

}
