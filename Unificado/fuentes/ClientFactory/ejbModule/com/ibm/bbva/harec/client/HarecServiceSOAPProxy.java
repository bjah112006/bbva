package com.ibm.bbva.harec.client;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

public class HarecServiceSOAPProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.bbva.harec.client.HarecService_Service _service = null;
        private com.ibm.bbva.harec.client.HarecService _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.bbva.harec.client.HarecService_Service(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.bbva.harec.client.HarecService_Service)ctx.lookup("java:comp/env/service/HarecService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new com.ibm.bbva.harec.client.HarecService_Service();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getHarecServiceSOAP();
        }

        public com.ibm.bbva.harec.client.HarecService getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://grupobbva.com.pe/HarecService/", "HarecServiceSOAP");
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

    public HarecServiceSOAPProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public HarecServiceSOAPProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public DatosGeneralesXpersonaRs obtenerDatosXPersona(DatosGeneralesXPersonaRq parameters) {
        return _getDescriptor().getProxy().obtenerDatosXPersona(parameters);
    }

    public DireccionesXPersonaRs obtenerDireccionesXPersona(DireccionesXPersonaRq parameters) {
        return _getDescriptor().getProxy().obtenerDireccionesXPersona(parameters);
    }

    public RelacionesContractualesRs obtenerContratosXPersona(RelacionesContractualesRq parameters) {
        return _getDescriptor().getProxy().obtenerContratosXPersona(parameters);
    }

    public MovimientosXContratoRs obtenerMovimientosXContratoCP(MovimientosXContratoRq parameters) {
        return _getDescriptor().getProxy().obtenerMovimientosXContratoCP(parameters);
    }

    public DatosXContratoRs obtenerDatosXContrato(DatosXContratoRq parameters) {
        return _getDescriptor().getProxy().obtenerDatosXContrato(parameters);
    }

    public MovimientoCPRs registrarMovimientoCP(MovimientoCPRq parameters) {
        return _getDescriptor().getProxy().registrarMovimientoCP(parameters);
    }

    public RegistrarMovimientoMPRs registrarMovimientoMP(RegistrarMovimientoMPRq parameters) {
        return _getDescriptor().getProxy().registrarMovimientoMP(parameters);
    }

    public EstadosDeCuentaXContratoMPRs obtenerEstadosDeCuentaXContratoMP(EstadosDeCuentaXContratoMPRq parameters) {
        return _getDescriptor().getProxy().obtenerEstadosDeCuentaXContratoMP(parameters);
    }

    public MovimientosProcesadosXContratoMPRs obtenerMovimientosProcesadosXContratoMP(MovimientosProcesadosXContratoMPRq parameters) {
        return _getDescriptor().getProxy().obtenerMovimientosProcesadosXContratoMP(parameters);
    }

    public MovimientosPorProcesarXContratoMPRs obtenerMovimientosPorProcesarXContratoMP(MovimientosPorProcesarXContratoMPRq parameters) {
        return _getDescriptor().getProxy().obtenerMovimientosPorProcesarXContratoMP(parameters);
    }

}