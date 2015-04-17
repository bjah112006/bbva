package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Territorio;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TerritorioBeanLocal;

/**
 * Session Bean implementation class TerritorioBean
 */
@Stateless
@Local(TerritorioBeanLocal.class)
public class TerritorioBean extends AbstractFacade<Territorio> implements TerritorioBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public TerritorioBean() {
    	super(Territorio.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public Territorio buscarPorId(long id) {
    	try{
    		return (Territorio) em.createNamedQuery("Territorio.findById")
    				.setParameter("id", id)
    				.getSingleResult();
    	}catch(NoResultException e){
			return null;
		}
    	
		
	}    
    
    @Override
	public List<Territorio> buscarTodos() {
		List<Territorio> resultList = em.createNamedQuery("Territorio.findAll").getResultList();
		return resultList;
	}
    
    @Override
	public List<Territorio> buscarPorFlagProvincia(String flagProv) {
		List<Territorio> resultList;
		String Query="SELECT t FROM Territorio t WHERE t.flagProv = :flagProv and t.flagActivo = :flagActivo";
		

		System.out.println("query = "+Query);
		try{
			resultList = em.createQuery(Query)
					.setParameter("flagProv", flagProv)
					.setParameter("flagActivo", "1")
					.getResultList();		
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
    @Override
	public Territorio buscarPorCodigo(String codigo) {
    	List<Territorio> resultList;
		String Query="SELECT t FROM Territorio t WHERE t.codigo = :codigo";
		try{
			resultList = em.createQuery(Query)
					.setParameter("codigo", codigo)
					.getResultList();		
			return resultList.get(0);			
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public Territorio create(Territorio entity){
		return super.create(entity);
	}
    
    @Override
    public void edit(Territorio entity){
    	super.edit(entity);
	}

}
