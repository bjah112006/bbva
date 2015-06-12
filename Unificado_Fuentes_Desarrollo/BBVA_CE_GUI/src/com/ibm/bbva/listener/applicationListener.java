package com.ibm.bbva.listener;

import javax.ejb.EJB;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractLinksMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.session.ExpedienteBeanLocal;

public class applicationListener implements HttpSessionListener  {

	private static final Logger LOG = LoggerFactory.getLogger(applicationListener.class);

	@EJB
	ExpedienteBeanLocal expedienteBean;
    
    public applicationListener(){    	

    }

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Apéndice de método generado automáticamente
		LOG.info("Eliminando flag de expediente activo .... sessionDestroyed");
		actualizarEstadoExpediente(se);
		
	}
	
	private void actualizarEstadoExpediente(HttpSessionEvent se){
		//Eliminar estado activo del ultimo expediente trabajado en session
		String idExpediente = (String) se.getSession().getValue(Constantes.EXPEDIENTE_ESTADO);
		//String idExpediente = (String) getObjectSession(Constantes.EXPEDIENTE_ESTADO);
		LOG.info("idExpediente ::: "+idExpediente);
		
		if(idExpediente!=null && !idExpediente.trim().equals("")){
			//Actualiza estado activo de Expediente para el momento de la reasignación			
			expedienteBean.actualizarEstadoExpediente(Constantes.FLAG_ESTADO_INACTIVO_EXPEDIENTE, Long.parseLong(idExpediente));
			LOG.info("Se desactivo el exp "+idExpediente);
		}
		//removeObjectSession(Constantes.EXPEDIENTE_ESTADO);		
	}	
    
    
    
}
