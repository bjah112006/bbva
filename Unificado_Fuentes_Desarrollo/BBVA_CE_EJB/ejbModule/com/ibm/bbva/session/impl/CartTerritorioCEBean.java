package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.CartTerritorioCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.CartTerritorioCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(CartTerritorioCEBeanLocal.class)
public class CartTerritorioCEBean extends AbstractFacade<CartTerritorioCE> implements CartTerritorioCEBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public CartTerritorioCEBean() {
        super(CartTerritorioCE.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	

	@Override
	public List<CartTerritorioCE> buscarPorIdTerr(long idTerritorio) {
		List<CartTerritorioCE> resultList = em.createNamedQuery("CartTerritorioCE.findIdTerr")
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("flagActivo", "1")				
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<CartTerritorioCE> buscarPorIdProdIdTerr(long idProducto, long idTerritorio) {
		List<CartTerritorioCE> resultList = em.createNamedQuery("CartTerritorioCE.findIdProdIdTerr")
				.setParameter("idProducto", idProducto)
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("flagActivo", "1")				
				.getResultList();
		return resultList;
	}
	
	
	@Override
	public List<CartTerritorioCE> verificarCartXProducto(long idPerfil, long idTerritorio, long idEmpleado) {
		List<CartTerritorioCE> resultList;
		String query="SELECT CARTERR from  CartTerritorioCE CARTERR " +
		        "INNER JOIN CARTERR.carterizacionCE CARTERZ " +
		        "WHERE CARTERR.carterizacionCE.id=CARTERZ.id  " +
		        "AND CARTERR.territorio.id= :idTerritorio " +
		        "AND CARTERR.flagActivo LIKE :flagActivo "+
		        "AND CARTERZ.flagActivo LIKE :flagActivo "+
		        "AND CARTERZ.id in (SELECT CAREMP.carterizacionCE.id from  CartEmpleadoCE CAREMP INNER JOIN CAREMP.empleado EMP " +
		        "Where CAREMP.empleado.id = EMP.id " +
		        "AND EMP.id=:idEmpleado " +
		        "AND EMP.perfil.id=:idPerfil " +
		        "AND EMP.flagActivo LIKE :flagActivo " +
		        "AND CAREMP.flagActivo LIKE :flagActivo )";
		
		System.out.println("query = "+query);
		try{
			resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idTerritorio", idTerritorio)
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("flagActivo", "1")
					.getResultList();		
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}	
}
