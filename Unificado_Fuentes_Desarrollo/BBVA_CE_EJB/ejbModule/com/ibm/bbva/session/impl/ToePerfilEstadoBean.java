package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;

/**
 * Session Bean implementation class ToePerfilEstadoBean
 */
@Stateless
public class ToePerfilEstadoBean extends AbstractFacade<ToePerfilEstado> implements ToePerfilEstadoBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(ToePerfilEstadoBean.class);
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ToePerfilEstadoBean() {
    	super(ToePerfilEstado.class);
    }
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<ToePerfilEstado> validaRegistroUnico(long idProducto, long idTarea, long idPerfil, long idEstado, long idOferta, String idFlujo, String nomColumna) {
		
		String query="SELECT t FROM ToePerfilEstado t WHERE t.producto.id = :idProducto "+
                     "AND t.tarea.id = :idTarea AND t.perfil.id = :idPerfil "+
                     "AND t.estado.id = :idEstado AND t.tipoOferta.id = :idOferta "+
                     "AND t.tipoFlujo = :idFlujo "+
                     "AND t.nomColumna = :nomColumna ";
		try{
			List<ToePerfilEstado> resultList = em.createQuery(query)
					.setParameter("idProducto", idProducto)
					.setParameter("idTarea", idTarea)					
					.setParameter("idPerfil", idPerfil)
					.setParameter("idEstado", idEstado)
					.setParameter("idOferta", idOferta)
					.setParameter("idFlujo", idFlujo)
					.setParameter("nomColumna", nomColumna)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public List<ToePerfilEstado> buscarRegistroToePerfilEstado(long idEstadoExp,long idPerfil, 
														 long idTipoOferta,String tipoFlujo,long idProducto,long idTarea){
		
		String query="SELECT tpe FROM ToePerfilEstado tpe " + 
					 "WHERE tpe.producto.id=:idProducto " +
					 "AND tpe.tipoOferta.id=:idTipoOferta "+
					 "AND tpe.perfil.id=:idPerfil "+
					 "AND tpe.tarea.id=:idTarea "+	
		             "AND tpe.tipoFlujo=:tipoFlujo "+
		             "AND tpe.estado.id in ( SELECT et.estadoCE.id FROM EstadoTareaCE et WHERE et.tarea.id=:idTarea AND et.estado.id=:idEstadoExp) ";
		
		LOG.info("query = "+query);
		try{
			List<ToePerfilEstado> resultList=(List<ToePerfilEstado>)em.createQuery(query)
						.setParameter("idProducto", idProducto)
						.setParameter("idEstadoExp",idEstadoExp)
						.setParameter("idPerfil", idPerfil)
						.setParameter("idTipoOferta", idTipoOferta)
						.setParameter("tipoFlujo", tipoFlujo)
						.setParameter("idTarea", idTarea)
						.getResultList();
				return resultList;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public List<ToePerfilEstado> buscarRegistroToePerfilEstadoTarea1(long idEstadoExp,long idPerfil, 
														 long idTipoOferta,String tipoFlujo,long idProducto,long idTarea){
		
		String query="SELECT tpe FROM ToePerfilEstado tpe " + 
					 "WHERE tpe.producto.id=:idProducto " +
					 "AND tpe.tipoOferta.id=:idTipoOferta "+
					 "AND tpe.perfil.id=:idPerfil "+
					 "AND tpe.tarea.id=:idTarea "+	
		             "AND tpe.tipoFlujo=:tipoFlujo ";
		
		LOG.info("query = "+query);
		try{
			List<ToePerfilEstado> resultList=(List<ToePerfilEstado>)em.createQuery(query)
						.setParameter("idProducto", idProducto)
						.setParameter("idPerfil", idPerfil)
						.setParameter("idTipoOferta", idTipoOferta)
						.setParameter("tipoFlujo", tipoFlujo)
						.setParameter("idTarea", idTarea)
						.getResultList();
				return resultList;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	
	
	
}