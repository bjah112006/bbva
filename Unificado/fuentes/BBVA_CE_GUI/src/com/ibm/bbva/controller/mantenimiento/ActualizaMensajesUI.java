package com.ibm.bbva.controller.mantenimiento;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ejb.EJB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.session.MensajesBeanLocal;

@ManagedBean (name="actualizaMensajes")
@SessionScoped
public class ActualizaMensajesUI extends AbstractMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(ActualizaMensajesUI.class);
	private Mensajes mensajes;
	@EJB
	private MensajesBeanLocal mensajesBean;
	private String strContenido;
	private String strDescripcion;
	private Long idMensajes;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void inicio(){
		mensajes=null;
		this.strContenido="";
		this.strDescripcion="";
	}
	
	public String grabar(){
		Long idMensajes=0L;
		FacesContext ctx = FacesContext.getCurrentInstance();
		LOG.info("en el metodo grabar guia documentaria");
		String ruta="";
		try{
			if (!esValido()){
				if (mensajes!=null){
					idMensajes=mensajes.getId();
				}else{
					idMensajes=0L;
				}
				if (idMensajes==0){ 
					crear(); // CREAR
				}else{
					actualizar(idMensajes);//ACTUALIZAR
				}
				ctx = FacesContext.getCurrentInstance();
				BuscarMensajesPersonalizadosUI buscarMensajes = (BuscarMensajesPersonalizadosUI) 
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarMensajes");
				buscarMensajes.actualizarLista();
				removeObjectSession(Constantes.MENSAJES_SESION);
				ruta= "/mantenimiento/formManMensajesPersonalizados?faces-redirect=true";
			}
		}catch(Exception ex){
			ex.printStackTrace();
			ruta= null;
		}
		return ruta;
	}
	
	public String actualizar(Long idMensajes){
		String ruta="";
		Mensajes mensajesActualizar = new Mensajes();
		try{
			mensajesActualizar.setId(idMensajes);
			mensajes.setDescripcion(strDescripcion);
			mensajes.setContenido(strContenido==null ? null : strContenido.getBytes());
			mensajesBean.update(mensajes);
			removeObjectSession(Constantes.MENSAJES_SESION);
			ruta= "/mantenimiento/formManMensajesPersonalizados?faces-redirect=true";
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ruta;
	}
	
	public String cancelar(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarMensajesPersonalizadosUI buscarMensajes = (BuscarMensajesPersonalizadosUI)
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarMensajes");
		buscarMensajes.inicio();
		removeObjectSession(Constantes.MENSAJES_SESION);
		return "/mantenimiento/formManMensajesPersonalizados?faces-redirect=true";
	}
	
	public void obtenerDatos(){
		FacesContext ctx =  FacesContext.getCurrentInstance();
		mensajes=(Mensajes)getObjectSession(Constantes.MENSAJES_SESION);
		if (mensajes!=null){ // inicializa cuando ya existe.
			LOG.info("Entra al init de actualizar datos existentes");
			LOG.info("Id Cabecera" + mensajes.getId());
			idMensajes=mensajes.getId();
			strDescripcion=mensajes.getDescripcion();
			LOG.info("strDescripcion " + strDescripcion);
			byte[] byeContenido=mensajes.getContenido();
			strContenido= byeContenido==null ? null : new String (byeContenido);
		}	
		removeObjectSession(Constantes.MENSAJES_SESION);
	}
	
	
	private boolean esValido(){
		boolean existeError = false;
		String idComponente = "formRegistroMensajes";
		if (strDescripcion == null || "".equals(strDescripcion)) {
			addMessageError(idComponente + ":txtDescripcion",
					"com.ibm.bbva.common.Mensajes.msg.Descripcion");
			existeError = true;
		}
		if (strContenido == null || "".equals(strContenido)|| "<p>&nbsp;</p>".equals(strContenido)) {
			addMessageError(idComponente + ":idContenido",
					"com.ibm.bbva.common.Mensajes.msg.Contenido");
			existeError = true;
		}
		return existeError;
	}
	
	public void crear(){
		Mensajes datosMensajesCrear = new Mensajes();
		try{
			datosMensajesCrear.setId(0);
			datosMensajesCrear.setDescripcion(strDescripcion);
			datosMensajesCrear.setContenido(strContenido==null ? null : strContenido.getBytes());
			mensajesBean.create(datosMensajesCrear);
			removeObjectSession(Constantes.MENSAJES_SESION);
		} catch(Exception ex){
			ex.printStackTrace();
		}	
	}


	public String getStrContenido() {
		return strContenido;
	}


	public void setStrContenido(String strContenido) {
		this.strContenido = strContenido;
	}


	public String getStrDescripcion() {
		return strDescripcion;
	}


	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}


	public Long getIdMensajes() {
		return idMensajes;
	}


	public void setIdMensajes(Long idMensajes) {
		this.idMensajes = idMensajes;
	}
	
	
}
