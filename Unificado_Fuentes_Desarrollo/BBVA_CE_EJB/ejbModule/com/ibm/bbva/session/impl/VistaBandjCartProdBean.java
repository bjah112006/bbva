package com.ibm.bbva.session.impl;


import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CartTerritorioCE;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.VistaBandjCartProd;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.VistaBandjCartProdBeanLocal;

/**
 * Session Bean implementation class VistaBandjCartProdBean
 */
@Stateless
@Local(VistaBandjCartProdBeanLocal.class)
public class VistaBandjCartProdBean extends AbstractFacade<VistaBandjCartProd> implements VistaBandjCartProdBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public VistaBandjCartProdBean() {
        // TODO Auto-generated constructor stub
    	super(VistaBandjCartProd.class);
    }


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	/**
	 * Cambio 08 Mayo 2015 
	 * Carterización por Producto
	 * */
	
	@Override
	public List<VistaBandjCartProd> verificarCartXProducto(long idPerfil, long idTerritorio, long idEmpleado) {
		List<VistaBandjCartProd> resultList;
		String query = "SELECT v FROM VistaBandjCartProd v " +
				"WHERE v.idPerfil = :idPerfil " +
				"AND v.idTerritorio =:idTerritorio " +
				"AND v.idEmpleado =:idEmpleado";
		
		/*String query="SELECT CARTERR from  CartTerritorioCE CARTERR " +
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
		        "AND CAREMP.flagActivo LIKE :flagActivo )";*/
		
		System.out.println("query = "+query);
		try{
			resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idTerritorio", idTerritorio)
					.setParameter("idEmpleado", idEmpleado)
					.getResultList();		
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	
	@Override
	public List<VistaBandjCartProd> buscarPorPerfil_CartProd(long idPerfil, List<Long> listIdsProd) {
		//String idActivo="1";
		
		String query="SELECT v FROM VistaBandjCartProd v " +
				"	WHERE v.idPerfil = :idPerfil " +
				"	AND v.idProducto in (:listIdsProd)";
		
		System.out.println("query = "+query);
		try{
			List<VistaBandjCartProd> resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("listIdsProd", listIdsProd)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<VistaBandjCartProd> buscarPorPerfilEmpleadoActivo(long idPerfil, long idOficina, List<Long> listIdsProd) {
		//String idActivo="1";
		String query="SELECT v FROM VistaBandjCartProd v " +
				"	WHERE v.idPerfil = :idPerfil " +
				"	AND v.idOficina = :idOficina " +
				"	AND v.idProducto in (:listIdsProd)";
		System.out.println("query = "+query);
		try{
			List<VistaBandjCartProd> resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idOficina", idOficina)
					.setParameter("listIdsProd", listIdsProd)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	
	/**
	 * */
	
}
