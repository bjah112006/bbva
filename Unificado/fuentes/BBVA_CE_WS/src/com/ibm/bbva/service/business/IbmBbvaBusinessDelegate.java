package com.ibm.bbva.service.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.CartTerritorioCE;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DelegacionRiesgo;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.LineaMaxima;
import com.ibm.bbva.entities.ParametrosConf;
import com.ibm.bbva.entities.ParametrosProceso;
import com.ibm.bbva.entities.PautaClasificacion;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.RetraccionTarea;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.entities.TblCeIbmDelegacionOficina;
import com.ibm.bbva.service.bean.DocumentoCM;
import com.ibm.bbva.service.bean.EmpleadoDTO;
import com.ibm.bbva.service.bean.EmpleadoPerfilVO;
import com.ibm.bbva.service.bean.EmpleadoVO;
import com.ibm.bbva.service.bean.ExpedienteDTO;
import com.ibm.bbva.service.bean.ParametrosProcesoDTO;
import com.ibm.bbva.service.bean.PerfilDTO;
import com.ibm.bbva.service.util.Util;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.CartTerritorioCEBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DelegacionRiesgoBeanLocal;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.LineaMaximaBeanLocal;
import com.ibm.bbva.session.MontoPesoBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.ParametrosProcesoBeanLocal;
import com.ibm.bbva.session.PautaClasificacionBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.RetraccionTareaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.session.TblCeIbmDelegacionOficinaLocal;
import com.ibm.bbva.ws.service.Constantes;
import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://business.service.bbva.ibm.com/", serviceName="IbmBbvaBusinessService", portName="IbmBbvaBusinessPort")
public class IbmBbvaBusinessDelegate{

    com.ibm.bbva.service.business.IbmBbvaBusiness _ibmBbvaBusiness = null;

    public IbmBbvaBusinessDelegate() throws NamingException {
        _ibmBbvaBusiness = new com.ibm.bbva.service.business.IbmBbvaBusiness(); }

    public boolean delegacionRiesgos (@WebParam(name="idTipoCategoria") Integer idTipoCategoria, @WebParam(name="idExpediente") Long idExpediente) {
        return _ibmBbvaBusiness.delegacionRiesgos(idTipoCategoria,idExpediente);
    }

    public boolean delegacionOficinaBD (@WebParam(name="idNivel") Integer idNivel, @WebParam(name="idExpediente") Long idExpediente) {
        return _ibmBbvaBusiness.delegacionOficinaBD(idNivel,idExpediente);
    }

    public boolean delegacionOficinaMemoria (@WebParam(name="objExpedienteDTO") ExpedienteDTO objExpedienteDTO) {
        return _ibmBbvaBusiness.delegacionOficinaMemoria(objExpedienteDTO);
    }

    public DocumentoCM consultarDocumentoExpediente (@WebParam(name="idExpediente") Long idExpediente, @WebParam(name="codigoTipoDoc") String codigoTipoDoc) {
        return _ibmBbvaBusiness.consultarDocumentoExpediente(idExpediente,codigoTipoDoc);
    }

    public boolean actualizarDocumentoExpTC (@WebParam(name="objDocumentoCM") DocumentoCM objDocumentoCM) {
        return _ibmBbvaBusiness.actualizarDocumentoExpTC(objDocumentoCM);
    }

    public boolean pautasClasificacionBD (@WebParam(name="idExpediente") Long idExpediente) {
        return _ibmBbvaBusiness.pautasClasificacionBD(idExpediente);
    }

    public boolean pautasClasificacionMemoria (@WebParam(name="idTipoOferta") Long idTipoOferta, @WebParam(name="idEstadoCivilTitular") Integer idEstadoCivilTitular, @WebParam(name="idPersonaTitular") Long idPersonaTitular, @WebParam(name="idPersonaConyuge") Long idPersonaConyuge, @WebParam(name="sbsTitular") Double sbsTitular, @WebParam(name="sbsConyuge") Double sbsConyuge) {
        return _ibmBbvaBusiness.pautasClasificacionMemoria(idTipoOferta,idEstadoCivilTitular,idPersonaTitular,idPersonaConyuge,sbsTitular,sbsConyuge);
    }

    public PerfilDTO lineaMaximaBD (@WebParam(name="idExpediente") Long idExpediente) {
        return _ibmBbvaBusiness.lineaMaximaBD(idExpediente);
    }

    public PerfilDTO lineaMaximaMemoria (@WebParam(name="objExpedienteDTO") ExpedienteDTO objExpedienteDTO) {
        return _ibmBbvaBusiness.lineaMaximaMemoria(objExpedienteDTO);
    }

    public boolean retraerTareas (@WebParam(name="idAccion") Long idAccion, @WebParam(name="salida") Long salida, @WebParam(name="llegada") Long llegada) {
        return _ibmBbvaBusiness.retraerTareas(idAccion,salida,llegada);
    }

    public EmpleadoPerfilVO obtenerUsuario (@WebParam(name="idTerritorio") Integer idTerritorio, @WebParam(name="idProducto") Integer idProducto, @WebParam(name="idPerfil") Integer idPerfil, @WebParam(name="idExpediente") Long idExpediente, @WebParam(name="idTarea") Long idTarea) {
        return _ibmBbvaBusiness.obtenerUsuario(idTerritorio,idProducto,idPerfil,idExpediente,idTarea);
    }

    public boolean actualizaEmpleadoExpediente (@WebParam(name="idEmpleado") Long idEmpleado, @WebParam(name="idExpediente") Long idExpediente) {
        return _ibmBbvaBusiness.actualizaEmpleadoExpediente(idEmpleado,idExpediente);
    }

    public ParametrosProcesoDTO obtenerParametroProceso (@WebParam(name="nombreVariable") String nombreVariable) {
        return _ibmBbvaBusiness.obtenerParametroProceso(nombreVariable);
    }

}