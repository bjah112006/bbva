package com.ibm.bbva.session.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.tivoli.pd.jadmin.util.e;

/**
 * Session Bean implementation class EmpleadoBean
 */
@Stateless
public class EmpleadoBean extends AbstractFacade<Empleado> implements EmpleadoBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmpleadoBean.class);
	
	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public EmpleadoBean() {
    	super(Empleado.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Empleado buscarPorCodigo(String codigo) {
		
		try{
			return (Empleado) em.createNamedQuery("Empleado.findByCodigo").setParameter("codigo", codigo).getSingleResult();		
		}catch(NoResultException e){
			return null;
		}
		
	}

	@Override
	public Empleado buscarPorId(long id) {
		String idActivo="1";
		String query="SELECT e FROM Empleado e WHERE e.id = :id and e.flagActivo like :idActivo";
		LOG.info("query = "+query);
		try{
			Empleado resultList = (Empleado) em.createQuery(query)
					.setParameter("id", id)
					.setParameter("idActivo", idActivo)
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public Empleado obtenerPorId(long id) {		
		String query="SELECT e FROM Empleado e WHERE e.id = :id";
		LOG.info("query = "+query);
		try{
			Empleado resultList = (Empleado) em.createQuery(query)
					.setParameter("id", id)					
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}		
	}

	@Override
	public List<Empleado> buscarPorIdTipoCategoria(long idTipoCategoria, long idPerfil) {
		List<Empleado> resultList = em.createNamedQuery("Empleado.findByIdTipoCategoria")
				.setParameter("idTipoCategoria", idTipoCategoria)
				.setParameter("idPerfil", idPerfil)
				.getResultList();
		return resultList;
	}
	
	@Override
	public List<Empleado> buscarPorIdPerfil(long idPerfil) {
		List<Empleado> resultList = em.createNamedQuery("Empleado.findByIdPerfil").setParameter("idPerfil", idPerfil).getResultList();
		return resultList;
	}	
	
	
	/*
	@Override
	public Empleado calculoCarga(long idTerritorio,long idProducto, String flagActivo){	
		
		String query="   SELECT EMPL.ID " + 
				" FROM Expediente E INNER JOIN E.expedienteTC T ON E.ID=T.idExpFk " + 
				" inner join E.empleado EMPL on EMPL.ID=E.empleado.ID " +
				" WHERE EMPL.ID IN (SELECT CT.empleado.id FROM Carterizacion CT WHERE " +
				" ct.producto.id="+idProducto+" AND ct.territorio.id="+idTerritorio+") AND "+
				" EMPL.flagActivo='"+flagActivo.trim()+"'" +		
				" GROUP BY EMPL.ID" + 
				" ORDER BY (NVL(SUM((SELECT P.PESO FROM Producto P WHERE P.ID=E.Producto.ID)*" +
				" (SELECT  SUM("+
				" CASE WHEN"+
				" DECODE(T.lineaCredAprob,NULL,T.lineaCredSol,T.lineaCredAprob)>= M.montoMinimo AND" +   
				" DECODE(T.lineaCredAprob,NULL,T.lineaCredSol,T.lineaCredAprob)<= M.montoMaximo" + 
				" AND M.tipoMoneda.id= 1" + 
				" THEN M.PESO " +
				" ELSE 0 END )" +  
				" FROM MontoPeso M )),0)) ASC";
		
		try{
			List<Empleado> listResult=(List<Empleado>)em.createQuery(query).getResultList();
			return listResult.get(0);			
		}catch(NoResultException e){
			return null;
		}

	}
	*/

	@Override
	public Empleado calculaPeso(long idTerritorio,long idProducto, String flagActivo, Long idTarea) {
/*
		String query="   SELECT EMPL.ID " + 
				" FROM Expediente E INNER JOIN E.expedienteTC T ON E.ID=T.idExpFk " + 
				" inner join E.empleado EMPL on EMPL.ID=E.empleado.ID " +
				" WHERE EMPL.ID IN (SELECT CT.empleado.id FROM Carterizacion CT WHERE " +
				" ct.producto.id="+idProducto+" AND ct.territorio.id="+idTerritorio+" and ct.empleado.perfil.id in (" +
				" SELECT tp.perfil.id FROM TareaPerfil tp WHERE tp.tarea.id = "+idTarea+" )) AND "+
				" EMPL.flagActivo='"+flagActivo.trim()+"'" +		
				" GROUP BY EMPL.ID" + 
				" ORDER BY (NVL(SUM((SELECT P.PESO FROM Producto P WHERE P.ID=E.Producto.ID)*" +
				" (SELECT  SUM("+
				" CASE WHEN"+
				" DECODE(T.lineaCredAprob,NULL,T.lineaCredSol,T.lineaCredAprob)>= M.montoMinimo AND" +   
				" DECODE(T.lineaCredAprob,NULL,T.lineaCredSol,T.lineaCredAprob)<= M.montoMaximo" + 
				" AND M.tipoMoneda.id= 1" + 
				" THEN M.PESO " +
				" ELSE 0 END )" +  
				" FROM MontoPeso M )),0)) ASC";	
		*/
		String query="   SELECT EMPL.ID " + 
				" FROM Expediente E INNER JOIN E.expedienteTC T " + 
				" inner join E.empleado EMPL " +
				" WHERE E.ID=T.idExpFk and EMPL.ID=E.empleado.ID and EMPL.ID IN (SELECT CT.empleado.id FROM Carterizacion CT WHERE " +
				" ct.producto.id="+idProducto+" AND ct.territorio.id="+idTerritorio+" and ct.empleado.perfil.id in (" +
				" SELECT tp.perfil.id FROM TareaPerfil tp WHERE tp.tarea.id = "+idTarea+" )) AND "+
				" EMPL.flagActivo='"+flagActivo.trim()+"'" +		
				" GROUP BY EMPL.ID" + 
				" ORDER BY (NVL(SUM((SELECT P.PESO FROM Producto P WHERE P.ID=E.Producto.ID)*" +
				" (SELECT  SUM("+
				" CASE WHEN"+
				" DECODE(T.lineaCredAprob,NULL,T.lineaCredSol,T.lineaCredAprob)>= M.montoMinimo AND" +   
				" DECODE(T.lineaCredAprob,NULL,T.lineaCredSol,T.lineaCredAprob)<= M.montoMaximo" + 
				" AND M.tipoMoneda.id= 1" + 
				" THEN M.PESO " +
				" ELSE 0 END )" +  
				" FROM MontoPeso M )),0)) ASC";	
		
		try {
			List<Empleado> listResult=(List<Empleado>)em.createQuery(query).getResultList();
			return listResult.get(0);	
		}
		catch (NoResultException e) {
			return null;
		}
		
	}
	
	@Override
	public List<Empleado> buscarPorIdOficina(long idOficina) {
		List<Empleado> resultList = em.createNamedQuery("Empleado.findByIdOficina").setParameter("idOficina", idOficina).getResultList();
		return resultList;
	}
	
	@Override
	public List<Empleado> buscarPorOficinaPerfil(long idOficina, long idPerfil) {
		
		String query="SELECT e FROM Empleado e WHERE e.oficina.id = :idOficina and e.perfil.id = :idPerfil and e.flagActivo like :flagActivo" +
				" Order by e.apepat, e.apemat, e.nombres";
		
		try{
			List<Empleado> resultList = em.createQuery(query)
					.setParameter("idOficina", idOficina)
					.setParameter("idPerfil", idPerfil)
					.setParameter("flagActivo", "1")
					.getResultList();
			return resultList;
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<Empleado> buscarPorPerfilEmpleadoActivo(long idPerfil, long idOficina) {
		String idActivo="1";
		String query="SELECT e FROM Empleado e WHERE  e.perfil.id = :idPerfil and e.oficina.id =:idOficina and e.flagActivo like :idActivo";
		LOG.info("query = "+query);
		try{
			List<Empleado> resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idOficina", idOficina)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<Empleado> buscarGerenteActivoPorOficinaPerfil(long idOficina, long idPerfil) {
		String idActivo="1";
		String query="SELECT e FROM Empleado e WHERE e.oficina.id = :idOficina and " +
				" e.perfil.id = :idPerfil and  e.flagActivo like :idActivo";
		LOG.info("query GerenteActivo = "+query);
		try{
			List<Empleado> resultList = em.createQuery(query)
					.setParameter("idOficina", idOficina)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<Empleado> buscarPorPerfilActivo(long idPerfil) {
		String idActivo="1";
		String query="SELECT e FROM Empleado e WHERE  e.perfil.id = :idPerfil and e.flagActivo like :idActivo";
		LOG.info("query = "+query);
		try{
			List<Empleado> resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public Empleado create(Empleado entity){
		return super.create(entity);
	}

	@Override
	public void edit(Empleado entity){
		super.edit(entity);
	}
	
	@Override
	public Empleado buscarEmpCreadorPorIdExpediente(long idExpediente){
		
		String query="SELECT e FROM Empleado e WHERE e.id=(SELECT h.empleado.id from Historial h where h.expediente.id=:idExpediente and h.tarea.id is null)";
		
		try{
			Empleado resultList = (Empleado) em.createQuery(query)
					.setParameter("idExpediente", idExpediente)
					.getSingleResult();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public Empleado buscarPerfilxEmpleado(long idEmpleado){
		String query="SELECT e FROM Empleado e WHERE e.id=:idEmpleado ";
		try{
			Empleado resultList = (Empleado) em.createQuery(query)
					.setParameter("idEmpleado", idEmpleado)
					.getSingleResult();
			return resultList;
		}catch(NoResultException e){
			return null;
		}
		
	}
	
	@Override
	public long countEmpleadoCartActiv(long idEmpleado) {

		String query = "SELECT COUNT(DISTINCT d.empleado.id)"
				+ " FROM CartEmpleadoCE d " 
				+ " WHERE d.empleado.id IN ( "
				+ " SELECT e.id FROM Empleado e WHERE e.perfil.id in ( SELECT f.perfil.id FROM Empleado f WHERE f.id=" + idEmpleado + ") " 
				+ " AND e.oficina.id in ( SELECT f.oficina.id FROM Empleado f WHERE f.id=" + idEmpleado + ") " 				
				+ " AND e.id<>" + idEmpleado 
				+ " AND e.flagActivo='1') "
				+ " AND d.flagActivo='1'";
		try {
			long resultado = (Long)em.createQuery(query)
						 .getSingleResult();

			return resultado;
		} catch (NoResultException e) {
			LOG.info("e" + e.getMessage());
			return 0;
		}

	}
	
	/**
	 * Cambio 08 Mayo 2015 
	 * Carterización por Producto
	 * */	
	
	@Override
	public List<Empleado> buscarPorPerfil_CartProd(long idPerfil, List<Long> listIdsProd) {
		//String idActivo="1";
		
		String query="SELECT e FROM Empleado e " +
				"WHERE e.id in (SELECT v.idEmpleado FROM VistaBandjCartProd v " +
				"	WHERE v.idPerfil = :idPerfil " +
				"	AND v.idProducto in (:listIdsProd) " +
				"	GROUP BY v.idEmpleado)";
		
		LOG.info("query = "+query);
		try{
			List<Empleado> resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("listIdsProd", listIdsProd)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}
	
	@Override
	public List<Empleado> buscarPorPerfilEmpleadoActivo(long idPerfil, long idOficina, List<Long> listIdsProd) {
		//String idActivo="1";
		String query="SELECT e FROM Empleado e " +
				"WHERE e.id in (SELECT v.idEmpleado FROM VistaBandjCartProd v " +
				"	WHERE v.idPerfil = :idPerfil " +
				"	AND v.idOficina = :idOficina " +
				"	AND v.idProducto in (:listIdsProd) " +
				"	GROUP BY v.idEmpleado)";
		LOG.info("query = "+query);
		try{
			List<Empleado> resultList = em.createQuery(query)
					.setParameter("idPerfil", idPerfil)
					.setParameter("idOficina", idOficina)
					.setParameter("listIdsProd", listIdsProd)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}

	}

	@Override
	public void ejecutarDescargaLDAP(long idLogJob) {
		em.createNativeQuery("{call CONELE.PG_CE_PROCESO.SP_CARGAR(?) }").setParameter(1, idLogJob).executeUpdate();
	}

	@Override
	public List<Empleado> buscarDebenReasignarse() 
	{
		String idActivo = "1";
		int idPerfil = 6;
		
		StringBuilder sbQuery = new StringBuilder(" SELECT e FROM Empleado e WHERE ");
		sbQuery.append(" e.perfil.id = :idPerfil and ");
		sbQuery.append(" e.flagActivo = :idActivo and ");		
		sbQuery.append(" (e.codigoCargoAnterior IS NOT NULL OR e.oficinaAnterior IS NOT NULL) ");
		
		try{
			List<Empleado> resultList = em.createQuery(sbQuery.toString())
					.setParameter("idPerfil", idPerfil)
					.setParameter("idActivo", idActivo)
					.getResultList();
			return resultList;			
		}catch (NoResultException e) {
			return null;
		}
	}
	
	
	
	
}
