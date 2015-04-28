package com.ibm.bbva.ctacte.servicio.proceso.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.bean.ViewNumeroExpedientesEmpleado;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.servicio.proceso.bean.EmpleadoCE;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

public class ProcesoUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProcesoUtil.class);
	
	private EmpleadoDAO empleadoDAO;
	private ExpedienteTareaProcesoDAO expedienteTareaProcesoDAO;
	
	public ProcesoUtil() {
		empleadoDAO = EJBLocator.getEmpleadoDAO();
		expedienteTareaProcesoDAO = EJBLocator.getExpedienteTareaProcesoDAO();
	}

	public EmpleadoCE copiarEmpleadoCE (ViewNumeroExpedientesEmpleado ViewNumeroExpedientesEmpleado){
		EmpleadoCE empleadoCE = new EmpleadoCE();
		empleadoCE.setCodEmpleado(ViewNumeroExpedientesEmpleado.getCodEmpleado());
		empleadoCE.setDesPerfil(ViewNumeroExpedientesEmpleado.getDesPerfil());
		empleadoCE.setIdEmpleado(ViewNumeroExpedientesEmpleado.getIdEmpleado());
		empleadoCE.setIdPerfil(ViewNumeroExpedientesEmpleado.getIdPerfil());
		empleadoCE.setIdProducto(ViewNumeroExpedientesEmpleado.getIdProducto());
		empleadoCE.setIdTarea(ViewNumeroExpedientesEmpleado.getIdTarea());
		empleadoCE.setIdTerritorio(ViewNumeroExpedientesEmpleado.getIdTerritorio());
		empleadoCE.setNomEmpleado(ViewNumeroExpedientesEmpleado.getNomEmpleado());
		empleadoCE.setNumExpedientesEmpleado(ViewNumeroExpedientesEmpleado.getNumExpedientesEmpleado());
		empleadoCE = obtenerDatosAdicionales(empleadoCE);
		return empleadoCE;
	}
	
	public EmpleadoCE obtenerDatosAdicionales (EmpleadoCE empleadoCE){
		Empleado empleado = new Empleado();
		empleado = empleadoDAO.load(empleadoCE.getIdEmpleado());
		obtenerDatosAdicionales(empleadoCE, empleado);
		return empleadoCE;
	}

	public void obtenerDatosAdicionales(EmpleadoCE empleadoCE,
			Empleado empleado) {
		if (empleado.getFlagAbogado()!=null){
			if (empleado.getFlagAbogado().compareTo("1")==0){
				empleadoCE.setCodAbogado(empleado.getCodigo());
				empleadoCE.setCodEstudioAbogado(empleado.getEstudio().getId().toString());
				empleadoCE.setIdEstudioAbogado(empleado.getEstudio().getId());
				empleadoCE.setNomAbogado(empleado.getNombres()+ " " + empleado.getApepat() + " " + empleado.getApemat());
				empleadoCE.setNomEstudioAbogado(empleado.getEstudio().getDescripcion());
			}
		}
	}
	
	public Integer grabarEmpleadoExpedienteTareaProceso(Integer idExpediente, Integer idEmpleado, Integer idTarea){
		Integer resultado = 0;
		ExpedienteTareaProceso expedienteTareaProceso = new ExpedienteTareaProceso();
		expedienteTareaProceso.setIdEmpleado(idEmpleado);
		expedienteTareaProceso.setIdExpediente(idExpediente);
		expedienteTareaProceso.setIdTarea(idTarea);
		expedienteTareaProcesoDAO.save(expedienteTareaProceso);
		resultado = expedienteTareaProceso.getId();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("ID expediente tarea proceso - "+resultado);
		return resultado;
	}
	
	public boolean EliminarEmpleadoExpedienteTareaProceso(Integer idExpediente, Integer idEmpleado, Integer idTarea){
		boolean resultado = false;
		ExpedienteTareaProceso expedienteTareaProceso = expedienteTareaProcesoDAO.finExpedienteTareaProceso(idExpediente, idEmpleado, idTarea);
		expedienteTareaProcesoDAO.delete(expedienteTareaProceso);
		return resultado;
	}
	
	public void enviarCorreo(String asunto,InternetAddress[] listAddresses,String mensaje) {
		LOG.info("enviarCorreo()");
		try {
			
			ParametrosSistema parametros = ParametrosSistema.getInstance();
			Properties propertiesWAS = parametros.getProperties(ParametrosSistema.CONF);
			String hostServidorCorreo;
			String puertoServidorCorreo;
			String remitente;
			hostServidorCorreo = propertiesWAS.getProperty(ConstantesParametros.HOST_SERVIDOR_CORREO);
			puertoServidorCorreo = propertiesWAS.getProperty(ConstantesParametros.PUERTO_SERVIDOR_CORREO);
			remitente = propertiesWAS.getProperty(ConstantesParametros.EMAIL_REMITENTE);
			
			
			// Get the session object
			LOG.info("Host Servidor Correo: "+hostServidorCorreo);
			LOG.info("Puerto Servidor Correo: "+puertoServidorCorreo);
			LOG.info("remitente: "+remitente);
			//Properties properties = System.getProperties();
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.host", hostServidorCorreo);
			properties.setProperty("mail.smtp.port", puertoServidorCorreo);
			Session session = Session.getDefaultInstance(properties);
	
			// compose the message
			
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(remitente));
			
			message.addRecipients(Message.RecipientType.TO, listAddresses);
			
			message.setSubject(asunto);
			StringBuilder sb = new StringBuilder();
			sb.append("Estimado usuario,\n");
			//sb.append("Le confirmamos que ya puede trabajar ").append(documento.getDescripcion()).append(" para el expediente ").append(numeroExpediente).append(".\n");
			sb.append(mensaje).append("\n");
			sb.append("\n");
			sb.append("Saludos,\n");
			sb.append("\n");
			message.setText(sb.toString());
			LOG.info("Texto del mensaje: "+sb.toString());

			// Send message
			Transport.send(message);
			LOG.info("Se envió el correo satisfactoriamente.");

		} catch (Exception ex) {
			LOG.error("Error al enviar el correo.", ex);
		}
	}
}
