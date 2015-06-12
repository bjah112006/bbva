package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.VistaBandejaExpediente;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;

/**
 * Session Bean implementation class CarterizacionCEBean
 */
@Stateless
@Local(AnsBeanLocal.class)
public class AnsBean extends AbstractFacade<Ans> implements AnsBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(AnsBean.class);
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
	@EJB
	private EstadoCEBeanLocal estadoCEBean;
	
    /**
     * Default constructor. 
     */
	public AnsBean() {
        super(Ans.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
			 
	@Override
	public Ans obtenerAns(long idProducto, long idTarea, long idTipoOferta, long idGrupoSegmento, long idEstado) {
		LOG.info("Ingresa a metodo obtenerAns");
		EstadoCE estadoCE = estadoCEBean.buscarPorIdTareaIdEstado(idTarea, idEstado);
		Ans ans = null;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t FROM Ans t ");
		sb.append("WHERE t.producto.id = :idProducto and t.tarea.id = :idTarea ");
		sb.append("and t.tipoOferta.id = :idTipoOferta and t.grupoSegmento.id = :idGrupoSegmento ");
		sb.append("and t.estadoCE.id = :idEstadoCE and t.flagActivo = : flagActivo");
		
	
		if (estadoCE != null) {
			try {
				LOG.debug("query 1");
				ans = (Ans) em.createQuery(sb.toString())
						.setParameter("idProducto", idProducto)
						.setParameter("idTarea", idTarea)
		                .setParameter("idTipoOferta", idTipoOferta)
						.setParameter("idGrupoSegmento", idGrupoSegmento)				
						.setParameter("idEstadoCE", estadoCE.getId())
						.setParameter("flagActivo", "1")
						.getSingleResult();
			} catch (NoResultException e) {
				ans = null;
			}
		}
		

		
		if (ans == null) {
			
			sb = new StringBuilder();
			sb.append("SELECT t FROM Ans t ");
			sb.append("WHERE t.producto.id = :idProducto and t.tarea.id = :idTarea ");
			sb.append("and t.tipoOferta.id = :idTipoOferta and t.grupoSegmento.id = :idGrupoSegmento ");
			sb.append("and t.flagActivo = : flagActivo and t.estadoCE is null ");
			
			try {
				LOG.debug("query 2");
				ans = (Ans) em.createQuery(sb.toString())
						.setParameter("idProducto", idProducto)
						.setParameter("idTarea", idTarea)
		                .setParameter("idTipoOferta", idTipoOferta)
						.setParameter("idGrupoSegmento", idGrupoSegmento)
						.setParameter("flagActivo", "1")
						.getSingleResult();
			} catch (NoResultException e) {
				ans = null;
			}
		}
		
		LOG.info("sb:::"+sb);
		
		return ans;
	}
	
	@Override
	public List<Ans> cargarValoresAns(List<Long> listIdsTareas, List<Long> listIdsProd) {
		LOG.info("Ingresa a metodo cargarValoresAns");

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t FROM Ans t ");
		sb.append("WHERE t.tarea.id in (:listIdsTareas) and t.flagActivo like : flagActivo "); 
		
		if(listIdsProd!=null && listIdsProd.size()>0)
			sb.append("and t.producto.id in (:listIdsProd)"); 
		
		try {
			LOG.debug("query:"+sb.toString());
			//List<Ans> resultListAns = (List<Ans>) em.createQuery(sb.toString())		
					
					Query queryExe= em.createQuery(sb.toString());
			
					
					queryExe.setParameter("listIdsTareas", listIdsTareas);
					queryExe.setParameter("flagActivo", "1");
						
					if(listIdsProd!=null && listIdsProd.size()>0)						
						queryExe.setParameter("listIdsProd", listIdsProd);
					
					List<Ans> resultListAns = (List<Ans>)queryExe.getResultList();
								
			return resultListAns;
			
		} catch (NoResultException e) {
			return null;
		}

	}	

	@Override
	public List<Ans> buscarTodos() {
		return copyResultList(em.createNamedQuery("Ans.findAll").getResultList());
	}

	@Override
	public List<Ans> buscarConsultaParametrizable(Ans objConsulta) {
		StringBuilder querySb = new StringBuilder("SELECT t FROM Ans t WHERE 1=1");
		if (objConsulta.getProducto() != null && objConsulta.getProducto().getId() > 0) {
			querySb.append(" AND t.producto.id = :idProducto");
		}
		if (objConsulta.getTarea() != null && objConsulta.getTarea().getId() > 0) {
			querySb.append(" AND t.tarea.id = :idTarea");
		}
		if (objConsulta.getTipoOferta() != null && objConsulta.getTipoOferta().getId() > 0) {
			querySb.append(" AND t.tipoOferta.id = :idTipoOferta");
		}
		if (objConsulta.getGrupoSegmento() != null && objConsulta.getGrupoSegmento().getId() > 0) {
			querySb.append(" AND t.grupoSegmento.id = :idGrupoSegmento");
		}
		if (objConsulta.getEstadoCE() != null && objConsulta.getEstadoCE().getId() > 0) {
			querySb.append(" AND t.estadoCE.id = :idEstadoCE");
		}
		Query query = em.createQuery(querySb.toString());
		if (objConsulta.getProducto() != null && objConsulta.getProducto().getId() > 0) {
			query.setParameter("idProducto", objConsulta.getProducto().getId());
		}
		if (objConsulta.getTarea() != null && objConsulta.getTarea().getId() > 0) {
			query.setParameter("idTarea", objConsulta.getTarea().getId());
		}
		if (objConsulta.getTipoOferta() != null && objConsulta.getTipoOferta().getId() > 0) {
			query.setParameter("idTipoOferta", objConsulta.getTipoOferta().getId());
		}
		if (objConsulta.getGrupoSegmento() != null && objConsulta.getGrupoSegmento().getId() > 0) {
			query.setParameter("idGrupoSegmento", objConsulta.getGrupoSegmento().getId());
		}
		if (objConsulta.getEstadoCE() != null && objConsulta.getEstadoCE().getId() > 0) {
			query.setParameter("idEstadoCE", objConsulta.getEstadoCE().getId());
		}
		
		return copyResultList(query.getResultList());
	}

	@Override
	public Ans create(Ans entity) {
		return super.create(entity);
	}

	@Override
	public void edit(Ans entity) {
		super.edit(entity);
	}

	@Override
	public void remove(Ans entity) {
		super.remove(entity);
	}
	
}