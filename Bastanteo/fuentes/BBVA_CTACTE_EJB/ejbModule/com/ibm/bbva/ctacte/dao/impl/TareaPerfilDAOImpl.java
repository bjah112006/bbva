package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.TareaPerfil;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.TareaPerfilDAO;

/**
 * Session Bean implementation class TareaPerfilBean
 */
@Stateless
@Local(TareaPerfilDAO.class)
public class TareaPerfilDAOImpl extends GenericDAO<TareaPerfil, Integer> implements TareaPerfilDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public TareaPerfilDAOImpl() {
        super(TareaPerfil.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public TareaPerfil findTareaPerfilxTarea(Integer intIdTarea) {
		Query query = em.createQuery(
 			"select o from TareaPerfil o where o.tarea.id=:intIdTarea");
		query.setParameter("intIdTarea", intIdTarea);
		List<TareaPerfil> listTareaPerfil =  query.getResultList();
		TareaPerfil tareaPerfil =  new TareaPerfil();
		tareaPerfil = listTareaPerfil.get(0);
		return tareaPerfil;
	}

}
