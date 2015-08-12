package pe.ibm.bpd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Cliente;
import pe.ibm.bean.Constantes;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ConsultaServicio;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.Producto;
import pe.ibm.rest.RestBPM;
import pe.ibm.rest.RestBPMImpl;
import pe.ibm.util.Convertidor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.entities.VistaBandejaExpediente;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.VistaBandejaExpedienteBeanLocal;
import com.ibm.xtq.ast.nodes.ValueOf;

public class RemoteUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(RemoteUtils.class);
	
	private RestBPM restBPM = new RestBPMImpl();
	private static final String BANDEJA_SEARCH_TEMPLATE_NAME = "BBVA_TC_BANDEJA";
	private static final String INICIO_CADENA_JSON = "{\"expediente\":";
	private static final String INICIO_CONSULTA_JSON = "{\"consulta\":";
	
	private static final String INICIO_CONSULTA_JSON_2 = "{\"consulta\":\"";
	private static final String FINAL_CADENA_JSON_2 = "\"}"; 
	private static final String NODE_CANTIDAD_EXP = "cantidadExpedientes"; 
	
	private static final String FINAL_CADENA_JSON = "}";
	private static final String NODE_DATA = "data";
	private static final String NODE_ITEMS = "items";
	private static final String NODE_RESULTADO = "resultado"; 
	private static final String NODE_ITEM_IDTASK = "idTask";
	private static final String NODE_ITEM_ASIGNEDUSER = "asignedUser";
	private static final String NODE_ITEM_APPATERNO = "apPaterno";
	private static final String NODE_ITEM_APMATERNO = "apMaterno";
	private static final String NODE_ITEM_NOMBRES = "nombre";
	private static final String NODE_TASKID = "tkiid";	
	private static final String NODE_VARIABLES = "variables";
	private static final String NODE_EXPEDIENTE = "expediente";
	private static final String NODE_RESPONSE = "response";
	private static HashMap CONSTANTE_TABLA_TAREAS = null;
	private static final String NODE_ITEM_IDPIID = "piid";	
	private static final String ID_NO_EXISTE_TAREA = "-1";
	private static final String NODE_PIID = "piid";
	
	private static final String NODE_ITEM_ID_TAREA = "idTarea";
	private static final String NODE_ITEM_COD_EXP = "codExpediente"; 
	private static final String NODE_ITEM_FECHA_ACTIVACION = "fechaActivacion";	
	private static final String CONS_FORMATO_TIME_ZONE = "GMT-5:00:00";
	private static final String CONS_FORMATO_FECHA_1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	private VistaBandejaExpedienteBeanLocal vistaExpedienteListLocalBean;
	private AnsBeanLocal ansBeanLocal;
	public Map<Long, Map<Long, ArrayList<Ans>>> mapaAnsProdTarea;
	
	public RemoteUtils() {
		super();
		
		try {
			vistaExpedienteListLocalBean = (VistaBandejaExpedienteBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.VistaBandejaExpedienteBeanLocal");	
			ansBeanLocal = (AnsBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.AnsBeanLocal");			
			
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		CONSTANTE_TABLA_TAREAS = new HashMap();
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_1, Constantes.RUTA_NAVEGACION_WEB_TAREA_1); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_2, Constantes.RUTA_NAVEGACION_WEB_TAREA_2); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_3, Constantes.RUTA_NAVEGACION_WEB_TAREA_3); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_4, Constantes.RUTA_NAVEGACION_WEB_TAREA_4); 
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_5, Constantes.RUTA_NAVEGACION_WEB_TAREA_5);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_6, Constantes.RUTA_NAVEGACION_WEB_TAREA_6);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_7, Constantes.RUTA_NAVEGACION_WEB_TAREA_7);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_10, Constantes.RUTA_NAVEGACION_WEB_TAREA_10);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_11, Constantes.RUTA_NAVEGACION_WEB_TAREA_11);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_12, Constantes.RUTA_NAVEGACION_WEB_TAREA_12);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_13, Constantes.RUTA_NAVEGACION_WEB_TAREA_13);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_14, Constantes.RUTA_NAVEGACION_WEB_TAREA_14);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_15, Constantes.RUTA_NAVEGACION_WEB_TAREA_15);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_16, Constantes.RUTA_NAVEGACION_WEB_TAREA_16);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_17, Constantes.RUTA_NAVEGACION_WEB_TAREA_17);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_18, Constantes.RUTA_NAVEGACION_WEB_TAREA_18);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_19, Constantes.RUTA_NAVEGACION_WEB_TAREA_19);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_20, Constantes.RUTA_NAVEGACION_WEB_TAREA_20);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_26, Constantes.RUTA_NAVEGACION_WEB_TAREA_26);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_27, Constantes.RUTA_NAVEGACION_WEB_TAREA_27);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_32, Constantes.RUTA_NAVEGACION_WEB_TAREA_32);
		CONSTANTE_TABLA_TAREAS.put(Constantes.ID_TAREA_33, Constantes.RUTA_NAVEGACION_WEB_TAREA_33);		
		// TODO Apéndice de constructor generado automáticamente
	}

	/***
	 * Obtiene un listado de tareas o Expedientes de Negocio activos de Process
	 * @param consulta Bandeja de busqueda
	 */
	public List<ExpedienteTCWPSWeb> obtenerListaTareasBandBusqueda (Consulta consulta){
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();
		List<ExpedienteTCWPSWeb> taskListWeb = new ArrayList<ExpedienteTCWPSWeb>();

		String tkiid = "";
		String piid = "";
		ExpedienteTCWPS expedienteTC = null;

		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);

		LOG.info("respuesta:::"+respuesta);

		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();

		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;

			piid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).getAsString() : "";

			LOG.info("tkiid:::"+tkiid);
			LOG.info("piid:::"+piid);
			String idTarea=null;
			String fechaActivacion =null;
			
			if(!tkiid.equals("-1")){
				idTarea = !items.get(i).getAsJsonObject().get(NODE_ITEM_ID_TAREA).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_ID_TAREA).getAsString() : "-1";
				fechaActivacion = !items.get(i).getAsJsonObject().get(NODE_ITEM_FECHA_ACTIVACION).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_FECHA_ACTIVACION).getAsString() : null;
			}
				
			String coExpediente = !items.get(i).getAsJsonObject().get(NODE_ITEM_COD_EXP).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_COD_EXP).getAsString() : "-1";
			
			if (idTarea != null) {
				try {
					Integer.parseInt(idTarea);
				} catch (NumberFormatException e) {
					// los expedientes con error traen data no numérica en idTarea
					idTarea = "";
				}
			}
			
			expedienteTC = new ExpedienteTCWPS();
			expedienteTC.setIdTarea(idTarea);
			expedienteTC.setCodigo(coExpediente);			

		    expedienteTC.setTaskID(tkiid);

			if (!ID_NO_EXISTE_TAREA.equals(tkiid)){

				Calendar cal = Calendar.getInstance();

			    try { //2013-10-28T18:47:34Z
					TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
					SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
					readFormat.setTimeZone(utc);
					LOG.info("TIEMPO !!!!!"+ fechaActivacion);
			    	Date fechaStart = readFormat.parse(fechaActivacion);
					cal.setTime(fechaStart);
				} catch (ParseException e) {
					LOG.error(e.getMessage(), e);
				}

			    expedienteTC.setActivado(cal);

				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 

				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){						
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
			}else
				expedienteTC.setActivado(null);


			LOG.info("Tarea:::::"+i);
			LOG.info("codigoExp::"+expedienteTC.getCodigo());
			LOG.info("getIdTarea::"+expedienteTC.getIdTarea());
			LOG.info("fechaActivacion::"+expedienteTC.getActivado());

			taskList.add(expedienteTC);

		}

		//BULK Oracle
		taskListWeb = Convertidor.fromExpedienteTCWPSToExpedienteTCWPSWeb(taskList, "0");
		taskListWeb= actualizarDataBD(taskListWeb, consulta);

		//taskList = aplicarAjustes(taskList, consultaServicio);

		return taskListWeb;	
	}
	
	/***
	 * Obtiene un listado de tareas o Expedientes de Negocio activos de Process
	 * @param consulta
	 */
	public List<ExpedienteTCWPSWeb> obtenerListaTareasBandPendiente (Consulta consulta){
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();
		List<ExpedienteTCWPSWeb> taskListWeb = new ArrayList<ExpedienteTCWPSWeb>();

		String tkiid = "";
		String piid = "";
		ExpedienteTCWPS expedienteTC = null;

		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);

		LOG.info("respuesta:::"+respuesta);

		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();

		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;

			piid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).getAsString() : "";

			LOG.info("tkiid:::"+tkiid);
			LOG.info("piid:::"+piid);
/*
			String apPaterno = !items.get(i).getAsJsonObject().get(NODE_ITEM_APPATERNO).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_APPATERNO).getAsString() : null;
			String apMaterno = !items.get(i).getAsJsonObject().get(NODE_ITEM_APMATERNO).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_APMATERNO).getAsString() : null;
			String nombre = !items.get(i).getAsJsonObject().get(NODE_ITEM_NOMBRES).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_NOMBRES).getAsString() : null;			

			String apPaternoFilter = consulta.getApPaterno();
			String apMaternoFilter = consulta.getApMaterno();
			String nombreFilter = consulta.getNombres();
			if (apPaternoFilter != null && (apPaterno == null || !apPaterno.toUpperCase().contains(apPaternoFilter.toUpperCase())) ) continue;
			if (apMaternoFilter != null && (apMaterno == null || !apMaterno.toUpperCase().contains(apMaternoFilter.toUpperCase())) ) continue;
			if (nombreFilter != null && (nombre == null || !nombre.toUpperCase().contains(nombreFilter.toUpperCase())) ) continue;
*/
			String idTarea = !items.get(i).getAsJsonObject().get(NODE_ITEM_ID_TAREA).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_ID_TAREA).getAsString() : "-1";
			String coExpediente = !items.get(i).getAsJsonObject().get(NODE_ITEM_COD_EXP).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_COD_EXP).getAsString() : "-1";
			String fechaActivacion = !items.get(i).getAsJsonObject().get(NODE_ITEM_FECHA_ACTIVACION).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_FECHA_ACTIVACION).getAsString() : null;
			
			if (idTarea != null) {
				try {
					Integer.parseInt(idTarea);
				} catch (NumberFormatException e) {
					// los expedientes con error traen data no numérica en idTarea
					idTarea = "";
				}
			}

			expedienteTC = new ExpedienteTCWPS();
			expedienteTC.setIdTarea(idTarea);
			expedienteTC.setCodigo(coExpediente);			

		    expedienteTC.setTaskID(tkiid);

			if (!ID_NO_EXISTE_TAREA.equals(tkiid)){

				Calendar cal = Calendar.getInstance();

			    try { //2013-10-28T18:47:34Z
					TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
					SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
					readFormat.setTimeZone(utc);
					LOG.info("TIEMPO !!!!!"+ fechaActivacion);
			    	Date fechaStart = readFormat.parse(fechaActivacion);
					cal.setTime(fechaStart);
				} catch (ParseException e) {
					LOG.error(e.getMessage(), e);
				}

			    expedienteTC.setActivado(cal);

				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 

				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){						
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
			}else
				expedienteTC.setActivado(null);


			LOG.info("Tarea:::::"+i);
			LOG.info("codigoExp::"+expedienteTC.getCodigo());
			LOG.info("getIdTarea::"+expedienteTC.getIdTarea());
			LOG.info("fechaActivacion::"+expedienteTC.getActivado());

			taskList.add(expedienteTC);

		}

		//BULK Oracle
		taskListWeb = Convertidor.fromExpedienteTCWPSToExpedienteTCWPSWeb(taskList, "1");
		taskListWeb= actualizarDataBD(taskListWeb, consulta);

		//taskList = aplicarAjustes(taskList, consultaServicio);

		return taskListWeb;	
	}
	
	
	private Map<Long, Map<Long, ArrayList<Ans>>> agregarMapaProductoTarea (Map<Long, Map<Long, ArrayList<Ans>>>  mapa, 
			Ans objAns) {
		//LOG.info("metodo agregarMapaProductoTarea");
		Map<Long, ArrayList<Ans>> mapaTarea= mapa.get(objAns.getProducto().getId());
		
		if(mapaTarea==null){
			mapaTarea=new TreeMap<Long,ArrayList<Ans>>();
			//LOG.info("...creando map para producto:"+objAns.getProducto().getId());
			mapa.put(objAns.getProducto().getId(), mapaTarea);
		}
		
		mapaTarea= agregarMapaTarea(mapaTarea, objAns);
		
		return mapa;

	}
	
	private Map<Long, ArrayList<Ans>> agregarMapaTarea (Map<Long, ArrayList<Ans>>  mapa, 
			Ans objAns) {
		//LOG.info("metodo agregarMapaTarea");
		ArrayList<Ans> listAns= mapa.get(objAns.getTarea().getId());
		
		if(listAns==null){
			//LOG.info("...creando lis para tarea:"+objAns.getTarea().getId());
			listAns=new ArrayList();
			mapa.put(objAns.getTarea().getId(), listAns);
		}
		//LOG.info("Agregando Ans a lista:"+objAns.getId());
		listAns.add(objAns);
	
		return mapa;
	}	
	
	public boolean validarLong(String cad){
		Long valor;
		try{
			valor=new Long(cad);
			return true;
		}catch(NumberFormatException e){
			LOG.error(e.getMessage(), e);
			return false;
		}
	}
	
	/***
	 * BULK Oracle
	 * Obtiene los detalles del expediente
	 * @param consulta
	 * @param lista
	 */	
	public List<ExpedienteTCWPSWeb> actualizarDataBD(List<ExpedienteTCWPSWeb> lista, Consulta consulta){
		LOG.info("Metodo actualizarDataBD");
		List<ExpedienteTCWPSWeb> taskListResult = new ArrayList<ExpedienteTCWPSWeb>();
		List<Long> listIdsExpediente=new ArrayList<Long>();
		List<Long> listIdsTareas=new ArrayList<Long>();
		ArrayList arrayList=filtrosABuscar(consulta);
		List<VistaBandejaExpediente>  listBandejaExpedientes=null;
		
		for(ExpedienteTCWPSWeb obj : lista){
			if(obj!=null){
				listIdsExpediente.add(new Long(obj.getCodigo()));
				if(obj.getIdTarea()!=null && !obj.getIdTarea().equals("") && validarLong(obj.getIdTarea()))
					listIdsTareas.add(new Long(obj.getIdTarea()));
			}
			
		}
		
		if(listIdsExpediente!=null)
			LOG.info("Size listIdsExpediente:::"+listIdsExpediente.size());
		
		if(listIdsTareas!=null)
			LOG.info("Size listIdsTareas:::"+listIdsTareas.size());		
		
		List<Long> listIdsProd = consulta.getListIdsProd();
		List<Ans> listAns = ansBeanLocal.cargarValoresAns(listIdsTareas, listIdsProd);
			
		mapaAnsProdTarea= new TreeMap<Long,Map<Long,ArrayList<Ans>>>();
		
		for(Ans objAns : listAns){
			if(objAns!=null && objAns.getProducto()!=null)
				mapaAnsProdTarea= agregarMapaProductoTarea(mapaAnsProdTarea,objAns);
		}


		if(mapaAnsProdTarea!=null)
			LOG.info("Size mapaAnsProdTarea:::"+mapaAnsProdTarea.size());	
		
		if(listIdsProd!=null)
			LOG.info("Size listIdsProd:::"+listIdsProd.size());	
		
		if(listAns!=null)
			LOG.info("Size listAns:::"+listAns.size());	
		
		if(consulta.getTipoConsulta()==Constantes.COD_BAND_ASIGNACION){			
			listBandejaExpedientes= vistaExpedienteListLocalBean.buscarPorIdsExpedientes_BandjAsignacion(listIdsExpediente, arrayList, listIdsProd);
		}else{
			//if(tipoBand==Constantes.COD_BAND_ASIGNACION)
				listBandejaExpedientes= vistaExpedienteListLocalBean.buscarPorIdsExpedientes(listIdsExpediente, arrayList);
		}
		
		if(listBandejaExpedientes!=null)
		for(VistaBandejaExpediente obj : listBandejaExpedientes){
			//LOG.info(" id:::"+obj.getId());
			
			for(ExpedienteTCWPSWeb objTCWPS : lista){
				if(obj.getId()==Long.parseLong(objTCWPS.getCodigo())){
					LOG.info("OK EXP:"+objTCWPS.getCodigo());
					objTCWPS.setEstado(obj.getDesEstado());
					objTCWPS.setSegmento(obj.getDesSegmento());
					objTCWPS.setIdTipoOferta(String.valueOf(obj.getIdTipoOferta()));
					objTCWPS.setTipoOferta(obj.getDesTipoOferta());
					objTCWPS.setCliente(new Cliente());
					objTCWPS.getCliente().setTipoDOI(obj.getDesTipoDoc());
					objTCWPS.getCliente().setNumeroDOI(obj.getNumDoi());
					objTCWPS.getCliente().setApPaterno(obj.getApePat());
					objTCWPS.getCliente().setApMaterno(obj.getApeMat());
					objTCWPS.getCliente().setNombre(obj.getNombre());
					objTCWPS.setProducto(new Producto());
					objTCWPS.getProducto().setIdProducto(String.valueOf(obj.getIdProd()));
					objTCWPS.getProducto().setProducto(obj.getDesProducto());
					objTCWPS.getProducto().setSubProducto(obj.getDesSubProducto());
					objTCWPS.setMoneda(obj.getDesTipoMoneda());
					objTCWPS.setLineaCredito(obj.getLineaCredSol());
					objTCWPS.setMontoAprobado(obj.getLineaCredAprob());
					objTCWPS.setDesOficina(obj.getDesOficina());
					objTCWPS.setDesTerritorio(obj.getDesTerritorio());
					objTCWPS.setCodigoRVGL(obj.getRvgl());
					objTCWPS.setNumeroContrato(obj.getNroContrato());
					objTCWPS.setObservacion(obj.getComentario());
					objTCWPS.setIdOficina(String.valueOf(obj.getIdOficina()));
					objTCWPS.setFlagRetraer(obj.getFlagRetraer());
					objTCWPS.setFlagRetraer(null);
					objTCWPS.setAccionExp(obj.getAccion());
					objTCWPS.setIdGrupoSegmento(String.valueOf(obj.getIdGrupoSegmento()));
					objTCWPS.setNumeroDevoluciones(obj.getNroDevoluciones());
					//objTCWPS.setEstado(String.valueOf(obj.getIdEstado()));
					//objTCWPS.setDescripEstado(obj.getDesEstado());
					//objTCWPS.setIdTarea(String.valueOf(obj.getIdTarea()));
					/**
					 * Cambio 27 de mayo
					 * OPTIMIZACION BANDEJA DE ASIGNACION
					 * */
					objTCWPS.setPerfilUsuarioActual(obj.getDesPerfil());
					objTCWPS.setCodigoUsuarioActual(obj.getCodEmpleado());
					objTCWPS.setNombreUsuarioActual(obj.getNomEmpleado());
					objTCWPS.setIdOficinaUsu(String.valueOf(obj.getIdOficinaUsu()));
					
					if(listAns!=null && listAns.size()>0){
						
						Map<Long, ArrayList<Ans>> mapaTarea= mapaAnsProdTarea.get(Long.parseLong(objTCWPS.getProducto().getIdProducto()));
						
						if(mapaTarea!=null && mapaTarea.size()>0){
							ArrayList<Ans> listAnsTemp= null;
							
							if(objTCWPS.getIdTarea()!=null && !objTCWPS.getIdTarea().equals("") && validarLong(objTCWPS.getIdTarea()))
								listAnsTemp= mapaTarea.get(Long.parseLong(objTCWPS.getIdTarea()));
							
							if(listAnsTemp!=null && listAnsTemp.size()>0){
								LOG.info("Coincidencia con producto:"+objTCWPS.getProducto().getIdProducto()+" y tarea:"+objTCWPS.getIdTarea()+" es:"+listAnsTemp.size());
								ArrayList<Ans> listAnsTemp2=new ArrayList<Ans>();
								
								for(Ans objAnsTemp :listAnsTemp){
									if(objAnsTemp.getTipoOferta()!=null && objAnsTemp.getTipoOferta().getId()==Long.parseLong(objTCWPS.getIdTipoOferta()) && 
											objAnsTemp.getGrupoSegmento()!=null && objAnsTemp.getGrupoSegmento().getId()==Long.parseLong(objTCWPS.getIdGrupoSegmento())){
										listAnsTemp2.add(objAnsTemp);
									}
								}
								if(listAnsTemp2!=null && listAnsTemp2.size()>0){
									//LOG.info("Coincidencia con Tipo Oferta:"+objTCWPS.getIdTipoOferta()+" y grupo segemento:"+objTCWPS.getIdGrupoSegmento()+" es :"+listAnsTemp2.size());
									boolean band=false;
									if(listAnsTemp2!=null && listAnsTemp2.size()>1){
										//LOG.info("Tiene configurado estado, listAnsTemp2:"+listAnsTemp2.size());
										for(Ans objtTemp2: listAnsTemp2){
											if(objtTemp2.getEstadoCE()!=null && objtTemp2.getEstadoCE().getId()==obj.getIdEstadoCe()){
												LOG.info("Estado coincide::::Tar:"+objtTemp2.getId());
												objTCWPS.setValorAnsAmarillo(objtTemp2.getAmarilloMinutos().toString());
												objTCWPS.setValorAnsVerde(objtTemp2.getVerdeMinutos().toString());	
												LOG.info("Tar Amarillo:"+objTCWPS.getValorAnsAmarillo());
												LOG.info("Tar Verde:"+objTCWPS.getValorAnsVerde());												
												band=true;
												break;
											}
										}
										
										if(!band){
											for(Ans objtTemp2: listAnsTemp2){
												if(objtTemp2.getEstadoCE()==null){
													LOG.info("Estado No coincide:::: Tar:"+objtTemp2.getId());
													objTCWPS.setValorAnsAmarillo(objtTemp2.getAmarilloMinutos().toString());
													objTCWPS.setValorAnsVerde(objtTemp2.getVerdeMinutos().toString());	
													LOG.info("Tar Amarillo:"+objTCWPS.getValorAnsAmarillo());
													LOG.info("Tar Verde:"+objTCWPS.getValorAnsVerde());													
													break;
												}
											}
										}
									}else{
										LOG.info("Estado CE es null");
										LOG.info("Tar:"+listAnsTemp2.get(0).getId());
										objTCWPS.setValorAnsAmarillo(listAnsTemp2.get(0).getAmarilloMinutos().toString());
										objTCWPS.setValorAnsVerde(listAnsTemp2.get(0).getVerdeMinutos().toString());
										LOG.info("Tar Amarillo:"+objTCWPS.getValorAnsAmarillo());
										LOG.info("Tar Verde:"+objTCWPS.getValorAnsVerde());
									}									
								}else
									LOG.info("listAnsTemp2 es vacío");

							}else{
								if(objTCWPS.getIdTarea()!=null)
									LOG.info("listAnsTemp es vacío para TAREA:"+objTCWPS.getIdTarea());
								else
									LOG.info("listAnsTemp es vacío para TAREA nula");
								
								LOG.info("Seteando valores no validos para ANS amarillo y verde");
								objTCWPS.setValorAnsAmarillo("-1");
								objTCWPS.setValorAnsVerde("-1");
								LOG.info("Tar Amarillo:"+objTCWPS.getValorAnsAmarillo());
								LOG.info("Tar Verde:"+objTCWPS.getValorAnsVerde());								
							}
						}else
							LOG.info("mapaTarea es vacío");
					
					}else
						LOG.info("listAns es vacío");
					/**
					 * */
					
					Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
					Iterator iterator = set.iterator(); 
					
					while(iterator.hasNext()) { 
						Map.Entry me = (Map.Entry)iterator.next(); 
						if(objTCWPS.getIdTarea()!=null && !objTCWPS.getIdTarea().equals("") && me.getKey().toString().equals(objTCWPS.getIdTarea())){
							objTCWPS.setNombreNavegacionWeb(me.getValue().toString());
							break;
						}
					}
					
					taskListResult.add(objTCWPS);
					
					break;
				}
			}//Fin FOR list ExpedienteTCWPS
			
		}//Fin FOR list VistaBandejaExpediente
		
		return taskListResult;
	}
	
	public ArrayList filtrosABuscar(Consulta consulta){
		ArrayList arrayList=new ArrayList(5);
		String campo=null;
		
		if(consulta.getTipoConsulta()!=Constantes.COD_BAND_ASIGNACION){
			
	        campo = consulta.getNumeroContrato();
	        LOG.info("campo NumeroContrato::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//NUMERO DE CONTRATO 	(0)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getCodRVGL();
	        LOG.info("campo CodigoRVGL::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//CODIGO RVGL			(1)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getCodPreEvaluador();
	        LOG.info("campo CodigoPreEvaluador::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//CODIGO PRE EVALUADOR 	(2)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getSegmento();
	        LOG.info("campo Segmento::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//SEGMENTO 				(3)
	        else
	        	arrayList.add(null);
	        
			campo = consulta.getSubProducto();
	        LOG.info("campo SubProducto::::"+campo);
	        if (campo!=null && !"".equals(campo) && !campo.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) )
	        	arrayList.add(campo);									//SUB PRODUCTO 			(4)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getTipoOferta();
	        LOG.info("campo TipoOferta::::"+campo);
	        if (campo!=null && !"".equals(campo) && !campo.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) )
	        	arrayList.add(campo);									//TIPO DE OFERTA		(5)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getApPaterno();
	        LOG.info("campo ApePaterno::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//APE PATERNO			(6)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getApMaterno();
	        LOG.info("campo ApeMaterno::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//APE MATERNO			(7)
	        else
	        	arrayList.add(null);
	        
	        campo = consulta.getNombres();
	        LOG.info("campo Nombre::::"+campo);
	        if (campo!=null && !"".equals(campo))
	        	arrayList.add(campo);									//NOMBRES				(8)
	        else
	        	arrayList.add(null);			
		}else{
			if(consulta.getTipoConsulta()==Constantes.COD_BAND_ASIGNACION){
				
		        campo = consulta.getPerfilUsuarioSession();
		        LOG.info("campo PerfilUsuarioSession::::"+campo);
		        if (campo!=null && !"".equals(campo) && campo.equals(Constantes.COD_PERFIL_SUBGERENTE)){
		        	LOG.info("PERFIL SUBGERENTE::::USUARIO:::"+consulta.getOficinaUsuarioSession());
		        	arrayList.add(consulta.getOficinaUsuarioSession());									//ID USUARIO SESSION 	(0)
			}else
		        	arrayList.add(null);				
				
			}
			
		}
        
        
        return arrayList;
        
	}

	public List<ExpedienteTCWPSWeb> obtenerInstanciasTareasPorUsuario (Consulta consulta){
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();
		List<ExpedienteTCWPSWeb> taskListWeb = new ArrayList<ExpedienteTCWPSWeb>();
		//String cadenaUsuarios = null;
		String tkiid = "";
		String piid = "";
		
		
		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);
		
		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();
		
		List<String> lstIdTask = new ArrayList<String>();
		List<String> lstIdPiid = new ArrayList<String>();
		
		if(items!=null && items.size()>0){
			LOG.info("items = "+items.size());
		}else
			LOG.info("items es nulo o vacio ");

		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;
			
			piid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDPIID).getAsString() : "";
			
			//LOG.info("tkiid:::"+tkiid);
			//LOG.info("piid:::"+piid);
			
			//String asignedUser = "";
			
//			if (!ID_NO_EXISTE_TAREA.equals(tkiid)) // si No es instancia en proceso
//				asignedUser = !items.get(i).getAsJsonObject().get(NODE_ITEM_ASIGNEDUSER).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_ASIGNEDUSER).getAsString() : null;
			
			String apPaterno = !items.get(i).getAsJsonObject().get(NODE_ITEM_APPATERNO).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_APPATERNO).getAsString() : null;
			String apMaterno = !items.get(i).getAsJsonObject().get(NODE_ITEM_APMATERNO).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_APMATERNO).getAsString() : null;
			String nombre = !items.get(i).getAsJsonObject().get(NODE_ITEM_NOMBRES).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_NOMBRES).getAsString() : null;			
			
			String apPaternoFilter = consulta.getApPaterno();
			String apMaternoFilter = consulta.getApMaterno();
			String nombreFilter = consulta.getNombres();
			if (apPaternoFilter != null && (apPaterno == null || !apPaterno.toUpperCase().contains(apPaternoFilter.toUpperCase())) ) continue;
			
			if (apMaternoFilter != null && (apMaterno == null || !apMaterno.toUpperCase().contains(apMaternoFilter.toUpperCase())) ) continue;
			
			if (nombreFilter != null && (nombre == null || !nombre.toUpperCase().contains(nombreFilter.toUpperCase())) ) continue;

			if (consulta.isConsiderarUsuarios()) {         // Pendientes
//				List<String> usuarios = consulta.getUsuarios();
//				cadenaUsuarios = "";
//				
//				if (usuarios != null && usuarios.size() > 0){   //pendientes de usuarios dependientes
//					for (String usr : usuarios) {
//						if (usr.equalsIgnoreCase(asignedUser))
//							lstIdTask.add(tkiid);
//					}
//					
//				}else{   // considerar usuario logueado
//					if(consulta.getCodUsuarioActual() != null && consulta.getCodUsuarioActual().equalsIgnoreCase(asignedUser))
//						lstIdTask.add(tkiid);
//				}
					lstIdTask.add(tkiid);
			} else { //Busqueda		
				
				if (ID_NO_EXISTE_TAREA.equals(tkiid))
					lstIdPiid.add(piid);
				else
					lstIdTask.add(tkiid);
			}
		}
		
		if (lstIdTask.size() != 0) {
			//Consulta de los detalles de las tareas
			LOG.info("lstIdTask.size():"+lstIdTask.size());
			
			respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
			
			for (int i = 0; i < items.size(); i++) {
				
				respuesta = items.get(i).toString();
				
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
				
				tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
				
				try{					
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
						.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
				}catch(Exception ex){
					LOG.info("TC No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
							+ ex.getMessage());
					continue;
				}				
					
				ExpedienteTCWPS	expedienteTC = Convertidor.fromJSONtoExpedienteTC(respuesta,jsonElement,"1");
				
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 

				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){						
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}

				taskList.add(expedienteTC);
			}
		}			
		
		
		if (lstIdPiid.size() != 0) {
			//Consulta por los piids para los casos de los expedientes en proceso
			
			LOG.info("lstIdPiid.size():"+lstIdPiid.size());
			
			respuesta = restBPM.getProcessDetailsBulk(lstIdPiid);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
			
			for (int i = 0; i < items.size(); i++) {
				
				respuesta = items.get(i).toString();
				
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
				
				piid =  jsonElement.getAsJsonObject().get(NODE_PIID).getAsString();
				
		
				try{
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
							.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
				}catch(Exception ex){
					LOG.info("TC No se pudo cargar el proceso con piid : "+ piid + "  "
							+ ex.getMessage());
					continue;
				}				
				
				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC(respuesta,jsonElement,"0");
				
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 
	
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
	
				taskList.add(expedienteTC);
			}
		}
		
			
		taskList = aplicarAjustes(taskList, consultaServicio);
		taskListWeb = Convertidor.fromExpedienteTCWPSToExpedienteTCWPSWeb(taskList, "0");
		return taskListWeb;	
	}

	public List<ExpedienteTCWPSWeb> listarTareasBandejaAsignacion (Consulta consulta){
		
		// Parametros de consulta considerados: Fecha Inicio, Fecha Fin, Rol, Tarea, Estado, Oficina, Usuario, CodigoExp y tipoBusqieda =  4
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();
		List<ExpedienteTCWPSWeb> taskListWeb = new ArrayList<ExpedienteTCWPSWeb>();
		ExpedienteTCWPS expedienteTC = null;

		String tkiid = "";


		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);

		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();

		List<String> lstIdTask = new ArrayList<String>();

		if(items!=null && items.size()>0){
			System.out.println("items = "+items.size());
		}else
			System.out.println("items es nulo o vacio ");

//		for (int i = 0; i < items.size(); i++) {
//			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
//			items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;			
//
//			lstIdTask.add(tkiid);
//
//		}

		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;

			String idTarea = !items.get(i).getAsJsonObject().get(NODE_ITEM_ID_TAREA).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_ID_TAREA).getAsString() : "-1";
			String coExpediente = !items.get(i).getAsJsonObject().get(NODE_ITEM_COD_EXP).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_COD_EXP).getAsString() : "-1";
			String fechaActivacion = !items.get(i).getAsJsonObject().get(NODE_ITEM_FECHA_ACTIVACION).isJsonNull() ? items.get(i).getAsJsonObject().get(NODE_ITEM_FECHA_ACTIVACION).getAsString() : null;

			expedienteTC = new ExpedienteTCWPS();
			expedienteTC.setIdTarea(idTarea);
			expedienteTC.setCodigo(coExpediente);		
		    expedienteTC.setTaskID(tkiid);

			//if (!ID_NO_EXISTE_TAREA.equals(tkiid)){

				Calendar cal = Calendar.getInstance();

			    try { //2013-10-28T18:47:34Z
					TimeZone utc = TimeZone.getTimeZone(CONS_FORMATO_TIME_ZONE);
					SimpleDateFormat  readFormat = new SimpleDateFormat(CONS_FORMATO_FECHA_1);
					readFormat.setTimeZone(utc);
					System.out.println("TIEMPO !!!!!"+ fechaActivacion);
			    	Date fechaStart = readFormat.parse(fechaActivacion);
					cal.setTime(fechaStart);
				} catch (ParseException e) {
					System.out.println(e.getMessage());
				}

			    expedienteTC.setActivado(cal);

				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 

				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){						
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
//			}else
//				expedienteTC.setActivado(null);

			System.out.println("Tarea:::::"+i);
			System.out.println("codigoExp::"+expedienteTC.getCodigo());
			System.out.println("getIdTarea::"+expedienteTC.getIdTarea());
			System.out.println("fechaActivacion::"+expedienteTC.getActivado());

			taskList.add(expedienteTC);

		}

		//BULK Oracle
		//taskList = actualizarDataBD(taskList, consulta);
		taskListWeb = Convertidor.fromExpedienteTCWPSToExpedienteTCWPSWeb(taskList, "1");
		taskListWeb= actualizarDataBD(taskListWeb, consulta);
		//taskList = aplicarAjustes(taskList, consultaServicio);

		return taskListWeb;
	} 
	
	/***
	 * Obtiene un listados de tareas para la bandeja de reasignacion
	 * @param consulta
    */
	public List<ExpedienteTCWPS> listarTareasBandejaReasignacion (Consulta consulta){
		// Parametros de consulta considerados: 4
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();

		String cadenaUsuarios = null;
		String tkiid = "";
		

		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);

		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();

		List<String> lstIdTask = new ArrayList<String>();
				
		LOG.info("cadenaUsuarios = "+cadenaUsuarios);
		
		if(items!=null && items.size()>0){
			LOG.info("items = "+items.size());
		}else
			LOG.info("items es nulo o vacio ");
		
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
			items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;			
			
			lstIdTask.add(tkiid);
			
		}
	
		if (lstIdTask.size() != 0) {
			//Consulta de los detalles de las tareas
			LOG.info("lstIdTask.size():"+lstIdTask.size());
			
			respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
	
			for (int i = 0; i < items.size(); i++) {
	
				respuesta = items.get(i).toString();
	
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
	
				tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
				try{
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
						.getAsJsonObject().get(NODE_VARIABLES).getAsJsonObject().get(NODE_EXPEDIENTE).toString();
				}catch(Exception ex){
					LOG.info("TC No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
							+ ex.getMessage());
					continue;
				}
	
				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC(respuesta,jsonElement,"1");
	
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 
	
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}
				taskList.add(expedienteTC);
			}
		}	
					

		return taskList;
	}
	
	/***
	 * Obtiene un listados de tareas activas de Gestion en Process
	 * @param consulta
    */
	public List<ExpedienteTCWPS> obtenerListaTareasGestion(Consulta consulta){
		List<ExpedienteTCWPS> taskList = new ArrayList<ExpedienteTCWPS>();
		
		String tkiid = "";
		
		ConsultaServicio consultaServicio = Convertidor.fromConsultaCCToConsultaServicio(consulta);
		String respuesta = consultaListaTareasTC(consultaServicio);
		LOG.info("respuesta:::"+respuesta);
		
		JsonElement jsonElement = new JsonParser().parse(respuesta);
		JsonArray items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESULTADO)
				.getAsJsonObject().get(NODE_ITEMS).getAsJsonArray();
		
		List<String> lstIdTask = new ArrayList<String>();
		
		for (int i = 0; i < items.size(); i++) {
			tkiid = !items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).isJsonNull() ? 
					items.get(i).getAsJsonObject().get(NODE_ITEM_IDTASK).getAsString() : null;			
		
			LOG.info("tkiid:::"+tkiid);

			lstIdTask.add(tkiid);
		}
		
		if (lstIdTask.size() != 0) {
			//Consulta de los detalles de las tareas
			LOG.info("lstIdTask.size():"+lstIdTask.size());
			
			respuesta = restBPM.getTaksDetailsBulk(lstIdTask);
			jsonElement = new JsonParser().parse(respuesta);
			items = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_RESPONSE).getAsJsonArray();
			
			for (int i = 0; i < items.size(); i++) {
				
				respuesta = items.get(i).toString();
				
				jsonElement = new JsonParser().parse(respuesta);
				jsonElement = jsonElement.getAsJsonObject().get(NODE_DATA);
				
				tkiid =  jsonElement.getAsJsonObject().get(NODE_TASKID).getAsString();
				
				try{				
					respuesta = jsonElement.getAsJsonObject().get(NODE_DATA)
							.getAsJsonObject().get(NODE_VARIABLES).toString();					
					
				}catch(Exception ex){
					LOG.info("TC No se pudo cargar la actividad con taskiid : "+ tkiid + "  "
							+ ex.getMessage());
					continue;
				}				
				
				ExpedienteTCWPS expedienteTC = Convertidor.fromJSONtoExpedienteTC_Mantenimiento(respuesta,jsonElement,"1");
				
				Set set = CONSTANTE_TABLA_TAREAS.entrySet(); 
				Iterator iterator = set.iterator(); 
	
				while(iterator.hasNext()) { 
					Map.Entry me = (Map.Entry)iterator.next(); 
					if(me.getKey().toString().equals(expedienteTC.getIdTarea())){
						expedienteTC.setNombreNavegacionWeb(me.getValue().toString());
						break;
					}
				}

				taskList.add(expedienteTC);
			}
		}		
		
		return taskList;
	}
	
	public List<ExpedienteTCWPS> aplicarAjustes(List<ExpedienteTCWPS> taskList, ConsultaServicio consulta){
        
        //numeroContrato = "", codigoRVGL = "", codigoPreEvaluador = ""
        List<ExpedienteTCWPS> listaResultante = new ArrayList<ExpedienteTCWPS>();
        String campo = null;
        for (int j = 0; j < taskList.size(); j++) {                        
        
                campo = consulta.getNumeroContrato();
              //  LOG.info("campo 1::::"+campo);
                if (!"".equals(campo) && (taskList.get(j).getNumeroContrato() == null || "".equals(taskList.get(j).getNumeroContrato()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getNumeroContrato()) ) ) )continue;
                
                campo = consulta.getCodigoRVGL();
              //  LOG.info("campo 2::::"+campo);
                if (!"".equals(campo) && (taskList.get(j).getCodigoRVGL() == null || "".equals(taskList.get(j).getCodigoRVGL()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getCodigoRVGL()) ) ) )continue;
                
                campo = consulta.getCodigoPreEvaluador();
               // LOG.info("campo 3::::"+campo);
                if (!"".equals(campo) && (taskList.get(j).getCodigoPreEvaluador() == null || "".equals(taskList.get(j).getCodigoPreEvaluador()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getCodigoPreEvaluador()) ) ) )continue;
        
                campo = consulta.getSegmento();
    			if (!"".equals(campo) && (taskList.get(j).getSegmento() == null || "".equals(taskList.get(j).getSegmento()) ||
    			!(campo.equalsIgnoreCase(taskList.get(j).getSegmento()) ) ) )continue;

    			campo = consulta.getSubProducto();
    			if (!"".equals(campo) && (taskList.get(j).getProducto().getSubProducto()== null || "".equals(taskList.get(j).getProducto().getSubProducto()) ||
    			!(campo.equalsIgnoreCase(taskList.get(j).getProducto().getSubProducto()) ) ) )continue; 
    			
                campo = consulta.getTipoOferta();
               // LOG.info("campo 4::::"+campo);
               // LOG.info("campo 4.1::::"+taskList.get(j).getIdTipoOferta());
                if (!"".equals(campo) && (taskList.get(j).getIdTipoOferta() == null || "".equals(taskList.get(j).getIdTipoOferta()) ||
                !(campo.equalsIgnoreCase(taskList.get(j).getIdTipoOferta()) ) ) )continue;
                
                listaResultante.add(taskList.get(j));
    }
        LOG.info("tamaño aplicarAjustes::::"+listaResultante.size());
        return listaResultante;
}


	public String iniciarInstanciaProceso (String nombreInstanciaProceso, ExpedienteTCWPS expedienteTC){
		try {
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;		
			LOG.info(" iniciarInstanciaProceso strJSON:::"+strJSON);
//			restBPM.crearProcessInstance(strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
			restBPM.crearProcessInstance(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			return "";
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public String iniciarInstanciaProcesoTask (String nombreInstanciaProceso, ExpedienteTCWPS expedienteTC){
		try {
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;		
			LOG.info(" iniciarInstanciaProceso strJSON:::"+strJSON);
//			restBPM.crearProcessInstance(strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
			return restBPM.crearProcessInstanceTask(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/***
	 * Actualiza los datos del BO una tarea
	 * @param tkiid
	 * @param expedienteTC
	 */
	public void enviarExpedienteTC (String tkiid, ExpedienteTCWPSWeb expedienteTCWeb){ 
		try{
			ExpedienteTCWPS expedienteTC=Convertidor.fromExpedienteTCWPSWebToExpedienteTCWPS(expedienteTCWeb);
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;
			LOG.info("enviarExpedienteTC strJSON::::"+strJSON);
			restBPM.setDataTask(tkiid,java.net.URLEncoder.encode(strJSON, "UTF-8"));
			//restBPM.completeTask(tkiid, strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public void enviarExpedienteTC (String tkiid, ExpedienteTCWPS expedienteTC){ 
		try{
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;
			LOG.info("enviarExpedienteTC strJSON::::"+strJSON);
			restBPM.setDataTask(tkiid,java.net.URLEncoder.encode(strJSON, "UTF-8"));
			//restBPM.completeTask(tkiid, strJSON.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(" ","%20"));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}	
	
	public String completarTarea (String tkiid, ExpedienteTCWPS expedienteTC){
		String cadena = "";
		try{
			String strJSON=	Convertidor.fromExpedienteTCtoJSON(expedienteTC);
			strJSON = INICIO_CADENA_JSON+strJSON+FINAL_CADENA_JSON;
			LOG.info("completarTarea strJSON::::"+strJSON);
			cadena = restBPM.completeTask(tkiid, java.net.URLEncoder.encode(strJSON, "UTF-8"));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			cadena = "";
			throw new RuntimeException(e);
		}
		return cadena;
	}
	
	/*public void completarTarea (String tkiid){
		/*try{
			restBPM.completeTask(tkiid);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}*/
	
	//Comentar para ver donde se encuentra el error
	public String transferirTarea (String usuarioOrigen, String usuarioDestino, String tkiid) {
		String response = "ERROR";
		try{
			//String strJSON = "{\"user\":\""+usuarioDestino+"\",\"taskId\":\""+tkiid+"\"}";
			//LOG.info("transferirTarea strJSON::::"+strJSON);
			//restBPM.reassignTask(strJSON.replace("\"", "%22").replace("{", "{").replace("}", "}").replace(" "," "));
			response = restBPM.assignTask(tkiid, usuarioDestino);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			//throw new RuntimeException(e);
		}
		return response;
	}
	
	public void ejecutarOperacionEspera (String codigoExp){
		try{
			String strJSON= "{\"codigo\":\""+codigoExp+"\"}";
			LOG.info("strJSON::::"+strJSON);
			restBPM.completeWaitingOperation(java.net.URLEncoder.encode(strJSON, "UTF-8"));

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public Calendar obtenerTimestampServidorProcess() {
		try {
			LOG.info("Preparandose para obtener fechaActualSistemaPROCESS");
			Date fechaActualSistema = restBPM.getDateServer();
			LOG.info("fechaActualSistema PROCESS:::"+fechaActualSistema);
			//LOG.info("fechaActualWAS =====> " + (new Date()).toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(fechaActualSistema);
			return cal;
		} catch (Exception e) {
			LOG.info("Exception obtenerTimestampServidorProcess");
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public String consultaListaTareasTC (ConsultaServicio consultaServicio){ 
		try {
			//String strJSON=	INICIO_CONSULTA_JSON+json+FINAL_CADENA_JSON;

			//restBPM.getListTaskBBVA(java.net.URLEncoder.encode(strJSON, "UTF-8"));
			String strJSON=	INICIO_CONSULTA_JSON+Convertidor.fromConsultaServicioToJSON(consultaServicio)+FINAL_CADENA_JSON;
			LOG.info("strJSON getListTaskTC:::"+strJSON);
			String data = restBPM.getListTaskTC(strJSON);
			//LOG.info("data consultaListaTareasTC::"+data);
			return data;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}	
	
	public int countConsultaListaTareasTC (String usuario){ 
		try {

			if (usuario == null || "".equals(usuario))
				return 0;

			String strJSON=	INICIO_CONSULTA_JSON_2+usuario+FINAL_CADENA_JSON_2;
			String data = restBPM.getCountListTaskTC(strJSON);
			//LOG.info("data::"+data);

			JsonElement jsonElement = new JsonParser().parse(data);
			String cantidad = jsonElement.getAsJsonObject().get(NODE_DATA).getAsJsonObject().get(NODE_DATA).getAsJsonObject()
					.get(NODE_CANTIDAD_EXP).getAsString();

			LOG.info("cantidad exp ::::"+cantidad);

			return Integer.parseInt(cantidad);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
}
