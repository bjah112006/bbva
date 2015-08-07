package com.ibm.bbva.session.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.ibm.bbva.entities.OficinaTemporal;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.OficinaTemporalBeanLocal;

@Stateless
public class OficinaTemporalBean extends AbstractFacade<OficinaTemporal> implements OficinaTemporalBeanLocal
{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public OficinaTemporalBean() {
    	super(OficinaTemporal.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public OficinaTemporal create(OficinaTemporal entity){
		return super.create(entity);
	}

	@Override
	public void edit(OficinaTemporal entity){
		super.edit(entity);
	}
	
	@Override
	public OficinaTemporal buscarPorId(long id) 
	{
		try{			
			return (OficinaTemporal) em.createNamedQuery("OficinaTemporal.findById").setParameter("id", id).getSingleResult();			
		}catch(NoResultException e){
			return null;
		}
	}

	@Override
	public List<OficinaTemporal> buscar(long idEmpleado, Date fechaInicio, Date fechaFin, String horaInicio, String horaFin, String oficina, String estado) 
	{
		
		List<OficinaTemporal> resultList = null;
		StringBuilder sbQuery = new StringBuilder(" SELECT ot FROM OficinaTemporal ot ");
		sbQuery.append(" INNER JOIN ot.oficina o ");
		sbQuery.append(" INNER JOIN ot.empleado e ");
		sbQuery.append(" WHERE e.id = :empleado ");

		boolean isAnd = true;

		if(!estado.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" ot.estado = :estado ");
			isAnd = true;
		}
		if(!oficina.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" o.id = :oficina ");
			isAnd = true;
		}
		if(fechaInicio != null)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.fechaInicio >= :fechaInicio OR (ot.fechaInicio <= :fechaInicio AND ot.fechaFin >= :fechaInicio )) ");
			isAnd = true;
		}
		if(fechaFin != null)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.fechaFin <= :fechaFin OR (ot.fechaInicio <= :fechaFin AND ot.fechaFin >= :fechaFin )) ");
			isAnd = true;
		}
		if(horaInicio != null && horaInicio.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.horaInicio >= :horaInicio OR (ot.horaInicio <= :horaInicio AND ot.horaFin >= :horaInicio )) ");
			isAnd = true;
		}
		if(horaFin != null && horaFin.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.horaFin <= :horaFin OR (ot.horaInicio <= :horaFin AND ot.horaFin >= :horaFin )) ");
			isAnd = true;
		}
		
		sbQuery.append(" ORDER BY ot.fechaInicio desc");
		
		try{
			
			Query jpql = em.createQuery(sbQuery.toString());
			
			jpql.setParameter("empleado", idEmpleado);
			
			if(!estado.equals("-1"))
			{
				jpql.setParameter("estado", estado);
			}
			if(!oficina.equals("-1"))
			{
				jpql.setParameter("oficina", Long.parseLong(oficina));
			}
			if(fechaInicio != null)
			{
				jpql.setParameter("fechaInicio", fechaInicio);
			}
			if(horaInicio != null && horaInicio.length() > 0)
			{
				jpql.setParameter("horaInicio", horaInicio);
			}
			if(fechaFin != null)
			{
				jpql.setParameter("fechaFin", fechaFin);
			}
			if(horaFin != null && horaFin.length() > 0)
			{
				jpql.setParameter("horaFin", horaFin);
			}
			
			resultList = jpql.getResultList();	
						
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}	
		
	}

	@Override
	public List<OficinaTemporal> buscarTraslape(long idOficinaTemporal, long idEmpleado, Date fechaInicio, Date fechaFin, String horaInicio, String horaFin, String estado) 
	{
		
		List<OficinaTemporal> resultList = null;
		StringBuilder sbQuery = new StringBuilder(" SELECT ot FROM OficinaTemporal ot ");
		sbQuery.append(" INNER JOIN ot.oficina o ");
		sbQuery.append(" INNER JOIN ot.empleado e ");
		sbQuery.append(" WHERE e.id = :empleado ");

		boolean isAnd = true;

		if(idOficinaTemporal != -1L)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" ot.id <> :id ");
			isAnd = true;
		}
		if(!estado.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" ot.estado = :estado ");
			isAnd = true;
		}
		if(fechaInicio != null)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.fechaInicio >= :fechaInicio OR (ot.fechaInicio <= :fechaInicio AND ot.fechaFin >= :fechaInicio )) ");
			isAnd = true;
		}
		if(fechaFin != null)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.fechaFin <= :fechaFin OR (ot.fechaInicio <= :fechaFin AND ot.fechaFin >= :fechaFin )) ");
			isAnd = true;
		}
		if(horaInicio != null && horaInicio.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.horaInicio >= :horaInicio OR (ot.horaInicio <= :horaInicio AND ot.horaFin >= :horaInicio )) ");
			isAnd = true;
		}
		if(horaFin != null && horaFin.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }			
			sbQuery.append(" (ot.horaFin <= :horaFin OR (ot.horaInicio <= :horaFin AND ot.horaFin >= :horaFin )) ");
			isAnd = true;
		}
		
		sbQuery.append(" ORDER BY o.id ");
		
		try{
			
			Query jpql = em.createQuery(sbQuery.toString());
			
			jpql.setParameter("empleado", idEmpleado);
			
			if(!estado.equals("-1"))
			{
				jpql.setParameter("estado", estado);
			}
			if(fechaInicio != null)
			{
				jpql.setParameter("fechaInicio", fechaInicio);
			}
			if(horaInicio != null && horaInicio.length() > 0)
			{
				jpql.setParameter("horaInicio", horaInicio);
			}
			if(fechaFin != null)
			{
				jpql.setParameter("fechaFin", fechaFin);
			}
			if(horaFin != null && horaFin.length() > 0)
			{
				jpql.setParameter("horaFin", horaFin);
			}
			if(idOficinaTemporal != -1L)
			{
				jpql.setParameter("id", idOficinaTemporal);
			}
			resultList = jpql.getResultList();	
						
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}	
		
	}
	
	@Override
	public List<OficinaTemporal> obtenerActual(long idEmpleado) 
	{
		StringBuilder sbQuery = new StringBuilder(" SELECT ot FROM OficinaTemporal ot ");
		sbQuery.append(" INNER JOIN ot.empleado e ");
		sbQuery.append(" WHERE (:fechaActual >= ot.fechaInicio AND :fechaActual <= ot.fechaFin) AND ");
		sbQuery.append(" 	   (ot.estado = :estado) AND ");
		sbQuery.append(" 	   (e.id= :empleado) ");
		
		try
		{			
			Query jpql = em.createQuery(sbQuery.toString());
			DateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");
			jpql.setParameter("fechaActual", dateformat.parse(dateformat.format(new Date())), TemporalType.DATE);
			jpql.setParameter("estado", "A");
			jpql.setParameter("empleado", idEmpleado);
			return jpql.getResultList();	
			
		}catch (NoResultException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}	
	}
	
}
