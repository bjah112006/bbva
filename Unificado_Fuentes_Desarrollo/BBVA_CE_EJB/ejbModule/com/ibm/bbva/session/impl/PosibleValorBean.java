package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.PosibleValor;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.PosibleValorBeanLocal;

/**
 * Session Bean implementation class PosibleValorBean
 */
@Stateless
@Local(PosibleValorBeanLocal.class)
public class PosibleValorBean  extends AbstractFacade<PosibleValor>  implements PosibleValorBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public PosibleValorBean() {
    	super(PosibleValor.class);
    }
    
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}    

	@Override
	public List<PosibleValor> buscarPorIdColumna(long idColumna) {
		try{
			List<PosibleValor> resultList = em.createNamedQuery("PosibleValor.findByIdColumna")
					.setParameter("idColumna", idColumna)
					.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<PosibleValor> buscarPorIdColumnaIdValor(long idColumna, String idValor) {
		try{
			List<PosibleValor> resultList = em.createNamedQuery("PosibleValor.findByIdColumnaIdValor")
					.setParameter("idColumna", idColumna)
					.setParameter("idValor", idValor.trim())
					.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}	
}
