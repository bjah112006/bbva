package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class ExpedienteTareaProcesoBean
 */
@Stateless
@Local(ExpedienteTareaProcesoDAO.class)
public class ExpedienteTareaProcesoDAOImpl extends GenericDAO<ExpedienteTareaProceso, Integer> implements ExpedienteTareaProcesoDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteTareaProcesoDAOImpl.class);

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
	public ExpedienteTareaProceso findExpedienteTareaProceso(
			Integer idExpediente, Integer idEmpleado, Integer idTarea) {
		Query query = em.createQuery(
 			"select o from ExpedienteTareaProceso o where o.idExpediente=:idExpediente and o.idEmpleado=:idEmpleado and o.idTarea=:idTarea");
		query.setParameter("idExpediente", idExpediente);
		query.setParameter("idEmpleado", idEmpleado);
		query.setParameter("idTarea", idTarea);
		List<ExpedienteTareaProceso> listExpedienteTareaProceso =  query.getResultList();
		ExpedienteTareaProceso expedienteTareaProceso = new ExpedienteTareaProceso();
		if (listExpedienteTareaProceso != null && listExpedienteTareaProceso.size() > 0) {
			expedienteTareaProceso = listExpedienteTareaProceso.get(0);
		} else {
			expedienteTareaProceso = null;
		}
		return expedienteTareaProceso;
	}

	@Override
	public List<ExpedienteTareaProceso> findByIdExpIdTarea(
			Integer idExpediente, Integer idTarea) {
		Query query = em
				.createQuery("select o from ExpedienteTareaProceso o where o.idExpediente=:idExpediente and o.idTarea=:idTarea");
		query.setParameter("idExpediente", idExpediente);
		query.setParameter("idTarea", idTarea);
		return query.getResultList();
	}

	@Override
	public void eliminarAnterioresByIdExp(
			Integer idExpediente) {
		Query query = em
				.createQuery("select o from ExpedienteTareaProceso o where o.idExpediente=:idExpediente and o.idTarea <> :idTarea");
		query.setParameter("idExpediente", idExpediente);
		query.setParameter("idTarea", ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE);
		List<ExpedienteTareaProceso> lstExpTarProc = query.getResultList();
		if (lstExpTarProc != null && lstExpTarProc.size() > 0) {
			LOG.info("Se encontraron "+lstExpTarProc.size()+" registros de carga de trabajo huérfanos para el expediente, se eliminarán.");
			for (ExpedienteTareaProceso obj : lstExpTarProc) {
				em.remove(obj);
			}
		} else {
			LOG.info("No se encontraron otros registros de carga de trabajo huérfanos para el expediente.");
		}
	}

}
