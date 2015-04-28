package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.TareaExpedienteDAO;

/**
 * Session Bean implementation class TareaExpedienteBean
 */
@Stateless
@Local(TareaExpedienteDAO.class)
public class TareaExpedienteDAOImpl extends GenericDAO<ExpedienteTarea, Integer> implements TareaExpedienteDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public TareaExpedienteDAOImpl() {
        super(ExpedienteTarea.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
