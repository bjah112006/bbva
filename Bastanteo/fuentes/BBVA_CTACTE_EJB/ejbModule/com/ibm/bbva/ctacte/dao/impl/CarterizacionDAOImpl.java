package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Carterizacion;
import com.ibm.bbva.ctacte.bean.CarterizacionId;
import com.ibm.bbva.ctacte.dao.CarterizacionDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class CarterizacionBean
 */
@Stateless
@Local(CarterizacionDAO.class)
public class CarterizacionDAOImpl extends GenericDAO<Carterizacion, CarterizacionId> implements CarterizacionDAO{

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public CarterizacionDAOImpl() {
		super(Carterizacion.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<Carterizacion> buscarTodos() {
		Query query = em.createQuery(
	 			"SELECT o FROM Carterizacion o");
			List<Carterizacion> carterizacion = query.getResultList();
			return carterizacion;
	}
	
	public List<String> obtenerListaCodigos() {
		Query query = em.createQuery(
	 			"SELECT DISTINCT(o.codigo) FROM Carterizacion o");
			List<String> codigos = query.getResultList();
			return codigos;
	}
	
	@Override
	public List<Carterizacion> resultadoBusqueda(Carterizacion g){
		String where = "";
		
		if ( g.getProductoCE()!=null && g.getProductoCE().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"g.productoCE.id =:idProducto";
		}
		
		if ( g.getTerritorio()!=null && g.getTerritorio().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"g.territorio.id =:idTerritorio";
		}
		
		if ( g.getEmpleado()!=null && g.getEmpleado().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"g.empleado.id =:idEmpleado";
		}
		System.out.println("CODIGO:"+g.getCodigo());
		if ( g.getCodigo()!=null && g.getCodigo().trim() != "" && g.getCodigo().trim() != "-1") {
			where += (where.length()==0? "" : " AND ")+"g.codigo =:codigo";
		}
		
		
		String query = "SELECT g FROM Carterizacion g "+(where.length()==0? "" : (" WHERE "+where));
				
		//List<Historial> resultList = em.createQuery(query).getResultList();
				
		Query sql = (Query) em.createQuery(query);
		
		if ( g.getProductoCE()!=null && g.getProductoCE().getId() > 0 ) {
			sql.setParameter("idProducto",g.getProductoCE().getId());
		}
		
		if ( g.getTerritorio()!=null && g.getTerritorio().getId() > 0 ) {
			sql.setParameter("idTerritorio",g.getTerritorio().getId());
		}
		
		if (  g.getEmpleado()!=null && g.getEmpleado().getId() > 0 ) {
			sql.setParameter("idEmpleado", g.getEmpleado().getId());
		}
		
		if (  g.getCodigo()!=null && g.getCodigo().length() > 0 ) {
			sql.setParameter("codigo", g.getCodigo());
		}
				
		//System.out.println("sql : "+sql);
		
		List<Carterizacion> resultList = sql.getResultList();
		
		return resultList; 
		
	}
	/*
	@Override
	public Carterizacion load(CarterizacionId id){
		try {			
			Query query = em.createQuery(
					"select o from Carterizacion o where o.idProducto = :idProducto and o.idTerritorio = :idTerritorio and o.idEmpleado :idEmpleado");
				query.setParameter("idProducto", String.valueOf(id.getIdProductoFk()));
				query.setParameter("idTerritorio", String.valueOf(id.getIdTerritorioFk()));
				query.setParameter("idEmpleado", String.valueOf(id.getIdEmpleadoFk()));
				
				Carterizacion carterizacion = (Carterizacion) query.getSingleResult();
				return carterizacion;
			
		} catch (NoResultException e) {
			return null;
		}		
	}
	*/
}