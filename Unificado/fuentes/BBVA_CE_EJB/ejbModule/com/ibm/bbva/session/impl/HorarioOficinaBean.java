package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.HorarioOficina;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.HorarioOficinaBeanLocal;

/**
 * Session Bean implementation class HorarioOficinaBean
 */
@Stateless
@Local(HorarioOficinaBeanLocal.class)
public class HorarioOficinaBean extends AbstractFacade<HorarioOficina> implements HorarioOficinaBeanLocal {


	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public HorarioOficinaBean() {
        super(HorarioOficina.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<HorarioOficina> buscarTodos() {
		List<HorarioOficina> resultList = em.createNamedQuery("HorarioOficina.findAll").getResultList();
		return resultList;
	}

	@Override
	public HorarioOficina buscarPorId(long id) {
		return (HorarioOficina) em.createNamedQuery("HorarioOficina.findById").setParameter("id", id).getSingleResult();
	}
	
	@Override
	public HorarioOficina buscarPorIdOficinaIdHorario(long idOficina, long idHorario) throws Exception{
		
		return (HorarioOficina) em.createNamedQuery("HorarioOficina.findByIdOficinaIdHorario").
				setParameter("idOficina", idOficina).
				setParameter("idHorario", idHorario)
				.getSingleResult();
	}
	
	@Override
	public List<HorarioOficina> buscarPorIdHorario(long idHorario) {
		List<HorarioOficina> resultList =  em.createNamedQuery("HorarioOficina.findByIdHorario").setParameter("idHorario", idHorario).getResultList();
		return resultList;
	}
	
	@Override
	public List<HorarioOficina> buscarPorIdOficina(long idOficina) {
		List<HorarioOficina> resultList =  em.createNamedQuery("HorarioOficina.findByIdOficina").setParameter("idOficina", idOficina).getResultList();
		return resultList;
	}
	
	@Override
	public List<HorarioOficina> buscarPorIdOficinaActivosHorarios(long idOficina) {
		String idActivo="1";
		String query="SELECT h FROM HorarioOficina h WHERE h.oficina.id = :idOficina and h.oficina.flagActivo like :idActivo and h.horario.activo  like :idActivo";
		System.out.println("idOficina:::"+idOficina+" - query HorarioOficina:::"+query);
		try{
			List<HorarioOficina> resultList = em.createQuery(query)
					.setParameter("idOficina", idOficina)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}
		/*
		List<HorarioOficina> resultList =  em.createNamedQuery("HorarioOficina.findByIdOficina").setParameter("idOficina", idOficina).getResultList();
		return resultList;*/
	}

	@Override
	public List<HorarioOficina> buscarTodaOficinaActivosHorarios() {
		String idActivo="1";
		String query="SELECT h FROM HorarioOficina h WHERE h.oficina.flagActivo like :idActivo and h.horario.activo  like :idActivo";
		System.out.println("Query HorarioOficina:::"+query);
		try{
			List<HorarioOficina> resultList = em.createQuery(query)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}
		/*
		List<HorarioOficina> resultList =  em.createNamedQuery("HorarioOficina.findByIdOficina").setParameter("idOficina", idOficina).getResultList();
		return resultList;*/
	}	
	
	@Override
	public List<HorarioOficina> buscarXcriterios(HorarioOficina h) {
		String where = "";
		List<HorarioOficina> resultList = new ArrayList<HorarioOficina>();
		
		return resultList;
	}

	@Override
	public HorarioOficina create(HorarioOficina entity) {		
		return super.create(entity);
	}

	@Override
	public void edit(HorarioOficina entity) {
		super.edit(entity);
	}
}
