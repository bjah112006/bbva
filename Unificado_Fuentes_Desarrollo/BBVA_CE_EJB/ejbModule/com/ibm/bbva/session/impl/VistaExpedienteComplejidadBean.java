package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.VistaExpedienteCantidad;
import com.ibm.bbva.entities.VistaExpedienteComplejidad;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.VistaExpedienteCantidadBeanLocal;
import com.ibm.bbva.session.VistaExpedienteComplejidadBeanLocal;

@Stateless
@Local(VistaExpedienteComplejidadBeanLocal.class)
public class VistaExpedienteComplejidadBean extends AbstractFacade<VistaExpedienteComplejidad>  implements VistaExpedienteComplejidadBeanLocal{
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public VistaExpedienteComplejidadBean(){
		super(VistaExpedienteComplejidad.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<VistaExpedienteComplejidad> buscarPorIdProdIdTerrIdPer(long idProducto, long idTerritorio, long idPerfil){
		List<VistaExpedienteComplejidad> listVistaExpedienteComplejidad = em.createNamedQuery("VistaExpedienteComplejidad.findByIdProdIdTerrIdPer")
				.setParameter("idProducto", idProducto)
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("idPerfil", idPerfil)
				.getResultList();
		return listVistaExpedienteComplejidad;
	}
}
