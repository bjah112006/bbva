import java.io.Serializable;
import java.util.List;
import java.util.logging.Level
import java.util.logging.Logger

import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor
import org.bonitasoft.console.common.server.page.*
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.TenantAPIAccessor
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession

import javax.servlet.http.HttpServletRequest

import com.bbva.bonita.authentication.impl.DBUtil
import com.bbva.bonita.dto.SolicitudDTO;
import com.bbva.bonita.util.Constante;
import com.bbva.bonita.util.ConstantesEnum;
import com.bbva.bonita.util.Utils;

public class ConsultaSolicitud implements RestApiController {

    Logger logger
    Map<Long, ProcessDefinition> processDefinitions
    Map<Long, User> users
    APISession apiSession
    ProcessAPI processAPI
    IdentityAPI identityAPI
    
    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        logger = restApiUtil.logger
        ConsultaResponse response = consultar(request)

        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }

    private ConsultaResponse consultar(HttpServletRequest request) {
        ConsultaResponse consultaResponse = new ConsultaResponse();
        HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request)
        processDefinitions = new HashMap<Long, ProcessDefinition>()
        users = new HashMap<Long, User>()
        apiSession = requestAccessor.apiSession
        processAPI = TenantAPIAccessor.getProcessAPI(apiSession)
        identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession)

        String codigoFiltro = request.getParameter "filtro"
        String valorFiltro = request.getParameter "valorFiltro"
        String estacion = request.getParameter "estacion"

        logger.log Level.SEVERE, "=== FILTRO: " + codigoFiltro
        logger.log Level.SEVERE, "=== VALOR FILTRO: " + valorFiltro
        logger.log Level.SEVERE, "=== ESTACION: " + estacion

        SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1)
        if(codigoFiltro.compareTo("01") == 0 && !valorFiltro.isEmpty()){
            logger.log Level.SEVERE, "=== FILTRO POR NRO. SOLICITUD PENDIENTES: " + valorFiltro
            builder.filter ProcessInstanceSearchDescriptor.ID, valorFiltro
        }

        SearchResult<ProcessInstance> processInstanceResults = processAPI.searchOpenProcessInstances(builder.done())
        List<ProcessInstance> processInstance = processInstanceResults.getResult()
        
        consultaResponse.solicitudes = new ArrayList<Solicitud>()
        processInstance.eachWithIndex {instance, index ->
            consultaResponse.solicitudes.add(obtenerInstancia(instance))
            logger.log Level.SEVERE, "Obtener instancia: " + instance.getRootProcessInstanceId() + "[$index]"
        }

        logger.log Level.SEVERE, "Total de elementos encontrados: " + processInstanceResults.getCount()
        logger.log Level.SEVERE, "Elementos encontrados en la lista: " + (consultaResponse.solicitudes == null ? 0 : consultaResponse.solicitudes.size())

        consultaResponse.solicitudes
        
        return consultaResponse
    }

    private Solicitud obtenerInstancia(ProcessInstance instance) {
        Solicitud solicitud = new Solicitud()
        ProcessDefinition processDefinition = obtenerProceso(instance.getProcessDefinitionId())
        solicitud.setNroSolicitud(String.valueOf(instance.getRootProcessInstanceId()))
        solicitud.setNombreProceso(processDefinition.getName())
        solicitud.setVersionProceso(processDefinition.getVersion())
        solicitud.setVariable(Constante.PARAMETRO_VARIABLE)
        solicitud.setFechaLlegada(Utils.convertirDateEnCadena(instance.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()))

//        User user = getIdentityAPI().getUser(elementoSolicitud.getStartedBy())
//        solicitudDTO.setUsuarioEjecutorTarea(user.getUserName().concat("-").concat(user.getFirstName()).concat(" " + user.getLastName()))
        return solicitud
    }

    private ProcessDefinition obtenerProceso(Long id) {
        ProcessDefinition processDefinition = processDefinitions[id];
        if(processDefinition == null) {
            logger.log Level.SEVERE, "Obteniendo el proceso $id desde la fuente de datos"
            processDefinition = processAPI.getProcessDefinition(id);
        }
        
        return processDefinition;
    }
    
    private User obtenerUsuario() {
        return null;
    }
    
    abstract class AbstractSolicitud implements Comparable<AbstractSolicitud> {

        String nroSolicitud
        String tipoDOICliente
        String nroDOICliente
        String nombreCliente
        String nombreTarea
        String tipoOferta
        String oficinaSolicitud
        String fechaLlegada
        String rolEjecutorTarea
        String usuarioEjecutorTarea
        String fechaEnvio
        String nroRVGL
        String nroContrato
        String nroGarantia
        String dictamen
        String estado
        String producto
        String campania
        String causal_clte_cancela
        String causal_devol_gmc
        String clasificacion_clte
        String moneda
        String monto
        String plazo
        String tasa
        String abn_registante
        String num_preimpreso
        String versionProceso
        String nombreProceso
        String variable
        String idArchivada

        @Override
        public int compareTo(AbstractSolicitud o) {
            return o.getNroSolicitud().compareTo(this.getNroSolicitud());
        }        
    }

    class Solicitud extends AbstractSolicitud {
    }
        
    class ConsultaResponse {
        List<Solicitud> solicitudes
        String error
        String status
    }
    
    
}
