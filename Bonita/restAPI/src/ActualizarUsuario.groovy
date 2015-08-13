import java.io.IOException;

import org.apache.http.HttpResponse;

import com.bbva.bonita.rest.RestAPIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import indra.bbva.centros.webservice.request.ObtenerOficinaRequest;
import indra.bbva.centros.webservice.response.ObtenerOficinaResponse;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import groovy.json.JsonBuilder

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.page.PageContext;
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.RestApiController;
import org.bonitasoft.console.common.server.page.RestApiResponse;
import org.bonitasoft.console.common.server.page.RestApiResponseBuilder;
import org.bonitasoft.console.common.server.page.RestApiUtil;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.commons.LogUtil;
import org.bonitasoft.engine.identity.ContactDataCreator;
import org.bonitasoft.engine.identity.ContactDataUpdater;
import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.engine.identity.SUserNotFoundException;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.engine.identity.model.SUser;
import org.bonitasoft.engine.log.technical.TechnicalLogSeverity;
import org.bonitasoft.engine.session.APISession;

import com.bbva.bonita.authentication.impl.DBUtil;
import com.bbva.bonita.authentication.impl.LDAPValidate;

import pe.com.bbva.ws.ldap.servidor.Usuario;
import pe.com.bbva.www.CentrosBBVAWebService.CentrosBBVAWebServiceProxy;

public class ActualizarUsuario implements RestApiController  {

    Logger logger
    APISession apiSession
    IdentityAPI identityAPI
    
    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        Map<String, Object> response = new HashMap<String, Object>()
        HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor(request)
        String username = request.getParameter "username"

        logger = restApiUtil.logger
        apiSession = requestAccessor.apiSession
        identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession)
        
        try {
            SUser user = verificarUsuario(username)
            
            response.put "estado", "OK"
        } catch(Exception e) {
            response.put "estado", "KO"
            response.put "error", exceptionToString(e)
        }
        
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toString()
            build()
        }
    }
    
    private SUser verificarUsuario(String username) throws SUserNotFoundException {
        boolean crear = false
        boolean actualizar = false
        SUser user = null
        SUser jefe = null
        Usuario usuario = LDAPValidate.getInstance().obtenerUsuario(username)
        
        try {
            try {
                jefe = identityAPI.getUserByUserName(usuario.getRegistroJefe())
            } catch(final SUserNotFoundException sunfe) {
                logger.log(Level.SEVERE, "Se creara el usuario superior ==> " + usuario.getRegistroJefe())
                
                Usuario usuarioJefe = LDAPValidate.getInstance().obtenerUsuario(usuario.getRegistroJefe())
                jefe = crearUsuario(usuarioJefe, usuario.getRegistroJefe(), null);
            }
            
            try {
                user = identityAPI.getUserByUserName(username)
                actualizar = verificarActualizacion(user)
            } catch(final SUserNotFoundException sunfe) {
                logger.log(Level.SEVERE, "Se creara el usuario: " + username)
                crear = true
            }
            
            if(crear) {
                user = crearUsuario(usuario, username, jefe)
            } else if(actualizar) {
                actualizarUsuario(usuario, user, jefe)
            }
            
            if(crear || actualizar) {
                String puestosConOficina = LDAPValidate.getInstance().getProperty("puesto.con.oficina")
                String[] oficina = obtenerOficina(usuario.getCodigoCentro())

                /***
                 * - 1;"AMBITO"
                 * - 3;"OFICINA"
                 * - 4;"PUESTO"
                 **/
                if (puestosConOficina.indexOf("|" + usuario.getPuesto().getNombreCargoFuncionalLocal() + "|") > -1 && !oficina[1].isEmpty()) {
                    identityAPI.setCustomUserInfoValue(1, user.getId(), oficina[1])
                }
                
                if (!oficina[0].isEmpty()) {
                    identityAPI.setCustomUserInfoValue(3, user.getId(), oficina[0])
                }
                
                identityAPI.setCustomUserInfoValue(4, user.getId(), usuario.getPuesto().getNombreCargoFuncionalLocal())
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "verificarUsuario", e)
        }
        
        return user
    }
    
    private boolean verificarActualizacion(SUser user) {
        String horas = DBUtil.obtenerParametro(DBUtil.HORAS_TRASNCURRIDAS)
        Date lastConnection = new Date(user.getSUserLogin().getLastConnection())
        long horasTranscurridas = Long.valueOf(horas)
        long diff = diferenciaEnHoras(lastConnection, new Date())

        logger.log(Level.SEVERE, "Last Connection: " + lastConnection.toString())
        logger.log(Level.SEVERE, "Tiempo transcurrido: " + diff)
        logger.log(Level.SEVERE, "Tiempo a esperar: " + horas)
        
        return (diff > horasTranscurridas)
    }
    
    private long diferenciaEnHoras(Date fechaInicio, Date fechaFin) {
        Calendar cal1 = Calendar.getInstance()
        Calendar cal2 = Calendar.getInstance()

        cal1.setTime(fechaInicio)
        cal2.setTime(fechaFin)
        
        long milis1 = cal1.getTimeInMillis()
        long milis2 = cal2.getTimeInMillis()
        long diff = milis2 - milis1;

        return diff / (60 * 60 * 1000)
    }
    
    private String[] obtenerOficina(String codigoCentro) {
        String wsdl = LDAPValidate.getInstance().getProperty("wsdl.centrosBBVA");
        String[] result = new String[2]
        ObtenerOficinaRequest obtenerOficinaRequest = new ObtenerOficinaRequest()
        obtenerOficinaRequest.setCodOficina(codigoCentro)
        CentrosBBVAWebServiceProxy proxy = new CentrosBBVAWebServiceProxy(wsdl)
        try {
            ObtenerOficinaResponse obtenerOficinaResponse = proxy.obtenerOficina(obtenerOficinaRequest)
            result[0] = codigoCentro + " - " + obtenerOficinaResponse.getOficina().getNombreOficina()
            result[1] = obtenerOficinaResponse.getOficina().getCodTerritorio()
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Error al acceder al servicio", e)
            result[0] = ""
            result[1] = ""
        }

        return result
    }
    
    private void actualizarUsuario(Usuario usuario, SUser user, SUser jefe) {
        logger.log(Level.SEVERE, "Actualizando el usuario: " + usuario);
        
        ContactDataUpdater contactDataUpdater = new ContactDataUpdater()
        contactDataUpdater.setEmail(usuario.getEMail())
        
        UserUpdater userUpdater = new UserUpdater()
        userUpdater.setEnabled(true)
        userUpdater.setFirstName(usuario.getNombres())
        userUpdater.setLastName(usuario.getApellidos())
        userUpdater.setManagerId(jefe.getId())
        userUpdater.setProfessionalContactData(contactDataUpdater)
        
        identityAPI.updateUser(user.getId(), userUpdater)
    }
    
    private SUser crearUsuario(Usuario usuario, String username, SUser jefe) {
        logger.log(Level.SEVERE, "Registrando el usuario: " + usuario);
        
        ContactDataCreator contactDataCreator = new ContactDataCreator()
        contactDataCreator.setEmail(usuario.getEMail())
        
        UserCreator userCreator = new UserCreator(username, "123456")
        userCreator.setEnabled(true)
        userCreator.setFirstName(usuario.getNombres())
        userCreator.setLastName(usuario.getApellidos())
        userCreator.setManagerUserId(jefe.getId())
        userCreator.setProfessionalContactData(contactDataCreator)
                
        identityAPI.createUser(userCreator)
        SUser user = identityAPI.getUserByUserName(username)
        
        return user;
    }

    private String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter(0)
        PrintWriter out = new PrintWriter(sw)
        e.printStackTrace(out)
        return sw.toString().replace("\"", "'")
    }
}
