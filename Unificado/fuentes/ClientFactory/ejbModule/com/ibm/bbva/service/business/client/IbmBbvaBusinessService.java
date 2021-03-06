//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.service.business.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "IbmBbvaBusinessService", targetNamespace = "http://business.service.bbva.ibm.com/", wsdlLocation = "META-INF/wsdl/IbmBbvaBusinessService.wsdl")
public class IbmBbvaBusinessService
    extends Service
{

    private final static URL IBMBBVABUSINESSSERVICE_WSDL_LOCATION;
    private final static WebServiceException IBMBBVABUSINESSSERVICE_EXCEPTION;
    private final static QName IBMBBVABUSINESSSERVICE_QNAME = new QName("http://business.service.bbva.ibm.com/", "IbmBbvaBusinessService");

    static {
            IBMBBVABUSINESSSERVICE_WSDL_LOCATION = com.ibm.bbva.service.business.client.IbmBbvaBusinessService.class.getResource("/META-INF/wsdl/IbmBbvaBusinessService.wsdl");
        WebServiceException e = null;
        if (IBMBBVABUSINESSSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'META-INF/wsdl/IbmBbvaBusinessService.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        IBMBBVABUSINESSSERVICE_EXCEPTION = e;
    }

    public IbmBbvaBusinessService() {
        super(__getWsdlLocation(), IBMBBVABUSINESSSERVICE_QNAME);
    }

    public IbmBbvaBusinessService(WebServiceFeature... features) {
        super(__getWsdlLocation(), IBMBBVABUSINESSSERVICE_QNAME, features);
    }

    public IbmBbvaBusinessService(URL wsdlLocation) {
        super(wsdlLocation, IBMBBVABUSINESSSERVICE_QNAME);
    }

    public IbmBbvaBusinessService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, IBMBBVABUSINESSSERVICE_QNAME, features);
    }

    public IbmBbvaBusinessService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IbmBbvaBusinessService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IbmBbvaBusinessDelegate
     */
    @WebEndpoint(name = "IbmBbvaBusinessPort")
    public IbmBbvaBusinessDelegate getIbmBbvaBusinessPort() {
        return super.getPort(new QName("http://business.service.bbva.ibm.com/", "IbmBbvaBusinessPort"), IbmBbvaBusinessDelegate.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IbmBbvaBusinessDelegate
     */
    @WebEndpoint(name = "IbmBbvaBusinessPort")
    public IbmBbvaBusinessDelegate getIbmBbvaBusinessPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://business.service.bbva.ibm.com/", "IbmBbvaBusinessPort"), IbmBbvaBusinessDelegate.class, features);
    }

    private static URL __getWsdlLocation() {
        if (IBMBBVABUSINESSSERVICE_EXCEPTION!= null) {
            throw IBMBBVABUSINESSSERVICE_EXCEPTION;
        }
        return IBMBBVABUSINESSSERVICE_WSDL_LOCATION;
    }

}
