package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.PerfilXNivel;
import com.ibm.bbva.ctacte.bean.PerfilXNivelPK;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;

/**
 * Session Bean implementation class PerfilXNivelDAOImpl
 */
@Stateless
@Local(PerfilXNivelDAO.class)
public class PerfilXNivelDAOImpl extends GenericDAO<PerfilXNivel, PerfilXNivelPK> implements PerfilXNivelDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(PerfilXNivelDAOImpl.class);
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public PerfilXNivelDAOImpl() {
    	super(PerfilXNivel.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<PerfilXNivel> search(PerfilXNivel perfilXNivel) {
		StringBuilder sb = new StringBuilder();
		sb.append("select o from PerfilXNivel o where 1=1");
		if (perfilXNivel != null) {
			if (perfilXNivel.getId() != null && perfilXNivel.getId().getTipoNivel() > 0) {
				sb.append(" and o.id.tipoNivel = :tipoNivel");
			}
			if (perfilXNivel.getId() != null && perfilXNivel.getId().getValor() != null && !perfilXNivel.getId().getValor().trim().equals("")) {
				sb.append(" and o.id.valor = :valor");
			}
			if (perfilXNivel.getFlagActivo() != null && !perfilXNivel.getFlagActivo().trim().equals("")) {
				sb.append(" and o.flagActivo = :flagActivo");
			}
			if (perfilXNivel.getPerfil() != null && perfilXNivel.getPerfil().getId() != null && perfilXNivel.getPerfil().getId().intValue() > 0) {
				sb.append(" and o.perfil.id = :idPerfil");
			}
		}
		sb.append(" order by o.id.tipoNivel ASC, o.id.valor ASC");
		LOG.info("query-->"+sb.toString());
		Query query = em.createQuery(sb.toString());
		if (perfilXNivel != null) {
			if (perfilXNivel.getId() != null && perfilXNivel.getId().getTipoNivel() > 0) {
				query.setParameter("tipoNivel", perfilXNivel.getId().getTipoNivel());
			}
			if (perfilXNivel.getId() != null && perfilXNivel.getId().getValor() != null && !perfilXNivel.getId().getValor().trim().equals("")) {
				query.setParameter("valor", perfilXNivel.getId().getValor());
			}
			if (perfilXNivel.getFlagActivo() != null && !perfilXNivel.getFlagActivo().trim().equals("")) {
				query.setParameter("flagActivo", perfilXNivel.getFlagActivo());
			}
			if (perfilXNivel.getPerfil() != null && perfilXNivel.getPerfil().getId() != null && perfilXNivel.getPerfil().getId().intValue() > 0) {
				query.setParameter("idPerfil", perfilXNivel.getPerfil().getId());
			}
		}
		
		List<PerfilXNivel> resultado = query.getResultList();
		
		return resultado;
	}

	@Override
	public void actualizarPerfilEmpleados(PerfilXNivel perfilXNivel, boolean eliminado) throws ParametroIlegalException {
		long tipoNivel = perfilXNivel.getId().getTipoNivel();
		String queryString;
		if (tipoNivel == 3) { // Oficina
			queryString = "select e from Empleado e where e.oficina.codigo = :valor and (e.codcargo is null or e.codcargo not in (select o.id.valor from PerfilXNivel o where o.id.tipoNivel = 2 and o.flagActivo = '1')) and e.codigo not in (select o.id.valor from PerfilXNivel o where o.id.tipoNivel = 1 and o.flagActivo = '1') and e.flagActivo = '1'";
		} else if (tipoNivel == 2) { // Puesto
			queryString = "select e from Empleado e where e.codcargo = :valor and e.codigo not in (select o.id.valor from PerfilXNivel o where o.id.tipoNivel = 1 and o.flagActivo = '1') and e.flagActivo = '1'";
		} else if (tipoNivel == 1) { // Usuario
			queryString = "select e from Empleado e where e.codigo = :valor and e.flagActivo = '1'";
		} else {
			throw new ParametroIlegalException("Tipo de nivel "+tipoNivel+" no existe");
		}
		Query query = em.createQuery(queryString);
		query.setParameter("valor", perfilXNivel.getId().getValor());
		
		List<Empleado> lstEmpleados = query.getResultList();
		LOG.info("Número de Empleados afectados: "+lstEmpleados.size());
		for (Empleado empleado : lstEmpleados) {
			Perfil perfil;
			if (perfilXNivel.getFlagActivo().equals("1") && !eliminado) { // Activo
				perfil = perfilXNivel.getPerfil();
			} else { // Inactivo o eliminado
				perfil = obtenerPerfilAsignado(empleado.getCodigo(), empleado.getCodcargo(), empleado.getOficina().getCodigo());
			}
			empleado.setPerfil(perfil);
			getEntityManager().merge(empleado);
			LOG.info("Empleado (id="+empleado.getId()+", codigo="+empleado.getCodigo()+") cambia a perfil (id="+perfil.getId()+", desc="+perfil.getDescripcion()+")");
		}
	}

	@Override
	public Perfil obtenerPerfilAsignado(String codigoEmpleado, String codcargo,
			String codigoOficina) {
		Query query = em.createQuery("select o from PerfilXNivel o where o.flagActivo = '1' and ((o.id.valor = :codigoEmpleado and o.id.tipoNivel = 1) or (o.id.valor = :codcargo and o.id.tipoNivel = 2) or (o.id.valor = :codigoOficina and o.id.tipoNivel = 3)) order by o.id.tipoNivel asc");
		query.setParameter("codigoEmpleado", codigoEmpleado);
		query.setParameter("codcargo", codcargo);
		query.setParameter("codigoOficina", codigoOficina);
		
		List<PerfilXNivel> lstPerfilXNivel= query.getResultList();
		if (lstPerfilXNivel.size() > 0) {
			return lstPerfilXNivel.get(0).getPerfil();
		} else { // Perfil por defecto si no hay nada configurado
			return em.find(Perfil.class, new Integer(Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_CONSULTA)));
		}
	}

}
