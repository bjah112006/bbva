package com.ibm.bbva.session.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.openjpa.lib.log.Log;

import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.AnsBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(AnsBeanLocal.class)
public class AnsBean extends AbstractFacade<Ans> implements AnsBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public AnsBean() {
        super(Ans.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	
			 
	@Override
	public Ans buscarPorId(long idProducto, long idTarea, long idTipoOferta, long idGrupoSegmento) {
		Ans result =null;
		try{
			 result = (Ans) em.createNamedQuery("Ans.findById")
					.setParameter("idProducto", idProducto)
					.setParameter("idTarea", idTarea)
	                .setParameter("idTipoOferta", idTipoOferta)
					.setParameter("idGrupoSegmento", idGrupoSegmento)				
					.setParameter("flagActivo", "1")
					.getSingleResult();
		}catch (Exception e) {
			return null;
		}
		return result;
	}
}