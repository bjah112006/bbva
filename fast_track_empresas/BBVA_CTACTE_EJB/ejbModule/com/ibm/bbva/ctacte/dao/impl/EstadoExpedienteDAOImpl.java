package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class EstadoExpedienteBean
 */
@Stateless
@Local(EstadoExpedienteDAO.class)
public class EstadoExpedienteDAOImpl extends GenericDAO<EstadoExpediente, Integer> implements EstadoExpedienteDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public EstadoExpedienteDAOImpl() {
        super(EstadoExpediente.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
