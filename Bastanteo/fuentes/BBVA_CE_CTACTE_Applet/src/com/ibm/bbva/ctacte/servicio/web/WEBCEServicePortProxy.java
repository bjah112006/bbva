package com.ibm.bbva.ctacte.servicio.web;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.List;
import javax.xml.ws.Action;
import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.bean.DocumentoPid;

public class WEBCEServicePortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.bbva.ctacte.servicio.web.WEBCEServiceService _service = null;
        private com.ibm.bbva.ctacte.servicio.web.WEBCEServiceDelegate _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.bbva.ctacte.servicio.web.WEBCEServiceService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            _service = new com.ibm.bbva.ctacte.servicio.web.WEBCEServiceService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getWEBCEServicePort();
        }

        public com.ibm.bbva.ctacte.servicio.web.WEBCEServiceDelegate getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://web.servicio.ctacte.bbva.ibm.com/", "WEBCEServicePort");
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

    public WEBCEServicePortProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public WEBCEServicePortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public Documento cmObtenerDocumento(Integer idDocumento, Integer idExpediente) {
        return _getDescriptor().getProxy().cmObtenerDocumento(idDocumento,idExpediente);
    }

    public Documento cmObtenerDocumentoxCodigo(String codigoDocumento, Integer idExpediente) {
        return _getDescriptor().getProxy().cmObtenerDocumentoxCodigo(codigoDocumento,idExpediente);
    }

    public Documento cmObtenerDocumentItemType(String codigoDocumento, Integer idExpediente) {
        return _getDescriptor().getProxy().cmObtenerDocumentItemType(codigoDocumento,idExpediente);
    }

    public List<Documento> cmHistorialDocumentoxCliente(String codigoDocumento, String codCliente, int cantDocumento) {
        return _getDescriptor().getProxy().cmHistorialDocumentoxCliente(codigoDocumento,codCliente,cantDocumento);
    }

    public Documento cmObtenerDocumentoxNombreArchivo(String tipoDocumento, String nombreArchivo, Integer idCM) {
        return _getDescriptor().getProxy().cmObtenerDocumentoxNombreArchivo(tipoDocumento,nombreArchivo,idCM);
    }

    public List<DocumentoPid> cmBuscarVistaUnica(String codCliente) {
        return _getDescriptor().getProxy().cmBuscarVistaUnica(codCliente);
    }

}