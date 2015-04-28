package com.ibm.bbva.ctacte.dao.impl;

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

}
