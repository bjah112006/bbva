package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(CartEmpleadoCEBeanLocal.class)
public class CartEmpleadoCEBean extends AbstractFacade<CartEmpleadoCE> implements CartEmpleadoCEBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public CartEmpleadoCEBean() {
        super(CartEmpleadoCE.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	
	
	/*@Override
	public List<CartEmpleadoCE> buscarPorIdCaract(long idCaract) {
		List<CartEmpleadoCE> resultList = em.createNamedQuery("CartEmpleadoCE.findIdCaract")
				.setParameter("idCaract", idCaract)
				.setParameter("flagActivo", "1")
				.getResultList();
		return resultList;
	}*/
	
	@Override
	public List<CartEmpleadoCE> buscarPorIdCaractIdPerfil(long idCaract, long idPerfil) {
		List<CartEmpleadoCE> resultList = em.createNamedQuery("CartEmpleadoCE.findIdCaractIdPerfil")
				.setParameter("idCaract", idCaract)
				.setParameter("idPerfil", idPerfil)
				.setParameter("flagActivo", "1")
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<CartEmpleadoCE> buscarPorIdCaractIdEmpleado(long idCaract, long idEmpleado) {
		List<CartEmpleadoCE> resultList = em.createNamedQuery("CartEmpleadoCE.findIdCaractIdEmpl")
				.setParameter("idCaract", idCaract)
				.setParameter("idEmpleado", idEmpleado)
				.getResultList();
		return resultList;
	}	
	
	@Override
	public List<CartEmpleadoCE> buscarPorIdEmpleado(long idEmpleado) {
		List<CartEmpleadoCE> resultList = em.createNamedQuery("CartEmpleadoCE.findIdEmpl")
				.setParameter("idEmpleado", idEmpleado)
				.getResultList();
		return resultList;
	}

	@Override
	public List<CartEmpleadoCE> buscarPorIdEmpleadoActivo(long idEmpleado) {
		List<CartEmpleadoCE> resultList = em.createNamedQuery("CartEmpleadoCE.findIdEmplActivo")
				.setParameter("idEmpleado", idEmpleado)
				.getResultList();
		return resultList;
	}
	
	@Override
	public Long contarPorIdCaractIdEmpleado(long idCaract, long idEmpleado) {
		Long result;
		String query="SELECT count(ce) from CartEmpleadoCE ce, Empleado em "+
		             "WHERE ce.empleado.id = em.id "+
		             "AND ce.carterizacionCE.id = :idCaract "+
		             "AND em.flagActivo = :flagActivo "+
		             "AND ce.flagActivo = :flagActivo "+
		             "AND em.perfil.id in ("+
		             "SELECT em1.perfil.id from Empleado em1 where em1.id = :idEmpleado)";
		System.out.println("query = "+query);
		try{
			result = (Long) em.createQuery(query)
					.setParameter("idCaract", idCaract)
					.setParameter("flagActivo", "1")
					.setParameter("idEmpleado", idEmpleado)
					.getSingleResult();		
			return result;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public List<CartEmpleadoCE> verificarCartEmpleado(long idPerfil, long idOficina, long idProducto, long idEmpleado) {
		List<CartEmpleadoCE> resultList;
		String query="SELECT CAREMP from  CartEmpleadoCE CAREMP " +
				"INNER JOIN CAREMP.empleado EMP "+
		        "INNER JOIN CAREMP.carterizacionCE CARTERZ " +
		        "WHERE CAREMP.empleado.id = EMP.id AND CAREMP.carterizacionCE.id=CARTERZ.id AND " +
		        "EMP.perfil.id=:idPerfil AND  EMP.oficina.id=:idOficina "+
		        "AND CARTERZ.id=:idProducto AND EMP.id=:idEmpleado "+ 
		        "AND CAREMP.flagActivo = :flagActivo "+
		        "AND CARTERZ.flagActivo = :flagActivo "+
		        "AND EMP.flagActivo = :flagActivo ";
		
		System.out.println("query = "+query);
		try{
			resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idOficina", idOficina)
					.setParameter("idProducto", idProducto)
					.setParameter("idEmpleado", idEmpleado)
					.setParameter("flagActivo", "1")
					.getResultList();		
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}		
}
