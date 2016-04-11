package com.ibm.bbva.ctacte.dao.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dto.ExpedienteDTO;
import com.ibm.bbva.ctacte.vo.ReporteVO;

/**
 * Session Bean implementation class ExpedienteBean
 */
@Stateless
@Local(ExpedienteDAO.class)
public class ExpedienteDAOImpl extends GenericDAO<Expediente, Integer> implements ExpedienteDAO {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteDAOImpl.class);
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public ExpedienteDAOImpl() {
        super(Expediente.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Expediente> findByCodigoCentral(String codigoCentral) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent");
		query.setParameter("codCent", codigoCentral);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralSinEstado(String codigoCentral,
			List<Integer> estados) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.estado.id not in (:listEst)");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("listEst", estados);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralConEstado(String codigoCentral,
			Integer idEstado) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.estado.id = :idEst");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("idEst", idEstado);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralConCodOperacion(
			String codigoCentral, String codOperacion) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.operacion.codigoOperacion=:codOper");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("codOper", codOperacion);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByCodigoCentralSinIdExpediente(
			String codigoCentral, Integer idExpediente) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and o.id <> :idExp");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("idExp", idExpediente);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public Expediente getExpediente(String codigoCentral, int idEstado,
			String codOperacion) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.cliente.codigoCentral=:codCent and " +
 			"o.estado.id=:idEst and o.operacion.codigoOperacion=:codOper");
		query.setParameter("codCent", codigoCentral);
		query.setParameter("idEst", idEstado);
		query.setParameter("codOper", codOperacion);
		//Expediente expediente = (Expediente) query.uniqueResult();
		Expediente expediente = null;
		List<Expediente> expedientes = query.getResultList();
		if (!expedientes.isEmpty()) {
			expediente = (Expediente) expedientes.get(0);
		}
		return expediente;
	}

	@Override
	public List<Expediente> findByEstado(String codUsuario, int idestado) {
		Query query = em.createQuery(
 			"select o from Expediente o where o.estado.id=:idestado and o.empleado.codigo=:codusuario");
		query.setParameter("idestado", idestado);
		query.setParameter("codusuario", codUsuario);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idestado-->"+idestado);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codUsuario-->"+codUsuario);
		List<Expediente> expedientes = query.getResultList();
		return expedientes;
	}

	@Override
	public List<Expediente> findByFiltrofromExpediente(String nroExp,
			String estadoExp, String codCentral, String idOperacion,
			String nroDoi, String razonSocial, String codGestor,
			String idTerritorio, String idOficina, String codResponsable,
			Date fechaInicioLimInf, Date fechaInicioLimSup) {
		/* Consulta registros de la tabla Expediente cuyos estados sea igual a Preregistro ()estadoExp = 1 )
		 * 
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("select o from Expediente o ");
		sb.append("where o.estado.id = 1");
		if (!nroExp.equalsIgnoreCase("0"))
			sb.append(" and o.id =:nroExp");
		if (codCentral != null && !codCentral.trim().equals(""))
			sb.append(" and o.cliente.codigoCentral =:codCentral");
		if (!idOperacion.equalsIgnoreCase("0"))
			sb.append(" and o.operacion.id =:idOperacion");
		if (nroDoi != null && !nroDoi.trim().equals(""))
			sb.append(" and o.cliente.numeroDoi =:nroDoi");
		if (razonSocial != null && !razonSocial.trim().equals(""))
			sb.append(" and upper(o.cliente.razonSocial) like :razonSocial");
		if (codGestor != null && !codGestor.trim().equals(""))
			sb.append(" and o.empleado.codigo =:codGestor");
		if (!idTerritorio.equalsIgnoreCase("0"))
			sb.append(" and o.oficina.territorio.id =:idTerritorio");
		if (!idOficina.equalsIgnoreCase("0"))
			sb.append(" and o.oficina.id =:idOficina");
		if (codResponsable != null && !codResponsable.trim().equals(""))
			sb.append(" and o.empleado.codigo =:codResponsable");
		if (fechaInicioLimInf != null && fechaInicioLimSup != null)
			sb.append(" and o.fechaRegistro between :fechaInicioLimInf and :fechaInicioLimSup");
		
		LOG.info("strSQLHist-->"+sb.toString());
		Query query = em.createQuery(sb.toString());
		if (!nroExp.equalsIgnoreCase("0"))
			query.setParameter("nroExp", Integer.parseInt(nroExp));
		if (codCentral != null && !codCentral.trim().equals(""))
			query.setParameter("codCentral", codCentral);
		if (!idOperacion.equalsIgnoreCase("0"))
			query.setParameter("idOperacion", Integer.parseInt(idOperacion));
		if (nroDoi != null && !nroDoi.trim().equals(""))
			query.setParameter("nroDoi", nroDoi);
		if (razonSocial != null && !razonSocial.trim().equals(""))
			query.setParameter("razonSocial", "%"+razonSocial.toUpperCase()+"%");
		if (codGestor != null && !codGestor.trim().equals(""))
			query.setParameter("codGestor", codGestor);
		if (!idTerritorio.equalsIgnoreCase("0"))
			query.setParameter("idTerritorio", Integer.parseInt(idTerritorio));
		if (!idOficina.equalsIgnoreCase("0"))
			query.setParameter("idOficina", Integer.parseInt(idOficina));
		if (codResponsable != null && !codResponsable.trim().equals(""))
			query.setParameter("codResponsable", codResponsable);
		if (fechaInicioLimInf != null && fechaInicioLimSup != null) {
			query.setParameter("fechaInicioLimInf", fechaInicioLimInf);
			query.setParameter("fechaInicioLimSup", fechaInicioLimSup);
		}
		
		List<Expediente> expedientes = query.getResultList();
		
		return expedientes;
	}

	@Override
	public List<ExpedienteDTO> getAllDataByFilterBD(ReporteVO vo, String inQuery) {
		
		List<Object[]> expedientes = new ArrayList<Object[]>();
		List<ExpedienteDTO> expedientesDTO = new ArrayList<ExpedienteDTO>();
		
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" SELECT EXP.ID , ");
		queryString.append(" CLI.CODIGO_CENTRAL , ");
		queryString.append(" O.CODIGO , ");
		queryString.append(" O.DESCRIPCION , ");
		queryString.append(" OPE.DESCRIPCION ,  ");
		queryString.append(" CN.DESCRIPCION , ");
		queryString.append(" EST.DESCRIPCION , ");
		queryString.append(" EXP.FECHA_REGISTRO , ");
		queryString.append(" T.CODIGO , ");
		queryString.append(" T.DESCRIPCION , ");
		queryString.append(" EMP.CODIGO , ");
		queryString.append(" EMP.NOMBRES_COMPLETOS, ");
		queryString.append(" CLI.RAZON_SOCIAL , ");
		queryString.append(" CLI.FLAG_ORIGEN_SFP , ");
		queryString.append(" EXP.COD_TIPO_PJ, ");
		queryString.append(" EXP.DES_TIPO_PJ, ");
		queryString.append(" EXP.FLAG_EXONERACION_COMISION, ");
		queryString.append(" EXP.DICTAMEN_VER_REL_BAST  ,  ");
		queryString.append(" EXP.RESULTADO_VER_REL_BAST  , ");
		queryString.append(" EXP.DT_ULT_FECH_MESA_DOC  , ");
		queryString.append(" EXP.DT_ULT_FECH_MESA_FIR  , ");
		queryString.append(" EXP.DT_ULT_FECH_REAL_BAST  , ");
		queryString.append(" EXP.DT_ULT_FECH_VER_RES_TRA  , ");
		queryString.append(" EXP.FLAG_OBS_MES_DOC  , ");
		queryString.append(" EXP.FLAG_OBS_MES_FIR ,   ");
		queryString.append(" EXP.FLAG_CIERRE_AUTOMATICO  ");
		queryString.append(" FROM TBL_CE_IBM_EXPEDIENTE_CC EXP  ");
		queryString.append(" INNER JOIN TBL_CE_IBM_OPERACION_CC OPE ON OPE.ID = EXP.ID_OPERACION_FK ");
		queryString.append(" INNER JOIN TBL_CE_IBM_CLIENTE_CC CLI ON CLI.ID = EXP.ID_CLIENTE_FK ");
		queryString.append(" INNER JOIN TBL_CE_IBM_EMPLEADO EMP ON EXP.ID_EMPLEADO_FK = EMP.ID ");
		queryString.append(" INNER JOIN TBL_CE_IBM_OFICINA O ON O.ID = EMP.ID_OFICINA_FK ");
		queryString.append(" INNER JOIN TBL_CE_IBM_TERRITORIO T  ON T.ID = O.ID_TERRITORIO_FK ");
		queryString.append(" INNER JOIN TBL_CE_IBM_ESTADO_EXP_CC EST ON EST.ID = EXP.ID_ESTADO_FK ");
		queryString.append(" LEFT JOIN TBL_CE_IBM_CASO_NEGOCIO_CC CN ON CN.ID = EXP.ID_CASO_NEGOCIO_FK  ");
		queryString.append(" WHERE 1 = 1 ");
		
		if(!vo.getCodigoCentral().isEmpty()){
			queryString.append(" AND UPPER(CLI.CODIGO_CENTRAL) = ?1 "); 
			
		}
		
		if(vo.getFechaInicio() != null && vo.getFechaFin() != null && vo.getFechaInicio().before(vo.getFechaFin() )){
			queryString.append(" AND  TO_DATE(TO_CHAR( EXP.FECHA_REGISTRO, 'DD-MM-YYYY'), 'DD-MM-YYYY')  >= ?9  AND  TO_DATE(TO_CHAR(EXP.FECHA_REGISTRO, 'DD-MM-YYYY'), 'DD-MM-YYYY') <= ?10 "); 	
		}
		
		if(!vo.getRazonSocial().isEmpty()){
			queryString.append(" AND UPPER(CLI.RAZON_SOCIAL) LIKE ?2 "); 
//			queryString.append(" AND UPPER(CLI.RAZON_SOCIAL) = :pRazonSocial "); 
		}
		
		if(!vo.getNumeroExpediente().isEmpty()){
			queryString.append(" AND to_char(EXP.ID) = ?3 "); 
//			queryString.append(" AND to_char(EXP.ID) = :pNumeroExpediente "); 
		}
		
		if(!vo.getResponsable().isEmpty()){
			queryString.append(" AND UPPER(EMP.CODIGO) = ?4 "); 
//			queryString.append(" AND UPPER(EMP.CODIGO) = :pResponsable "); 
		}
			
		if(!(""+vo.getIdOperacion()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(OPE.ID) = ?5 ");  
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion ");  
		} 
		
		if(!(""+vo.getIdEstadoExpediente()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(EST.ID) = ?6 ");  
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion ");  
		} 
		
		if(!(""+vo.getIdTerritorio()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(T.ID) = ?7 ");  
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion ");  
		} 
		
		if(!(""+vo.getIdOficina()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(O.ID) = ?8 ");  
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion ");  
		} 
		
		if(!inQuery.isEmpty()){
			queryString.append(" AND  EXP.ID IN " + inQuery +" ");
		}
		
		queryString.append(" ORDER BY  EXP.ID DESC ");
		
		Query query = em.createNativeQuery(queryString.toString());
		
		if(!vo.getCodigoCentral().isEmpty()){
			query.setParameter(1, vo.getCodigoCentral().toUpperCase());
//			query.setParameter("pCodigoCentral", vo.getCodigoCentral().toUpperCase());
		}
		
		if(vo.getFechaInicio() != null && vo.getFechaFin() != null && vo.getFechaInicio().before(vo.getFechaFin() )){
//			queryString.append(" AND  EXP.FECHA_REGISTRO >= ?6  AND ? 7 "); 	
			query.setParameter(9, vo.getFechaInicio());
			query.setParameter(10, vo.getFechaFin());
		}
		
		if(!vo.getRazonSocial().isEmpty()){
			query.setParameter(2, "%"+ vo.getRazonSocial().trim().toUpperCase() + "%" );
//			query.setParameter("pRazonSocial", "%"+ vo.getRazonSocial().toUpperCase() + "%" );
			
		}
		
		if(!vo.getNumeroExpediente().isEmpty()){
			
//			query.setParameter("pNumeroExpediente",  vo.getNumeroExpediente() );
			query.setParameter(3,  vo.getNumeroExpediente() );
		}
		
		if(!vo.getResponsable().isEmpty()){
//			query.setParameter("pResponsable",   vo.getResponsable().toUpperCase());
			query.setParameter(4,   vo.getResponsable().toUpperCase());
			
		}
		
		if(!(""+vo.getIdOperacion()+"").equalsIgnoreCase("0")){
//			query.setParameter("pIdOperacion", vo.getIdOperacion());
			query.setParameter(5, vo.getIdOperacion());
		} 
		
		if(!(""+vo.getIdEstadoExpediente()+"").equalsIgnoreCase("0")){
//			queryString.append(" AND UPPER(EST.ID) = ?6 ");  
			query.setParameter(6, vo.getIdEstadoExpediente());
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion ");  
		} 
		
		if(!(""+vo.getIdTerritorio()+"").equalsIgnoreCase("0")){
//			queryString.append(" AND UPPER(T.ID) = ?7 ");  
			query.setParameter(7, vo.getIdTerritorio());
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion ");  
		} 
		
		if(!(""+vo.getIdOficina()+"").equalsIgnoreCase("0")){
//			queryString.append(" AND UPPER(O.ID) = ?8 ");  
//			queryString.append(" AND UPPER(OPE.ID) like :pIdOperacion "); 
			query.setParameter(8, vo.getIdOficina());
		} 
		
		expedientes = query.getResultList();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ExpedienteDTO expedienteDTO;
		for(Object[] expediente : expedientes ){
			expedienteDTO = new ExpedienteDTO();
			expedienteDTO.setIdExpediente(expediente[0]==null ? "" : expediente[0].toString());
			expedienteDTO.setCodigoCentral(expediente[1] == null ? "" : expediente[1].toString());
			expedienteDTO.setCodigoOficina(expediente[2]  == null ? "" : expediente[2].toString());
			expedienteDTO.setDescripcionOficina(expediente[3] == null ? "" : expediente[3].toString());
			expedienteDTO.setOperacion(expediente[4] == null ? "" : expediente[4].toString());
			expedienteDTO.setCasoNegocio(expediente[5] == null ? "" : expediente[5].toString());
			expedienteDTO.setEstadoExpediente(expediente[6] == null ? "" : expediente[6].toString());
			expedienteDTO.setFechaRegistro(expediente[7] == null ? "" : df.format((java.util.Date) expediente[7]));
			expedienteDTO.setCodigoTerritorio(expediente[8] == null ? "" : expediente[8].toString());
			expedienteDTO.setDescripcionTerritorio(expediente[9] == null ? "" : expediente[9].toString());
			expedienteDTO.setGestor(expediente[10] == null ? "" : expediente[10].toString());
			expedienteDTO.setNombresCompletos(expediente[11] == null ? "" : expediente[11].toString());
			expedienteDTO.setRazonSocial(expediente[12] == null ? "" : expediente[12].toString());
			expedienteDTO.setMigrado(expediente[13] == null ? "" : getFLag(expediente[13].toString()));
			expedienteDTO.setCodigoPJ(expediente[14] == null ? "" : expediente[14].toString());
			expedienteDTO.setDescripcionTipoPJ(expediente[15] == null ? "" : expediente[15].toString());
			expedienteDTO.setExonerado(expediente[16] == null ? "" :  getFLag(expediente[16].toString()));
			
			String dictamen = "";
			try {
				if ( expediente[17] != null){
					StringBuffer strOut = new StringBuffer();
					String aux;
					BufferedReader br = new BufferedReader(new InputStreamReader(((Blob)expediente[17]).getBinaryStream()));
					while ((aux=br.readLine())!=null) {
					strOut.append(aux);
					}
					dictamen= strOut.toString();
					dictamen = html2text(dictamen);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			expedienteDTO.setDictamenBastanteo(dictamen);
//			expedienteDTO.setDictamenBastanteo(expediente[17] == null ? "" : expediente[17].toString());
			expedienteDTO.setResultadoBastanteo(expediente[18] == null ? "" : expediente[18].toString());
			expedienteDTO.setFechaMesaDocumentoAprobado(expediente[19] == null ? "" : df.format((java.util.Date)expediente[19]));
			expedienteDTO.setFechaMesaFirmasAprobado(expediente[20] == null ? "" : df.format((java.util.Date)expediente[20]));
			expedienteDTO.setFechaAbogadoBastanteo(expediente[21] == null ? "" : df.format((java.util.Date)expediente[21]));
			expedienteDTO.setFechaVerifResultadoTramite(expediente[22] == null ? "" : df.format((java.util.Date)expediente[22]));
			expedienteDTO.setFlagObservadoMesaDocumentos(expediente[23] == null ? "" : expediente[23].toString());
			expedienteDTO.setFlagObservadoMesaFirmas(expediente[24] == null ? "" : expediente[24].toString());
			
			String cierreAutomatico = "";
			
			if(expediente[25] == null || expediente[25].toString().equalsIgnoreCase("")){
				cierreAutomatico = "NO";
			}else{
				cierreAutomatico ="SI";
			}
			expedienteDTO.setFlagCierreAutomatico(cierreAutomatico);
			expedientesDTO.add(expedienteDTO);
		}
		
		
		return expedientesDTO;
	}

	public String getFLag(String flag){
		flag = "";
		if(flag.equalsIgnoreCase("1")){
			flag = "SI";
		}else if (flag.equalsIgnoreCase("2")){
			flag= "NO";
		}
		return flag;
	}
	
	@Override
	public Expediente findByIdExpUltBastanteo(Integer id) {
		Query query = em.createQuery(
	 			"select o from Expediente o where o.idExpUltBastanteo=:id");
		query.setParameter("id", id);
		Expediente expediente = null;
		List<Expediente> expedientes = query.getResultList();
		if (!expedientes.isEmpty()) {
			if (expedientes.size() > 1)
				LOG.warn("Existe más de un expediente automático generado.");
			expediente = (Expediente) expedientes.get(0);
		}
		return expediente;
	}
	
	public String html2text(String html) {
	    return Jsoup.parse(html).text();
	}

	@Override
	public List<ExpedienteDTO> getReporteAns(ReporteVO vo) {
		List<Object[]> expedientes = new ArrayList<Object[]>();
		List<ExpedienteDTO> expedientesDTO = new ArrayList<ExpedienteDTO>();
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" EXP.ID , ");
		queryString.append(" CLI.CODIGO_CENTRAL , "); 
		queryString.append(" CLI.RAZON_SOCIAL ,  ");
		queryString.append(" TAREA.DESCRIPCION, ");
		queryString.append(" EMP.CODIGO, ");
		queryString.append(" EMP.NOMBRES_COMPLETOS,  ");
		queryString.append(" HI.FECHA_ENVIO, ");
		queryString.append(" HI.FECHA_FIN, ");
		queryString.append(" HI.NUM_ANS_ESPERADO, ");
		queryString.append(" HI.NUM_ANS_REAL, ");
		queryString.append(" HI.FLAG_CUMPLE_ANS ");
		queryString.append(" FROM TBL_CE_IBM_EXPEDIENTE_CC EXP   ");
		queryString.append(" INNER JOIN TBL_CE_IBM_HISTORIAL_CC HI ON HI.ID_EXPEDIENTE_CC_FK = EXP.ID ");
		queryString.append(" INNER JOIN TBL_CE_IBM_TAREA_CC TAREA ON TAREA.ID = HI.ID_TAREA_FK ");
		queryString.append(" INNER JOIN TBL_CE_IBM_OPERACION_CC OPE ON OPE.ID = EXP.ID_OPERACION_FK  ");
		queryString.append(" INNER JOIN TBL_CE_IBM_CLIENTE_CC CLI ON CLI.ID = EXP.ID_CLIENTE_FK  ");
		queryString.append(" INNER JOIN TBL_CE_IBM_EMPLEADO EMP ON HI.ID_EMPLEADO_FK = EMP.ID  ");
		queryString.append(" INNER JOIN TBL_CE_IBM_OFICINA O ON O.ID = EMP.ID_OFICINA_FK  ");
		queryString.append(" INNER JOIN TBL_CE_IBM_TERRITORIO T  ON T.ID = O.ID_TERRITORIO_FK  ");
		queryString.append(" INNER JOIN TBL_CE_IBM_ESTADO_EXP_CC EST ON EST.ID = EXP.ID_ESTADO_FK  ");
		queryString.append(" LEFT JOIN TBL_CE_IBM_CASO_NEGOCIO_CC CN ON CN.ID = EXP.ID_CASO_NEGOCIO_FK  WHERE 1=1 ");
		
		
		if(!vo.getCodigoCentral().isEmpty()){
			queryString.append(" AND UPPER(CLI.CODIGO_CENTRAL) = ?1 "); 
			
		}
		
		if(vo.getFechaInicio() != null && vo.getFechaFin() != null && vo.getFechaInicio().before(vo.getFechaFin() )){
//			queryString.append(" AND  TO_DATE(TO_CHAR( EXP.FECHA_REGISTRO, 'DD-MM-YYYY'), 'DD-MM-YYYY')  >= ?9  AND  TO_DATE(TO_CHAR(EXP.FECHA_REGISTRO, 'DD-MM-YYYY'), 'DD-MM-YYYY') <= ?10 "); 
			queryString.append(" AND  HI.FECHA_REGISTRO >= ?9  AND  HI.FECHA_REGISTRO <= ?10 "); 	
		}
		
		if(!vo.getRazonSocial().isEmpty()){
			queryString.append(" AND UPPER(CLI.RAZON_SOCIAL) LIKE ?2 "); 
		}
		
		if(!vo.getNumeroExpediente().isEmpty()){
			queryString.append(" AND to_char(EXP.ID) = ?3 ");  
		}
		
		if(!vo.getResponsable().isEmpty()){
			queryString.append(" AND UPPER(EMP.CODIGO) = ?4 "); 
		}
			
		if(!(""+vo.getIdOperacion()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(OPE.ID) = ?5 ");  
		} 
		
		if(!(""+vo.getIdEstadoExpediente()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(EST.ID) = ?6 ");   
		} 
		
		if(!(""+vo.getIdTerritorio()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(T.ID) = ?7 ");  
		} 
		
		if(!(""+vo.getIdOficina()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(O.ID) = ?8 ");  
		} 
		
		if(!(""+vo.getIdTarea()+"").equalsIgnoreCase("0")){
			queryString.append(" AND UPPER(TAREA.ID) = ?11 ");  
		} 
		
		queryString.append(" ORDER BY  EXP.ID, HI.FECHA_ENVIO DESC ");
		
		Query query = em.createNativeQuery(queryString.toString());
		
		LOG.info("Query REPORTE Ans :" +queryString.toString() );
		
		if(!vo.getCodigoCentral().isEmpty()){
			query.setParameter(1, vo.getCodigoCentral().toUpperCase());
		}
		
		if(vo.getFechaInicio() != null && vo.getFechaFin() != null && vo.getFechaInicio().before(vo.getFechaFin() )){	
			query.setParameter(9, vo.getFechaInicio());
			query.setParameter(10, vo.getFechaFin());
		}
		
		if(!vo.getRazonSocial().isEmpty()){
			query.setParameter(2, "%"+ vo.getRazonSocial().trim().toUpperCase() + "%" );	
		}
		
		if(!vo.getNumeroExpediente().isEmpty()){
			query.setParameter(3,  vo.getNumeroExpediente() );
		}
		
		if(!vo.getResponsable().isEmpty()){
			query.setParameter(4,   vo.getResponsable().toUpperCase());
			
		}
		
		if(!(""+vo.getIdOperacion()+"").equalsIgnoreCase("0")){
			query.setParameter(5, vo.getIdOperacion());
		} 
		
		if(!(""+vo.getIdEstadoExpediente()+"").equalsIgnoreCase("0")){ 
			query.setParameter(6, vo.getIdEstadoExpediente());
		} 
		
		if(!(""+vo.getIdTerritorio()+"").equalsIgnoreCase("0")){ 
			query.setParameter(7, vo.getIdTerritorio()); 
		} 
		
		if(!(""+vo.getIdOficina()+"").equalsIgnoreCase("0")){
			query.setParameter(8, vo.getIdOficina());
		} 
		
		if(!(""+vo.getIdTarea()+"").equalsIgnoreCase("0")){
			query.setParameter(11, vo.getIdTarea());
		}
		
		expedientes = query.getResultList();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		ExpedienteDTO expedienteDTO;
		System.out.println("query: " +queryString.toString());
		System.out.println("canridadResultado: " + expedientes.size());
		for(Object[] expediente : expedientes ){
			
			expedienteDTO = new ExpedienteDTO();
			expedienteDTO.setIdExpediente(expediente[0]==null ? "" : expediente[0].toString());
			expedienteDTO.setCodigoCentral(expediente[1] == null ? "" : expediente[1].toString());
			expedienteDTO.setRazonSocial(expediente[2] == null ? "" : expediente[2].toString());
			expedienteDTO.setTareaDescripcion(expediente[3] == null ? "" : expediente[3].toString());
			expedienteDTO.setGestor(expediente[4] == null ? "" : expediente[4].toString());
			expedienteDTO.setNombresCompletos(expediente[5] == null ? "" : expediente[5].toString());
			expedienteDTO.setFechaEnvio(expediente[6] == null ? "" : df.format((java.util.Date)expediente[6]));
			expedienteDTO.setFechaFin(expediente[7] == null ? "" : df.format((java.util.Date)expediente[7]));
			expedienteDTO.setAnsEsperado(expediente[8] == null ? "" : expediente[8].toString());
			expedienteDTO.setAnsReal(expediente[9] == null ? "" : expediente[9].toString());
			
			String cumpleAns = "";
			
			if (expediente[10] != null) {
				if (expediente[10].toString().equalsIgnoreCase("0")) {
					cumpleAns = "NO";
				} else {
					cumpleAns = "SI";
				}
			}
			expedienteDTO.setCumpleAns(cumpleAns);
			
			
			expedientesDTO.add(expedienteDTO);
		}
		
		
		return expedientesDTO;
		
	}
	
}
