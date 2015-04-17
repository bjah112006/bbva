package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.SegmentoBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class SegmentoBean
 */
@Stateless
@Local(SegmentoBeanLocal.class)
public class SegmentoBean extends AbstractFacade<Segmento> implements SegmentoBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public SegmentoBean() {
    	super(Segmento.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public Segmento buscarPorId(long id) {
    	try{
    		return (Segmento) em.createNamedQuery("Segmento.findById").setParameter("id", id).getSingleResult();
    	}catch(NoResultException e){
			return null;
		} 
		
	}
    
    @Override
	public Segmento buscarPorCodigo(String codigo) {
		return (Segmento) em.createNamedQuery("Segmento.findByCodigo").setParameter("codigo", codigo).getSingleResult();
	}
    
    @Override
	public Segmento buscarPorCodigoSegmento(String codigoSegmento) {
		try{
			return (Segmento) em.createNamedQuery("Segmento.findByCodigoSegmento")
					.setParameter("codigoSegmento", codigoSegmento)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}    	
		
	}
    
    @Override
	public List<Segmento> buscarTodos() {
    	try{
			List<Segmento> resultList = em.createNamedQuery("Segmento.findAll").getResultList();
			return resultList;
    	}catch(NoResultException e){
			return null;
		} 
	}
}
