package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class ClienteBean
 */
@Stateless
@Local(ClienteDAO.class)
public class ClienteDAOImpl extends GenericDAO<Cliente, Integer> implements
		ClienteDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public ClienteDAOImpl() {
		super(Cliente.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Cliente> findByExpediente(Expediente expediente) {
		Query query = em
				.createQuery("select o from Cliente o where o.id=:idCliente");
		query.setParameter("idCliente", expediente.getCliente().getId());
		List<Cliente> clientes = query.getResultList();
		return clientes;
	}

	@Override
	public Cliente findByCodigCentral(String codigoCentral) {
		try {
			Query query = em
					.createQuery("select o from Cliente o where o.codigoCentral=:codCent order by o.id desc");
			query.setParameter("codCent", codigoCentral);
			Cliente cliente = (Cliente) query.getResultList().get(0);
			return cliente;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Cliente findByNumeroDoi(String numeroDoi) {
		try {
			Query query = em
					.createQuery("select o from Cliente o where o.numeroDoi=:numeroDoi order by o.id desc");
			query.setParameter("numeroDoi", numeroDoi);
			Cliente cliente = (Cliente) query.getResultList().get(0);
			return cliente;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

}
