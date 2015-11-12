package com.ibm.bbva.service.business;

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

public class IbmBbvaBusinessPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.ibm.bbva.service.business.IbmBbvaBusinessService _service = null;
        private com.ibm.bbva.service.business.IbmBbvaBusinessDelegate _proxy = null;
        private Dispatch<Source> _dispatch = null;
        private boolean _useJNDIOnly = false;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.ibm.bbva.service.business.IbmBbvaBusinessService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            try
            {
                InitialContext ctx = new InitialContext();
                _service = (com.ibm.bbva.service.business.IbmBbvaBusinessService)ctx.lookup("java:comp/env/service/IbmBbvaBusinessService");
            }
            catch (NamingException e)
            {
                if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
                    System.out.println("JNDI lookup failure: javax.naming.NamingException: " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }

            if (_service == null && !_useJNDIOnly)
                _service = new com.ibm.bbva.service.business.IbmBbvaBusinessService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getIbmBbvaBusinessPort();
        }

        public com.ibm.bbva.service.business.IbmBbvaBusinessDelegate getProxy() {
            return _proxy;
        }

        public void useJNDIOnly(boolean useJNDIOnly) {
            _useJNDIOnly = useJNDIOnly;
            init();
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://business.service.bbva.ibm.com/", "IbmBbvaBusinessPort");
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

    public IbmBbvaBusinessPortProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public IbmBbvaBusinessPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public boolean delegacionRiesgos(Integer idTipoCategoria, Long idExpediente) {
        return _getDescriptor().getProxy().delegacionRiesgos(idTipoCategoria,idExpediente);
    }

    public boolean delegacionOficinaBD(Integer idNivel, Long idExpediente) {
        return _getDescriptor().getProxy().delegacionOficinaBD(idNivel,idExpediente);
    }

    public boolean delegacionOficinaMemoria(ExpedienteDTO objExpedienteDTO) {
        return _getDescriptor().getProxy().delegacionOficinaMemoria(objExpedienteDTO);
    }

    public DocumentoCM consultarDocumentoExpediente(Long idExpediente, String codigoTipoDoc) {
        return _getDescriptor().getProxy().consultarDocumentoExpediente(idExpediente,codigoTipoDoc);
    }

    public boolean actualizarDocumentoExpTC(DocumentoCM objDocumentoCM) {
        return _getDescriptor().getProxy().actualizarDocumentoExpTC(objDocumentoCM);
    }

    public EmpleadoDTO distribucionCarga(Long idTerritorio, Long idExpediente) {
        return _getDescriptor().getProxy().distribucionCarga(idTerritorio,idExpediente);
    }

    public boolean pautasClasificacionBD(Long idExpediente) {
        return _getDescriptor().getProxy().pautasClasificacionBD(idExpediente);
    }

    public boolean pautasClasificacionMemoria(Long idTipoOferta, Integer idEstadoCivilTitular, Long idPersonaTitular, Long idPersonaConyuge, Double sbsTitular, Double sbsConyuge) {
        return _getDescriptor().getProxy().pautasClasificacionMemoria(idTipoOferta,idEstadoCivilTitular,idPersonaTitular,idPersonaConyuge,sbsTitular,sbsConyuge);
    }

    public PerfilDTO lineaMaximaBD(Long idExpediente) {
        return _getDescriptor().getProxy().lineaMaximaBD(idExpediente);
    }

    public PerfilDTO lineaMaximaMemoria(ExpedienteDTO objExpedienteDTO) {
        return _getDescriptor().getProxy().lineaMaximaMemoria(objExpedienteDTO);
    }

    public boolean retraerTareas(Long idAccion, Long salida, Long llegada) {
        return _getDescriptor().getProxy().retraerTareas(idAccion,salida,llegada);
    }

    public EmpleadoPerfilVO obtenerUsuario(Integer idTerritorio, Integer idProducto, Integer idPerfil, Long idExpediente, Long idTarea) {
        return _getDescriptor().getProxy().obtenerUsuario(idTerritorio,idProducto,idPerfil,idExpediente,idTarea);
    }

    public boolean actualizaEmpleadoExpediente(Long idEmpleado, Long idExpediente) {
        return _getDescriptor().getProxy().actualizaEmpleadoExpediente(idEmpleado,idExpediente);
    }

    public ParametrosProcesoDTO obtenerParametroProceso(String nombreVariable) {
        return _getDescriptor().getProxy().obtenerParametroProceso(nombreVariable);
    }

}