package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ViewPesoParticipeExpediente;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoParticipeExpedienteDAO;

/**
 * Session Bean implementation class ViewPesoParticipeExpedienteBean
 */
@Stateless
@Local(ViewPesoParticipeExpedienteDAO.class)
public class ViewPesoParticipeExpedienteDAOImpl extends GenericDAO<ViewPesoParticipeExpediente, Integer> implements ViewPesoParticipeExpedienteDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ViewPesoParticipeExpedienteDAOImpl() {
        super(ViewPesoParticipeExpediente.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<ViewPesoParticipeExpediente> findListaPesoParticipesxExpediente(
			Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea) {
		Query query = em.createQuery(
 			"select o from ViewPesoParticipeExpediente o where o.idProducto=:idProd and o.idTerritorio=:idTerr and o.idTarea=:idTar");
		query.setParameter("idProd", intIdProducto);
		query.setParameter("idTerr", intIdTerritorio);
		query.setParameter("idTar", intIdTarea);
		List<ViewPesoParticipeExpediente> viewPesoParticipeExpediente = query.getResultList();
		return viewPesoParticipeExpediente;
	}

}
