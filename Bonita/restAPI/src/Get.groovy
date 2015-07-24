import java.util.logging.Level;
import java.util.logging.Logger

import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.page.*

import javax.servlet.http.HttpServletRequest

import com.bbva.bonita.authentication.impl.DBUtil

public class Get implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        Map<String, String> response = [:]
		Logger logger = restApiUtil.logger
		String key = request.getParameter "key"
		
		logger.log Level.SEVERE, "find key: [" + key + "]"
		
        if(key != null) {
			String value = DBUtil.obtenerParametro(key)
			response.put "key", key
			response.put "value", value
			if(value.isEmpty()) {
				response.put "error", "Valor de clave no registrado en la base de datos"
			}
        } else {
			response.put "key", ""
			response.put "value", ""     
            response.put "error", "Valor de clave invalido" 
        }
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }
}
