package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Accion;
import com.ibm.bbva.ctacte.dao.AccionDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class AccionDAOImpl
 */
@Stateless
@Local(AccionDAO.class)
public class AccionDAOImpl extends GenericDAO<Accion, Integer> implements AccionDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public AccionDAOImpl() {
        super(Accion.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Accion> findByTarea(Integer idTarea) {
		Query query = em.createQuery(
 			"select o from Accion o where o.tarea.id = :idTarea order by o.descripcion ASC");
		query.setParameter("idTarea", idTarea);
		List<Accion> list =  query.getResultList();
		return list;
	}

}
