package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.ParametrosVistaUnica;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.ParametrosVistaUnicaDAO;

/**
 * Session Bean implementation class ParametrosVistaUnicaBean
 */
@Stateless
@Local(ParametrosVistaUnicaDAO.class)
public class ParametrosVistaUnicaDAOImpl extends GenericDAO<ParametrosVistaUnica, Integer> implements ParametrosVistaUnicaDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ParametrosVistaUnicaDAOImpl() {
        super(ParametrosVistaUnica.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public boolean findParametroVistaUnica(int id) {
		boolean blnSoloUltimaVersion = false;
		Query query = em.createQuery(
 			"select o from ParametrosVistaUnica o where o.id=:id");
		query.setParameter("id", id);
		ParametrosVistaUnica parametrosVistaUnica = (ParametrosVistaUnica) query.getSingleResult();
		if(parametrosVistaUnica.getFlag_solo_ultima_version().equals("1")){
			blnSoloUltimaVersion = true;
		}else{
			blnSoloUltimaVersion = false;
		}
		return blnSoloUltimaVersion;
	}

}
