package com.ibm.bbva.controller.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bpd.RemoteUtils;

import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.OficinaTemporal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.OficinaTemporalBeanLocal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@ManagedBean(name = "datosCabecera")
@SessionScoped
public class DatosCabeceraMB extends AbstractMBean {
	
	@EJB
	private EmpleadoBeanLocal empleadobean;	
	private Empleado empleado;
    
	@EJB
	private OficinaTemporalBeanLocal oficinaTemporalBeanLocal;
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosCabeceraMB.class);
	
	private String mensajeAdvertencia;
	private String etiquetaTemporal;
	
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
    				
		List<OficinaTemporal> listaOficinaTemporal = this.oficinaTemporalBeanLocal.obtenerActual(this.empleado.getId());
		OficinaTemporal objOficinaTemporal = null;		
		if(listaOficinaTemporal.size() > 0)
		{
			objOficinaTemporal = listaOficinaTemporal.get(0);
			DateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");
			DateFormat timeformat= new SimpleDateFormat("HH:mm");
			try {
				Date fechaActual = dateformat.parse(dateformat.format(new Date()));
				Date horaActual = timeformat.parse(timeformat.format(new Date()));
				if(objOficinaTemporal != null && fechaActual.compareTo(objOficinaTemporal.getFechaInicio()) == 0)
				{
					if(timeformat.parse(objOficinaTemporal.getHoraInicio()).compareTo(horaActual) > 0)
					{
						objOficinaTemporal = null;
					}
				}
				if(objOficinaTemporal != null && fechaActual.compareTo(objOficinaTemporal.getFechaFin()) == 0)
				{
					if(timeformat.parse(objOficinaTemporal.getHoraFin()).compareTo(horaActual) < 0)
					{
						objOficinaTemporal = null;
					}
				}
			} catch (ParseException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}					
		}
		
		if(objOficinaTemporal == null)
		{
			if(this.empleado.getOficinaBackup() != null)
			{				
				RemoteUtils remoteUtils = new RemoteUtils();
				long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
				if(cantexp > 0)
				{
					this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para la oficina temporal establecida");
				}
				else
				{				
					this.empleado.setOficina(this.empleado.getOficinaBackup());
					this.empleado.setOficinaBackup(null);
					this.empleadobean.edit(this.empleado);
				}
			}
		}
		else
		{
			if(this.empleado.getOficinaBackup() == null)
			{
				RemoteUtils remoteUtils = new RemoteUtils();
				long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
				if(cantexp > 0)
				{
					this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes, no se le puede establecer la oficina temporal configurada");
				}
				else
				{
					this.empleado.setOficinaBackup(this.empleado.getOficina());
					this.empleado.setOficina(objOficinaTemporal.getOficina());				
					this.empleadobean.edit(this.empleado);
				}							
			}
			else
			{
				if(this.empleado.getOficina().getId() != objOficinaTemporal.getOficina().getId())
				{
					RemoteUtils remoteUtils = new RemoteUtils();
					long cantexp = remoteUtils.countConsultaListaTareasTC(this.empleado.getCodigo());					
					if(cantexp > 0)
					{
						this.setMensajeAdvertencia("Usted cuenta con expedientes pendientes para la oficina temporal establecida");
					}
					else
					{
						this.empleado.setOficina(objOficinaTemporal.getOficina());
						this.empleadobean.edit(this.empleado);
					}
				}
			}
		}
		
		if(this.empleado.getOficinaBackup() != null) { this.setEtiquetaTemporal("(Temporal)"); }
		
		if (empleado == null) {
			LOG.warn("El empleado (usuario: {}) esta registrado en LDAP y no esta en el sistema", user);
		} else if (Constantes.FLAG_INACTIVO.equals(empleado.getFlagActivo())) {
			LOG.info("El empleado {} esta inactivo", empleado.getCodigo());
			/*HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
			try {
				response.sendRedirect("ibm_security_logout?logoutExitPage=/index.faces");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}*/
			try{
				menu.timeOutWAS();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
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
			LOG.error(e.getMessage(), e);
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

	public String getMensajeAdvertencia() {
		return mensajeAdvertencia;
	}

	public void setMensajeAdvertencia(String mensajeAdvertencia) {
		this.mensajeAdvertencia = mensajeAdvertencia;
	}

	public String getEtiquetaTemporal() {
		return etiquetaTemporal;
	}

	public void setEtiquetaTemporal(String etiquetaTemporal) {
		this.etiquetaTemporal = etiquetaTemporal;
	}
	
}
