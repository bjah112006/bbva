package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ViewNumeroExpedientesEmpleado;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ViewNumeroExpedientesEmpleadoDAO;

/**
 * Session Bean implementation class ViewNumeroExpedientesEmpleadoBean
 */
@Stateless
@Local(ViewNumeroExpedientesEmpleadoDAO.class)
public class ViewNumeroExpedientesEmpleadoDAOImpl extends GenericDAO<ViewNumeroExpedientesEmpleado, Integer> implements ViewNumeroExpedientesEmpleadoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ViewNumeroExpedientesEmpleadoDAOImpl() {
        super(ViewNumeroExpedientesEmpleado.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<ViewNumeroExpedientesEmpleado> findListaNumeroExpedientesxEmpleado(
			Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea) {
		Query query = em.createQuery(
 			"select o from ViewNumeroExpedientesEmpleado o where o.idProducto=:idProd and o.idTerritorio=:idTerr and o.idTarea=:idTar");
		query.setParameter("idProd", intIdProducto);
		query.setParameter("idTerr", intIdTerritorio);
		query.setParameter("idTar", intIdTarea);
		List<ViewNumeroExpedientesEmpleado> listViewNumeroExpedientesEmpleado = query.getResultList();
		return listViewNumeroExpedientesEmpleado;
	}

	@Override
	public List<ViewNumeroExpedientesEmpleado> findListaNumeroExpedientesxEmpleadoPorEstudio(
			Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea,
			Integer intIdEstudio) {
		Query query = em.createQuery(
 			"select o from ViewNumeroExpedientesEmpleado o where o.idProducto=:idProd and o.idTerritorio=:idTerr and o.idTarea=:idTar and o.idEstudio=:idEstudio");
		query.setParameter("idProd", intIdProducto);
		query.setParameter("idTerr", intIdTerritorio);
		query.setParameter("idTar", intIdTarea);
		query.setParameter("idEstudio", intIdEstudio);
		List<ViewNumeroExpedientesEmpleado> listViewNumeroExpedientesEmpleado = query.getResultList();
		return listViewNumeroExpedientesEmpleado;
	}

	@Override
	public List<ViewNumeroExpedientesEmpleado> findListaEmpleadosSinExpedientes(
			Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea) {
		Query query = em.createQuery(
 			"select o from ViewNumeroExpedientesEmpleado o where o.idProducto=:idProd and o.idTerritorio=:idTerr and o.idTarea=:idTar and o.numExpedientesEmpleado=0");
		query.setParameter("idProd", intIdProducto);
		query.setParameter("idTerr", intIdTerritorio);
		query.setParameter("idTar", intIdTarea);
		List<ViewNumeroExpedientesEmpleado> viewNumeroExpedientesEmpleado = query.getResultList();
		return viewNumeroExpedientesEmpleado;
	}

	@Override
	public List<ViewNumeroExpedientesEmpleado> findListaEmpleadosSinExpedientesPorEstudio(
			Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea,
			Integer intIdEstudio) {
		Query query = em.createQuery(
 			"select o from ViewNumeroExpedientesEmpleado o where o.idProducto=:idProd and o.idTerritorio=:idTerr and o.idTarea=:idTar and o.idEstudio=:idEstudio and o.numExpedientesEmpleado=0");
		query.setParameter("idProd", intIdProducto);
		query.setParameter("idTerr", intIdTerritorio);
		query.setParameter("idTar", intIdTarea);
		query.setParameter("idEstudio", intIdEstudio);
		List<ViewNumeroExpedientesEmpleado> viewNumeroExpedientesEmpleado = query.getResultList();
		return viewNumeroExpedientesEmpleado;
	}

}
