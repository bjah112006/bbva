package com.ibm.bbva.controller.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.session.EmpleadoBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "datosCabecera")
@SessionScoped
public class DatosCabeceraMB extends AbstractMBean {
	
	@EJB
	private EmpleadoBeanLocal empleadobean;	
	private Empleado empleado;
    
	private static final Logger LOG = LoggerFactory.getLogger(DatosCabeceraMB.class);
	
    public DatosCabeceraMB() {
	}
    
    @PostConstruct
    public void init() {
    	String user = "";
	    LOG.info("Usuario : {}",user);
	    ServiciosSeguridadBbva ssBbva = new ServiciosSeguridadBbva((HttpServletRequest) getExternalContext().getRequest());
		if(ssBbva != null) {
			ssBbva.obtener_ID();
			user  =  ssBbva.getUsuario().toUpperCase(); 
			LOG.info("Usuario : {}",user);
		}else{
			LOG.warn("No se encontro el Usurio en el request : {}",user);
		}
    	this.empleado = empleadobean.buscarPorCodigo(user);
    	
    	FacesContext ctx = FacesContext.getCurrentInstance();
		MenuMB menu = (MenuMB) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "menu");
    	
		if (empleado == null) {
			LOG.warn("El empleado (usuario: {}) esta registrado en LDAP y no esta en el sistema", user);
		} else if (Constantes.FLAG_INACTIVO.equals(empleado.getFlagActivo())) {
			LOG.info("El empleado {} esta inactivo", empleado.getCodigo());
			/*HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
			try {
				response.sendRedirect("ibm_security_logout?logoutExitPage=/index.faces");
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			try{
				menu.timeOutWAS();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		addObjectSession(Constantes.USUARIO_SESION, empleado);
    	
    	if(empleado != null && empleado.getPerfil() != null && empleado.getPerfil().getFlagAdministracion().equals("1"))
    		addObjectSession(Constantes.USUARIO_AD_SESION, empleado);
    	
    	//SUBIENDO A SESSION VALORES DE SCANEO DE DOCUMENTOS
	    addObjectSession(Constantes.COMANDO_SESION, Constantes.RUTA_GMD);
	    /*TODO parametros escaner
	    TipoDocumentoEscanerDelegate tipoDocumentoEscanerDelegate = new TipoDocumentoEscanerDelegate();
	    String parametrosEscaner = tipoDocumentoEscanerDelegate.obtenerParametros();
	    addObjectSession(Constantes.PARAMETROS_SESION, parametrosEscaner);*/
	    if(empleado!=null && empleado.getOficina()!=null)
	    	addObjectSession(Constantes.OFICINA_SESION, empleado.getOficina());	  
	    
	    //muestra el menu correspondiente
	    try{
			menu.restringuirAcceso();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	
	public String cortarNombreCompleto(String nc){
		if(nc!=null && !nc.equals("") && nc.length()>42)
			return nc.substring(0, 42);
		else
			return nc;
	}

}
