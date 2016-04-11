package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.ctacte.bean.Mensaje;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.MensajeDAO;

/**
 * Session Bean implementation class MensajeDAOImpl
 */
@Stateless
@Local(MensajeDAO.class)
public class MensajeDAOImpl extends GenericDAO<Mensaje, Integer> implements MensajeDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public MensajeDAOImpl() {
        super(Mensaje.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
