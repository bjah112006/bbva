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

public class ConsultaDocumentos implements RestApiController {

    String CARGOS_OFICINA = "010";
    long AMBITO = 1L;
    long OFICINA = 3L;
    long PUESTO = 4L;
    Logger logger
    APISession apiSession
    IdentityAPI identityAPI
    
    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        logger = restApiUtil.logger
        ConsultaResponse response = consultar(request)

        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toString()
            build()
        }
    }

    /**
     *
     * Formato del filtro (%3d equivale a =)
     * 
     * f=campo1%3dvalue1,campo2%3dvalue2
     * o=campo1 ASC, campo2 ASC
     * p=pagina
     * c=cantidad de registros por pagina
     *
     **/
    private ConsultaResponse consultar(HttpServletRequest request) {
        ConsultaResponse consultaResponse = new ConsultaResponse();
        HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request)
        apiSession = requestAccessor.apiSession
        identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession)

        try {
            String requestFilter = isNullRequestParam(request, "f") 
            String[] filters = requestFilter.split(",")
            String sorts = isNullRequestParam(request, "o")
            Integer page = Integer.parseInt(isNullRequestParam(request, "p", "0"))
            Integer rowForPage = Integer.parseInt(isNullRequestParam(request, "c", "10"))
            String username = isNullRequestParam(request, "u", null)
            
            logger.log Level.SEVERE, "===> Filtros: " + filters
            logger.log Level.SEVERE, "===> Ordenado por: " + sorts
            logger.log Level.SEVERE, "===> P\u00E1gina: " + page
            logger.log Level.SEVERE, "===> Filas por p\u00E1gina: " + rowForPage
    
            buscarDocumentosInstancias(filters, sorts, page, rowForPage, username, true, consultaResponse);
        } catch(Exception e) {
            consultaResponse.setStatus("KO")
            consultaResponse.setError(exceptionToString(e))
            consultaResponse.setTotalDocumentos(-1)
            consultaResponse.setDocumentosSolicitudes(new ArrayList<Map<String, Object>>())
        }
        return consultaResponse
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
    
    private String createSelect(String prefix) {
        return """select * from fastpyme.${prefix}instance"""
    }
    
    private String createSelect(String prefix, String orderBy, boolean rowNum) {
        String actv = createSelect("file_")
        String rowNumber = rowNum ? "row_number() over(order by a.tenantid, ${orderBy}) row_num, " : ""
        
        return "select ${rowNumber}* from (${actv}) a"
    }
    
    private void buscarDocumentosInstancias(String[] filters, String sorts, int page, int rowsForPage, String username, boolean active, ConsultaResponse response) {
        StringBuilder where = new StringBuilder("");
        StringBuilder orderBy = new StringBuilder("");
        Integer rowNum = (page * rowsForPage) + 1;
        
        filters.eachWithIndex {value, index ->
            if(!value.isEmpty()){
                if(value.indexOf("instance") > -1) {
                    where.append(value);
                }
            }
        }
        
        if(sorts.isEmpty()) {
            orderBy.append("fecha_documento desc")
        } else {
            orderBy.append(sorts)
        }
        
        String select = createSelect("", orderBy.toString(), true)
        String query = """
            select * from (
                ${select}
                where
                    ${where}
            ) b where row_num >= ${rowNum} limit ${rowsForPage}"""
        logger.log Level.SEVERE, "===> Consulta: " + query
        
        response.setDocumentosSolicitudes(new ArrayList<Map<String, Object>>())
        response.getDocumentosSolicitudes().addAll(executeQuery(query))
        
        if(response.getDocumentosSolicitudes().size() < rowsForPage && page == 0) {
            response.setTotalDocumentos(response.getDocumentosSolicitudes().size())
        } else {
            select = createSelect("", orderBy.toString(), false)
            query = """
                    select count(1) totalRows from (
                        ${select}
                        where
                            ${where}
                    ) b"""
            logger.log Level.SEVERE, "===> Consulta: " + query
            List<Map<String, Object>> result = executeQuery(query)
            response.setTotalDocumentos(0)
            if(!result.isEmpty()) {
                response.setTotalDocumentos(Long.parseLong(result.get(0).get("totalrows").toString()))
            }
        }
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
                    logger.log Level.SEVERE, "===> COLUMNNAME: " + o.getColumnName()
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
                logger.log Level.SEVERE, "============================================= "
                logger.log Level.SEVERE, "=== ROW: " + row
                logger.log Level.SEVERE, "=== ROWNUM: " + rs.getObject("row_num")
                logger.log Level.SEVERE, "=== FECHADOCUMENTO: " + rs.getObject("fecha_documento")
                logger.log Level.SEVERE, "=== INSTANCE: " + rs.getObject("instance")
                logger.log Level.SEVERE, "=== TENANTID: " + rs.getObject("tenantid")
                logger.log Level.SEVERE, "=== NAME_INSTANCE: " + rs.getObject("name_instance")
                logger.log Level.SEVERE, "=== DEFINITION_ID: " + rs.getObject("definition_id")
                logger.log Level.SEVERE, "=== FILENAME_MAPPING: " + rs.getObject("filename_mapping")
                logger.log Level.SEVERE, "=== DESCRIPTION: " + rs.getObject("descripcion")
                logger.log Level.SEVERE, "=== FILENAME: " + rs.getObject("filename")
                logger.log Level.SEVERE, "=== MIMETYPE: " + rs.getObject("mimetype")
                logger.log Level.SEVERE, "=== URL: " + rs.getObject("url")
                logger.log Level.SEVERE, "============================================= "
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
    
    class ConsultaResponse {
        List<Map<String, Object>> documentosSolicitudes
        Long totalDocumentos
        String error
        String status
    }

}
