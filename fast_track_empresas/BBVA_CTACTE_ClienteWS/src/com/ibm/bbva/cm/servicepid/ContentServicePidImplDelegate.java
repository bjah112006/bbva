//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.servicepid;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "ContentServicePidImplDelegate", targetNamespace = "http://impl.servicepid.cm.bbva.ibm.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ContentServicePidImplDelegate {


    /**
     * 
     * @param documento
     * @return
     *     returns java.util.List<com.ibm.bbva.cm.servicepid.DocumentoPid>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findAll", targetNamespace = "http://impl.servicepid.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.servicepid.FindAll")
    @ResponseWrapper(localName = "findAllResponse", targetNamespace = "http://impl.servicepid.cm.bbva.ibm.com/", className = "com.ibm.bbva.cm.servicepid.FindAllResponse")
    @Action(input = "http://impl.servicepid.cm.bbva.ibm.com/ContentServicePidImplDelegate/findAllRequest", output = "http://impl.servicepid.cm.bbva.ibm.com/ContentServicePidImplDelegate/findAllResponse")
    public List<DocumentoPid> findAll(
        @WebParam(name = "documento", targetNamespace = "")
        DocumentoPid documento);

}