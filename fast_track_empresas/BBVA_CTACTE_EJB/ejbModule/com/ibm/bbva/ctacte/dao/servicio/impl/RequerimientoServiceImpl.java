package com.ibm.bbva.ctacte.dao.servicio.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Attachment;
import com.ibm.bbva.ctacte.bean.Requerimiento;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.AttachmentDAO;
import com.ibm.bbva.ctacte.dao.RequerimientoDAO;
import com.ibm.bbva.ctacte.dao.servicio.RequerimientoService;
import com.ibm.bbva.ctacte.model.RequerimientoModel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(RequerimientoService.class)
public class RequerimientoServiceImpl implements RequerimientoService {

	private static final Logger LOG = LoggerFactory.getLogger(RequerimientoServiceImpl.class);

	@EJB
	private RequerimientoDAO requerimientoDAO;

	@EJB
	private AttachmentDAO attachmentDAO;
	
	

	/**
	 * Default constructor. 
	 */
	public RequerimientoServiceImpl() {
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public List<RequerimientoModel> listaRequerimiento(RequerimientoModel filtro) throws SQLException {
		LOG.info("FILTRO SERVICE : "+filtro.toString());
		return requerimientoDAO.listaRequerimiento(filtro);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public RequerimientoModel Registrar(RequerimientoModel requerimiento,
			Attachment attachment) {
		Requerimiento r=new Requerimiento();
		r.setId(requerimiento.getId());
		r.setCod_central(requerimiento.getCod_central());
		r.setDescripcion_absolucion(requerimiento.getDescripcion_absolucion()==null?null:requerimiento.getDescripcion_absolucion().isEmpty()?null:requerimiento.getDescripcion_absolucion().getBytes());
		r.setDescripcion_reclamo_Consulta(requerimiento.getDescripcion_reclamo_Consulta()==null?null:requerimiento.getDescripcion_reclamo_Consulta().isEmpty()?null:requerimiento.getDescripcion_reclamo_Consulta().getBytes());
		r.setMotivo_reconsideracion(requerimiento.getMotivo_reconsideracion()==null?null:requerimiento.getMotivo_reconsideracion().isEmpty()?null:requerimiento.getMotivo_reconsideracion().getBytes());
		r.setDetalle_categoria_reclamo(requerimiento.getDetalle_categoria_reclamo());
		r.setEs_migrado(requerimiento.getEs_migrado().equalsIgnoreCase("SI")?"S":"N");
		r.setEstado(requerimiento.getEstado());
		r.setResultado(requerimiento.getIdResultado());
		r.setFecha_absuelve(requerimiento.getFecha_absuelve());
		r.setFecha_absuelve_reconsidera(requerimiento.getFecha_absuelve_reconsidera());
		r.setFecha_reconsidera(requerimiento.getFecha_reconsidera());
		r.setFecha_registro(requerimiento.getFecha_registro());
		r.setNro_doi(requerimiento.getNro_doi());
		r.setRazon_social(requerimiento.getRazon_social());
		r.setVersion(requerimiento.getVersion()+1);
		r.setReconsideracion(requerimiento.isReconsideracion()==false?"N":"S");
		r.setTipo_operacion(requerimiento.getTipo_operacion());
		r.setUsuario_absuelve(requerimiento.getUsuario_absuelve());
		r.setUsuario_absuelve_reconsidera(requerimiento.getUsuario_absuelve_reconsidera());
		r.setUsuario_reconsidera(requerimiento.getUsuario_reconsidera());
		r.setUsuario_registra(requerimiento.getUsuario_registra());
		r.setNombre_usuario_registra(requerimiento.getNombre_usuario_registra());
		r.setResultado(requerimiento.getIdResultado());
		r.setIdClasificacion(requerimiento.getIdClasificacion());
		r.setTipoDoi(requerimiento.getTipoDoi());
		r.setCod_oficina(requerimiento.getCod_oficina());
		r.setDescripcion_absolucion_recon(requerimiento.getDescripcion_absolucion_recon()==null?null:requerimiento.getDescripcion_absolucion_recon().isEmpty()?null:requerimiento.getDescripcion_absolucion_recon().getBytes());

		LOG.info("IF :" + requerimiento.toString());
		if(requerimiento.getId()==null){
			LOG.info("REGISTRAR SERVICE :" + r.toString());
			requerimientoDAO.save(r);
			requerimiento.setId(r.getId());
		}else{
			LOG.info("ACTUALIZAR SERVICE :" + r.toString());
			requerimientoDAO.update(r);			
		}
		if(attachment.getNombre_archivo()!=null && !attachment.getNombre_archivo().isEmpty()){
			LOG.info("r.getId(): " + r.getId());
			attachment.setId_Requerimiento(r.getId());
			attachment.setFecha_registro(new Date());
			attachment.setUsuario_registro(requerimiento.getUsuario_registra());
			attachment.setRuta_archivo(attachment.getRuta_archivo());


			LOG.info("SERVICE attachment :"+"ID :"+attachment.getId() +"RUTA:"+ attachment.getRuta_archivo() +" NOMBRE:"+ attachment.getNombre_archivo());
			attachmentDAO.save(attachment);
		}


		return requerimiento;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public RequerimientoModel Actualizar(RequerimientoModel requerimiento,
			Attachment attachment) {
		Requerimiento r=new Requerimiento();
		r.setId(requerimiento.getId());
		r.setCod_central(requerimiento.getCod_central());
		r.setDescripcion_absolucion(requerimiento.getDescripcion_absolucion()==null?null:requerimiento.getDescripcion_absolucion().isEmpty()?null:requerimiento.getDescripcion_absolucion().getBytes());
		r.setDescripcion_reclamo_Consulta(requerimiento.getDescripcion_reclamo_Consulta()==null?null:requerimiento.getDescripcion_reclamo_Consulta().isEmpty()?null:requerimiento.getDescripcion_reclamo_Consulta().getBytes());
		r.setMotivo_reconsideracion(requerimiento.getMotivo_reconsideracion()==null?null:requerimiento.getMotivo_reconsideracion().isEmpty()?null:requerimiento.getMotivo_reconsideracion().getBytes());
		r.setDetalle_categoria_reclamo(requerimiento.getDetalle_categoria_reclamo());
		r.setEs_migrado(requerimiento.getEs_migrado().equalsIgnoreCase("SI")?"S":"N");
		r.setEstado(requerimiento.getEstado());
		r.setResultado(requerimiento.getIdResultado());
		r.setFecha_absuelve(requerimiento.getFecha_absuelve());
		r.setFecha_absuelve_reconsidera(requerimiento.getFecha_absuelve_reconsidera());
		r.setFecha_reconsidera(requerimiento.getFecha_reconsidera());
		r.setFecha_registro(requerimiento.getFecha_registro());
		r.setNro_doi(requerimiento.getNro_doi());
		r.setRazon_social(requerimiento.getRazon_social());
		r.setVersion(requerimiento.getVersion());
		r.setReconsideracion(requerimiento.isReconsideracion()==false?"N":"S");
		r.setTipo_operacion(requerimiento.getTipo_operacion());
		r.setUsuario_absuelve(requerimiento.getUsuario_absuelve());
		r.setUsuario_absuelve_reconsidera(requerimiento.getUsuario_absuelve_reconsidera());
		r.setUsuario_reconsidera(requerimiento.getUsuario_reconsidera());
		r.setUsuario_registra(requerimiento.getUsuario_registra());
		r.setNombre_usuario_registra(requerimiento.getNombre_usuario_registra());
		r.setResultado(requerimiento.getIdResultado());
		r.setIdClasificacion(requerimiento.getIdClasificacion());
		r.setTipoDoi(requerimiento.getTipoDoi());
		r.setCod_oficina(requerimiento.getCod_oficina());
		r.setDescripcion_absolucion_recon(requerimiento.getDescripcion_absolucion_recon()==null?null:requerimiento.getDescripcion_absolucion_recon().isEmpty()?null:requerimiento.getDescripcion_absolucion_recon().getBytes());
		r.setIdAbogado(requerimiento.getIdAbogado());
		r.setNombreAbogadoAsignado(requerimiento.getNombreAbogadoAsignado());
		requerimientoDAO.update(r);
		return requerimiento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public RequerimientoModel obtener(RequerimientoModel obj) throws SQLException {
		LOG.info("RequerimientoServiceImp - Obtener");
		RequerimientoModel objecto=new RequerimientoModel();
		objecto= requerimientoDAO.obtener(obj.getId());
		objecto.setEsAbogado(obj.isEsAbogado());
		LOG.info("RequerimientoServiceImp - Obtener - OBJECT :" + objecto);
		if(objecto.getTipo_operacion().compareTo(ConstantesBusiness.CODIGO_HIJO_TIPO_OPERACION_CONSULTA)==0){
			LOG.info("RequerimientoServiceImp - CONSULTA");
			objecto.setShowReconsideracion(false);
			objecto.setDisableReconsideracion(true);
			objecto.setShowResultado(false);
			objecto.setDisableResultado(true);
			objecto.setShowClasificacion(true);
			if(objecto.isEsAbogado()){
				LOG.info("RequerimientoServiceImp - CONSULTA - ABOGADO");
				if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO)==0){								
					objecto.setDisableClasificacion(true);
					objecto.setReadOnly(true);
				}else{			
					objecto.setDisableClasificacion(false);
					objecto.setReadOnly(false);
				}
			}else if(!objecto.isEsAbogado()){
				LOG.info("RequerimientoServiceImp - CONSULTA - NO ABOGADO");
				if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO)==0 || 
						objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO)==0){								
					objecto.setDisableClasificacion(true);
					objecto.setReadOnly(true);
				}
			}
		}else{
			LOG.info("RequerimientoServiceImp - RECLAMO");
			objecto.setShowAbsolucion(false);
			objecto.setShowReconsideracion(false);
			objecto.setShowAbsolucionRecon(false);
			objecto.setShowClasificacion(false);			
			objecto.setShowResultado(false);

			objecto.setDisableAbsolucion(true);
			objecto.setDisableAbsolucionRecon(true);
			objecto.setDisableClasificacion(true);
			objecto.setDisableReconsideracion(true);
			objecto.setDisableResultado(true);
			
			if(objecto.isEsAbogado()){
				LOG.info("RequerimientoServiceImp - RECLAMO - ABOGADO");
				objecto.setShowAbsolucion(true);			
				objecto.setShowClasificacion(true);
				objecto.setShowResultado(true);
				if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO)==0){
					LOG.info("RequerimientoServiceImp - RECLAMO - ABOGADO - ENVIADO");
					objecto.setDisableAbsolucion(false);
					objecto.setDisableClasificacion(false);
					objecto.setDisableResultado(false);
					objecto.setReadOnly(false);
					
				}else if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_DICTAMINADO)==0){
					LOG.info("RequerimientoServiceImp - RECLAMO - ABOGADO - DICTAMINADO");
					objecto.setDisableAbsolucion(true);
					objecto.setDisableClasificacion(true);
					objecto.setDisableResultado(true);

					objecto.setReadOnly(true);
				}else if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_RECONSIDERADO)==0){
					LOG.info("RequerimientoServiceImp - RECLAMO - ABOGADO - RECONSIDERADO");
					objecto.setShowReconsideracion(true);
					objecto.setShowAbsolucionRecon(true);

					objecto.setDisableAbsolucion(true);
					objecto.setDisableClasificacion(false);
					objecto.setDisableResultado(false);
					objecto.setDisableReconsideracion(true);
					objecto.setDisableAbsolucionRecon(false);
					objecto.setReadOnly(false);

				}else if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO)==0){					
					LOG.info("RequerimientoServiceImp - RECLAMO - ABOGADO - CERRADO");
					objecto.setDisableAbsolucion(true);
					objecto.setDisableClasificacion(true);
					objecto.setDisableResultado(true);

					if(objecto.getMotivo_reconsideracion()!=null&&!(objecto.getMotivo_reconsideracion().isEmpty())){
						LOG.info("RequerimientoServiceImp - RECLAMO - ABOGADO - CERRADO - RECONSIDERADO");
						objecto.setShowReconsideracion(true);
						objecto.setShowAbsolucionRecon(true);

						objecto.setDisableReconsideracion(true);
						objecto.setDisableAbsolucionRecon(true);
					}
					objecto.setReadOnly(true);
				}


			}else if(!objecto.isEsAbogado()){
				LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO");
				objecto.setShowAbsolucion(true);			
				objecto.setShowClasificacion(true);
				objecto.setShowResultado(true);
				if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO)==0 ||
						objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO)==0 ||
						objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_RECONSIDERADO)==0){
					LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO - SI CERRADO || ENVIADO || RECONSIDERADO");
					objecto.setDisableAbsolucion(false);
					objecto.setDisableClasificacion(false);
					objecto.setDisableResultado(false);
					objecto.setReadOnly(true);
					if(objecto.getMotivo_reconsideracion()!=null&&!(objecto.getMotivo_reconsideracion().isEmpty())){
						LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO - SI RECONSIDERADO");
						objecto.setShowReconsideracion(true);
						objecto.setDisableReconsideracion(true);
						
					}
					if(objecto.getDescripcion_absolucion_recon()!=null&&!(objecto.getDescripcion_absolucion_recon().isEmpty())){
						LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO - SI ABSOLUCION RECONSIDERACION");
						objecto.setShowAbsolucionRecon(true);
						objecto.setDisableAbsolucionRecon(true);						
					}
					
				}else{
					LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO - NO (CERRADO ENVIADO RECONSIDERADO)");
					objecto.setShowAbsolucion(true);
					objecto.setShowReconsideracion(true);
				
					
					objecto.setDisableAbsolucion(true);
					objecto.setDisableReconsideracion(false);
					
					
					objecto.setDisableClasificacion(false);
					objecto.setDisableResultado(false);
					objecto.setReadOnly(false);
				}
				
				
				
				
//				if(objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO)==0 ||
//						objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO)==0 ||
//						objecto.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_RECONSIDERADO)==0){
//					LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO - CERRADO ENVIADO RECONSIDERADO");
//					objecto.setDisableReconsideracion(true);
//					objecto.setDisableResultado(true);
//					objecto.setDisableClasificacion(true);
//					objecto.setReadOnly(true);
//				}else{
//					LOG.info("RequerimientoServiceImp - RECLAMO - NO ABOGADO - NO (CERRADO ENVIADO RECONSIDERADO)");
//					objecto.setDisableReconsideracion(false);
//					objecto.setDisableResultado(false);
//					objecto.setDisableClasificacion(false);
//					objecto.setReadOnly(false);
//				}
			}

		}


		return objecto;
	}


}
