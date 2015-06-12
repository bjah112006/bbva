package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.VistaBandejaExpediente;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.VistaBandejaExpedienteBeanLocal;

@Stateless
@Local(VistaBandejaExpedienteBeanLocal.class)
public class VistaBandejaExpedienteBean extends AbstractFacade<VistaBandejaExpediente> implements VistaBandejaExpedienteBeanLocal{
	
	private static final Logger LOG = LoggerFactory.getLogger(VistaBandejaExpedienteBean.class);
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
	public VistaBandejaExpedienteBean() {
    	super(VistaBandejaExpediente.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<VistaBandejaExpediente> buscarPorIdsExpedientes(List<Long> listIds, ArrayList arrayList) {

		String queryWhere="";
		//NUMERO DE CONTRATO 	(0)
		if(!arrayList.isEmpty() && arrayList.get(0)!=null && !arrayList.get(0).toString().equals(""))
			queryWhere+=" AND e.nroContrato like :numeroContrato ";
		
		//CODIGO RVGL			(1)
		if(!arrayList.isEmpty() && arrayList.get(1)!=null && !arrayList.get(1).toString().equals(""))
			queryWhere+=" AND e.rvgl like :codigoRvgl ";
		
		//CODIGO PRE EVALUADOR 	(2)
		if(!arrayList.isEmpty() && arrayList.get(2)!=null && !arrayList.get(2).toString().equals(""))
			queryWhere+=" AND e.codPreEval like :codigoPreEvaluador";
		
		//SEGMENTO 				(3)
		if(!arrayList.isEmpty() && arrayList.get(3)!=null && !arrayList.get(3).toString().equals(""))
			queryWhere+=" AND e.desSegmento like :segmento";
		
		//SUB PRODUCTO 			(4)
		if(!arrayList.isEmpty() && arrayList.get(4)!=null && !arrayList.get(4).toString().equals(""))
			queryWhere+=" AND UPPER(e.desSubProducto) = :codigoSubProducto";
		
		//TIPO DE OFERTA		(5)
		if(!arrayList.isEmpty() && arrayList.get(5)!=null && !arrayList.get(5).toString().equals(""))
			queryWhere+=" AND e.idTipoOferta = :codigoTipoOferta";
		
		//APELLIDO PATERNO		(6)
		if(!arrayList.isEmpty() && arrayList.get(6)!=null && !arrayList.get(6).toString().equals(""))
			queryWhere+=" AND UPPER(e.apePat) like :apePaterno";
		
		//APELLIDO MATERNO		(7)
		if(!arrayList.isEmpty() && arrayList.get(7)!=null && !arrayList.get(7).toString().equals(""))
			queryWhere+=" AND UPPER(e.apeMat) like :apeMaterno";
		
		//NOMBRE				(8)
		if(!arrayList.isEmpty() && arrayList.get(8)!=null && !arrayList.get(8).toString().equals(""))
			queryWhere+=" AND UPPER(e.nombre) like :nombreCompl";		
		
		String query="SELECT e FROM VistaBandejaExpediente e WHERE e.id in (:listIds) "+queryWhere;
		LOG.info("query = "+query);
		
		try{
			Query queryExe= em.createQuery(query);
			
			queryExe.setParameter("listIds", listIds);
					
				//NUMERO DE CONTRATO 	(0)
				if(!arrayList.isEmpty() && arrayList.get(0)!=null && !arrayList.get(0).toString().equals(""))					
					queryExe.setParameter("numeroContrato", arrayList.get(0).toString().trim());
				//CODIGO RVGL			(1)
				if(!arrayList.isEmpty() && arrayList.get(1)!=null && !arrayList.get(1).toString().equals(""))				
					queryExe.setParameter("codigoRvgl", arrayList.get(1).toString().trim());
				//CODIGO PRE EVALUADOR 	(2)
				if(!arrayList.isEmpty() && arrayList.get(2)!=null && !arrayList.get(2).toString().equals(""))				
					queryExe.setParameter("codigoPreEvaluador", arrayList.get(2).toString().trim());
				//SEGMENTO 				(3)
				if(!arrayList.isEmpty() && arrayList.get(3)!=null && !arrayList.get(3).toString().equals(""))				
					queryExe.setParameter("segmento", arrayList.get(3).toString().trim());
				//SUB PRODUCTO 			(4)
				if(!arrayList.isEmpty() && arrayList.get(4)!=null && !arrayList.get(4).toString().equals(""))				
					queryExe.setParameter("codigoSubProducto", arrayList.get(4).toString().trim().toUpperCase());
				//TIPO DE OFERTA		(5)
				if(!arrayList.isEmpty() && arrayList.get(5)!=null && !arrayList.get(5).toString().equals(""))				
					queryExe.setParameter("codigoTipoOferta", Long.parseLong(arrayList.get(5).toString()));
				//APELLIDO PATERNO 		(6)
				if(!arrayList.isEmpty() && arrayList.get(6)!=null && !arrayList.get(6).toString().equals(""))				
					queryExe.setParameter("apePaterno", "%" + arrayList.get(6).toString().trim().toUpperCase() + "%");
				//APELLIDO MATERNO		(7)
				if(!arrayList.isEmpty() && arrayList.get(7)!=null && !arrayList.get(7).toString().equals(""))				
					queryExe.setParameter("apeMaterno", "%" + arrayList.get(7).toString().trim().toUpperCase() + "%");
				//NOMBRE				(8)
				if(!arrayList.isEmpty() && arrayList.get(8)!=null && !arrayList.get(8).toString().equals(""))				
					queryExe.setParameter("nombreCompl", "%" + arrayList.get(8).toString().trim().toUpperCase() + "%");
				
			List<VistaBandejaExpediente> resultList = (List<VistaBandejaExpediente>) queryExe.getResultList();
							
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}	
	
	@Override
	public List<VistaBandejaExpediente> buscarPorIdsExpedientes_BandjAsignacion(List<Long> listIds, ArrayList arrayList, 
			List<Long> listIdsProd) {

		String queryWhere="";
		
		//CARTERIZACION POR PRODUCTO
		if(listIdsProd!=null && listIdsProd.size()>0){
			queryWhere+=" AND e.idProd in (:listIdsProd)";
		}
		
		//ID USUARIO SESSION 	(0)
		if(!arrayList.isEmpty() && arrayList.get(0)!=null && !arrayList.get(0).toString().equals(""))
			queryWhere+=" AND (e.idOficina = :idOficinaPrin OR e.idOficinaPrincipal = :idOficinaPrin ) AND " +
					" e.flagDesplazada like :activo";
		
		String query="SELECT e FROM VistaBandejaExpediente e WHERE e.id in (:listIds) "+queryWhere;
		LOG.info("query = "+query);
		
		try{
			Query queryExe= em.createQuery(query);
			
			queryExe.setParameter("listIds", listIds);
			
			if(listIdsProd!=null && listIdsProd.size()>0)
				queryExe.setParameter("listIdsProd", listIdsProd);
			
			//NUMERO DE CONTRATO 	(0)
			if(!arrayList.isEmpty() && arrayList.get(0)!=null && !arrayList.get(0).toString().equals("")){					
				queryExe.setParameter("idOficinaPrin", Long.parseLong(arrayList.get(0).toString().trim()));
				queryExe.setParameter("activo", "1");
			}
			
			
			List<VistaBandejaExpediente> resultList = (List<VistaBandejaExpediente>) queryExe.getResultList();
							
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}	
	
}
