package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.entities.AyudaMemoria;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DatosCorreoBeanLocal;

/**
 * Session Bean implementation class DatosCorreoBean
 */
@Stateless
public class DatosCorreoBean extends AbstractFacade<DatosCorreo> implements DatosCorreoBeanLocal{
	 
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public DatosCorreoBean() {
    	super(DatosCorreo.class);
    }

	@Override                                             
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DatosCorreo buscarPorId(long id) {
		try{
			return (DatosCorreo) em.createNamedQuery("DatosCorreo.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public List<DatosCorreo> buscarTodos() {
		List<DatosCorreo> resultList = em.createNamedQuery("DatosCorreo.findAll").getResultList();
		return resultList;
		
	}
	
	
	@Override
	public Long buscarExistentes(long idTarea, long idAccion, long idProducto){
		Long result;
		String query="SELECT count(d) FROM DatosCorreo d WHERE d.tarea.id = :idTarea AND d.accion.id=:idAccion AND d.producto.id=:idProducto AND d.flagActivo=:flagActivo";
		System.out.println("query = "+query);
		try{
			result = (Long) em.createQuery(query)
					.setParameter("idTarea", idTarea)
					.setParameter("idAccion", idAccion)
					.setParameter("idProducto", idProducto)
					.setParameter("flagActivo", "1")
					
					.getSingleResult();		
			return result;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	
	@Override
	public DatosCorreo buscarPorTareaAccion(long idTarea,long idAccion, long idProducto) {
		String query="SELECT d FROM DatosCorreo d WHERE d.tarea.id =:idTarea AND d.accion.id=:idAccion AND d.producto.id=:idProducto";
		System.out.println("query = "+query);
		try{
			DatosCorreo resultList = (DatosCorreo) em.createQuery(query)
					.setParameter("idTarea", idTarea)
					.setParameter("idAccion", idAccion)
					.setParameter("idProducto",idProducto)
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override 
	public List<DatosCorreo> buscarPorProducto(long idProducto){
		String query="SELECT e"+
					 " FROM DatosCorreo e"+
					 " WHERE e.producto.id = :idProducto ";
	
		System.out.println("query = "+query);
		try{
			List<DatosCorreo> resultList = em.createQuery(query)
					.setParameter("idProducto", idProducto)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			System.out.println("error = "+e);
			return null;
		}	
		
	}
	
	@Override
	public List<DatosCorreo> resultadoBusqueda(DatosCorreo d, TareaPerfil tp){
		String where = "";
		if (tp.getPerfil()!=null){
			 where = "d.tarea.id=tp.tarea.id";
		}
		
		if ( d.getProducto()!=null && d.getProducto().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"d.producto.id =:idProducto ";
		}
		
		if ( d.getTarea()!=null && d.getTarea().getId()>0 ) {
			where += (where.length()==0? "" : " AND ")+"d.tarea.id =:idTarea ";
		}
		
//		if (tp.getTarea()!=null && tp.getTarea().getId()>0){
//			where += (where.length()==0? "" : " AND ")+"tp.perfil.id =:idPerfil ";
//		}
		
		if (tp!=null && tp.getPerfil()!=null){
			where += (where.length()==0? "" : " AND ")+"tp.perfil.id =:idPerfil ";
		}
		
		String query1 = "SELECT d FROM DatosCorreo d, TareaPerfil tp "+(where.length()==0? "" : (" WHERE "+where));
		String query2= " ORDER BY d.tarea.id ASC";
		
		String query=query1+query2;
		
		System.out.println("query : "+query);
				
		Query sql = (Query) em.createQuery(query);
		
		if ( d.getProducto()!=null && d.getProducto().getId() > 0 ) {
			sql.setParameter("idProducto",d.getProducto().getId());
			System.out.println("idProducto "+d.getProducto().getId());
		}
		
		if ( d.getTarea()!=null && d.getTarea().getId()>0  ) {
			sql.setParameter("idTarea",d.getTarea().getId());
			System.out.println("idTarea "+d.getTarea().getId());
		}
		
//		if ( tp.getTarea()!=null && tp.getTarea().getId()>0  ) {
		if (tp!=null && tp.getPerfil()!=null){
			sql.setParameter("idPerfil",tp.getPerfil().getId());
			System.out.println("idPerfil "+tp.getPerfil().getId());
		}
		System.out.println("sql : "+sql);
		
		List<DatosCorreo> resultList = sql.getResultList();
		
		return resultList; 
	}
	
	@Override
	public DatosCorreo create(DatosCorreo entity) {
		try{
			DatosCorreo dc = super.create(entity);
			return dc;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	@Override
	public void delete(DatosCorreo entity) {
		super.remove(entity);
		
	}
	
	public void update(DatosCorreo entity){
		super.edit(entity);
	}
	
	
}
