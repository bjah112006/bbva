package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.session.AbstractFacade;

import com.ibm.bbva.session.MensajesBeanLocal;

/**
 * Session Bean implementation class DatosCorreoBean
 */
@Stateless
public class MensajesBean  extends AbstractFacade<Mensajes> implements MensajesBeanLocal{
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public MensajesBean() {
    	super(Mensajes.class);
    }

	@Override                                             
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<Mensajes> buscarTodos() {
		List<Mensajes> resultList = em.createNamedQuery("Mensajes.findAll").getResultList();
		return resultList;
		
	}
	
	
	@Override
	public Mensajes create(Mensajes entity) {
		try{
			Mensajes dc = super.create(entity);
			return dc;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public Mensajes buscarPorId(long id) {
		String query="SELECT m FROM Mensajes m WHERE m.id = :id";
		System.out.println("query = "+query);
		try{
			Mensajes resultList = (Mensajes) em.createQuery(query)
					.setParameter("id", id)
					/*.setParameter("idActivo", idActivo)*/
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			System.out.println("NoResultException::::");
			e.printStackTrace();
			return null;
		} catch(Exception ex){
			System.out.println("Exception::::");
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void delete(Mensajes entity) {
		super.remove(entity);
		
	}
	@Override
	public void update(Mensajes entity){
		super.edit(entity);
	}
	
	

}
