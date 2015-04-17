package com.ibm.bbva.session.impl;

import java.math.BigDecimal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.MontoPeso;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.MontoPesoBeanLocal;

/**
 * Session Bean implementation class MontoPesoBean
 */
@Stateless
@Local(MontoPesoBeanLocal.class)
public class MontoPesoBean extends AbstractFacade<MontoPeso> implements MontoPesoBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public MontoPesoBean() {
        super(MontoPeso.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public MontoPeso buscarPorIdProducto(long idProducto, BigDecimal monto) {
		return (MontoPeso) em.createNamedQuery("MontoPeso.findByIdProducto")
				.setParameter("idProducto", idProducto)
				.setParameter("monto", monto)
				.getSingleResult();
				
	}
	
	@Override
	public MontoPeso buscarPorIdProductoIdTipoOfertaMonto(long idProducto, double monto, long idTipoMoneda) {
		String query = "SELECT e FROM MontoPeso e WHERE e.producto.id = :idProducto and " +
				" e.tipoMoneda.id=:idTipoMoneda and e.montoMinimo<=:monto AND e.montoMaximo>=:monto";
		
		System.out.println("query : "+query);	
		
		try{
			return (MontoPeso) em.createQuery(query)
					.setParameter("idProducto", idProducto)
					.setParameter("monto", monto)
					.setParameter("idTipoMoneda", idTipoMoneda)
					.getSingleResult();			
		}catch (NoResultException e) {
			return null;
		}

				
	}	
	
}
