package com.pe.bbva.pyme.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserMembership;
import org.bonitasoft.engine.identity.UserMembershipCriterion;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pe.bbva.pyme.dao.ISolicitudDAO;
import com.pe.bbva.pyme.model.Solicitud;
import com.pe.bbva.pyme.utils.ConstantesEnum;
import com.pe.bbva.pyme.utils.Utils;

public class SolicitudDAOImpl extends BonitaDataAccess implements ISolicitudDAO{
	
	private static final Logger LOG = LoggerFactory.getLogger(SolicitudDAOImpl.class);
	protected int MAX_RESULT_INSTANCES;
	protected int MAX_RESULT_TASKS;
	protected int MAX_TRACE_DAYS;
	
	public SolicitudDAOImpl(Properties props) throws Exception {	
		super(props);
		try {
			MAX_RESULT_INSTANCES = Integer.parseInt(DBUtil.obtenerParametro(DBUtil.MAX_RESULT_INSTANCES));
			MAX_RESULT_TASKS = Integer.parseInt(DBUtil.obtenerParametro(DBUtil.MAX_RESULT_TASKS));
			MAX_TRACE_DAYS = Integer.parseInt(DBUtil.obtenerParametro(DBUtil.MAX_TRACE_DAYS)) * -1;
		} catch (Exception e) {
			MAX_RESULT_INSTANCES = 10000;
			MAX_RESULT_TASKS = 100;
			MAX_TRACE_DAYS = -31;
		}
	}
	
	public List<Solicitud> listarSolicitudes()	throws Exception {
		LOG.debug("...inicia listar Solicitudes");
		List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
		//1. Obtenemos el Id del proceso "Contratacion Pymes" activo.		
		Long idProcesoPrincipal = (long) 0;//getProcessDefinition().getId();
		//2. Obtenemos las Solicitudes
		//2.1 Solicitudes Pendientes
		List<ProcessInstance> listProcessInstances = obtenerListaSolicitudesPendientes(idProcesoPrincipal);		
		//2.2 Solicitudes Hechas
		List<ArchivedProcessInstance> listArchivedProcessInstances = obtenerListaSolicitudesHechas(idProcesoPrincipal);	
		//2.3 preparamos las iteraciones
		Iterator<ProcessInstance> itrSolicitudPendiente = listProcessInstances.iterator();
		Iterator<ArchivedProcessInstance> itrSolicitudHecha = listArchivedProcessInstances.iterator();		
		//3. Recorremos las Solicitudes.
		//3.1. Solicitudes Hechas
		while(itrSolicitudHecha.hasNext()) {
			ArchivedProcessInstance elementoSolicitud = (ArchivedProcessInstance) itrSolicitudHecha.next();
	    	Long idElemento = elementoSolicitud.getRootProcessInstanceId();
	    	Date fechaLlegadaAux = elementoSolicitud.getStartDate();	
	    	//3.1.1 obtenemos sus datos
	    	Solicitud solicitudAux = setearDatosGeneralesSolicitudHecha(elementoSolicitud);
	    	//3.1.2 obtenemos sus Tareas Pendientes
//	    	List<FlowNodeInstance> listaTareasPendientes = obtenerListaTareasPendientes(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	//3.1.3 obtenemos sus Tareas Hechas
	    	List<ArchivedFlowNodeInstance> listaTareasHechas = obtenerListaTareasHechas(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	//4. Recorremos las listas
	    	  Iterator<ArchivedFlowNodeInstance> itrTareaHecha = listaTareasHechas.iterator();
	    	  int index_tarea=0;
		      while(itrTareaHecha.hasNext()) {
		    	  index_tarea++;
		    	  ArchivedFlowNodeInstance elementoTareaHecha = (ArchivedFlowNodeInstance) itrTareaHecha.next();
		    	  Solicitud solicitudHecha = setearDatosTrazaTareaHecha(solicitudAux,elementoTareaHecha);
//		    	  if(!solicitudHecha.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
		    		  solicitudHecha.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		    		  listaSolicitudes.add(solicitudHecha);
		    		  if(index_tarea>2 && index_tarea<=listaTareasHechas.size()){
		    			  listaSolicitudes.get(listaSolicitudes.size()-2).setEstado(solicitudHecha.getEstado());
		    		  }
		    		  if(index_tarea==listaTareasHechas.size()){
		    			  solicitudHecha.setEstado(solicitudAux.getEstado());
		    		  }
			    	  fechaLlegadaAux = elementoTareaHecha.getArchiveDate();  
//		    	  }
		      }	
		      /*Iterator<FlowNodeInstance> itrTareaPendiente = listaTareasPendientes.iterator();
		      while(itrTareaPendiente.hasNext()) {
		    	  FlowNodeInstance elementoTareaPendiente = (FlowNodeInstance) itrTareaPendiente.next();
		    	  Solicitud solicitudPendiente = setearDatosTareaPendiente(solicitudAux,elementoTareaPendiente);
		    	  if(!solicitudPendiente.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
		    		  solicitudPendiente.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));		    	  
			    	  listaSolicitudes.add(solicitudPendiente);
			    	  fechaLlegadaAux = null;  
		    	  }
		      }*/	
		}
		//3.2 Solicitudes Pendientes
		while(itrSolicitudPendiente.hasNext()) {
			ProcessInstance elementoSolicitud = (ProcessInstance) itrSolicitudPendiente.next();
	    	Long idElemento = elementoSolicitud.getRootProcessInstanceId();
	    	Date fechaLlegadaAux = elementoSolicitud.getStartDate();
	    	//3.2.1 obtenemos sus datos
	    	Solicitud solicitudAux = setearDatosGeneralesSolicitudPendiente(elementoSolicitud);
	    	//List<FlowNodeInstance> listaTareasPendientes = obtenerListaTareasPendientes(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	List<ArchivedFlowNodeInstance> listaTareasHechas = obtenerListaTareasHechas(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	//4. Recorremos las listas
	    	  Iterator<ArchivedFlowNodeInstance> itrTareaHecha = listaTareasHechas.iterator();
	    	  int index_tarea=0;
		      while(itrTareaHecha.hasNext()) {
		    	  index_tarea++;
		    	  ArchivedFlowNodeInstance elementoTareaHecha = (ArchivedFlowNodeInstance) itrTareaHecha.next();
		    	  //Solicitud solicitudHecha = setearDatosTareaHecha(solicitudAux,elementoTareaHecha);
		    	  Solicitud solicitudHecha = setearDatosTrazaTareaHecha(solicitudAux,elementoTareaHecha);
//		    	  if(!solicitudHecha.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
		    		  solicitudHecha.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
			    	  listaSolicitudes.add(solicitudHecha);
			    	  if(index_tarea>2 && index_tarea<=listaTareasHechas.size()){
		    			  listaSolicitudes.get(listaSolicitudes.size()-2).setEstado(solicitudHecha.getEstado());
		    		  }
			    	  if(index_tarea==listaTareasHechas.size()){
		    			  solicitudHecha.setEstado(solicitudAux.getEstado());
		    		  }
			    	  fechaLlegadaAux = elementoTareaHecha.getArchiveDate();  
//		    	  }
		      }	
		      /*Iterator<FlowNodeInstance> itrTareaPendiente = listaTareasPendientes.iterator();
		      while(itrTareaPendiente.hasNext()) {
		    	  FlowNodeInstance elementoTareaPendiente = (FlowNodeInstance) itrTareaPendiente.next();
		    	  Solicitud solicitudPendiente = setearDatosTrazaTareaPendiente(solicitudAux,elementoTareaPendiente);
		    	  if(!solicitudPendiente.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
		    		  solicitudPendiente.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));		    	  
			    	  listaSolicitudes.add(solicitudPendiente);
			    	  fechaLlegadaAux = null;  
		    	  }
		      }*/
		}
	    return listaSolicitudes;
	}
	
	private List<FlowNodeInstance> obtenerListaTareasPendientes(Long idProcesoPrincipal,Long idElemento) throws SearchException{
		List<FlowNodeInstance> listaTareasPendientes = null;
		SearchOptionsBuilder builder = new SearchOptionsBuilder(0,MAX_RESULT_TASKS);
		builder.filter(FlowNodeInstanceSearchDescriptor.PROCESS_DEFINITION_ID, idProcesoPrincipal);
		builder.filter(FlowNodeInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID,idElemento);
		final SearchResult<FlowNodeInstance> processFlowNodeInstance = getProcessAPI().searchFlowNodeInstances(builder.done());
		 listaTareasPendientes = processFlowNodeInstance.getResult();
		return listaTareasPendientes;
	}
	
	private List<ArchivedFlowNodeInstance> obtenerListaTareasHechas(Long idProcesoPrincipal,Long idElemento) throws SearchException{
		List<ArchivedFlowNodeInstance> listaTareasHechas = null;
		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, MAX_RESULT_TASKS);
		builder.filter(ArchivedFlowNodeInstanceSearchDescriptor.PROCESS_DEFINITION_ID, idProcesoPrincipal);
		builder.filter(ArchivedFlowNodeInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID,idElemento);
		builder.filter(ArchivedFlowNodeInstanceSearchDescriptor.STATE_NAME,ConstantesEnum.FILTRO_ESTADO_COMPLETADO.getNombre());
		builder.differentFrom(ArchivedFlowNodeInstanceSearchDescriptor.FLOW_NODE_TYPE, ConstantesEnum.FILTRO_TIPO_NODO_GATE.getNombre());
		builder.differentFrom(ArchivedFlowNodeInstanceSearchDescriptor.NAME, "Consultar Cliente");
		final SearchResult<ArchivedFlowNodeInstance> processArchivedFlowNodeInstance = getProcessAPI().searchArchivedFlowNodeInstances(builder.done());
		listaTareasHechas = processArchivedFlowNodeInstance.getResult();
		return listaTareasHechas;
	}
	
	private List<ProcessInstance> obtenerListaSolicitudesPendientes(Long idProcesoPrincipal) throws SearchException{
		List<ProcessInstance> listProcessInstances = null;
		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, MAX_RESULT_INSTANCES);
		//builder.filter(ProcessInstanceSearchDescriptor.PROCESS_DEFINITION_ID, idProcesoPrincipal);
		builder.filter(ProcessInstanceSearchDescriptor.NAME, ConstantesEnum.PROCESS_NAME.getNombre());
		Date startDate=Utils.addDaysTodate(new Date(), MAX_TRACE_DAYS);
		builder.greaterOrEquals(ProcessInstanceSearchDescriptor.START_DATE, startDate.getTime());
		final SearchResult<ProcessInstance> processInstanceResults = getProcessAPI().searchOpenProcessInstances(builder.done());
		listProcessInstances = processInstanceResults.getResult();		
		return listProcessInstances;
	}
	
	private List<ArchivedProcessInstance> obtenerListaSolicitudesHechas(Long idProcesoPrincipal) throws SearchException{
		List<ArchivedProcessInstance> listArchivedProcessInstances = null;
		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, MAX_RESULT_INSTANCES);
		//builder.filter(ProcessInstanceSearchDescriptor.PROCESS_DEFINITION_ID, idProcesoPrincipal);
		builder.filter(ProcessInstanceSearchDescriptor.NAME, ConstantesEnum.PROCESS_NAME.getNombre());
		Date startDate=Utils.addDaysTodate(new Date(), MAX_TRACE_DAYS);
		builder.greaterOrEquals(ProcessInstanceSearchDescriptor.START_DATE, startDate.getTime());
		final SearchResult<ArchivedProcessInstance> archivedProcessInstanceResults = getProcessAPI().searchArchivedProcessInstances(builder.done());
		listArchivedProcessInstances = archivedProcessInstanceResults.getResult();
		return listArchivedProcessInstances;
	}
	
	private Solicitud setearDatosTrazaTareaPendiente(Solicitud solicitudAux,FlowNodeInstance elementoTareaPendiente) throws Exception{
		Solicitud solicitudPendiente = new Solicitud(solicitudAux);
		solicitudPendiente.setNombreTarea(elementoTareaPendiente.getName());
//		solicitudPendiente.setEstado(Utils.traducirEstadoSolicitud(elementoTareaPendiente.getState()));	
		solicitudPendiente.setUsuarioEjecutorTarea(ConstantesEnum.CADENA_VACIA.getNombre());
		solicitudPendiente.setRolEjecutorTarea(ConstantesEnum.CADENA_VACIA.getNombre());
		if(elementoTareaPendiente.getExecutedBy()>0){
			User userTareaPendiente = getIdentityAPI().getUser(elementoTareaPendiente.getExecutedBy());
			UserMembership rolUserTareaPendiente = getIdentityAPI().getUserMembership(elementoTareaPendiente.getExecutedBy());
			solicitudPendiente.setUsuarioEjecutorTarea(userTareaPendiente.getUserName());
			solicitudPendiente.setRolEjecutorTarea(rolUserTareaPendiente.getRoleName());
		}
		
		List<DataInstance> datosTarePendiente = getProcessAPI().getActivityDataInstances(elementoTareaPendiente.getId(), 1, 1000);
		Iterator<DataInstance> itrDataTarea = datosTarePendiente.iterator();
		while(itrDataTarea.hasNext()) {
			DataInstance elementDataCaso = (DataInstance) itrDataTarea.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			solicitudAux = setearDetallesTarea(solicitudAux, nombre, valor);
		}
		
		
		return solicitudPendiente;
	}	
	
	private Solicitud setearDatosTrazaTareaHecha(Solicitud solicitud_base,ArchivedFlowNodeInstance elementoTareaHecha) throws Exception{
		Solicitud solicitudHecha = new Solicitud(solicitud_base);
		solicitudHecha.setNombreTarea(elementoTareaHecha.getName());    	  
		solicitudHecha.setFechaEnvio(Utils.convertirDateEnCadena(elementoTareaHecha.getArchiveDate(),ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		solicitudHecha.setUsuarioEjecutorTarea(ConstantesEnum.USUARIO_SISTEMA_NOMBRE.getNombre());
		solicitudHecha.setRolEjecutorTarea(ConstantesEnum.USUARIO_SISTEMA_ROL.getNombre()); 
		if(elementoTareaHecha.getExecutedBy()>0){
			User userTareaHecha = getIdentityAPI().getUser(elementoTareaHecha.getExecutedBy());
			try{
				List<UserMembership> lst_membership=getIdentityAPI().getUserMemberships(elementoTareaHecha.getExecutedBy(), 0, 1, UserMembershipCriterion.ASSIGNED_DATE_ASC);
				UserMembership rolUserTareaHecha=	lst_membership.get(0);
				solicitudHecha.setRolEjecutorTarea(rolUserTareaHecha.getRoleName());
			}catch(Exception e){
				solicitudHecha.setRolEjecutorTarea("");
			}
			solicitudHecha.setUsuarioEjecutorTarea(userTareaHecha.getUserName());
		}
	
//		solicitudHecha.setFechaLlegada(Utils.convertirDateEnCadena(elementoTareaHecha., ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		List<ArchivedDataInstance> datosTareaArchivada = getProcessAPI().getArchivedActivityDataInstances(elementoTareaHecha.getSourceObjectId(), 1, 1000);
		Iterator<ArchivedDataInstance> itrDataTarea = datosTareaArchivada.iterator();
		while(itrDataTarea.hasNext()) {
			ArchivedDataInstance elementDataCaso = (ArchivedDataInstance) itrDataTarea.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			solicitudHecha = setearDetallesTarea(solicitudHecha, nombre, valor);	
		}			
		return solicitudHecha;
	}
	
	private Solicitud setearDatosGeneralesSolicitudHecha(ArchivedProcessInstance elementoSolicitud){
		Solicitud solicitudAux = new Solicitud();
		solicitudAux.setNroSolicitud(String.valueOf(elementoSolicitud.getRootProcessInstanceId()));
		solicitudAux.setFechaLlegada(Utils.convertirDateEnCadena(elementoSolicitud.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		List<ArchivedDataInstance> datosArchivadosAdicionales = getProcessAPI().getArchivedProcessDataInstances(elementoSolicitud.getRootProcessInstanceId(), 1, 1000);
		Iterator<ArchivedDataInstance> itrDataCaso = datosArchivadosAdicionales.iterator();
		while(itrDataCaso.hasNext()) {
			ArchivedDataInstance elementDataCaso = (ArchivedDataInstance) itrDataCaso.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			solicitudAux = setearDetalleSolicitud(solicitudAux, nombre, valor);
		}	
		return solicitudAux;
	}
	
	private Solicitud setearDatosGeneralesSolicitudPendiente(ProcessInstance elementoSolicitud){
		Solicitud solicitudAux = new Solicitud();
		solicitudAux.setNroSolicitud(String.valueOf(elementoSolicitud.getRootProcessInstanceId()));
		solicitudAux.setFechaLlegada(Utils.convertirDateEnCadena(elementoSolicitud.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		List<DataInstance> datosPendientesAdicionales = getProcessAPI().getProcessDataInstances(elementoSolicitud.getRootProcessInstanceId(), 1, 1000);
		Iterator<DataInstance> itrDataCaso = datosPendientesAdicionales.iterator();
		while(itrDataCaso.hasNext()) {
			DataInstance elementDataCaso = (DataInstance) itrDataCaso.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			solicitudAux = setearDetalleSolicitud(solicitudAux, nombre, valor);
		}	
		return solicitudAux;
	}
	
	private Solicitud setearDetalleSolicitud(Solicitud solicitudAux,String nombre,String valor){
		Solicitud solicitud = new Solicitud(solicitudAux);
		switch (nombre) {
		case "tipo_doi_cliente": solicitud.setTipoDOICliente(valor);
		break;
		case "num_doi_cliente": solicitud.setNroDOICliente(valor);
		break;
		case "nombre_cliente": solicitud.setNombreCliente(valor);
		break;
		case "estado_solicitud": solicitud.setEstado(valor);
		break;
		case "oferta_aprobada": solicitud.setTipoOferta(valor);
		break;
		case "ofi_registro": solicitud.setOficinaSolicitud(valor);
		break;
		case "num_rvgl": solicitud.setNroRVGL(valor);
		break;
//		case "num_contrato": solicitud.setNroContrato(valor);
//		break;
//		case "num_garantia": solicitud.setNroGarantia(valor);
//		break;
//		case "dictamen": solicitud.setDictamen(valor);
//		break;
		case "producto": solicitud.setProducto(valor);
		break;
		case "campania": solicitud.setCampania(valor);
		break;
//		case "causal_clte_rechaza": solicitud.setCausal_clte_cancela(valor);
//		break;
//		case "causal_devolucion_gmc": solicitud.setCausal_devol_gmc(valor);
//		break;
		case "clte_clasificacion": solicitud.setClasificacion_clte(valor);
		break;
//		case "moneda": solicitud.setMoneda(valor);
//		break;
//		case "monto": solicitud.setMonto(valor);
//		break;
//		case "plazo": solicitud.setPlazo(valor);
//		break;
//		case "tasa": solicitud.setTasa(valor);
//		break;
		case "num_tramite": solicitud.setNum_preimpreso(valor);
		break;
		case "usu_registrante": solicitud.setAbn_registante(valor);
		break;
		default: 
			break;
		}			
		return solicitud;
	}
	
	private Solicitud setearDetallesTarea(Solicitud solicitudAux,String nombre,String valor){
		Solicitud solicitud = new Solicitud(solicitudAux);
		switch (nombre) {
		case "aux_estado_solicitud": solicitud.setEstado(valor);
		break;
		case "aux_tipo_oferta": solicitud.setTipoOferta(valor);
		break;
		case "aux_num_rvgl": solicitud.setNroRVGL(valor);
		break;
		case "aux_num_contrato": solicitud.setNroContrato(valor);
		break;
		case "aux_num_garantia": solicitud.setNroGarantia(valor);
		break;
		case "aux_dictamen": solicitud.setDictamen(valor);
		break;
		case "aux_producto": solicitud.setProducto(valor);
		break;
		case "aux_campania": solicitud.setCampania(valor);
		break;
		case "aux_causal_clte_rechaza": solicitud.setCausal_clte_cancela(valor);
		break;
		case "aux_causal_devolucion_gmc": solicitud.setCausal_devol_gmc(valor);
		break;
		case "aux_clte_clasificacion": solicitud.setClasificacion_clte(valor);
		break;
		case "aux_moneda": solicitud.setMoneda(valor);
		break;
		case "aux_monto": solicitud.setMonto(valor);
		break;
		case "aux_plazo": solicitud.setPlazo(valor);
		break;
		case "aux_tasa": solicitud.setTasa(valor);
		break;
		default: 
			break;
		}			
		return solicitud;
	}
}
