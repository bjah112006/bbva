
package com.ibm.bbva.app.servicio.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.ibm.bbva.cm.bean.Documento;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebService(name = "WEBCEService", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com")
@XmlSeeAlso({
    com.ibm.bbva.cm.bean.ObjectFactory.class,
    com.ibm.bbva.app.servicio.web.ObjectFactory.class
})
public interface WEBCEService {


    /**
     * 
     * @param nombreArchivo
     * @param tipoDocumento
     * @param idCM
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_Obtener_documentoxNombreArchivo", action = "CM_Obtener_documentoxNombreArchivo")
    @WebResult(name = "CM_Obtener_documentoxNombreArchivoReturn", targetNamespace = "")
    @RequestWrapper(localName = "CM_Obtener_documentoxNombreArchivo", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxNombreArchivo")
    @ResponseWrapper(localName = "CM_Obtener_documentoxNombreArchivoResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxNombreArchivoResponse")
    public Documento cmObtenerDocumentoxNombreArchivo(
        @WebParam(name = "tipoDocumento", targetNamespace = "")
        String tipoDocumento,
        @WebParam(name = "nombreArchivo", targetNamespace = "")
        String nombreArchivo,
        @WebParam(name = "idCM", targetNamespace = "")
        Integer idCM);

    /**
     * 
     * @param codCliente
     * @param codigoDocumento
     * @param cantDocumento
     * @return
     *     returns com.ibm.bbva.ctacte.servicio.web.ArrayOf412342274NillableDocumento
     */
    @WebMethod(operationName = "CM_historialDocumentoxCliente", action = "CM_historialDocumentoxCliente")
    @WebResult(name = "CM_historialDocumentoxClienteReturn", targetNamespace = "")
    @RequestWrapper(localName = "CM_historialDocumentoxCliente", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMHistorialDocumentoxCliente")
    @ResponseWrapper(localName = "CM_historialDocumentoxClienteResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMHistorialDocumentoxClienteResponse")
    public ArrayOf412342274NillableDocumento cmHistorialDocumentoxCliente(
        @WebParam(name = "codigoDocumento", targetNamespace = "")
        String codigoDocumento,
        @WebParam(name = "codCliente", targetNamespace = "")
        String codCliente,
        @WebParam(name = "cantDocumento", targetNamespace = "")
        int cantDocumento);

    /**
     * 
     * @param codigoDocumento
     * @param idExpediente
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_ObtenerDocumentItemType", action = "CM_ObtenerDocumentItemType")
    @WebResult(name = "CM_ObtenerDocumentItemTypeReturn", targetNamespace = "")
    @RequestWrapper(localName = "CM_ObtenerDocumentItemType", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentItemType")
    @ResponseWrapper(localName = "CM_ObtenerDocumentItemTypeResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentItemTypeResponse")
    public Documento cmObtenerDocumentItemType(
        @WebParam(name = "codigoDocumento", targetNamespace = "")
        String codigoDocumento,
        @WebParam(name = "idExpediente", targetNamespace = "")
        Integer idExpediente);

    /**
     * 
     * @param idExpediente
     * @param idDocumento
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_Obtener_documento", action = "CM_Obtener_documento")
    @WebResult(name = "CM_Obtener_documentoReturn", targetNamespace = "")
    @RequestWrapper(localName = "CM_Obtener_documento", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumento")
    @ResponseWrapper(localName = "CM_Obtener_documentoResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoResponse")
    public Documento cmObtenerDocumento(
        @WebParam(name = "idDocumento", targetNamespace = "")
        Integer idDocumento,
        @WebParam(name = "idExpediente", targetNamespace = "")
        Integer idExpediente);

    /**
     * 
     * @param codigoDocumento
     * @param idExpediente
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_Obtener_documentoxCodigo", action = "CM_Obtener_documentoxCodigo")
    @WebResult(name = "CM_Obtener_documentoxCodigoReturn", targetNamespace = "")
    @RequestWrapper(localName = "CM_Obtener_documentoxCodigo", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxCodigo")
    @ResponseWrapper(localName = "CM_Obtener_documentoxCodigoResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxCodigoResponse")
    public Documento cmObtenerDocumentoxCodigo(
        @WebParam(name = "codigoDocumento", targetNamespace = "")
        String codigoDocumento,
        @WebParam(name = "idExpediente", targetNamespace = "")
        Integer idExpediente);

}
