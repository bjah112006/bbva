package com.ibm.bbva.session.impl;

import java.util.List;

import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TipoClienteBean
 */
@Stateless
@Local(TipoClienteBeanLocal.class)
public class TipoClienteBean extends AbstractFacade<TipoCliente> implements TipoClienteBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TipoClienteBean() {
    	super(TipoCliente.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<TipoCliente> buscarTodos() {
		List<TipoCliente> resultList = em.createNamedQuery("TipoCliente.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public TipoCliente buscarPorId(long id) {
		return (TipoCliente) em.createNamedQuery("TipoCliente.findById").setParameter("id", id).getSingleResult();
	}
	
	@Override
	public TipoCliente buscarPorCodigo(String codigo) {
		return (TipoCliente) em.createNamedQuery("TipoCliente.findByCodigo").setParameter("codigo", codigo).getSingleResult();
	}

}
