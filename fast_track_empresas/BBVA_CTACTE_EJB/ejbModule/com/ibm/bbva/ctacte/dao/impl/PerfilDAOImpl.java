package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;

/**
 * Session Bean implementation class PerfilBean
 */
@Stateless
@Local(PerfilDAO.class)
public class PerfilDAOImpl extends GenericDAO<Perfil, Integer> implements PerfilDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public PerfilDAOImpl() {
        super(Perfil.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Perfil findByCodigo(String codigo) {
		try {
			Query query = em.createQuery("select o from Perfil o where o.codigo = :cod");
			query.setParameter("cod", codigo);
			Perfil perfil = (Perfil) query.getSingleResult();
			return perfil;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Perfil> buscarSupervizados(Integer idPerfil) {
		Query query = em.createQuery("select o from Perfil o where o.perfil.id = :p_idPerfil");
		query.setParameter("p_idPerfil", idPerfil);
		List<Perfil> list = query.getResultList();
		return list;
	}

	@Override
	public List<Perfil> buscarTodos() {
		Query query = em.createQuery("select o from Perfil o order by o.descripcion ASC");
		List<Perfil> list = new ArrayList<Perfil>(query.getResultList());
		return list;
	}

	@Override
	public List<Perfil> buscarPerfilesConTareaAsignada() {
		// La tarea 30 es una tarea ficticia duplicada de la 12
		Query query = em.createQuery("select distinct o.perfil from TareaPerfil o where o.tarea.id <> 30 order by o.perfil.descripcion ASC");
		List<Perfil> list = new ArrayList<Perfil>(query.getResultList());
		return list;
	}

	@Override
	public List<Perfil> buscarPorListaIDs(List<Integer> idPerfiles) {
		Query query = em.createQuery("select o from Perfil o where o.id in (:idPerfiles) order by o.descripcion ASC");
		query.setParameter("idPerfiles", idPerfiles);
		List<Perfil> list = new ArrayList<Perfil>(query.getResultList());
		return list;
	}

}
