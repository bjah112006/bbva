package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.entities.VerificacionExp;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.VerificacionExpBeanLocal;

/**
 * Session Bean implementation class VerificacionExpBean
 */
@Stateless
@Local(VerificacionExpBeanLocal.class)
public class VerificacionExpBean extends AbstractFacade<VerificacionExp> implements VerificacionExpBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public VerificacionExpBean() {
    	super(VerificacionExp.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}
    
    @Override
	public VerificacionExp buscarPorId(long id) {
    	VerificacionExp objVerificacionExp=null;
        try{     
        	objVerificacionExp=(VerificacionExp) em.createNamedQuery("VerificacionExp.findById")
        			.setParameter("id", id)
        			.getSingleResult();
            
         }catch (Exception e) {
        	 objVerificacionExp = null;
 		}    	
		return objVerificacionExp;
	}
    
    @Override
	public List<VerificacionExp> buscarPorExpediente(long id) {
		List<VerificacionExp> resultList = em.createNamedQuery("VerificacionExp.findByExpediente").setParameter("id", id).getResultList();
		return resultList;
	}
    
    @Override
	public List<VerificacionExp> buscarPorExpTipoVer(long idExp, long idTipoVer) {
		List<VerificacionExp> resultList = em.createNamedQuery("VerificacionExp.findByExpTipoVer").setParameter("idExp", idExp).setParameter("idTipoVer", idTipoVer).getResultList();
		return resultList;
	}
    
    @Override
	public VerificacionExp reporteHistPlano(long idExp, long idTipoVer) {
    
    	VerificacionExp result = null;
    	
		String query = "SELECT v1 " +
                       "FROM VerificacionExp v1 " +
                       "WHERE v1.id IN "+
                       "  (SELECT MAX(v.id) " +
                       "   FROM VerificacionExp v" +
                       "   WHERE v.tipoVerificacion.id = :idTipoVer "+
                       "   GROUP BY v.expediente.id) " +
                       "AND v1.expediente.id= :idExp ";
		
	    Query q = em.createQuery(query);
	
	    if ( idExp > 0 ) {
	        q.setParameter("idExp", idExp);	
        }
        if ( idTipoVer > 0 ) {
	        q.setParameter("idTipoVer", idTipoVer);
	    }
    	
        try{        
           result = (VerificacionExp) q.getSingleResult();
        }catch (Exception e) {
        	result = new VerificacionExp();
		}
		return result;
	}
}
