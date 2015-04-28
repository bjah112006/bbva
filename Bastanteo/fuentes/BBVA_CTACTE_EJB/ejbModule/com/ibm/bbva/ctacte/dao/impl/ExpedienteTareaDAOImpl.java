package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaDAO;

/**
 * Session Bean implementation class ExpedienteTareaBean
 */
@Stateless
@Local(ExpedienteTareaDAO.class)
public class ExpedienteTareaDAOImpl extends GenericDAO<ExpedienteTarea, Integer> implements ExpedienteTareaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ExpedienteTareaDAOImpl() {
        super(ExpedienteTarea.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<ExpedienteTarea> findByIdTarea(Integer idTarea) {
		Query q =  em.createQuery("select o from ExpedienteTarea o where o.tarea.id = :id");
		q.setParameter("id", idTarea);
		List<ExpedienteTarea> lista = q.getResultList();
		return  lista;
	}

	@Override
	public List<ExpedienteTarea> findByIdExpediente(Integer idExpe) {
		Query q =  em.createQuery("select o from ExpedienteTarea o where o.expediente.id = :id");
		q.setParameter("id", idExpe);
		List<ExpedienteTarea> lista = q.getResultList();
		return  lista;
	}

}
