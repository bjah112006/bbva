package com.bbva.bonita.authentication.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.authentication.AuthenticationConstants;
import org.bonitasoft.engine.authentication.GenericAuthenticationService;
import org.bonitasoft.engine.commons.LogUtil;
import org.bonitasoft.engine.identity.IdentityService;
import org.bonitasoft.engine.identity.SIdentityException;
import org.bonitasoft.engine.identity.SUserNotFoundException;
import org.bonitasoft.engine.identity.model.SUser;
import org.bonitasoft.engine.identity.model.SUserMembership;
import org.bonitasoft.engine.log.technical.TechnicalLogSeverity;
import org.bonitasoft.engine.log.technical.TechnicalLoggerService;

import pe.com.bbva.ws.ldap.servidor.Usuario;

public class JAASGenericAuthenticationServiceImpl implements GenericAuthenticationService {

	private final IdentityService identityService;
	private final TechnicalLoggerService logger;
	private final LDAPService ldapService;

	public JAASGenericAuthenticationServiceImpl(final IdentityService identityService, final TechnicalLoggerService logger) {
        this.identityService = identityService;
        this.logger = logger;
        ldapService = new LDAPService(LDAPValidate.getInstance(logger));
    }

	public SUser modificarUsuario(String userName, Usuario usuario) throws SUserNotFoundException {
        boolean crear = false;
        SUser user = null;
        SUser jefe = null;
        
        try {
            try {
                jefe = identityService.getUserByUserName(usuario.getRegistroJefe());
            } catch(final SUserNotFoundException sunfe) {
                logger.log(this.getClass(), TechnicalLogSeverity.INFO, "Registrando superior ==> " + usuario.getRegistroJefe());
                Usuario usuarioJefe = LDAPValidate.getInstance(logger).obtenerUsuario(usuario.getRegistroJefe());
                ldapService.crearUsuario(usuarioJefe, usuario.getRegistroJefe(), null);
            }
            
            try {
                user = identityService.getUserByUserName(userName);
            } catch(final SUserNotFoundException sunfe) {
                logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogOnExceptionMethod(this.getClass(), "modificarUsuario", sunfe));
                crear = false;
            }
            
            logger.log(this.getClass(), TechnicalLogSeverity.INFO, "Superior ==> " + jefe.getUserName());
            if(crear) {
                ldapService.crearUsuario(usuario, userName, jefe);
            } else {
                ldapService.actualizarUsuario(usuario, user, jefe);
            }
            
        } catch (Exception e) {
            logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
        }
        
        return user == null ? identityService.getUserByUserName(userName) : user;
	}
	
    @Override
    public String checkUserCredentials(Map<String, Serializable> credentials) {
        String methodName = "checkUserCredentials";
        SUser user = null;
        
        try {
        	String password = String.valueOf(credentials.get(AuthenticationConstants.BASIC_PASSWORD));
        	String userName = String.valueOf(credentials.get(AuthenticationConstants.BASIC_USERNAME)).toUpperCase();
            
            if (logger.isLoggable(this.getClass(), TechnicalLogSeverity.TRACE)) {
                logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogBeforeMethod(this.getClass(), methodName));
            }
            
            Usuario usuario = LDAPValidate.getInstance(logger).obtenerUsuario(userName);
            try {
            	user = modificarUsuario(userName, usuario);
            	logger.log(this.getClass(), TechnicalLogSeverity.INFO, user.getFirstName() + " " + user.getLastName());
            } catch (final Exception e) {
            	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "Exception generic");
            	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, LogUtil.getLogOnExceptionMethod(this.getClass(), methodName, e));
            }
            
            logger.log(this.getClass(), TechnicalLogSeverity.ERROR, user.getUserName());
            
            String isValidLDAP = DBUtil.obtenerParametro(DBUtil.FLAG_ACTIVACION_LDAP); 
            boolean isCheckCredentials = false;
            
            if ("0".equalsIgnoreCase(isValidLDAP)) {
            	isCheckCredentials = identityService.chechCredentials(user, password);
            } else {
            	String userBonitaProfile=getPrincipalUserProfileFromBonita(user.getId());
            	if(userBonitaProfile.equals("ABN")){
            		isCheckCredentials = identityService.chechCredentials(user, password);
            	}else{
            		isCheckCredentials = LDAPValidate.getInstance(logger).checkCredentials(userName, password);	
            	}
            }
            
            if (isCheckCredentials) {
                if (logger.isLoggable(this.getClass(), TechnicalLogSeverity.TRACE)) {
                    logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogAfterMethod(this.getClass(), methodName));
                }
                return userName;
            }
            if (logger.isLoggable(this.getClass(), TechnicalLogSeverity.TRACE)) {
                logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogAfterMethod(this.getClass(), methodName));
            }
        } catch (final Exception sunfe) {
            logger.log(this.getClass(), TechnicalLogSeverity.ERROR, LogUtil.getLogOnExceptionMethod(this.getClass(), methodName, sunfe));
        }
        
        return null;
    }
    
    private String getPrincipalUserProfileFromBonita(long userId){
    	String profile="";
    	try {
			List<SUserMembership> lst_membership=identityService.getUserMembershipsOfUser(userId, -1, 3);
			if(lst_membership!=null){
				for(SUserMembership membership:lst_membership){
					profile=membership.getRoleName();
					break;
				}
			}
		} catch (SIdentityException e) {
		    logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
		}
    	return profile;
    }
}
