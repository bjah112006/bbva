import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.beanutils.MethodUtils;

import com.everis.core.exception.BussinesException;
import com.everis.util.DBUtilSpring;
import com.everis.util.db.Metadata;
import com.everis.util.db.mapper.RowMapper;
import com.everis.util.db.mapper.impl.StringMapperImpl;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level
import java.util.logging.Logger

import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor
import org.bonitasoft.console.common.server.page.PageContext;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.RestApiController;
import org.bonitasoft.console.common.server.page.RestApiResponse;
import org.bonitasoft.console.common.server.page.RestApiResponseBuilder;
import org.bonitasoft.console.common.server.page.RestApiUtil;
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.TenantAPIAccessor
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.identity.CustomUserInfoValue;
import org.bonitasoft.engine.identity.CustomUserInfoValueSearchDescriptor;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.Order;
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
import com.everis.util.db.mapper.impl.MapMapperImpl;

public class Listado implements RestApiController {

    Logger logger
    APISession apiSession
    IdentityAPI identityAPI
    
    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        logger = restApiUtil.logger
        Response response = consultar(request)

        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toString()
            build()
        }
    }

    private Response consultar(HttpServletRequest request) {
        Response response = new Response();
        HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request)
        apiSession = requestAccessor.apiSession
        identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession)

        try {
            response.setStatus("OK")
            response.setError("OK")
            response.setTipoRed(new ArrayList<Map<String, Object>>())
            response.setAreas(new ArrayList<Map<String, Object>>())
            response.setUsuarioPorAreas(new ArrayList<Map<String, Object>>())
            response.setDetalleSolicitudAreas(new ArrayList<Map<String, Object>>())
            response.setTotalDetalleSolicitudAreas(-1)
            
            String tipoConsulta = isNullRequestParam(request, "tipoConsulta")
            String query = ""
            String centroNegocio = ""
            List<Map<String, Object>> rows
            
            switch(tipoConsulta) {
                case "red":
                    consultarTipoRed(response)
                    break;
                case "areas":
                    String tipoRed = isNullRequestParam(request, "tipoRed")
                    consultarAreas(response, tipoRed)
                    break;
                case "detalle":
                    centroNegocio = isNullRequestParam(request, "centroNegocio");
                    String where = "";
                    
                    if(!centroNegocio.equalsIgnoreCase("[Todos]")) {
                        where = "where codigo_centro_negocio = '${centroNegocio}'";
                    }
                    
                    query = """
                    select codigo_centro_negocio
                        , sum(case when taskname='Asignar Evaluacion' or taskname='Asignar Evaluación' then 1 else 0 end) ASIGNAR_EVALUACION
                        , sum(case when taskname='Autorizar Aprobacion' or taskname='Autorizar Aprobación' then 1 else 0 end) AUTORIZAR_EVALUACION
                        , sum(case when taskname='Evaluar Riesgo Campo' then 1 else 0 end) EVALUAR_RIESGO_CAMPO
                        , sum(case when taskname='Evaluar Riesgo Mesa' then 1 else 0 end) EVALUAR_RIESGO_MESA
                        , sum(case when taskname='Registrar Solicitud ' then 1 else 0 end) REGISTRAR_SOLICITUD
                        , sum(case when taskname='Realizar Desembolso' then 1 else 0 end) REALIZAR_DESEMBOLSO
                        , sum(case when taskname='Revisar Resultado Dictamen' then 1 else 0 end) REVISAR_RESULTADO_DICTAMEN
                        , sum(case when taskname='Subsanar Documentacion' or taskname='Subsanar Documentación' then 1 else 0 end) SUBSANAR_DOCUMENTACION
                        , sum(case when taskname='Validar Documentacion' or taskname='Validar Documentación' then 1 else 0 end) VALIDAR_DOCUMENTACION
                        , 0 TOTAL
                    from fastpyme.instance
                    ${where} 
                    group by codigo_centro_negocio order by codigo_centro_negocio
                    """
                    rows = executeQuery(query)
                    response.setDetalleSolicitudAreas(rows)
                    response.setTotalDetalleSolicitudAreas(rows.size())
                    break;
                case "detalleCentroNegocio":
                    centroNegocio = isNullRequestParam(request, "centroNegocio")
                    query = """
                    select lastname || ' ' || firstname username, count(1) cant
                    from fastpyme.instance
                    where codigo_centro_negocio='${centroNegocio}'
                    group by lastname || ' ' || firstname
                    order by count(1) desc
                    """
                    rows = executeQuery(query)
                    response.setDetalleSolicitudAreas(rows)
                    response.setTotalDetalleSolicitudAreas(rows.size())
                    break;
                default:
                    break;
            }
            
        } catch(Exception e) {
            response.setStatus("KO")
            response.setError(exceptionToString(e))
        }
        
        return response
    }

    private void consultarTipoRed(Response response) {
        Map<String, Object> red;
        List<Map<String, Object>> tipoRed = new ArrayList<Map<String, Object>>()
        
        red = new HashMap<String, Object>()
        red.put("label", "Lima")
        red.put("value", "LIM")
        tipoRed.add(red)
        
        red = new HashMap<String, Object>()
        red.put("label", "Provincia")
        red.put("value", "PRV")
        tipoRed.add(red)
        
        response.setTipoRed(tipoRed)
    }
        
    private void consultarAreas(Response response, String tipoRed) {
        String query = "SELECT * FROM TBL_PYME_PARAMETER WHERE ID_TABLE=4 AND ID_REFERENCE='" + tipoRed + "' ORDER BY VAL_COLUMN1" 
        response.setAreas(executeQuery(query))
    }
    
    private String isNullRequestParam(HttpServletRequest request, String param, String defaultValue) {
        String value = request.getParameter param
        if(value == null) {
            value = defaultValue
        }
        
        return value
    }
    
    private String isNullRequestParam(HttpServletRequest request, String param) {
        return isNullRequestParam(request, param, "")
    }
    
    private List<Metadata> getMetadata(ResultSet result) {
        List<Metadata> cols = new ArrayList<Metadata>();
        int i = 0;
        Metadata o = null;
        if (result != null) {
            try {
                ResultSetMetaData metaData = result.getMetaData();
                for (i = 1; i <= metaData.getColumnCount(); i++) {
                    o = new Metadata();
                    o.setColumnName(metaData.getColumnName(i));
                    o.setColumnType(metaData.getColumnType(i));
                    o.setPrecision(metaData.getPrecision(i));
                    o.setScale(metaData.getScale(i));
                    cols.add(o);
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "[SQLUtil:getMetadata] initialContext SQLException", e);
            }
        }

        return cols;
    }
    
    private List<Map<String, Object>> executeQuery(String query) throws BussinesException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;
        Connection cn = null
        PreparedStatement ps = null
        ResultSet rs = null
        try {
            InitialContext ic = new InitialContext()
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS")
            cn = ds.getConnection()
            ps = cn.prepareStatement(query);
            rs = ps.executeQuery();
            List<Metadata> metadata = getMetadata rs
            MapMapperImpl mapper = new MapMapperImpl()
            
            while (rs.next()) {
                row = mapper.proccess(rs, metadata)
                result.add row;
            }
        } catch (Exception e) {
            throw new BussinesException(e)
        } finally {
            try {
                if(rs != null) {
                    rs.close()
                }
                if(ps != null) {
                    ps.close()
                }
                if(cn != null) {
                    cn.close()
                }
            } catch (Exception e) {
                throw new BussinesException(e)
            }
        }
        
        return result;
    }
    
    private String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter(0)
        PrintWriter out = new PrintWriter(sw)
        e.printStackTrace(out)
        return sw.toString().replace("\"", "'")
    }
    
    class Response {
        List<Map<String, Object>> tipoRed
        List<Map<String, Object>> areas
        List<Map<String, Object>> detalleSolicitudAreas
        List<Map<String, Object>> usuarioPorAreas
        Long totalDetalleSolicitudAreas
        String error
        String status
    }

}
