package com.ibm.bbva.session.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.OficinaBeanLocal;

/**
 * Session Bean implementation class OficinaBean
 */
@Stateless
@Local(OficinaBeanLocal.class)
public class OficinaBean extends AbstractFacade<Oficina> implements OficinaBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(OficinaBean.class);

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public OficinaBean() {
    	super(Oficina.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Oficina> buscarTodos() {
		List<Oficina> resultList = (List<Oficina>) em.createNamedQuery("Oficina.findAll").getResultList();
		List<Oficina> result=new ArrayList<Oficina>();
		ArrayList<Oficina> lista=null;
		
		if(resultList!=null)
			LOG.info("resultList ta,año:::"+resultList.size());
		
		Map<String, ArrayList<Oficina>> mapaOficina=new TreeMap<String,ArrayList<Oficina>>();

		if(resultList!=null)
		for (Oficina obj : resultList) {
						
			lista = mapaOficina.get(obj.getCodigo());
			if (lista==null || lista.size()<=0) {
				lista = new ArrayList<Oficina>();
				lista.add(obj);
				result.add(obj);
				mapaOficina.put(obj.getCodigo(), lista);
			}
			
			
		}
		

		
		//result=new ArrayList<Oficina>(lista);

		if(result!=null)
			LOG.info("result ta,año:::"+result.size());
		
		return result;
	}

	@Override
	public Oficina buscarPorId(long id) {
		return (Oficina) em.createNamedQuery("Oficina.findById").setParameter("id", id).getSingleResult();
	}
	
	@Override
	public Oficina buscarPorCodigo(String codigo) {
		Oficina oficina = null;
		try{
			oficina =(Oficina) em.createNamedQuery("Oficina.findByCodigo").setParameter("codigo", codigo).getSingleResult();
		}catch(Exception e){
			return oficina;
		}
		return oficina;
	}
	
	@Override
	public List<Oficina> buscarPorIdTerritorio(long idTerritorio) {
		List<Oficina>  resultList = em.createNamedQuery("Oficina.findByIdTerritorio").setParameter("idTerritorio", idTerritorio).getResultList();
		return resultList;
	}
	
	/**
	 * Obtiene las oficinas que tienen empleados relacionados
	 * y activos con el perfil indicado como parámetro.
	 * 
	 */
	@Override
	public List<Oficina> buscarPorIdPerfilEmpleados(long idPerfil) {
		Query query = em.createQuery("SELECT DISTINCT (e.oficina) FROM Empleado e WHERE e.flagActivo = '1' and e.perfil.id = :idPerfil AND e.oficina IS NOT NULL order by e.oficina.codigo asc");
		query = query.setParameter("idPerfil", idPerfil);
		List<Oficina>  resultList = query.getResultList();
		return resultList;
	}
	
	@Override
	public Oficina create(Oficina entity){
		return super.create(entity);
	}
    
    @Override
    public void edit(Oficina entity){
    	super.edit(entity);
    }
    
    @Override
	public List<Oficina> obtenerOficinasDesplazadas(long id) {
		Query query = em.createQuery("SELECT o FROM Oficina o WHERE o.flagDesplazada like '1' and o.oficinaPrincipal.id = :idOficina and o.flagActivo like '1'");
		query = query.setParameter("idOficina", id);
		List<Oficina>  resultList = query.getResultList();
		return resultList;
	}    
}