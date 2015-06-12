package com.ibm.bbva.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.messages.Mensajes;


public abstract class AbstractMBean implements Serializable {

	private static final long serialVersionUID = 2298554199433638716L;
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMBean.class);

	/**
	 * Agrega un mensaje de error
	 * @param idComponente Identificador del componente
	 * @param idMensaje Identificador del mensaje en el archivo de propiedades
	 * @param params Parametros para el mensaje
	 */
	protected void addMessageError(String idComponente, String idMensaje, Object... params) {
		if (idMensaje != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(idComponente, Mensajes.getFacesMessageError(idMensaje, params));
		}
	}
	
	/**
	 * Obtiene un objeto del ambito Request
	 * @param nombreMB Nombre del objeto
	 * @return
	 */
	public Object getObjectRequest (String nombreMB) {
		Object objeto = null;
		if (nombreMB != null) {
			objeto = FacesContext.getCurrentInstance().getExternalContext()
            		.getRequestMap().get(nombreMB);
		}
		return objeto;
	}
	
	/**
	 * Asigna un objeto al ambito Request
	 * @param nombreMB Nombre del objeto 
	 * @param objeto Objeto que se agregara a la sesion
	 */
	protected void addObjectRequest (String nombreMB, Object objeto) {
		FacesContext.getCurrentInstance().getExternalContext()
        		.getRequestMap().put(nombreMB, objeto);
	}
	
	/**
	 * Obtiene un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 * @return
	 */
	public Object getObjectSession (String nombreMB) {
		Object objeto = null;
		if (nombreMB != null) {
			objeto = FacesContext.getCurrentInstance().getExternalContext()
            		.getSessionMap().get(nombreMB);
		}
		return objeto;
	}
	
	/**
	 * Alamacena un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 * @param objeto Objeto que se almacenara en la sesion
	 */
	public void addObjectSession (String nombreMB, Object objeto) {
		FacesContext.getCurrentInstance().getExternalContext()
            		.getSessionMap().put(nombreMB, objeto);
	}
	
	/**
	 * Retorna el Id de la session
	 * @return 
	 */
	public String getIdSession () {
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		return session.getId();
	}
	
	/**
	 * Elimina un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 */
	protected void removeObjectSession (String nombreMB) {
		FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(nombreMB);
	}
	
	/**
	 * Obtiene un objeto del ambito Application
	 * @param nombreMB Nombre del objeto
	 * @return
	 */
	protected Object getObjectApplication (String nombreMB) {
		Object objeto = null;
		if (nombreMB != null) {
			objeto = FacesContext.getCurrentInstance().getExternalContext()
            		.getApplicationMap().get(nombreMB);
		}
		return objeto;
	}
	
	/**
	 * @return Nombre del JSP Principal
	 */
	public String getNombreJSPPrincipal () {
		FacesContext context = FacesContext.getCurrentInstance();
	    String path = context.getViewRoot().getViewId();
	    return path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
	}
	
	/**
	 * @param parameter Nombre del parametro
	 * @return Parametro de la solicitud
	 */
	protected String getRequestParameter (String parameter) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
        		.getRequestParameterMap().get(parameter);
	}
	
	/**
	 * @return Retorna el Objeto ExternalContext del FacesContext
	 */
	protected ExternalContext getExternalContext () {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	/**
	 * Refresca la pagina jsp
	 */
	protected void refrescarJSP () {		
		FacesContext context = FacesContext.getCurrentInstance();
		String viewId = context.getViewRoot().getViewId();
		ViewHandler handler = context.getApplication().getViewHandler();
		UIViewRoot root = handler.createView(context, viewId);
		root.setViewId(viewId);
		context.setViewRoot(root);
	}
	
	/*protected static CtVerificarExisteContratoNACARRs verificarExisteContratoNACAR(CtVerificarExisteContratoNACARRq obj) {
		CtVerificarExisteContratoNACARRs veri = null;
		try {
			SCE_PE20_Service serv = new SCE_PE20_ServiceLocator();
			SCE_PE20_PortType port = serv.getSCE_PE20_Port();
			veri = port.verificarExisteContratoNACAR(obj);
		} catch (Exception ex) {
			//LOG.error(ex.getMessage(), ex);
			logger.error("Ocurrio un error de conexion para el servicio VerificarExisteContratoNACAR : SCE_PE20_Service");
			logger.error(ex);
		}

		return veri;
	}*/
	
	protected void redirectAction (String pagina, String[]... parametros) {
		StringBuilder sb = new StringBuilder(pagina).append(".jsf");
		if (parametros != null) {
			boolean esPrimero =  true;
			for (String[] param : parametros) {
				if (esPrimero) {
					sb.append('?');
				} else {
					sb.append('&');
				}
				sb.append(param[0]).append('=').append(param[1]);
				esPrimero = false;
			}
		}
		
		try {
			getExternalContext().redirect(sb.toString());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Agrega un mensaje de error a un componente gráfico de la pantalla.
	 * 
	 * @param idComponente
	 *            Id del componente al cual se le va a asociar el mensaje de
	 *            error.
	 * @param mensaje
	 *            Mensaje de error a mostrar para el componente.
	 */
	public void addComponentMessage(String idComponente, String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(idComponente, new FacesMessage(mensaje));
	}
	
	/**
	 * Agrega un mensaje de error al contexto de la pantalla.
	 * 
	 * @param e
	 *            - Excepción cuyo mensaje se va a agregar.
	 */
	public void addErrorMessage(Exception e) {
		if (e != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getCause() != null ? e
							.getCause().getMessage() : e.getMessage(), ""));
		}
	}
	
	/**
	 * Agrega un mensaje de error al contexto de la pantalla.
	 * 
	 * @param e
	 *            -Mensaje a agregar.
	 */
	public void addErrorMessage(String mensaje) {
		if (mensaje != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, mensaje, ""));
		}
	}
	
	public String getAttribute(ActionEvent event, String name) {
        return (String) event.getComponent().getAttributes().get(name);
    }
}