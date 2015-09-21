package com.ibm.bbva.cm.iice.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

public class ContentServiceImplProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.bbva.cm.iice.service.ContentServiceImplService _service = null;
        private com.ibm.bbva.cm.iice.service.ContentServiceImpl _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.bbva.cm.iice.service.ContentServiceImplService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.bbva.cm.iice.service.ContentServiceImplService)ctx.lookup("java:comp/env/service/ContentServiceImplService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new com.ibm.bbva.cm.iice.service.ContentServiceImplService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getContentServiceImpl();
        }

        public com.ibm.bbva.cm.iice.service.ContentServiceImpl getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://impl.service.cm.bbva.ibm.com", "ContentServiceImpl");
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

    public ContentServiceImplProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public ContentServiceImplProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public String insertPID(Documento documento) {
        return _getDescriptor().getProxy().insertPID(documento);
    }

    public String insertAll(ArrayOf1659910637NillableDocumento documentos) {
        return _getDescriptor().getProxy().insertAll(documentos);
    }

    public ArrayOf1659910637NillableDocumento findAll(Documento documento) {
        return _getDescriptor().getProxy().findAll(documento);
    }

    public String update(Documento documento) {
        return _getDescriptor().getProxy().update(documento);
    }

    public String actualizarTipoDoc(Integer id, String tipoDoc) {
        return _getDescriptor().getProxy().actualizarTipoDoc(id,tipoDoc);
    }

    public Documento findAsImage(Documento documento, String mimeType) {
        return _getDescriptor().getProxy().findAsImage(documento,mimeType);
    }

    public String delete(Documento documento) {
        return _getDescriptor().getProxy().delete(documento);
    }

    public String deleteAll(ArrayOf1659910637NillableDocumento documentos) {
        return _getDescriptor().getProxy().deleteAll(documentos);
    }

    public String insert(Documento documento) {
        return _getDescriptor().getProxy().insert(documento);
    }

    public Documento find(Documento documento) {
        return _getDescriptor().getProxy().find(documento);
    }

}