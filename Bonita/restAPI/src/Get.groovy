import groovy.json.JsonBuilder
import org.bonitasoft.console.common.server.page.*

import javax.servlet.http.HttpServletRequest

import com.bbva.bonita.authentication.impl.DBUtil

public class Get implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        Map<String, String> response = [:]
        Object key = request.parameterMap["key"]
        if(key != null) {
            response.put "value", DBUtil.obtenerParametro(key.toString())
        } else {
            response.put "value", ""        
            response.put "error", "Parametro no registrado" 
        }
        // response.putAll request.parameterMap
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }
}
