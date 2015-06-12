//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.reniec.consultapordni;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.grupobbva.pe.sir.ents.body.consultapordni package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ConsultaPorDNIResponse_QNAME = new QName("http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI", "consultaPorDNIResponse");
    private final static QName _ConsultaPorDNIRequest_QNAME = new QName("http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI", "consultaPorDNIRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.grupobbva.pe.sir.ents.body.consultapordni
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConsultaPorDNIRequest }
     * 
     */
    public ConsultaPorDNIRequest createConsultaPorDNIRequest() {
        return new ConsultaPorDNIRequest();
    }

    /**
     * Create an instance of {@link ConsultaPorDNIResponse }
     * 
     */
    public ConsultaPorDNIResponse createConsultaPorDNIResponse() {
        return new ConsultaPorDNIResponse();
    }

    /**
     * Create an instance of {@link DatosNacimiento }
     * 
     */
    public DatosNacimiento createDatosNacimiento() {
        return new DatosNacimiento();
    }

    /**
     * Create an instance of {@link DatosDomicilio }
     * 
     */
    public DatosDomicilio createDatosDomicilio() {
        return new DatosDomicilio();
    }

    /**
     * Create an instance of {@link RespuestaImagenes }
     * 
     */
    public RespuestaImagenes createRespuestaImagenes() {
        return new RespuestaImagenes();
    }

    /**
     * Create an instance of {@link RespuestaDNI }
     * 
     */
    public RespuestaDNI createRespuestaDNI() {
        return new RespuestaDNI();
    }

    /**
     * Create an instance of {@link RespuestaDatos }
     * 
     */
    public RespuestaDatos createRespuestaDatos() {
        return new RespuestaDatos();
    }

    /**
     * Create an instance of {@link DatosDNI }
     * 
     */
    public DatosDNI createDatosDNI() {
        return new DatosDNI();
    }

    /**
     * Create an instance of {@link DatosPersona }
     * 
     */
    public DatosPersona createDatosPersona() {
        return new DatosPersona();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaPorDNIResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI", name = "consultaPorDNIResponse")
    public JAXBElement<ConsultaPorDNIResponse> createConsultaPorDNIResponse(ConsultaPorDNIResponse value) {
        return new JAXBElement<ConsultaPorDNIResponse>(_ConsultaPorDNIResponse_QNAME, ConsultaPorDNIResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultaPorDNIRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI", name = "consultaPorDNIRequest")
    public JAXBElement<ConsultaPorDNIRequest> createConsultaPorDNIRequest(ConsultaPorDNIRequest value) {
        return new JAXBElement<ConsultaPorDNIRequest>(_ConsultaPorDNIRequest_QNAME, ConsultaPorDNIRequest.class, null, value);
    }

}
