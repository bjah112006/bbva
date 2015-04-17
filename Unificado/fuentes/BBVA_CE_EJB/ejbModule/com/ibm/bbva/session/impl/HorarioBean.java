package com.ibm.bbva.session.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.HorarioBeanLocal;

/**
 * Session Bean implementation class HorarioBean
 */
@Stateless
@Local(HorarioBeanLocal.class)
public class HorarioBean extends AbstractFacade<Horario> implements HorarioBeanLocal {

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public HorarioBean() {
        super(Horario.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Horario> buscarTodos() {
		List<Horario> resultList = em.createNamedQuery("Horario.findAll").getResultList();
		return resultList;
	}

	@Override
	public Horario buscarPorId(long id) {
		return (Horario) em.createNamedQuery("Horario.findById").setParameter("id", id).getSingleResult();
	}
	
	@Override
	public List<Horario> buscarXcriterios(Horario h) {
		String where = "";
		
		if ( h.getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"h.id = "+h.getId();
		}
		if ( h.getActivo()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.activo = '"+h.getActivo()+"'";
		}
		if ( h.getDom()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.dom = "+h.getDom();
		}
		if ( h.getExcepcion()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.excepcion = '"+h.getExcepcion()+"'";
		}
		if ( h.getHoraFin()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.horaFin = '"+h.getHoraFin()+"'";
		}
		if ( h.getHoraInicio()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.horaInicio = '"+h.getHoraInicio()+"'";
		}
		if ( h.getDiaFin()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.diaFin = "+h.getDiaFin();
		}
		if ( h.getDiaInicio()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.diaInicio = "+h.getDiaInicio();
		}
		if ( h.getJue()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.jue = "+h.getJue();
		}
		if ( h.getLun()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.lun = "+h.getLun();
		}
		if ( h.getMar()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.mar = "+h.getMar();
		}
		if ( h.getMie()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.mie = "+h.getMie();
		}
		if ( h.getSab()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.sab = "+h.getSab();
		}
		if ( h.getVie()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.vie = "+h.getVie();
		}
		if ( h.getTodoDia()!=null ) {
			where += (where.length()==0? "" : " AND ")+"h.todoDia = '"+h.getTodoDia()+"'";
		}
		
		String query = "SELECT h FROM Horario h"+(where.length()==0? "" : " WHERE "+where);
		
        //System.out.println("query : "+query);
		
		List<Horario> resultList = em.createQuery(query).getResultList();
		
		return resultList;
	}

	@Override
	public List<Horario>  buscarHorarioNormalXoficina(long oficina) {
		
		String query = " SELECT DISTINCT h FROM HorarioOficina ho, Horario h, Oficina o "+
		               " WHERE ho.horario.id = h.id "+
		               " AND h.excepcion = '0' "+
		               " AND h.activo = '1' "+
		               " AND ho.oficina.id = o.id "+
		               " AND o.id = "+oficina;		
		
		List<Horario> resultList = em.createQuery(query).getResultList();		
		return resultList;
	}
	
	@Override
	public Horario buscarFeriado(Date fechaInicio, String codOficina) {
		String queryStr = "select h.horario from HorarioOficina h" +
				" where h.horario.diaInicio = :fechaInicio" +
				" and h.oficina.codigo = :codOficina" +
				" and h.horario.todoDia = '1'" +
				" and h.horario.excepcion = '1'";
		Query query = em.createQuery(queryStr);
		query.setParameter("fechaInicio", fechaInicio, TemporalType.DATE);
		query.setParameter("codOficina", codOficina);
		List<Horario> resultList = query.getResultList();
		if (resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Horario create(Horario entity) {
		return super.create(entity);
	}

	@Override
	public void edit(Horario entity) {
		super.edit(entity);
	}

	@Override
	public void remove(Horario entity) {
		super.remove(entity);
	}
}
