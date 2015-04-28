package com.ibm.bbva.ctacte.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.accpj.pagser.CtBodyRq;
import pe.com.grupobbva.accpj.pagser.CtHeader;
import pe.com.grupobbva.accpj.pagser.CtPagoServicioRq;
import pe.com.grupobbva.accpj.pagser.CtPagoServicioRs;
import pe.com.grupobbva.accpj.pagser.PagoServicio_PortProxy;
import pe.com.grupobbva.sce.qsp5.CtInqPerfilUsuarioRq;
import pe.com.grupobbva.sce.qsp5.CtInqPerfilUsuarioRs;
import pe.com.grupobbva.sce.qsp5.SCE_QSP5_PortProxy;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;

public class AyudaCobroComision {
	
	private static final Logger LOG = LoggerFactory.getLogger(AyudaCobroComision.class);
	
//	public static RespuestaServicioDTO cobroComisionInmediato(String strIdExpediente,String codEmpleadoResponsable){
//		LOG.info("Servicio Cobro Comision (strIdExpediente): " + strIdExpediente);
//		return new RespuestaServicioDTO("0", "OK");
//	}
	
	public static RespuestaServicioDTO cobroComisionInmediato(String strIdExpediente,String codEmpleadoResponsable){
		RespuestaServicioDTO respuesta;
		String strCobroComision = "-2";
		try{
			LOG.info("Servicio Cobro Comision (strIdExpediente): " + strIdExpediente);
			Expediente expediente = new Expediente();
			//ExpedienteDAO expedienteDAO = new ExpedienteDAO();
			ExpedienteDAO expedienteDAO = EJBLocator.getExpedienteDAO();
			
			int idExpediente = Integer.parseInt(strIdExpediente.trim());
			expediente = expedienteDAO.load(idExpediente);
			String strcodCentralCliente = expediente.getCliente().getCodigoCentral();
			LOG.info("strcodCentralCliente : " + strcodCentralCliente);
			String strnumCuenta = expediente.getNumeroCuentaCobro();
			LOG.info("strnumCuenta : " + strnumCuenta);
			
			LOG.info("codEmpleadoResponsable : " + codEmpleadoResponsable);
			pe.com.grupobbva.sce.qsp5.CtHeader rhCC = new pe.com.grupobbva.sce.qsp5.CtHeader();
			rhCC.setUsuario(codEmpleadoResponsable);
			LOG.info("usuCC : " + codEmpleadoResponsable);
			pe.com.grupobbva.sce.qsp5.CtBodyRq rqCC = new pe.com.grupobbva.sce.qsp5.CtBodyRq();
			rqCC.setUsuario(codEmpleadoResponsable);
			LOG.info("usuCC : " + codEmpleadoResponsable);
			CtInqPerfilUsuarioRq qCC = new CtInqPerfilUsuarioRq();
			qCC.setHeader(rhCC);
			qCC.setData(rqCC);
			LOG.info("rhCC : " + rhCC);
			
			SCE_QSP5_PortProxy servCC = new SCE_QSP5_PortProxy();
			//http://118.180.36.26:7802/sce/qsp5/
			Properties properties = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
			String urlQSP5 = properties.getProperty(ConstantesParametros.SERVICIO_QSP5);
			servCC._getDescriptor().setEndpoint(urlQSP5);
			
			LOG.info("codEmpleadoResponsable: " + codEmpleadoResponsable);
			LOG.info("Invocando callQSP5");
			CtInqPerfilUsuarioRs sCC = servCC.callQSP5(qCC);
			LOG.info("sCC.getHeader().getCodigo(): " + sCC.getHeader().getCodigo());
			LOG.info("sCC.getHeader().getDescripcion(): " + sCC.getHeader().getDescripcion());
			String codOficina = sCC.getData().getOficOperativa();
			if (sCC.getHeader().getCodigo().compareTo(ConstantesBusiness.CODIGO_OK_SERVICIO_SCE_QSP5)==0){
			
				String strCentroCosto = "";
				LOG.info("sCC : " + sCC);
				if (sCC!=null){
					strCentroCosto = sCC.getData().getOficOperativa();
					LOG.info("strCentroCosto : " + strCentroCosto);
				}
				/*Fin Obtener Centro Contable*/
				LOG.info("strCentroCosto: " + strCentroCosto);
				
				CtHeader rh = new CtHeader();
				rh.setUsuario(codEmpleadoResponsable);
				LOG.info("Servicio Cobro Comision CtHeader (usu): " + codEmpleadoResponsable);
				
				CtBodyRq rq = new CtBodyRq();
				rq.setCentroContable(strCentroCosto);
				LOG.info("Servicio Cobro Comision CtBodyRq (centroContable): " + strCentroCosto);
				rq.setCodCentralCliente(strcodCentralCliente);
				LOG.info("Servicio Cobro Comision CtBodyRq (codCentralCliente): " + strcodCentralCliente);
				rq.setDescCargo(ConstantesServicio.DESC_CARGO_SERVICIO_COBRO_COMISION);
				LOG.info("Servicio Cobro Comision CtBodyRq (descCargo): " + ConstantesServicio.DESC_CARGO_SERVICIO_COBRO_COMISION);
				rq.setNumCuenta(strnumCuenta);
				LOG.info("Servicio Cobro Comision CtBodyRq (numCuenta): " + strnumCuenta);
				rq.setOficinaUsuario(codOficina);
				LOG.info("Servicio Cobro Comision CtBodyRq (oficinaUsuario): " + codOficina);
				rq.setReferencia(strIdExpediente);
				LOG.info("Servicio Cobro Comision CtBodyRq (referencia): " + strIdExpediente);
				//rq.setRegistroUsuario(registroUsuario);
				//LOG.info("Servicio Cobro Comision CtBodyRq (registroUsuario): " + registroUsuario);
				rq.setRegistroUsuario(codEmpleadoResponsable);
				
				CtPagoServicioRq q = new CtPagoServicioRq();
				q.setHeader(rh);
				q.setData(rq);
				
				CtPagoServicioRs s = new CtPagoServicioRs();
				
				try {
					PagoServicio_PortProxy serv = new PagoServicio_PortProxy();
					//http://118.180.36.26:7820/accpj/pagser/
					String urlStr = properties.getProperty(ConstantesParametros.SERVICIO_PAGO_SERVICIO);
					LOG.info("accpj/pagser : "+urlStr);
					serv._getDescriptor().setEndpoint(urlStr); 
					
					s = serv.pagarServicio(q);
					
					strCobroComision = s.getHeader().getCodigo();
					respuesta = new RespuestaServicioDTO(s.getHeader());
					//+POR SOLICITUD BBVA+System.out..println("Pago Servicio : "+strCobroComision);
					String descError = "";
					if (!ConstantesServicio.PAGO_SERVICIO_CORRECTO.equals(strCobroComision)){
						descError = s.getHeader().getDescripcion();
					}
					LOG.info("Servicio Cobro Comision (idExpediente): " + strIdExpediente);
					LOG.info("Servicio Cobro Comision (codigoError): " + strCobroComision);
					LOG.info("Servicio Cobro Comision (descError): " + descError);
				} catch (Exception ex) {
					strCobroComision = ConstantesServicio.ERROR_COD_SERVICIO_COBRO_COMISION;
					LOG.error("Servicio Cobro Comision Error (idExpediente): " + strIdExpediente);
					LOG.error("Servicio Cobro Comision Error (codigoError): " + strCobroComision);
					LOG.error("Servicio Cobro Comision Error (descError ex): ",ex);
					respuesta = new RespuestaServicioDTO(ConstantesServicio.ERROR_COD_SERVICIO_COBRO_COMISION, "Ocurrió un error al invocar el servicio de Cobro Comisión.");
				}
			} else {
				respuesta = new RespuestaServicioDTO(sCC.getHeader());
			}
		}catch (Exception ex) {
			strCobroComision = ConstantesServicio.ERROR_COD_SERVICIO_COBRO_COMISION;
			LOG.error("Servicio Cobro Comision Error (idExpediente): " + strIdExpediente);
			LOG.error("Servicio Cobro Comision Error (codigoError): " + strCobroComision);
			LOG.error("Servicio Cobro Comision Error (descError ex): ",ex);
			respuesta = new RespuestaServicioDTO(ConstantesServicio.ERROR_COD_SERVICIO_COBRO_COMISION, "Ocurrió un error al obtener el centro de costo.");
			return respuesta;
		}
		return respuesta;
	}

}
