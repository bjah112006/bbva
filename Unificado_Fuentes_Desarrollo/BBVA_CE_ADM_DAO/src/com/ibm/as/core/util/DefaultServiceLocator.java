package com.ibm.as.core.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.as.core.exception.ServiceLocationException;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.bbva.tabla.vo.ParametrosConfVO;


public class DefaultServiceLocator extends AbstractServiceLocator {

	private static DefaultServiceLocator instance = null;

	public static DefaultServiceLocator getInstance() throws ServiceLocationException {
		if (instance == null) {
			instance = new DefaultServiceLocator();
		}
		return instance;
	}

	@Override
	protected Context getInitialContext(boolean isLocal) throws ServiceLocationException {
		try {
			if (isLocal) {
				if (ctx == null)
					ctx = new InitialContext();

				return ctx;
			}
			else {
				if ( ctxRemote==null ) {
					Properties props = new Properties();

					//props.put(InitialContext.PROVIDER_URL, ParametersDelegate.getInstance().getValue(CommonConstants.BBVA_WPS_JAVA_NAMING_PROVIDER_URL)); 
					//props.put(InitialContext.INITIAL_CONTEXT_FACTORY, ParametersDelegate.getInstance().getValue(CommonConstants.BBVA_WPS_JAVA_NAMING_FACTORY_INITIAL)); 

					//ParametrosConfDAO parametrosConfDAO = TablaFacadeBean.daoFactory
					
					//props.put(InitialContext.PROVIDER_URL, ConstantesServidor.BBVA_WPS_JAVA_NAMING_PROVIDER_URL); 
					//props.put(InitialContext.INITIAL_CONTEXT_FACTORY, ConstantesServidor.BBVA_WPS_JAVA_NAMING_FACTORY_INITIAL);
					
					TablaFacadeBean tablaFacadeBean = new TablaFacadeBean();					
					ParametrosConfVO param1 = tablaFacadeBean.getParametrosConf(Constantes.ID_APLICATIVO_TC, "BBVA_WPS_JAVA_NAMING_PROVIDER_URL");
					props.put(InitialContext.PROVIDER_URL, param1.getValorVariable());
					
					ParametrosConfVO param2 = tablaFacadeBean.getParametrosConf(Constantes.ID_APLICATIVO_TC, "BBVA_WPS_JAVA_NAMING_FACTORY_INITIAL");
					props.put(InitialContext.INITIAL_CONTEXT_FACTORY, param2.getValorVariable());
					
					//LOG.info("BBVA_WPS_JAVA_NAMING_PROVIDER_URL : "+param1.getValorVariable());
					//LOG.info("BBVA_WPS_JAVA_NAMING_FACTORY_INITIAL : "+param2.getValorVariable());
					
					ctxRemote = new InitialContext(props);
				}

				return ctxRemote;
			}
		}
		catch (NamingException ne) {
			throw new ServiceLocationException(ne);
		}
		catch (Exception e) {
			throw new ServiceLocationException(e);
		}
	}
}
