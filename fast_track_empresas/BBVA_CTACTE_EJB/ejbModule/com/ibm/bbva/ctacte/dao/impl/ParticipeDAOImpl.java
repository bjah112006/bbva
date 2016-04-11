package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;

/**
 * Session Bean implementation class ParticipeBean
 */
@Stateless
@Local(ParticipeDAO.class)
public class ParticipeDAOImpl extends GenericDAO<Participe, Integer> implements ParticipeDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ParticipeDAOImpl() {
        super(Participe.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Participe> findByExpediente(Expediente expediente) {
		Query query = em.createQuery(
 			"select o from Participe o where o.cliente.id=:idClien");
		query.setParameter("idClien", expediente.getCliente().getId());
		List<Participe> participes = query.getResultList();
		return participes;
	}

	@Override
	public List<Participe> findByExpedienteParticipes(Integer intIdExpediente) {
		Query query = em.createQuery(
 			"select o from Participe o, ExpedienteCuenta ec where o.cuenta.id= ec.idCuenta and ec.idExpediente=:idExpediente");
		query.setParameter("idExpediente", intIdExpediente);
		List<Participe> participes = query.getResultList();
		return participes;
	}

	@Override
	public List<Participe> findByCuentaParticipes(Integer intIdCuenta) {
		Query query = em.createQuery(
 			"select o from Participe o where o.cuenta.id=:idCuenta");
		query.setParameter("idCuenta", intIdCuenta);
		List<Participe> participes = query.getResultList();
		return participes;
	}

	@Override
	public List<Participe> findByCuenta(Cuenta cuenta) {
		Query query = em.createQuery("select o from Participe o where o.numeroCuenta=:numCuenta");
		query.setParameter("numCuenta", cuenta.getNumeroContrato());
		List<Participe> participes = query.getResultList();
		return participes;
	}

	@Override
	public List<Participe> findByExpedienteParticipesUnicos(
			Integer intIdExpediente) {
		Query query = em.createQuery(
 			"select o from Participe o, ExpedienteCuenta ec where o.cuenta.id= ec.idCuenta and ec.idExpediente=:idExpediente");
		query.setParameter("idExpediente", intIdExpediente);
		List<Participe> auxParticipes = query.getResultList();

		List<Participe> participes = new ArrayList();
		ComparatorParticipe comparator = new ComparatorParticipe ();
		
		for (Participe participe : auxParticipes) {
			Collections.sort(participes, comparator);
			if (Collections.binarySearch(participes, participe, comparator)<0) {
				participes.add(participe);
			}
		}
		return participes;
	}

	@Override
	public List<Participe> findByFirmaObservadas(Expediente expediente) {
		Query query = em.createQuery(
 			"select o from Participe o where o.cliente.id=:idClien and o.flagFirmaAsociada = '0'");
		query.setParameter("idClien", expediente.getCliente().getId());
		List<Participe> participes = query.getResultList();		
		return participes;
	}
	
	private class ComparatorParticipe implements Comparator<Participe> {

		public int compare(Participe o1, Participe o2) {
			return o1.getCodigoCentral().compareTo(o2.getCodigoCentral());
		}
		
	}

}
