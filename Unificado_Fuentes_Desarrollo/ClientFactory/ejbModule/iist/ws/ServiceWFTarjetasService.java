//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:27 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:17 AM(foreman)-)
//


package iist.ws;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "ServiceWFTarjetasService", targetNamespace = "http://ws.iist", wsdlLocation = "META-INF/wsdl/ServiceWFTarjetas.wsdl")
public class ServiceWFTarjetasService
    extends Service
{

    private final static URL SERVICEWFTARJETASSERVICE_WSDL_LOCATION;
    private final static WebServiceException SERVICEWFTARJETASSERVICE_EXCEPTION;
    private final static QName SERVICEWFTARJETASSERVICE_QNAME = new QName("http://ws.iist", "ServiceWFTarjetasService");

    static {
            SERVICEWFTARJETASSERVICE_WSDL_LOCATION = iist.ws.ServiceWFTarjetasService.class.getResource("/META-INF/wsdl/ServiceWFTarjetas.wsdl");
        WebServiceException e = null;
        if (SERVICEWFTARJETASSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'META-INF/wsdl/ServiceWFTarjetas.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        SERVICEWFTARJETASSERVICE_EXCEPTION = e;
    }

    public ServiceWFTarjetasService() {
        super(__getWsdlLocation(), SERVICEWFTARJETASSERVICE_QNAME);
    }

    public ServiceWFTarjetasService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SERVICEWFTARJETASSERVICE_QNAME, features);
    }

    public ServiceWFTarjetasService(URL wsdlLocation) {
        super(wsdlLocation, SERVICEWFTARJETASSERVICE_QNAME);
    }

    public ServiceWFTarjetasService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICEWFTARJETASSERVICE_QNAME, features);
    }

    public ServiceWFTarjetasService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServiceWFTarjetasService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ServiceWFTarjetas
     */
    @WebEndpoint(name = "ServiceWFTarjetas")
    public ServiceWFTarjetas getServiceWFTarjetas() {
        return super.getPort(new QName("http://ws.iist", "ServiceWFTarjetas"), ServiceWFTarjetas.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServiceWFTarjetas
     */
    @WebEndpoint(name = "ServiceWFTarjetas")
    public ServiceWFTarjetas getServiceWFTarjetas(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.iist", "ServiceWFTarjetas"), ServiceWFTarjetas.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SERVICEWFTARJETASSERVICE_EXCEPTION!= null) {
            throw SERVICEWFTARJETASSERVICE_EXCEPTION;
        }
        return SERVICEWFTARJETASSERVICE_WSDL_LOCATION;
    }

}