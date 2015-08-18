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
			queryWhere+=" AND e.nroContrato = :numeroContrato ";
		
		//CODIGO RVGL			(1)
		if(!arrayList.isEmpty() && arrayList.get(1)!=null && !arrayList.get(1).toString().equals(""))
			queryWhere+=" AND e.rvgl = :codigoRvgl ";
		
		//CODIGO PRE EVALUADOR 	(2)
		if(!arrayList.isEmpty() && arrayList.get(2)!=null && !arrayList.get(2).toString().equals(""))
			queryWhere+=" AND e.codPreEval = :codigoPreEvaluador";
		
		//SEGMENTO 				(3)
		if(!arrayList.isEmpty() && arrayList.get(3)!=null && !arrayList.get(3).toString().equals(""))
			queryWhere+=" AND e.desSegmento = :segmento";
		
		//SUB PRODUCTO 			(4)
		if(!arrayList.isEmpty() && arrayList.get(4)!=null && !arrayList.get(4).toString().equals(""))
			queryWhere+=" AND UPPER(e.desSubProducto) = :codigoSubProducto";
		
		//TIPO DE OFERTA		(5)
		if(!arrayList.isEmpty() && arrayList.get(5)!=null && !arrayList.get(5).toString().equals(""))
			queryWhere+=" AND e.idTipoOferta = :codigoTipoOferta";
		
		//APELLIDO PATERNO		(6)
		if(!arrayList.isEmpty() && arrayList.get(6)!=null && !arrayList.get(6).toString().equals(""))
			queryWhere+=" AND UPPER(e.apePat) = :apePaterno";
		
		//APELLIDO MATERNO		(7)
		if(!arrayList.isEmpty() && arrayList.get(7)!=null && !arrayList.get(7).toString().equals(""))
			queryWhere+=" AND UPPER(e.apeMat) = :apeMaterno";
		
		//NOMBRE				(8)
		if(!arrayList.isEmpty() && arrayList.get(8)!=null && !arrayList.get(8).toString().equals(""))
			queryWhere+=" AND UPPER(e.nombre) = :nombreCompl";	
		
		//NUMERO DOI			(9)
		if(!arrayList.isEmpty() && arrayList.get(9)!=null && !arrayList.get(9).toString().equals(""))
			queryWhere+=" AND e.numDoi = :numDoi";
		
		//TIPO DOI				(10)
		if(!arrayList.isEmpty() && arrayList.get(10)!=null && !arrayList.get(10).toString().equals("-1") && !arrayList.get(10).toString().equals(""))
			queryWhere+=" AND e.tipoDoi = :tipoDoi";		
		
		queryWhere+=" AND e.id in (:listIds)";
		
		String query="SELECT e FROM VistaBandejaExpediente e WHERE 1=1 "+queryWhere;
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
					queryExe.setParameter("apePaterno" , arrayList.get(6).toString().trim().toUpperCase());
				//APELLIDO MATERNO		(7)
				if(!arrayList.isEmpty() && arrayList.get(7)!=null && !arrayList.get(7).toString().equals(""))				
					queryExe.setParameter("apeMaterno", arrayList.get(7).toString().trim().toUpperCase());
				//NOMBRE				(8)
				if(!arrayList.isEmpty() && arrayList.get(8)!=null && !arrayList.get(8).toString().equals(""))				
					queryExe.setParameter("nombreCompl", arrayList.get(8).toString().trim().toUpperCase());
				//NUMERO DOI				(9)
				if(!arrayList.isEmpty() && arrayList.get(9)!=null && !arrayList.get(9).toString().equals(""))				
					queryExe.setParameter("numDoi", arrayList.get(9).toString().trim());
				//TIPO DOI				(10)
				if(!arrayList.isEmpty() && arrayList.get(10)!=null && !arrayList.get(10).toString().equals("-1") && !arrayList.get(10).toString().equals(""))			
					queryExe.setParameter("tipoDoi",  Long.parseLong(arrayList.get(10).toString()));
				
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
			//queryWhere+=" AND e.idProd in (:listIdsProd)";
			if(listIdsProd.size()==1)
				queryWhere+=" AND e.idProd = :idProducto";
			else
				queryWhere+=" AND (e.idProd = 1 OR e.idProd = 2)";
		}
		
		//ID USUARIO SESSION 	(0)
		if(!arrayList.isEmpty() && arrayList.get(0)!=null && !arrayList.get(0).toString().equals("")){
			//queryWhere+=" AND (e.idOficina = :idOficinaPrin OR e.idOficinaPrincipal = :idOficinaPrin ) AND e.flagDesplazada like :activo";
			queryWhere+=" AND (e.idOficina = :idOficinaPrin OR (e.idOficinaPrincipal = :idOficinaPrin AND e.flagDesplazada like :activo) )  ";			
		}

		queryWhere+=" AND e.id in (:listIds) ";
		
		String query="SELECT e FROM VistaBandejaExpediente e WHERE 1=1 "+queryWhere;
		LOG.info("query = "+query);
		
		try{
			Query queryExe= em.createQuery(query);
			
			queryExe.setParameter("listIds", listIds);
			
			if(listIdsProd!=null && listIdsProd.size()>0){
				//queryExe.setParameter("listIdsProd", listIdsProd);
				if(listIdsProd.size()==1){
					queryExe.setParameter("idProducto",listIdsProd.get(0).longValue());
					LOG.info("un solo valor de producto = "+listIdsProd.get(0).longValue());					
				}
			}
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
