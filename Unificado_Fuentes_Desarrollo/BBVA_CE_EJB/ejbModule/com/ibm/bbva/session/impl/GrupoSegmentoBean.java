package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.GrupoSegmento;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.GrupoSegmentoBeanLocal;

/**
 * Session Bean implementation class GrupoSegmento
 */
@Stateless
@Local(GrupoSegmentoBeanLocal.class)
public class GrupoSegmentoBean extends AbstractFacade<GrupoSegmento> implements GrupoSegmentoBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public GrupoSegmentoBean() {
        super(GrupoSegmento.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<GrupoSegmento> buscarTodos() {
		return copyResultList(em.createNamedQuery("GrupoSegmento.findAll").getResultList());
	}

	@Override
	public GrupoSegmento buscarPorId(long id) {
		GrupoSegmento result = null;
		try {
			 result = (GrupoSegmento) em.createNamedQuery("GrupoSegmento.findById")
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

}
