package com.ibm.bbva.session.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.CarterizacionCE;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.CarterizacionCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(CarterizacionCEBeanLocal.class)
public class CarterizacionCEBean extends AbstractFacade<CarterizacionCE> implements CarterizacionCEBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
    /**
     * Default constructor. 
     */
	public CarterizacionCEBean() {
        super(CarterizacionCE.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	
	@Override
	public CarterizacionCE buscarPorIdProdIdTerrIdPerfil(String codigo) {
		return (CarterizacionCE) em.createNamedQuery("CarterizacionCE.findByCodigo").setParameter("codigo", codigo).getSingleResult();
		
	}
	
/*	@Override
	public Long verificarCartEmpleado(long idCaract, long idEmpleado) {
		Long result;
		String query="SELECT count(ce) from  CartEmpleadoCE CAREMP " +
				"INNER JOIN CAREMP.empleado EMP ON CAREMP.empleado.ID = EMP.ID "+
		        "INNER JOIN CAREMP.carterizacionCE CARTERZ ON CAREMP.carterizacionCE.ID=CARTERZ.ID "+
		        "AND EMP.perfil.ID=:idPerfil AND  EMP.oficina.ID=:idOficina "+
		        "CARTERZ.ID=:idProducto and EMP.id=:idEmpleado"+ 
		        "AND CAREMP.flagActivo = :flagActivo "+
		        "AND CARTERZ.flagActivo = :flagActivo "+
		        "AND EMP.flagActivo = :flagActivo ";
		
		String Query="  FROM CONELE.TBL_CE_IBM_CART_EMPLEADO_CE CAREMP "+
    "INNER JOIN CONELE.TBL_CE_IBM_EMPLEADO_CE EMP ON CAREMP.ID_EMPLEADO_FK = EMP.ID"+
    "INNER JOIN CONELE.TBL_CE_IBM_CART_TERRITORIO_CE CARTER ON CARTER.ID_CARTERIZACION_FK = CAREMP.ID_CARTERIZACION_FK"+
        "AND CAREMP.id_perfil_fk=6 AND CAREMP.id_oficina_fk=128"+ 
        "AND CARTER.id_producto_fk=2 AND CARTER.id_territorio_fk=1"+
        "AND EMP.id=128"+ 
        "AND CAREMP.flag_activo='1'"+
        "AND CARTER.flag_activo='1'"+ 
        "AND EMP.FLAG_ACTIVO= '1'";
		
		LOG.info("query = "+query);
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
	}*/		
}
