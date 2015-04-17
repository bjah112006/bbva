package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;

/**
 * Session Bean implementation class ClienteNaturalBean
 */
@Stateless
@Local(ClienteNaturalBeanLocal.class)
public class ClienteNaturalBean extends AbstractFacade<ClienteNatural> implements ClienteNaturalBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ClienteNaturalBean() {
        super(ClienteNatural.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public ClienteNatural buscarPorId(long id) {
		try{
			return (ClienteNatural) em.createNamedQuery("ClienteNatural.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public List<ClienteNatural> buscarPorTipoNumDoi(ClienteNatural clienteNatural) {
		List<ClienteNatural> resultList = em.createNamedQuery("ClienteNatural.findByTipoNumDoi").setParameter("idTipoDoi", clienteNatural.getTipoDoi().getId()).setParameter("numDoi", clienteNatural.getNumDoi()).getResultList();
		 return resultList;
	}

	@Override
	public ClienteNatural create(ClienteNatural entity) {
		return super.create(entity);
	}

	@Override
	public void edit(ClienteNatural entity) {
		super.edit(entity);
	}	
	
}
