package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DescargaLDAPBeanLocal;

@Stateless
public class DescargaLDAPBean extends AbstractFacade<DescargaLDAP> implements DescargaLDAPBeanLocal
{

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public DescargaLDAPBean() {
    	super(DescargaLDAP.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DescargaLDAP create(DescargaLDAP entity){
		return super.create(entity);
	}

	@Override
	public void edit(DescargaLDAP entity){
		super.edit(entity);
	}

	@Override
	public List<DescargaLDAP> buscar(String tipo, String codigo,
			String carterizacion, String estado, String perfil) {
		
		List<DescargaLDAP> resultList = null;
		StringBuilder sbQuery = new StringBuilder(" SELECT DISTINCT d FROM DescargaLDAP d ");
		sbQuery.append(" INNER JOIN d.perfil p ");
		sbQuery.append(" INNER JOIN d.descargaLDAPCarterizaciones dc ");
		sbQuery.append(" LEFT JOIN FETCH d.descargaLDAPCarterizaciones ");			
		
		boolean isAnd = false;
		if(!tipo.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }		
			sbQuery.append(" d.tipo = :tipo ");
			isAnd = true;
		}		
		if(codigo != null && codigo.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" d.codigo LIKE :codigo ");
			isAnd = true;
		}
		if(!carterizacion.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" dc.carterizacion.id  = :carterizacion ");
			isAnd = true;
		}
		if(!estado.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" d.estado = :estado ");
			isAnd = true;
		}
		if(!perfil.equals("-1"))
		{
			if(sbQuery.indexOf("WHERE") == -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" p.id = :perfil ");
			isAnd = true;
		}
		
		sbQuery.append(" ORDER BY d.id ");
		
		try{
			
			Query jpql = em.createQuery(sbQuery.toString());
			if(!tipo.equals("-1"))
			{
				jpql.setParameter("tipo", tipo);
			}
			if(codigo != null && codigo.length() > 0)
			{
				jpql.setParameter("codigo", "%" + codigo + "%");
			}
			if(!carterizacion.equals("-1"))
			{
				jpql.setParameter("carterizacion", Long.valueOf(carterizacion));
			}
			if(!estado.equals("-1"))
			{
				jpql.setParameter("estado", estado);
			}
			if(!perfil.equals("-1"))
			{
				jpql.setParameter("perfil", Long.parseLong(perfil));
			}
			
			resultList = jpql.getResultList();	
						
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}	
	}

	@Override
	public DescargaLDAP buscarPorId(long id) 
	{
		try{			
			return (DescargaLDAP) em.createNamedQuery("DescargaLDAP.findById").setParameter("id", id).getSingleResult();			
		}catch(NoResultException e){
			return null;
		}
	}
	
}
