package com.ibm.bbva.ctacte.servicio.sfp.servicio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientecontent.ClienteContent;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.servicio.sfp.bean.CuentaSTD;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMActivacionOUTResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMActivacionResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMActivacionSETRequest;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMAsociarFirmaRequest;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMAsociarFirmaResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoINResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoSETRequest;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoSETResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.ParticipeSTD;
import com.ibm.bbva.ctacte.servicio.sfp.util.Constantes;
import com.ibm.bbva.ctacte.servicio.sfp.util.SFPUtil;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;


public class SFPExternal {
	
	private static final Logger LOG = LoggerFactory.getLogger(SFPExternal.class);
	
	public ParticipeSTD[] IBMAsociarFirmaGET(String IDExpediente){
		//Select a la tabla participe
		try{
			LOG.info("Inicio : IBMAsociarFirmaGET");
			ParticipeDAO participeDAO = EJBLocator.getParticipeDAO();
			ParticipeSTD participeSTD = new ParticipeSTD();
			SFPUtil sfpUtil = new SFPUtil();
			int idExpediente = Integer.parseInt(IDExpediente);
			LOG.info("idExpediente : " + idExpediente);
			List<Participe> listParticipeUnicos = participeDAO.findByExpedienteParticipesUnicos(idExpediente);
			LOG.info("listParticipeUnicos.size() : " + listParticipeUnicos.size());
			ParticipeSTD[] listParticipeSTD = new ParticipeSTD[listParticipeUnicos.size()];
			for (int i=0;i<listParticipeUnicos.size();i++){
				Participe partTemp = listParticipeUnicos.get(i);
				LOG.info("***CodigoParticipe: {}", partTemp.getCodigoCentral());
				participeSTD = sfpUtil.copiarDatosParticipeSTD(partTemp);
				listParticipeSTD[i] = participeSTD;
			}
			return listParticipeSTD;

		} catch (Exception ex) {
			ParticipeSTD[] listParticipeSTD = new ParticipeSTD[1];
			LOG.error("",ex);
			return listParticipeSTD;
		}
		
	}

	public Documento CM_Obtener_documento_FRF(String IDExpediente){
		try{
			Documento documento = new Documento();
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			int idExpediente = Integer.parseInt(IDExpediente);
			documentoExp = documentoExpDAO.findByCodDocExp(Constantes.CODIGO_DOCUMENTO_FRDF, idExpediente);
			LOG.debug("DocumentoExp: " + documentoExp.getIdCm());
			LOG.info("URL Content Manager: " + ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			ClienteContent clienteContent = new ClienteContent(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento.setId(documentoExp.getIdCm());
			documento = clienteContent.buscarDocumentoCM(documento);
			LOG.debug("Documento Encontrado: " + documento);
			
			return documento;
		} catch (Exception ex) {
			Documento documento = new Documento();
			LOG.error("",ex);
			return documento;
		}
	}
	
	public IBMAsociarFirmaResponse IBMAsociarFirmaSET(String IDExpediente, IBMAsociarFirmaRequest[] participes){
		//Update a la tabla participe
		try{
			LOG.debug("***Metodo IBMAsociarFirmaSET SFPExternal***");
			IBMAsociarFirmaResponse response = new IBMAsociarFirmaResponse(true);
			IBMAsociarFirmaRequest participeAsociacion = new IBMAsociarFirmaRequest();
			//UPDATE a la tabla con los datos del request
			ParticipeDAO participeDAO = EJBLocator.getParticipeDAO();
			int idExpediente = Integer.parseInt(IDExpediente);
			List<Participe> listParticipe = participeDAO.findByExpedienteParticipes(idExpediente);
			Participe participe = new Participe();
			String flagFirmaAsociacion = "0";
			for (int i=0;i<participes.length;i++){
				participeAsociacion = participes[i];
				flagFirmaAsociacion = participeAsociacion.getIndicadorAsociacion().trim();
				LOG.debug("CodCentral: " + participeAsociacion.getCodCentral());
				LOG.debug("flagFirmaAsociacion: " + flagFirmaAsociacion);
				for(int j=0;j<listParticipe.size();j++){
					participe = listParticipe.get(j);
					if (participeAsociacion.getCodCentral().toString().compareToIgnoreCase(participe.getCodigoCentral())==0){
						LOG.debug("entro if comparacion IBMAsociarFirmaSET");
						participe.setFlagFirmaAsociada(flagFirmaAsociacion);
						participeDAO.update(participe);
					}
				}
			}
			return response;
		} catch (Exception ex) {
			IBMAsociarFirmaResponse response = new IBMAsociarFirmaResponse(false);
			LOG.error("Erros en método: IBMAsociarFirmaSET");
			LOG.error("",ex);
			return response;
		}
	}
	
	public IBMRealizarBastanteoINResponse IBMRealizarBastanteoGET(String IDExpediente){
		
		try{
			LOG.debug("***Metodo IBMRealizarBastanteoGET SFPExternal***");
			LOG.info("IDExpediente: "+IDExpediente);
			IBMRealizarBastanteoINResponse ibmRealizarBastanteoINResponse = new IBMRealizarBastanteoINResponse();
			SFPUtil sfpUtil = new SFPUtil();
			ExpedienteDAO expedienteDAO = EJBLocator.getExpedienteDAO();
			Expediente expediente = new Expediente();
			int idExpediente = Integer.parseInt(IDExpediente);
			expediente = expedienteDAO.load(idExpediente);
			ibmRealizarBastanteoINResponse = sfpUtil.copiarDatosBastanteo(expediente);
			
			CuentaDAO cuentaDAO = EJBLocator.getCuentaDAO();
			Cuenta cuenta = new Cuenta();
			CuentaSTD cuentaSTD = new CuentaSTD();
			
			ExpedienteCuentaDAO expedienteCuentaDAO = EJBLocator.getExpedienteCuentaDAO();
			List<ExpedienteCuenta> listExpedienteCuenta = expedienteCuentaDAO.findListaExpedienteCuenta(idExpediente);
			
			CuentaSTD[] listCuentaSTD = new CuentaSTD[listExpedienteCuenta.size()];
			LOG.info("Se encontraron "+listExpedienteCuenta.size()+"cuentas para el expediente.");
			
			for(int i=0;i<listExpedienteCuenta.size();i++){
				cuenta = cuentaDAO.load(listExpedienteCuenta.get(i).getIdCuenta());
				LOG.info("IdCuenta: "+cuenta.getId());
				cuentaSTD = sfpUtil.copiarDatosCuentaSTD(cuenta);
				
				Participe participe = new Participe();
				ParticipeDAO participeDAO = EJBLocator.getParticipeDAO();
				List<Participe> listParticipe = participeDAO.findByCuentaParticipes(listExpedienteCuenta.get(i).getIdCuenta());
				
				ParticipeSTD[] listParticipeSTD = new ParticipeSTD[listParticipe.size()];
				LOG.info("Se encontraron "+listParticipe.size()+"partícipes para la cuenta.");
				ParticipeSTD participeSTD = new ParticipeSTD();
				for(int j=0;j<listParticipe.size();j++){
					participe = listParticipe.get(j);
					LOG.info("IdParticipe: "+participe.getId());
					participeSTD = sfpUtil.copiarDatosParticipeSTD(participe);
					listParticipeSTD[j] = participeSTD;
				}
				
				cuentaSTD.setParticipeXCuenta(listParticipeSTD);
				listCuentaSTD[i] = cuentaSTD;
			}
			ibmRealizarBastanteoINResponse.setListaCuentas(listCuentaSTD);
			
			
			
			return ibmRealizarBastanteoINResponse;
			
		} catch (Exception ex) {
			
			IBMRealizarBastanteoINResponse ibmRealizarBastanteoINResponse = new IBMRealizarBastanteoINResponse();
			LOG.error("Error en método IBMRealizarBastanteoGET",ex);
			return ibmRealizarBastanteoINResponse;
		}
		
	}
	
	public IBMRealizarBastanteoSETResponse IBMRealizarBastanteoSET(IBMRealizarBastanteoSETRequest request){
		//Update a la tabla Expediente por Resultado Bastanteo
		try{
			LOG.debug("***IBMRealizarBastanteoSET***");
			IBMRealizarBastanteoSETResponse response = new IBMRealizarBastanteoSETResponse(true);
			ExpedienteDAO expedienteDAO = EJBLocator.getExpedienteDAO();
			Expediente expediente = new Expediente();
			int idExpediente = Integer.parseInt(request.getIdExpediente());
			expediente = expedienteDAO.load(idExpediente);
			String flagFacultadesEspeciales = request.getIndicadorFacultades().trim();
			expediente.setFlagFacultadesEspeciales(flagFacultadesEspeciales);
			String flagIndicadorGrabacion = request.getIndicadorGrabacion().trim();
			expediente.setFlagIndicadorBastanteo(flagIndicadorGrabacion);
			expediente.setDescripcionMensajeBastanteo(request.getDescripcionError().toString());
			LOG.debug("flagFacultadesEspeciales: " + flagFacultadesEspeciales);
			LOG.debug("flagIndicadorGrabacion: " + flagIndicadorGrabacion);
			LOG.debug("expediente.setDescripcionMensajeBastanteo: " + expediente.getDescripcionMensajeBastanteo());
			expedienteDAO.update(expediente);
			
			return response;
			
		} catch (Exception ex) {
			IBMRealizarBastanteoSETResponse response = new IBMRealizarBastanteoSETResponse(false);
			LOG.error("Error en método: IBMRealizarBastanteoSET");
			LOG.error("",ex);
			return response;
		}
	}
	
	public IBMActivacionResponse IBMActivacionGET(String IDExpediente){
		IBMActivacionResponse response = new IBMActivacionResponse(true); 
		//*******************************************************************
		//*****************NO REQUIERE IMPLEMENTACION************************
		return response;
	}
	
	public IBMActivacionOUTResponse IBMActivacionSET(String IDExpediente, IBMActivacionSETRequest[] participes){
		IBMActivacionOUTResponse response = new IBMActivacionOUTResponse(true);
		//*******************************************************************
		//*****************NO REQUIERE IMPLEMENTACION************************
		//UPDATE 
		//Agreegar descripcion de error en la tabla
		return response;
	}
}
