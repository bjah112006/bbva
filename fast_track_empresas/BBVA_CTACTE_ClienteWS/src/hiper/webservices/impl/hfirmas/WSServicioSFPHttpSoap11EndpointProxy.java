package hiper.webservices.impl.hfirmas;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.List;
import hiper.spring.beans.hfirmas.webservices.xsd.ClientePJBE;
import hiper.spring.beans.hfirmas.webservices.xsd.EmpresaBE;
import hiper.spring.beans.hfirmas.webservices.xsd.PoderFirmaActivacion;
import hiper.spring.beans.hfirmas.webservices.xsd.RespuestaBE;
import hiper.spring.beans.hfirmas.webservices.xsd.RpteActivacionBE;
import hiper.spring.beans.hfirmas.webservices.xsd.TipoPJBE;

public class WSServicioSFPHttpSoap11EndpointProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private hiper.webservices.impl.hfirmas.WSServicioSFP _service = null;
        private hiper.webservices.impl.hfirmas.WSServicioSFPPortType _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new hiper.webservices.impl.hfirmas.WSServicioSFP(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            _service = new hiper.webservices.impl.hfirmas.WSServicioSFP();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getWSServicioSFPHttpSoap11Endpoint();
        }

        public hiper.webservices.impl.hfirmas.WSServicioSFPPortType getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://hfirmas.impl.webservices.hiper", "WSServicioSFPHttpSoap11Endpoint");
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

    public WSServicioSFPHttpSoap11EndpointProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public WSServicioSFPHttpSoap11EndpointProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public Boolean testRegistrarCliente(Integer idCliente) {
        return _getDescriptor().getProxy().testRegistrarCliente(idCliente);
    }

    public List<TipoPJBE> obtenerTipoPJ(String indicadorOperacion, String codTipoPJ, String descripcionTipoPJ, String estado) {
        return _getDescriptor().getProxy().obtenerTipoPJ(indicadorOperacion,codTipoPJ,descripcionTipoPJ,estado);
    }

    public String activarFirmasyPoderesEnHost(String codOperacion, String usrActivacion, String codCliente, Integer idCliente, List<RpteActivacionBE> listaRepresentantes) {
        return _getDescriptor().getProxy().activarFirmasyPoderesEnHost(codOperacion,usrActivacion,codCliente,idCliente,listaRepresentantes);
    }

    public RespuestaBE fusionarEmpresas(String tipoDOI, String numeroDOI, String codigoCentral, String razonSocial, List<String> listaEmpresasAbsorbidas) {
        return _getDescriptor().getProxy().fusionarEmpresas(tipoDOI,numeroDOI,codigoCentral,razonSocial,listaEmpresasAbsorbidas);
    }

    public RespuestaBE escindirEmpresas(List<EmpresaBE> listaEmpresas, String codigoCentral, String tipoDOI, String nroDOI, String razonSocial) {
        return _getDescriptor().getProxy().escindirEmpresas(listaEmpresas,codigoCentral,tipoDOI,nroDOI,razonSocial);
    }

    public Boolean testRegistrarBastanteo(Integer idCliente) {
        return _getDescriptor().getProxy().testRegistrarBastanteo(idCliente);
    }

    public PoderFirmaActivacion activarPoderes(String numeroExpediente, String tipoOperacion, List<RpteActivacionBE> listaRepresentantes, String resultadoBastanteo, String usuario) {
        return _getDescriptor().getProxy().activarPoderes(numeroExpediente,tipoOperacion,listaRepresentantes,resultadoBastanteo,usuario);
    }

    public ClientePJBE obtenerPoderes(String codCentralClte, String tipoDOI, String nroDOI, String version) {
        return _getDescriptor().getProxy().obtenerPoderes(codCentralClte,tipoDOI,nroDOI,version);
    }

}