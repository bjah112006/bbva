package pe.com.grupobbva.accpj;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import pe.com.grupobbva.accpj.pj.CtBuscarPersonaJuridicaRq;
import pe.com.grupobbva.accpj.pj.CtBuscarPersonaJuridicaRs;

public class ACCPJ_ServiciosWeb_PortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private pe.com.grupobbva.accpj.ACCPJServiciosWebService _service = null;
        private pe.com.grupobbva.accpj.ACCPJServiciosWebPortType _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new pe.com.grupobbva.accpj.ACCPJServiciosWebService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            _service = new pe.com.grupobbva.accpj.ACCPJServiciosWebService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getACCPJServiciosWebPort();
        }

        public pe.com.grupobbva.accpj.ACCPJServiciosWebPortType getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://grupobbva.com.pe/accpj/", "ACCPJ_ServiciosWeb_Port");
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

    public ACCPJ_ServiciosWeb_PortProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public ACCPJ_ServiciosWeb_PortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public pe.com.grupobbva.accpj.rela.CtConsultarRelacionesRs consultarRelaciones(pe.com.grupobbva.accpj.rela.CtConsultarRelacionesRq consultarRelacionesRq) {
        return _getDescriptor().getProxy().consultarRelaciones(consultarRelacionesRq);
    }

    public CtBuscarPersonaJuridicaRs buscarPersonaJuridica(CtBuscarPersonaJuridicaRq buscarPersonaJuridicaRq) {
        return _getDescriptor().getProxy().buscarPersonaJuridica(buscarPersonaJuridicaRq);
    }

    public pe.com.grupobbva.accpj.pey4.CtConsultarRelacionesRs consultarRelacionesPaginado(pe.com.grupobbva.accpj.pey4.CtConsultarRelacionesRq consultarRelacionesPaginadoRq) {
        return _getDescriptor().getProxy().consultarRelacionesPaginado(consultarRelacionesPaginadoRq);
    }

}