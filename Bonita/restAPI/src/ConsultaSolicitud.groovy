import java.util.List;
import java.util.logging.Level
import java.util.logging.Logger

import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor
import org.bonitasoft.console.common.server.page.*
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.TenantAPIAccessor
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession

import javax.servlet.http.HttpServletRequest

import com.bbva.bonita.authentication.impl.DBUtil


public class ConsultaSolicitud implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {      
        Logger logger = restApiUtil.logger
        ConsultaResponse response = consultar(request, logger)

        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }
    
    public ConsultaResponse consultar(HttpServletRequest request, Logger logger) {
        ConsultaResponse consultaResponse = new ConsultaResponse();
        HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request)
        APISession apiSession = requestAccessor.apiSession
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession)
        IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession)
        
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
        consultaResponse.processInstance = processInstanceResults.getResult()
        
        consultaResponse.instances.put("instances", processInstanceResults.getResult())
        
        logger.log Level.SEVERE, "Total de elementos encontrados: " + processInstanceResults.getCount()
        logger.log Level.SEVERE, "Elementos encontrados en la lista: " + (consultaResponse.processInstance == null ? 0 : consultaResponse.processInstance.size())
        
        return consultaResponse
    }
    
    class ConsultaResponse {
        List<ProcessInstance> processInstance
        Map<String, Object> instances = new HashMap<String, Object>()
        String error
        String status
    }
}
