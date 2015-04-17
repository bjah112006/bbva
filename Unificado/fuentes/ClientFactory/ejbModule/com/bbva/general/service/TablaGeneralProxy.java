package com.bbva.general.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import com.bbva.general.entities.Producto;
import com.bbva.general.entities.Territorio;
import com.bbva.general.entities.TipoCambio;

public class TablaGeneralProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.bbva.general.service.TablaGeneralService _service = null;
        private com.bbva.general.service.TablaGeneral _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.bbva.general.service.TablaGeneralService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.bbva.general.service.TablaGeneralService)ctx.lookup("java:comp/env/service/TablaGeneralService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new com.bbva.general.service.TablaGeneralService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getTablaGeneral();
        }

        public com.bbva.general.service.TablaGeneral getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://service.general.bbva.com", "TablaGeneral");
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

    public TablaGeneralProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public TablaGeneralProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public ArrayOfTns2NillableOficina getOficinas(String codOficina, String descOficina) {
        return _getDescriptor().getProxy().getOficinas(codOficina,descOficina);
    }

    public ArrayOfXsdNillableString getCentroCostosPorTerritorio(String codTerritorio) {
        return _getDescriptor().getProxy().getCentroCostosPorTerritorio(codTerritorio);
    }

    public ArrayOfTns2NillableProducto getProductoListado(String productoId, String subProductoId, String productoNombre, String subProductoNombre) {
        return _getDescriptor().getProxy().getProductoListado(productoId,subProductoId,productoNombre,subProductoNombre);
    }

    public Territorio getTerritorio(String codOficina) {
        return _getDescriptor().getProxy().getTerritorio(codOficina);
    }

    public ArrayOfTns2NillableUbigeo getProvinciaListado() {
        return _getDescriptor().getProxy().getProvinciaListado();
    }

    public ArrayOfTns2NillableCentroSuperior getCentroSuperior(String codigoOficina, String codigoAgrupacion) {
        return _getDescriptor().getProxy().getCentroSuperior(codigoOficina,codigoAgrupacion);
    }

    public ArrayOfTns2NillableUbigeo getDistritoListado() {
        return _getDescriptor().getProxy().getDistritoListado();
    }

    public String getCentroCosto(String codOficina) {
        return _getDescriptor().getProxy().getCentroCosto(codOficina);
    }

    public ArrayOfTns2NillableUbigeo getProvincia(String idUbigeo, String descripcion) {
        return _getDescriptor().getProxy().getProvincia(idUbigeo,descripcion);
    }

    public ArrayOfTns2NillableFeriado getFeriadoListado() {
        return _getDescriptor().getProxy().getFeriadoListado();
    }

    public ArrayOfTns2NillableCentro getCentroListado(String codigoOficina) {
        return _getDescriptor().getProxy().getCentroListado(codigoOficina);
    }

    public Producto getProducto(String productoId, String subProductoId) {
        return _getDescriptor().getProxy().getProducto(productoId,subProductoId);
    }

    public ArrayOfTns2NillableTerritorio getTerritorioListado() {
        return _getDescriptor().getProxy().getTerritorioListado();
    }

    public ArrayOfTns2NillableUbigeo getDepartamentoListado() {
        return _getDescriptor().getProxy().getDepartamentoListado();
    }

    public ArrayOfTns2NillableUbigeo getDepartamento(String descripcion) {
        return _getDescriptor().getProxy().getDepartamento(descripcion);
    }

    public ArrayOfTns2NillableUbigeo getDistrito(String idUbigeo, String descripcion) {
        return _getDescriptor().getProxy().getDistrito(idUbigeo,descripcion);
    }

    public TipoCambio getTipoCambio(String fecha, String tipo, String divisa) {
        return _getDescriptor().getProxy().getTipoCambio(fecha,tipo,divisa);
    }

}