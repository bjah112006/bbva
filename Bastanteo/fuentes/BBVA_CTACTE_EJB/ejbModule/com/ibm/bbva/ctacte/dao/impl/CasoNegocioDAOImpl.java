package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.CasoNegocio;
import com.ibm.bbva.ctacte.dao.CasoNegocioDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class CasoNegocioDAOImpl
 */
@Stateless
@Local(CasoNegocioDAO.class)
public class CasoNegocioDAOImpl extends GenericDAO<CasoNegocio, Integer>
		implements CasoNegocioDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public CasoNegocioDAOImpl() {
		super(CasoNegocio.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<CasoNegocio> listaCasosNegocioGuia(String codTipoPj,
			Integer operacion) {
		Query query = em.createQuery(
			"select distinct o.casoNegocio from GuiaDocument o where o.id.codTipoPj=:codTipoPj and o.operacion.id=:operacion and o.casoNegocio.id <> 1 order by o.casoNegocio.descripcion ASC");
		query.setParameter("codTipoPj", codTipoPj);
		query.setParameter("operacion", operacion);
		List<CasoNegocio> casosNegocio = query.getResultList();
		return casosNegocio;
	}

}
