package com.ibm.bbva.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.session.MensajesBeanLocal;

public class UtilWebService {
	private static final Logger LOG = LoggerFactory.getLogger(UtilWebService.class);
	
	public static String validacionNumeroContradoClientePackPyme(BBVAFacadeLocal bbvaFacade, MensajesBeanLocal mensajeBean, Expediente expediente, String idEmpleado, String idSession){
		LOG.info("Validar Nro Contrato");
		String msgErrorPersonalizado = null;
		boolean validaNumContratoPackPyme = false;
		String codigoRespuesta;
		Long idMensaje = null;
		
		String strNumeroContrato=null;
		
		if (expediente.getExpedienteTC().getNroContrato()!=null && !expediente.getExpedienteTC().getNroContrato().trim().equals("")){
			strNumeroContrato=expediente.getExpedienteTC().getNroContrato();
		}else if (expediente.getExpedienteTC().getNroCta()!=null && !expediente.getExpedienteTC().getNroCta().trim().equals("")){
			strNumeroContrato=expediente.getExpedienteTC().getNroCta();
		}
		
		if(strNumeroContrato != null ){
			codigoRespuesta = bbvaFacade.validacionNumeroContradoClientePackPyme(
					strNumeroContrato, 
					expediente.getClienteNatural().getTipoDoi().getCodigoTipoDoi(), 
					expediente.getClienteNatural().getNumDoi(),
					idEmpleado,
					Util.formatDateObject("yyyy-MM-dd-HH.mm.ss.SSSSSS", new Date()),
					idSession);
			LOG.info("Response PackPyme: " + codigoRespuesta);
			
			if(codigoRespuesta!=null && codigoRespuesta.equals(com.ibm.bbva.service.Constantes.PACKPYME_RESPUESTA_CONTRATO_ENCONTRADO)){ 
				validaNumContratoPackPyme = true;
			} else if(codigoRespuesta!=null && codigoRespuesta.equals(com.ibm.bbva.service.Constantes.PACKPYME_RESPUESTA_CONTRATO_NO_CORRESPONDE_CLIENTE)){
				idMensaje = Constantes.PACKPYME_RESPUESTA_CONTRATO_NO_CORRESPONDE_CLIENTE_MSG;
			} else if(codigoRespuesta!=null && codigoRespuesta.equals(com.ibm.bbva.service.Constantes.PACKPYME_RESPUESTA_CONTRATO_NO_ENCONTRADO)){
				idMensaje = Constantes.PACKPYME_RESPUESTA_CONTRATO_NO_ENCONTRADO_MSG;
			} else if(codigoRespuesta!=null && codigoRespuesta.equals(com.ibm.bbva.service.Constantes.PACKPYME_RESPUESTA_ERROR_ACCESO)){
				idMensaje = Constantes.PACKPYME_RESPUESTA_ERROR_ACCESO_MSG;
			} 
			
			if(!validaNumContratoPackPyme && idMensaje!=null){
				Mensajes mensajePersonalizado = mensajeBean.buscarPorId(idMensaje);
				if (mensajePersonalizado == null) {
					msgErrorPersonalizado  = new String("Mensaje de Error con ID="+idMensaje+" no existe.");
				} else if (mensajePersonalizado.getContenido() == null) {
					msgErrorPersonalizado  = new String("Mensaje de Error '"+mensajePersonalizado.getDescripcion()+"' no existe.");
				} else {
					msgErrorPersonalizado  = new String(mensajePersonalizado.getContenido());
				}
				LOG.info("ErrorWS: " + codigoRespuesta + " al consultar el nro contrato: "+ strNumeroContrato);			
			}else{
				LOG.info("validacionNumeroContradoClientePackPyme::::idMensaje es nulo");
			}
		} else{
			LOG.info("Nro de contrato no indicado.");
		} 
		return msgErrorPersonalizado;
		
		
	}

}
