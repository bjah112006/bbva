package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.EmpleadoLdap;
import com.ibm.bbva.ctacte.dao.EmpleadoLdapDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

@Stateless
@Local(EmpleadoLdapDAO.class)
public class EmpleadoLdapDAOImpl extends GenericDAO<EmpleadoLdap, Integer> implements EmpleadoLdapDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	public EmpleadoLdapDAOImpl(Class<EmpleadoLdap> entityClass) {
		super(entityClass);
	}
	
	
	public EmpleadoLdapDAOImpl() {
		super(EmpleadoLdap.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	@Override
	public List<EmpleadoLdap> getAllNotInEmpleado() {
		
		Query query = em.createQuery("select u from EmpleadoLdap u where TRIM(u.codigoUsuario) not in (select trim(e.codigo) from Empleado e)");
		
		List<EmpleadoLdap> lus = query.getResultList();
		return lus;
	}
	
	@Override
	public EmpleadoLdap findByCodigoUsuario(String codigoUsuario) throws NoResultException, NonUniqueResultException {

		Query query = em.createQuery("select u from EmpleadoLdap u where u.codigoUsuario=:codigoUsuario");

		query.setParameter("codigoUsuario", codigoUsuario);

		EmpleadoLdap lus = (EmpleadoLdap) query.getSingleResult();

		return lus;
	}


	@Override
	public int deleteAll() {
		
		return   em.createQuery("DELETE FROM EmpleadoLdap").executeUpdate();
		
	}
}
