//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.service;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "ContentServiceImplDelegate", targetNamespace = "http://impl.service.cm.bbva.ibm.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ContentServiceImplDelegate {


    /**
     * 
     * @param documento
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "delete", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.Delete")
    @ResponseWrapper(localName = "deleteResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.DeleteResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/deleteRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/deleteResponse")
    public String delete(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento);

    /**
     * 
     * @param documentos
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deleteAll", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.DeleteAll")
    @ResponseWrapper(localName = "deleteAllResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.DeleteAllResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/deleteAllRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/deleteAllResponse")
    public String deleteAll(
        @WebParam(name = "documentos", targetNamespace = "")
        List<Documento> documentos);

    /**
     * 
     * @param documento
     * @return
     *     returns com.ibm.bbva.cm.service.Documento
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "find", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.Find")
    @ResponseWrapper(localName = "findResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.FindResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/findRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/findResponse")
    public Documento find(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento);

    /**
     * 
     * @param documento
     * @return
     *     returns java.util.List<com.ibm.bbva.cm.service.Documento>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findAll", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.FindAll")
    @ResponseWrapper(localName = "findAllResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.FindAllResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/findAllRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/findAllResponse")
    public List<Documento> findAll(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento);

    /**
     * 
     * @param documento
     * @param mimeType
     * @return
     *     returns com.ibm.bbva.cm.service.Documento
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findAsImage", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.FindAsImage")
    @ResponseWrapper(localName = "findAsImageResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.FindAsImageResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/findAsImageRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/findAsImageResponse")
    public Documento findAsImage(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento,
        @WebParam(name = "mimeType", targetNamespace = "")
        String mimeType);

    /**
     * 
     * @param documento
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "insert", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.Insert")
    @ResponseWrapper(localName = "insertResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.InsertResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/insertRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/insertResponse")
    public String insert(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento);

    /**
     * 
     * @param documento
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "insert_PID")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "insert_PID", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.InsertPID")
    @ResponseWrapper(localName = "insert_PIDResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.InsertPIDResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/insert_PIDRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/insert_PIDResponse")
    public String insertPID(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento);

    /**
     * 
     * @param documentos
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "insertAll", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.InsertAll")
    @ResponseWrapper(localName = "insertAllResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.InsertAllResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/insertAllRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/insertAllResponse")
    public String insertAll(
        @WebParam(name = "documentos", targetNamespace = "")
        List<Documento> documentos);

    /**
     * 
     * @param documento
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "update", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.Update")
    @ResponseWrapper(localName = "updateResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.UpdateResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/updateRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/updateResponse")
    public String update(
        @WebParam(name = "documento", targetNamespace = "")
        Documento documento);

    /**
     * 
     * @param id
     * @param tipoDoc
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "actualizarTipoDoc", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.ActualizarTipoDoc")
    @ResponseWrapper(localName = "actualizarTipoDocResponse", targetNamespace = "http://impl.service.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.service.ActualizarTipoDocResponse")
    @Action(input = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/actualizarTipoDocRequest", output = "http://impl.service.cm.bbva.ibm.com/ContentServiceImplDelegate/actualizarTipoDocResponse")
    public String actualizarTipoDoc(
        @WebParam(name = "id", targetNamespace = "")
        Integer id,
        @WebParam(name = "tipoDoc", targetNamespace = "")
        String tipoDoc);

}