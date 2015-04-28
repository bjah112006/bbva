package com.ibm.bbva.ctacte.dao.impl;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class EmpleadoBean
 */
@Stateless
@Local(EmpleadoDAO.class)
public class EmpleadoDAOImpl extends GenericDAO<Empleado, Integer> implements EmpleadoDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmpleadoDAOImpl.class);


	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public EmpleadoDAOImpl() {
		super(Empleado.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public List<Empleado> findAll() {
		Query query = em.createQuery(
 			"select o from Empleado o where o.flagActivo = :flagActivo");
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getEmpleados(String tipoEmpleado) {
		Query query = em.createQuery(
 			"select o from Empleado o where o.flagAbogado = :tipEmpl and o.flagActivo = :flagActivo");
		query.setParameter("tipEmpl", tipoEmpleado);
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getAbogadosBastanteo(String tipoEmpleado) {
		Query query= em.createQuery("select o from Empleado o where o.flagAbogado = :tipEmpl and o.perfil.id=18 and o.flagActivo = :flagActivo");
		query.setParameter("tipEmpl", tipoEmpleado);
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados=query.getResultList();
		return empleados;
	}

	@Override
	public Empleado findByCodigo(String codigo) {
		try {
			return (Empleado) em
					.createQuery(
							"select o from Empleado o where o.codigo = :cod")
					.setParameter("cod", codigo).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Empleado> findByEstudio(Integer idEstudioAbog) {
		Query query = em.createQuery(
 			"select o from Empleado o where o.flagAbogado = '1' and o.estudio.id = :idEstudioAbog and o.flagActivo = :flagActivo");
		query.setParameter("idEstudioAbog", idEstudioAbog);
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getEmpleadosPorPerfil(int idPerfil) {
		Query query = em.createQuery(
 			"select o from Empleado o where o.perfil.id = :idPerfil and o.flagActivo = :flagActivo");
		query.setParameter("idPerfil", idPerfil);
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getPosiblesOwners(String codUsuario) {
		Query query = em.createQuery("select o from Empleado o" +
				  " where o.perfil.id in (select e.perfil.id from Empleado e where e.codigo like :codUsuario)" +
				  " and o.codigo not like :codUsuario and o.flagActivo = '1'"+
				  " and o.oficina.id in (select a.oficina.id from Empleado a where a.codigo like :codUsuario)");
		query.setParameter("codUsuario", codUsuario);
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getEmpleadosCarterizacion(int idProducto,
			int idTerritorio, int idPerfil) {
		Query query = em.createQuery("select o from Empleado o where o.perfil.id=:idPerfil " +
				"and o.id in (select c.id.idEmpleadoFk from Carterizacion c where " +
				"c.id.idTerritorioFk=:idTerritorio and c.id.idProductoFk=:idProducto) and o.flagActivo = :flagActivo");
		query.setParameter("idPerfil", idPerfil);
		query.setParameter("idTerritorio", idTerritorio);
		query.setParameter("idProducto", idProducto);
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getPosiblesNombres(String nombreCompleto) {
		String strSQL=" select o from Empleado o " +
        " where UPPER(CONCAT(o.nombres,CONCAT(' ',CONCAT(o.apepat,CONCAT(' ',o.apemat))))) like :p_nombresCompletos";
		
		Query query = em.createQuery(strSQL);
		query.setParameter("p_nombresCompletos", nombreCompleto);
		
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getEmpleadosPorPerfilOficina(int idPerfil,
			int idOficina) {
		Query query = em.createQuery(
 			"select o from Empleado o where o.perfil.id = :idPerfil and o.flagActivo = :flagActivo and o.oficina.id = :idOficina" );
		query.setParameter("idPerfil", idPerfil);
		query.setParameter("flagActivo", "1");
		query.setParameter("idOficina", idOficina);
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getEmpleadosPorPerfiles(List<Integer> idPerfiles) {
		Query query = em.createQuery(
 			"select o from Empleado o where o.perfil.id in (:idPerfiles) and o.flagActivo = :flagActivo");
		query.setParameter("idPerfiles", idPerfiles);
		query.setParameter("flagActivo", "1");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getEmpleadosPorPerfilesYEstudio(
			List<Integer> idPerfiles, Integer idEstudioAbog) {
		Query query = em.createQuery(
 			"select o from Empleado o where o.perfil.id in (:idPerfiles) and o.flagActivo = :flagActivo and o.estudio.id = :idEstudioAbog");
		query.setParameter("idPerfiles", idPerfiles);
		query.setParameter("flagActivo", "1");
		query.setParameter("idEstudioAbog", idEstudioAbog);
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getPosiblesOwnersPorEstudio(String codUsuario,
			Integer idEstudioAbog) {
		Query query = em.createQuery("select o from Empleado o" +
				  " where o.perfil.id in (select e.perfil.id from Empleado e where e.codigo like :codUsuario)" +
				  " and o.codigo not like :codUsuario and o.flagActivo = '1'"+
				  " and o.oficina.id in (select a.oficina.id from Empleado a where a.codigo like :codUsuario)"+
				  " and o.estudio.id = :idEstudioAbog");
		query.setParameter("codUsuario", codUsuario);
		query.setParameter("idEstudioAbog", idEstudioAbog);
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}

	@Override
	public List<Empleado> getAllNotInLDAP() {
		Query query = em.createQuery("select o from Empleado o where o.codigo NOT IN ( SELECT lu.codusu from LDAPUsuario lu)");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}
	
	@Override
	public List<Empleado> getAllInLDAP() {
		Query query = em.createQuery("select o from Empleado o where o.codigo IN ( SELECT lu.codusu from LDAPUsuario lu)");
		List<Empleado> empleados = query.getResultList();
		return empleados;
	}
	
	/**
	 * Buscar un empleado por diferentes parámetros
	 * contenidos en un objeto Empleado 
	 * 
	 * @return
	 */
	@Override
	public List<Empleado> search(Empleado empleado){
		
		String queryString = "SELECT e FROM Empleado e WHERE ";
		
		ArrayList<String> conditions = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		int index = 1;
		
		// PERFIL
		
		if(empleado.getPerfil() != null){
			conditions.add(" e.perfil.id = ?" + index++ + " ");
			values.add(empleado.getPerfil().getId());
		}
				
		// OFICINA
		
		if(empleado.getOficina() != null){
			conditions.add(" e.oficina.id = ?" + index++ + " ");
			values.add(empleado.getOficina().getId());
		}
		
		// ESTUDIO ABOGADO
		
		if(empleado.getEstudio() != null){
			conditions.add(" e.estudio.id = ?" + index++ + " ");
			values.add(empleado.getEstudio().getId());
		}
		
		// CODIGO EMPLEADO
		
		if(empleado.getCodigo() != null){
			if(empleado.getCodigo().trim().length() > 0){
				conditions.add(" e.codigo = ?" + index++ + " ");
				values.add(empleado.getCodigo());
			}
		}
		
		// FLAG ACTIVO
		
		if(empleado.getFlagActivo() != null){
			conditions.add(" e.flagActivo = ?" + index++ + " ");
			
			if(empleado.getFlagActivo() == "true")
				values.add("1");
			else
				values.add("0");
		}
		
		queryString += join(conditions,"AND");
		
		LOG.debug("search() - query : " + queryString);
		
		Query query = em.createQuery(queryString);
		
		for(int i=0;i<values.size();i++){
			LOG.debug("search() - values " + i + " : " + values.get(i));
			query.setParameter(i + 1, values.get(i));
		}
		
		List<Empleado> empleados = query.getResultList();
		return empleados;
		
	}
	
	
	private String join(AbstractCollection<String> s, String delimiter)
	  {
	    if (s == null || s.isEmpty()) return "";
	    Iterator<String> iter = s.iterator();
	    StringBuilder builder = new StringBuilder(iter.next());
	    while( iter.hasNext() ) {
	      builder.append(delimiter).append(iter.next());
	    }
	    return builder.toString();
	  }

	
}
