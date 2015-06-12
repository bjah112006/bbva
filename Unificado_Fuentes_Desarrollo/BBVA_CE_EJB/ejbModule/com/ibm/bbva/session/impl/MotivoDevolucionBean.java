package com.ibm.bbva.session.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.MotivoDevolucion;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.MotivoDevolucionBeanLocal;

/**
 * Session Bean implementation class MotivoDevolucionBean
 */
@Stateless
@Local(MotivoDevolucionBeanLocal.class)
public class MotivoDevolucionBean extends AbstractFacade<MotivoDevolucion> implements MotivoDevolucionBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public MotivoDevolucionBean() {
        super(MotivoDevolucion.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public MotivoDevolucion buscarPorId(long id) {
		try{
			return (MotivoDevolucion) em.createNamedQuery("MotivoDevolucion.findbyId")
				.setParameter("id", id)
				.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<MotivoDevolucion> buscarPorIdTarea(long idTarea) {
		//Rechazo
		String idActivo="1";
		List<MotivoDevolucion> resultList = em
				.createNamedQuery("MotivoDevolucion.findbyIdTarea")
				.setParameter("idTarea", idTarea)
				.setParameter("idActivo", idActivo)
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<MotivoDevolucion> buscarPorIdTareaDevolver(long idTarea) {
		//Devolver
		String idActivo="1";
		List<MotivoDevolucion> resultList = em
				.createNamedQuery("MotivoDevolucion.findbyIdTareaDevolver")
				.setParameter("idTarea", idTarea)
				.setParameter("idActivo", idActivo)
				.getResultList();
		return resultList;
	}	

	@Override
	public List<MotivoDevolucion> buscarPorIdTareaYAccion(long idTarea,
			BigDecimal accion) {
		List<MotivoDevolucion> resultList = em
				.createNamedQuery("MotivoDevolucion.findbyIdTareaYAccion")
				.setParameter("idTarea", idTarea)
				.setParameter("accion", accion).getResultList();
		return resultList;
	}

	@Override
	public List<MotivoDevolucion> buscarPorIdMotivoIdTareaFlagOtros(long idMotivo,
			long idTarea, String flagOtros) {
		String idActivo="1";
		List<MotivoDevolucion> resultList = em
				.createNamedQuery("MotivoDevolucion.findbyIdMotivoIdTareaFlagOtros")
				.setParameter("idMotivo", idMotivo)
				.setParameter("idTarea", idTarea)
				.setParameter("flagOtros", flagOtros)
				.setParameter("idActivo", idActivo)
				.getResultList();
		return resultList;
	}	
	


	
	
}
