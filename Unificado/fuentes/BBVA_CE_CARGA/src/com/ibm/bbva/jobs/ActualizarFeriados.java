/**
 * 
 */
package com.ibm.bbva.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

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

import bbva.ws.api.view.BBVAFacadeLocal;

import com.bbva.general.entities.Centro;
import com.bbva.general.entities.Feriado;
import com.bbva.general.entities.Territorio;
import com.ibm.bbva.comun.ConstantesParametros;
import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.entities.HorarioOficina;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.session.HorarioBeanLocal;
import com.ibm.bbva.session.HorarioOficinaBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.bbva.util.Utils;
//import com.bbva.general.entities.Oficina;
//Entidades for DataBase
//Entidades dek Web Service

/**
 * @author palvarad
 *
 */
public class ActualizarFeriados implements Job{
	private static final Logger LOG = LoggerFactory.getLogger("ActualizarFeriados");
	
	private ParametrosConfBeanLocal parametrosConfBean;
	
	private HorarioBeanLocal horarioBean;
	
	private TerritorioBeanLocal territorioBean;
	
	private OficinaBeanLocal oficinaBean;
	
	private HorarioOficinaBeanLocal horarioOficinaBean;
	
	private BBVAFacadeLocal bbvaFacade;
	
	public Integer frecuenciaDias;
	public String horaEjecucion ;
	public Integer flagCargaActivo ;
	
	private static final DateFormat FORMATO_HORAS = new SimpleDateFormat("HH:mm");
	
	public ActualizarFeriados(){		
		//LOG.debug("******INICIANDO JOB********");
		cargarVariables();
	}
	
	public void cargarVariables(){		
		try {
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			territorioBean = (TerritorioBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.TerritorioBeanLocal");
			oficinaBean = (OficinaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.OficinaBeanLocal");
			horarioBean = (HorarioBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.HorarioBeanLocal");
			horarioOficinaBean = (HorarioOficinaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.HorarioOficinaBeanLocal");
			bbvaFacade = (BBVAFacadeLocal) new InitialContext().lookup("ejblocal:bbva.ws.api.view.BBVAFacadeLocal");
						
			//LOG.debug("parametrosConfBean:"+parametrosConfBean);
			frecuenciaDias =  Integer.valueOf(parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, ConstantesParametros.FERIADOS_FRECUENCIA_DIAS).getValorVariable());
			LOG.debug("frecuenciaDias:"+frecuenciaDias);
			//LOG.debug("frecuenciaDias:"+frecuenciaDias);
			horaEjecucion =  parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, ConstantesParametros.FERIADOS_HORA_EJECUCION).getValorVariable();
			LOG.debug("horaEjecucion:"+horaEjecucion);
			//LOG.debug("horaEjecucion:"+horaEjecucion);
			flagCargaActivo =  Integer.valueOf(parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, ConstantesParametros.FERIADOS_FLAG_CARGA_ACTIVO).getValorVariable());
			LOG.debug("flagCargaActivo:"+flagCargaActivo);
			//LOG.debug("flagCargaActivo:"+flagCargaActivo);
			// Este mensaje error solo se muestra cuando el expedienteTCWPS tiene
			// una tarea cuyo id sea 50

			//String mError = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "MENSAJE_ERROR_DOCUMENTO_GESTION_CM").getValorVariable();
			//this.setMensajeError(mError);
		} catch (NamingException e) {
			LOG.debug("***Paso Error");
			e.printStackTrace();
		}
	}

	
	public void load(Scheduler sched) {
		LOG.debug("Iniciando carga");
		if(flagCargaActivo == null){
			LOG.debug("No se ha establecido en BD el parámetro feriados.cargaActivo para activar el job ...");
			LOG.debug("Cancelada la configuración del job.");
			return;
		}
		
		if(flagCargaActivo.equals("0")){
			LOG.debug("Los parámetros establecidos en base de datos indican que NO se configure la ejecución del job ...");
			LOG.debug("Cancelada la configuración del job.");
			return;
		}
		
		LOG.debug("Configurando ejecución de job ...");
			
		try {

//			LOG.debug(" Iniciando scheduler ...");
//		    	
//		    SchedulerFactory sf = new StdSchedulerFactory();
//		    Scheduler sched = sf.getScheduler();
//		    
//		    LOG.debug(" Scheduler obtenido ...");
			    
		    LOG.debug(" Agendando ejecución ...");
		    
		    LOG.debug(" Hora ejecución : " + horaEjecucion);
		    
		    LOG.debug(" Frecuencia días : " + frecuenciaDias);
		    
		    JobDetail job = JobBuilder.newJob(ActualizarFeriados.class).withIdentity("JOB_ACTUALIZAR_FERIADOS", "GRUPO_JOBS_CARGA").build();
		    
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
		    
		    
		    LOG.debug(" La primera ejecución será el : " + startDate.toString());

		    SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder
		    		.newTrigger()
		    		.withIdentity("TRIGGER_JOB_ACTUALIZAR_FERIADOS", "GRUPO_JOBS_CARGA")
		    		.forJob(job)
		    		.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24 * this.frecuenciaDias).repeatForever())
		    		.startAt(startDate)
		    		.build();
		    
		    sched.scheduleJob(job, trigger);
		    sched.start();
		    
		    LOG.debug("Job agendado satisfactoriamente ...");
		    		    
		}catch(Exception e){
			LOG.error("Error cuando se agendaba la ejecución del job : ", e);
		}
		
		
		
	}
	
	public void logic(){
		LOG.debug("************LOGIC************");
		LOG.debug("Iniciando ejecución de job ...");
	
		actualizarTerritorios();
		actualizarOficinas();
		actualizarFeriados();
		
	}
	
	
	
	public void actualizarTerritorios() {
		LOG.debug("Actualizacion de territorios");
		try{
						
			LOG.debug("Obteniendo Territorios del web service");
			List<Territorio> listaTerritoriosWS = (List<Territorio>) (List<Territorio>) bbvaFacade.getTerritorioListado();

			LOG.debug("Obteniendo Territorios de la base de datos");
			List<com.ibm.bbva.entities.Territorio> listaTerritoriosDB = territorioBean.buscarTodos();
			
			/*
			if(listaTerritoriosDB != null){
				int itemTerritorio = 0;
				for(com.ibm.bbva.entities.Territorio territorio : listaTerritoriosDB){
					itemTerritorio++;
					LOG.debug("Territorio Nro "+itemTerritorio);
					LOG.debug("COD:"+territorio.getCodigo());
					LOG.debug("DES:"+territorio.getDescripcion());
				}
			}
			*/
			
			for(Territorio territorioWS : listaTerritoriosWS){
				
				if(territorioWS != null){				
					if(!existeTerritorio(territorioWS.getCodigoTerritorio(), listaTerritoriosDB)){
						LOG.debug("Registrando el nuevo territorio en la base de datos con código="+territorioWS.getCodigoTerritorio());
						com.ibm.bbva.entities.Territorio territorioDB = new com.ibm.bbva.entities.Territorio();
						territorioDB.setCodigo(territorioWS.getCodigoTerritorio());
						territorioDB.setDescripcion(territorioWS.getCodigoTerritorio() + " - " + territorioWS.getDescripcionTerritorio());
						territorioDB.setFlagActivo("1");
						territorioBean.create(territorioDB);
					}else{
						LOG.debug("Actualizando el nuevo territorio en la base de datos con código="+territorioWS.getCodigoTerritorio());
						com.ibm.bbva.entities.Territorio territorioDB = obtenerTerritorio(territorioWS.getCodigoTerritorio(), listaTerritoriosDB);
						if(territorioDB != null){
							territorioDB.setDescripcion(territorioWS.getCodigoTerritorio() + " - " + territorioWS.getDescripcionTerritorio());
							territorioDB.setFlagActivo("1");
							territorioBean.edit(territorioDB);
						}
					}					
				}
			}
			
			/*
			TerritoriosData territorioData = new TerritoriosData();
			List<Territorio> listaTerritoriosDATA = territorioData.getTerritorioListado();
			if(listaTerritoriosDATA != null){
				int itemTerritorio = 0;
				for(Territorio territorio : listaTerritoriosDATA){
					com.ibm.bbva.entities.Territorio territorioDB = new com.ibm.bbva.entities.Territorio();
					
					territorioDB.setCodigo(territorio.getCodigoTerritorio());
					territorioDB.setDescripcion(territorio.getCodigoTerritorio() + " - " + territorio.getDescripcionTerritorio());
					territorioDB.setFlagActivo("1");
					
					itemTerritorio++;
					LOG.debug("Territorio Nro "+itemTerritorio);
					LOG.debug("Codigo:"+territorio.getCodigoTerritorio());
					LOG.debug("Descripcion:"+territorio.getDescripcionTerritorio());
					
					territorioBean.create(territorioDB);
				}
			}
			*/
			
		}catch (Exception e) {
			LOG.error("Error en actualizar territorios", e);
			e.printStackTrace();
		}
	}
	
	public void actualizarOficinas() {
		LOG.debug("Actualizacion de oficinas");
		try{
			LOG.debug("Obteniendo Oficinas del web service");
			//List<Oficina> listaOficinasWS = (List<Oficina>) tablaGeneral.getOficinas(null, null);
			List<Centro> listaOficinasWS = (List<Centro>) bbvaFacade.getCentroListado("");
			
			LOG.debug("Obteniendo Oficinas de la base de datos");
			List<Oficina> listaOficinasDB = oficinaBean.buscarTodos();
			
			/** @commenter Daniel Flores
			 *  No entiendo por qué el anterior desarrollador crea la oficina
			 *  considerando solo el código, la descripción y la ubicación. La resolución
			 *  de la incidencia solo abarca el problema de registros duplicados al momento
			 *  de una edición de oficina.
			 */
			
			for(Centro oficinaWS : listaOficinasWS){
				
				if(oficinaWS != null){
					if(!existeOficina(oficinaWS.getCodigoOficina(), listaOficinasDB)){
						LOG.debug("Registrando la nueva oficina en la base de datos con código="+oficinaWS.getCodigoOficina());
						Oficina oficinaDB = new Oficina();
						
						oficinaDB.setCodigo(oficinaWS.getCodigoOficina());
						oficinaDB.setDescripcion(oficinaWS.getNombre());
						oficinaDB.setUbicacion(oficinaWS.getNombre());
						oficinaDB.setFlagActivo("1");
						
						if (oficinaWS.getTerritorio() !=null && oficinaWS.getTerritorio().getCodigoTerritorio() != null) {
							com.ibm.bbva.entities.Territorio territorio = territorioBean.buscarPorCodigo(oficinaWS.getTerritorio().getCodigoTerritorio());
							if (territorio == null)
								LOG.debug("No se encontró territorio con código "+oficinaWS.getTerritorio().getCodigoTerritorio());
							oficinaDB.setTerritorio(territorio);
						}
						
						oficinaBean.create(oficinaDB);
					}else{
						LOG.debug("Actualizando la nueva oficina en la base de datos con código="+oficinaWS.getCodigoOficina());
						Oficina oficinaDB = obtenerOficina(oficinaWS.getCodigoOficina(), listaOficinasDB);
						if(oficinaDB != null){
							oficinaDB.setDescripcion(oficinaWS.getNombre());
							oficinaDB.setUbicacion(oficinaWS.getNombre());
							oficinaDB.setFlagActivo("1");
							
							if (oficinaWS.getTerritorio() !=null && oficinaWS.getTerritorio().getCodigoTerritorio() != null) {
								com.ibm.bbva.entities.Territorio territorio = territorioBean.buscarPorCodigo(oficinaWS.getTerritorio().getCodigoTerritorio());
								if (territorio == null)
									LOG.debug("No se encontró territorio con código "+oficinaWS.getTerritorio().getCodigoTerritorio());
								oficinaDB.setTerritorio(territorio);
							}
							
							oficinaBean.edit(oficinaDB);
						}
					}			
				}
			}
		}catch (Exception e) {
			LOG.error("Error en actualizar territorios", e);
		}
	}
	
	
	public void actualizarFeriados() {
		LOG.debug("Actualizacion de feriados");
		try{
			LOG.debug("Obteniendo todos los feriados del web service");
			
			List<Feriado> listaFeriadosWS = (List<Feriado>) bbvaFacade.getFeriadoListado();
			//List<Feriado> listaFeriadosWS = (List<Feriado>) FeriadosTestData.getFeriadoListado();
//			actualizarHorario(listaFeriadosWS);
//			
//			LOG.debug("Obteniendo todos los feriados de base de datos");
//			List<Horario> listaHorariosDB = horarioBean.buscarTodos();
//			
//			actualizarHorarioOficina(listaFeriadosWS, listaHorariosDB);
			
			for (Feriado feriado : listaFeriadosWS) {
				List<Oficina> oficinasFeriado = new ArrayList<Oficina>();
				if (feriado.getIndicador().equals("L")) { // para un solo ubigeo
					List<Centro> listaOficinasWS = (List<Centro>) bbvaFacade.getCentroListado("");
					for (Centro centro : listaOficinasWS) {
						if (feriado.getUbigeo().equals(centro.getDistrito().getIDUbigeo())) {
							Oficina oficina = oficinaBean.buscarPorCodigo(centro.getCodigoOficina());
							if (oficina != null) {
								LOG.debug("No existe oficina con código="+centro.getCodigoOficina());
								oficinasFeriado.add(oficina);
							}
						}
					}
				} else { // todas las oficinas
					oficinasFeriado = oficinaBean.buscarTodos();
				}
				for (Oficina oficina : oficinasFeriado) {
					Date fechaFeriado = Utils.xmlGregorianCalendarToDate(feriado.getFecha());

					Calendar fechaFeriadoCalendar = Calendar.getInstance();
					fechaFeriadoCalendar.setTime(fechaFeriado);
					
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(0);
					cal.set(
							fechaFeriadoCalendar.get(Calendar.YEAR), 
							fechaFeriadoCalendar.get(Calendar.MONTH), 
							fechaFeriadoCalendar.get(Calendar.DAY_OF_MONTH),
							0,
							0, 
							0
					);
					Date date = cal.getTime();
					
					Horario horario = horarioBean.buscarFeriado(date, oficina.getCodigo());
					boolean existe = false;
					if (horario == null) {
						horario = new Horario();
						existe = false;
					} else {
						existe = true;
					}
					horario.setHoraInicio(null);
					horario.setHoraFin(null);
					horario.setDiaInicio(date);
					horario.setDiaFin(null);
					horario.setExcepcion("1");
					horario.setActivo("1");
					horario.setTodoDia("1");
					
					if (existe) {
						horarioBean.edit(horario);
					} else {
						horario = horarioBean.create(horario);
						HorarioOficina horarioOficina = new HorarioOficina();
						horarioOficina.setHorario(horario);
						horarioOficina.setOficina(oficina);
						
						horarioOficinaBean.create(horarioOficina);
					}
				}
			}
		}catch (Exception e) {
			LOG.error("Error en actualizar feriados", e);
		}
	}
	
	public void actualizarHorario(List<Feriado> listaFeriadosWS){
		
		try{
			LOG.debug("Obteniendo todos los horarios (feriados) de base de datos");
			List<Horario> listaFeriadoDB = horarioBean.buscarTodos();
			
			for(Feriado feriado : listaFeriadosWS){
				if(feriado != null){
						
					if(obtenerIDHorario(feriado, listaFeriadoDB) == 0){
						LOG.debug("Registrando el feriado en la tabla horario de base de datos");
						Horario horarioNuevo = new Horario();						
						Date fechaFeriado = Utils.xmlGregorianCalendarToDate(feriado.getFecha());

						Calendar fechaFeriadoCalendar = Calendar.getInstance();
						fechaFeriadoCalendar.setTime(fechaFeriado);
						
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(0);
						cal.set(
								fechaFeriadoCalendar.get(Calendar.YEAR), 
								fechaFeriadoCalendar.get(Calendar.MONTH), 
								fechaFeriadoCalendar.get(Calendar.DAY_OF_MONTH),
								0,
								0, 
								0
						);
						Date date = cal.getTime();
						
						horarioNuevo.setDiaInicio(date);
						horarioNuevo.setHoraInicio(FORMATO_HORAS.format(fechaFeriadoCalendar.getTime()));
						
						if(fechaFeriadoCalendar.get(Calendar.HOUR_OF_DAY) == 0)
							horarioNuevo.setTodoDia("1");
						else
							horarioNuevo.setTodoDia("0");
						
						horarioNuevo.setActivo("1");
						
						// Flag que determina que es un día feriado
						horarioNuevo.setExcepcion("1");
						
						horarioNuevo = horarioBean.create(horarioNuevo);
						
						LOG.debug("Nuevo horario registrado en la tabla TBL_CE_IBM_HORARIO_CE con id = " + horarioNuevo.getId());
					}
				}
			}
		}catch (Exception e) {
			LOG.error("Error en actualizar horarios", e);
		}
		
	}
	
	public void actualizarHorarioOficina(List<Feriado> listaFeriadosWS, List<Horario> listaHorariosDB){
		
		try{
			LOG.debug("Obteniendo Oficinas de la base de datos");
			List<Oficina> listaOficinasDB = oficinaBean.buscarTodos();
			
			for(Oficina oficinaDB : listaOficinasDB){
				if(oficinaDB != null){
					long idOficina = oficinaDB.getId();
					String codOficina = oficinaDB.getCodigo().toString();
					String ubigeoOficina = obtenerUbigeoOficina(codOficina);
					
					if(ubigeoOficina != null && !ubigeoOficina.equalsIgnoreCase("") && !ubigeoOficina.equals("null")){
						
						//Buscar todos los feriados por el ubigeo					
						for(Feriado feriadoWS : listaFeriadosWS){
							if(feriadoWS.getUbigeo() != null && ubigeoOficina.equalsIgnoreCase(feriadoWS.getUbigeo().toString())){							
								long idHorario = obtenerIDHorario(feriadoWS, listaHorariosDB);
								
								if(idHorario != 0){
									if(!existeHorarioOficina(idOficina, idHorario)){
										grabarHorarioOficinaDB(idOficina, idHorario);
									}
								}
								
							}
						}
					}	
				}
			}
		}catch (Exception e) {
			LOG.debug("Error en actualizar horario oficina");
		}
		
	}
	
	
	public void grabarHorarioOficinaDB(long idOficina, long idHorario){
		Horario horario = new Horario();
		horario.setId(idHorario);
		
		Oficina oficina = new Oficina();
		oficina.setId(idOficina);
		
		HorarioOficina horarioOficina = new HorarioOficina();
		horarioOficina.setHorario(horario);
		horarioOficina.setOficina(oficina);
		
		horarioOficinaBean.create(horarioOficina);
	}
	
	/**
	 * Obtiene el ID de un determinado Horario obtenido
	 * del WS
	 * 
	 * @param feriadoWS
	 * @param listaHorariosDB
	 * @return
	 */
	public long obtenerIDHorario(Feriado feriadoWS, List<Horario> listaHorariosDB){
		long idHorario = 0;
		
		Date fechaFeriado = Utils.xmlGregorianCalendarToDate(feriadoWS.getFecha());
		
		// Para obtener hora y minuto
		Calendar fechaFeriadoCalendar = Calendar.getInstance();
		fechaFeriadoCalendar.setTime(fechaFeriado);
		String fechaFeriado_horaInicio = FORMATO_HORAS.format(fechaFeriadoCalendar.getTime());
		
		// Para obtener año, mes y día
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(fechaFeriadoCalendar.get(Calendar.YEAR), fechaFeriadoCalendar.get(Calendar.MONTH), fechaFeriadoCalendar.get(Calendar.DAY_OF_MONTH),0,0,0);
		Date fechaFeriado_diaInicio = cal.getTime();
		
		if(listaHorariosDB != null){
			for(Horario horarioDB : listaHorariosDB){
				if(horarioDB != null){
					if(horarioDB.getDiaInicio() != null && 
						horarioDB.getDiaInicio().equals(fechaFeriado_diaInicio) && 
						horarioDB.getHoraInicio() != null && 
						horarioDB.getHoraInicio().equals(fechaFeriado_horaInicio))
					{
						idHorario = horarioDB.getId();
						LOG.debug("ID HORARIO con Hora inicio = " + fechaFeriado_horaInicio + " : " + idHorario);
						break;
					}
				}
			}
		}
		
		return idHorario;
	}	
	
	public boolean existeHorarioOficina(long idOficina, long idHorario){
		boolean bExiste = false;
		
		if(idOficina == 0 || idHorario == 0)
			return false;
		
		HorarioOficina horarioOficina = null;
		
		try{
			horarioOficina = horarioOficinaBean.buscarPorIdOficinaIdHorario(idOficina, idHorario);
		}catch(javax.persistence.NoResultException ex){
			horarioOficina = null;
		}catch(javax.ejb.EJBException ex){
			horarioOficina = null;
		}catch(Exception ex){
			horarioOficina = null;
		}
		
		if(horarioOficina != null){
			bExiste = true;
		}
		
		return bExiste;
	}
	
	
	public String obtenerUbigeoOficina(String codOficina){
		String ubigeo = "";
		
		if(!codOficina.equalsIgnoreCase("")){
			LOG.debug("Obteniendo Oficinas del web service");
			List<com.bbva.general.entities.Oficina> listaOficinasWS = (List<com.bbva.general.entities.Oficina>) bbvaFacade.getOficinas(codOficina, null);
			
			for(com.bbva.general.entities.Oficina oficinaWS : listaOficinasWS){
				if(oficinaWS != null){
					ubigeo = oficinaWS.getDistrito().getIDUbigeo().toString();
					break;
				}
			}						
		}
				
		return ubigeo;
	}
		
	//existe oficina en DB
	public boolean existeOficina(String codigoOficina, List<Oficina> listaOficinasDB){
		
		boolean bExiste = false;
		
		//List<Horario> listaferiadosDB = horarioBean.buscarTodos();
		if(listaOficinasDB != null){
			for(Oficina oficinaDB : listaOficinasDB){
				if(oficinaDB != null){
					if(oficinaDB.getCodigo().equalsIgnoreCase(codigoOficina)){
						bExiste = true;
						break;
					}
				}
			}
		}
		
		return bExiste;
	};
	
	public com.ibm.bbva.entities.Oficina obtenerOficina(String codigoOficina, List<Oficina> listaOficinasDB){
		
		com.ibm.bbva.entities.Oficina oficina = null;
		
		if(listaOficinasDB != null){
			for(Oficina oficinaDB : listaOficinasDB){
				if(oficinaDB != null){
					if(oficinaDB.getCodigo().equalsIgnoreCase(codigoOficina)){
						oficina = oficinaDB;
						break;
					}
				}
			}
		}
		
		return oficina;
	}
	
	public boolean existeTerritorio(String codigoTerritorio, List<com.ibm.bbva.entities.Territorio> listaTerritoriosDB){
		
		boolean bExiste = false;
		
		//List<Horario> listaferiadosDB = horarioBean.buscarTodos();
		if(listaTerritoriosDB != null){
			for(com.ibm.bbva.entities.Territorio territorioDB : listaTerritoriosDB){
				if(territorioDB != null){
					if(territorioDB.getCodigo().equalsIgnoreCase(codigoTerritorio)){
						bExiste = true;
						break;
					}
				}
			}
		}
		
		return bExiste;
	};
	
	public com.ibm.bbva.entities.Territorio obtenerTerritorio(String codigoTerritorio, List<com.ibm.bbva.entities.Territorio> listaTerritoriosDB){
		
		com.ibm.bbva.entities.Territorio territorio = null;
		
		//List<Horario> listaferiadosDB = horarioBean.buscarTodos();
		if(listaTerritoriosDB != null){
			for(com.ibm.bbva.entities.Territorio territorioDB : listaTerritoriosDB){
				if(territorioDB != null){
					if(territorioDB.getCodigo().equalsIgnoreCase(codigoTerritorio)){
						territorio = territorioDB;
						break;
					}
				}
			}
		}
		
		return territorio;
	};
	
	
	
	
	/**
	 * Ejecución de lógica del 
	 * Job
	 */
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			LOG.error("Ejecutando el Job");
			logic();
		}catch(Exception e){
			LOG.error("Error en el job de carga de Feriados : ", e);
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
