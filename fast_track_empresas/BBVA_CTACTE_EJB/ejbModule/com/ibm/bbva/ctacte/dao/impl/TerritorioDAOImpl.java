package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;

/**
 * Session Bean implementation class TerritorioBean
 */
@Stateless
@Local(TerritorioDAO.class)
public class TerritorioDAOImpl extends GenericDAO<Territorio, Integer> implements TerritorioDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public TerritorioDAOImpl() {
        super(Territorio.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

    @Override
	public List<Territorio> findAll() {
		Query query = em.createQuery(
 			"select o from Territorio o ");
		List<Territorio> territorios = query.getResultList();
		return territorios;
	}
    
    @Override
	public Territorio findByID(Long id) {
		String query="SELECT e FROM Territorio e WHERE e.id = :id";
		System.out.println("query = "+query);
		try{
			Territorio resultList = (Territorio) em.createQuery(query)
					.setParameter("id", id)
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
    
	@Override
	public List<Territorio> findAllOrderedByCodigo() {
		Query query = em
				.createQuery("select o from Territorio o order by o.codigo ASC");
		List<Territorio> territorios = query.getResultList();
		return territorios;
	}
	
}
