package com.ibm.bbva.ctacte.servicio.web;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientecontent.ClienteContent;
import clientecontent.ClienteContentCC;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.cm.servicepid.DocumentoPid;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.servicio.util.ListSorter;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;


public class WEBCEService {

	private static final Logger LOG = LoggerFactory.getLogger(WEBCEService.class);
	
	public Documento CM_Obtener_documento(Integer idDocumento, Integer idExpediente){
		try{
			Documento documento = new Documento();
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			
			documentoExp = documentoExpDAO.findByDocExp(idDocumento, idExpediente);
			ClienteContent clienteContent = new ClienteContent(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento.setId(documentoExp.getIdCm());
			documento = clienteContent.buscarDocumentoCM(documento);
			LOG.debug("Documento Encontrado: " + documento);
			
			return documento;
		} catch (Exception ex) {
			Documento documento = new Documento();
			LOG.error("Error en CM_Obtener_documento("+idDocumento+","+idExpediente+")",ex);
			return documento;
		}
	}
	
	public Documento CM_Obtener_documentoxCodigo(String codigoDocumento, Integer idExpediente){
		try{
			Documento documento = new Documento();
			DocumentoExp documentoExp = new DocumentoExp();
			DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
			
			LOG.info("codigoDocumento : " + codigoDocumento);
			LOG.info("idExpediente : " + idExpediente);
			
			documentoExp = documentoExpDAO.findByCodDocExp(codigoDocumento, idExpediente);
			
			LOG.info("documentoExp : " + documentoExp.getIdCm());
			
			//ClienteContent clienteContent = new ClienteContent(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento.setId(documentoExp.getIdCm());
			documento = clienteContent.buscarDocumentoItemTypeCM(documento);
			LOG.debug("Documento Encontrado: " + documento);
			
			return documento;
		} catch (Exception ex) {
			Documento documento = new Documento();
			LOG.error("Error en CM_Obtener_documentoxCodigo("+codigoDocumento+","+idExpediente+")",ex);
			return documento;
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
			LOG.error("Error en CM_ObtenerDocumentItemType("+codigoDocumento+","+idExpediente+")",ex);
			return documento;
		}
	}
	
	public Documento[] CM_historialDocumentoxCliente(String codigoDocumento, String codCliente, int cantDocumento){
		try{
			
			Documento documento = new Documento();
			
			int numDocumentos = 0;
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento.setCodCliente(codCliente);
			documento.setTipo(codigoDocumento);
			LOG.debug("Documento a buscar en historico: " + documento);
			Documento[] listTemporal = clienteContent.historialDocumentoxCliente(documento);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("listTemporal: " + listTemporal);
			Documento[] listdocumento = null;
			if (listTemporal!=null){
				if (listTemporal.length>1){
					if (cantDocumento==0){
						numDocumentos = listTemporal.length-1;
					}else if (cantDocumento==1){
						numDocumentos = 1;
					}else if(listTemporal.length>cantDocumento){
						numDocumentos = cantDocumento;
					}else if(listTemporal.length<=cantDocumento){
						numDocumentos = listTemporal.length-1;
					}
					listdocumento = new Documento[numDocumentos];
					for(int i=0;i<numDocumentos;i++){
						listdocumento[i] = listTemporal[i];
					}
				}
			}
			return listdocumento;
			
		} catch (Exception ex) {
			Documento[] listdocumento = null;
			LOG.error("Error en CM_historialDocumentoxCliente("+codigoDocumento+","+codCliente+","+cantDocumento+")",ex);
			return listdocumento;
		}
	}

	public Documento CM_Obtener_documentoxNombreArchivo(String tipoDocumento, String nombreArchivo, Integer idCM){
		try{
			Documento documento = new Documento();					
			
			//ClienteContent clienteContent = new ClienteContent(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
			documento.setTipo(tipoDocumento);
			//documento.setNombreArchivo(nombreArchivo);//ignoro el nombre de archivo porque no es igual en BD que en content y además el idCM es suficiente
			documento.setId(idCM);
			documento = clienteContent.buscarDocumentoItemTypeCM(documento);
			LOG.debug("Documento Encontrado: " + documento);
			
			return documento;
		} catch (Exception ex) {
			Documento documento = new Documento();
			LOG.error("Error en CM_Obtener_documentoxNombreArchivo("+tipoDocumento+","+nombreArchivo+","+idCM+")",ex);
			return documento;
		}
	}	
	
	public List<DocumentoPid> CM_buscarVistaUnica(String codCliente){	
        try{               
    		LOG.info("***********TRY -WEBCEService-CM_buscarVistaUnica(String codCliente)***************"+codCliente);    
    		DocumentoPid documento = new DocumentoPid();  
            ClienteContentCC clienteContent = new ClienteContentCC(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT_PID));
            documento.setCodCliente(codCliente);
            LOG.debug("Documento a buscar !!! : " + documento);
            
            DocumentoPid[] listTemporal = clienteContent.historialDocumentoxCliente(documento);
            LOG.info("listTemporal VISTA UNICA: " + listTemporal);
            List<DocumentoPid> listaDocs = Arrays.asList(listTemporal);
            ListSorter.ordenar(listaDocs, false, "tipo","fechaCreacion");
            return listaDocs;
                
        } catch (Exception ex) {
        	LOG.error("Error en CM_buscarVistaUnica("+codCliente+")",ex);
            //throw new ServiceLocationException(e);
            return null;
        }
	}
	
}
