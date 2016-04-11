package com.ibm.bbva.ctacte.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.bbva.ctacte.bean.HorarioCC;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.HorarioCCDAO;
import com.ibm.bbva.ctacte.vo.HorarioVO;

@Stateless
@Local(HorarioCCDAO.class)
public class HorarioCCDAOImpl extends GenericDAO<HorarioCC, Integer> implements HorarioCCDAO  {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public HorarioCCDAOImpl() {
        super(HorarioCC.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<HorarioCC> obtenerFeriados(HorarioVO horarioVO)
			throws DataAccessException {
		
		List<HorarioCC> excepcionesFInal = new ArrayList<HorarioCC>();
		List<HorarioCC> excepciones = new ArrayList<HorarioCC>();
		List<HorarioCC> excepcionesAplicaTodo = new ArrayList<HorarioCC>();
		List<HorarioCC> excepcionesAplicaOficina = new ArrayList<HorarioCC>();
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select h from HorarioCC h ");
		queryString.append(" join h.perfil p  ");
		queryString.append(" join h.oficina o  ");
		queryString.append(" where 1 = 1 and h.excepcion = '1' and  h.aplicaTodo <> '1' and  h.aplicaOficina <> '1'");
		queryString.append(" and o.id = :pIdOficina  ");
		queryString.append(" and p.id = :pIdPerfil  ");
		queryString.append("order by  h.fechaExcepcion desc ");
		
		Query query = em.createQuery(queryString.toString());
		
		query.setParameter("pIdOficina", horarioVO.getIdOficina());
		query.setParameter("pIdPerfil", horarioVO.getIdPerfil());

		excepciones = query.getResultList();
		
		StringBuilder queryStringTodo = new StringBuilder();
		queryStringTodo.append(" select h from HorarioCC h ");
		queryStringTodo.append(" join h.perfil p  ");
		queryStringTodo.append(" join h.oficina o  ");
		queryStringTodo.append(" where 1 = 1  and h.aplicaTodo = '1' ");
		
		queryStringTodo.append(" order by  h.fechaExcepcion desc ");
		
		Query query2 = em.createQuery(queryStringTodo.toString());
		
		

		excepcionesAplicaTodo = query2.getResultList();
		
		StringBuilder queryStringOficina = new StringBuilder();
		queryStringOficina.append(" select h from HorarioCC h ");
		queryStringOficina.append(" join h.perfil p  ");
		queryStringOficina.append(" join h.oficina o  ");
		queryStringOficina.append(" where 1 = 1 and  h.aplicaOficina = '1' and h.aplicaTodo <> '1' ");
		queryStringOficina.append(" and o.id = :pIdOficina  ");
	
		queryStringOficina.append(" order by  h.fechaExcepcion desc ");
		
		Query query3 = em.createQuery(queryStringOficina.toString());
		
		query3.setParameter("pIdOficina", horarioVO.getIdOficina());
		//query3.setParameter("pIdPerfil", horarioVO.getIdPerfil());

		excepcionesAplicaOficina = query3.getResultList();
		
			
		for( HorarioCC 	h : excepciones ){
			excepcionesFInal.add(h);
		}
		
		for( HorarioCC 	h : excepcionesAplicaTodo ){
			excepcionesFInal.add(h);
		}
		
		for( HorarioCC 	h : excepcionesAplicaOficina ){
			excepcionesFInal.add(h);
		}
		
		
		return excepcionesFInal;
	}

	@Override
	public HorarioCC getDayOFTheWeek(int idPerfil, int idOficina,
			int idDiaSeleccionado) throws DataAccessException {
		HorarioCC horario = null;
		try {

			StringBuilder queryString = new StringBuilder();
			queryString.append(" select h from HorarioCC h where h.perfil.id = "
					+ idPerfil + " and h.oficina.id = " + idOficina
					+ "and h.dia = " + idDiaSeleccionado);
			Query query = em.createQuery(queryString.toString());
			horario = (HorarioCC) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e);
			horario = null;
		}
		return horario;
	}

	@Override
	public List<HorarioCC> obtenerFeriadosPorPeriodo(Integer idOficina, Integer idPerfil,
			int dayFrom, int dayTo, int year) {
		List<HorarioCC> excepciones = new ArrayList<HorarioCC>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select h from HorarioCC h where h.perfil.id = "
				+ idPerfil + " and h.oficina.id = " + idOficina
				+ " and h.dia <= " + dayFrom + " and h.dia >= " + dayFrom
				+ " and h.fechaExcepcion is not null "
				+ " and EXTRACT(YEAR, h.fechaExcepcion) =  " + year);
		Query query = em.createQuery(queryString.toString());
		excepciones = query.getResultList();
		return excepciones;
	}

	@Override
	public List<HorarioCC> obtenerFeriadosPorPeriodo(Date fechaIni,
			Date fechaFin, int idPerfil, int idOficina) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		List<HorarioCC> excepcionesFInal = new ArrayList<HorarioCC>();
		List<HorarioCC> excepciones = new ArrayList<HorarioCC>();
		List<HorarioCC> excepcionesAplicaTodo = new ArrayList<HorarioCC>();
		List<HorarioCC> excepcionesAplicaOficina = new ArrayList<HorarioCC>();

		try {
			
			
			StringBuilder queryString = new StringBuilder();
			queryString.append(" select h from HorarioCC h ");
			queryString.append(" join h.perfil p  ");
			queryString.append(" join h.oficina o  ");
			queryString
					.append(" where 1 = 1 and h.excepcion = '1' and  h.aplicaTodo <> '1' and  h.aplicaOficina <> '1' ");
			queryString.append(" and o.id = :pIdOficina  ");
			queryString.append(" and p.id = :pIdPerfil  ");
			queryString.append(" and h.fechaExcepcion >=  :pFechaIni  and h.fechaExcepcion <= :pFechaFin ");
			
			queryString.append(" order by  h.fechaExcepcion desc , h.aplicaOficina desc , h.aplicaTodo ");
			
			Query query = em.createQuery(queryString.toString());
	
			query.setParameter("pIdOficina", idOficina);
			query.setParameter("pIdPerfil", idPerfil);
			query.setParameter("pFechaIni", df.parse(df.format(fechaIni)));
			query.setParameter("pFechaFin", df.parse(df.format(fechaFin)));
	
			excepciones = query.getResultList();
	
			StringBuilder queryStringTodo = new StringBuilder();
			queryStringTodo.append(" select h from HorarioCC h ");
			queryStringTodo.append(" join h.perfil p  ");
			queryStringTodo.append(" join h.oficina o  ");
			queryStringTodo.append(" where 1 = 1  and h.aplicaTodo = '1' ");
	
			queryStringTodo.append(" order by  h.fechaExcepcion desc ");
	
			Query query2 = em.createQuery(queryStringTodo.toString());
	
			excepcionesAplicaTodo = query2.getResultList();
	
			StringBuilder queryStringOficina = new StringBuilder();
			queryStringOficina.append(" select h from HorarioCC h ");
			queryStringOficina.append(" join h.perfil p  ");
			queryStringOficina.append(" join h.oficina o  ");
			queryStringOficina
					.append(" where 1 = 1 and  h.aplicaOficina = '1' and h.aplicaTodo <> '1' ");
			queryStringOficina.append(" and o.id = :pIdOficina  ");
	
			queryStringOficina.append(" order by  h.fechaExcepcion desc ");
	
			Query query3 = em.createQuery(queryStringOficina.toString());
	
			query3.setParameter("pIdOficina", idOficina);
			
	
			excepcionesAplicaOficina = query3.getResultList();
	
			for (HorarioCC h : excepciones) {
				excepcionesFInal.add(h);
			}
	
			for (HorarioCC h : excepcionesAplicaTodo) {
				excepcionesFInal.add(h);
			}
	
			for (HorarioCC h : excepcionesAplicaOficina) {
				excepcionesFInal.add(h);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excepcionesFInal;
	}
    
    

}
