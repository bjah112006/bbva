package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DatosCorreo;
import com.ibm.bbva.ctacte.bean.DatosCorreoXPerfil;
import com.ibm.bbva.ctacte.dao.DatosCorreoDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class DatosCorreoDAOImpl
 */
@Stateless
@Local(DatosCorreoDAO.class)
public class DatosCorreoDAOImpl extends GenericDAO<DatosCorreo, Integer> implements DatosCorreoDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosCorreoDAOImpl.class);
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public DatosCorreoDAOImpl() {
    	super(DatosCorreo.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<DatosCorreo> buscarTodos() {
		Query query = em.createQuery("select o from DatosCorreo o order by o.accion.tarea.descripcion ASC, o.accion.descripcion ASC");
		List<DatosCorreo> list = new ArrayList<DatosCorreo>(query.getResultList());
		return list;
	}

	@Override
	public List<DatosCorreo> buscarPorTarea(Integer idTarea) {
		Query query = em.createQuery("select o from DatosCorreo o where o.accion.tarea.id = :idTarea order by o.accion.tarea.descripcion ASC, o.accion.descripcion ASC");
		query.setParameter("idTarea", idTarea);
		List<DatosCorreo> list = new ArrayList<DatosCorreo>(query.getResultList());
		return list;
	}

	@Override
	public List<DatosCorreo> buscarPorTareas(List<Integer> idTareas) {
		Query query = em.createQuery("select o from DatosCorreo o where o.accion.tarea.id in (:idTareas) order by o.accion.tarea.descripcion ASC, o.accion.descripcion ASC");
		query.setParameter("idTareas", idTareas);
		List<DatosCorreo> list = new ArrayList<DatosCorreo>(query.getResultList());
		return list;
	}

	@Override
	public DatosCorreo buscarPorIdTareaYNombreAccion(Integer idTarea,
			String nombreAccion) {
		Query query = em.createQuery("select o from DatosCorreo o where o.accion.tarea.id = :idTarea and o.accion.nombre = :nombreAccion");
		query.setParameter("idTarea", idTarea);
		query.setParameter("nombreAccion", nombreAccion);
		List<DatosCorreo> list = new ArrayList<DatosCorreo>(query.getResultList());
		if (list.size() > 0) {
			if (list.size() > 1) {
				LOG.warn("Existe más de una acción con el mismo nombre para la misma tarea.");
			}
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void eliminar(Integer id) {
		DatosCorreo obj = em.find(DatosCorreo.class, id);
		if (obj.getDatosCorreoXPerfil() != null && !obj.getDatosCorreoXPerfil().isEmpty()) {
			for (DatosCorreoXPerfil destinatario : obj.getDatosCorreoXPerfil()) {
				em.remove(destinatario);
			}
		}
		obj.setDatosCorreoXPerfil(new ArrayList<DatosCorreoXPerfil>());
		em.remove(obj);
	}

}
