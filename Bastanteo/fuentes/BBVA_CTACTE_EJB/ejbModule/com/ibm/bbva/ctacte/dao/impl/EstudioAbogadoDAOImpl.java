package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;

/**
 * Session Bean implementation class EstudioAbogadoBean
 */
@Stateless
@Local(EstudioAbogadoDAO.class)
public class EstudioAbogadoDAOImpl extends GenericDAO<EstudioAbogado, Integer> implements EstudioAbogadoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public EstudioAbogadoDAOImpl() {
        super(EstudioAbogado.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public EstudioAbogado findByIdEstudio(Integer idEstudio) {
		try {
			Query query = em.createQuery(
	 			"select o from EstudioAbogado o where o.id=:idEstudio");
			query.setParameter("idEstudio", idEstudio);
			EstudioAbogado estudioAbogado = (EstudioAbogado) query.getSingleResult();
			return estudioAbogado;
		} catch (NoResultException e) {
			return null;
		}
	}

}
