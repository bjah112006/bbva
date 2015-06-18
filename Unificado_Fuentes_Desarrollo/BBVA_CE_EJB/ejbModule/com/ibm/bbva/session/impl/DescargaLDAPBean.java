package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.ibm.bbva.entities.DescargaLDAP;
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
			int caracterizacion, String estado) {
		
		List<DescargaLDAP> resultList = null;
		StringBuilder sbQuery = new StringBuilder(" SELECT DISTINCT d FROM DescargaLDAP d ");
		sbQuery.append(" INNER JOIN d.descargaLDAPCarterizaciones dc WHERE dc.carterizacion.id = 1 ");
		sbQuery.append(" ORDER BY d.id ");
		
		/*boolean isAnd = false;
		if(tipo != null && tipo.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") != -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }		
			sbQuery.append(" d.tipo = :tipo ");
			isAnd = true;
		}
		if(codigo != null && codigo.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") != -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" d.tipo = :tipo ");
			isAnd = true;
		}
		if(codigo != null && codigo.length() > 0)
		{
			if(sbQuery.indexOf("WHERE") != -1) { sbQuery.append(" WHERE "); }
			if(isAnd) { sbQuery.append(" AND "); }
			sbQuery.append(" d.tipo = :tipo ");
			isAnd = true;
		}
		*/
		
		try{
			resultList = em.createQuery(sbQuery.toString())
					.getResultList();		
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}	
	}
	
	/*
	List<CartEmpleadoCE> resultList;
	String query="SELECT CAREMP from  CartEmpleadoCE CAREMP " +
			"INNER JOIN CAREMP.empleado EMP "+
	        "INNER JOIN CAREMP.carterizacionCE CARTERZ " +
	        "WHERE CAREMP.carterizacionCE.id=CARTERZ.id AND CAREMP.empleado.id = EMP.id AND " +
	        "EMP.perfil.id=:idPerfil AND  EMP.oficina.id=:idOficina "+
	        "AND EMP.id=:idEmpleado AND CAREMP.empleado.perfil.id=:idPerfil AND CAREMP.empleado.oficina.id=:idOficina "+ 
	        "AND CAREMP.flagActivo = :flagActivo "+
	        "AND CARTERZ.flagActivo = :flagActivo "+
	        "AND EMP.flagActivo = :flagActivo  and CARTERZ.id in (SELECT CARTERR.carterizacionCE.id from  CartTerritorioCE CARTERR " +
	        "Where CARTERR.territorio.id= :idTerritorio AND CARTERR.producto.id= :idProducto)";
	
	LOG.info("query = "+query);
	try{
		resultList = em.createQuery(query)
				.setParameter("idPerfil", idPerfil)
				.setParameter("idOficina", idOficina)
				.setParameter("idProducto", idProducto)
				.setParameter("idTerritorio", idTerritorio)
				.setParameter("idEmpleado", idEmpleado)
				.setParameter("flagActivo", "1")
				.getResultList();		
		return resultList;			
	}catch (NoResultException e) {
		return null;
	}	
	*/
}
