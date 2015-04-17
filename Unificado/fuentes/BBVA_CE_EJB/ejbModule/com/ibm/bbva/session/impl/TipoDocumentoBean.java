package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;

/**
 * Session Bean implementation class TipoDocumentoBean
 */
@Stateless
@Local(TipoDocumentoBeanLocal.class)
public class TipoDocumentoBean extends AbstractFacade<TipoDocumento> implements TipoDocumentoBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoDocumentoBean() {
        super(TipoDocumento.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public TipoDocumento buscarPorId(long id) {
		return (TipoDocumento) em.createNamedQuery("TipoDocumento.findById").setParameter("id", id).getSingleResult();
	}
	
	@Override
	public TipoDocumento buscarPorCodigo(String codigo) {
		return (TipoDocumento) em.createNamedQuery("TipoDocumento.findByCodigo").setParameter("codigo", codigo).getSingleResult();
	}
	
	@Override
	public List<TipoDocumento> buscar(){
		try{
			List<TipoDocumento> resultList = em.createNamedQuery("TipoDocumento.findAll").getResultList();
			return resultList;			
		}catch(NoResultException e){
			return null;
		}
	}
	
}
