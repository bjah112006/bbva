//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.ctacte.servicio.web;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.bean.DocumentoPid;

@WebService(name = "WEBCEServiceDelegate", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/")
@XmlSeeAlso({
    com.ibm.bbva.cm.bean.ObjectFactory.class,
    com.ibm.bbva.ctacte.servicio.web.ObjectFactory.class
})
public interface WEBCEServiceDelegate {


    /**
     * 
     * @param idExpediente
     * @param idDocumento
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_Obtener_documento")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "CM_Obtener_documento", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumento")
    @ResponseWrapper(localName = "CM_Obtener_documentoResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoResponse")
    @Action(input = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_Obtener_documentoRequest", output = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_Obtener_documentoResponse")
    public Documento cmObtenerDocumento(
        @WebParam(name = "idDocumento", targetNamespace = "")
        Integer idDocumento,
        @WebParam(name = "idExpediente", targetNamespace = "")
        Integer idExpediente);

    /**
     * 
     * @param idExpediente
     * @param codigoDocumento
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_Obtener_documentoxCodigo")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "CM_Obtener_documentoxCodigo", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxCodigo")
    @ResponseWrapper(localName = "CM_Obtener_documentoxCodigoResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxCodigoResponse")
    @Action(input = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_Obtener_documentoxCodigoRequest", output = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_Obtener_documentoxCodigoResponse")
    public Documento cmObtenerDocumentoxCodigo(
        @WebParam(name = "codigoDocumento", targetNamespace = "")
        String codigoDocumento,
        @WebParam(name = "idExpediente", targetNamespace = "")
        Integer idExpediente);

    /**
     * 
     * @param idExpediente
     * @param codigoDocumento
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_ObtenerDocumentItemType")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "CM_ObtenerDocumentItemType", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentItemType")
    @ResponseWrapper(localName = "CM_ObtenerDocumentItemTypeResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentItemTypeResponse")
    @Action(input = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_ObtenerDocumentItemTypeRequest", output = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_ObtenerDocumentItemTypeResponse")
    public Documento cmObtenerDocumentItemType(
        @WebParam(name = "codigoDocumento", targetNamespace = "")
        String codigoDocumento,
        @WebParam(name = "idExpediente", targetNamespace = "")
        Integer idExpediente);

    /**
     * 
     * @param cantDocumento
     * @param codCliente
     * @param codigoDocumento
     * @return
     *     returns java.util.List<com.ibm.bbva.cm.bean.Documento>
     */
    @WebMethod(operationName = "CM_historialDocumentoxCliente")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "CM_historialDocumentoxCliente", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMHistorialDocumentoxCliente")
    @ResponseWrapper(localName = "CM_historialDocumentoxClienteResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMHistorialDocumentoxClienteResponse")
    @Action(input = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_historialDocumentoxClienteRequest", output = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_historialDocumentoxClienteResponse")
    public List<Documento> cmHistorialDocumentoxCliente(
        @WebParam(name = "codigoDocumento", targetNamespace = "")
        String codigoDocumento,
        @WebParam(name = "codCliente", targetNamespace = "")
        String codCliente,
        @WebParam(name = "cantDocumento", targetNamespace = "")
        int cantDocumento);

    /**
     * 
     * @param tipoDocumento
     * @param nombreArchivo
     * @param idCM
     * @return
     *     returns com.ibm.bbva.cm.bean.Documento
     */
    @WebMethod(operationName = "CM_Obtener_documentoxNombreArchivo")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "CM_Obtener_documentoxNombreArchivo", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxNombreArchivo")
    @ResponseWrapper(localName = "CM_Obtener_documentoxNombreArchivoResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMObtenerDocumentoxNombreArchivoResponse")
    @Action(input = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_Obtener_documentoxNombreArchivoRequest", output = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_Obtener_documentoxNombreArchivoResponse")
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
     * @return
     *     returns java.util.List<com.ibm.bbva.cm.bean.DocumentoPid>
     */
    @WebMethod(operationName = "CM_buscarVistaUnica")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "CM_buscarVistaUnica", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMBuscarVistaUnica")
    @ResponseWrapper(localName = "CM_buscarVistaUnicaResponse", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com/", className = "com.ibm.bbva.ctacte.servicio.web.CMBuscarVistaUnicaResponse")
    @Action(input = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_buscarVistaUnicaRequest", output = "http://web.servicio.ctacte.bbva.ibm.com/WEBCEServiceDelegate/CM_buscarVistaUnicaResponse")
    public List<DocumentoPid> cmBuscarVistaUnica(
        @WebParam(name = "codCliente", targetNamespace = "")
        String codCliente);

}
