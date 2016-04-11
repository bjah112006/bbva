package com.ibm.bbva.ctacte.cm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientecontent.ClienteContentCC;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

public class ConsultaContentManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConsultaContentManager.class);
	
	public Documento[] CM_historialDocumentoxCliente(String codigoDocumento, String codCliente, int cantDocumento, DocumentoExp docExp) throws Exception{
		try{
			LOG.info("*********consltaContentManager-CM_historialDocumentoxCliente()**********");
			LOG.info("codigoDocumento-->"+codigoDocumento);
			LOG.info("codCliente-->"+codCliente);
			LOG.info("cantDocumento-->"+cantDocumento);
			Documento documento = new Documento();
			
			int numDocumentos = 0;
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("bbva.cm.wspath--> "+ParametersDelegate.getInstance().getValue("bbva.cm.wspath"));
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			
			documento.setCodCliente(codCliente);
			documento.setTipo(codigoDocumento);
			LOG.info("**********clienteContent.historialDocumentoxCliente(documento)*********");
			LOG.info("**********codigo del cliente*********"+codCliente);
			LOG.info("**********codigoDocumento*********"+codigoDocumento);
			Documento[] listTemporal = clienteContent.historialDocumentoxCliente(documento);
			Documento[] listdocumento = null;
			LOG.info("listTemporal.length-->"+listTemporal.length);
			if (listTemporal!=null){
				
				if (listTemporal.length>=1){
					if (docExp != null && docExp.getIdCm() != null && docExp.getIdCm().intValue() > 0) {
						List<Documento> listaDocHist = new ArrayList<Documento>();
						for (int i=0; i<listTemporal.length; i++) {
							// vienen ordenados ascendentemente por fecha de creación
							if (docExp.getIdCm().intValue() != listTemporal[i].getId().intValue()) {
								listaDocHist.add(listTemporal[i]);
							} else {
								break;
							}
						}
						listTemporal = listaDocHist.toArray(new Documento[0]);
					}
					
					if (cantDocumento==0){
						numDocumentos = listTemporal.length-1;
					}else if (cantDocumento==1){
						numDocumentos = 1;
					}else if(listTemporal.length>cantDocumento){
						numDocumentos = cantDocumento;
					}else if(listTemporal.length<=cantDocumento){
						numDocumentos = listTemporal.length;
					}
					listdocumento = new Documento[numDocumentos];
					// se traen los n más recientes ordenados del más reciente al más antiguo
					int j=listTemporal.length-1;
					for(int i=0;i<numDocumentos;i++){
						listdocumento[i] = listTemporal[j];
						j--;
					}
				}
			}
			return listdocumento;
			
		} catch (Exception ex) {
			LOG.info("**********CATCH CM_historialDocumentoxCliente ********");
			//Documento[] listdocumento = null;
			//return listdocumento;
			throw new Exception();
		}
	}

	public Documento CM_DocumentoxCliente(String codigoDocumento, String codCliente) throws Exception{
		try{
			
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoDocumento-->"+codigoDocumento);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codCliente-->"+codCliente);
			Documento documento = new Documento();

			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("bbva.cm.wspath--> "+ParametersDelegate.getInstance().getValue("bbva.cm.wspath"));
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			
			documento.setCodCliente(codCliente);
			documento.setTipo(codigoDocumento);
			Documento[] listTemporal = clienteContent.historialDocumentoxCliente(documento);
			Documento documentoCM = null;
			if (listTemporal!=null){
				if (listTemporal.length>0){
					documentoCM = listTemporal[listTemporal.length-1];
				}
			}
			return documentoCM;
			
		} catch (Exception ex) {
			return null;
		}
	}
	
	public Documento CM_ObtenerDocumentItemType(String codigoDocumento, Integer idExpediente){
		try{
			Documento documento = new Documento();
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			
			documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento.setId(documentoExp.getIdCm());
			documento = clienteContent.buscarDocumentoItemTypeCM(documento);
			LOG.debug("Documento Encontrado: " + documento);
			
			return documento;
		} catch (Exception ex) {
			Documento documento = new Documento();
			LOG.error("",ex);
			return documento;
		}
	}
	
	public Documento CM_ObtenerDocumentItemType(Integer idCM) {
		try{
			if (idCM == null || idCM.intValue() <= 0) 
				return new Documento();
			
			Documento documento = new Documento();
			documento.setId(idCM);
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento = clienteContent.buscarDocumentoItemTypeCM(documento);
			LOG.debug("Documento Encontrado: " + documento);
			
			return documento;
		} catch (Exception ex) {
			LOG.error("",ex);
			return new Documento();
		}
	}
	
	public Documento[] CM_ListaHistorialDocumento(Integer cantidadHistorialxDocumento, List<Documento> listdocumento, String codigoDocumento)  throws Exception{
		try{
			LOG.info("*********consltaContentManager-CM_ListaHistorialDocumento()**********");
			Documento documento = new Documento();

			int numDocumentos = 0;
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("bbva.cm.wspath--> "+ParametersDelegate.getInstance().getValue("bbva.cm.wspath"));
			Documento[] listTemporal = listdocumento.toArray(new Documento[0]);
			Documento[] listHistorialCliente = null;
			LOG.info("listTemporal.length-->"+listTemporal.length);
			if (listTemporal!=null){
				if (listTemporal.length>1){
					if (cantidadHistorialxDocumento==0){
						numDocumentos = listTemporal.length-1;
					}else if (cantidadHistorialxDocumento==1){
						numDocumentos = 1;
					}else if(listTemporal.length>cantidadHistorialxDocumento){
						numDocumentos = cantidadHistorialxDocumento;
					}else if(listTemporal.length<=cantidadHistorialxDocumento){
						numDocumentos = listTemporal.length-1;
					}
					listHistorialCliente = new Documento[numDocumentos];
					for(int i=0;i<numDocumentos;i++){
						listHistorialCliente[i] = listTemporal[i];
					}
				}
			}
			return listHistorialCliente;

		} catch (Exception ex) {
			LOG.info("**********CATCH CM_historialDocumentoxCliente ********");
			//Documento[] listdocumento = null;
			//return listdocumento;
			throw new Exception();
		}
	}
}
