//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package pe.com.grupobbva.accpj.pagser;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(name = "PagoServicio_PortType", targetNamespace = "http://grupobbva.com.pe/accpj/pagser/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PagoServicioPortType {


    /**
     * 
     * @param pagoServicioRq
     * @return
     *     returns pe.com.grupobbva.accpj.pagser.CtPagoServicioRs
     */
    @WebMethod
    @WebResult(name = "PagoServicioRs", targetNamespace = "http://grupobbva.com.pe/accpj/pagser/", partName = "PagoServicioRs")
    public CtPagoServicioRs pagarServicio(
        @WebParam(name = "PagoServicioRq", targetNamespace = "http://grupobbva.com.pe/accpj/pagser/", partName = "PagoServicioRq")
        CtPagoServicioRq pagoServicioRq);

}