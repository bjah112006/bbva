//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.proxy.ext;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "WSLDAPServiceExtImpl", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/")
public interface WSLDAPServiceExtImpl {


    /**
     * 
     * @param codigoUsuario
     * @return
     *     returns java.util.List<java.lang.Object>
     * @throws WSLdapException_Exception
     */
    @WebMethod
    @WebResult(name = "usuario", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/UsuarioExtendido")
    @RequestWrapper(localName = "obtenerUsuarios", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerUsuarios")
    @ResponseWrapper(localName = "obtenerUsuariosResponse", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerUsuariosResponse")
    public List<UsuarioExtendido> obtenerUsuarios(
        @WebParam(name = "codigoUsuario", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Usuario/codigoUsuario")
        List<String> codigoUsuario)
        throws WSLdapException_Exception
    ;

    /**
     * 
     * @param codigoPerfil
     * @return
     *     returns java.util.List<java.lang.Object>
     * @throws WSLdapException_Exception
     */
    @WebMethod
    @WebResult(name = "usuario", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Usuario")
    @RequestWrapper(localName = "obtenerUsuariosPorPerfil", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerUsuariosPorPerfil")
    @ResponseWrapper(localName = "obtenerUsuariosPorPerfilResponse", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerUsuariosPorPerfilResponse")
    public List<Object> obtenerUsuariosPorPerfil(
        @WebParam(name = "codigoPerfil", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Perfil/codigoPerfil")
        List<String> codigoPerfil)
        throws WSLdapException_Exception
    ;

    /**
     * 
     * @param codigoAplicacion
     * @return
     *     returns java.util.List<pe.com.bbva.ws.ldap.servidor.ext.Perfil>
     * @throws WSLdapException_Exception
     */
    @WebMethod
    @WebResult(name = "perfil", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Perfil")
    @RequestWrapper(localName = "obtenerPerfiles", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerPerfiles")
    @ResponseWrapper(localName = "obtenerPerfilesResponse", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerPerfilesResponse")
    public List<Perfil> obtenerPerfiles(
        @WebParam(name = "codigoAplicacion", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Aplicacion/codigoAplicacion")
        List<String> codigoAplicacion)
        throws WSLdapException_Exception
    ;

    /**
     * 
     * @param codigoPuesto
     * @return
     *     returns java.util.List<pe.com.bbva.ws.ldap.servidor.ext.Puesto>
     * @throws WSLdapException_Exception
     */
    @WebMethod
    @WebResult(name = "puesto", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Puesto")
    @RequestWrapper(localName = "obtenerPuestos", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerPuestos")
    @ResponseWrapper(localName = "obtenerPuestosResponse", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerPuestosResponse")
    public List<Puesto> obtenerPuestos(
        @WebParam(name = "codigoPuesto", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Puesto/codigoPuesto")
        List<String> codigoPuesto)
        throws WSLdapException_Exception
    ;

    /**
     * 
     * @param codigoUsuario
     * @param codigoAplicacion
     * @return
     *     returns java.util.List<pe.com.bbva.ws.ldap.servidor.ext.Perfil>
     * @throws WSLdapException_Exception
     */
    @WebMethod
    @WebResult(name = "perfil", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Perfil")
    @RequestWrapper(localName = "obtenerPerfilesPorUsuarioyAplicacion", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerPerfilesPorUsuarioyAplicacion")
    @ResponseWrapper(localName = "obtenerPerfilesPorUsuarioyAplicacionResponse", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerPerfilesPorUsuarioyAplicacionResponse")
    public List<Perfil> obtenerPerfilesPorUsuarioyAplicacion(
        @WebParam(name = "codigoUsuario", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Usuario/codigoUsuario")
        String codigoUsuario,
        @WebParam(name = "codigoAplicacion", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Aplicacion/codigoAplicacion")
        String codigoAplicacion)
        throws WSLdapException_Exception
    ;

    /**
     * 
     * @param codigoEmpresa
     * @return
     *     returns java.util.List<java.lang.Object>
     * @throws WSLdapException_Exception
     */
    @WebMethod
    @WebResult(name = "usuario", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Usuario")
    @RequestWrapper(localName = "obtenerUsuariosPorEmpresa", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerUsuariosPorEmpresa")
    @ResponseWrapper(localName = "obtenerUsuariosPorEmpresaResponse", targetNamespace = "http://ext.servidor.ldap.ws.bbva.com.pe/", className = "pe.com.bbva.ws.ldap.servidor.ext.ObtenerUsuariosPorEmpresaResponse")
    public List<Object> obtenerUsuariosPorEmpresa(
        @WebParam(name = "codigoEmpresa", targetNamespace = "http://servidor.ldap.ws.bbva.com.pe/Empresa/codigoEmpresa")
        String codigoEmpresa)
        throws WSLdapException_Exception
    ;

}
