package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;

/**
 * Session Bean implementation class TareaBean
 */
@Stateless
@Local(TareaDAO.class)
public class TareaDAOImpl extends GenericDAO<Tarea, Integer> implements TareaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public TareaDAOImpl() {
        super(Tarea.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Tarea> findById(Integer codigoTarea) {
		Query query = em.createQuery(
 			"select o from Tarea o where o.id=:codigoTarea");
		query.setParameter("codigoTarea", codigoTarea);		
		List<Tarea> tarea = query.getResultList();
		return tarea;
	}

	@Override
	public Tarea findById(int codigoTarea) {
		try {
			Query query = em.createQuery(
	 			"select o from Tarea o where o.id=:codigoTarea");
			query.setParameter("codigoTarea", codigoTarea);		
			Tarea tarea = (Tarea)query.getSingleResult();
			return tarea;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Tarea> findByPerfil(Integer idPerfil) {
		Query query = em.createQuery(
 			"select o.tarea from TareaPerfil o where o.perfil.id = :idPerfil order by o.tarea.descripcion ASC");
		query.setParameter("idPerfil", idPerfil);
		List<Tarea> list =  query.getResultList();
		return list;
	}

}
