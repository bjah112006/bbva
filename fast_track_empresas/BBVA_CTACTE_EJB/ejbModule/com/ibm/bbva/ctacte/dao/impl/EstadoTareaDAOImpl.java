package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;

/**
 * Session Bean implementation class EstadoTareaBean
 */
@Stateless
@Local(EstadoTareaDAO.class)
public class EstadoTareaDAOImpl extends GenericDAO<EstadoTarea, Integer> implements EstadoTareaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public EstadoTareaDAOImpl() {
        super(EstadoTarea.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
