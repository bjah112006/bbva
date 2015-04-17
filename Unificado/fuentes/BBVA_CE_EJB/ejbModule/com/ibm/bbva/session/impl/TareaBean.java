package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.TareaBeanLocal;

/**
 * Session Bean implementation class TareaBean
 */
@Stateless
public class TareaBean extends AbstractFacade<Tarea> implements TareaBeanLocal {
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public TareaBean() {
        super(Tarea.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Tarea buscarPorId(long id) {
		
		Tarea tarea = null;
		
		try{
			tarea = (Tarea) em.createNamedQuery("Tarea.findById").setParameter("id", id).getSingleResult();
		}catch(javax.persistence.NoResultException ex){
		
		}catch(javax.persistence.NonUniqueResultException ex){
			
		}
		
		return tarea;
	}
	
    @Override
	public List<Tarea> buscarTodos() {
		List<Tarea> resultList = em.createNamedQuery("Tarea.findAll").getResultList();
		return resultList;
	}  
    
	@Override
	public List<Tarea> buscarPorPerfil(long idPerfil) {
		String query= " SELECT ta FROM TareaPerfil tape INNER JOIN tape.tarea ta  " + 
				" WHERE tape.tarea.id=ta.id AND tape.perfil.id="+idPerfil+" ORDER BY ta.codigo";
		
		try{
			List<Tarea> resultList = (List<Tarea>)em.createQuery(query)
							.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<Tarea> buscarPorProducto(long idProducto){
		String query=" select ta from ProductoTarea tape inner join tape.tarea ta "+
						"where tape.tarea.id=ta.id and tape.producto.id="+idProducto+"";
		
		try{
			List<Tarea> resultList = (List<Tarea>)em.createQuery(query)
							.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}
    

}
