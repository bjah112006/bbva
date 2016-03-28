package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.GuiaDocumentariaBeanLocal;

/**
 * Session Bean implementation class GuiaDocumentariaBean
 */
@Stateless
@Local(GuiaDocumentariaBeanLocal.class)
public class GuiaDocumentariaBean extends AbstractFacade<GuiaDocumentaria> implements GuiaDocumentariaBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(GuiaDocumentariaBean.class);

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public GuiaDocumentariaBean() {
        super(GuiaDocumentaria.class);
    }
    
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<GuiaDocumentaria> resultadoBusqueda(GuiaDocumentaria g){
		String where = "";
		
		if ( g.getTipoDocumento()!=null && g.getTipoDocumento().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"g.tipoDocumento.id = : idTipoDocumento ";
		}
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"g.producto.id = :idProducto ";
		}
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+"g.tipoOferta.id = :idTipoOferta ";
		}
		
		if ( g.getCategoriaRenta()!=null && g.getCategoriaRenta().getId()>0 ) {
			where += (where.length()==0? "" : " AND ")+"g.categoriaRenta.id = :idCategoriaRenta ";
		}
		
		if ( g.getPersona()!=null && g.getPersona().getId()>0 ) {
			//where += (where.length()==0? "" : " AND ")+"h.clienteNatural.apeMat = '"+h.getClienteNatural().getApeMat()+"'";
			where += (where.length()==0? "" : " AND ")+"g.persona.id = :idPersona ";
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId()>0 ) {
			where += (where.length()==0? "" : " AND ")+"g.tarea.id = :idTarea ";
		}		
		
		//String query = "SELECT g FROM GuiaDocumentaria g "+(where.length()==0? "" : (" WHERE "+where)+ " ORDER BY g.id ASC");
		String query1 = "SELECT g FROM GuiaDocumentaria g "+(where.length()==0? "" : (" WHERE "+where));
		String query2= " ORDER BY g.id ASC";
		
		String query=query1+query2;
		
		LOG.info("query : "+query);
		
		//List<Historial> resultList = em.createQuery(query).getResultList();
				
		Query sql = (Query) em.createQuery(query);
		
		if ( g.getTipoDocumento()!=null && g.getTipoDocumento().getId() > 0 ) {
			sql.setParameter("idTipoDocumento",  g.getTipoDocumento().getId());
			LOG.info("id TipoDocumento"+ g.getTipoDocumento().getId());
		}
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			sql.setParameter("idProducto",g.getProducto().getId());
			LOG.info("idProducto "+g.getProducto().getId());
		}
		
		if (  g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			sql.setParameter("idTipoOferta", g.getTipoOferta().getId());
			LOG.info("idTipoOferta "+g.getTipoOferta().getId());
		}
		
		if (g.getCategoriaRenta()!=null && g.getCategoriaRenta().getId()>0 ) {
			sql.setParameter("idCategoriaRenta", g.getCategoriaRenta().getId());
			LOG.info("idCategoriaRenta "+g.getCategoriaRenta().getId());
		}
		
		if ( g.getPersona()!=null && g.getPersona().getId()>0  ) {
			sql.setParameter("idPersona",g.getPersona().getId());
			LOG.info("idPersona "+g.getPersona().getId());
		}		
		
		if ( g.getTarea()!=null && g.getTarea().getId()>0  ) {
			sql.setParameter("idTarea",g.getTarea().getId());
			LOG.info("idTarea "+g.getTarea().getId());
		}
		
		LOG.info("sql resultadoBusqueda Guia Doc : "+sql);
		
		List<GuiaDocumentaria> resultList = sql.getResultList();
		
		LOG.info("resultList:::::" + resultList.size());
		
		return resultList; 
		
	}
	

	@Override
	public List<GuiaDocumentaria> buscarGuiaDoc(GuiaDocumentaria g) {
		//	.setParameter("subproducto", g.getSubproducto().getId())
		
/*		List<GuiaDocumentaria> resultList = em.createNamedQuery("GuiaDocumentaria.findGuiaDoc")
				.setParameter("producto", g.getProducto().getId())
				.setParameter("subproducto", g.getSubproducto().getId())
				.setParameter("tipoOferta", g.getTipoOferta().getId())
				.setParameter("flagCliente", g.getFlagCliente())
				.setParameter("flagPagoHab", g.getFlagPagoHab())
				.setParameter("flagPep", g.getFlagPep())
				.setParameter("flagTasaEsp", g.getFlagTasaEsp())
				.setParameter("flagVerifDom", g.getFlagVerifDom())
				.setParameter("flagVerifLab", g.getFlagVerifLab())
				.getResultList();*/

				List<GuiaDocumentaria> resultList = em.createNamedQuery("GuiaDocumentaria.findGuiaDoc")
		.setParameter("producto", g.getProducto().getId())		
		.setParameter("tipoOferta", g.getTipoOferta().getId())
		.setParameter("flagCliente", g.getFlagCliente())
		.setParameter("flagPagoHab", g.getFlagPagoHab())
		.setParameter("flagPep", g.getFlagPep())
		.setParameter("flagTasaEsp", g.getFlagTasaEsp())
		.setParameter("flagVerifDom", g.getFlagVerifDom())
		.setParameter("flagVerifLab", g.getFlagVerifLab())
		.setParameter("flagDps", g.getFlagDps())
		.getResultList();	
		
		
		return resultList;
	}	
	
	//@Override
	public List<GuiaDocumentaria> obtenerGuiaDocOrden_(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String where = "";
		String whereAND = "";
		String whereOR = "";	
		String wherePersona = "";
		String whereOpcional = "";
		String whereObligatorio = "";
		
		String verifDom = g.getFlagVerifDom() == null ? "0" : g.getFlagVerifDom();
		String verifLab = g.getFlagVerifLab() == null ? "0" : g.getFlagVerifLab();
		String verifDps = g.getFlagDps() == null ? "0" : g.getFlagDps();
		String pep = g.getFlagPep() == null ? "0" : g.getFlagPep();
		String tasaEsp = g.getFlagTasaEsp() == null ? "0" : g.getFlagTasaEsp();
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tarea.id = "+g.getTarea().getId();			
	    }
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();			
		}
		
		if ( g.getFlagCliente()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";			
		}
		
		if ( g.getFlagCliente()!=null ) {
			if (g.getFlagCliente().equals("1")) {
				if ( g.getFlagPagoHab()!=null ) {
					whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";					
				}
			}else{
				whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPagoHab = '0' ";				
			}
		}
		
		where = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1' AND g.flagPep = '0' AND g.flagTasaEsp = '0' AND g.flagVerifDom = '0' AND g.flagVerifLab = '0' AND g.flagDps = '0' AND g.categoriaRenta.id is null ";

		wherePersona += " g.persona.id = 1 ";
		
		if ( isConyugue ) {
			wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}	
		
		where += (where.length()==0? "" : " AND ")+"(" + wherePersona + ")";		
//------------------------------------------------------------------------------------------------
		if ( g.getFlagPep()!=null ) {
			if ( g.getFlagPep().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ") + " g.flagPep = '"+g.getFlagPep()+"'";	
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";
			}
		}		
		
		if ( g.getFlagTasaEsp()!=null ) {
			if ( g.getFlagTasaEsp().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ") + " g.flagTasaEsp = '"+g.getFlagTasaEsp()+"'";
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";
			}
		}
		
		if ( g.getFlagVerifDom()!=null ) {
			if ( g.getFlagVerifDom().equalsIgnoreCase("1") ) {	
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ") + " g.flagVerifDom = '"+g.getFlagVerifDom()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";
			}
		}
		
		if ( g.getFlagVerifLab()!=null ) {
			if ( g.getFlagVerifLab().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ") + " g.flagVerifLab = '"+g.getFlagVerifLab()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";
			}
		}		

		if ( g.getFlagDps()!=null ) {
			if ( g.getFlagDps().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ") + " g.flagDps = '"+g.getFlagDps()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";				
			}
		}
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			//if ( g.getTipoOferta().getId() == 2 ) {
				if(categoriaRenta.size() < 1){
					whereOR = " OR g.categoriaRenta.id = 0 ";
				}
				else{					
					CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
					for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
						whereAND = "";
						categoriaRentaTmp = new CategoriaRenta();
						categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
						g.setCategoriaRenta(categoriaRentaTmp);
						
						if ( g.getCategoriaRenta()==null ) { g.getCategoriaRenta().setId(0); }
						whereAND += (whereAND.length()==0? "" : " AND ")+" g.categoriaRenta.id = "+g.getCategoriaRenta().getId();
						
						whereAND += (whereAND.length()==0? "" : " AND ")+ whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1' AND g.flagPep = '0' AND g.flagTasaEsp = '0' AND g.flagVerifDom = '0' AND g.flagVerifLab = '0' AND g.flagDps = '0' ";						
						whereOR += " OR (" + whereAND + ") ";
					}
				}				
				where += whereOR;
			//}
		}
				
		String query = "SELECT g FROM GuiaDocumentaria g "+(where.length()==0? "" : (" WHERE "+where));;
		query=query+" ORDER BY g.persona.id ASC, g.obligatorio DESC";
		LOG.info("obtenerGuiaDocOrden - query 1: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		return resultList;
	}
	
	/*@Override
	public List<GuiaDocumentaria> obtenerGuiaDocOrden_ORIGINAL(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String whereOR = "";	
		String wherePersona = "";
		String whereObligatorio = "";
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tarea.id = "+g.getTarea().getId();			
	    }
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();			
		}
		
		if ( g.getFlagCliente()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";
		}

		if ( g.getFlagPagoHab()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";
		}
		
		if ( g.getFlagVerifDom()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagVerifDom = '"+g.getFlagVerifDom()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagVerifDom = '0'";
		}
		
		if ( g.getFlagVerifLab()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagVerifLab = '"+g.getFlagVerifLab()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagVerifLab = '0'";
		}
		
		if ( g.getFlagDps()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagDps = '"+g.getFlagDps()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagDps = '0'";
		}
		
		if ( g.getFlagPep()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '"+g.getFlagPep()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '0'";
		}
		
		if ( g.getFlagTasaEsp()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagTasaEsp = '"+g.getFlagTasaEsp()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagTasaEsp = '0'";
		}
		
		if(categoriaRenta.size() < 1){
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.categoriaRenta.id IS NULL OR g.categoriaRenta.id = 0) ";
		}
		else{					
			CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
			for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
				categoriaRentaTmp = new CategoriaRenta();
				categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
				g.setCategoriaRenta(categoriaRentaTmp);
				
				if ( g.getCategoriaRenta()==null ) { 
					g.getCategoriaRenta().setId(0); 
				}
				
				if(i <= n-2){
					whereOR = whereOR + "g.categoriaRenta.id= "+g.getCategoriaRenta().getId() + " OR ";
				}
				else{
					whereOR = whereOR + "g.categoriaRenta.id= "+g.getCategoriaRenta().getId();
				}
				
			}
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(" + whereOR + ")";
		}

		wherePersona += " g.persona.id = 1 ";
		if ( isConyugue ) {
			wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(" + wherePersona + ")";
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1'";	
				
		String query = "SELECT g FROM GuiaDocumentaria g "+(whereObligatorio.length()==0? "" : (" WHERE "+whereObligatorio));;
		query = query + " ORDER BY g.persona.id ASC, g.obligatorio DESC";
		LOG.info("obtenerGuiaDocOrden - query 1: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		
		List<GuiaDocumentaria> resultListTmp = new ArrayList<GuiaDocumentaria>();
		Set<String> resultSet = new HashSet<String>();
		for(GuiaDocumentaria guiaDocumentaria : resultList){
			resultSet.add(String.valueOf(guiaDocumentaria.getTipoDocumento().getId()));
		}
		
		for(String idTipoDocumento : resultSet){
			GuiaDocumentaria guiaDocumentariaObligatoria = null;
			GuiaDocumentaria guiaDocumentariaNoObligatoria = null;
			for(GuiaDocumentaria guiaDocumentaria : resultList){
				if(String.valueOf(guiaDocumentaria.getTipoDocumento().getId()).equals(idTipoDocumento)){
					if(guiaDocumentaria.getObligatorio().equals("1")){
						guiaDocumentariaObligatoria = guiaDocumentaria;
					}
					if(guiaDocumentaria.getObligatorio().equals("0")){
						guiaDocumentariaNoObligatoria = guiaDocumentaria;
					}
				}
			}
			if(guiaDocumentariaObligatoria != null){
				resultListTmp.add(guiaDocumentariaObligatoria);
			}
			if(guiaDocumentariaNoObligatoria != null){
				resultListTmp.add(guiaDocumentariaNoObligatoria);
			}
		}
		
		for(GuiaDocumentaria guiaDocumentaria : resultListTmp){
			LOG.info("añadido:::::"+ guiaDocumentaria.getId() + " - " + guiaDocumentaria.getTipoDocumento().getDescripcion() + " - " + guiaDocumentaria.getObligatorio());
		}
		
		return resultListTmp;
	}*/
	
	@Override
	public List<GuiaDocumentaria> obtenerGuiaDocOrden(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String whereOR = "";	
		String wherePersona = "";
		String whereObligatorio = "";
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tarea.id = "+g.getTarea().getId();			
	    }
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.tipoOferta.id = "+g.getTipoOferta().getId()+" OR g.tipoOferta.id IS NULL) ";			
		}
		
		if ( g.getFlagCliente()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagCliente = '"+g.getFlagCliente()+"' OR g.flagCliente IS NULL) ";
		}

		if ( g.getFlagPagoHab()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagPagoHab = '"+g.getFlagPagoHab()+"' OR g.flagPagoHab IS NULL) ";
		}
		
		if ( g.getFlagVerifDom()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifDom = '"+g.getFlagVerifDom()+"' OR g.flagVerifDom IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifDom = '0' OR g.flagVerifDom IS NULL) ";
		}
		
		if ( g.getFlagVerifLab()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifLab = '"+g.getFlagVerifLab()+"' OR g.flagVerifLab IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifLab = '0' OR g.flagVerifLab IS NULL) ";
		}
		
		if ( g.getFlagDps()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagDps = '"+g.getFlagDps()+"' OR g.flagDps IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagDps = '0' OR g.flagDps IS NULL) ";
		}
		
		if ( g.getFlagPep()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '"+g.getFlagPep()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '0'";
		}
		
		if ( g.getFlagTasaEsp()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagTasaEsp = '"+g.getFlagTasaEsp()+"' OR g.flagTasaEsp IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagTasaEsp = '0' OR g.flagTasaEsp IS NULL) ";
		}
		
		if(categoriaRenta.size() < 1){
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.categoriaRenta.id IS NULL OR g.categoriaRenta.id = 0) ";
		}
		else{					
			CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
			for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
				categoriaRentaTmp = new CategoriaRenta();
				categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
				g.setCategoriaRenta(categoriaRentaTmp);
				
				if ( g.getCategoriaRenta()==null ) { 
					g.getCategoriaRenta().setId(0); 
				}
				
				if(i <= n-2){
					whereOR = whereOR + "g.categoriaRenta.id= "+g.getCategoriaRenta().getId() + " OR ";
				}
				else{
					whereOR = whereOR + "g.categoriaRenta.id= "+g.getCategoriaRenta().getId();
				}
				
			}
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(" + whereOR + " OR g.categoriaRenta.id IS NULL)";
		}

		wherePersona += " g.persona.id = 1 ";
		if ( isConyugue ) {
			wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(" + wherePersona + " OR g.persona.id IS NULL)";
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1'";
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+ " (g.subproducto.id IS NULL or g.subproducto.id = 0) ";
				
		String query = "SELECT g FROM GuiaDocumentaria g "+(whereObligatorio.length()==0? "" : (" WHERE "+whereObligatorio));;
		query = query + " ORDER BY g.persona.id ASC, g.obligatorio DESC";
		LOG.info("obtenerGuiaDocOrden - query 1: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		
		List<GuiaDocumentaria> resultListTmp = new ArrayList<GuiaDocumentaria>();
		Set<String> resultSet = new HashSet<String>();
		for(GuiaDocumentaria guiaDocumentaria : resultList){
			resultSet.add(String.valueOf(guiaDocumentaria.getTipoDocumento().getId()));
		}
		
		for(String idTipoDocumento : resultSet){
			GuiaDocumentaria guiaDocumentariaObligatoria = null;
			GuiaDocumentaria guiaDocumentariaNoObligatoria = null;
			for(GuiaDocumentaria guiaDocumentaria : resultList){
				if(String.valueOf(guiaDocumentaria.getTipoDocumento().getId()).equals(idTipoDocumento)){
					if(guiaDocumentaria.getObligatorio().equals("1")){
						guiaDocumentariaObligatoria = guiaDocumentaria;
					}
					if(guiaDocumentaria.getObligatorio().equals("0")){
						guiaDocumentariaNoObligatoria = guiaDocumentaria;
					}
				}
			}
			if(guiaDocumentariaObligatoria != null){
				resultListTmp.add(guiaDocumentariaObligatoria);
			}
			if(guiaDocumentariaNoObligatoria != null){
				resultListTmp.add(guiaDocumentariaNoObligatoria);
			}
		}
		
		for(GuiaDocumentaria guiaDocumentaria : resultListTmp){
			LOG.info("añadido:::::"+ guiaDocumentaria.getId() + " - " + guiaDocumentaria.getTipoDocumento().getDescripcion() + " - " + guiaDocumentaria.getObligatorio());
		}
		
		return resultListTmp;
	}
	
	
	
	
	
	@Override
	public List<GuiaDocumentaria> obtenerGuiaDocXSubProdOrden(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String whereOR = "";	
		String wherePersona = "";
		String whereObligatorio = "";
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}
		
		if ( g.getSubproducto()!=null && g.getSubproducto().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(g.subproducto.id = "+g.getSubproducto().getId()+" OR g.subproducto.id IS NULL) ";
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tarea.id = "+g.getTarea().getId();			
	    }
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.tipoOferta.id = "+g.getTipoOferta().getId()+" OR g.tipoOferta.id IS NULL) ";			
		}
		
		if ( g.getFlagCliente()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagCliente = '"+g.getFlagCliente()+"' OR g.flagCliente IS NULL) ";
		}

		if ( g.getFlagPagoHab()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagPagoHab = '"+g.getFlagPagoHab()+"' OR g.flagPagoHab IS NULL) ";
		}
		
		if ( g.getFlagVerifDom()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifDom = '"+g.getFlagVerifDom()+"' OR g.flagVerifDom IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifDom = '0' OR g.flagVerifDom IS NULL) ";
		}
		
		if ( g.getFlagVerifLab()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifLab = '"+g.getFlagVerifLab()+"' OR g.flagVerifLab IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagVerifLab = '0' OR g.flagVerifLab IS NULL) ";
		}
		
		if ( g.getFlagDps()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagDps = '"+g.getFlagDps()+"' OR g.flagDps IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagDps = '0' OR g.flagDps IS NULL) ";
		}
		
		if ( g.getFlagPep()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '"+g.getFlagPep()+"'";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '0'";
		}
		
		if ( g.getFlagTasaEsp()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagTasaEsp = '"+g.getFlagTasaEsp()+"' OR g.flagTasaEsp IS NULL) ";
		}
		else{
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.flagTasaEsp = '0' OR g.flagTasaEsp IS NULL) ";
		}
		
		if(categoriaRenta.size() < 1){
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" (g.categoriaRenta.id IS NULL OR g.categoriaRenta.id = 0) ";
		}
		else{					
			CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
			for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
				categoriaRentaTmp = new CategoriaRenta();
				categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
				g.setCategoriaRenta(categoriaRentaTmp);
				
				if ( g.getCategoriaRenta()==null ) { 
					g.getCategoriaRenta().setId(0); 
				}
				
				if(i <= n-2){
					whereOR = whereOR + "g.categoriaRenta.id= "+g.getCategoriaRenta().getId() + " OR ";
				}
				else{
					whereOR = whereOR + "g.categoriaRenta.id= "+g.getCategoriaRenta().getId();
				}
				
			}
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(" + whereOR + " OR g.categoriaRenta.id IS NULL)";
		}

		wherePersona += " g.persona.id = 1 ";
		if ( isConyugue ) {
			wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+"(" + wherePersona + " OR g.persona.id IS NULL)";
		whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1'";	
				
		String query = "SELECT g FROM GuiaDocumentaria g "+(whereObligatorio.length()==0? "" : (" WHERE "+whereObligatorio));;
		query = query + " ORDER BY g.persona.id ASC, g.obligatorio DESC";
		LOG.info("obtenerGuiaDocOrden - query 1: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		
		List<GuiaDocumentaria> resultListTmp = new ArrayList<GuiaDocumentaria>();
		Set<String> resultSet = new HashSet<String>();
		for(GuiaDocumentaria guiaDocumentaria : resultList){
			resultSet.add(String.valueOf(guiaDocumentaria.getTipoDocumento().getId()));
		}
		
		for(String idTipoDocumento : resultSet){
			GuiaDocumentaria guiaDocumentariaObligatoria = null;
			GuiaDocumentaria guiaDocumentariaNoObligatoria = null;
			for(GuiaDocumentaria guiaDocumentaria : resultList){
				if(String.valueOf(guiaDocumentaria.getTipoDocumento().getId()).equals(idTipoDocumento)){
					if(guiaDocumentaria.getObligatorio().equals("1")){
						guiaDocumentariaObligatoria = guiaDocumentaria;
					}
					if(guiaDocumentaria.getObligatorio().equals("0")){
						guiaDocumentariaNoObligatoria = guiaDocumentaria;
					}
				}
			}
			if(guiaDocumentariaObligatoria != null){
				resultListTmp.add(guiaDocumentariaObligatoria);
			}
			if(guiaDocumentariaNoObligatoria != null){
				resultListTmp.add(guiaDocumentariaNoObligatoria);
			}
		}
		
		for(GuiaDocumentaria guiaDocumentaria : resultListTmp){
			LOG.info("añadido:::::"+ guiaDocumentaria.getId() + " - " + guiaDocumentaria.getTipoDocumento().getDescripcion() + " - " + guiaDocumentaria.getObligatorio());
		}
		
		return resultListTmp;
	}
	
	
	
	
	
	
	
	
	
	@Override
	public List<GuiaDocumentaria> obtenerGuiaDoc(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String where = "";
		String whereAND = "";
		String whereOR = "";	
		String wherePersona = "";
		String whereOpcional = "";
		String whereObligatorio = "";
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tarea.id = "+g.getTarea().getId();			
	    }
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();			
		}
		
		if ( g.getFlagCliente()!=null ) {
			whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";			
		}
		
		if ( g.getFlagCliente()!=null ) {
			if (g.getFlagCliente().equals("1")) {
				if ( g.getFlagPagoHab()!=null ) {
					whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";					
				}
			}else{
				whereObligatorio += (whereObligatorio.length()==0? "" : " AND ")+" g.flagPagoHab = '0' ";				
			}
		}	
		
		where = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1' AND g.flagPep = '0' AND g.flagTasaEsp = '0' AND g.flagVerifDom = '0' AND g.flagVerifLab = '0' AND g.flagDps = '0' AND g.categoriaRenta.id is null";

		wherePersona += " g.persona.id = 1 ";

		if ( isConyugue ) {
			wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				wherePersona += (wherePersona.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}	
		
		where = "(" + wherePersona + ")";		
//-----------------------------------------------------
		if ( g.getFlagPep()!=null ) {
			if ( g.getFlagPep().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+" g.flagPep = '"+g.getFlagPep()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";				
			}
		}		
		
		if ( g.getFlagTasaEsp()!=null ) {
			if ( g.getFlagTasaEsp().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+" g.flagTasaEsp = '"+g.getFlagTasaEsp()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";				
			}
		}

		if ( g.getFlagVerifDom()!=null ) {
			if ( g.getFlagVerifDom().equalsIgnoreCase("1") ) {	
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+" g.flagVerifDom = '"+g.getFlagVerifDom()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";
			}
		}
		
		if ( g.getFlagVerifLab()!=null ) {
			if ( g.getFlagVerifLab().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+" g.flagVerifLab = '"+g.getFlagVerifLab()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";				
			}
		}
		
		if ( g.getFlagDps()!=null ) {
			if ( g.getFlagDps().equalsIgnoreCase("1") ) {
				whereOpcional = whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+" g.flagDps = '"+g.getFlagDps()+"'";				
				where += (whereObligatorio.length()==0? "" : " OR ")+"(" + whereOpcional + ")";				
			}
		}
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			//if ( g.getTipoOferta().getId() == 2 ) {
				if(categoriaRenta.size() < 1){
					whereOR = " OR g.categoriaRenta.id = 0 ";
				}
				else{					
					CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
					for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
						whereAND = "";
						categoriaRentaTmp = new CategoriaRenta();
						categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
						g.setCategoriaRenta(categoriaRentaTmp);
						
						if ( g.getCategoriaRenta()==null ) { g.getCategoriaRenta().setId(0);}
						whereAND += (whereAND.length()==0? "" : " AND ")+" g.categoriaRenta.id = "+g.getCategoriaRenta().getId();
						
						whereAND += (whereAND.length()==0? "" : " AND ")+ whereObligatorio + (whereObligatorio.length()==0? "" : " AND ")+ " g.flagActivo = '1' AND  g.flagPep = '0' AND g.flagTasaEsp = '0' AND g.flagVerifDom = '0' AND g.flagVerifLab = '0' AND g.flagDps = '0' ";						
						whereOR += " OR (" + whereAND + ") ";
					}
				}				
				where += whereOR;
			//}
		}		
		
		String query = "SELECT g FROM GuiaDocumentaria g "+(where.length()==0? "" : (" WHERE "+where));
				
		LOG.info("query 2: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		return resultList;
	}
	
	@Override
	public Persona obtenerPersonaPorCodigo(String codigo){
		
		try{
			return (Persona) em.createNamedQuery("Persona.findByCodigo")
					.setParameter("codigo", codigo)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}
	
	/*@Override
	public List<GuiaDocumentaria> obtenerGuiaDocOrden(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String where = "";
		String whereAND = "";
		String whereOR = "";		
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}		
		if ( g.getSubproducto()!=null && g.getSubproducto().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+" g.subproducto.id = "+g.getSubproducto().getId();			
		}
			
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();			
		}
		if ( g.getFlagCliente()!=null ) {
			where += (where.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";			
		}
		if ( g.getFlagCliente()!=null ) {
			if (g.getFlagCliente().equals("1")) {
				if ( g.getFlagPagoHab()!=null ) {
					where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";					
				}
			}else{
				where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '0' ";				
			}
		}		
		
		where += (where.length()==0? "" : " AND ")+" g.persona.id = 1 ";
		
		where += " AND g.tarea.id IS NULL AND (g.categoriaRenta.id IS NULL OR g.categoriaRenta.id = 0) "; 
		where = "(" + where + ")";
		
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			if ( g.getTipoOferta().getId() == 2 ) {
				if(categoriaRenta.size() < 1){
					whereOR = " OR g.categoriaRenta.id = 0 ";
				}
				else{					
					CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
					for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
						whereAND = "";
						categoriaRentaTmp = new CategoriaRenta();
						categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
						g.setCategoriaRenta(categoriaRentaTmp);
						
						if ( g.getCategoriaRenta()==null ) { g.getCategoriaRenta().setId(0);}
						whereAND += (whereAND.length()==0? "" : " AND ")+" g.categoriaRenta.id = "+g.getCategoriaRenta().getId();
						
						if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
						}
						if ( g.getSubproducto()!=null && g.getSubproducto().getId() > 0 ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.subproducto.id = "+g.getSubproducto().getId();
						}
						if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();
						}
						if ( g.getFlagCliente()!=null ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";
						}
						
						if ( g.getFlagCliente()!=null ) {
							if (g.getFlagCliente().equals("1")) {
								if ( g.getFlagPagoHab()!=null ) {
									where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";					
								}
							}else{
								where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '0' ";				
							}
						}						
						whereOR += " OR (" + whereAND + ") ";
					}
				}				
				where += whereOR;
			}
		}		
		
		if ( g.getFlagPep()!=null ) {
			if ( g.getFlagPep().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagPep = '"+g.getFlagPep()+"'";
			}
		}		
		if ( isConyugue ) {
			where += (where.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				where += (where.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			where += (where.length()==0? "" : " OR ")+" g.tarea.id = "+g.getTarea().getId();			
		}
		else{
			where += (where.length()==0? "" : " OR ")+" g.tarea.id = 1 ";
		}
		if ( g.getFlagTasaEsp()!=null ) {
			if ( g.getFlagTasaEsp().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagTasaEsp = '"+g.getFlagTasaEsp()+"'";
			}
		}
		if ( g.getFlagVerifDom()!=null ) {
			if ( g.getFlagVerifDom().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagVerifDom = '"+g.getFlagVerifDom()+"'";
			}
		}
		if ( g.getFlagVerifLab()!=null ) {
			if ( g.getFlagVerifLab().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagVerifLab = '"+g.getFlagVerifLab()+"'";
			}
		}		

		if ( g.getFlagDps()!=null ) {
			if ( g.getFlagDps().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagDps = '"+g.getFlagDps()+"'";
			}
		}
		
		String query = "SELECT g FROM GuiaDocumentaria g "+(where.length()==0? "" : (" WHERE "+where));;
		query=query+" ORDER BY g.persona.id ASC, g.obligatorio DESC";
		LOG.info("obtenerGuiaDocOrden - query 1: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		return resultList;
	}*/
	
	/*@Override
	public List<GuiaDocumentaria> obtenerGuiaDoc(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta ) {
		String where = "";
		String whereAND = "";
		String whereOR = "";		
		
		if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
		}		
		if ( g.getSubproducto()!=null && g.getSubproducto().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+" g.subproducto.id = "+g.getSubproducto().getId();			
		}
		
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
		where += (where.length()==0? "" : " AND ")+" g.tarea.id = "+g.getTarea().getId();			
	    }
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			where += (where.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();			
		}
		if ( g.getFlagCliente()!=null ) {
			where += (where.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";			
		}
		if ( g.getFlagCliente()!=null ) {
			if (g.getFlagCliente().equals("1")) {
				if ( g.getFlagPagoHab()!=null ) {
					where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";					
				}
			}else{
				where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '0' ";				
			}
		}		
		
		where += (where.length()==0? "" : " AND ")+" g.persona.id = 1 ";
		
		where += " AND g.tarea.id IS NULL AND (g.categoriaRenta.id IS NULL OR g.categoriaRenta.id = 0) "; 
		where = "(" + where + ")";
		
		
		if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
			if ( g.getTipoOferta().getId() == 2 ) {
				if(categoriaRenta.size() < 1){
					whereOR = " OR g.categoriaRenta.id = 0 ";
				}
				else{					
					CategoriaRenta categoriaRentaTmp = new CategoriaRenta(); 
					for (int i = 0, n = categoriaRenta.size(); i < n; i++) {
						whereAND = "";
						categoriaRentaTmp = new CategoriaRenta();
						categoriaRentaTmp.setId(Long.parseLong(categoriaRenta.get(i)));
						g.setCategoriaRenta(categoriaRentaTmp);
						
						if ( g.getCategoriaRenta()==null ) { g.getCategoriaRenta().setId(0);}
						whereAND += (whereAND.length()==0? "" : " AND ")+" g.categoriaRenta.id = "+g.getCategoriaRenta().getId();
						
						if ( g.getProducto()!=null && g.getProducto().getId() > 0 ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.producto.id = "+g.getProducto().getId();
						}
						if ( g.getSubproducto()!=null && g.getSubproducto().getId() > 0 ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.subproducto.id = "+g.getSubproducto().getId();
						}
						if ( g.getTipoOferta()!=null && g.getTipoOferta().getId() > 0 ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.tipoOferta.id = "+g.getTipoOferta().getId();
						}
						if ( g.getFlagCliente()!=null ) {
							whereAND += (whereAND.length()==0? "" : " AND ")+" g.flagCliente = '"+g.getFlagCliente()+"'";
						}
						
						if ( g.getFlagCliente()!=null ) {
							if (g.getFlagCliente().equals("1")) {
								if ( g.getFlagPagoHab()!=null ) {
									where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '"+g.getFlagPagoHab()+"'";					
								}
							}else{
								where += (where.length()==0? "" : " AND ")+" g.flagPagoHab = '0' ";				
							}
						}						
						whereOR += " OR (" + whereAND + ") ";
					}
				}				
				where += whereOR;
			}
		}		
		
		if ( g.getFlagPep()!=null ) {
			if ( g.getFlagPep().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagPep = '"+g.getFlagPep()+"'";
			}
		}		
		if ( isConyugue ) {
			where += (where.length()==0? "" : " OR ")+" g.persona.id = 2 ";
		}
		if ( g.getPersona()!=null && g.getPersona().getId() > 0 ) {
			if ( g.getPersona().getId() == 3) {
				where += (where.length()==0? "" : " OR ")+" g.persona.id = "+g.getPersona().getId();
			}
		}
		if ( g.getTarea()!=null && g.getTarea().getId() > 0 ) {
			where += (where.length()==0? "" : " OR ")+" g.tarea.id = "+g.getTarea().getId();			
		}
		else{
			where += (where.length()==0? "" : " OR ")+" g.tarea.id = 1 ";
		}
		if ( g.getFlagTasaEsp()!=null ) {
			if ( g.getFlagTasaEsp().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagTasaEsp = '"+g.getFlagTasaEsp()+"'";
			}
		}
		if ( g.getFlagVerifDom()!=null ) {
			if ( g.getFlagVerifDom().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagVerifDom = '"+g.getFlagVerifDom()+"'";
			}
		}
		if ( g.getFlagVerifLab()!=null ) {
			if ( g.getFlagVerifLab().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagVerifLab = '"+g.getFlagVerifLab()+"'";
			}
		}		
		if ( g.getFlagDps()!=null ) {
			if ( g.getFlagDps().equalsIgnoreCase("1") ) {
				where += (where.length()==0? "" : " OR ")+" g.flagDps = '"+g.getFlagDps()+"'";
			}
		}
		
		String query = "SELECT g FROM GuiaDocumentaria g "+(where.length()==0? "" : (" WHERE "+where));
				
		LOG.info("query 2: "+query);
		
		List<GuiaDocumentaria> resultList = em.createQuery(query).getResultList();
		return resultList;
	}*/	
	
	
	@Override
	public List<GuiaDocumentaria> buscarTodos() {
		List<GuiaDocumentaria> resultList = em.createNamedQuery("GuiaDocumentaria.findAll").getResultList();
		return resultList;
	}
	
	@Override
	public GuiaDocumentaria buscarPorId(long id) {
		//String idActivo="1";
		//String query="SELECT g FROM GuiaDocumentaria g WHERE g.id = :id and g.flagActivo like :idActivo";
		String query="SELECT g FROM GuiaDocumentaria g WHERE g.id = :id";
		LOG.info("query = "+query);
		try{
			GuiaDocumentaria resultList = (GuiaDocumentaria) em.createQuery(query)
					.setParameter("id", id)
					/*.setParameter("idActivo", idActivo)*/
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			LOG.info("NoResultException::::");
			LOG.error(e.getMessage(), e);
			return null;
		} catch(Exception ex){
			LOG.info("Exception::::");
			LOG.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	
	@Override
	public GuiaDocumentaria create(GuiaDocumentaria entity) {
		return super.create(entity);
	}
	
	@Override
	public void delete(GuiaDocumentaria entity) {
		super.remove(entity);
		
	}
	
	
	public void update(GuiaDocumentaria entity){
		super.edit(entity);
	}
	
	
	
	
	
	
}
