package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.CuentaDAO;

/**
 * Session Bean implementation class CuentaBean
 */
@Stateless
@Local(CuentaDAO.class)
public class CuentaDAOImpl extends GenericDAO<Cuenta, Integer> implements
		CuentaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public CuentaDAOImpl() {
		super(Cuenta.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Cuenta> findByCliente(Cliente cliente) {
		Query query = em
				.createQuery("select o from Cuenta o where o.cliente.id=:idCliente");
		query.setParameter("idCliente", cliente.getId());
		List<Cuenta> cuentas = query.getResultList();
		return cuentas;
	}

	@Override
	public List<Cuenta> findByClienteActivos(Cliente cliente) {
		Query query = em
				.createQuery("select o from Cuenta o where o.cliente.id=:idCliente and o.situacionCuenta='ACT'");
		query.setParameter("idCliente", cliente.getId());
		List<Cuenta> cuentas = query.getResultList();
		return cuentas;
	}

	@Override
	public Cuenta findByNumeroContrato(Cliente cliente, String numeroContrato) {
		try {
			Query query = em
					.createQuery("select o from Cuenta o where o.cliente.id=:idCliente and o.numeroContrato=:numCont");
			query.setParameter("idCliente", cliente.getId());
			query.setParameter("numCont", numeroContrato);
			Cuenta cuenta = (Cuenta) query.getSingleResult();
			return cuenta;
		} catch (NoResultException e) {
			return null;
		}
	}

}
