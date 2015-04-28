package com.ibm.bbva.jobs;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.LDAPUsuario;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.LDAPUsuarioDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

/**
 * 
 * @author devniel
 * Sincronización de usuarios , desde
 * el LDAP a la tabla de empleados
 * 
 */

public class LDAPCarga implements Job {
	
	private static final Logger LOG = LoggerFactory.getLogger("LDAPCarga");
	
	ParametrosSistema parametros = ParametrosSistema.getInstance();
	Properties props = parametros.getProperties(ParametrosSistema.CARGA);
	
	private OficinaDAO oficinaDAO;
	
	private EmpleadoDAO empleadoDAO;
	
	private LDAPUsuarioDAO ldapUsuarioDAO;

	private PerfilDAO perfilDAO;
	
	private PerfilXNivelDAO perfilXNivelDAO;
	
	public LDAPCarga(){
		oficinaDAO = EJBLocator.getOficinaDAO();
		empleadoDAO = EJBLocator.getEmpleadoDAO();
		ldapUsuarioDAO = EJBLocator.getLDAPUsuarioDAO();
		perfilDAO = EJBLocator.getPerfilDAO();
		perfilXNivelDAO = EJBLocator.getPerfilXNivelDAO();
	}
	
	
	
	/**
	 * Frecuencia de dias en los que
	 * se debe ejecutar el job.
	 * 
	 * Por defecto : "1"
	 */
	
	public Integer frecuenciaDias = Integer.valueOf(props.getProperty(ConstantesParametros.FRECUENCIA_DIAS));
	
	/**
	 * Hora de ejecución (incluyendo minutos y segundos)
	 * en los que se debe ejecutar el job, el formato a 
	 * seguir en cadena de texto es "23:00:00".
	 * 
	 * Por defecto : "23:00:00"
	 */
	
	public String horaEjecucion = props.getProperty(ConstantesParametros.HORA_EJECUCION);
	
	/**
	 * Flag de ejecución del proceso de LDAP_CARGA
	 * 0 -> No activo
	 * 1 -> Activo
	 * 
	 * Por defecto : "0"
	 */
	
	public String ldapCargaActivo = props.getProperty(ConstantesParametros.LDAP_CARGA_FLAG_ACTIVO);
	
	/**
	 * Cargar configuración de job y agendar
	 * su ejecución con los paramétros establecidos
	 * en frecuenciaDias y horaEjecucion
	 */
	
	public void load(Scheduler sched) {
		
		if(ldapCargaActivo == null){
			LOG.info("No se ha establecido en BD el parámetro empleados.ldapCargaActivo para activar el job ...");
			LOG.info("Cancelada la configuración del job.");
			return;
		}
		
		if(ldapCargaActivo.equals("0")){
			LOG.info("Los parámetros establecidos en base de datos indican que NO se configure la ejecución del job ...");
			LOG.info("Cancelada la configuración del job.");
			return;
		}
		
		LOG.info("Configurando ejecución de job ...");
		
		// Configuración de carga del LDAP
			
		try {
			    
		    LOG.info(" Agendando ejecución ...");
		    
		    LOG.info(" Hora ejecución : " + horaEjecucion);
		    
		    LOG.info(" Frecuencia días : " + frecuenciaDias);
		    
		    JobDetail job = JobBuilder.newJob(LDAPCarga.class).withIdentity("JOB_CARGA_LDAP", "GRUPO_JOBS_LDAP").build();
		    
		    Date startDate = null;
		    
		    Date datetime = DateBuilder.todayAt(
		    		Integer.valueOf(horaEjecucion.split(":")[0]),
		    		Integer.valueOf(horaEjecucion.split(":")[1]), 
		    		Integer.valueOf(horaEjecucion.split(":")[2])
		    		);
		    		    	    
		    Date now = new Date();
		    
		    if(datetime.after(now)){
		    	startDate = datetime;		    	
		    }else if(datetime.before(now)){
		    	startDate = DateBuilder.tomorrowAt(
		    		Integer.valueOf(horaEjecucion.split(":")[0]),
		    		Integer.valueOf(horaEjecucion.split(":")[1]), 
		    		Integer.valueOf(horaEjecucion.split(":")[2])
	    		);
		    }
		    
		    
		    LOG.info(" La primera ejecución será el : " + startDate.toString());

		    SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder
		    		.newTrigger()
		    		.withIdentity("TRIGGER_JOB_CARGA_LDAP", "GRUPO_JOBS_LDAP")
		    		.forJob(job)
		    		.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24 * this.frecuenciaDias).repeatForever())
		    		.startAt(startDate)
		    		.build();
		    
		    sched.scheduleJob(job, trigger);
		    sched.start();
		    
		    LOG.info("Job agendado satisfactoriamente ...");
		    		    
		}catch(Exception e){
			LOG.error("Error cuando se agendaba la ejecución del job : ", e);
		}
		
	}
	
	/**
	 * Lógica a ejecutar
	 * por el job
	 */
	
	public void logic(){
		
		LOG.info("Iniciando ejecución de job ...");
		
		/* 
		 * 1.5.	Si el usuario no existe en LDAP y esta en la tabla de empleado 
		 * de la base de datos se cambia el flag de activo a "inactivo".
		 * 
		 */		
		
		LOG.info("Obteniendo empleados no presentes en LDAP ...");
		List<Empleado> empleadosADesactivar = empleadoDAO.getAllNotInLDAP();
		
		LOG.info("Obteniendo empleados presentes en LDAP ...");
		List<Empleado> empleadosAActualizar = empleadoDAO.getAllInLDAP();
		
		LOG.info("Obteniendo usuarios LDAP a crearse como empleado ...");
		List<LDAPUsuario> usuarios_a_crear_como_empleado = ldapUsuarioDAO.getAllNotInEmpleado();

		
		/* Cambiar el flag de activo a inactivo a
		 * empleados no existentes en LDAP
		 */
		
		LOG.info("Iniciando cambio de flag de empleados no presentes en LDAP ...");

		for(Empleado empleado : empleadosADesactivar){
			
			try{
				LOG.info("Empleado a desactivar : " + empleado.getCodigo());
				empleado.setFlagActivo("0");	
				empleadoDAO.update(empleado);
				LOG.info("Empleado " + empleado.getCodigo() + " desactivado");
			}catch(Exception e){
				LOG.error("Error al desactivar a empleado : " + empleado.getCodigo(), e);
			}
		}
		
		/* Actualizar datos de empleados
		 * existentes en LDAP 
		 */
		
		LOG.info("Iniciando actualización de empleados presentes en el LDAP ...");
		
		for(Empleado empleado : empleadosAActualizar){
			
			try{
			
				LOG.info("------------------------------------------------------");
				
				// Revisar fecha de vigencia
				
				LOG.info("Revisando FECHA_VIGENCIA de empleado : " + empleado.getCodigo());
				
				LOG.info("FECHA_VIGENCIA de empleado : " + empleado.getFechaVigencia());
				
				if(empleado.getFechaVigencia() != null){
					if(empleado.getFechaVigencia().after(new Date())){
						LOG.info("Empleado " + empleado.getCodigo() + " con FECHA_VIGENCIA no vencida, NO ACTUALIZARLO");
						continue;
					}
					LOG.info("Empleado " + empleado.getCodigo() + " con FECHA_VIGENCIA vencida, SÍ ACTUALIZARLO");
				}
							
				// Usuario LDAP
				LDAPUsuario usuario =  ldapUsuarioDAO.findByCodigoUsuario(empleado.getCodigo());
				
				LOG.info("Actualizando nombres de empleado con código " + empleado.getCodigo());
				
				// Nombres
				empleado.setNombres(usuario.getNombre());
				
				LOG.info("Actualizando apellido materno de empleado con código " + empleado.getCodigo());
	
				// Apellidos
				empleado.setApemat(usuario.getApemat());
				
				LOG.info("Actualizando apellido paterno de empleado con código " + empleado.getCodigo());
				
				empleado.setApepat(usuario.getApepat());
				
				LOG.info("Actualizando oficina de empleado con código " + empleado.getCodigo());
				
				// Oficina
				empleado.setOficina(oficinaDAO.findByCodigo(usuario.getCodofi()));
				
				LOG.info("Actualizando fecha de vigencia de empleado con código " + empleado.getCodigo());
	
				// Fecha de vigencia
				empleado.setFechaVigencia(null);
				
				LOG.info("Actualizando correo electrónico de empleado con código " + empleado.getCodigo());
				
				// Correo electrónico
				empleado.setCorreo(usuario.getCorelec());
				
				LOG.info("Actualizando codigo de cargo de empleado con código " + empleado.getCodigo());
				
				// Puesto (cargo)
				empleado.setCodcargo(usuario.getCodcargo());
				
				LOG.info("Actualizando nombre de cargo de empleado con código " + empleado.getCodigo());
				
				// Descripción del puesto (cargo)
				empleado.setNomcargo(usuario.getNomcargo());
				
				// Se obtiene el perfil de acuerdo a las reglas configuradas
				Perfil perfil = perfilXNivelDAO.obtenerPerfilAsignado(empleado.getCodigo(), empleado.getCodcargo(), empleado.getOficina().getCodigo());
				
				LOG.info("Actualizando perfil: '" + perfil.getDescripcion() + "' de empleado con código " + empleado.getCodigo());
				
				empleado.setPerfil(perfil);
				
				LOG.info("Actualizando nombre de empleado con código " + empleado.getCodigo());
				
				// Nombre completo
				if(empleado.getPerfil().getFlagMostrarIniciales().equals("0")){
					
					LOG.info("Perfil de empleado con FLAG_MOSTRAR_INICIALES = 0");
	
					String nombreCompleto = empleado.getNombres() + " " + empleado.getApepat() + " " + empleado.getApemat();
					empleado.setNombresCompletos(nombreCompleto);
					
				}else if(empleado.getPerfil().getFlagMostrarIniciales().equals("1")) {
					
					LOG.info("Perfil de empleado con FLAG_MOSTRAR_INICIALES = 1");
					
					String nombreCompleto = empleado.getNombres().substring(0,1) + empleado.getApepat().substring(0,1) + empleado.getApemat().substring(0,1);
					empleado.setNombresCompletos(nombreCompleto);
					
				}
				
				empleadoDAO.update(empleado);
				
				LOG.info("Empleado " + empleado.getCodigo() + " actualizado correctamente");
				
			}catch(Exception e){
				LOG.error("Error al actualizar empleado " + empleado.getCodigo()  + ".", e);
			}

		}
		
		/**
		 * Crear nuevos empleados
		 * a partir de usuarios LDAP
		 */
		
		LOG.info("Iniciando creación de empleados desde el LDAP ...");
		
		for(LDAPUsuario usuario : usuarios_a_crear_como_empleado){
			
			try{
			
				LOG.info("------------------------------------------------------");
				
				LOG.info("Creando nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu());
				
				Empleado empleado = new Empleado();
				
				LOG.info("Estableciendo codigo '" + usuario.getCodusu() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
				
				empleado.setCodigo(usuario.getCodusu());
				
				LOG.info("Estableciendo nombre '" + usuario.getNombre() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
		
				// Nombres
				empleado.setNombres(usuario.getNombre());
				
				LOG.info("Estableciendo apellido materno '" + usuario.getApemat() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
		
				// Apellidos
				empleado.setApemat(usuario.getApemat());
				
				LOG.info("Estableciendo apellido paterno '" + usuario.getApepat() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");

				empleado.setApepat(usuario.getApepat());
				
				LOG.info("Estableciendo oficina '" + usuario.getCodofi() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
				
				// Oficina
				empleado.setOficina(oficinaDAO.findByCodigo(usuario.getCodofi()));
				
				LOG.info("Estableciendo correo '" + usuario.getCorelec() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
				
				// Correo electrónico
				empleado.setCorreo(usuario.getCorelec());
				
				LOG.info("Estableciendo código de cargo '" + usuario.getCodcargo() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
				
				// Puesto (cargo)
				empleado.setCodcargo(usuario.getCodcargo());
				
				LOG.info("Estableciendo nombre de cargo '" + usuario.getNomcargo() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
				
				// Descripción del puesto (cargo)
				empleado.setNomcargo(usuario.getNomcargo());
				
				// Se obtiene el perfil de acuerdo a las reglas configuradas
				Perfil perfil = perfilXNivelDAO.obtenerPerfilAsignado(empleado.getCodigo(), empleado.getCodcargo(), empleado.getOficina().getCodigo());
				
				LOG.info("Estableciendo perfil '" + perfil.getDescripcion() + "' a nuevo empleado a partir de las reglas de asignación de perfil configuradas");
				
				empleado.setPerfil(perfil);
				
				LOG.info("Estableciendo nombre completo de empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " ...");
							
				// Nombre completo
				if(empleado.getPerfil().getFlagMostrarIniciales().equals("0")){
					
					LOG.info("El nombre completo de empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " será una concatenación de nombres y apellidos ...");
					
					String nombreCompleto = empleado.getNombres() + " " + empleado.getApepat() + " " + empleado.getApemat();
					empleado.setNombresCompletos(nombreCompleto);
					
				}else if(empleado.getPerfil().getFlagMostrarIniciales().equals("1")) {
					
					LOG.info("El nombre completo de empleado a partir del usuario del LDAP : " + usuario.getCodusu() + " será una concatenación de las iniciales de nombres y apellidos ...");
					
					String nombreCompleto = empleado.getNombres().substring(0,1) + empleado.getApepat().substring(0,1) + empleado.getApemat().substring(0,1);
					empleado.setNombresCompletos(nombreCompleto);
					
				}
				
				empleadoDAO.save(empleado);
				
				LOG.info("Empleado creado correctamente");
				
			}catch(Exception e){
				LOG.error("Error al crear empleado a partir del usuario del LDAP " + usuario.getCodusu(), e);
			}
		}
		
	}
	
	/**
	 * Ejecución de lógica del 
	 * Job
	 */
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logic();
		}catch(Exception e){
			LOG.error("Error en el job de carga del LDAP : ", e);
		}
	}
	
	/**
	 * Getters and setters
	 * 
	 */

	public Integer getFrecuenciaDias() {
		return frecuenciaDias;
	}

	public void setFrecuenciaDias(Integer frecuenciaDias) {
		this.frecuenciaDias = frecuenciaDias;
	}

	public String getHoraEjecucion() {
		return horaEjecucion;
	}

	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}
	
	
}
