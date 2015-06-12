//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.harec.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(name = "HarecService", targetNamespace = "http://grupobbva.com.pe/HarecService/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface HarecService {


    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs
     */
    @WebMethod(operationName = "ObtenerDatosXPersona", action = "http://grupobbva.com.pe/HarecService/ObtenerDatosXPersona")
    @WebResult(name = "ObtenerDatosXPersonaResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public DatosGeneralesXpersonaRs obtenerDatosXPersona(
        @WebParam(name = "ObtenerDatosXPersona", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        DatosGeneralesXPersonaRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.DireccionesXPersonaRs
     */
    @WebMethod(operationName = "ObtenerDireccionesXPersona", action = "http://grupobbva.com.pe/HarecService/ObtenerDireccionesXPersona")
    @WebResult(name = "ObtenerDireccionesXPersonaResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public DireccionesXPersonaRs obtenerDireccionesXPersona(
        @WebParam(name = "ObtenerDireccionesXPersona", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        DireccionesXPersonaRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.RelacionesContractualesRs
     */
    @WebMethod(operationName = "ObtenerContratosXPersona", action = "http://grupobbva.com.pe/HarecService/ObtenerContratosXPersona")
    @WebResult(name = "ObtenerContratosXPersonaResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public RelacionesContractualesRs obtenerContratosXPersona(
        @WebParam(name = "ObtenerContratosXPersona", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        RelacionesContractualesRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.MovimientosXContratoRs
     */
    @WebMethod(operationName = "ObtenerMovimientosXContratoCP", action = "http://grupobbva.com.pe/HarecService/ObtenerMovimientosXContratoCP")
    @WebResult(name = "ObtenerMovimientosXContratoCPResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public MovimientosXContratoRs obtenerMovimientosXContratoCP(
        @WebParam(name = "ObtenerMovimientosXContratoCP", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        MovimientosXContratoRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.DatosXContratoRs
     */
    @WebMethod(operationName = "ObtenerDatosXContrato", action = "http://grupobbva.com.pe/HarecService/ObtenerDatosXContrato")
    @WebResult(name = "ObtenerDatosXContratoResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public DatosXContratoRs obtenerDatosXContrato(
        @WebParam(name = "ObtenerDatosXContrato", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        DatosXContratoRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.MovimientoCPRs
     */
    @WebMethod(operationName = "RegistrarMovimientoCP", action = "http://grupobbva.com.pe/HarecService/RegistrarMovimientoCP")
    @WebResult(name = "RegistrarMovimientoCPResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public MovimientoCPRs registrarMovimientoCP(
        @WebParam(name = "RegistrarMovimientoCP", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        MovimientoCPRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.RegistrarMovimientoMPRs
     */
    @WebMethod(operationName = "RegistrarMovimientoMP", action = "http://grupobbva.com.pe/HarecService/RegistrarMovimientoMP")
    @WebResult(name = "RegistrarMovimientoMPResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public RegistrarMovimientoMPRs registrarMovimientoMP(
        @WebParam(name = "RegistrarMovimientoMP", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        RegistrarMovimientoMPRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.EstadosDeCuentaXContratoMPRs
     */
    @WebMethod(operationName = "ObtenerEstadosDeCuentaXContratoMP", action = "http://grupobbva.com.pe/HarecService/ObtenerEstadosDeCuentaXContratoMP")
    @WebResult(name = "ObtenerEstadosDeCuentaXContratoMPResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public EstadosDeCuentaXContratoMPRs obtenerEstadosDeCuentaXContratoMP(
        @WebParam(name = "ObtenerEstadosDeCuentaXContratoMP", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        EstadosDeCuentaXContratoMPRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.MovimientosProcesadosXContratoMPRs
     */
    @WebMethod(operationName = "ObtenerMovimientosProcesadosXContratoMP", action = "http://grupobbva.com.pe/HarecService/ObtenerMovimientosProcesadosXContratoMP")
    @WebResult(name = "ObtenerMovimientosProcesadosXContratoMPResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public MovimientosProcesadosXContratoMPRs obtenerMovimientosProcesadosXContratoMP(
        @WebParam(name = "ObtenerMovimientosProcesadosXContratoMP", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        MovimientosProcesadosXContratoMPRq parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns com.ibm.bbva.harec.client.MovimientosPorProcesarXContratoMPRs
     */
    @WebMethod(operationName = "ObtenerMovimientosPorProcesarXContratoMP", action = "http://grupobbva.com.pe/HarecService/ObtenerMovimientosPorProcesarXContratoMP")
    @WebResult(name = "ObtenerMovimientosPorProcesarXContratoMPResponse", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
    public MovimientosPorProcesarXContratoMPRs obtenerMovimientosPorProcesarXContratoMP(
        @WebParam(name = "ObtenerMovimientosPorProcesarXContratoMP", targetNamespace = "http://grupobbva.com.pe/HarecService/", partName = "parameters")
        MovimientosPorProcesarXContratoMPRq parameters);

}
