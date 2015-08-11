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
import com.everis.util.db.mapper.impl.MapMapperImpl;

public class ConsultaSolicitud implements RestApiController {

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
    
            consultaResponse.setStatus("OK")
            consultaResponse.setError("OK")
            
            if(requestFilter.indexOf("rootprocessinstanceid=") > -1) {
                logger.log Level.SEVERE, "===> Buscando en pendientes"
                buscarInstancias(filters, sorts, page, rowForPage, username, true, consultaResponse)
            } else if(requestFilter.indexOf("estacion=") > -1) {
                logger.log Level.SEVERE, "===> Buscando en pendientes"
                buscarInstancias(filters, sorts, page, rowForPage, username, true, consultaResponse)
            } else {
                logger.log Level.SEVERE, "===> Buscando en pendientes y archivadas"
                buscarInstancias(filters, sorts, page, rowForPage, username, false, consultaResponse)
            }
        } catch(Exception e) {
            consultaResponse.setStatus("KO")
            consultaResponse.setError(exceptionToString(e))
            consultaResponse.setTotalSolicitudes(-1)
            consultaResponse.setSolicitudes(new ArrayList<Map<String, Object>>())
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
    
    private String createSelect(String prefix, String orderBy, boolean active, boolean rowNum) {
        String arch = ""
        String actv = createSelect("")
        String rowNumber = rowNum ? "row_number() over(order by tenantid, ${orderBy}) row_num, " : ""
        
        if(!active) {
            arch =  " union all " + createSelect("arch_")
        }
        
        return "select ${rowNumber}* from (${actv}${arch}) a"
    }
    
    private void buscarInstancias(String[] filters, String sorts, int page, int rowsForPage, String username, boolean active, ConsultaResponse response) {
        StringBuilder where = new StringBuilder("");
        StringBuilder orderBy = new StringBuilder("");
        Integer rowNum = (page * rowsForPage) + 1;
        
        filters.eachWithIndex {value, index ->
            if(index > 0 && where.length() > 0) {
                where.append(" and ")
            }
            if(value.toLowerCase().indexOf("username") == -1) {
                where.append(value)
            }
        }
        
        if(sorts.isEmpty()) {
            orderBy.append("rootprocessinstanceid, actor")
        } else {
            orderBy.append(sorts)
        }
        
        String select = createSelect("", orderBy.toString(), active, true)
        String query = """
            select * from (
                ${select}
                where
                    ${where}
            ) b where row_num >= ${rowNum} limit ${rowsForPage}"""
        logger.log Level.SEVERE, "===> Consulta: " + query
        
        response.setSolicitudes(new ArrayList<Map<String, Object>>())
        response.getSolicitudes().addAll(executeQuery(query))
        
        if(response.getSolicitudes().size() < rowsForPage && page == 0) {
            response.setTotalSolicitudes(response.getSolicitudes().size())
        } else {
            select = createSelect("", orderBy.toString(), active, false)
            query = """
                    select count(1) totalRows from (
                        ${select}
                        where
                            ${where}
                    ) b"""
            logger.log Level.SEVERE, "===> Consulta: " + query
            List<Map<String, Object>> result = executeQuery(query)
            response.setTotalSolicitudes(0)
            if(!result.isEmpty()) {
                response.setTotalSolicitudes(Long.parseLong(result.get(0).get("totalrows").toString()))
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
                row.put("userTask", row.get("username").toString().trim() + " - " + (row.get("firstname").toString().trim() + " " + row.get("lastname").toString().trim()).trim())
                result.add row;
            }
        } catch (Exception e) {
            throw new BussinesException(e)
            // logger.log(Level.SEVERE, "Ejecutando consulta", e);
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
                // logger.log(Level.SEVERE, "Cerrando conexiones", e);
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
        List<Map<String, Object>> solicitudes
        Long totalSolicitudes
        String error
        String status
    }

}
