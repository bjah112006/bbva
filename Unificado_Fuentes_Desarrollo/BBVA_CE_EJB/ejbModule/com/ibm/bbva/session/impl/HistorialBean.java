package com.ibm.bbva.session.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.OficinaTemporal;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.HistorialBeanLocal;

/**
 * Session Bean implementation class HistorialBean
 */
@Stateless
@Local(HistorialBeanLocal.class)
public class HistorialBean extends AbstractFacade<Historial> implements HistorialBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(HistorialBean.class);
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public HistorialBean() {
        super(Historial.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Historial> buscarPorIdExpediente(long idExpediente) {
		List<Historial> resultList = em.createNamedQuery("Historial.findbyIdExpediente").setParameter("idExpediente", idExpediente).getResultList();
		return resultList;
	}
	
	@Override
	public Historial buscarPorId(long id) {
		return (Historial) em.createNamedQuery("Historial.findById").setParameter("id", id).getSingleResult();
	}

	@Override
	public List<Historial> buscarXcriterios(Historial h, Date fechaInicio, Date fechaFin, List<Long> list) {
		String where = "";
		
		if ( h.getExpediente()!=null && h.getExpediente().getId() > 0 ) {
			//where += (where.length()==0? "" : " AND ")+"h.expediente.id = "+h.getExpediente().getId();
			where += (where.length()==0? "" : " AND ")+"h.expediente.id = : idExpediente ";
		}
		
		if ( h.getProducto()!=null && h.getProducto().getId() > 0 ) {
			//where += (where.length()==0? "" : " AND ")+"h.producto.id = "+h.getProducto().getId();
			where += (where.length()==0? "" : " AND ")+"h.producto.id = :idProducto ";
		}
		
		if ( h.getSubproducto()!=null && h.getSubproducto().getId() > 0 ) {
			//where += (where.length()==0? "" : " AND ")+"h.subproducto.id = "+h.getSubproducto().getId();
			where += (where.length()==0? "" : " AND ")+"h.subproducto.id = :idSubproducto ";
		}
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getApePat() != null ) {
			//where += (where.length()==0? "" : " AND ")+"h.clienteNatural.apePat = '"+h.getClienteNatural().getApePat()+"'";
			where += (where.length()==0? "" : " AND ")+"h.clienteNatural.apePat = :apePat ";
		}
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getApeMat() != null ) {
			//where += (where.length()==0? "" : " AND ")+"h.clienteNatural.apeMat = '"+h.getClienteNatural().getApeMat()+"'";
			where += (where.length()==0? "" : " AND ")+"h.clienteNatural.apeMat = :apeMat ";
		}
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getNombre() != null ) {
			//where += (where.length()==0? "" : " AND ")+"h.clienteNatural.nombre = '"+h.getClienteNatural().getNombre()+"'";
			where += (where.length()==0? "" : " AND ")+"h.clienteNatural.nombre = :nombre ";
		}		
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getNumDoi() != null ) {
			//where += (where.length()==0? "" : " AND ")+"h.clienteNatural.numDoi = '"+h.getClienteNatural().getNumDoi()+"'";
			where += (where.length()==0? "" : " AND ")+"h.clienteNatural.numDoi = :numDoi ";
		}		
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getTipoDoi() != null && h.getClienteNatural().getTipoDoi().getId() > 0 ) {
			//where += (where.length()==0? "" : " AND ")+"h.clienteNatural.tipoDoi.id = "+h.getClienteNatural().getTipoDoi().getId();
			where += (where.length()==0? "" : " AND ")+"h.clienteNatural.tipoDoi.id = :idTipoDoi ";
		}
		
		if ( h.getEstado()!=null && h.getEstado().getId() > 0 ) {
			//where += (where.length()==0? "" : " AND ")+"h.estado.id = "+h.getEstado().getId();
			where += (where.length()==0? "" : " AND ")+" h.estado.id = :idestado ";
		}		
		
		if ( fechaInicio!=null ) {
			//where += (where.length()==0? "" : " AND ")+"h.fecRegistro >= " + fechaInicio;			
			where += (where.length()==0? "" : " AND ")+"h.fecRegistro >= :fechaInicio ";
		}
		if ( fechaFin!=null ) {			
			//where += (where.length()==0? "" : " AND ")+"h.fecRegistro <= " + fechaFin;
			where += (where.length()==0? "" : " AND ")+"h.fecRegistro <= :fechaFin ";
		}
		
		if(list!=null && list.size()>0){
			where += (where.length()==0? "" : " AND ")+" h.estado.id in (:listIdsEstados) ";
		}
		
		String query = "SELECT h FROM Historial h "+(where.length()==0? "" : (" WHERE "+where)+ " ORDER BY h.expediente.id DESC");
		
		LOG.info("query : "+query);
		
		//List<Historial> resultList = em.createQuery(query).getResultList();
				
		Query sql = (Query) em.createQuery(query);
		
		if ( h.getExpediente()!=null && h.getExpediente().getId() > 0 ) {
			sql.setParameter("idExpediente", h.getExpediente().getId());
			LOG.info("id exp "+h.getExpediente().getId());
		}
		
		if ( h.getProducto()!=null && h.getProducto().getId() > 0 ) {
			sql.setParameter("idProducto", h.getProducto().getId());
			LOG.info("id prod "+h.getProducto().getId());
		}
		
		if ( h.getSubproducto()!=null && h.getSubproducto().getId() > 0 ) {
			sql.setParameter("idSubproducto", h.getSubproducto().getId());
			LOG.info("id subProd "+h.getSubproducto().getId());
		}
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getApePat() != null ) {
			sql.setParameter("apePat", h.getClienteNatural().getApePat());
			LOG.info("apePat "+h.getClienteNatural().getApePat());
		}
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getApeMat() != null ) {
			sql.setParameter("apeMat", h.getClienteNatural().getApeMat());
			LOG.info("apeMat "+h.getClienteNatural().getApeMat());
		}
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getNombre() != null ) {
			sql.setParameter("nombre", h.getClienteNatural().getNombre());
			LOG.info("nombre "+h.getClienteNatural().getNombre());
		}		
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getNumDoi() != null ) {
			sql.setParameter("numDoi", h.getClienteNatural().getNumDoi());
			LOG.info("numDoi "+h.getClienteNatural().getNumDoi());
		}		
		
		if ( h.getClienteNatural()!=null && h.getClienteNatural().getTipoDoi() != null && h.getClienteNatural().getTipoDoi().getId() > 0 ) {
			sql.setParameter("idTipoDoi", h.getClienteNatural().getTipoDoi().getId());
			LOG.info("idTipoDoi "+h.getClienteNatural().getTipoDoi().getId());
		}
		
		if ( h.getEstado()!=null && h.getEstado().getId() > 0 ) {
			sql.setParameter("idestado", h.getEstado().getId());
			LOG.info("idestado "+h.getEstado().getId());
		}		
		
		if ( fechaInicio!=null ) {
			sql.setParameter("fechaInicio", new Timestamp (fechaInicio.getTime()));
			LOG.info("fechaInicio "+new Timestamp (fechaInicio.getTime()));
		}
		if ( fechaFin!=null ) {			
			sql.setParameter("fechaFin", new Timestamp (fechaFin.getTime()));
			LOG.info("fechaFin "+new Timestamp (fechaFin.getTime()));
		}
		if(list!=null && list.size()>0){
			sql.setParameter("listIdsEstados", list);
			//LOG.info("listIdsEstados "+new Timestamp (fechaFin.getTime()));
			
		}		
		
		
		LOG.info("sql : "+sql);
		
		List<Historial> resultList = sql.getResultList();
		
		//LOG.info("fechaInicio : "+ new Timestamp (fechaInicio.getTime()));
		
		return resultList;
	}

	@Override
	public Historial create(Historial entity) {
		return super.create(entity);
	}
	
	@Override
	public List<Historial> buscarXCriterioExpedienteYTarea(Long idExpediente, Long idtarea) {

		String query = "SELECT h FROM Historial h  WHERE h.expediente.id="+idExpediente+" and h.tarea.id="+idtarea+" ORDER BY h.expediente.id DESC";
		
		//LOG.info("query : "+query);
		
		List<Historial> resultList = em.createQuery(query).getResultList();
		
		return resultList;
	}
	
	@Override
	public List<Historial> buscarXCriterioExpedienteYTarea_TareaSiete(Long idExpediente, Long idtarea1, Long idtarea2) {

		String query = "SELECT h FROM Historial h  WHERE h.expediente.id="+idExpediente+" and (h.tarea.id="+idtarea1+" or h.tarea.id="+idtarea2+" ) ORDER BY h.id DESC";
		
		LOG.info("query buscarXCriterioExpedienteYTarea_TareaSiete::::"+query);
		
		List<Historial> resultList = em.createQuery(query).getResultList();
		
		return resultList;
	}	
	
	@Override
	public List<Historial> buscarXCriterioExpedienteYPerfilEjecutivo(Long idExpediente, Long idPerfil) {
		String idActivo="1";
		
		try{
			String query = "SELECT h FROM Historial h  WHERE h.expediente.id=:idExpediente and h.empleado.perfil.id=:idPerfil and h.empleado.flagActivo like :idActivo ORDER BY h.id DESC";
			List<Historial> resultList = em.createQuery(query)
					.setParameter("idExpediente", idExpediente)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;
			
		}catch(NoResultException e){
			return null;
		}
		
		
		//LOG.info("query : "+query);
		

	}	
	@Override 
	public List<Historial> buscarXCriterioExpedienteXPerfil(Long idExpediente, Long idPerfil){
		String idActivo="1";
		try{
			String query="SELECT h FROM Historial h WHERE h.empleado.flagActivo=:idActivo "+
						"AND h.fecRegistro=(SELECT max(h1.fecRegistro) FROM Historial h1 WHERE h1.expediente.id=:idExpediente AND h1.perfil.id=:idPerfil)";
			List<Historial> resultList=em.createQuery(query)
					.setParameter("idExpediente",idExpediente)
					.setParameter("idPerfil",idPerfil)
					.setParameter("idActivo",idActivo)
					.getResultList();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public List<Historial> buscarXCriterioProductoTerritorioPerfil(Long idProducto, Long idTerritorio, Long idPerfil, String flagActivo) {
		String query = "SELECT h FROM Historial h  WHERE h.producto.id=:idProducto and h.empleado.perfil.id=:idPerfil " +
				" and h.empleado.oficina.territorio.id=:idTerritorio and  h.empleado.flagActivo=:flagActivo";
		
		LOG.info("query : "+query);
		try{
			List<Historial> resultList = em.createQuery(query)
					.setParameter("idProducto", idProducto)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idTerritorio", idTerritorio)
					.setParameter("flagActivo", flagActivo)
					.getResultList();				
			
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<Historial> buscarPorIdExpRetraer(long idExpediente, String flagRetaer) {
		List<Historial> resultList = em.createNamedQuery("Historial.findbyIdExpFlagRetraer").setParameter("idExpediente", idExpediente).setParameter("flagRetaer", flagRetaer).getResultList();
		return resultList;
	}	
	
	
	@Override 
	public List<Historial> buscarPerfilRecientePorId(long idExpediente){
		String query="SELECT h FROM Historial h "+
					 "WHERE h.expediente.id= :idExpediente "+
					 "AND h.fecRegistro=(SELECT max(h1.fecRegistro) from Historial h1 where h1.expediente.id=:idExpediente)";
		LOG.info("query = "+query);
		try{
			List<Historial> resultList = em.createQuery(query)
					.setParameter("idExpediente", idExpediente)
					.getResultList();
			return resultList;
		}catch (NoResultException e) {
			return null;
		}
	}
	
	@Override 
	public Historial buscarMasRecienPorId(long idExpediente){
		String query="SELECT h FROM Historial h "+
					 "WHERE h.expediente.id= :idExpediente "+
					 "AND h.fecRegistro=(SELECT max(h1.fecRegistro) from Historial h1 where h1.expediente.id=:idExpediente)";
		LOG.info("query = "+query);
		try{
			Historial resultList = (Historial)em.createQuery(query)
					.setParameter("idExpediente", idExpediente)
					.getSingleResult();
			return resultList;
		}catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public int obtenerNumeroRegistradosXTemporalidad(long idEmpleado, long idOficina, Date fechaHoraInicio, Date fechaHoraFin) 
	{
		
		StringBuilder sbQuery = new StringBuilder(" SELECT COUNT(h) FROM Historial h ");
		sbQuery.append(" INNER JOIN h.empleado e ");
		sbQuery.append(" INNER JOIN h.oficina o ");
		sbQuery.append(" LEFT JOIN h.tarea t ");
		sbQuery.append(" WHERE t.id IS NULL AND ");
		sbQuery.append(" e.id = :empleado AND ");
		sbQuery.append(" o.id = :oficina AND ");
		sbQuery.append(" h.fecRegistro BETWEEN :fechaHoraInicio AND :fechaHoraFin ");
	
		try{
			
			Query jpql = em.createQuery(sbQuery.toString());
			jpql.setParameter("empleado", idEmpleado);
			jpql.setParameter("oficina", idOficina);
			jpql.setParameter("fechaHoraInicio", fechaHoraInicio, TemporalType.TIMESTAMP);
			jpql.setParameter("fechaHoraFin", fechaHoraFin, TemporalType.TIMESTAMP);
			
			return Integer.parseInt(jpql.getSingleResult().toString());	
								
		}catch (NoResultException e) {
			return 0;
		}	

	}
		@Override 
	public List<Historial> buscarultimoPorId(long idExpediente){
		String query="SELECT h FROM Historial h "+
					 "WHERE h.expediente.id= :idExpediente "+
					 "ORDER BY h.id desc";
		LOG.info("query = "+query);
		try{
			List<Historial> resultList = em.createQuery(query)
					.setParameter("idExpediente", idExpediente)
					.getResultList();
			return resultList;
		}catch (NoResultException e) {
			return null;
		}
	}
	
	
	
	
	
}