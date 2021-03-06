package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;

/**
 * Session Bean implementation class OficinaBean
 */
@Stateless
@Local(OficinaDAO.class)
public class OficinaDAOImpl extends GenericDAO<Oficina, Integer> implements OficinaDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	private static final Logger LOG = LoggerFactory.getLogger(OficinaDAOImpl.class);
	/**
	 * Default constructor. 
	 */
	public OficinaDAOImpl() {
		super(Oficina.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Oficina> findByIdTerritorio(Integer codigoTerritorio) {
		String q="select o from Oficina o where 1=1 ";
		if(codigoTerritorio!=0){
			q=q+"and o.territorio.id=:codigoTerritorio ";
		}
		q=q+" order by o.codigo ASC";
		
		LOG.info("Query :"+q);
		Query query = em.createQuery(q);

		if(codigoTerritorio!=0){
			query.setParameter("codigoTerritorio", codigoTerritorio);	
		}
		List<Oficina> oficina = query.getResultList();
		return oficina;
	}

	@Override
	public Oficina findByCodigo(String codofi) throws NoResultException, NonUniqueResultException {
		Query query = em.createQuery("select o from Oficina o where o.codigo =:codofi");
		query.setParameter("codofi", codofi);
		Oficina oficina = (Oficina) query.getSingleResult();
		return oficina;
	}

	@Override
	public List<Oficina> findAllOrderedByCodigo(boolean tipo) {
		Query query = em.createNativeQuery("SELECT * FROM CONELE.TBL_CE_IBM_OFICINA order by TO_NUMBER(codigo) asc", Oficina.class);
		List<Oficina> oficina = query.getResultList();
		return oficina;
	}

}
