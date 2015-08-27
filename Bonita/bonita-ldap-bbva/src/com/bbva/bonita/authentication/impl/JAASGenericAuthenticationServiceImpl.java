package com.bbva.bonita.authentication.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.authentication.AuthenticationConstants;
import org.bonitasoft.engine.authentication.GenericAuthenticationService;
import org.bonitasoft.engine.commons.LogUtil;
import org.bonitasoft.engine.identity.IdentityService;
import org.bonitasoft.engine.identity.SIdentityException;
import org.bonitasoft.engine.identity.model.SUser;
import org.bonitasoft.engine.identity.model.SUserMembership;
import org.bonitasoft.engine.log.technical.TechnicalLogSeverity;
import org.bonitasoft.engine.log.technical.TechnicalLoggerService;

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
        boolean isCheckCredentials = false;
        
        try {
        	String password = String.valueOf(credentials.get(AuthenticationConstants.BASIC_PASSWORD));
        	String userName = String.valueOf(credentials.get(AuthenticationConstants.BASIC_USERNAME));
            
            if (logger.isLoggable(this.getClass(), TechnicalLogSeverity.TRACE)) {
                logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogBeforeMethod(this.getClass(), methodName));
            }
            
            String bonitaUser = LDAPValidate.getInstance().getProperty("user_name_bonita");
            // logger.log(this.getClass(), TechnicalLogSeverity.ERROR, bonitaUser);
            // logger.log(this.getClass(), TechnicalLogSeverity.ERROR, userName);
            // logger.log(this.getClass(), TechnicalLogSeverity.ERROR, password);
            if(bonitaUser.equalsIgnoreCase(userName)) {
                user = identityService.getUserByUserName(userName);
                logger.log(this.getClass(), TechnicalLogSeverity.ERROR, user.getUserName());
                isCheckCredentials = identityService.chechCredentials(user, password);
                if (isCheckCredentials) {
                    if (logger.isLoggable(this.getClass(), TechnicalLogSeverity.TRACE)) {
                        logger.log(this.getClass(), TechnicalLogSeverity.TRACE, LogUtil.getLogAfterMethod(this.getClass(), methodName));
                    }
                    return userName;
                }
                return null;
            }
            
            userName = String.valueOf(credentials.get(AuthenticationConstants.BASIC_USERNAME)).toUpperCase();
            
            String isVerificarUsuario= DBUtil.obtenerParametro(DBUtil.FLAG_VERIFICAR_USUARIO); 
            if ("0".equalsIgnoreCase(isVerificarUsuario)) {
                try {
                    LDAPService ldapService = new LDAPService();
                    ldapService.verificarUsuario(userName);
                	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, userName);
                } catch (final Exception e) {
                	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "Exception generic");
                	logger.log(this.getClass(), TechnicalLogSeverity.ERROR, LogUtil.getLogOnExceptionMethod(this.getClass(), methodName, e));
                }
            }
            
            user = identityService.getUserByUserName(userName);
            logger.log(this.getClass(), TechnicalLogSeverity.ERROR, user.getUserName());
            
            String isValidLDAP = DBUtil.obtenerParametro(DBUtil.FLAG_ACTIVACION_LDAP); 
            
            if ("0".equalsIgnoreCase(isValidLDAP)) {
                isCheckCredentials = identityService.chechCredentials(user, password);
            } else {
                String userBonitaProfile = getPrincipalUserProfileFromBonita(user.getId());
                String roles = DBUtil.obtenerParametro(DBUtil.ROLES_EXCLUIDOS);

                logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "Roles Bonita: " + roles);
                logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "Profile: " + userBonitaProfile);

                if (roles.indexOf("|" + userBonitaProfile + "|") > -1) {
                    isCheckCredentials = identityService.chechCredentials(user, password);
                } else {
                    isCheckCredentials = LDAPValidate.getInstance().checkCredentials(userName, password);
                    logger.log(this.getClass(), TechnicalLogSeverity.ERROR, "Autentifica LDAP");
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
