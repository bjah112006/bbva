package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
// !DO NOT EDIT THIS FILE!
// This source file is generated by Oracle tools
// Contents may be subject to change
// For reporting problems, use the following
// Version = Oracle WebServices (11.1.1.0.0, build 130224.1947.04102)

@WebService(wsdlLocation="http://118.180.188.27:9080/ContentManagerService/ContentManagerServiceSB12?wsdl",
  targetNamespace="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws",
  name="ContentManagerWSPortType")
@XmlSeeAlso(
  { pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.ObjectFactory.class, pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.base.ObjectFactory.class })
@SOAPBinding(style=Style.DOCUMENT, parameterStyle=ParameterStyle.BARE)
public interface ContentManagerWSPortType
{
  @WebMethod(action="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/insertarDoc")
  @SOAPBinding(parameterStyle=ParameterStyle.BARE)
  @Action(input="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/insertarDoc",
    output="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/ContentManagerWSPortType/insertarDocResponse")
  @WebResult(targetNamespace="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types",
    partName="response", name="insertarDocResponse")
  public pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.InsertarDocResponse insertarDoc(@WebParam(targetNamespace="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types",
      partName="request", name="insertarDocRequest")
    pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.InsertarDocRequest request);

  @WebMethod(action="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/obtenerDoc")
  @SOAPBinding(parameterStyle=ParameterStyle.BARE)
  @Action(input="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/obtenerDoc",
    output="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/ContentManagerWSPortType/obtenerDocResponse")
  @WebResult(targetNamespace="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types",
    partName="response", name="obtenerDocResponse")
  public pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.ObtenerDocResponse obtenerDoc(@WebParam(targetNamespace="http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types",
      partName="request", name="obtenerDocRequest")
    pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.ObtenerDocRequest request);
}
