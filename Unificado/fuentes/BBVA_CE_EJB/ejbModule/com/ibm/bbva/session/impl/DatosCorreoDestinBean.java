package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DatosCorreoDestinBeanLocal;

/**
 * Session Bean implementation class DatosCorreoBean
 */
@Stateless
public class DatosCorreoDestinBean extends AbstractFacade<DatosCorreoDestin> implements DatosCorreoDestinBeanLocal{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public DatosCorreoDestinBean() {
    	super(DatosCorreoDestin.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DatosCorreoDestin buscarPorId(long id) {
		try{
			return (DatosCorreoDestin) em.createNamedQuery("DatosCorreoDestin.findById")
					.setParameter("id", id)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public DatosCorreoDestin buscarPorIdPerfilAndCabecera(String codPerfil,long idCabecera) {
		long idPerfil=Long.valueOf(codPerfil);
		try{
			return (DatosCorreoDestin) em.createNamedQuery("DatosCorreoDestin.findByPerfilAndCabecera")
					.setParameter("idPerfil", idPerfil)
					.setParameter("idCabecera", idCabecera)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<DatosCorreoDestin> buscarTodos() {
		try{
			List<DatosCorreoDestin> resultList = em.createNamedQuery("DatosCorreoDestin.findAll").getResultList();
			return resultList;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public List<DatosCorreoDestin> buscarPorIdDatosCorreo(long idCabecera){
		String query="SELECT d FROM DatosCorreoDestin d WHERE d.datosCorreo.id=:idCabecera ";
		System.out.println("query = "+query);
		try{
			List<DatosCorreoDestin> resultList = em.createQuery(query)
					.setParameter("idCabecera", idCabecera)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	
	
	
	@Override
	public List<DatosCorreoDestin> resultadoBusqueda(long idTarea, long idProducto){
		String query="SELECT e"+
					 " FROM DatosCorreoDestin e INNER JOIN e.datosCorreo dat "+
				     " WHERE dat.id=e.datosCorreo.id " +
				     " AND dat.producto.id = :idProducto "+
				     " AND dat.tarea = :idTarea";
		
		System.out.println("query = "+query);
			try{
				List<DatosCorreoDestin> resultList = em.createQuery(query)
						.setParameter("idTarea", idTarea)
						.setParameter("idProducto", idProducto)
						.getResultList();
				return resultList;			
			}catch (NoResultException e) {
				System.out.println("error = "+e);
				return null;
			}	

		
	}
	
	
	
	@Override
	public DatosCorreoDestin create(DatosCorreoDestin entity) {
		try{
			DatosCorreoDestin dc = super.create(entity);
			return dc;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
	
	@Override
	public void delete(DatosCorreoDestin datosCorreoDestin) {
		super.remove(datosCorreoDestin);
	}
	
	@Override
	public void update(DatosCorreoDestin datosCorreoDestin){
		super.edit(datosCorreoDestin);
	}
	
	
}
