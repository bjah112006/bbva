package com.bbva.bonita.authentication.impl;

import indra.bbva.centros.webservice.request.ObtenerOficinaRequest;
import indra.bbva.centros.webservice.response.ObtenerOficinaResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.http.HttpResponse;
import org.bonitasoft.engine.identity.model.SUser;

import pe.com.bbva.ws.ldap.servidor.Usuario;
import pe.com.bbva.www.CentrosBBVAWebService.CentrosBBVAWebServiceProxy;

import com.bbva.bonita.rest.RestAPIClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LDAPService {

    private static final Logger logger = Logger.getLogger("LDAPService");
    private String user;
    private String pwd;
    private String server;
    private String horas;
    private String puestosConOficina;
    private String urlCentros;

    public LDAPService(LDAPValidate ldapValidate) {
        super();
        this.user = ldapValidate.getProperty("user_name_bonita");
        this.pwd = ldapValidate.getProperty("password_bonita");
        this.server = ldapValidate.getProperty("server_url") + "/bonita/";
        this.horas = DBUtil.obtenerParametro(DBUtil.HORAS_TRASNCURRIDAS);
        this.puestosConOficina = ldapValidate.getProperty("puesto.con.oficina");
        this.urlCentros = ldapValidate.getProperty("wsdl.centrosBBVA");
    }

    private long diferenciaEnHoras(Date fechaInicio, Date fechaFin) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        // Establecer las fechas
        cal1.setTime(fechaInicio);
        cal2.setTime(fechaFin);
        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        // calcular la diferencia en milisengundos
        long diff = milis2 - milis1;
        // calcular la diferencia en segundos: long diffSeconds = diff / 1000;
        // calcular la diferencia en minutos: long diffMinutes = diff / (60 *
        // 1000);
        // calcular la diferencia en horas: long diffHours = diff / (60 * 60 *
        // 1000);
        // calcular la diferencia en dias: long diffDays = diff / (24 * 60 * 60
        // * 1000);
        return diff / (60 * 60 * 1000);
    }

    public String obtenerIdentificadorUsuario(String userName) throws JsonProcessingException, IOException {
        RestAPIClient restApiClient = new RestAPIClient(server);
        restApiClient.loginAs(user, pwd);
        HttpResponse response = restApiClient.executeGetRequest("/API/identity/user?p=0&c=10&o=lastname%20ASC&s=" + userName + "&f=enabled%3dtrue");
        String result = restApiClient.consumeResponseIfNecessary(response);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = mapper.readTree(result);

        logger.log(Level.SEVERE, result);

        return jsonObj.get("id").textValue();
    }

    private String[] obtenerOficina(String codigoCentro) {
        String[] result = new String[2];
        ObtenerOficinaRequest obtenerOficinaRequest = new ObtenerOficinaRequest();
        obtenerOficinaRequest.setCodOficina(codigoCentro);
        CentrosBBVAWebServiceProxy proxy = new CentrosBBVAWebServiceProxy(urlCentros);
        try {
            ObtenerOficinaResponse obtenerOficinaResponse = proxy.obtenerOficina(obtenerOficinaRequest);
            result[0] = codigoCentro + " - " + obtenerOficinaResponse.getOficina().getNombreOficina();
            result[1] = obtenerOficinaResponse.getOficina().getCodTerritorio();
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Error al acceder al servicio", e);
            result[0] = "";
            result[1] = "";
        }

        return result;
    }

    private String stringEntity(Usuario usuario, String userName, SUser jefe) {
        MessageFormat message = new MessageFormat("\"enabled\":\"true\", \"userName\":\"{0}\", \"password\":\"123456\", \"password_confirm\":\"123456\", \"firstname\":\"{1}\", \"lastname\":\"{2}\", \"professional_data_email\":\"{3}\", \"manager_id\":\"{4}\"");
        String[] params = new String[5];
        params[0] = userName;
        params[1] = (usuario.getPrimerNombre() + " " + (usuario.getSegundoNombre() == null ? "" : usuario.getSegundoNombre())).trim();
        params[2] = (usuario.getPrimerApellido() + " " + usuario.getSegundoApellido()).trim();
        params[3] = usuario.getEMail();
        params[4] = String.valueOf(jefe.getId());

        String entity = "{" + message.format(params) + "}";
        logger.log(Level.SEVERE, entity);
        logger.log(Level.SEVERE, "Puesto: " + usuario.getPuesto().getNombreCargoFuncionalLocal());
        
        return entity;
    }
    
    public void crearUsuario(Usuario usuario, String userName, SUser jefe) throws JsonProcessingException, IOException {
        logger.log(Level.SEVERE, "Registrando usuario");
        String entity = stringEntity(usuario, userName, jefe);

        RestAPIClient restApiClient = new RestAPIClient(server);
        restApiClient.loginAs(user, pwd);
        HttpResponse response = restApiClient.executePostRequest("API/identity/user", entity);
        String result = restApiClient.consumeResponseIfNecessary(response);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonObj = mapper.readTree(result);

        actualizarMembresia(jsonObj.get("id").textValue(), usuario.getPuesto().getNombreCargoFuncionalLocal());
        actualizarDatosAdicionales(jsonObj.get("id").textValue(), usuario);
    }

    public void actualizarDatosAdicionales(String id, Usuario usuario) {
        String[] oficina = obtenerOficina(usuario.getCodigoCentro());
        actualizarDatosAdicionales(id, "301", usuario.getPuesto().getNombreCargoFuncionalLocal()); // Puesto

        if (!oficina[0].isEmpty()) {
            actualizarDatosAdicionales(id, "101", oficina[0]); // Oficina
        }

        if (puestosConOficina.indexOf("|" + usuario.getPuesto().getNombreCargoFuncionalLocal() + "|") > -1) {
            if (!oficina[1].isEmpty()) {
                actualizarDatosAdicionales(id, "201", oficina[1]); // Ambito
            }
        }
    }

    public void actualizarUsuario(Usuario usuario, SUser user, SUser jefe) {
        Date lastConnection = new Date(user.getSUserLogin().getLastConnection());
        long horasTranscurridas = Long.valueOf(this.horas);
        long diff = diferenciaEnHoras(lastConnection, new Date());

        logger.log(Level.SEVERE, "Last Connection: " + lastConnection.toString());
        logger.log(Level.SEVERE, "Tiempo transcurrido: " + diff);
        logger.log(Level.SEVERE, "Tiempo a esperar: " + this.horas);
        
        if (diff > horasTranscurridas) {
            logger.log(Level.SEVERE, "Actualizando usuario");
            String entity = stringEntity(usuario, user.getUserName(), jefe);

            RestAPIClient restApiClient = new RestAPIClient(server);
            restApiClient.loginAs(this.user, pwd);
            HttpResponse response = restApiClient.executePutRequest("API/identity/user/" + user.getId(), entity);
            restApiClient.consumeResponse(response, true);
            restApiClient.shutdown();
            actualizarDatosAdicionales(String.valueOf(user.getId()), usuario);
        }
    }

    public void actualizarDatosAdicionales(String id, String key, String value) {
        String entity = "{\"value\":\"" + value + "\"}";

        logger.log(Level.SEVERE, "API/customuserinfo/value/" + id + "/" + key + "==>" + entity);
        
        RestAPIClient restApiClient = new RestAPIClient(server);
        restApiClient.loginAs(user, pwd);
        restApiClient.executePutRequest("API/customuserinfo/value/" + id + "/" + key, entity);
        restApiClient.shutdown();
    }

    public void actualizarMembresia(String id, String puesto) {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM BBVA_PERFIL WHERE PUESTO='" + puesto + "'");
            ResultSet rs = ps.executeQuery();

            RestAPIClient restApiClient = new RestAPIClient(server);
            restApiClient.loginAs(user, pwd);
            while (rs.next()) {
                logger.log(Level.SEVERE, "ID_ROLE: " + rs.getInt("ID_ROLE"));
                logger.log(Level.SEVERE, "ID_GROUP: " + rs.getInt("ID_GROUP"));
                restApiClient.executePostRequest("API/identity/membership", "{\"user_id\":\"" + id + "\",\"group_id\":\"" + rs.getInt("ID_GROUP") + "\",\"role_id\":\"" + rs.getInt("ID_ROLE") + "\"}");
            }

            rs.close();
            ps.close();
            cn.close();

            // Request url http://../API/identity/membership/<the id of the
            // user>/<the id of the group>/<the id of the role>
            // Request method
            // DELETE

            // http://../API/identity/membership?p=0&c=10&f=user_id%3d<the id of
            // the user>

            restApiClient.shutdown();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "actualizarMembresia", e);
        }
    }
}