package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.HorarioOficina;
import com.ibm.bbva.ctacte.bean.LDAPUsuario;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.LDAPUsuarioDAO;

@Stateless
@Local(LDAPUsuarioDAO.class)
public class LDAPUsuarioDAOImpl extends GenericDAO<LDAPUsuario, Integer> implements LDAPUsuarioDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	public LDAPUsuarioDAOImpl(Class<LDAPUsuario> entityClass) {
		super(entityClass);
	}
	
	
	public LDAPUsuarioDAOImpl() {
		super(LDAPUsuario.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Apéndice de método generado automáticamente
		return em;
	}

	@Override
	public LDAPUsuario findByCodigoUsuario(String codigoUsuario) throws NoResultException, NonUniqueResultException {

		Query query = em.createQuery("select u from LDAPUsuario u where u.codusu=:codigoUsuario");

		query.setParameter("codigoUsuario", codigoUsuario);

		LDAPUsuario lus = (LDAPUsuario) query.getSingleResult();

		return lus;
	}

	@Override
	public List<LDAPUsuario> getAllNotInEmpleado() {
		Query query = em.createQuery("select u from LDAPUsuario u where u.codusu not in (select e.codigo from Empleado e)");

		List<LDAPUsuario> lus = query.getResultList();

		return lus;
	}
	
	@Override
	public List<LDAPUsuario> getAllInEmpleado() {
		Query query = em.createQuery("select u from LDAPUsuario u where u.codusu in (select e.codigo from Empleado e)");

		List<LDAPUsuario> lus = query.getResultList();

		return lus;
	}

}
