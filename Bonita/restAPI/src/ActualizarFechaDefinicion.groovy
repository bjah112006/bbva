import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import com.everis.util.db.SQLUtil;

public class ActualizarFechaDefinicion implements RestApiController {

    Logger logger
    
    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        logger = restApiUtil.logger
        Map<String, String> response = [:]
        SimpleDateFormat dfmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")
        
        try {
            // TODO: enddate
            String startdate = dfmt.parse(request.getParameter("startdate"))
            String id = Long.parseLong(request.getParameter("id"))

            SQLUtil<Object> sqlUtil = new SQLUtil<Object>("java:comp/env/bonitaSequenceManagerDS")
            sqlUtil.executeUpdate("UPDATE PROCESS_INSTANCE SET STARTDATE=? WHERE ID=?", null)
            
            response.put "status", "OK"
        } catch(Exception e) {
            logger.log Level.SEVERE, "Error al actualizar la instancia de proceso: $id", e
            
            response.put "status", "KO"
            response.put "error", e.getMessage()
        }
        
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }
}
