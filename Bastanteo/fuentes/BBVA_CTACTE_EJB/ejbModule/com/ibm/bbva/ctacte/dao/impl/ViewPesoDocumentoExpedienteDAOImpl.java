package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ViewPesoDocumentoExpediente;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoDocumentoExpedienteDAO;

/**
 * Session Bean implementation class ViewPesoDocumentoExpedienteBean
 */
@Stateless
@Local(ViewPesoDocumentoExpedienteDAO.class)
public class ViewPesoDocumentoExpedienteDAOImpl extends GenericDAO<ViewPesoDocumentoExpediente, Integer> implements ViewPesoDocumentoExpedienteDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ViewPesoDocumentoExpedienteDAOImpl() {
        super(ViewPesoDocumentoExpediente.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<ViewPesoDocumentoExpediente> findListaPesoDocumentoxExpediente(
			Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea) {
		Query query = em.createQuery(
 			"select o from ViewPesoDocumentoExpediente o where o.idProducto=:idProd and o.idTerritorio=:idTerr and o.idTarea=:idTar");
		query.setParameter("idProd", intIdProducto);
		query.setParameter("idTerr", intIdTerritorio);
		query.setParameter("idTar", intIdTarea);
		List<ViewPesoDocumentoExpediente> viewPesoDocumentoExpediente = query.getResultList();
		return viewPesoDocumentoExpediente;
	}

}
