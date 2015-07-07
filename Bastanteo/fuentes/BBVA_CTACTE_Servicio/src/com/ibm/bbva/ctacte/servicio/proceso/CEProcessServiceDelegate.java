package com.ibm.bbva.ctacte.servicio.proceso;

import hiper.spring.beans.hfirmas.webservices.xsd.PoderFirmaActivacion;
import hiper.spring.beans.hfirmas.webservices.xsd.RpteActivacionBE;
import hiper.webservices.impl.hfirmas.WSServicioSFPHttpSoap11EndpointProxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import clientecontent.ClienteContent;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.AuditoriaCriteriosSupervision;
import com.ibm.bbva.ctacte.bean.CobroComision;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.PerfilBalanceo;
import com.ibm.bbva.ctacte.bean.TareaPerfil;
import com.ibm.bbva.ctacte.bean.ViewNumeroExpedientesEmpleado;
import com.ibm.bbva.ctacte.bean.ViewPesoDocumentoExpediente;
import com.ibm.bbva.ctacte.bean.ViewPesoParticipeExpediente;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.AuditoriaBastanteoDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaClienteDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaCriteriosSupervisionDAO;
import com.ibm.bbva.ctacte.dao.CobroComisionDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.dao.PerfilBalanceoDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TareaPerfilDAO;
import com.ibm.bbva.ctacte.dao.ViewNumeroExpedientesEmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoDocumentoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoParticipeExpedienteDAO;
import com.ibm.bbva.ctacte.servicio.proceso.bean.EmpleadoCE;
import com.ibm.bbva.ctacte.servicio.proceso.util.ProcesoUtil;
import com.ibm.bbva.ctacte.servicio.util.Constantes;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://proceso.servicio.ctacte.bbva.ibm.com/", serviceName="CEProcessServiceService", portName="CEProcessServicePort", wsdlLocation="WEB-INF/wsdl/CEProcessServiceService.wsdl")
public class CEProcessServiceDelegate{

    com.ibm.bbva.ctacte.servicio.proceso.CEProcessService _cEProcessService = null;

    public CEProcessServiceDelegate() throws NamingException {
        _cEProcessService = new com.ibm.bbva.ctacte.servicio.proceso.CEProcessService(); }

    public Integer dentroPlazoSubsanacion (@WebParam(name="dtFechaRegistroExpediente") Date dtFechaRegistroExpediente, @WebParam(name="dtFechaUltimoBastanteo") Date dtFechaUltimoBastanteo) {
        return _cEProcessService.dentroPlazoSubsanacion(dtFechaRegistroExpediente,dtFechaUltimoBastanteo);
    }

    public ViewNumeroExpedientesEmpleado[] obtenerNumExpedientesxEmpleado (@WebParam(name="intIdProducto") Integer intIdProducto, @WebParam(name="intIdTerritorio") Integer intIdTerritorio, @WebParam(name="intIdTarea") Integer intIdTarea) {
        return _cEProcessService.obtenerNumExpedientesxEmpleado(intIdProducto,intIdTerritorio,intIdTarea);
    }

    public ViewPesoParticipeExpediente obtenerEmpleadoxPesoParticipe (@WebParam(name="intIdProducto") Integer intIdProducto, @WebParam(name="intIdTerritorio") Integer intIdTerritorio, @WebParam(name="intIdTarea") Integer intIdTarea) {
        return _cEProcessService.obtenerEmpleadoxPesoParticipe(intIdProducto,intIdTerritorio,intIdTarea);
    }

    public ViewPesoDocumentoExpediente obtenerEmpleadoxPesoDocumento (@WebParam(name="intIdProducto") Integer intIdProducto, @WebParam(name="intIdTerritorio") Integer intIdTerritorio, @WebParam(name="intIdTarea") Integer intIdTarea) {
        return _cEProcessService.obtenerEmpleadoxPesoDocumento(intIdProducto,intIdTerritorio,intIdTarea);
    }

    public EmpleadoCE obtenerEmpleado (@WebParam(name="intIdExpediente") Integer intIdExpediente, @WebParam(name="intIdProducto") Integer intIdProducto, @WebParam(name="intIdTerritorio") Integer intIdTerritorio, @WebParam(name="intIdTarea") Integer intIdTarea) {
        return _cEProcessService.obtenerEmpleado(intIdExpediente,intIdProducto,intIdTerritorio,intIdTarea);
    }

    public Integer verificarCriterioSupervision (@WebParam(name="strCodigoCentral") String strCodigoCentral, @WebParam(name="strResultadoBastanteo") String strResultadoBastanteo) {
        return _cEProcessService.verificarCriterioSupervision(strCodigoCentral,strResultadoBastanteo);
    }

    public Integer activarFirmas (@WebParam(name="intIdExpediente") Integer intIdExpediente, @WebParam(name="tipoOperacion") String tipoOperacion, @WebParam(name="usuarioResponsable") String usuarioResponsable) {
        return _cEProcessService.activarFirmas(intIdExpediente,tipoOperacion,usuarioResponsable);
    }

    public String obtenerEstadoBastanteo (@WebParam(name="intIdExpediente") Integer intIdExpediente) {
        return _cEProcessService.obtenerEstadoBastanteo(intIdExpediente);
    }

    public CobroComision obtenerHoraReintento (@WebParam(name="id") Integer id) {
        return _cEProcessService.obtenerHoraReintento(id);
    }

    public String cobroComisionInmediato (@WebParam(name="strIdExpediente") String strIdExpediente, @WebParam(name="codEmpleadoResponsable") String codEmpleadoResponsable) {
        return _cEProcessService.cobroComisionInmediato(strIdExpediente,codEmpleadoResponsable);
    }

    public boolean crearPreRegistroSubsanacion (@WebParam(name="codigoExpediente") String codigoExpediente) {
        return _cEProcessService.crearPreRegistroSubsanacion(codigoExpediente);
    }

    public boolean crearPreRegistroRevocatoria (@WebParam(name="codigoExpediente") String codigoExpediente) {
        return _cEProcessService.crearPreRegistroRevocatoria(codigoExpediente);
    }

    public Integer grabarEmpleadoExpedienteTareaProceso (@WebParam(name="idExpediente") Integer idExpediente, @WebParam(name="codEmpleado") String codEmpleado, @WebParam(name="idTarea") Integer idTarea) {
        return _cEProcessService.grabarEmpleadoExpedienteTareaProceso(idExpediente,codEmpleado,idTarea);
    }

    public boolean EliminarEmpleadoExpedienteTareaProceso (@WebParam(name="idExpediente") Integer idExpediente, @WebParam(name="codEmpleado") String codEmpleado, @WebParam(name="idTarea") Integer idTarea) {
        return _cEProcessService.EliminarEmpleadoExpedienteTareaProceso(idExpediente,codEmpleado,idTarea);
    }

    public String eliminarDocumentosCM (@WebParam(name="idExpediente") Integer idExpediente) {
        return _cEProcessService.eliminarDocumentosCM(idExpediente);
    }

}