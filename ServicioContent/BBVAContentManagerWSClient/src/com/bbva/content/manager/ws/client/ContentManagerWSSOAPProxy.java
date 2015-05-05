package com.bbva.content.manager.ws.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

public class ContentManagerWSSOAPProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.bbva.content.manager.ws.client.ContentManagerWS_Service _service = null;
        private com.bbva.content.manager.ws.client.ContentManagerWS _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation) {
            _service = new com.bbva.content.manager.ws.client.ContentManagerWS_Service(wsdlLocation);
            initCommon();
        }
        
        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.bbva.content.manager.ws.client.ContentManagerWS_Service(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            _service = new com.bbva.content.manager.ws.client.ContentManagerWS_Service();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getContentManagerWSSOAP();
        }

        public com.bbva.content.manager.ws.client.ContentManagerWS getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://www.example.org/ContentManagerWS/", "ContentManagerWSSOAP");
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

    public ContentManagerWSSOAPProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public ContentManagerWSSOAPProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public ContentManagerWSSOAPProxy(URL wsdlLocation) {
        _descriptor = new Descriptor(wsdlLocation);
        _descriptor.setMTOMEnabled(false);
    }
    
    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public ObtenerResponse obtener(ObtenerRequest parameters) {
        return _getDescriptor().getProxy().obtener(parameters);
    }

    public InsertarResponse insertar(InsertarRequest parameters) {
        return _getDescriptor().getProxy().insertar(parameters);
    }

//    public ActualizarResponse actualizar(ActualizarRequest parameters) {
//        return _getDescriptor().getProxy().actualizar(parameters);
//    }
//
//    public EliminarResponse eliminar(EliminarRequest parameters) {
//        return _getDescriptor().getProxy().eliminar(parameters);
//    }

}