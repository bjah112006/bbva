package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;

/**
 * Session Bean implementation class OperacionBean
 */
@Stateless
@Local(OperacionDAO.class)
public class OperacionDAOImpl extends GenericDAO<Operacion, Integer> implements OperacionDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public OperacionDAOImpl() {
        super(Operacion.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Operacion> getOperaciones(List<String> codigos) {
		Query query = em.createQuery(
 			"select o from Operacion o where o.codigoOperacion in (:codOpers) and o.estado=1 order by o.codigoOperacion");
		query.setParameter("codOpers", codigos);
		List<Operacion> operaciones = query.getResultList();
		return operaciones;
	}

	@Override
	public Operacion findByCodOperacion(String codOperacion) {
		try {
			Query query = em.createQuery(
	 			"select o from Operacion o where o.codigoOperacion=:codOperacion");
			query.setParameter("codOperacion", codOperacion);
			Operacion operacion = (Operacion) query.getSingleResult();
			return operacion;
		} catch (NoResultException e) {
			return null;
		}
	}

}
