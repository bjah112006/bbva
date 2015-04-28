package com.ibm.bbva.ctacte.dao.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.MultiTabla;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.MultiTablaDAO;

@Stateless
@Local(MultiTablaDAO.class)
public class MultiTablaDAOImpl extends GenericDAO<MultiTabla, Long> implements MultiTablaDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public MultiTablaDAOImpl() {
    	super(MultiTabla.class);
    }
    
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public MultiTabla obtener(Long idPadre, String codigo, String texto) {
		Query query = em.createQuery("select o from MultiTabla o where o.parametro=:idPadre and o.codigo=:codigo and o.texto=:texto");
		query.setParameter("idPadre", idPadre);
		query.setParameter("codigo", codigo);
		query.setParameter("texto", texto);
		return (MultiTabla) query.getSingleResult();
	}

	@Override
	public MultiTabla obtener(Long id) {
		Query query = em.createQuery("select o from MultiTabla o where o.id=:id");
		query.setParameter("id", id);
		return (MultiTabla) query.getSingleResult();
	}

}
