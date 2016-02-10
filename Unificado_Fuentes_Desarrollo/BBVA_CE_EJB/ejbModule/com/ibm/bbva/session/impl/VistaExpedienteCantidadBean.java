package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.VistaExpedienteCantidad;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.VistaExpedienteCantidadBeanLocal;

@Stateless
@Local(VistaExpedienteCantidadBeanLocal.class)
public class VistaExpedienteCantidadBean extends AbstractFacade<VistaExpedienteCantidad> implements VistaExpedienteCantidadBeanLocal{
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public VistaExpedienteCantidadBean() {
    	super(VistaExpedienteCantidad.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<VistaExpedienteCantidad> buscarPorIdProdIdTerrIdPer(long idProducto, long idTerritorio, long idPerfil){
		List<VistaExpedienteCantidad> listVistaExpedienteCantidad = em.createNamedQuery("VistaExpedienteCantidad.findByIdProdIdTerrIdPer")
				.setParameter("idProducto", idProducto)
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("idPerfil", idPerfil)
				.getResultList();
		return listVistaExpedienteCantidad;
	}
	
	@Override
	public List<VistaExpedienteCantidad> buscarPorIdProdIdTerrIdOfIdPer(long idProducto, long idTerritorio, long idOficina, long idPerfil){
		List<VistaExpedienteCantidad> listVistaExpedienteCantidad = em.createNamedQuery("VistaExpedienteCantidad.findByIdProdIdTerrIdOfIdPer")
				.setParameter("idProducto", idProducto)
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("idOficina", idOficina)
				.setParameter("idPerfil", idPerfil)
				.getResultList();
		return listVistaExpedienteCantidad;
	}
	
	@Override
	public List<VistaExpedienteCantidad> buscarPorIdProdIdPer(long idProducto, long idPerfil){
		List<VistaExpedienteCantidad> listVistaExpedienteCantidad = em.createNamedQuery("VistaExpedienteCantidad.findByIdProdIdPer")
				.setParameter("idProducto", idProducto)
				.setParameter("idPerfil", idPerfil)
				.getResultList();
		return listVistaExpedienteCantidad;
	}
	@Override
	public List<VistaExpedienteCantidad> buscarPorIdEmpleado(long idEmpleado){
		try{
			List<VistaExpedienteCantidad> listVistaExpedienteCantidad = em.createNamedQuery("VistaExpedienteCantidad.findByIdEmpleado")
					.setParameter("idEmpleado", idEmpleado)
					.getResultList();
			return listVistaExpedienteCantidad;
		}
		catch(Exception e){
			return null;
		}
	}
	@Override
	public VistaExpedienteCantidad cantidadExpPorIdEmpleado(long idEmpleado){
		try{
			VistaExpedienteCantidad vistaExpedienteCantidad = new VistaExpedienteCantidad();
			List<Object[]> result = em.createNamedQuery("VistaExpedienteCantidad.sumExpByIdEmpleado")
					.setParameter("idEmpleado", idEmpleado)
					.getResultList();
			for (Object[] obj : result) {
				vistaExpedienteCantidad.setIdEmpleado((Long) obj[0]);
				vistaExpedienteCantidad.setNumExpedientes((Long) obj[1]);
			}
			return vistaExpedienteCantidad;
		}
		catch(Exception e){
			System.err.println("error: " + e.getMessage());
			return null;
		}
	}
	@Override
	public Map<Integer,Object> cantidadExpPorEmpleado(){
		try{
			List<Object[]> result = em.createNamedQuery("VistaExpedienteCantidad.sumExpByAllEmpleado")
					.getResultList();
			Map<Integer,Object> mapCantidadExpPorEmpleado = new TreeMap<Integer, Object>();
			for (Object[] obj : result) {
				Long id = (Long) obj[0];
				Long valor = (Long) obj[1];
				mapCantidadExpPorEmpleado.put(id.intValue(), valor.intValue()); 
			}
			return mapCantidadExpPorEmpleado;
		}
		catch(Exception e){
			System.err.println("error: " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<VistaExpedienteCantidad> cantidadExpPorIdPerfilIdProd(long idPerfil, long idProducto){
	    try{
	      List listVistaExpedienteCantidad = new ArrayList();
	      List<Object[]> result = this.em.createNamedQuery("VistaExpedienteCantidad.sumExpByIdPerfilIdProd")
	    		  .setParameter("idPerfil", Long.valueOf(idPerfil))
	    		  .setParameter("idProducto", Long.valueOf(idProducto))
	    		  .getResultList();
	      for (Object[] obj : result) {
	        VistaExpedienteCantidad vistaExpedienteCantidad = new VistaExpedienteCantidad();
	        vistaExpedienteCantidad.setIdEmpleado(((Long)obj[0]).longValue());
	        vistaExpedienteCantidad.setNumExpedientes(((Long)obj[1]).longValue());
	        listVistaExpedienteCantidad.add(vistaExpedienteCantidad);
	        vistaExpedienteCantidad = null;
	      }
	      return listVistaExpedienteCantidad;
	    }
	    catch (Exception e) {
	      System.err.println("error: " + e.getMessage()); }
	    return null;
	  }
	
	@Override
	public List<VistaExpedienteCantidad> cantidadExpPorIdPerfilIdProdIdOfi(long idPerfil, long idProducto, long idOficina){
	    try{
	      List listVistaExpedienteCantidad = new ArrayList();
	      List<Object[]> result = this.em.createNamedQuery("VistaExpedienteCantidad.sumExpByIdPerfilIdProdIdOfi")
	    		  .setParameter("idPerfil", Long.valueOf(idPerfil))
	    		  .setParameter("idProducto", Long.valueOf(idProducto))
	    		  .setParameter("idOficina", Long.valueOf(idOficina))
	    		  .getResultList();
	      for (Object[] obj : result) {
	        VistaExpedienteCantidad vistaExpedienteCantidad = new VistaExpedienteCantidad();
	        vistaExpedienteCantidad.setIdEmpleado(((Long)obj[0]).longValue());
	        vistaExpedienteCantidad.setNumExpedientes(((Long)obj[1]).longValue());
	        listVistaExpedienteCantidad.add(vistaExpedienteCantidad);
	        vistaExpedienteCantidad = null;
	      }
	      return listVistaExpedienteCantidad;
	    }
	    catch (Exception e) {
	      System.err.println("error: " + e.getMessage()); }
	    return null;
	  }

}
