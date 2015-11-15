import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger

import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.page.*

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest
import javax.sql.DataSource;

import com.bbva.bonita.authentication.impl.DBUtil

public class ConfiguracionReporte implements RestApiController {

    private Logger logger;

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        logger = restApiUtil.logger

        String idTable = request.getParameter "idTable"
        logger.log Level.SEVERE, "find idTable: [" + idTable + "]"
        
        apiResponseBuilder.with {
            withResponse new JsonBuilder(obtenerParametros(idTable)).toString()
            build()
        }
    }
    
    public List<Config> obtenerParametros(String table) {
        List<Config> params = new ArrayList<Config>();
        Config param;

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT ID_REFERENCE, VAL_COLUMN2, VAL_COLUMN1 FROM TBL_PYME_PARAMETER WHERE ID_TABLE='" + table + "'");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                param = new Config();
                param.idReference = rs.getString("ID_REFERENCE");
                param.valColumn1 = rs.getString("VAL_COLUMN1");
                param.valColumn2 = rs.getString("VAL_COLUMN2");
                
                params.add(param);
            }

            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "obtenerParametro", e);
        }
        
        return params;
    }
    
    class Config {
        String idReference;
        String valColumn1;
        String valColumn2;
    }
}
