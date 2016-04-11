package com.ibm.bbva.jobs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.bbva.ws.ldap.servidor.Usuario;
import pe.com.bbva.ws.ldap.servidor.WSLDAPServiceImplService;
import pe.com.bbva.ws.ldap.servidor.WSLdapException_Exception;
import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.ObjectQuery.crud.util.Array;
import com.ibm.bbva.ctacte.bean.Carterizacion;
import com.ibm.bbva.ctacte.bean.CarterizacionId;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EmpleadoLdap;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.bean.LDAPUsuario;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.Proceso;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.TareasReasig;
import com.ibm.bbva.ctacte.bean.auxiliares.EmpleadoAuxiliar;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.CarterizacionDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoLdapDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.LDAPUsuarioDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.bbva.ctacte.dao.ProcesoDAO;
import com.ibm.bbva.ctacte.dao.ProductoCEDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TareasReasigDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.valueObject.TareaBandejaVO;

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
	
	private EmpleadoLdapDAO empleadoLdapDAO;
	
	private CarterizacionDAO carterizacionDAO;
	
	private ProductoCEDAO productoDAO;
	
	private EstudioAbogadoDAO estudioAbogadoDAO;
	
	private List<EmpleadoAuxiliar> empleadosAuxiliares;
	
	private TareaDAO tareaDAO;
	
	private ExpedienteDAO expedienteDAO;
	
	private MotivoDAO motivoDAO;
	
	private TareasReasigDAO tareaReasigDAO;
	
	private ProcesoDAO procesoDAO;
	
	private ExpedienteTareaProcesoDAO expTareaProcesoDAO;
	
	private static final String SUF_PENDIENTES = "_PD";
	
	public LDAPCarga(){
		oficinaDAO = EJBLocator.getOficinaDAO();
		empleadoDAO = EJBLocator.getEmpleadoDAO();
		ldapUsuarioDAO = EJBLocator.getLDAPUsuarioDAO();
		perfilDAO = EJBLocator.getPerfilDAO();
		perfilXNivelDAO = EJBLocator.getPerfilXNivelDAO();
		empleadoLdapDAO = EJBLocator.getEmpleadoLdapDAO();
		empleadosAuxiliares = new ArrayList<EmpleadoAuxiliar>();
		carterizacionDAO = EJBLocator.getCarterizacionDAO();
		productoDAO =  EJBLocator.getProductoCEDAO();
		estudioAbogadoDAO = EJBLocator.getEstudioAbogadoDAO();
		tareaDAO = EJBLocator.getTareaDAO();
		expedienteDAO = EJBLocator.getExpedienteDAO();
		tareaReasigDAO = EJBLocator.getTareasReasigDAO();
		motivoDAO = EJBLocator.getMotivoDAO();
		expTareaProcesoDAO = EJBLocator.getExpedienteTareaProcesoDAO();
		procesoDAO = EJBLocator.getProcesoDAO();
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
	
	
	public String urlWS = props.getProperty(ConstantesParametros.CARGA_LDAP_WS);
	
	/**
	 * Flag de ejecución del proceso de LDAP_CARGA
	 * 0 -> No activo
	 * 1 -> Activo
	 * 
	 * Por defecto : "0"
	 */
	
	public String ldapCargaActivo = props.getProperty(ConstantesParametros.LDAP_CARGA_FLAG_ACTIVO);
	
	
	
	public void load(){
		
	}
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
	
	private EmpleadoLdap getEmpleadoLDAP(Usuario usu) {
		EmpleadoLdap emp = null;
		try {

			if (usu != null && usu.getUsuario() != null
					&& (usu.getUsuario().length() > 0)) {

				emp = new EmpleadoLdap();
				emp.setCodigoUsuario(usu.getUsuario());
				emp.setCodigoEntidad(usu.getCodigoEntidadUsuario());
				emp.setCodigoOficina(usu.getCodigoCentro());
				emp.setDescripcionOficina(usu.getDescripcionCentro());
				emp.setNombreEmpresa(usu.getEmpresa());
				emp.setEstado(!usu.isInactivo());
				emp.setNombres(usu.getNombres());
				emp.setApellidoPaterno(usu.getPrimerApellido());
				emp.setApellidoMaterno(usu.getSegundoApellido());
				emp.setTipoDocumento(usu.getTipoDocumento());
				emp.setNumeroDocumento(usu.getNumeroDocumento());
				emp.setPuesto(usu.getPuesto() != null ? usu.getPuesto()
						.getDescripcionPuesto() : "");
				emp.setCodigoPuesto(usu.getPuesto() != null ? usu.getPuesto()
						.getNombreCargoCorporativo() : "");
				emp.setCodigoFuncionalPuesto(usu.getPuesto() != null ? usu
						.getPuesto().getNombreCargoFuncionalLocal() : "");
				emp.setEmail(usu.getEMail()==null ? "" : usu.getEMail());

			} else {
				LOG.info("No se agrego empleado pues no posee codigo");
			}
		} catch (Exception e) {
			LOG.info("No se agrego empleado debido a un error", e);
		}
		return emp;
	}
	
	public void newLogic(){
		Proceso proceso = new Proceso();
		try {
			LOG.info("Iniciando ejecución de job ...");
			
			/* 
			 * 1. Poblar tabla EmpleadoLDAP a partir del SW LDAP.
			 *
			 */	
			
			Calendar fecha = new GregorianCalendar();
			int anio = fecha.get(Calendar.YEAR);
			int mes = fecha.get(Calendar.MONTH);
			int dia = fecha.get(Calendar.DAY_OF_MONTH);
			int hora = fecha.get(Calendar.HOUR_OF_DAY);
			int minuto = fecha.get(Calendar.MINUTE);
			int segundo = fecha.get(Calendar.SECOND);
			String sufijo = dia + "" + (mes + 1) + "" + anio + "_" + hora + ""
					+ minuto + "" + segundo;
			
			    
			String nombre = "CargaEmpleados_" + sufijo;
			
			proceso.setNombre(nombre);
			proceso.setFechaInicio(new Date());
			proceso.setFechaFin(null);
			proceso.setEstado(ConstantesBusiness.ESTADO_CARGA_INICIADO);
			proceso.setDescripcion("Proceso de carga Iniciado...");
			procesoDAO.save(proceso);
			
			URL url;
			String webServiceUrl =  this.urlWS;
	//		String webServiceUrl =  "http://118.180.34.112:9080/ws-ldap3/wService?wsdl";
			List<Usuario> empleadosLDAP  = new ArrayList<Usuario>();
			empleadoLdapDAO.deleteAll();
			try {
				url = new URL(webServiceUrl);
				WSLDAPServiceImplService proxy = new WSLDAPServiceImplService(url);
				empleadosLDAP = new ArrayList<Usuario>();
				empleadosLDAP = proxy.getWSLDAPServiceImplPort().obtenerUsuarios(new ArrayList<String>());
			} catch (MalformedURLException e) {
				LOG.info("Error de formato del Servicio Web", e);
			} catch (WSLdapException_Exception e1) {
				LOG.info("Erro al consultar el Servicio Web", e1);
			}
			
			
			String cargosAdmitidos = props.getProperty(ConstantesParametros.CARGOS_LDAP);
			LOG.info("Cargos configurados para la carga de empleados LDAP ... " + cargosAdmitidos);
			
			String listaNegraEmpleados = props.getProperty(ConstantesParametros.LISTA_NEGRA_CARGA);
			LOG.info("Empleddos configurados para que no participen de la carga ... " + listaNegraEmpleados);
			
			String codigoProducto = props.getProperty(ConstantesParametros.CODIGO_PRODUCTO_BASTANTEO);
			LOG.info("Codigo de producto para la carterizacion por defecto ... " + codigoProducto);
			
			LOG.info("Insertando empleados LDAP a la tabla EMPLEADO_LDAP_TMP de CONELE ...");
			for (Usuario empleadoLDAP : empleadosLDAP) {
				EmpleadoLdap emp = getEmpleadoLDAP(empleadoLDAP);
				if (emp!=null){
					if(cargosAdmitidos.toUpperCase().indexOf(emp.getCodigoPuesto().toUpperCase())>-1 && !emp.getCodigoPuesto().equalsIgnoreCase("")){
						empleadoLdapDAO.save(emp);
						LOG.info("Se inserto empleado en la tabla temporal " +  emp.getCodigoUsuario());
					}
				} 
			}
			
			
			proceso.setNombre(nombre);
			proceso.setEstado(ConstantesBusiness.ESTADO_CARGA_EMPLEADOS_TEMPORALES);
			proceso.setDescripcion("Empleados temporales del LDAP agregados...");
			procesoDAO.update(proceso);
			
			/* 
			 * 1.5.	Si el usuario no existe en LDAP y esta en la tabla de empleado 
			 * de la base de datos se cambia el flag de activo a "inactivo".
			 * 
			 */	
			
			LOG.info("Obteniendo empleados no presentes en la tabla temporal de LDAP ...");
			List<Empleado> empleadosADesactivar = empleadoDAO.getAllNotInLDAPTMP();
			
			LOG.info("Obteniendo empleados presentes en la tabla temporal LDAP...");
			List<Empleado> empleadosAActualizar = empleadoDAO.getAllInLDAPTMP();
			
			LOG.info("Obteniendo usuarios LDAP TMP a crearse como empleado ...");
			List<EmpleadoLdap> usuarios_a_crear_como_empleado = empleadoLdapDAO.getAllNotInEmpleado();
			
			
			/* Cambiar el flag de activo a inactivo a
			 * empleados no existentes en LDAP
			 */
			
			LOG.info("Iniciando cambio de flag de empleados no presentes en LDAP ...");
	
			for(Empleado empleado : empleadosADesactivar){
				
				try{
					
					if(listaNegraEmpleados.toUpperCase().indexOf(empleado.getCodigo().toUpperCase())==-1){
						
						LOG.info("Empleado a desactivar : " + empleado.getCodigo());
						empleado.setFlagActivo("0");	
						empleadoDAO.update(empleado);
						LOG.info("Empleado " + empleado.getCodigo() + " desactivado");
						
						List<Carterizacion> carteras = carterizacionDAO.findByWorkerId(empleado.getId());
						for(Carterizacion c : carteras){
							carterizacionDAO.delete(c);
							LOG.info("Carterizacion del empleado eliminada");
						}
						
						EmpleadoAuxiliar aux = new EmpleadoAuxiliar();
						aux.setEsActivo(false);
						aux.setEsOficinaNueva(false);
						aux.setOficinaAntigua(empleado.getOficina());
						aux.setOficinaNueva(empleado.getOficina());
						aux.setEmpleado(empleado);
						empleadosAuxiliares.add(aux);
//						LOG.info("Empleado " + aux.getEmpleado().getCodigo() + " fue agregado como empleado auxiliar");
					}
				}catch(Exception e){
					LOG.error("Error al desactivar a empleado : " + empleado.getCodigo(), e);
				}
			}
			
			
			/* Actualizar datos de empleados
			 * existentes en LDAP 
			 */
			
			LOG.info("Iniciando actualización de empleados presentes en el LDAP ...");
			
			for(Empleado empleado : empleadosAActualizar){
				EmpleadoAuxiliar aux = new EmpleadoAuxiliar();
				if(listaNegraEmpleados.toUpperCase().indexOf(empleado.getCodigo().toUpperCase())==-1){
					try{
					
						LOG.info("------------------------------------------------------");
							
						// Revisar fecha de vigencia
						
						LOG.info("Revisando FECHA_VIGENCIA de empleado : " + empleado.getCodigo());
							
						LOG.info("FECHA_VIGENCIA de empleado : " + empleado.getFechaVigencia());
						
						/*COMENTADO 25-11-2015*/
						//TODO: Consultar por fecha de vigencia.	
//						if(empleado.getFechaVigencia() != null){
//							if(empleado.getFechaVigencia().after(new Date())){
//								LOG.info("Empleado " + empleado.getCodigo() + " con FECHA_VIGENCIA no vencida, NO ACTUALIZARLO");
//								continue;
//							}
//							LOG.info("Empleado " + empleado.getCodigo() + " con FECHA_VIGENCIA vencida, SÍ ACTUALIZARLO");
//						}
										
						
						EmpleadoLdap usuario =  empleadoLdapDAO.findByCodigoUsuario(empleado.getCodigo());
							
						aux.setEsActivo(usuario.isEstado());
						aux.setEsOficinaNueva(empleado.getOficina().getCodigo().equalsIgnoreCase(usuario.getCodigoOficina()) ? false :true);
						aux.setOficinaAntigua(empleado.getOficina());
						Oficina oficinaNueva = new Oficina();
						oficinaNueva.setCodigo(usuario.getCodigoOficina());
						aux.setOficinaNueva(oficinaNueva);
						aux.setEmpleado(empleado);
		
						LOG.info("Actualizando nombres de empleado con código " + empleado.getCodigo());		
						empleado.setNombres(usuario.getNombres().trim());
								
						LOG.info("Actualizando apellido materno de empleado con código " + empleado.getCodigo());
						empleado.setApemat(usuario.getApellidoMaterno().trim());
								
						LOG.info("Actualizando apellido paterno de empleado con código " + empleado.getCodigo());
						empleado.setApepat(usuario.getApellidoPaterno().trim());
								
						LOG.info("Actualizando oficina de empleado con código " + empleado.getCodigo());
						empleado.setOficina(oficinaDAO.findByCodigo(usuario.getCodigoOficina().trim()));
								
						LOG.info("Actualizando fecha de vigencia de empleado con código " + empleado.getCodigo());
						empleado.setFechaVigencia(null);
								
						LOG.info("Actualizando correo electrónico de empleado con código " + empleado.getCodigo());
								
						empleado.setCorreo(usuario.getEmail().trim());
								
						LOG.info("Actualizando codigo de cargo de empleado con código " + empleado.getCodigo());	
						empleado.setCodcargo(usuario.getCodigoPuesto());
								
						LOG.info("Actualizando nombre de cargo de empleado con código " + empleado.getCodigo());
						empleado.setNomcargo(usuario.getPuesto());
								
						Perfil perfil = perfilXNivelDAO.obtenerPerfilAsignado(empleado.getCodigo(), empleado.getCodcargo(), empleado.getOficina().getCodigo());
						LOG.info("Actualizando perfil: '" + perfil.getDescripcion() + "' de empleado con código " + empleado.getCodigo());
						empleado.setPerfil(perfil);
								
						LOG.info("Actualizando nombre de empleado con código " + empleado.getCodigo());
		
						if(empleado.getPerfil().getFlagMostrarIniciales().equals("0")){
									
							LOG.info("Perfil de empleado con FLAG_MOSTRAR_INICIALES = 0");
							String nombreCompleto = empleado.getNombres() + " " + empleado.getApepat() + " " + empleado.getApemat();
							empleado.setNombresCompletos(nombreCompleto);
									
						}else if(empleado.getPerfil().getFlagMostrarIniciales().equals("1")) {
									
							LOG.info("Perfil de empleado con FLAG_MOSTRAR_INICIALES = 1");			
							String nombreCompleto = empleado.getNombres().substring(0,1) + empleado.getApepat().substring(0,1) + empleado.getApemat().substring(0,1);
							empleado.setNombresCompletos(nombreCompleto);
									
						}
						empleado.setFlagActivo(usuario.isEstado() ? "1" : "0");
						List<Carterizacion> carteras = carterizacionDAO
								.findByWorkerId(empleado.getId());
						
						
						for (Carterizacion c : carteras) {
							carterizacionDAO.delete(c);
						}
						
						if (empleado.getFlagActivo().equals("1")) {
							
							Carterizacion c = new Carterizacion();
							c.setEmpleado(empleado);
							c.setTerritorio(empleado.getOficina().getTerritorio());
							c.setProducto(productoDAO.findByCodigo(codigoProducto));
							c.setDescripcion(empleado.getOficina().getTerritorio().getCodigo());
							c.setCodigo(empleado.getOficina().getTerritorio().getCodigo());
							
							CarterizacionId carterizacionId = new CarterizacionId();
							carterizacionId.setIdEmpleadoFk(empleado.getId());
							carterizacionId.setIdProductoFk(c.getProductoCE().getId());
							carterizacionId.setIdTerritorioFk(c.getTerritorio().getId());
							c.setId(carterizacionId);
							
							
							carterizacionDAO.save(c);
						} 
		
						empleadoDAO.update(empleado);
	
						LOG.info("Empleado " + empleado.getCodigo()	+ " actualizado correctamente");
						
					}catch(Exception e){
						aux = null;
						LOG.error("Error al actualizar empleado " + empleado.getCodigo()  + ".", e);
					}
					if(aux !=null){
						empleadosAuxiliares.add(aux);
					}
				}
			}
			
			/**
			 * Crear nuevos empleados
			 * a partir de usuarios LDAP
			 */
			
			LOG.info("Iniciando creación de empleados desde el LDAP ...");
			
			for(EmpleadoLdap usuario : usuarios_a_crear_como_empleado){
				EmpleadoAuxiliar aux = new EmpleadoAuxiliar();
				try{
					if(oficinaDAO.findByCodigo(usuario.getCodigoOficina())!=null){
						
					
					LOG.info("------------------------------------------------------");
					
					LOG.info("Creando nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario());
					
					Empleado empleado = new Empleado();
					
					LOG.info("Estableciendo codigo '" + usuario.getCodigoUsuario() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
					
					empleado.setCodigo(usuario.getCodigoUsuario());
					
					LOG.info("Estableciendo nombre '" + usuario.getNombres() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
			
					// Nombres
					empleado.setNombres(usuario.getNombres());
					
					LOG.info("Estableciendo apellido materno '" + usuario.getApellidoMaterno() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
			
					// Apellidos
					empleado.setApemat(usuario.getApellidoMaterno());
					
					LOG.info("Estableciendo apellido paterno '" + usuario.getApellidoPaterno() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
	
					empleado.setApepat(usuario.getApellidoPaterno());
					
					LOG.info("Estableciendo oficina '" + usuario.getCodigoOficina() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
					
					// Oficina
					empleado.setOficina(oficinaDAO.findByCodigo(usuario.getCodigoOficina()));
					
					LOG.info("Estableciendo correo '" + usuario.getEmail() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
					
					// Correo electrónico
					empleado.setCorreo(usuario.getEmail());
					
					LOG.info("Estableciendo código de cargo '" + usuario.getCodigoPuesto() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
					
					// Puesto (cargo)
					empleado.setCodcargo(usuario.getCodigoPuesto());
					
					LOG.info("Estableciendo nombre de cargo '" + usuario.getPuesto() + "' a nuevo empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
					
					// Descripción del puesto (cargo)
					empleado.setNomcargo(usuario.getPuesto());
					
					// Se obtiene el perfil de acuerdo a las reglas configuradas
					Perfil perfil = perfilXNivelDAO.obtenerPerfilAsignado(empleado.getCodigo(), empleado.getCodcargo(), empleado.getOficina().getCodigo());
					
					LOG.info("Estableciendo perfil '" + perfil.getDescripcion() + "' a nuevo empleado a partir de las reglas de asignación de perfil configuradas");
					
					empleado.setPerfil(perfil);
					
					LOG.info("Estableciendo nombre completo de empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " ...");
								
					// Nombre completo
					if(empleado.getPerfil().getFlagMostrarIniciales().equals("0")){
						
						LOG.info("El nombre completo de empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " será una concatenación de nombres y apellidos ...");
						
						String nombreCompleto = empleado.getNombres() + " " + empleado.getApepat() + " " + empleado.getApemat();
						empleado.setNombresCompletos(nombreCompleto);
						
					}else if(empleado.getPerfil().getFlagMostrarIniciales().equals("1")) {
						
						LOG.info("El nombre completo de empleado a partir del usuario del LDAP : " + usuario.getCodigoUsuario() + " será una concatenación de las iniciales de nombres y apellidos ...");
						
						String nombreCompleto = empleado.getNombres().substring(0,1) + empleado.getApepat().substring(0,1) + empleado.getApemat().substring(0,1);
						empleado.setNombresCompletos(nombreCompleto);
						
					}
					
					empleadoDAO.save(empleado);
					
					
					Carterizacion c = new Carterizacion();
					c.setEmpleado(empleado);
					c.setTerritorio(empleado.getOficina().getTerritorio());
					c.setProducto(productoDAO.findByCodigo(codigoProducto));
					c.setDescripcion(empleado.getOficina().getTerritorio().getCodigo());
					c.setCodigo(empleado.getOficina().getTerritorio().getCodigo());
					
					CarterizacionId carterizacionId = new CarterizacionId();
					carterizacionId.setIdEmpleadoFk(empleado.getId());
					carterizacionId.setIdProductoFk(c.getProductoCE().getId());
					carterizacionId.setIdTerritorioFk(c.getTerritorio().getId());
					c.setId(carterizacionId);
					
					carterizacionDAO.save(c);
					
					aux.setEsActivo(true);
					aux.setEsOficinaNueva(true);
					Oficina oficinaNueva = new Oficina();
					oficinaNueva.setCodigo(usuario.getCodigoOficina());
					aux.setOficinaAntigua(oficinaNueva);
					aux.setOficinaNueva(oficinaNueva);
					aux.setEmpleado(empleado);
					
					LOG.info("Empleado creado correctamente");
					
					}
				}catch(Exception e){
					LOG.error("Error al crear empleado a partir del usuario del LDAP " + usuario.getCodigoUsuario(), e.getMessage());
					aux=null;
				}
				if(aux !=null){
					empleadosAuxiliares.add(aux);
				}
				
			}
			
			proceso.setEstado(ConstantesBusiness.ESTADO_CARGA_EMPLEADOS_ACTUALIZADOS);
			proceso.setDescripcion("Empleados de la aplicacion actualizados...");
			procesoDAO.update(proceso);
			
			
			reasignarExpedientes(empleadosAuxiliares);
			
			
			proceso.setEstado(ConstantesBusiness.ESTADO_CARGA_EMPLEADOS_ACTUALIZADOS);
			proceso.setDescripcion("Expedientes reasignados...");
			procesoDAO.update(proceso);
			
			/*Eliminamos los datos temporales de la tabla LDAP TMP*/
			empleadoLdapDAO.deleteAll();
			
			
			proceso.setEstado(ConstantesBusiness.ESTADO_CARGA_FINALIZADO);
			proceso.setFechaFin(new Date());
			proceso.setDescripcion("Proceso de carga finalizado...");
			procesoDAO.update(proceso);
		
		} catch (Exception e) {
			LOG.error("Error en el proceso de carga LDAP " , e);
			proceso.setEstado(ConstantesBusiness.ESTADO_CARGA_FINALIZADO_ERROR);
			proceso.setFechaFin(new Date());
			proceso.setDescripcion("Proceso de carga finalizado con error...");
			procesoDAO.update(proceso);
		}
		
		
	}
	
	public void reasignarExpedientes(List<EmpleadoAuxiliar> empleados){
		List<TareaBandejaVO> tareas = new ArrayList<TareaBandejaVO>();
		
		ConsultaCC consulta = new ConsultaCC();
		TareaBandejaVO filtro;
		
		consulta.setConsiderarUsuarios(false);
		filtro =  new TareaBandejaVO();
		filtro.setCodEstadoTarea(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE));
		filtroProcess(filtro, consulta);
		tareas = cargaRegProcess(consulta);
		
		String nombreTareas =  props.getProperty(ConstantesParametros.TAREAS_REASIGNACION_EXPEDIENTE);
		String unicoPerfil =  props.getProperty(ConstantesParametros.UNICO_PERFIL);
		for(EmpleadoAuxiliar emp : empleados){
			//Si el empleado nuevo actualizado es diferente a gestor entonces se debe reasignar a otros empleados gestores, cambiar la consula
			//a gestores de la oficina.

//			LOG.info("tareas del usuario " + emp.getEmpleado().getCodigo());
			
			for(TareaBandejaVO t : tareas){
				
					if(t.getCodResponsable().equalsIgnoreCase(emp.getEmpleado().getCodigo()) && nombreTareas.indexOf(t.getNomTarea())> -1 && !t.getNomTarea().isEmpty()){

						try {
							
							
							List<Empleado> empleadosPorOFicina = new ArrayList<Empleado>();
							Oficina oficina;
		
							if(emp.isEsActivo()==false){
								oficina = oficinaDAO.findByCodigo(emp.getOficinaAntigua().getCodigo()); 
							}else if (!emp.getEmpleado().getPerfil().getDescripcion().equalsIgnoreCase(unicoPerfil)){
								oficina = oficinaDAO.findByCodigo(emp.getOficinaAntigua().getCodigo());
							}else if (emp.isEsActivo()==true && emp.isEsOficinaNueva()){
								oficina = oficinaDAO.findByCodigo(emp.getOficinaAntigua().getCodigo());
							}else{
								oficina = null;
							}
							
							if(oficina!=null){
								
								empleadosPorOFicina = empleadoDAO.getGestorActivosPorOficina(oficina.getId(), unicoPerfil);
								if (empleadosPorOFicina.size()>0){
									Random r = new Random();
									int valorDado = r.nextInt(empleadosPorOFicina.size());
									reasignarExpediente(t,empleadosPorOFicina.get(valorDado).getCodigo());
								}
							}
						} catch (Exception e2) {
							LOG.error("Error al reasignar expediente  " + t.getExpedienteCC().getCodigoExpediente() ,e2);
						}
					}
				}
				
				
			}

	}
	
	
	
	public void reasignarExpediente(TareaBandejaVO task, String destino){
		LOG.info("reasignarExpediente (ActionEvent action)");
		
		ExpedienteCC expedienteCC = task.getExpedienteCC();
		
		//String numeroTarea = (expedienteCC.getNumeroTarea() == null?"0":expedienteCC.getNumeroTarea());
		String numeroTarea = (expedienteCC.getDatosFlujoCtaCte().getIdTarea() == null?"0":expedienteCC.getDatosFlujoCtaCte().getIdTarea());
		int idTarea = Integer.parseInt(numeroTarea);
		Tarea tarea = tareaDAO.findById(idTarea);
		
		Expediente expediente = expedienteDAO.load(Integer.parseInt(task.getExpediente()));
		
		//Empleado de = empleadoDAO.load(expediente.getEmpleado().getId());
		Empleado de = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Usuario de.."+de.getId());
		Empleado a = empleadoDAO.findByCodigo(destino);
		//expedienteCC.setIdUsuarioActual(a.getId()+"");
		//expedienteCC.setCodUsuarioActual(codUsuario);
		//expedienteCC.setNomUsuarioActual(a.getNombresCompletos());
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Usuario a.."+a.getId());
		
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();

		if (a != null) {
		    bandeja.transferirTarea(expedienteCC.getCodUsuarioActual(),destino,tkiid);
		    
		    expedienteCC.setCodUsuarioActual(a.getCodigo());
		    expedienteCC.setNomUsuarioActual(a.getNombres()+" "+a.getApepat()+" "+a.getApemat());
		    expedienteCC.setCodOficina(a.getOficina().getCodigo());
		    
		    if (a.getFlagAbogado() != null && a.getFlagAbogado().equals("1")) {
		    	expedienteCC.getDatosFlujoCtaCte().setCodUsuarioAbogado(a.getCodigo());
		    	expedienteCC.getDatosFlujoCtaCte().setIdUsuarioAbogado(a.getId().toString());
		    	expedienteCC.getDatosFlujoCtaCte().setNomUsuarioAbogado(a.getNombres()+" "+a.getApepat()+" "+a.getApemat());
		    	if (a.getEstudio() != null) {
		    		expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado(a.getEstudio().getId().toString());
		    		expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado(a.getEstudio().getDescripcion());
		    	} else {
		    		expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado("");
		    		expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado("");
		    	}
		    } else {
		    	expedienteCC.getDatosFlujoCtaCte().setCodUsuarioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setIdUsuarioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setNomUsuarioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setCodEstudioAbogado("");
		    	expedienteCC.getDatosFlujoCtaCte().setNomEstudioAbogado("");
		    }
		    expedienteCC.getOperacionesCtaCte().setIdEmpleadoActual(a.getId());
		    
		    bandeja.enviarExpediente(tkiid, expedienteCC);
		}
		
		TareasReasig tareaReasig = new TareasReasig();
		tareaReasig.setTarea(tarea);
		tareaReasig.setEmpleadoByIdEmpleadoAFk(a);
		tareaReasig.setEmpleadoByIdEmpleadoDeFk(de);
		tareaReasig.setExpediente(expediente);
		
		String idMotivo = props.getProperty(ConstantesParametros.ID_MOTIVO);
		
		Motivo motivo = motivoDAO.load(Integer.valueOf(idMotivo));
		tareaReasig.setMotivo(motivo);
		
		tareaReasigDAO.save(tareaReasig);
		
		// se actualiza la carga de trabajo al nuevo usuario
		ExpedienteTareaProceso expTarProc = expTareaProcesoDAO.findExpedienteTareaProceso(expediente.getId(), de.getId(), idTarea);
		if (expTarProc != null) {
			expTarProc.setIdEmpleado(a.getId());
			expTareaProcesoDAO.update(expTarProc);
		} else {
			LOG.warn("No existe carga de trabajo: (idExpediente={}, idEmpleado={}, idTarea={})", new Object[]{expediente.getId(), de.getId(), idTarea});
		}
		
		
		
	}
	
	private List<TareaBandejaVO> cargaRegProcess(ConsultaCC consulta) {
		List<TareaBandejaVO> lista = new ArrayList<TareaBandejaVO>();
		LOG.info("*********************cargaRegProcess(ConsultaCC consulta)*********************");
		RemoteUtils bandejaTareasBDelegate = new RemoteUtils();
		List<ExpedienteCC> expedientes = null;
		
			try {
				expedientes = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(consulta);
			} catch (RuntimeException e) {
				LOG.error("Error al obtener las tareas", e);
				expedientes = Collections.EMPTY_LIST;
			}
			LOG.info("Expedientes : {}", expedientes.size());
			for (ExpedienteCC expedienteCC : expedientes) {
				TareaBandejaVO tareaBandeja = crearTareaBandejaVO(expedienteCC); // al crear tareabandeja llena la fechAsignacion
				//SEQ_CE_IBM_TAREAS_REASIG_CC+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tareaBandeja.getCodSemaforo() "	+ tareaBandeja.getCodSemaforo());
				lista.add(tareaBandeja);
			}

		
		LOG.info("listaTareas Process: " + lista.size());
		return lista;
	}
	
	private TareaBandejaVO crearTareaBandejaVO(ExpedienteCC expedienteCC) {
		TareaBandejaVO tareaBandeja = new TareaBandejaVO();
		tareaBandeja.setExpedienteCC(expedienteCC);
		tareaBandeja.setId(expedienteCC.getCodigoExpediente()
				+ expedienteCC.getDatosFlujoCtaCte().getIdTarea()
				+ SUF_PENDIENTES);
		tareaBandeja.setTerritorioExp(expedienteCC.getDesTerritorio());
		tareaBandeja.setCodTarea(expedienteCC.getDatosFlujoCtaCte()
				.getIdTarea());
		tareaBandeja.getStrFechaAsignacion().setTarget(
				expedienteCC.getActivado().getTime());
		// tareaBandeja.getStrFechaAtencion().setTarget(expedienteCC.getFechaAtencion());
		tareaBandeja.getStrFechaInicio().setTarget(
				expedienteCC.getFechaRegistro());// fecha en la q se registro el
													// expediente
		// tareaBandeja.getStrFechaTermino().setTarget(Util.parseStringToDate("24/05/2012",
		// "dd/MM/yyyy"));
		// tareaBandeja.setCodSemaforo("verde.png");
		tareaBandeja.setDscCliente(expedienteCC.getRazonSocialCliente());
		tareaBandeja.setDscEstadoTarea(expedienteCC.getEstadoTarea());
		tareaBandeja.setDscEstadoExp(expedienteCC.getEstadoExpediente());
		tareaBandeja.setExpediente(expedienteCC.getCodigoExpediente());
		tareaBandeja.setNomTarea(expedienteCC.getDatosFlujoCtaCte()
				.getNombreTarea());
		tareaBandeja.setOficinaExp(expedienteCC.getDesOficina());
		tareaBandeja.setOperacion(expedienteCC.getDesOperacion());
		tareaBandeja.setResponsable(expedienteCC.getNomUsuarioActual());
		tareaBandeja.setCodResponsable(expedienteCC.getCodUsuarioActual());
		tareaBandeja.setExpedienteCC(expedienteCC);
		tareaBandeja.setTerritorioTarea(expedienteCC.getDesTerritorio());
		tareaBandeja.setOficinaTarea(expedienteCC.getDesOficina());
		
		if (expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("12") ||
			expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("13") ||
			expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("14")) {
		    
		    Empleado empAB = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
		    if (empAB != null && empAB.getEstudio() != null) {
			    EstudioAbogado estudioDAO = estudioAbogadoDAO.findByIdEstudio(empAB.getEstudio().getId());
			    tareaBandeja.setEstudio(estudioDAO.getDescripcion().trim());
			    tareaBandeja.setAbogadoEstudio(empAB.getNombres().trim()+" "+empAB.getApepat().trim()+" "+empAB.getApemat().trim());
		    }
		}else{
		    tareaBandeja.setAbogadoEstudio(expedienteCC.getDatosFlujoCtaCte().getNomUsuarioAbogado());
		    tareaBandeja.setEstudio(expedienteCC.getDatosFlujoCtaCte().getNomEstudioAbogado());
		}		
		
		return tareaBandeja;
	}
	
	public void filtroProcess(TareaBandejaVO filtro,ConsultaCC  consulta ) {
		
		// consulta.setIdEstadoExpediente(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO+"");
		LOG.info("*********************BandejaMB-filtroProcess()**************************");
		LOG.info("*********************consulta.getIdEstadoExpediente()**********"+consulta.getIdEstadoExpediente());
		if (!(filtro.getExpediente() == null || filtro.getExpediente().equals(""))) {
			LOG.info("f1- filtro.getExpediente(): " + filtro.getExpediente());
			consulta.setCodigoExpediente(filtro.getExpediente());
		}
		if (!(filtro.getCodEstadoExp() == null || filtro.getCodEstadoExp().equals("0"))) {
			LOG.info("f2-filtro.getCodEstadoExp(): " + filtro.getCodEstadoExp());
			consulta.setIdEstadoExpediente(filtro.getCodEstadoExp());
		}
		if (!(filtro.getCodEstadoTarea() == null || filtro.getCodEstadoTarea().equals("0"))) {
			LOG.info("f3- filtro.getCodEstadoTarea(): " + filtro.getCodEstadoTarea());
			consulta.setIdEstadoTarea(filtro.getCodEstadoTarea());
		}
		if (!(filtro.getCodTarea() == null || filtro.getCodTarea().equals(""))) {
			LOG.info("f4-filtro.getCodTarea(): " + filtro.getCodTarea());
			consulta.setNumeroTarea(filtro.getCodTarea());
		}
		if (!(filtro.getNomTarea() == null || filtro.getNomTarea().equals("0"))) {
			LOG.info("f5-filtro.getNomTarea(): " + filtro.getNomTarea());
			consulta.setNombreTarea(filtro.getNomTarea());
		}
		if (!(filtro.getCodCentral() == null || filtro.getCodCentral().equals(""))) {
			LOG.info("f6-filtro.getCodCentral(): " + filtro.getCodCentral());
			consulta.setCodCentralCliente(filtro.getCodCentral());
		}
		if (!(filtro.getOperacion() == null || filtro.getOperacion().equals("0"))) {
			LOG.info("f7-filtro.getOperacion(): " + filtro.getOperacion());
			consulta.setIdOperacion(filtro.getOperacion());
		}
		if (!(filtro.getNumDoi() == null || filtro.getNumDoi().equals(""))) {
			LOG.info("f8-filtro.getNumDoi(): " + filtro.getNumDoi());
			consulta.setNumDOICliente(filtro.getNumDoi());
		}
		if (!(filtro.getRazSoc() == null || filtro.getRazSoc().equals(""))) {
			LOG.info("f9-filtro.getRazSoc(): " + filtro.getRazSoc());
			consulta.setRazonSocialCliente(filtro.getRazSoc());
		}
		if (!(filtro.getResponsable() == null || filtro.getResponsable().equals(""))) {
			LOG.info("f10-filtro.getResponsable(): " + filtro.getResponsable());
			consulta.setCodUsuarioActual(filtro.getResponsable());
		}
		if (!(filtro.getOficinaExp() == null || filtro.getOficinaExp().equals("0"))) {
			LOG.info("f11-filtro.getOficinaExp(): " + filtro.getOficinaExp());
			consulta.setCodOficina(filtro.getOficinaExp());
		}
		if (!(filtro.getTerritorioExp() == null || filtro.getTerritorioExp().equals("0"))) {
			LOG.info("f12-filtro.getTerritorioExp(): " + filtro.getTerritorioExp());
			consulta.setIdTerritorio(filtro.getTerritorioExp());
		}
		if (!(filtro.getAbogadoEstudio() == null|| filtro.getAbogadoEstudio().equals("0") || filtro.getAbogadoEstudio().equals(""))) {
			LOG.info("f13-filtro.getAbogadoEstudio(): " + filtro.getAbogadoEstudio());
			consulta.setCodUsuarioAbogado(filtro.getAbogadoEstudio());
		}
		if (!(filtro.getGestor() == null || filtro.getGestor().equals(""))) {
			LOG.info("f14-filtro.getGestor(): " + filtro.getGestor());
			consulta.setCodUsuarioResponsable(filtro.getGestor());
		}
		if (!(filtro.getEstudio() == null || filtro.getEstudio().equals("0") || filtro.getEstudio().equals(""))) {
			LOG.info("f15-filtro.getEstudio(): " + filtro.getEstudio());
			consulta.setCodEstudioAbogado(filtro.getEstudio());
		}

		if (!(filtro.getStrFechaAsignacion().getLimInf() == null || filtro
				.getStrFechaAsignacion().getLimInf().equals(""))) {
			LOG.info("f16: "
					+ filtro.getStrFechaAsignacion().getLimInf());
			consulta.setFechaAsignacionInf(filtro.getStrFechaAsignacion()
					.getLimInf());
		}
		if (!(filtro.getStrFechaAsignacion().getLimSup() == null || filtro
				.getStrFechaAsignacion().getLimSup().equals(""))) {
			LOG.info("f17: "
					+ filtro.getStrFechaAsignacion().getLimSup());
			consulta.setFechaAsignacionSup(filtro.getStrFechaAsignacion()
					.getLimSup());
		}

		if (!(filtro.getStrFechaInicio().getLimInf() == null || filtro
				.getStrFechaInicio().getLimInf().equals(""))) {
			LOG.info("f18: " + filtro.getStrFechaInicio().getLimInf());
			consulta.setFechaInicioInf(filtro.getStrFechaInicio().getLimInf());
		}
		if (!(filtro.getStrFechaInicio().getLimSup() == null || filtro
				.getStrFechaInicio().getLimSup().equals(""))) {
			LOG.info("f19: " + filtro.getStrFechaInicio().getLimSup());
			consulta.setFechaInicioSup(filtro.getStrFechaInicio().getLimSup());
		}		
		if (!(filtro.getOficinaTarea() == null || filtro.getOficinaTarea()
				.equals("0"))) {
			LOG.info("f20: " + filtro.getOficinaTarea());
			consulta.setCodOficinaTarea(filtro.getOficinaTarea());
		}
		if (!(filtro.getTerritorioTarea() == null || filtro
				.getTerritorioTarea().equals("0"))) {
			LOG.info("f21: " + filtro.getTerritorioTarea());
			consulta.setIdTerritorioTarea(filtro.getTerritorioTarea());
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
	
//	@Override
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//			logic();
//		}catch(Exception e){
//			LOG.error("Error en el job de carga del LDAP : ", e);
//		}
//	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			newLogic();
		}catch(Exception e){
			LOG.error("Error en el job de carga del LDAP : ", e);
		}
	}
	
	public void execute()throws JobExecutionException{
		try {
			newLogic();
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
