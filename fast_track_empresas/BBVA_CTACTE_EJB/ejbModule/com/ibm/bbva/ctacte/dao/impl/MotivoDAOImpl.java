package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;

/**
 * Session Bean implementation class MotivoBean
 */
@Stateless
@Local(MotivoDAO.class)
public class MotivoDAOImpl extends GenericDAO<Motivo, Integer> implements MotivoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public MotivoDAOImpl() {
        super(Motivo.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Motivo> findByTarea(int idTarea, String strTipoMotivo) {
		Query query = em.createQuery(
 			"select o from Motivo o where o.tarea.id=:idTarea and o.tipoMotivo=:tipoMotivo and o.flagActivo = '1' ");
		query.setParameter("idTarea", idTarea);
		query.setParameter("tipoMotivo", strTipoMotivo);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idTarea-->"+idTarea);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("strTipoMotivo-->"+strTipoMotivo);
		List<Motivo> motivos = query.getResultList();
		return motivos;
	}

	@Override
	public List<Motivo> getMotivos(String tipoMotivo) {
		Query query=em.createQuery(
				"select o from Motivo o where o.tipoMotivo=:tipoMot and o.flagActivo = '1'");
		query.setParameter("tipoMot",tipoMotivo);
		List<Motivo> motivos=query.getResultList();
		return motivos;
	}

}
