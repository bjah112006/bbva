package com.ibm.bbva.ctacte.servicio.cm;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.Action;

public class CEContentManagerServicePortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServiceService _service = null;
        private com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServiceDelegate _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServiceService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServiceService)ctx.lookup("java:comp/env/service/CEContentManagerServiceService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServiceService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getCEContentManagerServicePort();
        }

        public com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServiceDelegate getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://cm.servicio.ctacte.bbva.ibm.com/", "CEContentManagerServicePort");
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

    public CEContentManagerServicePortProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public CEContentManagerServicePortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public DocumentoCM obtenerDocumentoCM(int idExpediente, int idDocumento) {
        return _getDescriptor().getProxy().obtenerDocumentoCM(idExpediente,idDocumento);
    }

    public DocumentoCM obtenerDocumentoCMxCodigo(int idExpediente, String codigoDocumento) {
        return _getDescriptor().getProxy().obtenerDocumentoCMxCodigo(idExpediente,codigoDocumento);
    }

    public String actualizarEstadoDocumentoCC(int idExpediente, int idDocumento, String flagCM, int idCM, String pidCM) {
        return _getDescriptor().getProxy().actualizarEstadoDocumentoCC(idExpediente,idDocumento,flagCM,idCM,pidCM);
    }

    public String actualizarEstadoDocumentoxCodigoCC(int idExpediente, String codigoDocumento, String flagCM, int idCM, String pidCM) {
        return _getDescriptor().getProxy().actualizarEstadoDocumentoxCodigoCC(idExpediente,codigoDocumento,flagCM,idCM,pidCM);
    }

    public String actualizarDocumentoExpIdCMCopia(int idExpediente, String codigoDocumento, int idCMCopia, String pidCM) {
        return _getDescriptor().getProxy().actualizarDocumentoExpIdCMCopia(idExpediente,codigoDocumento,idCMCopia,pidCM);
    }

    public Integer obtenerDocumentoxCodigoCC(int idExpediente, String codigoDocumento) {
        return _getDescriptor().getProxy().obtenerDocumentoxCodigoCC(idExpediente,codigoDocumento);
    }

    public DocumentoExpediente obtenerDocumentoExpedienteCC(int idExpediente, String codigoDocumento) {
        return _getDescriptor().getProxy().obtenerDocumentoExpedienteCC(idExpediente,codigoDocumento);
    }

}