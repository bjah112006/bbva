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

	public JAASGenericAuthenticationServiceImpl(final IdentityService identityService, final TechnicalLoggerService logger) {
        this.identityService = identityService;
        this.logger = logger;
    }

    @Override
    public String checkUserCredentials(Map<String, Serializable> credentials) {
        String methodName = "checkUserCredentials";
        SUser user = null;
        LDAPService ldapService = new LDAPService(LDAPValidate.getInstance(logger));
        
        try {
        	String password = String.valueOf(credentials.get(AuthenticationConstants.BASIC_PASSWORD));
        	String userName = String.valueOf(credentials.get(AuthenticationConstants.BASIC_USERNAME));
            
            if (logger.isLoggable(this.getClass(), TechnicalLogSeverity.TRACE)) {
                logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogBeforeMethod(this.getClass(), methodName));
            }
            
            try {
            	user = identityService.getUserByUserName(userName);
            	logger.log(this.getClass(), TechnicalLogSeverity.INFO, user.getFirstName());
            } catch (final SUserNotFoundException sunfe) {
				try {
					Usuario usuario = LDAPValidate.getInstance(logger).obtenerUsuario(userName);
					ldapService.crearUsuario(usuario, userName);
				} catch (Exception e) {
					logger.log(this.getClass(), TechnicalLogSeverity.ERROR, e);
				}
            } catch (final Exception e) {
            	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "Exception generic");
            	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, LogUtil.getLogOnExceptionMethod(this.getClass(), methodName, e));
            }
            
            user = identityService.getUserByUserName(userName);
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
