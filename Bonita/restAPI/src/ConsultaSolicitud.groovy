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
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsultaSolicitud implements RestApiController {

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

        ObjectMapper mapper = new ObjectMapper();
        logger.log Level.SEVERE, mapper.writeValueAsString(response.getSolicitudes());
        apiResponseBuilder.with {
            withResponse mapper.writeValueAsString(response)
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
                buscarInstancias(filters, sorts, page, rowForPage, "-1", false, consultaResponse, request)
            } else if(requestFilter.indexOf("estacion=") > -1) {
                logger.log Level.SEVERE, "===> Buscando en pendientes"
                buscarInstancias(filters, sorts, page, rowForPage, username, true, consultaResponse, request)
            } else {
                logger.log Level.SEVERE, "===> Buscando en pendientes y archivadas"
                buscarInstancias(filters, sorts, page, rowForPage, "-1", false, consultaResponse, request)
            }
        } catch(Exception e) {
            consultaResponse.setStatus("KO")
            consultaResponse.setError(exceptionToString(e))
            consultaResponse.setTotalSolicitudes(-1)
            consultaResponse.setSolicitudes(new ArrayList<Solicitud>())
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
    
    private void buscarInstancias(String[] filters, String sorts, int page, int rowsForPage, String username, boolean active, ConsultaResponse response, HttpServletRequest request) {
        long t1 = System.currentTimeMillis();
        String cargosOficina = DBUtil.obtenerParametro(CARGOS_OFICINA);
        String select = "";
        String orderBy = "";
        Integer rowNum = (page * rowsForPage) + 1;
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.with {
            put "p_name", "-1"
            put "p_value", "-1"
            put "p_id", 0L
            put "p_estacion", "-1"
            put "p_username", username
            put "p_oficina", "-1"
            put "p_ambito", "-1"
            put "p_rownum", 0
            put "p_posicion", isNullRequestParam(request, "n", "S")
        }
        
        if(!"-1".equalsIgnoreCase(username)) {
            // Filter for user
            User user = identityAPI.getUserByUserName(username.trim());
            SearchOptionsBuilder optionsBuilder;
            SearchResult<CustomUserInfoValue> searchResult;
            List<CustomUserInfoValue> infoValue;
            String oficina;
            String ambito;
            String puesto;
            
            // Info Customize
            optionsBuilder = new SearchOptionsBuilder(0, 10);
            optionsBuilder.filter(CustomUserInfoValueSearchDescriptor.USER_ID, user.getId());
            optionsBuilder.sort(CustomUserInfoValueSearchDescriptor.USER_ID, Order.ASC);
            searchResult = identityAPI.searchCustomUserInfoValues(optionsBuilder.done());
            infoValue = searchResult.getResult();
            
            for(CustomUserInfoValue info : infoValue) {
                switch(info.getDefinitionId()) {
                    case AMBITO:
                        ambito = info.getValue();
                        break;
                    case PUESTO:
                        puesto = info.getValue();
                        break;
                    case OFICINA:
                        oficina = info.getValue().split("-")[0];
                        break;
                    default:
                        break;
                }
            }
            
            logger.log Level.SEVERE, "===> Ambito: " + ambito;
            logger.log Level.SEVERE, "===> Oficina: " + oficina;
            logger.log Level.SEVERE, "===> Puesto: " + puesto;
            
            if(!"-1".equalsIgnoreCase(username)) {
                if(cargosOficina.indexOf("|" + puesto + "|") > -1) {
                    params.put "p_oficina", oficina;
                } else if(!ambito.isEmpty()) {
                    params.put "p_ambito", ambito;
                }
            }
        }
        
        if(sorts.isEmpty()) {
            orderBy = "rootprocessinstanceid, actor";
        } else {
            orderBy = sorts;
        }

        
        String[] param;
        filters.eachWithIndex {value, index ->
            if(!value.isEmpty()) {
                param = value.split("=");
                if(value.toLowerCase().indexOf("estacion") > -1) {
                    params.put "p_id", -1L;
                    params.put "p_username", "-1";
                    params.put "p_estacion", param[1];
                } else if(value.toLowerCase().indexOf("rootprocessinstanceid") > -1) {
                    params.put "p_id", Long.parseLong(param[1]);
                } else {
                    params.put "p_name", param[0];
                    params.put "p_value", param[1];
                }
            }
        }
        
        select = "select * from fastpyme.fn_instance(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        List<Solicitud> result = new ArrayList<Solicitud>();
        params.put "p_rownum", Long.parseLong(isNullRequestParam(request, "r1", "0"));
        logger.log Level.SEVERE, "===> Consulta en proceso: " + select;
        logger.log Level.SEVERE, "===> Parametros en proceso: " + params;
        result.addAll(executeQuery(select, params));
        logger.log Level.SEVERE, result.size() + "";
        
        if(!active) {
            select = "select * from fastpyme.fn_arch_instance(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            params.put "p_rownum", Long.parseLong(isNullRequestParam(request, "r2", "0"));
            logger.log Level.SEVERE, "===> Consulta archivadas: " + select;
            logger.log Level.SEVERE, "===> Parametros archivados: " + params;
            result.addAll(executeQuery(select, params));
            logger.log Level.SEVERE, result.size() + "";
        }
        
        Collections.sort(result);
        response.setSolicitudes(result.size() <= rowsForPage ? result : result.subList(0, rowsForPage - 1));
        response.setTotalSolicitudes(response.getSolicitudes().size());
        
        long t2 = System.currentTimeMillis();
        response.setTiempoEjecucion(t2 - t1);
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
    
    private List<Solicitud> executeQuery(String query, Map<String, Object> parameters) throws BussinesException {
        List<Solicitud> result = new ArrayList<Solicitud>();
        Solicitud row = null;
        Connection cn = null
        PreparedStatement ps = null
        ResultSet rs = null
        try {
            InitialContext ic = new InitialContext()
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS")
            cn = ds.getConnection()
            ps = cn.prepareStatement(query)
            
            int i = 1;
            for(String key : parameters.keySet()) {
                ps.setObject(i, parameters.get(key));
                i++;
            }
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                row = new Solicitud();
                row.name = rs.getString("name");
                row.version = rs.getString("version");
                row.tenantid = rs.getLong("tenantid");
                row.rootprocessinstanceid = rs.getLong("rootprocessinstanceid");
                row.startdate = rs.getLong("startdate");
                row.actorid = rs.getLong("actorid");
                row.actor = rs.getString("actor");
                row.flownodedefinitionid = rs.getLong("flownodedefinitionid");
                row.taskname = rs.getString("taskname");
                row.stateid = rs.getLong("stateid");
                row.statename = rs.getString("statename");
                row.assigneeid = rs.getLong("assigneeid");
                row.username = rs.getString("username");
                row.firstname = rs.getString("firstname");
                row.lastname = rs.getString("lastname");
                row.estacion = rs.getString("estacion");
                row.tipo_doi_cliente = rs.getString("tipo_doi_cliente");
                row.num_doi_cliente = rs.getString("num_doi_cliente");
                row.nombre_cliente = rs.getString("nombre_cliente");
                row.estado_solicitud = rs.getString("estado_solicitud");
                row.oferta_aprobada = rs.getString("oferta_aprobada");
                row.ofi_registro = rs.getString("ofi_registro");
                row.num_rvgl = rs.getString("num_rvgl");
                row.producto = rs.getString("producto");
                row.campania = rs.getString("campania");
                row.num_tramite = rs.getString("num_tramite");
                row.num_tramite = rs.getString("num_tramite");
                row.usu_registrante = rs.getString("usu_registrante");
                row.ambito_registro = rs.getString("ambito_registro");
                row.url = rs.getString("url");
                row.codigo_centro_negocio = rs.getString("codigo_centro_negocio");
                row.codigo_cliente = rs.getString("codigo_cliente");
                row.rownum = rs.getLong("rownum");
                row.type_instance = rs.getString("type_instance");
                row.userTask = row.username == null ? "" : (row.username.toString().trim() + " - " + (row.firstname.toString().trim() + " " + row.lastname.toString().trim()).trim());
                result.add row
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
    
    class Solicitud implements Comparable<Solicitud> {
        String name; // character varying
        String version; // character varying
        Long tenantid; // bigint
        Long rootprocessinstanceid; // bigint
        Long startdate; // bigint
        Long actorid; // bigint
        String actor; // character varying
        Long flownodedefinitionid; // bigint
        String taskname; // character varying
        Integer stateid; // integer
        String statename; // character varying
        Long assigneeid; // bigint
        String username; // character varying
        String firstname; // character varying
        String lastname; // character varying
        String estacion; // text
        String tipo_doi_cliente; // text
        String num_doi_cliente; // text
        String nombre_cliente; // text
        String estado_solicitud; // text
        String oferta_aprobada; // text
        String ofi_registro; // text
        String num_rvgl; // text
        String producto; // text
        String campania; // text
        String clte_clasificacion; // text
        String num_tramite; // text
        String usu_registrante; // text
        String ambito_registro; // text
        String url; // text
        String codigo_centro_negocio; // text
        String codigo_cliente; // text
        Long rownum; // bigint
        String type_instance; // text
        String userTask; // text

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Long getTenantid() {
            return tenantid;
        }

        public void setTenantid(Long tenantid) {
            this.tenantid = tenantid;
        }

        public Long getRootprocessinstanceid() {
            return rootprocessinstanceid;
        }

        public void setRootprocessinstanceid(Long rootprocessinstanceid) {
            this.rootprocessinstanceid = rootprocessinstanceid;
        }

        public Long getStartdate() {
            return startdate;
        }

        public void setStartdate(Long startdate) {
            this.startdate = startdate;
        }

        public Long getActorid() {
            return actorid;
        }

        public void setActorid(Long actorid) {
            this.actorid = actorid;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public Long getFlownodedefinitionid() {
            return flownodedefinitionid;
        }

        public void setFlownodedefinitionid(Long flownodedefinitionid) {
            this.flownodedefinitionid = flownodedefinitionid;
        }

        public String getTaskname() {
            return taskname;
        }

        public void setTaskname(String taskname) {
            this.taskname = taskname;
        }

        public Integer getStateid() {
            return stateid;
        }

        public void setStateid(Integer stateid) {
            this.stateid = stateid;
        }

        public String getStatename() {
            return statename;
        }

        public void setStatename(String statename) {
            this.statename = statename;
        }

        public Long getAssigneeid() {
            return assigneeid;
        }

        public void setAssigneeid(Long assigneeid) {
            this.assigneeid = assigneeid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEstacion() {
            return estacion;
        }

        public void setEstacion(String estacion) {
            this.estacion = estacion;
        }

        public String getTipo_doi_cliente() {
            return tipo_doi_cliente;
        }

        public void setTipo_doi_cliente(String tipo_doi_cliente) {
            this.tipo_doi_cliente = tipo_doi_cliente;
        }

        public String getNum_doi_cliente() {
            return num_doi_cliente;
        }

        public void setNum_doi_cliente(String num_doi_cliente) {
            this.num_doi_cliente = num_doi_cliente;
        }

        public String getNombre_cliente() {
            return nombre_cliente;
        }

        public void setNombre_cliente(String nombre_cliente) {
            this.nombre_cliente = nombre_cliente;
        }

        public String getEstado_solicitud() {
            return estado_solicitud;
        }

        public void setEstado_solicitud(String estado_solicitud) {
            this.estado_solicitud = estado_solicitud;
        }

        public String getOferta_aprobada() {
            return oferta_aprobada;
        }

        public void setOferta_aprobada(String oferta_aprobada) {
            this.oferta_aprobada = oferta_aprobada;
        }

        public String getOfi_registro() {
            return ofi_registro;
        }

        public void setOfi_registro(String ofi_registro) {
            this.ofi_registro = ofi_registro;
        }

        public String getNum_rvgl() {
            return num_rvgl;
        }

        public void setNum_rvgl(String num_rvgl) {
            this.num_rvgl = num_rvgl;
        }

        public String getProducto() {
            return producto;
        }

        public void setProducto(String producto) {
            this.producto = producto;
        }

        public String getCampania() {
            return campania;
        }

        public void setCampania(String campania) {
            this.campania = campania;
        }

        public String getClte_clasificacion() {
            return clte_clasificacion;
        }

        public void setClte_clasificacion(String clte_clasificacion) {
            this.clte_clasificacion = clte_clasificacion;
        }

        public String getNum_tramite() {
            return num_tramite;
        }

        public void setNum_tramite(String num_tramite) {
            this.num_tramite = num_tramite;
        }

        public String getUsu_registrante() {
            return usu_registrante;
        }

        public void setUsu_registrante(String usu_registrante) {
            this.usu_registrante = usu_registrante;
        }

        public String getAmbito_registro() {
            return ambito_registro;
        }



        public void setAmbito_registro(String ambito_registro) {
            this.ambito_registro = ambito_registro;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCodigo_centro_negocio() {
            return codigo_centro_negocio;
        }

        public void setCodigo_centro_negocio(String codigo_centro_negocio) {
            this.codigo_centro_negocio = codigo_centro_negocio;
        }

        public String getCodigo_cliente() {
            return codigo_cliente;
        }

        public void setCodigo_cliente(String codigo_cliente) {
            this.codigo_cliente = codigo_cliente;
        }

        public Long getRownum() {
            return rownum;
        }

        public void setRownum(Long rownum) {
            this.rownum = rownum;
        }

        public String getType_instance() {
            return type_instance;
        }

        public void setType_instance(String type_instance) {
            this.type_instance = type_instance;
        }

        public String getUserTask() {
            return userTask;
        }

        public void setUserTask(String userTask) {
            this.userTask = userTask;
        }

        @Override
        public int compareTo(Solicitud o) {
            return this.getRootprocessinstanceid().compareTo(o.getRootprocessinstanceid());
        }
    }
    
    class ConsultaResponse {
        List<Solicitud> solicitudes
        Long totalSolicitudes
        String error
        String status
        Long tiempoEjecucion
        Long ids1
        Long ids2
    }

}
