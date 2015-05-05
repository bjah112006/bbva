package com.bbva.content.manager.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbva.content.manager.EstadoOperacion;
import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.service.impl.ContentServiceImpl;
import com.ibm.bbva.cm.support.impl.DateHelper;
import com.ibm.bbva.cm.util.Util;

@javax.jws.WebService (endpointInterface="com.bbva.content.manager.ws.ContentManagerWS", targetNamespace="http://www.example.org/ContentManagerWS/", serviceName="ContentManagerWS", portName="ContentManagerWSSOAP")
public class ContentManagerWSSOAPImpl implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ContentManagerWSSOAPImpl.class);
	private ContentServiceImpl contentServiceImpl;

    public ContentManagerWSSOAPImpl() {
    	contentServiceImpl = new ContentServiceImpl();
	}

    private ResponseHeader createResponse() {
    	ResponseHeader responseHeader = new ResponseHeader();
    	responseHeader.setCodigoEstado(EstadoOperacion.OK);
    	responseHeader.setMensaje("Operaci\u00F3n realizada con \u00E9xito");
    	return responseHeader;
    }

    private ResponseHeader createResponse(String message) {
    	ResponseHeader responseHeader = new ResponseHeader();
    	responseHeader.setCodigoEstado(EstadoOperacion.KO);
    	responseHeader.setMensaje(message);
    	return responseHeader;
    }
    
    private ResponseHeader createResponse(Exception e) {
    	ResponseHeader responseHeader = new ResponseHeader();
    	responseHeader.setCodigoEstado(EstadoOperacion.KO);
    	responseHeader.setMensaje(Util.mostrarMensajeHTML(e));
    	return responseHeader;
    }
    
    private Documento obtenerDocumento(ObtenerDocumentoRequest doc) {
    	Documento documento = new Documento();
    	documento.setId(doc.getId());
    	documento.setTipo(doc.getTipo());
    	documento.setMandatorio(null);
    	if("1".equalsIgnoreCase(doc.getMandatorio())) {
    		documento.setMandatorio(Boolean.TRUE);
    	} else if("0".equalsIgnoreCase(doc.getMandatorio())) {
    		documento.setMandatorio(Boolean.FALSE);
    	}
    	documento.setCodCliente(doc.getCodigoCliente());
    	documento.setTipoDoi(doc.getTipoDOI());
    	documento.setNumDoi(doc.getNumDOI());
    	documento.setNombreArchivo(doc.getNombreArchivo());
    	documento.setOrigen(doc.getOrigen());
    	
    	logger.info("Recibido: {}", documento.toString());
    	
    	return documento;
    }
    
    private Documento obtenerDocumento(DocumentoRequest doc) {
    	Documento documento = new Documento();
    	documento.setId(doc.getId());
    	documento.setTipo(doc.getTipo());
    	documento.setMandatorio(null);
    	if("1".equalsIgnoreCase(doc.getMandatorio())) {
    		documento.setMandatorio(Boolean.TRUE);
    	} else if("0".equalsIgnoreCase(doc.getMandatorio())) {
    		documento.setMandatorio(Boolean.FALSE);
    	}
    	documento.setCodCliente(doc.getCodigoCliente());
    	documento.setFechaCreacion(DateHelper.toCalendar(doc.getFechaCreacion()));
    	documento.setFechaExpiracion(DateHelper.toCalendar(doc.getFechaExpiracion()));
    	documento.setTipoDoi(doc.getTipoDOI());
    	documento.setNumDoi(doc.getNumDOI());
    	documento.setNombreArchivo(doc.getNombreArchivo());
    	documento.setOrigen(doc.getOrigen());
    	documento.setContenido(doc.getContenido());
    	return documento;
    }
    
    private List<Documento> obtenerDocumento(List<DocumentoRequest> docs) {
    	List<Documento> documentos = new ArrayList<Documento>();
    	for(DocumentoRequest doc : docs) {
    		documentos.add(obtenerDocumento(doc));
    	}
    	return documentos;
    }
    
    private List<DocumentoResponse> obtenerDocumentoResponse(Documento[] docs) {
    	List<DocumentoResponse> documentos = new ArrayList<DocumentoResponse>();
    	for(Documento doc : docs) {
    		documentos.add(obtenerDocumentoResponse(doc));
    	}
    	return documentos;
    }
    
    private DocumentoResponse obtenerDocumentoResponse(Documento doc) {
    	DocumentoResponse documento = new DocumentoResponse();
    	documento.setId(doc.getId());
    	documento.setTipo(doc.getTipo());
    	documento.setMandatorio(doc.getMandatorio() ? "1" : "0");
    	documento.setCodigoCliente(doc.getCodCliente());
    	documento.setFechaCreacion(DateHelper.toXMLGregorianCalendar(doc.getFechaCreacion()));
    	documento.setFechaExpiracion(DateHelper.toXMLGregorianCalendar(doc.getFechaExpiracion()));
    	documento.setTipoDOI(doc.getTipoDoi());
    	documento.setNumDOI(doc.getNumDoi());
    	documento.setNombreArchivo(doc.getNombreArchivo());
    	documento.setOrigen(doc.getOrigen());
    	documento.setUrlContent(doc.getUrlContent());
    	return documento;
    }
	
    // ******************************************************************************************* //
	// Metodos Expuestos                                                                           //
    // ******************************************************************************************* //
    public ObtenerResponse obtener(ObtenerRequest parameters) {
    	ObtenerResponse obtenerResponse = new ObtenerResponse();
        try {
        	if(parameters.getBody() == null || parameters.getBody().getDocumento().isEmpty()) {
        		obtenerResponse.setHeader(createResponse("Datos inv\u00E1lidos o sin documentos a insertar"));
        	} else {
	        	obtenerResponse.setHeader(createResponse());
	        	List<ObtenerDocumentoRequest> documentos = parameters.getBody().getDocumento();
	        	for(ObtenerDocumentoRequest doc : documentos) {
	        		if(doc.getCodigoCliente() == null || doc.getCodigoCliente().isEmpty()) {
	        			if(doc.getTipoDOI() == null || doc.getTipoDOI().isEmpty() || doc.getNumDOI() == null || doc.getNumDOI().isEmpty()) {
	        				throw new NullPointerException("De ingresar el c\u00F3digo de cliente o tipo y nro. de documento");
	        			}
	        		}
	        		
	        		Documento[] docs = contentServiceImpl.findAll(obtenerDocumento(doc));
	        		obtenerResponse.getBody().getDocumentos().addAll(obtenerDocumentoResponse(docs));
	        	}
        	}
        } catch(Exception e) {
        	obtenerResponse.setHeader(createResponse(e));
        }
        
        return obtenerResponse;
    }

    public InsertarResponse insertar(InsertarRequest parameters) {
    	InsertarResponse response = new InsertarResponse();
        try {
        	if(parameters.getBody() == null || parameters.getBody().getDocumento().isEmpty()) {
        		response.setHeader(createResponse("Datos inv\u00E1lidos o sin documentos a insertar"));
        	} else {
        		logger.info("Elementos: {}", parameters.getBody().getDocumento().size());
            	response.setHeader(createResponse());
            	contentServiceImpl.insertAll(obtenerDocumento(parameters.getBody().getDocumento()));
            	
            	for(DocumentoRequest doc : parameters.getBody().getDocumento()) {
            		Documento[] docs = contentServiceImpl.findAll(obtenerDocumento(doc));
            		response.getBody().getDocumentos().addAll(obtenerDocumentoResponse(docs));
            	}
        	}
        } catch(Exception e) {
        	response.setHeader(createResponse(e));
        }
        
        return response;
    }

//    public ActualizarResponse actualizar(ActualizarRequest parameters) {
//    	ActualizarResponse response = new ActualizarResponse();
//    	Documento documento;
//        try {
//        	if(parameters.getBody() == null || parameters.getBody().getDocumento().isEmpty()) {
//        		response.setHeader(createResponse("Datos inv\u00E1lidos o sin documentos a actualizar"));
//        	} else {
//        		logger.info("Elementos: {}", parameters.getBody().getDocumento().size());        		
//	        	response.setHeader(createResponse());
//	        	
//	        	for(DocumentoRequest doc : parameters.getBody().getDocumento()) {
//	        		documento = obtenerDocumento(doc);
//	        		contentServiceImpl.update(documento);
//	        		Documento[] docs = contentServiceImpl.findAll(documento);
//	        		response.getBody().getDocumentos().addAll(obtenerDocumentoResponse(docs));
//	        	}
//        	}
//        } catch(Exception e) {
//        	response.setHeader(createResponse(e));
//        }
//        
//        return response;
//    }
//
//    public EliminarResponse eliminar(EliminarRequest parameters) {
//    	EliminarResponse response = new EliminarResponse();
//        try {
//        	if(parameters.getBody() == null || parameters.getBody().getDocumento().isEmpty()) {
//        		response.setHeader(createResponse("Datos inv\u00E1lidos o sin documentos a eliminar"));
//        	} else {
//	        	response.setHeader(createResponse());
//	        	contentServiceImpl.deleteAll(obtenerDocumento(parameters.getBody().getDocumento()));
//        	}
//        } catch(Exception e) {
//        	response.setHeader(createResponse(e));
//        }
//        
//        return response;
//    }

}