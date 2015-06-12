package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.VistaOficinasExpedientePerfil;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.VistaOficinasExpedientePerfilBeanLocal;

@Stateless
@Local(VistaOficinasExpedientePerfilBeanLocal.class)
public class VistaOficinasExpedientePerfilBean extends
		AbstractFacade<VistaOficinasExpedientePerfil> implements
		VistaOficinasExpedientePerfilBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
	public VistaOficinasExpedientePerfilBean() {
		super(VistaOficinasExpedientePerfil.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<VistaOficinasExpedientePerfil> buscarOfExpediente() {
		List<VistaOficinasExpedientePerfil> listVistaOficinasExpediente = em.createNamedQuery("VistaOficinasExpedientePerfil.findAll")
				.getResultList();
		return listVistaOficinasExpediente;
	}

	@Override
	public List<VistaOficinasExpedientePerfil> buscarOfExpPorPerfil(
			long idPerfil) {
		List<VistaOficinasExpedientePerfil> listVistaOficinasExpedientePerfil = em.createNamedQuery("VistaOficinasExpedientePerfil.findByIdPerfil")
				.setParameter("id_perfil", idPerfil)
				.getResultList();
		return listVistaOficinasExpedientePerfil;
	}	

}
