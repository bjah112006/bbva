package com.ibm.bbva.ctacte.dao.impl;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Attachment;
import com.ibm.bbva.ctacte.bean.Requerimiento;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.RequerimientoDAO;
import com.ibm.bbva.ctacte.model.RequerimientoModel;
import com.ibm.icu.text.SimpleDateFormat;

@Stateless
@Local(RequerimientoDAO.class)
public class RequerimientoDaoImpl extends GenericDAO<Requerimiento, Integer> implements RequerimientoDAO{

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	private static final Logger LOG = LoggerFactory.getLogger(RequerimientoDaoImpl.class);

	public RequerimientoDaoImpl() {
		super(Requerimiento.class);
	}
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	@Override
	public List<RequerimientoModel> listaRequerimiento(RequerimientoModel filtro) throws SQLException {
		
		List<Object[]> listaObject =new ArrayList<Object[]>();	
		List<RequerimientoModel> requerimientoModels =new ArrayList<RequerimientoModel>();	
		LOG.info("FILTRO DAO : "+filtro.toString());

		String q="select R.ID,R.COD_CENTRAL," +
				"R.RAZON_SOCIAL,R.TIPO_OPERACION," +
				"R.ES_MIGRADO,R.VERSION," +
				"R.DESCRIPCION_RECLAMO_CONSULTA,R.DETALLE_CATEGORIA_RECLAMO,"+
				"R.DESCRIPCION_ABSOLUCION,R.RECONSIDERACION," +
				"R.MOTIVO_RECONSIDERACION,R.USUARIO_REGISTRA," +
				"R.USUARIO_ABSUELVE,R.USUARIO_RECONSIDERA,"+
				"R.USUARIO_ABSUELVE_RECONSIDERA,R.FECHA_REGISTRO," +
				"R.FECHA_ABSUELVE,R.FECHA_RECONSIDERA," +
				"R.FECHA_ABSUELVE_RECONSIDERA,R.ESTADO,"+
				"R.NRO_DOI,MT.NOMBRE AS STR_ESTADO," +
				"MTA.NOMBRE AS STR_TIPO_OPERACION," +
				"MTAB.NOMBRE AS STR_RESULTADO," +
				"MTABL.NOMBRE AS STR_CATEGORIA_RECLAMO," +
				"R.NOMBRE_USUARIO_REGISTRA," +
				"R.RESULTADO," +
				"R.TIPO_DOI," +
				"R.ID_CLASIFICACION," +
				"OFI.DESCRIPCION," +
				"R.COD_OFICINA," +
				"R.DESCRIPCION_ABSOLUCION_RECON," +
				"R.ID_ABOGADO," +
				"R.NOMBRE_ABOGADO_ASIGNADO from CONELE.TBL_CE_IBM_REQUERIMIENTO_CC R LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA  MT ON R.ESTADO=MT.ENTERO "+
				"LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA MTAB ON R.RESULTADO=MTAB.ENTERO "+
				"LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA MTABL ON R.DETALLE_CATEGORIA_RECLAMO=MTABL.ENTERO "+
				"LEFT JOIN CONELE.TBL_CE_IBM_OFICINA OFI ON R.COD_OFICINA=OFI.CODIGO "+
				"LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA MTA ON R.TIPO_OPERACION=MTA.ENTERO " +
				"WHERE 1=1 ";

				
		
		if(filtro.getCod_central()!=null && !filtro.getCod_central().isEmpty()){
			//			q=q.concat(" and R.cod_central=:cod_central");
			q=q.concat(" and r.cod_central=?1");
		}
		if(filtro.getTipo_operacion()!=null && !(filtro.getTipo_operacion().compareTo(0L)==0)){
			q=q.concat(" and r.tipo_operacion=?2");
		}
		if(filtro.getNro_doi()!=null && !filtro.getNro_doi().isEmpty()){
			q=q.concat(" and r.nro_doi=?3");
		}
		
		if((filtro.getEstado()!=null && !(filtro.getEstado().compareTo(0L)==0)) && filtro.isIncluirCerrados()==true){
			q=q.concat(" and (r.estado=?4 or r.estado=?5)");
		}else if((filtro.getEstado()==null || filtro.getEstado().compareTo(0L)==0) && filtro.isIncluirCerrados()==true){
//			All
		}else if((filtro.getEstado()==null || filtro.getEstado().compareTo(0L)==0) && filtro.isIncluirCerrados()==false){
			q=q.concat(" and r.estado<>?5");
		}else if((filtro.getEstado()!=null && !(filtro.getEstado().compareTo(0L)==0)) && filtro.isIncluirCerrados()==false){
			q=q.concat(" and r.estado=?4");
		}
//		1-false Solo el marcado 
//		1 -true solo el marcado y cerrado X
//		0 -false todos menos cerrados X
//		0 - true todos X
		
//		if((filtro.getEstado()!=null && !(filtro.getEstado().compareTo(0L)==0))&& filtro.isIncluirCerrados()==true){
//			q=q.concat(" and (r.estado=?4 or r.estado=?5)");
//		}else if(filtro.getEstado()!=null && !(filtro.getEstado().compareTo(0L)==0)){
//			q=q.concat(" and r.estado=?4");
//		}else if(filtro.isIncluirCerrados()==true){
//			q=q.concat(" and r.estado=?5");
//		}
				
		if(filtro.getIdResultado()!=null && !(filtro.getIdResultado().compareTo(0L)==0)){
			q=q.concat(" and r.resultado=?6");
		}
		
		if(filtro.getFechaInicio()!=null){
			q=q.concat(" and r.fecha_registro>=?7");
		}
		if(filtro.getFechafin()!=null){
			q=q.concat(" and r.fecha_registro<=?8");
		}
		
		if(filtro.getCod_oficina()!=null){
			q=q.concat(" and r.cod_oficina<=?9");
		}
		
		if(filtro.getStrId()!=null && !filtro.getStrId().isEmpty()){
			//			q=q.concat(" and R.cod_central=:cod_central");
			q=q.concat(" and r.id=?10");
		}
		q=q.concat(" ORDER BY R.ID desc");
		LOG.info("Query Busqueda : "+q);
		
		Query query = em.createNativeQuery(q);
		if(filtro.getCod_central()!=null && !filtro.getCod_central().isEmpty()){
			query.setParameter(1, filtro.getCod_central());
		}
		if(filtro.getTipo_operacion()!=null && !(filtro.getTipo_operacion().compareTo(0L)==0)){
			query.setParameter(2, filtro.getTipo_operacion());
		}
		if(filtro.getNro_doi()!=null && !filtro.getNro_doi().isEmpty()){
			query.setParameter(3, filtro.getNro_doi());
		}
		
		if((filtro.getEstado()!=null && !(filtro.getEstado().compareTo(0L)==0)) && filtro.isIncluirCerrados()==true){
			query.setParameter(4, filtro.getEstado());
			query.setParameter(5, ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO);
		}else if((filtro.getEstado()==null || filtro.getEstado().compareTo(0L)==0) && filtro.isIncluirCerrados()==true){
//			All
		}else if((filtro.getEstado()==null || filtro.getEstado().compareTo(0L)==0) && filtro.isIncluirCerrados()==false){
			query.setParameter(5, ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO);
		}else if((filtro.getEstado()!=null && !(filtro.getEstado().compareTo(0L)==0)) && filtro.isIncluirCerrados()==false){
			query.setParameter(4, filtro.getEstado());
		}
		
		
		
		if(filtro.getIdResultado()!=null && !(filtro.getIdResultado().compareTo(0L)==0)){
			query.setParameter(6, filtro.getIdResultado());
		}
		
		if(filtro.getFechaInicio()!=null){
			query.setParameter(7, filtro.getFechaInicio());
		}
		if(filtro.getFechafin()!=null){
			query.setParameter(8, filtro.getFechafin());
		}
		
		if(filtro.getCod_oficina()!=null){
			query.setParameter(9, filtro.getCod_oficina());
		}
		if(filtro.getStrId()!=null && !filtro.getStrId().isEmpty()){
			query.setParameter(10, filtro.getStrId());
		}
		listaObject = query.getResultList();


		LOG.info("Parametros del querys :" + query.getParameters().toArray());


		for (Object[] r : listaObject) {
			Blob blob6=(Blob)r[6];
			Blob blob8=(Blob)r[8];
			Blob blob10=(Blob)r[10];
			Blob blob31=(Blob)r[31];

			RequerimientoModel rm = new RequerimientoModel();

			rm.setId(((BigDecimal)r[0]).longValue());
			rm.setStrId(rm.getId().toString());			
			rm.setCod_central((String)r[1]);
			rm.setRazon_social((String) r[2]);
			rm.setTipo_operacion(((BigDecimal)r[3])==null?null:((BigDecimal)r[3]).longValue());
			rm.setEs_migrado((String)r[4]);
			rm.setVersion(((BigDecimal)r[5])==null?null:((BigDecimal)r[5]).intValue());
			rm.setDescripcion_reclamo_Consulta(blob6==null?"":new String(blob6.getBytes(1,(int) blob6.length())));
			rm.setDetalle_categoria_reclamo(((BigDecimal)r[7])==null?null:((BigDecimal)r[7]).longValue());
			rm.setDescripcion_absolucion(blob8==null?"":new String(blob8.getBytes(1,(int) blob8.length())));
			rm.setReconsideracion(r[9]==null?false:((String)r[9]).equalsIgnoreCase("S")?true:false);
			rm.setMotivo_reconsideracion(blob10==null?"":new String(blob10.getBytes(1,(int) blob10.length())));
			rm.setUsuario_registra((String)r[11]);
			rm.setUsuario_absuelve((String)r[12]);
			rm.setUsuario_reconsidera((String)r[13]);
			rm.setUsuario_absuelve_reconsidera((String)r[14]);
			rm.setFecha_registro((Date)r[15]);
			rm.setStrFechaRegistro(new SimpleDateFormat("dd/MM/yyyy").format(rm.getFecha_registro()));				
			rm.setFecha_absuelve((Date)r[16]);
			rm.setFecha_reconsidera((Date)r[17]);
			rm.setFecha_absuelve_reconsidera((Date)r[18]);
			rm.setEstado(((BigDecimal)r[19]).longValue());
			rm.setNro_doi((String)r[20]);
			rm.setStrEstado((String)r[21]);	
			rm.setStr_tipo_operacion((String) r[22]);
			rm.setStr_resultado((String) r[23]);
			rm.setStr_categoria_reclamo((String) r[24]);
			rm.setNombre_usuario_registra((String) r[25]);
			rm.setIdResultado(((BigDecimal)r[26])==null?null:((BigDecimal)r[26]).longValue());
			rm.setTipoDoi((String) r[27]);
			rm.setIdClasificacion(((BigDecimal)r[28])==null?null:((BigDecimal)r[28]).longValue());
			rm.setStr_oficina((String) r[29]);
			rm.setCod_oficina(((String) r[30])==null?null:(String) r[30]);
			rm.setDescripcion_absolucion_recon(blob31==null?"":new String(blob31.getBytes(1,(int) blob31.length())));
			rm.setIdAbogado(((String) r[32])==null?null:(String) r[32]);
			rm.setNombreAbogadoAsignado(((String) r[33])==null?null:(String) r[33]);
			
			
			rm.setStrDescripcion_reclamo_Consulta(Jsoup.parse(rm.getDescripcion_reclamo_Consulta()).text());
			rm.setStrDescripcion_absolucion(Jsoup.parse(rm.getDescripcion_absolucion()).text());
			rm.setStrMotivo_reconsideracion(Jsoup.parse(rm.getMotivo_reconsideracion()).text());
			rm.setStrDescripcion_absolucion_recon(Jsoup.parse(rm.getDescripcion_absolucion_recon()).text());
			
			
			requerimientoModels.add(rm);
		}
		return requerimientoModels;

	}
	@Override
	public RequerimientoModel obtener(Long id) throws SQLException {
		Object[] r ;		
		RequerimientoModel rm = new RequerimientoModel();
		LOG.info("RequerimientoDaoImp - Obtener - ID :" + id);
		if(id!=null){
			List<Object[]> listaObject =new ArrayList<Object[]>();	
			List<Attachment> listaAttachment =new ArrayList<Attachment>();	

			String q2="SELECT ATTA.ID, " +
					"ATTA.ID_REQUERIMIENTO, " +
					"ATTA.NOMBRE_ARCHIVO, " +
					"ATTA.RUTA_ARCHIVO, " +
					"ATTA.USUARIO_REGISTRO," +
					"ATTA.FECHA_REGISTRO," +
					"ATTA.ESTADO_REF " +
					"FROM TBL_CE_IBM_ATTACHMENT_CC ATTA " +
					"WHERE ATTA.ID_REQUERIMIENTO=?1";
			LOG.info("query q2" +q2);
			Query query2 = em.createNativeQuery(q2);
			LOG.info("query2" +query2);
			query2.setParameter(1, id);

			listaObject = query2.getResultList();
			
			LOG.info("listaObject" +listaObject);
			
			for (Object[] obj : listaObject) {
				Attachment a=new Attachment();
				a.setId(((BigDecimal)obj[0]).longValue());
				a.setId_Requerimiento(((BigDecimal)obj[1]).longValue());
				a.setNombre_archivo((String) obj[2]);
				a.setRuta_archivo((String) obj[3]);
				a.setUsuario_registro((String) obj[4]);
				a.setFecha_registro((Date)obj[5]);
				a.setEstado_ref((String) obj[6]);
				LOG.info("objecto a " +a);
				listaAttachment.add(a);
			}

			String q="select R.ID,R.COD_CENTRAL," +
					"R.RAZON_SOCIAL,R.TIPO_OPERACION," +
					"R.ES_MIGRADO,R.VERSION," +
					"R.DESCRIPCION_RECLAMO_CONSULTA,R.DETALLE_CATEGORIA_RECLAMO,"+
					"R.DESCRIPCION_ABSOLUCION,R.RECONSIDERACION," +
					"R.MOTIVO_RECONSIDERACION,R.USUARIO_REGISTRA," +
					"R.USUARIO_ABSUELVE,R.USUARIO_RECONSIDERA,"+
					"R.USUARIO_ABSUELVE_RECONSIDERA,R.FECHA_REGISTRO," +
					"R.FECHA_ABSUELVE,R.FECHA_RECONSIDERA," +
					"R.FECHA_ABSUELVE_RECONSIDERA,R.ESTADO,"+
					"R.NRO_DOI,MT.NOMBRE AS STR_ESTADO," +
					"MTA.NOMBRE AS STR_TIPO_OPERACION," +
					"MTAB.NOMBRE AS STR_RESULTADO," +
					"MTABL.NOMBRE AS STR_CATEGORIA_RECLAMO," +
					"R.NOMBRE_USUARIO_REGISTRA," +
					"R.RESULTADO," +
					"R.TIPO_DOI," +
					"R.ID_CLASIFICACION," +
					"OFI.DESCRIPCION," +
					"R.COD_OFICINA," +
					"R.DESCRIPCION_ABSOLUCION_RECON," +
					"R.ID_ABOGADO," +
					"R.NOMBRE_ABOGADO_ASIGNADO from CONELE.TBL_CE_IBM_REQUERIMIENTO_CC R LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA  MT ON R.ESTADO=MT.ENTERO "+
					"LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA MTAB ON R.RESULTADO=MTAB.ENTERO "+
					"LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA MTABL ON R.DETALLE_CATEGORIA_RECLAMO=MTABL.ENTERO "+
					"LEFT JOIN CONELE.TBL_CE_IBM_OFICINA OFI ON R.COD_OFICINA=OFI.CODIGO "+
					"LEFT JOIN CONELE.TBL_CE_IBM_MULTITABLA MTA ON R.TIPO_OPERACION=MTA.ENTERO " +
					"WHERE R.id=?1  ";


			Query query = em.createNativeQuery(q);

			query.setParameter(1, id);

			r = (java.lang.Object[]) query.getSingleResult();

			LOG.info("query :" + q);
			LOG.info("Parametros del querys :" + query.getParameters().toArray());

			LOG.info("objecto r " +r);
			if (r!=null) {
				LOG.info("objecto R " +r);
				Blob blob6=(Blob)r[6];
				Blob blob8=(Blob)r[8];
				Blob blob10=(Blob)r[10];
				Blob blob31=(Blob)r[31];

				rm.setId(((BigDecimal)r[0]).longValue());
				rm.setCod_central((String)r[1]);
				rm.setRazon_social((String) r[2]);
				rm.setTipo_operacion(((BigDecimal)r[3])==null?null:((BigDecimal)r[3]).longValue());
				rm.setEs_migrado(r[4]==null?null:((String)r[4]).equalsIgnoreCase("S")?"SI":"NO");
				rm.setVersion(((BigDecimal)r[5])==null?null:((BigDecimal)r[5]).intValue());
				rm.setDescripcion_reclamo_Consulta(blob6==null?"":new String(blob6.getBytes(1,(int) blob6.length())));
				rm.setDetalle_categoria_reclamo(((BigDecimal)r[7])==null?null:((BigDecimal)r[7]).longValue());
				rm.setDescripcion_absolucion(blob8==null?"":new String(blob8.getBytes(1,(int) blob8.length())));
				rm.setReconsideracion(r[9]==null?false:((String)r[9]).equalsIgnoreCase("S")?true:false);
				rm.setMotivo_reconsideracion(blob10==null?"":new String(blob10.getBytes(1,(int) blob10.length())));
				rm.setUsuario_registra((String)r[11]);
				rm.setUsuario_absuelve((String)r[12]);
				rm.setUsuario_reconsidera((String)r[13]);
				rm.setUsuario_absuelve_reconsidera((String)r[14]);
				rm.setFecha_registro((Date)r[15]);
				rm.setFecha_absuelve((Date)r[16]);
				rm.setFecha_reconsidera((Date)r[17]);
				rm.setFecha_absuelve_reconsidera((Date)r[18]);
				rm.setEstado(((BigDecimal)r[19]).longValue());
				rm.setNro_doi((String)r[20]);
				rm.setStrEstado((String)r[21]);	
				rm.setStr_tipo_operacion((String) r[22]);
				rm.setStr_resultado((String) r[23]);
				rm.setStr_categoria_reclamo((String) r[24]);
				rm.setNombre_usuario_registra((String) r[25]);
				rm.setIdResultado(((BigDecimal)r[26])==null?null:((BigDecimal)r[26]).longValue());
				rm.setTipoDoi((String) r[27]);
				rm.setIdClasificacion(((BigDecimal)r[28])==null?null:((BigDecimal)r[28]).longValue());
				rm.setStr_oficina((String) r[29]);
				rm.setCod_oficina(((String) r[30])==null?null:(String) r[30]);
				rm.setListaAttachment(listaAttachment);
				rm.setDescripcion_absolucion_recon(blob31==null?"":new String(blob31.getBytes(1,(int) blob31.length())));
				rm.setIdAbogado(((String) r[32])==null?null:(String) r[32]);
				rm.setNombreAbogadoAsignado(((String) r[33])==null?null:(String) r[33]);
				
				
				rm.setStrDescripcion_reclamo_Consulta(Jsoup.parse(rm.getDescripcion_reclamo_Consulta()).text());
				rm.setStrDescripcion_absolucion(Jsoup.parse(rm.getDescripcion_absolucion()).text());
				rm.setStrMotivo_reconsideracion(Jsoup.parse(rm.getMotivo_reconsideracion()).text());
				rm.setStrDescripcion_absolucion_recon(Jsoup.parse(rm.getDescripcion_absolucion_recon()).text());
			}

		}

		LOG.info("RM " +rm);
		return rm;

	}
}
