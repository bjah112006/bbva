package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.PerfilBalanceo;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.PerfilBalanceoDAO;

/**
 * Session Bean implementation class PerfilBalanceoBean
 */
@Stateless
@Local(PerfilBalanceoDAO.class)
public class PerfilBalanceoDAOImpl extends GenericDAO<PerfilBalanceo, Integer> implements PerfilBalanceoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public PerfilBalanceoDAOImpl() {
        super(PerfilBalanceo.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public PerfilBalanceo findObtenerTipoBalanceo(Integer idPerfil) {
		PerfilBalanceo perfilBalanceo = new PerfilBalanceo();
		Query query = em.createQuery(
 			"select o from PerfilBalanceo o where o.idPerfil=:idPerfil");
		query.setParameter("idPerfil", idPerfil);
		List<PerfilBalanceo> listPerfilBalanceo =  query.getResultList();
		perfilBalanceo = listPerfilBalanceo.get(0);
		return perfilBalanceo;
	}

}
