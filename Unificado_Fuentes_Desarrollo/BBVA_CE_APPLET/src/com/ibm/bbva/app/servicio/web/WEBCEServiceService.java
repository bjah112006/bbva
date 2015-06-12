
package com.ibm.bbva.app.servicio.web;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "WEBCEServiceService", targetNamespace = "http://web.servicio.ctacte.bbva.ibm.com", wsdlLocation = "file:/C:/Documents%20and%20Settings/Administrator/Desktop/WEBCEService.wsdl")
public class WEBCEServiceService
    extends Service
{

    private final static URL WEBCESERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException WEBCESERVICESERVICE_EXCEPTION;
    private final static QName WEBCESERVICESERVICE_QNAME = new QName("http://web.servicio.ctacte.bbva.ibm.com", "WEBCEServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Documents%20and%20Settings/Administrator/Desktop/WEBCEService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WEBCESERVICESERVICE_WSDL_LOCATION = url;
        WEBCESERVICESERVICE_EXCEPTION = e;
    }

    public WEBCEServiceService() {
        super(__getWsdlLocation(), WEBCESERVICESERVICE_QNAME);
    }

    public WEBCEServiceService(URL wsdlLocation) {
        super(wsdlLocation, WEBCESERVICESERVICE_QNAME);
    }

    public WEBCEServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns WEBCEService
     */
    @WebEndpoint(name = "WEBCEService")
    public WEBCEService getWEBCEService() {
        return super.getPort(new QName("http://web.servicio.ctacte.bbva.ibm.com", "WEBCEService"), WEBCEService.class);
    }

    private static URL __getWsdlLocation() {
        if (WEBCESERVICESERVICE_EXCEPTION!= null) {
            throw WEBCESERVICESERVICE_EXCEPTION;
        }
        return WEBCESERVICESERVICE_WSDL_LOCATION;
    }

}
