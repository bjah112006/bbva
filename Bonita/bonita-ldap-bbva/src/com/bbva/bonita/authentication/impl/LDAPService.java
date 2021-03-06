package com.bbva.bonita.authentication.impl;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.identity.ContactDataCreator;
import org.bonitasoft.engine.identity.ContactDataUpdater;
import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.identity.UserMembership;
import org.bonitasoft.engine.identity.UserMembershipCriterion;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.identity.UserUpdater;

import pe.com.bbva.centrosbbvawebservice.CentrosBBVAWebService;
import pe.com.bbva.centrosbbvawebservice.CentrosBBVAWebService_Service;
import pe.com.bbva.centrosbbvawebservice.ObtenerOficinaRequest;
import pe.com.bbva.centrosbbvawebservice.ObtenerOficinaResponse;
import pe.com.bbva.ws.ldap.servidor.Usuario;

public class LDAPService {

    private static final Logger logger = Logger.getLogger("LDAPService");
    private static final QName SERVICE_NAME = new QName("http://www.bbva.com.pe/CentrosBBVAWebService/", "CentrosBBVAWebService");
    private BonitaClientAPI bonitaClientAPI;
    private IdentityAPI identityAPI;
    
    public LDAPService() {
        super();
        try {
            bonitaClientAPI = new BonitaClientAPI();
            identityAPI = bonitaClientAPI.getIdentityAPI();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "LDAPService", e);
        }
        logger.log(Level.SEVERE, "LDAPService creado");
    }
    
    public User verificarUsuario(String username) throws AlreadyExistsException, UserNotFoundException, CreationException, UpdateException {
        boolean crear = false;
        boolean actualizar = false;
        User user = null;
        User jefe = null;
        logger.log(Level.SEVERE, "Consultando servicio de LDAP");
        Usuario usuario = LDAPValidate.getInstance().obtenerUsuario(username);
        
        try {
            jefe = identityAPI.getUserByUserName(usuario.getRegistroJefe());
        } catch(final UserNotFoundException sunfe) {
            logger.log(Level.SEVERE, "Se creara el usuario superior ==> " + usuario.getRegistroJefe());
            
            Usuario usuarioJefe = LDAPValidate.getInstance().obtenerUsuario(usuario.getRegistroJefe());
            jefe = crearUsuario(usuarioJefe, usuario.getRegistroJefe(), null);
            actualizarDatosUsuario(true, false, usuarioJefe, jefe, usuario.getRegistroJefe());
        }
        
        try {
            user = identityAPI.getUserByUserName(username);
            actualizar = verificarActualizacion(user);
        } catch(final UserNotFoundException sunfe) {
            logger.log(Level.SEVERE, "Se creara el usuario: " + username);
            crear = true;
        }
        
        if(crear) {
            user = crearUsuario(usuario, username, jefe);
        } else if(actualizar) {
            actualizarUsuario(usuario, user, jefe);
        }
        actualizarDatosUsuario(crear, actualizar, usuario, user, username);
        
        return user;
    }
    
    private void actualizarDatosUsuario(boolean crear, boolean actualizar, Usuario usuario, User user, String username) {
        try {
            if(crear || actualizar) {
                String puestosConOficina = LDAPValidate.getInstance().getProperty("puesto.con.oficina");
                String[] oficina = obtenerOficina(usuario.getCodigoCentro());
                String ambito = "";
                logger.log(Level.SEVERE, "Territorio: " + oficina[1]);
                logger.log(Level.SEVERE, "Oficina: " + oficina[0]);
                logger.log(Level.SEVERE, "Cod. Oficina: " + oficina[2]);
                
                long idVariableAmbito = 0L;
                long idVariableCentroNegocio = 0L;
                long idVariableOficina = 0L;
                long idVariablePuesto = 0L;
                long idVariableTerritorio = 0L;
                
                List<CustomUserInfoDefinition> definitions = identityAPI.getCustomUserInfoDefinitions(0, 100);
                
                for(CustomUserInfoDefinition def : definitions) {
                    if(def.getName().equalsIgnoreCase("AMBITO")) {
                        idVariableAmbito = def.getId();
                    } else if(def.getName().equalsIgnoreCase("CN")) {
                        idVariableCentroNegocio = def.getId();
                    } else if(def.getName().equalsIgnoreCase("OFICINA")) {
                        idVariableOficina = def.getId();
                    } else if(def.getName().equalsIgnoreCase("PUESTO")) {
                        idVariablePuesto = def.getId();
                    } else if(def.getName().equalsIgnoreCase("TERRITORIO")) {
                        idVariableTerritorio = def.getId();
                    }
                    
                    logger.log(Level.SEVERE, def.getName() + ": " + def.getId());
                }
                
                /***
                 * -   1: "AMBITO"
                 * -   2: "CENTRO NEGOCIOS"
                 * -   3: "OFICINA"
                 * -   4: "PUESTO"
                 * - 101: "TERRITORIO"
                 **/
                if (puestosConOficina.indexOf("|" + usuario.getPuesto().getNombreCargoFuncionalLocal() + "|") > -1 && !oficina[1].isEmpty()) {
                    ambito = DBUtil.obtenerAmbito(oficina[2]); 
                    identityAPI.setCustomUserInfoValue(idVariableAmbito, user.getId(), ambito == null || ambito.isEmpty() ? oficina[1] : ambito);
                    
                    logger.log(Level.SEVERE, "Ambito Consultado: " + ambito);
                }
                
                if (!oficina[1].isEmpty()) {
                    String centroNegocio =DBUtil.obtenerCentroNegocio(ambito);
                    identityAPI.setCustomUserInfoValue(idVariableCentroNegocio, user.getId(), centroNegocio);
                    
                    logger.log(Level.SEVERE, "Centro de Negocio: " + centroNegocio);
                }
                
                if (!oficina[0].isEmpty()) {
                    identityAPI.setCustomUserInfoValue(idVariableOficina, user.getId(), oficina[0]);
                }
                
                if (!oficina[1].isEmpty()) {
                    identityAPI.setCustomUserInfoValue(idVariableTerritorio, user.getId(), oficina[1]);
                }
                
                identityAPI.setCustomUserInfoValue(idVariablePuesto, user.getId(), usuario.getPuesto().getNombreCargoFuncionalLocal());
                actualizarMembresia(user, usuario.getPuesto().getNombreCargoFuncionalLocal(), actualizar);
            }
        } catch(final UpdateException sunfe) {
            logger.log(Level.SEVERE, "Se creara el usuario: " + username);
            crear = true;
        }
    }

    private boolean verificarActualizacion(User user) {
        String horas = DBUtil.obtenerParametro(DBUtil.HORAS_TRASNCURRIDAS);
        Date lastConnection = user.getLastConnection();
        long horasTranscurridas = Long.valueOf(horas);
        long diff = diferenciaEnHoras(lastConnection, new Date());

        logger.log(Level.SEVERE, "Last Connection: " + lastConnection.toString());
        logger.log(Level.SEVERE, "Tiempo transcurrido: " + diff);
        logger.log(Level.SEVERE, "Tiempo a esperar: " + horas);
        
        return (diff > horasTranscurridas);
    }
    
    private long diferenciaEnHoras(Date fechaInicio, Date fechaFin) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        long result;
        
        try {
            cal1.setTime(fechaInicio);
            cal2.setTime(fechaFin);
            
            long milis1 = cal1.getTimeInMillis();
            long milis2 = cal2.getTimeInMillis();
            long diff = milis2 - milis1;
    
            result = diff / (60 * 60 * 1000);
        } catch(Exception e) {
            result = 0;
        }
        
        return result;
    }
    
    private String[] obtenerOficina(String codigoCentro) {
        String wsdl = LDAPValidate.getInstance().getProperty("wsdl.centrosBBVA");
        String[] result = new String[3];
        ObtenerOficinaRequest obtenerOficinaRequest = new ObtenerOficinaRequest();
        obtenerOficinaRequest.setCodOficina(codigoCentro);

        result[0] = "";
        result[1] = "";
        result[2] = "";
        
        try {
            URL wsdlURL = new URL(wsdl);
            CentrosBBVAWebService_Service centrosService = new CentrosBBVAWebService_Service(wsdlURL, SERVICE_NAME);
            CentrosBBVAWebService port = centrosService.getCentrosBBVAWebServiceSOAP();  
            ObtenerOficinaResponse obtenerOficinaResponse = port.obtenerOficina(obtenerOficinaRequest);
            if(obtenerOficinaResponse != null && obtenerOficinaResponse.getOficina() != null) {
                result[0] = codigoCentro + " - " + obtenerOficinaResponse.getOficina().getNombreOficina();
                result[1] = "0" + StringUtils.right(obtenerOficinaResponse.getOficina().getTerritorio().getId().trim(), 3);
                result[2] = codigoCentro;
            } else {
                logger.log(Level.SEVERE, "Codigo de centro " + codigoCentro + " no registrado");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al acceder al servicio", e);
        }

        return result;
    }
    
    private void actualizarUsuario(Usuario usuario, User user, User jefe) throws UserNotFoundException, UpdateException {
        logger.log(Level.SEVERE, "Actualizando el usuario: " + usuario);
        
        ContactDataUpdater contactDataUpdater = new ContactDataUpdater();
        contactDataUpdater.setEmail(usuario.getEMail());
        
        UserUpdater userUpdater = new UserUpdater();
        userUpdater.setEnabled(true);
        userUpdater.setFirstName(usuario.getNombres().toUpperCase());
        userUpdater.setLastName(usuario.getApellidos().toUpperCase());
        if(jefe != null) {
            userUpdater.setManagerId(jefe.getId());
        }
        userUpdater.setProfessionalContactData(contactDataUpdater);
        userUpdater.setPersonalContactData(contactDataUpdater);
        
        identityAPI.updateUser(user.getId(), userUpdater);
    }
    
    private User crearUsuario(Usuario usuario, String username, User jefe) throws AlreadyExistsException, CreationException, UserNotFoundException {
        logger.log(Level.SEVERE, "Registrando el usuario: " + usuario);
        
        ContactDataCreator contactDataCreator = new ContactDataCreator();
        contactDataCreator.setEmail(usuario.getEMail());
        
        UserCreator userCreator = new UserCreator(username, "123456");
        userCreator.setEnabled(true);
        userCreator.setFirstName(usuario.getNombres().toUpperCase());
        userCreator.setLastName(usuario.getApellidos().toUpperCase());
        if(jefe != null) {
            userCreator.setManagerUserId(jefe.getId());
        }
        userCreator.setProfessionalContactData(contactDataCreator);
        userCreator.setPersonalContactData(contactDataCreator);

        return identityAPI.createUser(userCreator);
    }
    
    private void actualizarMembresia(User user, String puesto, boolean actualizar) {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/bonitaSequenceManagerDS");
            Connection cn = ds.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM BBVA_PERFIL WHERE PUESTO='" + puesto + "'");
            ResultSet rs = ps.executeQuery();

            UserMembership membership = null;
            List<UserMembership> userMemberships = identityAPI.getUserMemberships(user.getId(), 0, 100, UserMembershipCriterion.ASSIGNED_DATE_DESC);
            long idRole;
            long idGroup;
            while (rs.next()) {
                idRole = rs.getLong("ID_ROLE");
                idGroup = rs.getLong("ID_GROUP");
                logger.log(Level.SEVERE, "ID_ROLE: " + idRole);
                logger.log(Level.SEVERE, "ID_GROUP: " + idGroup);
                if(actualizar = false || userMemberships.isEmpty()) {
                    logger.log(Level.SEVERE, "Membresia adicionada");
                    identityAPI.addUserMembership(user.getId(), idGroup, idRole);
                } else {
                    membership = null;
                    for(UserMembership um : userMemberships) {
                        if(um.getGroupId() == idGroup && um.getRoleId() == idRole) {
                            membership = um;
                            break;
                        }
                    }
                    
                    if(membership == null) {
                        logger.log(Level.SEVERE, "Membresia adicionada Actualización");
                        identityAPI.addUserMembership(user.getId(), idGroup, idRole);
                    } /* else {
                        userMemberships.remove(membership);
                    }*/
                }
            }

            /*
            boolean existe;
            for(UserMembership um : userMemberships) {
                existe = false;
                for(String m : memberships) {
                    if((um.getGroupId() + "=" + um.getRoleId()).equalsIgnoreCase(m)) {
                        existe = true;
                        break;
                    }
                }
                
                if(!existe) {
                    identityAPI.deleteUserMembership(um.getId());
                }
            }
            */
            
            rs.close();
            ps.close();
            cn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "actualizarMembresia", e);
        }
    }
}