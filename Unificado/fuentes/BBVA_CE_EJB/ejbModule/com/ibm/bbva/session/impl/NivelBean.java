package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Nivel;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.NivelBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class NivelBean
 */
@Stateless
@Local(NivelBeanLocal.class)
public class NivelBean extends AbstractFacade<Nivel> implements NivelBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */

    public NivelBean() {
    	super(Nivel.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Nivel sinNivel(long idNivel) {
		
		String query="SELECT e FROM Nivel e WHERE e.id = :idNivel";
		
		try{
			return (Nivel) em.createQuery(query)
					.setParameter("idNivel", idNivel)
					.getSingleResult();		
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<Nivel>  buscarTodos() {
		List<Nivel> resultList = em.createNamedQuery("Nivel.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public Nivel  buscarPorId(long id) {
		try{
			Nivel result = (Nivel) em.createNamedQuery("Nivel.findById")
					.setParameter("id", id)
					.getSingleResult();
			return result;
		}catch(NoResultException e){
			return null;
		}
	}
}
