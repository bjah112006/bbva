package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.bbva.ctacte.bean.RangoCC;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.RangoCCDAO;
import com.ibm.bbva.ctacte.vo.HorarioVO;

@Stateless
@Local(RangoCCDAO.class)
public class RangoCCDAOImpl extends GenericDAO<RangoCC, Integer> implements
		RangoCCDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public RangoCCDAOImpl() {
		super(RangoCC.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<RangoCC> obtenerRangos(HorarioVO horarioVO)
			throws DataAccessException {
		List<RangoCC> rangos = new ArrayList<RangoCC>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select r from RangoCC r ");
		queryString.append(" join r.horario h  ");
		queryString.append(" join h.perfil p  ");
		queryString.append(" join h.oficina o  ");
		queryString.append(" where 1 = 1 ");

//		if (horarioVO.getIdOficina() = 0) {
			queryString.append(" and o.id = :pIdOficina  ");
//		}

//		if (horarioVO.getIdPerfil() = 0) {
			queryString.append(" and p.id = :pIdPerfil  ");
//		}

		queryString.append("order by  h.dia asc ");
		
		Query query = em.createQuery(queryString.toString());
		
//		if (horarioVO.getIdOficina() != 0) {
			query.setParameter("pIdOficina", horarioVO.getIdOficina());
//		}

//		if (horarioVO.getIdPerfil() != 0) {
			query.setParameter("pIdPerfil", horarioVO.getIdPerfil());
//		}
		rangos = query.getResultList();

		return rangos;
	}

	@Override
	public List<RangoCC> findByHorarioCC(Integer id) throws DataAccessException {
		List<RangoCC> rangos = new ArrayList<RangoCC>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select r from RangoCC r where r.horario.id = " + id);
		Query query = em.createQuery(queryString.toString());
		rangos = query.getResultList();
		return rangos;
	}

	@Override
	public List<RangoCC> obtenerRangosPorPeriodo(Integer idOficina, Integer idPerfil,
			int dayFrom, int dayTo) {
		List<RangoCC> rangos = new ArrayList<RangoCC>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select r from RangoCC r where r.horario.dia >= " + dayFrom + " and " +
				" r.horario.dia <= " + dayTo + " and  r.horario.perfil.id = " + idOficina + " and r.horario.oficina.id = " + idPerfil +
				" order by r.horario.id, r.id asc ");
		Query query = em.createQuery(queryString.toString()); 
		rangos = query.getResultList();
		return rangos;
	}
}
