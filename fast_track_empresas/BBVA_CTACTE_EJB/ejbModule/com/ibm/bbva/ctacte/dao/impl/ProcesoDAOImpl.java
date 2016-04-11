package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Proceso;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ProcesoDAO;

@Stateless
@Local(ProcesoDAO.class)
public class ProcesoDAOImpl extends GenericDAO<Proceso, Integer> implements ProcesoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	public ProcesoDAOImpl(Class<Proceso> entityClass) {
		super(entityClass);
	}
	
	
	public ProcesoDAOImpl() {
		super(Proceso.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	@Override
	public List<Proceso> listarUltimosProcesos(int cantidad) {
		List<Proceso> procesos = new ArrayList<Proceso>();
		Query query = em.createQuery("select o from Proceso o order by o.id desc ");
		query.setMaxResults(cantidad);
		procesos = query.getResultList();
		return procesos;
	}
	
	@Override
	public List<Proceso> getProcesosSinTerminar() {
		List<Proceso> procesos = new ArrayList<Proceso>();
		Query query = em.createQuery("select o from Proceso o where o.fechaFin is null ");
		procesos = query.getResultList();
		return procesos;
	}


}
