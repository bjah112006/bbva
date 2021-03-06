package com.ibm.bbva.proxy;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.List;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

public class WSLDAPServiceImplPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.bbva.proxy.WSLDAPServiceImplService _service = null;
        private com.ibm.bbva.proxy.WSLDAPServiceImpl _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.bbva.proxy.WSLDAPServiceImplService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.bbva.proxy.WSLDAPServiceImplService)ctx.lookup("java:comp/env/service/WSLDAPServiceImplService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new com.ibm.bbva.proxy.WSLDAPServiceImplService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getWSLDAPServiceImplPort();
        }

        public com.ibm.bbva.proxy.WSLDAPServiceImpl getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://servidor.ldap.ws.bbva.com.pe/", "WSLDAPServiceImplPort");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }

        public void setMTOMEnabled(boolean enable) {
            SOAPBinding binding = (SOAPBinding) ((BindingProvider) _proxy).getBinding();
            binding.setMTOMEnabled(enable);
        }
    }

    public WSLDAPServiceImplPortProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public WSLDAPServiceImplPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public List<Usuario> obtenerUsuarios(List<String> codigoUsuario) throws WSLdapException_Exception {
        return _getDescriptor().getProxy().obtenerUsuarios(codigoUsuario);
    }

    public List<Usuario> obtenerUsuariosPorPerfil(List<String> codigoPerfil) throws WSLdapException_Exception {
        return _getDescriptor().getProxy().obtenerUsuariosPorPerfil(codigoPerfil);
    }

    public List<Perfil> obtenerPerfiles(List<String> codigoAplicacion) throws WSLdapException_Exception {
        return _getDescriptor().getProxy().obtenerPerfiles(codigoAplicacion);
    }

    public List<Puesto> obtenerPuestos(List<String> codigoPuesto) throws WSLdapException_Exception {
        return _getDescriptor().getProxy().obtenerPuestos(codigoPuesto);
    }

    public List<Perfil> obtenerPerfilesPorUsuarioyAplicacion(String codigoUsuario, String codigoAplicacion) throws WSLdapException_Exception {
        return _getDescriptor().getProxy().obtenerPerfilesPorUsuarioyAplicacion(codigoUsuario,codigoAplicacion);
    }

    public List<Usuario> obtenerUsuariosPorEmpresa(String codigoEmpresa) throws WSLdapException_Exception {
        return _getDescriptor().getProxy().obtenerUsuariosPorEmpresa(codigoEmpresa);
    }

}