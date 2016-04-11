package com.ibm.bbva.ctacte.servicio.proceso.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.util.CommonUtils;
import com.ibm.bbva.ctacte.bean.DatosCorreo;
import com.ibm.bbva.ctacte.bean.DatosCorreoXPerfil;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.ViewNumeroExpedientesEmpleado;
import com.ibm.bbva.ctacte.bean.ViewPesoDocumentoExpediente;
import com.ibm.bbva.ctacte.bean.ViewPesoParticipeExpediente;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.DatosCorreoDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
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

	public EmpleadoCE copiarEmpleadoCE(ViewNumeroExpedientesEmpleado obj) {
		EmpleadoCE empleadoCE = new EmpleadoCE();
		empleadoCE.setCodEmpleado(obj.getCodEmpleado());
		empleadoCE.setDesPerfil(obj.getDesPerfil());
		empleadoCE.setIdEmpleado(obj.getIdEmpleado());
		empleadoCE.setIdPerfil(obj.getIdPerfil());
		empleadoCE.setIdProducto(obj.getIdProducto());
		empleadoCE.setIdTarea(obj.getIdTarea());
		empleadoCE.setIdTerritorio(obj.getIdTerritorio());
		empleadoCE.setNomEmpleado(obj.getNomEmpleado());
		empleadoCE.setNumExpedientesEmpleado(obj.getNumExpedientesEmpleado());
		empleadoCE = obtenerDatosAdicionales(empleadoCE);
		return empleadoCE;
	}
	
	public EmpleadoCE copiarEmpleadoCE(ViewPesoDocumentoExpediente obj) {
		EmpleadoCE empleadoCE = new EmpleadoCE();
		empleadoCE.setCodEmpleado(obj.getCodEmpleado());
		empleadoCE.setDesPerfil(obj.getDesPerfil());
		empleadoCE.setIdEmpleado(obj.getIdEmpleado());
		empleadoCE.setIdPerfil(obj.getIdPerfil());
		empleadoCE.setIdProducto(obj.getIdProducto());
		empleadoCE.setIdTarea(obj.getIdTarea());
		empleadoCE.setIdTerritorio(obj.getIdTerritorio());
		empleadoCE.setNomEmpleado(obj.getNomEmpleado());
		empleadoCE.setNumExpedientesEmpleado(obj.getSumPesoDocumentoExpediente());
		empleadoCE = obtenerDatosAdicionales(empleadoCE);
		return empleadoCE;
	}
	
	public EmpleadoCE copiarEmpleadoCE(ViewPesoParticipeExpediente obj) {
		EmpleadoCE empleadoCE = new EmpleadoCE();
		empleadoCE.setCodEmpleado(obj.getCodEmpleado());
		empleadoCE.setDesPerfil(obj.getDesPerfil());
		empleadoCE.setIdEmpleado(obj.getIdEmpleado());
		empleadoCE.setIdPerfil(obj.getIdPerfil());
		empleadoCE.setIdProducto(obj.getIdProducto());
		empleadoCE.setIdTarea(obj.getIdTarea());
		empleadoCE.setIdTerritorio(obj.getIdTerritorio());
		empleadoCE.setNomEmpleado(obj.getNomEmpleado());
		empleadoCE.setNumExpedientesEmpleado(obj.getSumPesoParticipeExpediente());
		empleadoCE = obtenerDatosAdicionales(empleadoCE);
		return empleadoCE;
	}
	
	public EmpleadoCE copiarEmpleadoCE(Empleado obj, Integer intIdPerfil, Integer intIdProducto, Integer intIdTarea, Integer intIdTerritorio) {
		EmpleadoCE empleadoCE = new EmpleadoCE();
		empleadoCE.setCodEmpleado(obj.getCodigo());
		empleadoCE.setDesPerfil(obj.getPerfil().getDescripcion());
		empleadoCE.setIdEmpleado(obj.getId());
		empleadoCE.setIdPerfil(intIdPerfil);
		empleadoCE.setIdProducto(intIdProducto);
		empleadoCE.setIdTarea(intIdTarea);
		empleadoCE.setIdTerritorio(intIdTerritorio);
		empleadoCE.setNomEmpleado(obj.getNombres()+" "+obj.getApepat()+" "+obj.getApemat());
		empleadoCE.setNumExpedientesEmpleado(0);
		obtenerDatosAdicionales(empleadoCE, obj);
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
	
	public Integer grabarEmpleadoExpedienteTareaProceso(Integer idExpediente, String codEmpleado, Integer idTarea){
		Empleado empleado = empleadoDAO.findByCodigo(codEmpleado);
		Integer resultado = 0;
		if (empleado != null) {
			ExpedienteTareaProceso expedienteTareaProceso = new ExpedienteTareaProceso();
			expedienteTareaProceso.setIdEmpleado(empleado.getId());
			expedienteTareaProceso.setIdExpediente(idExpediente);
			expedienteTareaProceso.setIdTarea(idTarea);
			expedienteTareaProcesoDAO.save(expedienteTareaProceso);
			resultado = expedienteTareaProceso.getId();
		}
		LOG.info("ID expediente tarea proceso - "+resultado);
		return resultado;
	}
	
	public boolean EliminarEmpleadoExpedienteTareaProceso(Integer idExpediente, String codEmpleado, Integer idTarea){
		Empleado empleado = empleadoDAO.findByCodigo(codEmpleado);
		boolean resultado = false;
		if (empleado != null) {
			ExpedienteTareaProceso expedienteTareaProceso = expedienteTareaProcesoDAO.findExpedienteTareaProceso(idExpediente, empleado.getId(), idTarea);
			if (expedienteTareaProceso != null) {
				expedienteTareaProcesoDAO.delete(expedienteTareaProceso);
			} else {
				LOG.warn("ExpedienteTareaProceso no existe, se buscará para otros empleados.");
				List<ExpedienteTareaProceso> lstExpTarProc = expedienteTareaProcesoDAO.findByIdExpIdTarea(idExpediente, idTarea);
				if (lstExpTarProc != null && lstExpTarProc.size() > 0) {
					LOG.warn("Se encontraron "+lstExpTarProc.size()+" registros de carga de trabajo para el mismo expediente y tarea.");
					for (ExpedienteTareaProceso obj : lstExpTarProc) {
						LOG.warn("Se eliminó el registro de carga de trabajo para el empleado con ID="+obj.getIdEmpleado());
						expedienteTareaProcesoDAO.delete(obj);
					}
				} else {
					LOG.warn("No se encontraron otros registros de carga de trabajo para el mismo expediente y tarea.");
				}
			}
		}
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
