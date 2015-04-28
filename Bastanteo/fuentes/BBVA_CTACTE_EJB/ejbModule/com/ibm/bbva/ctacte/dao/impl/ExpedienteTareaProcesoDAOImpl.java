package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;

/**
 * Session Bean implementation class ExpedienteTareaProcesoBean
 */
@Stateless
@Local(ExpedienteTareaProcesoDAO.class)
public class ExpedienteTareaProcesoDAOImpl extends GenericDAO<ExpedienteTareaProceso, Integer> implements ExpedienteTareaProcesoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ExpedienteTareaProcesoDAOImpl() {
        super(ExpedienteTareaProceso.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public ExpedienteTareaProceso finExpedienteTareaProceso(
			Integer idExpediente, Integer idEmpleado, Integer idTarea) {
		Query query = em.createQuery(
 			"select o from ExpedienteTareaProceso o where o.idExpediente=:idExpediente and o.idEmpleado=:idEmpleado and o.idTarea=:idTarea");
		query.setParameter("idExpediente", idExpediente);
		query.setParameter("idEmpleado", idEmpleado);
		query.setParameter("idTarea", idTarea);
		List<ExpedienteTareaProceso> listExpedienteTareaProceso =  query.getResultList();
		ExpedienteTareaProceso expedienteTareaProceso = new ExpedienteTareaProceso();
		expedienteTareaProceso = listExpedienteTareaProceso.get(0);
		return expedienteTareaProceso;
	}

}
