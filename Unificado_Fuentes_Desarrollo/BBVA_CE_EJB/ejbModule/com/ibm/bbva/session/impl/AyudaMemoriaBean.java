package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.AyudaMemoria;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.AyudaMemoriaBeanLocal;

/**
 * Session Bean implementation class AyudaMemoriaBean
 */
@Stateless
@Local(AyudaMemoriaBeanLocal.class)
public class AyudaMemoriaBean extends AbstractFacade<AyudaMemoria> implements AyudaMemoriaBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public AyudaMemoriaBean() {
    	super(AyudaMemoria.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<AyudaMemoria> buscarPorIdExpediente(long idExpediente) {
		List<AyudaMemoria> resultList = em.createNamedQuery("AyudaMemoria.findByIdExpediente").setParameter("idExpediente", idExpediente).getResultList();
		return resultList;
	}

	@Override
	public AyudaMemoria buscarPorId(long idAyudaMemoria) {
		AyudaMemoria  result = (AyudaMemoria)em.createNamedQuery("AyudaMemoria.findById").setParameter("idAyudaMemoria", idAyudaMemoria).getSingleResult();
		return result;
	}
	@Override
	public AyudaMemoria create(AyudaMemoria entity) {
		return super.create(entity);
	}
	@Override
	public void delete(AyudaMemoria entity) {
		super.remove(entity);
	}
	@Override
	public void update(AyudaMemoria entity) {
		 super.edit(entity);
	}
}
