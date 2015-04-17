package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.DevolucionTarea;
import com.ibm.bbva.entities.MotivoDevolucion;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;

/**
 * Session Bean implementation class DevolucionTareaBean
 */
@Stateless
@Local(DevolucionTareaBeanLocal.class)
public class DevolucionTareaBean extends AbstractFacade<DevolucionTarea> implements DevolucionTareaBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
    public DevolucionTareaBean() {
    	super(DevolucionTarea.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public DevolucionTarea create(DevolucionTarea entity) {	
		return super.create(entity);
	}
	
	public List<DevolucionTarea> buscarPorIdExpedienteIdTarea(long idExpediente , long idTarea){
		List<DevolucionTarea> resultList = em
				.createNamedQuery("DevolucionTarea.findbyIdExpedienteIdTarea")
				.setParameter("idExpediente", idExpediente)
				.setParameter("idTarea", idTarea)
				.getResultList();
		return resultList;
	}
	
	

}
