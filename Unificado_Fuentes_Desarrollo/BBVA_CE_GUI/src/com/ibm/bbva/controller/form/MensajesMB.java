package com.ibm.bbva.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.messages.Mensajes;

@SuppressWarnings("serial")
@ManagedBean(name = "mensajes")
@RequestScoped
public class MensajesMB extends AbstractMBean {

	private Expediente expediente;
	private String mensaje;
	private String flCopiaArchivos;
	private String mensajeNoCarterizacion;

	private static final Logger LOG = LoggerFactory.getLogger(MensajesMB.class);

	public MensajesMB() {
		mensaje = null;
		mensajeNoCarterizacion=null;
		Object tipo = getObjectSession(Constantes.TIPO_MENSAJE);		
		Object mensajeRol = getObjectSession(Constantes.DESCRIPCION_MENSAJE);
		Object msg;
		if (tipo != null) {
			expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
			switch ((Integer)tipo) {
		        case Constantes.TIPO_MENSAJE_GESTIONAR_SUBSANAR_OPERACION:
		    	    msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
		    	    mostrarMensajeGestionarSubsanrOperacionBD((String)msg, expediente.getId());
		            break;
		        case Constantes.TIPO_MENSAJE_EVALUAR_DEVOLUCION_RIESGOS:
		    	    msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
		    	    mostrarMensajeEvaluarDevolucionRiesgosBD((String)msg, expediente.getId());
		            break;
		        case Constantes.TIPO_MENSAJE_CONSULTAR_CLIENTE_MODIFICACIONES:
		        	msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
		    	    mostrarMensajeConsultarClienteModificacionesBD((String)msg, expediente.getId());
		            break;			
			    case Constantes.TIPO_MENSAJE_REGISTRAR_EXPEDIENTE_CU25:
			    	msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
			    	mostrarMensajeRegistrarExpedienteCu25BD((String)msg, expediente.getId());
			        break;
			    case Constantes.TIPO_MENSAJE_REGISTRAR_EXPEDIENTE_CU23:
			    	msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
			    	mostrarMensajeRegistrarExpedienteCu23BD((String)msg, expediente.getId());
			        break;
		        case Constantes.TIPO_MENSAJE_REGULARIZAR_ESCANEAR_DOCUMENTOS:
		        	msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
		        	mostrarMensajeRegularizarEscanearDocumentosBD((String)msg, expediente.getId());
			        break;			
			    case Constantes.TIPO_MENSAJE_COORDINAR_CLIENTE_SUBSANAR:
			    	msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_CORREGIDO);
			    	mostrarMensajeCoordinarClienteSubsanarBD((String)msg, expediente.getId());
				    break;	
				case Constantes.TIPO_MENSAJE_CANCELADO:
					mostrarMensajeCancelado(expediente.getId());
					break;
				case Constantes.TIPO_MENSAJE_PENDIENTE:
					String registrarExpediente = (String) getObjectSession(Constantes.REGISTRAR_EXPEDIENTE_SESION);
					mostrarMensajePendiente(registrarExpediente);
					removeObjectSession(Constantes.REGISTRAR_EXPEDIENTE_SESION);
					break;
				case Constantes.TIPO_MENSAJE_PREREGISTRADO:
					msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_PRE_REGISTRADO);
					mostrarMensajePreRegistroBD((String)msg, expediente.getId());
					break;
				case Constantes.TIPO_MENSAJE_REGISTRADO:
					msg = getObjectSession(Constantes.DESCRIPCION_MENSAJE_REGISTRADO);
					mostrarMensajeRegistradoBD((String)msg, expediente.getId());
					break;
				case Constantes.TIPO_MENSAJE_NO_EXISTE_ROLES:					
					mostrarMensajeNoExisteRoles((String) mensajeRol);
					break;
				case Constantes.TIPO_MENSAJE_NO_EXISTE_CARTEIZACION:					
					mostrarMensajeNoExisteCarterizacion((String) mensajeRol);
					break;	
			}
			
			/*String idTarea = (String) getObjectSession("tareaSession");
			Integer tamListGuiaDocumentaria = (Integer) getObjectSession("tamListaGuiaDoc");
			if((tamListGuiaDocumentaria == null || tamListGuiaDocumentaria.intValue() == 0) && !"1".equals(idTarea)){
				flCopiaArchivos = "0";
			}*/
			
			String strListaDocsTransferencias = (String)getObjectSession("strListaDocsTransferencias");
			if(strListaDocsTransferencias == null || "".equals(strListaDocsTransferencias) || "null".equals(strListaDocsTransferencias)){
				flCopiaArchivos = "0";
			}
			
			removeObjectSession(Constantes.TIPO_MENSAJE);
			removeObjectSession(Constantes.DESCRIPCION_MENSAJE);
		}
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensajeNoCarterizacion() {
		return mensajeNoCarterizacion;
	}

	public void setMensajeNoCarterizacion(String mensajeNoCarterizacion) {
		this.mensajeNoCarterizacion = mensajeNoCarterizacion;
	}
	
	private void mostrarMensajeRegistradoBD (String mensaje, long nroExpediene) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediene);
	}
	
	private void mostrarMensajePreRegistroBD (String mensaje, long nroExpediene) {		
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediene);
	}
	
	private void mostrarMensajeCancelado (long nroExpediene) {
		mensaje = Mensajes.getMensaje("com.ibm.bbva.mensajes.formMensajes.cancelado", 
				nroExpediene);
	}
	
	private void mostrarMensajePendiente (String... pendientes) {
		boolean esPrimero = true;
		StringBuilder sb = new StringBuilder ();
		for (String str : pendientes) {
			if (!esPrimero) {
				sb.append(", ");
			}
			sb.append(str);
			esPrimero = false;
		}
		mensaje = Mensajes.getMensaje("com.ibm.bbva.mensajes.formMensajes.pendiente", 
				sb.toString());
	}
	
	private void mostrarMensajeCoordinarClienteSubsanarBD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente);
	}	
	
	private void mostrarMensajeRegularizarEscanearDocumentosBD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);	
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente); 
	}
	
	private void mostrarMensajeRegistrarExpedienteCu23BD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente);
	}
	
	private void mostrarMensajeRegistrarExpedienteCu25BD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente);
	}
	
	
	private void mostrarMensajeConsultarClienteModificacionesBD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente);
	}	
	
	private void mostrarMensajeEvaluarDevolucionRiesgosBD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente);
	}	
	
	private void mostrarMensajeGestionarSubsanrOperacionBD (String mensaje, long nroExpediente) {
		flCopiaArchivos = (String) getObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);		
		addObjectSession(Constantes.ID_EXPEDIENTE_SESION, Long.toString(expediente.getId()));
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		this.mensaje = Mensajes.getMensajeBD(mensaje, nroExpediente);
	}

	private void mostrarMensajeNoExisteRoles (String mensajeRol) {
		mensaje = mensajeRol;		
	}
	private void mostrarMensajeNoExisteCarterizacion(String mensajeRol) {
		mensajeNoCarterizacion = mensajeRol;		
	}
	public String getFlCopiaArchivos() {
		return flCopiaArchivos;
	}

	public void setFlCopiaArchivos(String flCopiaArchivos) {
		this.flCopiaArchivos = flCopiaArchivos;
	}		
}
